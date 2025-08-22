package com.be.automation.core.logging;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LogContext {

    private static final Logger logger = LogManager.getLogger(LogContext.class);

    private static final ThreadLocal<String> testId = new ThreadLocal<>();
    private static final ThreadLocal<String> testName = new ThreadLocal<>();

    public static void setTestCase(String id, String name) {
        testId.set(id);
        testName.set(name);
    }

    public static String getTestId() {
        return testId.get();
    }

    public static String getTestName() {
        return testName.get();
    }

    public static void clear() {
        testId.remove();
        testName.remove();
    }

    // ===================== API LOGGING =====================

    public static void logRequest(
            String basePath,
            String method,
            Map<String, String> headers,
            Map<String, ?> queryParams,
            Map<String, ?> pathParams,
            Object body
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("API Request:\n")
                .append("TestId: ").append(getTestId()).append("\n")
                .append("TestName: ").append(getTestName()).append("\n")
                .append("Method: ").append(method).append("\n")
                .append("Endpoint: ").append(basePath).append("\n")
                .append("Headers: ").append(headers).append("\n")
                .append("QueryParams: ").append(queryParams).append("\n")
                .append("PathParams: ").append(pathParams).append("\n")
                .append("Body: ").append(body).append("\n");

        String requestLog = sb.toString();

        // Log4j2 JSON log (pipeline-friendly)
        logger.info(requestLog);

        // Allure attachment (HTML report)
        Allure.addAttachment("API Request - " + method + " " + basePath,
                "application/json",
                requestLog,
                StandardCharsets.UTF_8.name());
    }

    public static void logResponse(io.restassured.response.Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("API Response:\n")
                .append("TestId: ").append(getTestId()).append("\n")
                .append("TestName: ").append(getTestName()).append("\n")
                .append("Status: ").append(response.getStatusCode()).append("\n")
                .append("Headers: ").append(response.getHeaders()).append("\n")
                .append("Body: ").append(response.getBody().asPrettyString()).append("\n");

        String responseLog = sb.toString();

        // Log4j2 JSON log (pipeline-friendly)
        logger.info(responseLog);

        // Allure attachment (HTML report)
        Allure.addAttachment("API Response - " + response.getStatusCode(),
                "application/json",
                responseLog,
                StandardCharsets.UTF_8.name());
    }
}
