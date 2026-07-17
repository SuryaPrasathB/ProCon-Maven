package com.tasnetwork.spring.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Component
public class ConfigLoader {

    private static final String MASTER_CONFIG_PATH = "config"; // External folder
    private static final String MASTER_CONFIG_FILE_NAME = "master_config.json";
    
    private static final String APP_CONFIG_PATH = "config/app"; // External folder
    private static final String APP_CONFIG_FILE_NAME = "app_config_elmeasure_jan2025_v1_5.json";
    
    private MasterConfig masterConfig;

    public ConfigLoader() {
        loadConfigs();
    }

/*    public void loadConfig() {
        try {
            Resource resource = new ClassPathResource("config/master_config.json");
            InputStream inputStream = resource.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            masterConfig = objectMapper.readValue(inputStream, MasterConfig.class);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load master_config.json", e);
        }
    }*/
    
/*    private void loadConfig() {
        try {
            File configFile;

            // Detect if running inside Eclipse (development mode)
            if (new File("src/main/resources/config/master_config.json").exists()) {
                configFile = new File("src/main/resources/config/master_config.json");
                System.out.println("Loading config from development path: " + configFile.getAbsolutePath());
            } else {
                // Running from JAR, look in external "config" folder
                configFile = Paths.get(MASTER_CONFIG_PATH, MASTER_CONFIG_FILE_NAME).toFile();
                System.out.println("Loading config from external path: " + configFile.getAbsolutePath());
            }

            if (configFile.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                this.masterConfig = objectMapper.readValue(configFile, MasterConfig.class);
                this.masterConfig = objectMapper.readValue(configFile, MasterConfig.class);
            } else {
                throw new RuntimeException("Config file not found: " + configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }*/
    
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