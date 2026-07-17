package com.tasnetwork.calibration.conveyor.AsyncHttpClient;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

public class HttpClientSingleton {
	private static AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
			.setConnectTimeout(5000)
			.setRequestTimeout(5000)
			.setMaxConnectionsPerHost(20)
			.setMaxConnections(100)
			.setPooledConnectionIdleTimeout(30000)
		    .build();
	private static final AsyncHttpClient INSTANCE = new AsyncHttpClient();

    private HttpClientSingleton() {
    	
    	
    	//INSTANCE = new AsyncHttpClient(config);
    }

/*    public static AsyncHttpClient getInstance() {
        return INSTANCE;
    }*/
}
