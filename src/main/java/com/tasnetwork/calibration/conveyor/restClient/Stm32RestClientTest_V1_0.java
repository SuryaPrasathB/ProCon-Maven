package com.tasnetwork.calibration.conveyor.restClient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Stm32RestClientTest_V1_0 {

	//private static final String BASE_URL = "http://192.168.1.123/api/device";
    private static final String BASE_URL = "http://192.168.1.123:80/api/device/1/bay/1/set";
    private static final int TIMEOUT_MS = 5000; // 5 seconds

    public static void main(String[] args) {
        System.out.println("Starting Java client...");

        int positiveCount = 0;
        int negativeCount = 0;
        int i = 0;

        String redStatusLed = "Off";
        String greenStatusLed = "Off";
        String yellowStatusLed = "Off";

        // Create a persistent HTTP client (equivalent to Python's requests.Session)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MS)
                .setSocketTimeout(TIMEOUT_MS)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnPerRoute(10) // Adjust as needed
                .setMaxConnTotal(20) // Adjust as needed
                .build()) {

            while (i<100000) { // Infinite loop, like Python code
                i++;

                // Toggle LED states similar to the Python code
                if (redStatusLed.equals("On") && greenStatusLed.equals("Off") && yellowStatusLed.equals("Off")) {
                    redStatusLed = "Off";
                    greenStatusLed = "On";
                    yellowStatusLed = "Off";
                } else if (redStatusLed.equals("Off") && greenStatusLed.equals("On") && yellowStatusLed.equals("Off")) {
                    redStatusLed = "Off";
                    greenStatusLed = "Off";
                    yellowStatusLed = "On";
                } else if (redStatusLed.equals("Off") && greenStatusLed.equals("Off") && yellowStatusLed.equals("On")) {
                    redStatusLed = "On";
                    greenStatusLed = "Off";
                    yellowStatusLed = "Off";
                } else {
                    redStatusLed = "On";
                    greenStatusLed = "Off";
                    yellowStatusLed = "Off";
                }

                String requestUrl = String.format("%s?opRed=%s&opGreen=%s&opYellow=%s",
                        BASE_URL, redStatusLed, greenStatusLed, yellowStatusLed);

                try {
                    HttpGet request = new HttpGet(requestUrl);
                    request.addHeader("Connection", "keep-alive");

                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                        String responseBody = EntityUtils.toString(response.getEntity());
                        positiveCount++;
                        System.out.printf("Counter: %d -> Response: %s, Positive: %d (%.2f%%), Negative: %d (%.2f%%)%n",
                                i, responseBody, positiveCount,
                                (positiveCount / (double) i) * 100, negativeCount,
                                (negativeCount / (double) i) * 100);
                    }

                } catch (IOException e) {
                    negativeCount++;
                    System.err.println("Request failed: " + e.getMessage());
                }

                // Sleep 100ms between requests
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                	//TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        } catch (IOException e) {
            System.err.println("Error creating HTTP client: " + e.getMessage());
        }
    }
}

