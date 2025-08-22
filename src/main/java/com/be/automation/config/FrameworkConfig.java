package com.be.automation.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
        "classpath:env.properties"          // Fallback config
})
public interface FrameworkConfig extends Config {

    @Key("base.url")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String baseUrl();

    @Key("auth.type")
    @DefaultValue("none") // options: none | basic | token
    String authType();

    @Key("auth.token")
    @DefaultValue("")
    String authToken();

    @Key("auth.username")
    @DefaultValue("user")
    String username();

    @Key("auth.password")
    @DefaultValue("pass")
    String password();

    @Key("timeout.ms")
    @DefaultValue("5000")
    int timeoutMs();

    @Key("retries.count")
    @DefaultValue("2")
    int retries();

    @Key("log.level")
    @DefaultValue("INFO") // INFO, DEBUG, ERROR
    String logLevel();
}