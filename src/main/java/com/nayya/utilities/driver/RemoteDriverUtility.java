package com.nayya.utilities.driver;

import com.nayya.utilities.capabilities.NayyaCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.TestException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RemoteDriverUtility implements DriverUtility {
    private final static String SELENIUM_HOST = System.getProperty("selenium.remote.browser.url",
            "http://localhost:4444");
    private final static String SELENIUM_REMOTE_BROWSER = System.getProperty("selenium.remote.browser", "chrome");

    public RemoteDriverUtility() {
    }

    public WebDriver createDriver(Map<String, String> executionEnvironment) {
        RemoteWebDriver remoteWebDriver = null;
        do {
            try {
                remoteWebDriver = new RemoteWebDriver(getSeleniumURL(), getCapabilities(executionEnvironment));
            } catch (UnreachableBrowserException e) {
                System.err.println("UnreachableBrowserException: " + e.getMessage());
            }
        } while (remoteWebDriver == null);
        remoteWebDriver.setFileDetector(new LocalFileDetector());

        return remoteWebDriver;
    }

    private DesiredCapabilities getCapabilities(Map<String, String> executionEnvironment) {
        NayyaCapabilities capabilities = new NayyaCapabilities().remote(SELENIUM_REMOTE_BROWSER);

        return capabilities.get();
    }

    private URL getSeleniumURL() {
        URL seleniumHostURL;
        try {
            seleniumHostURL = new URL(SELENIUM_HOST + "/wd/hub");
        } catch (MalformedURLException e) {
            throw new TestException("Unable to create url from " + SELENIUM_HOST);
        }

        return seleniumHostURL;
    }
}
