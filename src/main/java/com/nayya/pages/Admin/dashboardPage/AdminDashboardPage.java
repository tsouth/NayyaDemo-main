package com.nayya.pages.Admin.dashboardPage;

import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.TestException;

public class AdminDashboardPage implements BasePage {
    private final NayyaDriver driver;

    private final By homeButtonLocator = By.cssSelector("a[href$='/employers/dashboard']");

    public AdminDashboardPage(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.visibilityOfElementLocated(homeButtonLocator));
    }

    @Override
    public void waitForPageLoad() {
        if (!driver.pageLoadedWithRefresh()) {
            throw new TestException(getClass().getName() + " failed to load!!");
        }
    }

}
