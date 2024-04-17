package com.nayya.utilities.testcase;

import org.testng.annotations.Listeners;

@Listeners(TestResultListener.class)
public interface TestCase {
    void setup(String environment);
    void teardown();
}
