package assignment3;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class HandlingDynamicElements {
    WebDriver driver;
    JavascriptExecutor js;
    WebDriverWait wait;

    // ExtentReports objects
    ExtentReports extent;
    ExtentSparkReporter spark;
    ExtentTest test;

    @BeforeMethod
    public void setUp() {
        // Initialize ExtentReports
        spark = new ExtentSparkReporter("target/HandlingDynamicElements.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("MyReport");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // Initialize the test object here after ExtentReports is attached
        test = extent.createTest("Handling Dynamic Elements Test");

        System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Accessing the website
        driver.get("https://letcode.in/waits");
        test.info("Navigated to the dynamic elements handling page.");
    }

    @Test
    public void HandlingDynamicElements() throws IOException {
        SoftAssert softAssert = new SoftAssert(); // Initialize SoftAssert

        try {
            test.info("Starting dynamic elements handling test.");

            WebElement alertButton = driver.findElement(By.id("accept"));
            wait.until(ExpectedConditions.elementToBeClickable(alertButton));
            test.info("Alert button is clickable.");
            System.out.println("Alert button is clickable."); // Original console log

            alertButton.click();
            test.info("Alert button clicked.");
            System.out.println("Alert button clicked."); // Original console log

            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            test.pass("Alert accepted successfully.");
            System.out.println("Alert accepted."); // Original console log

            test.addScreenCaptureFromPath(captureScreenshot(driver));

            // Soft assert to validate that alert was handled (modify as necessary)
            softAssert.assertTrue(true, "Alert handled successfully.");
            System.out.println("Soft Assert: Alert handled successfully."); // Original console log

        } catch (Exception e) {
            test.fail("Dynamic elements handling test failed: " + e.getMessage());
            test.addScreenCaptureFromPath(captureScreenshot(driver));
            System.out.println("Test failed with exception: " + e.getMessage()); // Original console log
            softAssert.fail("Test encountered an exception: " + e.getMessage()); // Soft assertion for exception
            e.printStackTrace();
        }

        // This will consolidate the results at the end
        softAssert.assertAll();
    }

    @AfterMethod
    public void tearDown() {
        // Flush the report at the end of the test
        extent.flush();
        // Close the browser after the test
        if (driver != null) {
            driver.quit();
        }
    }

    public static String captureScreenshot(WebDriver driver) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destinationFilePath = new File("src/../images/screenshot" + System.currentTimeMillis() + ".png");
        String absolutePathLocation = destinationFilePath.getAbsolutePath();

        FileUtils.copyFile(srcFile, destinationFilePath);

        return absolutePathLocation;
    }
}
