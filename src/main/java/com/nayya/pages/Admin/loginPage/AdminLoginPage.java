package com.nayya.pages.Admin.loginPage;

import com.nayya.pages.Admin.dashboardPage.AdminDashboardPage;
import com.nayya.utilities.email.EmailCredentialUtility;
import com.nayya.utilities.managers.PageManager;
import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.NayyaDriver;
import com.nayya.utilities.page.PageNavigation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.TestException;

import java.util.Map;

public class AdminLoginPage implements BasePage, PageNavigation {
    private final NayyaDriver driver;
    private final EmailCredentialUtility emailCredentialUtility = new EmailCredentialUtility();

    private final String URL = "admin-console";
    private final By emailFieldLocator = By.xpath("//input[@name='email'][@type='email']");
    private final By passwordFieldLocator = By.xpath("//input[@name='password'][@type='password']");
    private final By signInButtonLocator = By.xpath("//button[@type='submit']");

    public AdminLoginPage(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.visibilityOfElementLocated(emailFieldLocator));
    }

    @Override
    public void waitForPageLoad() {
        if (!driver.pageLoadedWithRefresh()) {
            throw new TestException(getClass().getName() + " failed to load!!");
        }
    }

    @Override
    public void navigateTo() {
        driver.navigateTo(driver.getNayyaURL());
    }

    public AdminDashboardPage signInAsAnAdmin() {
        signIn(emailCredentialUtility.getAdminCredentials());

        return PageManager.getInstance().instantiateCurrentPage(AdminDashboardPage.class);
    }

    private void signIn(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        WebElement emailField = driver.wait(ExpectedConditions.visibilityOfElementLocated(emailFieldLocator));
        driver.setText(emailField, email);
        WebElement passwordField = driver.wait(ExpectedConditions.presenceOfElementLocated(passwordFieldLocator));
        driver.setText(passwordField, password);
        WebElement signInButton = driver.wait(ExpectedConditions.elementToBeClickable(signInButtonLocator));
        driver.click(signInButton);
    }
}

