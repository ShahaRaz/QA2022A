package pkg1;// Generated by Selenium IDE

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MacbookTest {
    public interface KEYS {
        final int DELAY_LONG = 10;
        final int DELAY_MID = 3;
        final int DELAY_SHORT = 1;
        final String OLD_PASSWORD = "NewPassword1234567";
        final String NEW_PASSWORD = "NewPassword1234567";

    }

    private WebDriver driver;
    private Map<String, Object> vars;
    private ReadExcl objExcelFile;
    private JavascriptExecutor js;
    private final Logger logger = LoggerFactory.getLogger(MacbookTest.class);


    @BeforeAll
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", HelloTest.KEYS.CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
        objExcelFile = new ReadExcl();
        try {
            objExcelFile.readExcel("C:\\Users\\sagiz\\IdeaProjects\\HW2\\references", "Search queries.xls", "sheet1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public void tearDown() {
        goodNight(17);
        driver.quit();
    }

    private void goodNight(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void testOpenWebpage() {
        // Step # | name | target | value
        // 1 | open | /demo/index.php?route=common/home |
        driver.get("http://tutorialsninja.com/demo/index.php?route=common/home");
        // 2 | setWindowSize | 1760x898 |
        driver.manage().window().setSize(new Dimension(1760, 898));
    }


    @Test
    @Order(3)
    public void testAddProductsFromExcel() {
        // 3 | click | name=search |
        driver.findElement(By.name("search")).click();
        // 4 | type | name=search | macbook air

        // add products in to cart (described from excel)
        int rowCount = objExcelFile.getRowcount();
        Sheet thsSheet = objExcelFile.getsheet();

        for (int i = 1; i < rowCount + 1; i++) {

            Row row = thsSheet.getRow(i);

            //Create a loop to print cell values in a row
            if (row.getCell(0).getBooleanCellValue()) {
                logger.info(String.format("Ignoring '%s'", row.getCell(1).getStringCellValue()));
                continue;
            }
            logger.info(String.format("Searching for '%s'", row.getCell(1).getStringCellValue()));

            driver.findElement(By.name("search")).clear();
            driver.findElement(By.name("search")).sendKeys(row.getCell(1).getStringCellValue());
            // 5 | click | css=.btn-default |
            driver.findElement(By.cssSelector("#search > span > button")).click();
            try {
                WebElement addToCart = driver.findElement(By.cssSelector("#content > div:nth-child(8) > div:nth-child(1) > div > div:nth-child(2) > div.button-group > button:nth-child(1)"));
//                logger.info(String.format("Buying %f '%s'(s)", row.getCell(2).getNumericCellValue(), row.getCell(1).getStringCellValue()));
                System.out.println("row = " + i + ", rowCount = " + rowCount + "DATAAA?? ::: SAGI ::: " + row.getCell(2).getNumericCellValue());
                for (int clicks = 0; clicks < row.getCell(2).getNumericCellValue(); clicks++)
                    addToCart.click();
            } catch (NoSuchElementException e) {
                logger.warn(String.format("The item '%s' doesn't exist", row.getCell(1).getStringCellValue()));
            }
        }
    }

    @Test
    @Order(4)
    public void testRemoveSingleProduct() {
        logger.info("Subtracting quantity of the first product from the list");
        // 1. open cart
        driver.findElement(By.cssSelector("#top-links > ul > li:nth-child(4) > a > span")).click();

        WebElement amount_wgt = driver.findElement(By.cssSelector("#content > form > div > table > tbody > tr:nth-child(1) > td:nth-child(4) > div > input"));
        int currentAmount = Integer.parseInt(amount_wgt.getAttribute("value"));

        // 3. while(amount > 2) remove single object
        while (currentAmount > 2) {
            amount_wgt.clear();
            amount_wgt.sendKeys("" + (--currentAmount));
            amount_wgt.sendKeys(Keys.ENTER);
            goodNight(1);
            amount_wgt = driver.findElement(By.cssSelector("#content > form > div > table > tbody > tr:nth-child(1) > td:nth-child(4) > div > input"));
        }

        logger.info("Removing the first product from the list");

        // 4. Delete Remaining 2
        driver.findElement(By.cssSelector("#content > form > div > table > tbody > tr:nth-child(1) > td:nth-child(4) > div > span > button.btn.btn-danger")).click();

    }

    private String tryLogingIn(String email, String password) {
        logger.debug("Trying to log in with credentials {}, {}", email, password);
        // Email
        WebElement myDynamicElement =
                (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("input-email")));
        myDynamicElement.clear();
        myDynamicElement.sendKeys(email); // DontChangeMe (Shipping details saved for this account)

        // password
        driver.findElement(By.id("input-password")).clear();
        driver.findElement(By.id("input-password")).sendKeys(password);

        // press login
        driver.findElement(By.id("button-login")).click();
        goodNight(1);

        try {
            logger.debug("Trying to get the error message if the login failed");
            WebElement amount_wgt = driver.findElement(By.cssSelector("#collapse-checkout-option > div > div.alert.alert-danger.alert-dismissible"));
            return amount_wgt.getText();
        } catch (Exception e) {
            logger.debug("Login succeed (no warning label)");
            return "success";
        }
    }

    @Test
    @Order(5)
    public void testLogin() {
        logger.info("Testing login with different credentials");

        // Go into shopping cart
        driver.findElement(By.cssSelector("#top-links > ul > li:nth-child(4) > a > span")).click();
        //     Checkout
        driver.findElement(By.cssSelector("#content > div.buttons.clearfix > div.pull-right > a")).click();

        String resultBadPassword = tryLogingIn("asa.b123@gmail.com", "1234");
        goodNight(KEYS.DELAY_MID); //  Warning: No match for E-Mail Address and/or Password.
        String resultBadEmail = tryLogingIn("asa.b1233412f@gmail.com", KEYS.OLD_PASSWORD);
        goodNight(KEYS.DELAY_MID); //  Warning: No match for E-Mail Address and/or Password.
        String resultEmailNotInserted = tryLogingIn("", "Hey");
        goodNight(KEYS.DELAY_MID); //  Warning: No match for E-Mail Address and/or Password.
        String resultSuccess = tryLogingIn("asa.b123@gmail.com", KEYS.OLD_PASSWORD);

        logger.info("Last login result: {}", resultSuccess);
    }

    @Test
    @Order(6)
    public void testChangePassword() {
        logger.info("Testing changing passsssssssword");

        // Going to account's settings
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown > a")).click();
        goodNight(KEYS.DELAY_MID);
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown.open > ul > li:nth-child(1) > a")).click();
        goodNight(KEYS.DELAY_SHORT);
        driver.findElement(By.cssSelector("#column-right > div > a:nth-child(3)")).click();
        goodNight(KEYS.DELAY_SHORT);

        //insert NEW password
        driver.findElement(By.id("input-password")).sendKeys(KEYS.NEW_PASSWORD);
        driver.findElement(By.id("input-confirm")).sendKeys(KEYS.NEW_PASSWORD);
        driver.findElement(By.cssSelector("#content > form > div > div.pull-right > input")).click();
        logger.info("Changed password successfully");
        // logout
        logger.info("Logging out of the system");
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown > a")).click();
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown.open > ul > li:nth-child(5) > a")).click();

        goodNight(KEYS.DELAY_MID);
        logger.info("Logging back in with the new (and improved) password");
        // Going to account's settings
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown > a")).click();
        goodNight(KEYS.DELAY_MID);
        driver.findElement(By.cssSelector("#top-links > ul > li.dropdown.open > ul > li:nth-child(2) > a")).click();

        goodNight(KEYS.DELAY_MID);
        String resultSuccess = tryLogingIn("asa.b123@gmail.com", KEYS.NEW_PASSWORD);
        logger.info("Last login result: {}", resultSuccess);

    }

    @Test
    @Order(8)
    public void testCheckoutCart() {
        logger.info("Going to shopping cart");
        // Go into shopping cart
        driver.findElement(By.cssSelector("#top-links > ul > li:nth-child(4) > a")).click();
        logger.info("Going to checkout");
        //     Checkout
        driver.findElement(By.cssSelector("#content > div.buttons.clearfix > div.pull-right > a")).click();
        fillDetailsWithRandomInformationForCheckoutOrder();
    }

////    @Test
//    public void testFillShippingDetails(){
//        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("input-payment-firstname"))).sendKeys("Asa");
//        driver.findElement(By.id("input-payment-lastname")).sendKeys("Basa");
//        driver.findElement(By.id("input-payment-address-1")).sendKeys("Something classified");
//        driver.findElement(By.id("input-payment-city")).sendKeys("Brazil");
//        driver.findElement(By.id("input-payment-postcode")).sendKeys("1010220");
//            new Select(driver.findElement(By.id("input-payment-country"))).selectByVisibleText("Israel");
//        new Select(driver.findElement(By.id("input-payment-zone"))).selectByVisibleText("Tel Aviv");
//    }

    private void fillDetailsWithRandomInformationForCheckoutOrder() {
        logger.info("Pressing continue billion times");

        goodNight(KEYS.DELAY_SHORT);
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("button-payment-address"))).click();
        goodNight(KEYS.DELAY_SHORT);

        (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("button-shipping-address"))).click();
        goodNight(KEYS.DELAY_SHORT);

        goodNight(4);
        (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.id("button-shipping-method"))).click();
        goodNight(KEYS.DELAY_SHORT);

        goodNight(KEYS.DELAY_SHORT);
        driver.findElement(By.cssSelector("#collapse-payment-method > div > div.buttons > div > input[type=checkbox]:nth-child(2)")).click();

        (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.id("button-payment-method"))).click();

        goodNight(1);
        (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.id("button-confirm"))).click();

    }


}