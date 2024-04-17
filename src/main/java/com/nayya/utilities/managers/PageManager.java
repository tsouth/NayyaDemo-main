package com.nayya.utilities.managers;

import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.ModularURL;
import com.nayya.utilities.page.PageNavigation;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Optional;

public class PageManager {
    private static PageManager instance;
    private DriverManager driverManager;
    private String environment;

    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
        }

        return instance;
    }

    public void open(String environment) {
        driverManager = new DriverManager();
        driverManager.clearCookies();

        setEnvironment(environment);
    }

    public <T extends BasePage & PageNavigation> T navigateToPage(Class<T> pageClass) {
        T page = PageFactory.initElements(driverManager.getDriver(), pageClass);

        page.navigateTo();
        page.waitForPageLoad();
        page.verifyCorrectPage();

        return page;
    }

    public <T extends BasePage & PageNavigation> T navigateToPageWithNewContext(Class<T> pageClass) {
        driverManager.clearCookies();

        return navigateToPage(pageClass);
    }

    public <T extends BasePage & PageNavigation & ModularURL> T navigateToPage(Class<T> pageClass, Object... urlIds) {
        T page = PageFactory.initElements(driverManager.getDriver(), pageClass);

        page.modifyURL(urlIds);
        page.navigateTo();
        page.waitForPageLoad();
        page.verifyCorrectPage();

        return page;
    }

    public <T extends BasePage & PageNavigation & ModularURL> T navigateToPageWithNewContext(Class<T> pageClass,
                                                                                             Object... urlIds) {
        driverManager.clearCookies();
        T page = PageFactory.initElements(driverManager.getDriver(), pageClass);

        page.modifyURL(urlIds);
        page.navigateTo();
        page.waitForPageLoad();
        page.verifyCorrectPage();

        return page;
    }

    public <T extends BasePage> T instantiateCurrentPage(Class<T> pageClass) {
        T page = PageFactory.initElements(driverManager.getDriver(), pageClass);

        page.verifyCorrectPage();
        page.waitForPageLoad();

        return page;
    }

    public boolean isSupportedBrowser() {
        return driverManager.isSupportedOperatingSystem() && driverManager.isSupportedBrowser();
    }

    public boolean isSupportedOperatingSystem() {
        return driverManager.isSupportedOperatingSystem();
    }

    public boolean isSupportedMobileDevice() {
        return driverManager.isSupportedDevice();
    }

    public String getSessionID() {
        return driverManager.getSessionID();
    }

    public String getEnvironment() {
        return environment;
    }

    private void setEnvironment(String environment) {
        String systemEnvironment = System.getenv("ENVIRONMENT");
        if (systemEnvironment != null) {
            this.environment = systemEnvironment;
        } else {
            this.environment = environment;
        }
    }

    public String getBrowser() {
        return driverManager.getBrowser();
    }

    public String getOperatingSystem() {
        return driverManager.getOperatingSystem();
    }

    public String getDevice() {
        return driverManager.getDevice();
    }

    public void takeScreenshot() {
        this.takeScreenshot(Optional.empty());
    }

    public void takeScreenshot(Optional<String> fileName) {
        (new NayyaDriver(driverManager.getDriver())).takeScreenshot(fileName);
    }

    public void close() {
        driverManager.close();
    }
}