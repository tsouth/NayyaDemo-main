package com.nayya.utilities.page;

import com.nayya.utilities.logger.Logger;
import com.nayya.utilities.managers.PageManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;

public class NayyaDriver {
    private final static String REACT_TRIGGER_CHANGE_PATH = System.getProperty("user.dir")
            + "/src/main/resources/javascript/reach-trigger-change.min.js";
    private final static String SCREENSHOT_DIRECTORY_PATH = System.getProperty("user.dir") + "/target/screenshots/";

    private final WebDriver driver;
    private final Logger log = new Logger(getClass().getName());

    public NayyaDriver(WebDriver driver) {
        this.driver = driver;
    }

    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    public void click(WebElement button) {
        if (PageManager.getInstance().getBrowser().equalsIgnoreCase("ie")
                || PageManager.getInstance().getBrowser().equalsIgnoreCase("safari")) {
            executeScript("arguments[0].click();", button);
        } else {
            button.click();
        }
    }

    public Object executeScript(String script, Object... elements) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return javascriptExecutor.executeScript(script, elements);
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public String getNayyaURL() {
        String environment = PageManager.getInstance().getEnvironment();
        String baseUrl = "";

        if (environment.equalsIgnoreCase("production")) {
            baseUrl = "https://www.nayya.com";
        } else if (environment.equalsIgnoreCase("staging")) {
            baseUrl = "https://stage.nayya.com";
        } else if (environment.equalsIgnoreCase("development")) {
            baseUrl = "http://localhost:3000";
        }

        return baseUrl;    }

    public void moveToElement(WebElement element) {
        new Actions(driver).moveToElement(element);
    }

    public void navigateTo(String URL) {
        driver.navigate().to(URL);
    }

    public void setText(WebElement textField, String text) {
        textField.clear();
        if (PageManager.getInstance().getBrowser().equalsIgnoreCase("ie")) {
            executeScript("arguments[0].value='" + text + "';", textField);
            if (!((boolean) executeScript("return typeof reactTriggerChange === 'function';"))) {
                Scanner scanner;
                try {
                    scanner = new Scanner(new FileInputStream(REACT_TRIGGER_CHANGE_PATH));
                } catch (FileNotFoundException e) {
                    throw new TestException(REACT_TRIGGER_CHANGE_PATH + " was not found");
                }
                StringBuilder inject = new StringBuilder();
                while (scanner.hasNext()) {
                    String[] temp = scanner.next().split("\r\n");
                    for (String s : temp) {
                        inject.append(s);
                        inject.append(' ');
                    }
                }
                executeScript(inject.toString());
            }
            executeScript("reactTriggerChange(arguments[0]);", textField);
        } else {
            textField.sendKeys(text);
        }
    }

    public <T> T wait(ExpectedCondition<T> expectedCondition) {
        return waitWithTimeout(expectedCondition, 15);
    }

    public <T> T waitWithTimeout(ExpectedCondition<T> expectedCondition, int durationInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(durationInSeconds)).until(expectedCondition);
    }

    public void takeScreenshot(Optional<String> fileName) {
        try {
            String timestamp = Long.toString(System.currentTimeMillis());
            String destinationFilePath = SCREENSHOT_DIRECTORY_PATH + fileName.orElse(UUID.randomUUID().toString())
                    + timestamp + ".png";
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File file = screenshot.getScreenshotAs(OutputType.FILE);
            File destination = new File(destinationFilePath);
            FileUtils.copyFile(file, destination);
            log.info("Screenshot saved to: " + destinationFilePath);
        } catch (Throwable e) {
            log.warning("Error generating screenshot: " + e.getMessage());
        }
    }

    public boolean pageLoaded() {
        try {
            wait(driver -> executeScript("return document.readyState").equals("complete"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean pageLoadedWithRefresh() {
        for (int i = 0; i < 3; i++) {
            try {
                wait(driver -> executeScript("return document.readyState").equals("complete"));
                return true;
            } catch (TimeoutException e) {
                log.warning("Failed to load page, refreshing...");
                driver.navigate().refresh();
            }
        }
        return false;
    }

    public void zoom(int zoom) {
        executeScript(String.format("document.body.style.zoom='%d%%'", zoom));
    }

}