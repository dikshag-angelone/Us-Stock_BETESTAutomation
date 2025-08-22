package com.be.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils() {
        // prevent instantiation
    }

    public static Properties loadPropertiesFromClasspath(String fileName) {
        Properties props = new Properties();
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Properties file not found on classpath: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
        return props;
    }
}
