package com.nayya.pages.prospect.getDemoPage;

import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.TestException;

public class DemoConfirmationPage implements BasePage {
    private final NayyaDriver driver;

    private final By thankYouMessageLocator = By.xpath("//h1[contains(text(), 'Thank You')]");

    public DemoConfirmationPage(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.visibilityOfElementLocated(thankYouMessageLocator));
    }

    @Override
    public void waitForPageLoad() {
        if (!driver.pageLoadedWithRefresh()) {
            throw new TestException(getClass().getName() + " failed to load!!");
        }
    }

    public void verifyAppointmentRequest() {
        while (true) {
            try {
                driver.waitWithTimeout(ExpectedConditions.visibilityOfElementLocated(
                        thankYouMessageLocator), 10);
                break;
            } catch (NoSuchElementException | TimeoutException ignore) {}
        }
    }

}
