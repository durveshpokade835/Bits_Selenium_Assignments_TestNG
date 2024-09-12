package assignment3;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class FileUploadAndDownload {
	WebDriver driver;
	JavascriptExecutor js;
	WebDriverWait wait;
	ChromeOptions options;
	File downloadedFile;

	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
		// Setup Chrome options
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", "C:\\Users\\gaura\\OneDrive\\Documents\\Trash");
		options = new ChromeOptions();
		options.setExperimentalOption("prefs", prefs);

		driver = new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();

		// Accessing the website
		driver.get("https://letcode.in/file");

	}

	@Test
	public void FileUpload() {
		try {
			String filePath = "D:\\BITS new Workspace\\BITS_Assignments\\src\\assignment3\\randomFile";
			WebElement uploadEle = driver.findElement(By.xpath("//input[@name='resume']"));
			uploadEle.sendKeys(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("File upload test failed", e);
		}

	}

	@Test
	public void FileDownload() {
		try {
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
		} catch (Exception e) {
			// Handle exceptions and print the stack trace
			e.printStackTrace();
			throw new RuntimeException("File download test failed", e);
		
		} finally {
			// Clean up if necessary (e.g., delete downloaded files)
			if (downloadedFile.exists()) {
				downloadedFile.delete();
			}
		}

	}
	
	@Test
	public void FileDownload2() {
	    try {
	        // Array of file names and their corresponding download button IDs or locators
	    	WebElement downloadFiles;
	        String[] fileNames = {"sample.xlsx", "sample.pdf", "sample.txt"};
	        String[] buttonIds = {"xls","pdf","txt"};
	        String downloadPath = "C:\\Users\\gaura\\OneDrive\\Documents\\Trash\\";

	        // Loop to download and validate each file
	        for (int i = 0; i < fileNames.length; i++) {
	            // Click the download link or button for each file (modify selector if necessary)
	          downloadFiles= driver.findElement(By.id(buttonIds[i]));  
	          js.executeScript("arguments[0].scrollIntoView(true);", downloadFiles);
	          wait.until(ExpectedConditions.elementToBeClickable(downloadFiles));

	          try {
	        	  downloadFiles.click();
				} catch (org.openqa.selenium.ElementClickInterceptedException e) {
					js.executeScript("arguments[0].click();", downloadFiles);
				}
	          

	            // Define the file path for each file
	            downloadedFile = new File(downloadPath + fileNames[i]);

	            // Explicit wait for the file to be downloaded
	            wait.until(new ExpectedCondition<Boolean>() {
	                public Boolean apply(WebDriver driver) {
	                    return downloadedFile.exists() && downloadedFile.length() > 0;
	                }
	            });

	            // Verify if the file exists
	            Assert.assertTrue(downloadedFile.exists(), "File " + fileNames[i] + " was not downloaded successfully.");

	            System.out.println("File " + fileNames[i] + " downloaded successfully.");

	            // Clean up after each file download (optional, based on your needs)
	            if (downloadedFile.exists()) {
	                downloadedFile.delete();
	            }
	        }

	    } catch (Exception e) {
	        // Handle exceptions and print the stack trace
	        e.printStackTrace();
	        throw new RuntimeException("File download test failed", e);
	    
	    } 
	}

	
	

	@AfterMethod
	public void tearDown() {
		// Close the browser after the test
		if (driver != null) {
			driver.quit();
		}
	}
}
