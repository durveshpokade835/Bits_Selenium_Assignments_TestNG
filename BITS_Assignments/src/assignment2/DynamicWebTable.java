package assignment2;

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class DynamicWebTable {

	WebDriver driver;
	JavascriptExecutor js;
	ExtentReports extent = new ExtentReports();
	ExtentSparkReporter spark;
	ExtentTest test;

	@BeforeMethod
	public void setUp() {
		// Initialize ExtentReports
		spark = new ExtentSparkReporter("target/DynamicWebTableReport.html");
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("MyReport");
		extent.attachReporter(spark);

		System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();

		// Accessing the website
		driver.get("https://datatables.net/");
		ExtentTest test = extent.createTest("SetUp Test", "Opening the website and setting up the browser")
				.assignAuthor("Durvesh").assignCategory("functional Test Case").assignDevice("Windows");

		test.info("Handling Dynamic Web Tables ");
	}

	@Test
	public void testExtractAndValidate() throws IOException {
		try {
			test = extent.createTest("Data Extraction And Validation",
					"Extracting the data from the selected column and verifying the selected string is present or not")
					.assignAuthor("Durvesh").assignCategory("functional Test Case").assignDevice("Windows");

			WebElement dynamicTable = driver.findElement(By.id("example"));
			js.executeScript("arguments[0].scrollIntoView();", dynamicTable);

			// Specify the table ID and column name/index
			String tableId = "example";
			int columnIndex = 0; // Column index to interact with
			String expectedValue = "Airi Satou"; // Value to validate

			// Extract and validate data from the specified column
			TableUtils.extractAndValidate(driver, tableId, columnIndex, expectedValue, test);

			test.pass("Extracted and validated data from the table successfully");
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		} catch (Exception e) {
			test.fail("Failed to extract and validate data from the table: " + e.getMessage());
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		}
	}

	@Test
	public void testSortAndVerify() throws IOException {
		try {
			test = extent.createTest("Sort and Verify Test", "Test to sort and verify data in the dynamic table")
					.assignAuthor("Durvesh").assignCategory("functional Test Case").assignDevice("Windows");

			test.info("Handling Dynamic Web Tables ");
			WebElement dynamicTable = driver.findElement(By.id("example"));
			js.executeScript("arguments[0].scrollIntoView();", dynamicTable);

			// Specify the table ID and column name/index
			String tableId = "example";
			int columnIndex = 0; // Column index to interact with
			String columnName = "Name"; // Column name for sorting

			// Sort and verify the specified column
			TableUtils.sortAndVerify(driver, tableId, columnName, columnIndex, test);
			test.pass("Sorted and verified data in the table successfully");
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		} catch (Exception e) {
			test.fail("Failed to sort and verify data in the table: " + e.getMessage());
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		}
	}

	@AfterMethod
	public void tearDown() {
		// Flush the report
		extent.flush();

		// Close the browser after the test
		if (driver != null) {
			driver.quit();
		}
	}
	
	public static String captureScreenshot(WebDriver driver) throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File destinationFilePath = new File("src/../images/screenshot"+System.currentTimeMillis()+".png");
		String absolutePathLocation = destinationFilePath.getAbsolutePath();
		
		FileUtils.copyFile(srcFile, destinationFilePath);
		
		return absolutePathLocation;
	}
	
}