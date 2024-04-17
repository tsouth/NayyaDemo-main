package com.nayya.utilities.testcase;

import com.nayya.utilities.logger.Logger;
import org.testng.*;

import java.util.Arrays;
import java.util.Iterator;

public class TestResultListener extends TestListenerAdapter {
    private final String TRAVIS_BRANCH = System.getenv("TRAVIS_BRANCH");
    private final String TRAVIS_PULL_REQUEST = System.getenv("TRAVIS_PULL_REQUEST");
    // private final DataDogAPI dataDogAPI = new DataDogAPI();
    private final Logger log = new Logger(getClass().getName());

    public TestResultListener() {
    }

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String[] testSuite = getTestSuite(result.getInstanceName());
        String testName = result.getMethod().getMethodName();

        log.info(getTestSuiteName(testSuite) + ": " + testName + " ran successfully");
        sendTestMetric(testSuite, testName, "success");
        // PageManager.getInstance().testTearDown(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String[] testSuite = getTestSuite(result.getInstanceName());
        String testName = result.getMethod().getMethodName();

        log.warning(getTestSuiteName(testSuite) + ": " + testName + " skipped");
        sendTestMetric(testSuite, testName, "failure");
        // PageManager.getInstance().testTearDown(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer(result) != null) {
            RetryAnalyzer retryAnalyzer = (RetryAnalyzer) result.getMethod().getRetryAnalyzer(result);
            String[] testSuite = getTestSuite(result.getInstanceName());
            String testName = result.getMethod().getMethodName();

            if (retryAnalyzer.isRetryAvailable()) {
                sendTestMetric(testSuite, testName, "failure");
                result.setStatus(ITestResult.SKIP);
            } else {
                log.severe(getTestSuiteName(testSuite) + ": " + result.getMethod().getMethodName() + " failed!\r\n"
                        + result.getThrowable().getMessage());
                sendTestMetric(testSuite, testName, "failure");
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        }
        // PageManager.getInstance().testTearDown(result);
    }

    /*
     * credits to
     * https://stackoverflow.com/questions/26145666/how-to-send-testng-defaultsuite-
     * results-after-retryanalyzer-to-maven-surefire-pl
     */
    @Override
    public void onFinish(ITestContext context) {
        Iterator<ITestResult> failedTestCases = context.getFailedTests().getAllResults().iterator();
        while (failedTestCases.hasNext()) {
            log.warning("failedTestCases");
            ITestResult failedTestCase = failedTestCases.next();
            ITestNGMethod method = failedTestCase.getMethod();

            if (context.getFailedTests().getResults(method).size() > 1) {
                log.warning("failed test case remove as dup:" + failedTestCase.getTestClass().toString());
                failedTestCases.remove();
            } else if (context.getPassedTests().getResults(method).size() > 0) {
                log.warning("failed test case remove as pass retry:" + failedTestCase.getTestClass().toString());
                failedTestCases.remove();
            }
        }
    }

    private String[] getTestSuite(String instanceName) {
        String[] testPath = instanceName.split("\\.");

        String[] testSuite;
        if (testPath.length == 7) {
            testSuite = Arrays.copyOfRange(testPath, testPath.length - 2, testPath.length);
        } else {
            testSuite = Arrays.copyOfRange(testPath, testPath.length - 1, testPath.length);
        }

        return testSuite;
    }

    private String getTestSuiteName(String[] testSuite) {
        String testSuiteName;

        if (testSuite.length == 2) {
            testSuiteName = testSuite[testSuite.length - 2] + " ~ " + testSuite[testSuite.length - 1];
        } else {
            testSuiteName = testSuite[testSuite.length - 1];
        }

        return testSuiteName;
    }

    private void sendTestMetric(String[] testSuite, String testName, String status) {
        if (TRAVIS_BRANCH.equalsIgnoreCase("master") && TRAVIS_PULL_REQUEST.equalsIgnoreCase("false")) {
            // long timestamp = System.currentTimeMillis() / 1000;
            // String tag = "tests:" + String.join(".", testSuite).toLowerCase() + "." +
            // testName.toLowerCase();

            if (status.equalsIgnoreCase("failure")) {
                // dataDogAPI.postTestMetrics(timestamp, tag, 1);
            } else if (status.equalsIgnoreCase("success")) {
                // dataDogAPI.postTestMetrics(timestamp, tag, 0);
            }
        }
    }
}