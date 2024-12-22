package automation;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ECommerceAutomation {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.amazon.in/");
    }

    @Test(priority = 1)
    public void loginTest() {
        driver.findElement(By.id("nav-link-accountList-nav-line-1")).click();

        WebElement emailField = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
        emailField.sendKeys(System.getenv("AMAZON_USERNAME")); // Fetch from environment variable

        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("ap_password")).sendKeys(System.getenv("AMAZON_PASSWORD")); // Fetch from environment variable
        driver.findElement(By.id("signInSubmit")).click();

        // Verify login success
        WebElement userGreeting = driver.findElement(By.id("nav-link-accountList-nav-line-1"));
        Assert.assertTrue(userGreeting.getText().contains("Hello"), "Login failed!");
    }

    @Test(priority = 2)
    public void productSearchAndAddToCartTest() throws IOException {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("Iphone 14 128 GB" + Keys.ENTER);

        WebElement product = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Apple iPhone 14')]")));
        String productName = product.getText();
        System.out.println("Product Found: " + productName);

        // Scroll to the product
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", product);

        // Add to Cart
        WebElement addToCart = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.id("a-autoid-3-announce")));
        addToCart.click();

        // Take screenshot after adding to cart
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        
        // Specify the path for the screenshot
        Path path = Paths.get("C:/Users/LOGESH/eclipse-workspace/wrkspace/EcommerceAutomation/Screenshot/cart_Screen_shot.png");
        File destination = path.toFile();

        // Save the screenshot using FileHandler
        FileHandler.copy(screenshot, destination);

        // Log message for confirmation
        System.out.println("Screenshot saved at: " + path.toString());

        // Navigate back to the homepage
        driver.navigate().back();
    }

    @Test(priority = 3)
    public void findBrokenLinks() throws IOException {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        System.out.println("Total links found: " + links.size());
        int brokenLinkCount = 0;
        int validLinkCount = 0;

        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && !url.isEmpty() && !url.contains("javascript:void(0)") && !url.contains("#")) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();

                    // Check if the response code is 400 or greater (Broken links)
                    if (httpURLConnection.getResponseCode() >= 400) {
                        brokenLinkCount++;
                    } else {
                        validLinkCount++;
                    }
                } catch (Exception e) {
                    System.out.println("Error checking link: " + url + " - " + e.getMessage());
                }
            }
        }

        System.out.println("Total Broken Links: " + brokenLinkCount);
        System.out.println("Valid Links: " + validLinkCount);
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
