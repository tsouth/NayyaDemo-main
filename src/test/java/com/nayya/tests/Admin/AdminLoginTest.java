package com.nayya.tests.Admin;

import com.nayya.pages.Admin.dashboardPage.AdminDashboardPage;
import com.nayya.pages.Admin.loginPage.AdminLoginPage;
import com.nayya.utilities.managers.PageManager;
import com.nayya.utilities.testcase.RetryAnalyzer;
import com.nayya.utilities.testcase.TakeScreenshotOnFailureListener;
import com.nayya.utilities.testcase.TestCase;
import org.testng.annotations.*;

@Listeners(TakeScreenshotOnFailureListener.class)
public class AdminLoginTest implements TestCase {

    @Parameters({"environment"})
    @BeforeMethod
    public void setup(@Optional("production") String environment) {
        PageManager.getInstance().open(environment);
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void testLoginAsAnAdmin() throws InterruptedException {
        AdminLoginPage adminLoginPage = PageManager.getInstance().navigateToPage(AdminLoginPage.class);
        AdminDashboardPage adminDashboardPage = adminLoginPage.signInAsAnAdmin();
        adminDashboardPage.verifyCorrectPage();}

    @AfterMethod
    public void teardown() {
        PageManager.getInstance().close();
    }
}
