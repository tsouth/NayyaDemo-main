package com.nayya.tests.Prospect;

import com.nayya.pages.prospect.homePage.HamburgerMenu;
import com.nayya.pages.prospect.homePage.HomePage;
import com.nayya.pages.prospect.getDemoPage.DemoConfirmationPage;
import com.nayya.pages.prospect.getDemoPage.DemoRequestForm;
import com.nayya.utilities.testcase.TakeScreenshotOnFailureListener;
import com.nayya.utilities.managers.PageManager;
import com.nayya.utilities.testcase.RetryAnalyzer;
import com.nayya.utilities.testcase.TestCase;
import org.testng.annotations.*;

@Listeners(TakeScreenshotOnFailureListener.class)
public class RequestADemoTest implements TestCase {

    @Parameters({"environment"})
    @BeforeMethod
    public void setup(@Optional("production") String environment) {
        PageManager.getInstance().open(environment);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testRequestADemo() {
        HomePage homePage = PageManager.getInstance().navigateToPage(HomePage.class);
        DemoRequestForm demoRequestForm;
        if (PageManager.getInstance().isSupportedMobileDevice()) {
            HamburgerMenu hamburgerMenu = homePage.openHamburgerMenu();
            demoRequestForm = hamburgerMenu.getDemo();
        } else {
            demoRequestForm = homePage.getDemo();
        }
        DemoConfirmationPage demoConfirmationPage = demoRequestForm.submitDemoRequestForm();
        //demoConfirmationPage.verifyAppointmentRequest(); We won't land on this page due to a commented out portion of the submitDemoRequestForm function
    }

    @AfterMethod
    public void teardown() {
        PageManager.getInstance().close();
    }

}
