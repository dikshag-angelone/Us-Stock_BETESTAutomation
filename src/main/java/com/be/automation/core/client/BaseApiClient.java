package com.be.automation.core.client;

import com.be.automation.config.ConfigManager;
import com.be.automation.config.FrameworkConfig;
import com.be.automation.core.http.RequestBuilder;
import io.restassured.response.Response;

public abstract class BaseApiClient {

    protected final FrameworkConfig config = ConfigManager.getConfig();
    protected final String baseUrl;

    protected BaseApiClient() {
        this.baseUrl = config.baseUrl();
    }


    protected RequestBuilder request() {
        RequestBuilder builder = new RequestBuilder()
                .setBasePath(baseUrl);


        // Apply auth
        if ("token".equalsIgnoreCase(config.authType()) && !config.authToken().isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + config.authToken());
        } else if ("basic".equalsIgnoreCase(config.authType())) {
            builder.addHeader("Authorization", "Basic " +
                    java.util.Base64.getEncoder()
                            .encodeToString((config.username() + ":" + config.password()).getBytes())
            );
        }

        return builder;
    }

    // Convenience HTTP methods
    protected Response get(String path) {
        return request().setBasePath(baseUrl + path).get();
    }

    // Convenience HTTP methods
    protected Response get(String path, Object body) {
        return request().setBasePath(baseUrl + path).setBody(body).get();
    }

    protected Response post(String path, Object body) {
        return request().setBasePath(baseUrl + path).setBody(body).post();
    }

    protected Response put(String path, Object body) {
        return request().setBasePath(baseUrl + path).setBody(body).put();
    }

    protected Response patch(String path, Object body) {
        return request().setBasePath(baseUrl + path).setBody(body).patch();
    }

    protected Response delete(String path) {
        return request().setBasePath(baseUrl + path).delete();
    }

    protected Response delete(String path, Object body) {
        return request().setBasePath(baseUrl + path).setBody(body).delete();
    }
}
