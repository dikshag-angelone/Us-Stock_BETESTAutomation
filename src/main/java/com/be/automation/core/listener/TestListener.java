package com.be.automation.core.listener;

import com.be.automation.core.logging.LogContext;
import io.qameta.allure.Allure;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testId = result.getMethod().getDescription() != null ? result.getMethod().getDescription() : result.getMethod().getMethodName();
        String testName = result.getMethod().getMethodName();

        LogContext.setTestCase(testId, testName);
        Allure.step("🚀 Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Allure.step("✅ Test Passed: " + result.getMethod().getMethodName());
        LogContext.clear();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Allure.step("❌ Test Failed: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            Allure.addAttachment("Failure Stacktrace", result.getThrowable().toString());
        }
        LogContext.clear();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Allure.step("⚠️ Test Skipped: " + result.getMethod().getMethodName());
        LogContext.clear();
    }

    @Override
    public void onFinish(ITestContext context) {
        // no-op
    }
}
