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
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class ECommerceAutomation {

    WebDriver driver;

    @BeforeClass
    @Description("Setup ChromeDriver and navigate to Amazon India website.")
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.amazon.in/");
    }

    @Test(priority = 1)
    @Description("Test Amazon login functionality with valid credentials.")
    public void loginTest() {
        navigateToLoginPage();
        performLogin();
        verifyLoginSuccess();
    }

    @Step("Navigate to the login page.")
    private void navigateToLoginPage() {
        driver.findElement(By.id("nav-link-accountList-nav-line-1")).click();
    }

    @Step("Perform login with environment variables.")
    private void performLogin() {
        WebElement emailField = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
        emailField.sendKeys(System.getenv("AMAZON_USERNAME"));

        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("ap_password")).sendKeys(System.getenv("AMAZON_PASSWORD"));
        driver.findElement(By.id("signInSubmit")).click();
    }

    @Step("Verify login success.")
    private void verifyLoginSuccess() {
        WebElement userGreeting = driver.findElement(By.id("nav-link-accountList-nav-line-1"));
        Assert.assertTrue(userGreeting.getText().contains("Hello"), "Login failed!");
    }

    @Test(priority = 2)
    @Description("Search for a product, add it to the cart, and capture a screenshot.")
    public void productSearchAndAddToCartTest() throws IOException {
        searchProduct("Iphone 14 128 GB");
        addToCart("Apple iPhone 14");
        takeScreenshot("cart_Screen_shot.png");
        navigateToHomePage();
    }

    @Step("Search for the product: {productName}.")
    private void searchProduct(String productName) {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys(productName + Keys.ENTER);
    }

    @Step("Add the product to the cart: {productName}.")
    private void addToCart(String productName) {
        WebElement product = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'" + productName + "')]")));
        System.out.println("Product Found: " + product.getText());

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", product);

        WebElement addToCartButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Apple iPhone 14 (128 GB) - Midnight']/following::button[@type='button'][1]")));
        addToCartButton.click();
    }

    @Step("Take a screenshot and save to: {fileName}.")
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] takeScreenshot(String fileName) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path path = Paths.get("C:/Users/LOGESH/eclipse-workspace/wrkspace/EcommerceAutomation/Screenshot/" + fileName);
        File destination = path.toFile();
        FileHandler.copy(screenshot, destination);
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Navigate back to the homepage.")
    private void navigateToHomePage() {
        driver.get("https://www.amazon.in/");
    }

    @Test(priority = 3)
    @Description("Check for broken links on the website.")
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

                    if (httpURLConnection.getResponseCode() >= 400) {
                        System.out.println("Broken Link: " + url);
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
    @Description("Teardown WebDriver instance.")
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
