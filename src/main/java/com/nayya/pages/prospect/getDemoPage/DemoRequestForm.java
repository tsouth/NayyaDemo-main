package com.nayya.pages.prospect.getDemoPage;

import com.nayya.utilities.managers.PageManager;
import com.nayya.utilities.page.BasePage;
import com.nayya.utilities.page.PageNavigation;
import com.nayya.utilities.page.NayyaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.TestException;
import java.util.List;
import java.util.Random;

public class DemoRequestForm implements BasePage, PageNavigation {
    private final NayyaDriver driver;

    private final By firstNameFieldLocator = By.xpath("//input[@name='firstname']");
    private final By lastNameFieldLocator = By.xpath("//input[@name='lastname']");
    private final By workEmailAddressFieldLocator = By.xpath("//input[@type='email'][@placeholder='Work Email*']");
    private final By phoneNumberFieldLocator = By.xpath("//input[@name='phone']");
    private final By jobTitleFieldLocator = By.xpath("//input[@name='jobtitle']");
    private final By companyNameFieldLocator = By.xpath("//input[@name='company']");
    private final By organizationTypeFieldLocator = By.xpath("//select[@name='select_industry']");
    private final By employeeNumberListLocator = By.xpath("//select[@name='number_of_employees__dropdown_']");
    private final By disclaimerCheckboxLocator = By.xpath("//input[@name='disclaimer']");
    private final By getADemoSubmitButtonLocator = By.xpath("//input[@type='submit'][@value='Get a Demo']");

    public DemoRequestForm(WebDriver driver) {
        this.driver = new NayyaDriver(driver);
    }

    @Override
    public void verifyCorrectPage() {
        driver.wait(ExpectedConditions.visibilityOfElementLocated(firstNameFieldLocator));
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

    public DemoConfirmationPage submitDemoRequestForm() {
        String timestamp = Long.toString(System.currentTimeMillis());
        String phoneNumber = "777777777";

        WebElement firstNameField = driver.wait(ExpectedConditions.visibilityOfElementLocated(firstNameFieldLocator));
        driver.setText(firstNameField, "Nayya QA");

        WebElement lastNameField = driver.findElement(lastNameFieldLocator);
        driver.setText(lastNameField, "Testing Request A Demo");

        WebElement workEmailAddressField = driver.findElement(workEmailAddressFieldLocator);
        //Will replace this email address once we've set up a QA specific email account with IT
        driver.setText(workEmailAddressField, "tsouthworth+nayyaQaRequestADemo_" + timestamp + "@nayya.com");

        WebElement phoneNumberField = driver.findElement(phoneNumberFieldLocator);
        driver.setText(phoneNumberField, phoneNumber);

        WebElement jobTitleField = driver.findElement(jobTitleFieldLocator);
        driver.setText(jobTitleField, "Master of Disaster");

        WebElement firmNameField = driver.findElement(companyNameFieldLocator);
        driver.setText(firmNameField, "Nayya QA Organization");

        selectOrganizationType();
        selectNumberOfEmployees();

        WebElement disclaimerCheckBox = driver.findElement(disclaimerCheckboxLocator);
        driver.executeScript("arguments[0].scrollIntoView(true);", disclaimerCheckBox);
        driver.executeScript("window.scrollBy(0,-200)");
        driver.click(disclaimerCheckBox);

        WebElement getADemoSubmitButton = driver.wait(ExpectedConditions.elementToBeClickable(getADemoSubmitButtonLocator));
//        driver.click(getADemoSubmitButton);

        /*
Commented out the above line so that the form doesn't submit and interrupt the sales pipeline.
Let's work with Sales where they set up a rule in salesforce to filter and ignore all incoming request from a qa email address.
 */
        return PageManager.getInstance().instantiateCurrentPage(DemoConfirmationPage.class);
    }

    public DemoRequestForm selectOrganizationType() {
        WebElement organizationTypeList = driver.findElement(organizationTypeFieldLocator);
        Select select = new Select(organizationTypeList);
        List<WebElement> organizationTypes = select.getOptions();
        WebElement type = driver.wait(ExpectedConditions.elementToBeClickable(
                organizationTypes.get(new Random().nextInt(organizationTypes.size()))));
        driver.click(type);

        return PageManager.getInstance().instantiateCurrentPage(DemoRequestForm.class);
    }

    public DemoRequestForm selectNumberOfEmployees() {
        WebElement employeeNumberList = driver.wait(ExpectedConditions.presenceOfElementLocated(employeeNumberListLocator));
        Select select = new Select(employeeNumberList);
        List<WebElement> employeeNumbers = select.getOptions();
        WebElement type = driver.wait(ExpectedConditions.elementToBeClickable(
                employeeNumbers.get(new Random().nextInt(employeeNumbers.size()))));
        driver.click(type);

        return PageManager.getInstance().instantiateCurrentPage(DemoRequestForm.class);
    }
}
