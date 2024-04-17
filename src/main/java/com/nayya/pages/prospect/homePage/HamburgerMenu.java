package com.nayya.pages.prospect.homePage;

import com.nayya.pages.prospect.getDemoPage.DemoRequestForm;
import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.TestException;

public class HamburgerMenu implements BasePage {
    private final NayyaDriver driver;

    private final By getDemoButtonLocator = By.xpath("//button[@type='button'][@href^='/get-demo']");

    public HamburgerMenu(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.presenceOfElementLocated(getDemoButtonLocator));
    }

    @Override
    public void waitForPageLoad() {
        if (!driver.pageLoaded()) {
            throw new TestException(getClass().getName() + " failed to load!!");
        }
    }

    public DemoRequestForm getDemo() {
        WebElement getDemoButton = driver.wait(ExpectedConditions.elementToBeClickable(getDemoButtonLocator));
        driver.click(getDemoButton);

        throw new TestException("Failed to click the login button on mobile!!!");
    }
}