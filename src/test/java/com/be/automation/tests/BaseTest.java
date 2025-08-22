package com.be.automation.tests;


import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import io.qameta.allure.Allure;

public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("===== Starting API Test Suite =====");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String status = result.isSuccess() ? "PASSED" : "FAILED";
        Allure.addAttachment("Test Result",
                "Test: " + testName + " | Status: " + status);
    }
}

