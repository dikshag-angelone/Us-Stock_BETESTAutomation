package com.be.automation.config;

import org.aeonbits.owner.ConfigFactory;
import java.util.Properties;

public final class ConfigManager {

    private static volatile FrameworkConfig CONFIG;

    private ConfigManager() {}

    public static FrameworkConfig getConfig() {
        if (CONFIG == null) {
            synchronized (ConfigManager.class) {
                if (CONFIG == null) {
                    CONFIG = loadConfig();
                }
            }
        }
        return CONFIG;
    }

    public static void reload() {
        synchronized (ConfigManager.class) {
            CONFIG = loadConfig();
        }
    }

    private static FrameworkConfig loadConfig() {
        // Pick env from system property or ENV var, fallback to default
        String env = System.getProperty("env", System.getenv().getOrDefault("ENV", "default")).toLowerCase();

        String fileName;
        switch (env) {
            case "uat":
                fileName = "uat.properties";
                break;
            case "cug":
                fileName = "cug.properties";
                break;
            case "gm":
                fileName = "gm.properties";
                break;
            default:
                fileName = "env.properties";
        }

        System.out.println("[ConfigManager] Loading configuration: " + fileName);

        Properties props = PropertyUtils.loadPropertiesFromClasspath(fileName);

        return ConfigFactory.create(
                FrameworkConfig.class,
                System.getProperties(),   // system properties
                System.getenv(),          // env variables
                props                     // env-specific properties
        );
    }
}
