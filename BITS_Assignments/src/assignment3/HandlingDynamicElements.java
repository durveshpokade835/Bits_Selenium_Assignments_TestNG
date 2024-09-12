package assignment3;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HandlingDynamicElements {
	WebDriver driver;
	JavascriptExecutor js;
	WebDriverWait wait;

	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();

		// Accessing the website
		driver.get("https://letcode.in/waits");
	}

	@Test
	public void HandlingDynamicElements() {

		WebElement alertButton = driver.findElement(By.id("accept"));
		wait.until(ExpectedConditions.elementToBeClickable(alertButton));
//	    	wait.until(ExpectedConditions.visibilityOf(alertButton));
//	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accept")));
//	    	
		alertButton.click();
		wait.until(ExpectedConditions.alertIsPresent());
		driver.switchTo().alert().accept();

	}

	@AfterMethod
	public void tearDown() {
		// Close the browser after the test
		if (driver != null) {
			driver.quit();
		}
	}

}
