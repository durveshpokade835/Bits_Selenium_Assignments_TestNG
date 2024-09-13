package assignment3;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class FileUploadAndDownload {
	WebDriver driver;
	JavascriptExecutor js;
	WebDriverWait wait;
	ChromeOptions options;
	File downloadedFile;

	// ExtentReports objects
	ExtentReports extent;
	ExtentTest test;
	ExtentSparkReporter spark;

	@BeforeMethod
	public void setUp() {
		// Initialize ExtentReports
		spark = new ExtentSparkReporter("target/FileUploadAndDownloadReport.html");
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("MyReport");
		
		extent = new ExtentReports();
		extent.attachReporter(spark);
		
		test = extent.createTest("File Upload and Download Test");

		System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
		// Setup Chrome options
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", "C:\\Users\\gaura\\OneDrive\\Documents\\Trash");
		
		 // Disable the "Save As" prompt for file downloads
	    prefs.put("download.prompt_for_download", false);
	    prefs.put("download.directory_upgrade", true);
	    
	 // Auto-open certain file types like PDFs and Excel files without prompt
	    prefs.put("plugins.always_open_pdf_externally", true);  // Automatically open PDF files externally
	    prefs.put("safebrowsing.enabled", true);  // Enable safe browsing for automatic file handling

		options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);

		driver = new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();

		// Accessing the website
		driver.get("https://letcode.in/file");
		test.info("Navigated to the file upload/download page.");

	}

	@Test
	public void FileUpload() throws IOException {
		try {
			test.info("Starting file upload test.");
			String filePath = "D:\\BITS new Workspace\\BITS_Assignments\\src\\assignment3\\randomFile";
			WebElement uploadEle = driver.findElement(By.xpath("//input[@name='resume']"));
			uploadEle.sendKeys(filePath);
			test.pass("File uploaded successfully: " + filePath);
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		} catch (Exception e) {
			test.fail("File upload test failed: " + e.getMessage());
			test.addScreenCaptureFromPath(captureScreenshot(driver));
			e.printStackTrace();
			throw new RuntimeException("File upload test failed", e);
		}

	}

	@Test
	public void FileDownload() throws IOException {
		try {
			test.info("Starting file download test for sample.txt.");
			// Click the download link or button
			driver.findElement(By.id("txt")).click();

			// Define the file path and wait for the download to complete
			downloadedFile = new File("C:\\Users\\gaura\\OneDrive\\Documents\\Trash\\sample.txt");

			// Explicit wait for the file to be downloaded
			wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return downloadedFile.exists() && downloadedFile.length() > 0;
				}
			});

			// Verify if the file exists
			Assert.assertTrue(downloadedFile.exists());
			test.pass("File sample.txt downloaded successfully.");
			test.addScreenCaptureFromPath(captureScreenshot(driver));
		} catch (Exception e) {
			// Handle exceptions and print the stack trace
			test.fail("File download test failed: " + e.getMessage());
			test.addScreenCaptureFromPath(captureScreenshot(driver));
			e.printStackTrace();
			throw new RuntimeException("File download test failed", e);

		} finally {
			// Clean up if necessary (e.g., delete downloaded files)
			if (downloadedFile.exists()) {
				downloadedFile.delete();
				test.info("Downloaded file deleted after test.");
			}
		}

	}

	@Test
	public void FileDownload2() throws IOException {
		try {
			 test.info("Starting multiple file download test.");
			// Array of file names and their corresponding download button IDs or locators
			WebElement downloadFiles;
			String[] fileNames = { "sample.xlsx", "sample.pdf", "sample.txt" };
			String[] buttonIds = { "xls", "pdf", "txt" };
			String downloadPath = "C:\\Users\\gaura\\OneDrive\\Documents\\Trash\\";

			// Loop to download and validate each file
			for (int i = 0; i < fileNames.length; i++) {
				// Click the download link or button for each file (modify selector if
				// necessary)
				downloadFiles = driver.findElement(By.id(buttonIds[i]));
				js.executeScript("arguments[0].scrollIntoView(true);", downloadFiles);
				wait.until(ExpectedConditions.elementToBeClickable(downloadFiles));

				try {
//					downloadFiles.click();
					js.executeScript("arguments[0].click();", downloadFiles);
					test.info("Clicked download button for: " + fileNames[i]);
				} catch (org.openqa.selenium.ElementClickInterceptedException e) {
					js.executeScript("arguments[0].click();", downloadFiles);
					test.info("Handled click interception for: " + fileNames[i]);
				}

				// Define the file path for each file
				downloadedFile = new File(downloadPath + fileNames[i]);

				// Explicit wait for the file to be downloaded
				wait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver driver) {
						return downloadedFile.exists() && true;
					}
				});

				// Verify if the file exists
				Assert.assertTrue(downloadedFile.exists(),
						"File " + fileNames[i] + " was not downloaded successfully.");
				test.pass("File " + fileNames[i] + " downloaded successfully.");
				test.addScreenCaptureFromPath(captureScreenshot(driver));
				
				System.out.println("File " + fileNames[i] + " downloaded successfully.");

				// Clean up after each file download (optional, based on your needs)
				if (downloadedFile.exists()) {
					downloadedFile.delete();
					test.info("File " + fileNames[i] + " deleted after verification.");
					
				}
			}

		} catch (Exception e) {
			// Handle exceptions and print the stack trace
			test.fail("Multiple file download test failed: " + e.getMessage());
			test.addScreenCaptureFromPath(captureScreenshot(driver));
			e.printStackTrace();
			throw new RuntimeException("File download test failed", e);

		}
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
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File destinationFilePath = new File("src/../images/screenshot"+System.currentTimeMillis()+".png");
		String absolutePathLocation = destinationFilePath.getAbsolutePath();
		
		FileUtils.copyFile(srcFile, destinationFilePath);
		
		return absolutePathLocation;
	}
}