package com.nayya.utilities.testcase;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (isRetryAvailable()) {
            retryCount++;
            return true;
        }
        return false;
    }

    public boolean isRetryAvailable() {
        return retryCount < 0;
    }
}