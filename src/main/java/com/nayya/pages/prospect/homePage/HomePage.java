package com.nayya.pages.prospect.homePage;

import com.nayya.pages.prospect.getDemoPage.DemoRequestForm;
import com.nayya.utilities.managers.PageManager;
import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.PageNavigation;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.TestException;

public class HomePage implements BasePage, PageNavigation {
    private final NayyaDriver driver;

    private final By getDemoButtonLocator = By.xpath("//input[@type='submit'][@value='Get Demo']");
    private final By hamburgerMenuLocator = By.xpath("//*[@id='__next']/main/nav/div/div[2]/div");


    public HomePage(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.visibilityOfElementLocated(getDemoButtonLocator));
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

    public DemoRequestForm getDemo() {
        WebElement getDemoButton = driver.wait(ExpectedConditions.elementToBeClickable(getDemoButtonLocator));
        driver.click(getDemoButton);

        return PageManager.getInstance().instantiateCurrentPage(DemoRequestForm.class);
    }


    public HamburgerMenu openHamburgerMenu() {
        WebElement hamburgerMenu = driver.wait(ExpectedConditions.elementToBeClickable(hamburgerMenuLocator));
        driver.click(hamburgerMenu);

        return PageManager.getInstance().instantiateCurrentPage(HamburgerMenu.class);
    }

}
