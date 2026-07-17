package com.tasnetwork.spring.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterConfig {
    
    @JsonProperty("ConfigFileVersion")
    private String configFileVersion;

    @JsonProperty("CustomerDetails")
    private CustomerDetails customerDetails;

    // Getters and Setters
    public String getConfigFileVersion() {
        return configFileVersion;
    }

    public void setConfigFileVersion(String configFileVersion) {
        this.configFileVersion = configFileVersion;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public static class CustomerDetails {
        
        @JsonProperty("AppConfigFileName")
        private String appConfigFileName;

        @JsonProperty("ConfigFilePath")
        private String configFilePath;

        // Getters and Setters
        public String getAppConfigFileName() {
            return appConfigFileName;
        }

        public void setAppConfigFileName(String appConfigFileName) {
            this.appConfigFileName = appConfigFileName;
        }

        public String getConfigFilePath() {
            return configFilePath;
        }

        public void setConfigFilePath(String configFilePath) {
            this.configFilePath = configFilePath;
        }
    }
}