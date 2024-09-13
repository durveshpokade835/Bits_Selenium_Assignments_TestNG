package assignment2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableUtils {

    // Extract and validate rows/columns based on a condition for a given column index
    public static void extractAndValidate(WebDriver driver, String tableId, int columnIndex, String expectedValue, ExtentTest test) {
        boolean hasNextPage = true;
        test.info("Starting the extraction and validation of the table.");
        
     // Create a SoftAssert instance
        SoftAssert softAssert = new SoftAssert();

        while (hasNextPage) {
            WebElement table = driver.findElement(By.id(tableId));
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            if (rows.size() == 0) {
//                System.out.println("No rows found in the table.");
                test.fail("No rows found in the table.");
                softAssert.fail("No rows found in the table.");
              
                break;  // Exit the loop if no rows are found
            }

            // Collect and validate column data using streams
            List<String> columnDataList = rows.stream()
                    .skip(1)  // Skip the header row
                    .map(row -> row.findElements(By.tagName("td")))  // Map each row to its columns (td elements)
                    .filter(columns -> columns.size() > columnIndex)  // Ensure that the row has enough columns
                    .map(columns -> columns.get(columnIndex).getText())  // Extract text from the specified column
                    .collect(Collectors.toList());  // Collect the column data into a list
            System.out.println(columnDataList);
            test.info("Extracted column data: " + columnDataList.toString());
            
            // Perform validation based on the expected value
            columnDataList.forEach(columnData -> {
                if (columnData.equals(expectedValue)) {
                    System.out.println("Found the expected value: " + expectedValue);
                    test.pass("Found the expected value: " + expectedValue);
                    
                } 
            });

            // Handle pagination (next button logic)
            hasNextPage = goToNextPage(driver, test);
        }
    }

    // Navigate to the next page if available
    private static boolean goToNextPage(WebDriver driver, ExtentTest test) {
        try {
            WebElement nextButton = driver.findElement(By.xpath("//button[contains(text(), '>')]"));
            if (nextButton.isDisplayed()) {
                nextButton.click();
                test.info("Navigated to the next page.");
                return true;
            }
        } catch (Exception e) {
            // No next button or pagination ended
        	test.info("No next button available or pagination ended.");
        }
        return false;
    }

    // Sort the table by clicking on a column header and verify the sorting order for a given column index
    public static void sortAndVerify(WebDriver driver, String tableId, String columnName, int columnIndex,ExtentTest test) {
        // Click the column header for sorting
        WebElement columnHeader = driver.findElement(By.xpath("//th/span[text()='" + columnName + "']"));
        if (columnName != "Name") {
            columnHeader.click();
        }
        test.info("Sorting table by the column: " + columnName);
        
        List<String> allColumnData = new ArrayList<>();
        boolean hasNextPage = true;

        while (hasNextPage) {
            WebElement table = driver.findElement(By.id(tableId));
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            if (rows.size() == 0) {
                System.out.println("No rows found in the table.");
                test.fail("No rows found in the table.");
                break;  // Exit the loop if no rows are found
            }

            // Collect data from the specified column using Streams
            allColumnData = rows.stream()
                    .skip(1)  // Skipping the header row
                    .map(row -> row.findElements(By.tagName("td")))  // Get the columns (td elements) for each row
                    .filter(columns -> columns.size() > columnIndex)  // Filter rows with fewer columns than expected
                    .map(columns -> columns.get(columnIndex).getText())  // Extract text from the specified column
                    .collect(Collectors.toList());  // Collect the column data into a list
            System.out.println(allColumnData);
            test.info("Extracted sorted column data: " + allColumnData.toString());

            // Handle pagination (next button logic)
            hasNextPage = goToNextPage(driver,test);
        }

        // Verify sorting using streams and assertions
        verifySorting(allColumnData,test);
    }

    // Verify sorting of column data using streams and assertions
    private static void verifySorting(List<String> columnData, ExtentTest test) {
        // Create a sorted list using streams
        List<String> sortedData = columnData.stream()
                .sorted()
                .collect(Collectors.toList());

        // Assert that the original list is sorted
        try {
        assertEquals(sortedData, columnData, "The column is not sorted correctly.");
        System.out.println("The column is sorted correctly.");
        test.pass("The column is sorted correctly.");
        }catch (AssertionError e) {
            test.fail("The column is not sorted correctly. " + e.getMessage());
        }
    }
}