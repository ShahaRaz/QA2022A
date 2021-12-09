package pkg1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SearchGoogleFromExl {

    private WebDriver driver;
    private Map<String, Object> vars;
    private ReadExcl objExcelFile;
    JavascriptExecutor js;


    @Before
    public void setUp() throws IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\sagiz\\Downloads\\איכות תוכנה\\selenium\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();


        objExcelFile = new ReadExcl();
        objExcelFile.readExcel("C:\\Users\\sagiz\\Downloads\\איכות תוכנה\\excel files", "Search queries.xls", "sheet1");

        driver.get("https://www.google.com/");
        driver.manage().window().setSize(new Dimension(1004, 724));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void simple() {
        Logger logger = LogManager.getLogger(SearchGoogleFromExl.class);

        int rowCount = objExcelFile.getRowcount();
        Sheet thsSheet = objExcelFile.getsheet();

        for (int i = 0; i < rowCount + 1; i++) {

            Row row = thsSheet.getRow(i);
            logger.info("INFFOOOOOOOOOO " + row.toString());

            //Create a loop to print cell values in a row

            for (int j = 0; j < row.getLastCellNum(); j++) {
                logger.debug("DEBUGGGG " + row.toString());

                //Print Excel data in console

                System.out.print(row.getCell(j).getStringCellValue() + " ** ");

                driver.findElement(By.name("q")).sendKeys(row.getCell(j).getStringCellValue());
                driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
                logger.error("ERRRRRR " + row.toString());
            }
        }
    }


    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        org.junit.runner.Result result = junit.run(SearchGoogleFromExl.class); // Replace "SampleTest" with the name of your class
        if (result.getFailureCount() > 0) {
            System.out.println("Test failed.");
            System.exit(1);
        } else {
            System.out.println("Test finished successfully.");
            System.exit(0);
        }
    }

}