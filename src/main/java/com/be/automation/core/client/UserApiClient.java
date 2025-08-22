package com.be.automation.core.client;

import com.be.automation.core.http.RequestBuilder;
import io.restassured.response.Response;

import java.util.Map;

/**
 * Example client for JSONPlaceholder users/posts API
 */
public class UserApiClient extends BaseApiClient {

    public UserApiClient() {
        super();
    }

    // POST: Create dummy user/post
    public Response createUser(Map<String, Object> body) {
        return request()
                .setBasePath("/posts")
                .setBody(body)
                .post();
    }

    // GET: Retrieve dummy user by ID
    public Response getUserById(int id) {
        return request()
                .setBasePath("/users/{id}")
                .addPathParam("id", String.valueOf(id))
                .get();
    }
}
