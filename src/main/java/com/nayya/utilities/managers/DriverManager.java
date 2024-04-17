package com.nayya.utilities.managers;

import com.nayya.utilities.driver.DriverUtility;
import com.nayya.utilities.driver.LocalDriverUtility;
import com.nayya.utilities.driver.RemoteDriverUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DriverManager {
    private static final String DRIVER = System.getProperty("browser.driver", "CHROME");
    private final DriverUtility localDriverUtility = new LocalDriverUtility();
    private final DriverUtility remoteDriverUtility = new RemoteDriverUtility();
    private final WebDriver driver;
    private final Map<String, String> executionEnvironment = setExecutionEnvironment();

    public DriverManager() {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        driver = createDriver();
    }

    public void close() {
        if (driver != null && !driver.toString().contains("null")) {
            try {
                driver.quit();
            } catch (WebDriverException ignore) {
            }
        }
    }

    public void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    public String getSessionID() {
        return ((RemoteWebDriver) driver).getSessionId().toString();
    }

    public final WebDriver getDriver() {
        return driver;
    }

    private WebDriver createDriver() {
        WebDriver newDriver;
        long timeout = System.currentTimeMillis() + 30000;
        DriverUtility driverUtility = "REMOTE".equalsIgnoreCase(DRIVER) ? remoteDriverUtility : localDriverUtility;
        do {
            newDriver = driverUtility.createDriver(executionEnvironment);
        } while (System.currentTimeMillis() < timeout && (newDriver == null || newDriver.toString().contains("null")));

        if (newDriver == null || newDriver.toString().contains("null")) {
            throw new WebDriverException("Webdriver Failed to Open");
        }

        newDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

        if (isSupportedOperatingSystem()) {
            newDriver.manage().window().maximize();
        }

        return newDriver;
    }

    public String getBrowser() {
        return executionEnvironment.get("browser");
    }

    public String getOperatingSystem() {
        return executionEnvironment.get("os");
    }

    public String getDevice() {
        return executionEnvironment.get("device");
    }

    public boolean isSupportedBrowser() {
        return isSupportedBrowser(executionEnvironment.get("browser"));
    }

    public boolean isSupportedOperatingSystem() {
        return executionEnvironment.containsKey("os");
    }

    public boolean isSupportedDevice() {
        return executionEnvironment.containsKey("device");
    }

    private boolean isSupportedBrowser(String browser) {
        return Stream.of(SUPPORTED_BROWSERS.values()).map(Enum::name).collect(Collectors.toSet())
                .contains(browser.toUpperCase());
    }

    private boolean isSupportedOperatingSystem(String operatingSystem) {
        return Stream.of(SUPPORTED_OPERATING_SYSTEMS.values()).map(Enum::name).collect(Collectors.toSet())
                .contains(operatingSystem.toUpperCase());
    }

    private Map<String, String> setExecutionEnvironment() {
        Map<String, String> driverMap = new HashMap<>();

        if (DRIVER == null) {
            throw new IllegalArgumentException("No browser or device specified to test with");
        } else {
            ArrayList<String> driverSplit = new ArrayList<>(Arrays.asList(DRIVER.split("\\.")));

            String os = null;
            String browser = null;

            for (String driverPart : driverSplit) {
                if (isSupportedOperatingSystem(driverPart)) {
                    os = driverPart;
                } else if (isSupportedBrowser(driverPart)) {
                    browser = driverPart;
                }
            }

            if (browser != null) {
                if (browser.equalsIgnoreCase("edge") && (os == null || !os.equalsIgnoreCase("windows"))) {
                    throw new IllegalArgumentException(driver + " is not a supported driver setup");
                } else
                    driverMap.put("os", Objects.requireNonNullElse(os, "osx"));
                driverMap.put("browser", browser);
            } else {
                throw new IllegalArgumentException(driver + " is not a supported driver setup");
            }
        }
        return driverMap;
    }

    private enum SUPPORTED_BROWSERS {
        CHROME, FIREFOX, SAFARI, EDGE, REMOTE
    }

    private enum SUPPORTED_OPERATING_SYSTEMS {
        OSX
    }
}