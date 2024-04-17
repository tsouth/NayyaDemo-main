package com.nayya.utilities.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class LocalDriverUtility implements DriverUtility {
    private final String chromedriverPath = System.getProperty("user.dir") + "/bin/chromedriver-123-Mac.exe";

    public LocalDriverUtility() {
    }

    public WebDriver createDriver(Map<String, String> executionEnvironment) {
        if (executionEnvironment.containsKey("os")) {
            return getDesktopDriver(executionEnvironment.get("browser"));
        } else {
            throw new IllegalArgumentException("Unsupported local device or browser");
        }
    }

    private WebDriver getDesktopDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            return setUpChromeDriver();
                } else {
            throw new IllegalArgumentException("Unsupported local browser: " + browser);
        }
    }

    private WebDriver setUpChromeDriver() {
        System.setProperty("webdriver.chrome.driver", chromedriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        // options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }
}
