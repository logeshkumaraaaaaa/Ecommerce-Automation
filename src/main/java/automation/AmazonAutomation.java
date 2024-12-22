package automation;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.idealized.Javascript;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AmazonAutomation {

	public static void main(String[] args) throws InterruptedException {
WebDriverManager.chromedriver().setup();
		
		
		WebDriver driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait((Duration.ofSeconds(5)));
		
		driver.get
		("https://www.amazon.in/?ref=aisgw_intl_stripe_in");
		
		WebElement Login = driver.findElement(By.id("nav-link-accountList-nav-line-1"));
		Login.click();
		
		  WebElement Phonenumber = driver.findElement(By.id("ap_email"));
		  String UsernameVariable = System.getenv("AMAZON_USERNAME");
		  Phonenumber.sendKeys(UsernameVariable);
		  
		  WebElement ContinueButton = driver.findElement(By.id("continue"));
		  
		  ContinueButton.click();
		  
		  
		  WebElement Password =
		  driver.findElement(By.xpath("//input[@type='password']"));
		  
		  String PasswordVariable = System.getenv("AMAZON_PASSWORD");
		  
		  Password.sendKeys(PasswordVariable);
		  
		  
		  WebElement SignIn = driver.findElement(By.id("signInSubmit"));
		  SignIn.click();
		  
		  
			
			  WebElement Searchbox = driver.findElement(By.id("twotabsearchtextbox"));
			  Searchbox.sendKeys("Iphone14 128 GB" + Keys.ENTER);
			  
			  
			  WebElement Mobile = driver.findElement(By.xpath("//span[text()='Apple iPhone 14 (128 GB) - Midnight']"));
			  
			  
			  String text = Mobile.getText();
		 
			  System.out.println(text);
			  
			  
			  JavascriptExecutor JS = (JavascriptExecutor) driver;
			  
			  JS.executeScript("arguments[0].scrollIntoView(true);", Mobile);
			  
			  
			WebElement AddtoCart = driver.findElement(By.xpath("//span[text()='Apple iPhone 14 (128 GB) - Midnight']/following:: button [@type='button'][1]"));
			
			AddtoCart.click();
			
			
			
			WebElement  Cart = driver.findElement(By.id("nav-cart-count"));
			Cart.click();
			
			
			Thread.sleep(2000);
			
			driver.quit();
			  
				
				 	
		
	}
}




