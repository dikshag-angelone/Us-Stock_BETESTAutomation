package com.be.automation.core.http;

import com.be.automation.config.FrameworkConfig;
import com.be.automation.config.ConfigManager;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    private String baseUri;
    private String basePath;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> pathParams = new HashMap<>();
    private Object body;

    private final FrameworkConfig config;

    public RequestBuilder() {
        this.config = ConfigManager.getConfig();

        // Default values from config
        this.baseUri = config.baseUrl();

        if ("token".equalsIgnoreCase(config.authType())) {
            headers.put("Authorization", "Bearer " + config.authToken());
        } else if ("basic".equalsIgnoreCase(config.authType())) {
            String basicAuth = config.username() + ":" + config.password();
            headers.put("Authorization", "Basic " +
                    java.util.Base64.getEncoder().encodeToString(basicAuth.getBytes()));
        }
    }

    public RequestBuilder setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        return this;
    }

    public RequestBuilder setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public RequestBuilder addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public RequestBuilder addQueryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public RequestBuilder addPathParam(String key, String value) {
        pathParams.put(key, value);
        return this;
    }

    public RequestBuilder setBody(Object body) {
        this.body = body;
        return this;
    }

    private RequestSpecification buildSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();

        if (baseUri != null) builder.setBaseUri(baseUri);
        if (basePath != null) builder.setBasePath(basePath);
        if (!headers.isEmpty()) builder.addHeaders(headers);
        if (!queryParams.isEmpty()) builder.addQueryParams(queryParams);
        if (!pathParams.isEmpty()) builder.addPathParams(pathParams);
        if (body != null) builder.setBody(body);

        // Timeouts from config
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", config.timeoutMs())
                        .setParam("http.socket.timeout", config.timeoutMs()));

        return RestAssured.given()
                .spec(builder.build())
                .relaxedHTTPSValidation()
                .log().all();
    }

    private Response execute(Method method) {
        Response response = buildSpec().request(method);

        attachRequestToAllure(method);
        attachCurlCommand(method);
        attachResponseToAllure(response);

        return response;
    }

    private void attachRequestToAllure(Method method) {
        StringBuilder reqLog = new StringBuilder();
        reqLog.append("HTTP Method: ").append(method).append("\n");
        if (baseUri != null) reqLog.append("Base URI: ").append(baseUri).append("\n");
        if (basePath != null) reqLog.append("Base Path: ").append(basePath).append("\n");
        if (!headers.isEmpty()) reqLog.append("Headers: ").append(headers).append("\n");
        if (!queryParams.isEmpty()) reqLog.append("Query Params: ").append(queryParams).append("\n");
        if (!pathParams.isEmpty()) reqLog.append("Path Params: ").append(pathParams).append("\n");
        if (body != null) reqLog.append("Body: ").append(body.toString()).append("\n");

        Allure.addAttachment("API Request", "text/plain",
                reqLog.toString(), StandardCharsets.UTF_8.name());
    }

    private void attachCurlCommand(Method method) {
        StringBuilder curl = new StringBuilder("curl -X ")
                .append(method.name()).append(" '").append(baseUri);
        if (basePath != null) curl.append(basePath);
        if (!queryParams.isEmpty()) {
            curl.append("?");
            queryParams.forEach((k, v) -> curl.append(k).append("=").append(v).append("&"));
            curl.setLength(curl.length() - 1); // remove last &
        }
        curl.append("'");

        headers.forEach((k, v) -> curl.append(" -H '").append(k).append(": ").append(v).append("'"));
        if (body != null) curl.append(" -d '").append(body.toString()).append("'");

        Allure.addAttachment("Curl Command", "text/plain",
                curl.toString(), StandardCharsets.UTF_8.name());
    }

    private void attachResponseToAllure(Response response) {
        StringBuilder resLog = new StringBuilder();
        resLog.append("Status Code: ").append(response.getStatusCode()).append("\n");
        resLog.append("Headers: ").append(response.getHeaders().toString()).append("\n");
        resLog.append("Body: ").append(response.asPrettyString()).append("\n");

        Allure.addAttachment("API Response", "text/plain",
                resLog.toString(), StandardCharsets.UTF_8.name());
    }

    // === HTTP Methods ===
    public Response get()    { return execute(Method.GET); }
    public Response post()   { return execute(Method.POST); }
    public Response put()    { return execute(Method.PUT); }
    public Response patch()  { return execute(Method.PATCH); }
    public Response delete() { return execute(Method.DELETE); }
}
