package assignment2;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DynamicWebTable {

    WebDriver driver;
    JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:\\Bits New Trainning Automation\\Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();

        // Accessing the website
        driver.get("https://datatables.net/");
    }

    @Test
    public void testExtractAndValidate() {
        WebElement dynamicTable = driver.findElement(By.id("example"));
        js.executeScript("arguments[0].scrollIntoView();", dynamicTable);

        // Specify the table ID and column name/index
        String tableId = "example";
        int columnIndex = 0; // Column index to interact with
        String expectedValue = "Airi Satou"; // Value to validate

        // Extract and validate data from the specified column
        TableUtils.extractAndValidate(driver, tableId, columnIndex, expectedValue);
    }

    @Test
    public void testSortAndVerify() {
        WebElement dynamicTable = driver.findElement(By.id("example"));
        js.executeScript("arguments[0].scrollIntoView();", dynamicTable);

        // Specify the table ID and column name/index
        String tableId = "example";
        int columnIndex = 0; // Column index to interact with
        String columnName = "Name"; // Column name for sorting

        // Sort and verify the specified column
        TableUtils.sortAndVerify(driver, tableId, columnName, columnIndex);
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser after the test
        if (driver != null) {
            driver.quit();
        }
    }
}
