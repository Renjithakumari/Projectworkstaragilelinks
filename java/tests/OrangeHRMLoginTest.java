package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.ExcelUtils;

import java.util.List;

public class OrangeHRMLoginTest extends BaseTest {

    public static final String VALID_USERNAME = "admin";
    public static final String VALID_PASSWORD = "admin123";

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        List<String[]> dataList = ExcelUtils.getTestData("testdata/login_data.xlsx");
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i)[0];
            data[i][1] = dataList.get(i)[1];
        }
        return data;
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) throws InterruptedException {
        test = extent.createTest("Login Test - Username: " + username);
        Thread.sleep(2000);     
        driver.findElement(By.name("username")).sendKeys(username);
        Thread.sleep(2000);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(2000);

        captureScreenshot(username);

        try {
            boolean dashboardVisible = driver.findElement(By.xpath("//h6[text()='Dashboard']")).isDisplayed();

            if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                Assert.assertTrue(dashboardVisible, "Valid login should pass");
                test.pass("Login successful");

                
                driver.findElement(By.className("oxd-userdropdown-name")).click();
                Thread.sleep(2000);
                driver.findElement(By.linkText("Logout")).click();
                Thread.sleep(2000);
            } else {
                Assert.fail("Invalid credentials should not allow login");
            }
        } catch (NoSuchElementException e) {
            if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
                test.fail("Valid user login failed");
                Assert.fail("Valid login should pass");
            } else {
                test.pass("Invalid login blocked as expected");
            }
        }
    }
}
