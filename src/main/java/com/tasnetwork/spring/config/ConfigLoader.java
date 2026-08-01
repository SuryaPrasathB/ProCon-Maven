package com.tasnetwork.spring.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConfigLoader {

    private static final String MASTER_CONFIG_PATH = "config"; // External folder
    private static final String MASTER_CONFIG_FILE_NAME = "master_config.json";
    
    private MasterConfig masterConfig;

    public ConfigLoader() {
        loadConfigs();
    }
   
    private void loadConfigs() {
        masterConfig = loadConfigFile(MASTER_CONFIG_PATH,MASTER_CONFIG_FILE_NAME, MasterConfig.class);
        //secondaryConfig = loadConfigFile(SECONDARY_CONFIG_FILE, SecondaryConfig.class);
    }
    
    private <T> T loadConfigFile(String filePath,String fileName, Class<T> configClass) {
        try {
            File configFile;

            // Detect if running inside Eclipse (development mode)
            if (new File("src/main/resources/config/" + fileName).exists()) {
                configFile = new File("src/main/resources/config/" + fileName);
                System.out.println("Loading " + fileName + " from development path: " + configFile.getAbsolutePath());
            } else {
                // Running from JAR, look in external "config" folder
                configFile = Paths.get(filePath, fileName).toFile();
                System.out.println("Loading " + fileName + " from external path: " + configFile.getAbsolutePath());
            }

            if (configFile.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                return objectMapper.readValue(configFile, configClass);
            } else {
                throw new RuntimeException("Config file not found: " + configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public MasterConfig getMasterConfig() {
        return masterConfig;
    }
}