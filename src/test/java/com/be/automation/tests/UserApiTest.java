package com.be.automation.tests;

import com.be.automation.core.client.UserApiClient;
import com.be.automation.core.logging.LogContext;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UserApiTest extends BaseTest {

    private UserApiClient client;

    @BeforeMethod
    public void setup() {
        client = new UserApiClient();
        LogContext.setTestCase("TC-001", "User API Test");
    }



    @Test
    public void testCreateUser() {
        Map<String, Object> body = new HashMap<>();
        body.put("title", "Test Post");
        body.put("body", "This is a dummy post");
        body.put("userId", 1);

        Response response = client.createUser(body);
        Assert.assertEquals(response.getStatusCode(), 201);
        System.out.println(response.asPrettyString());
    }

    @Test
    public void testGetUser() {
        Response response = client.getUserById(1);
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println(response.asPrettyString());
    }
}
