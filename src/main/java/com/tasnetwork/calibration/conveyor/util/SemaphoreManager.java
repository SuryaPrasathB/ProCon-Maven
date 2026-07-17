package com.tasnetwork.calibration.conveyor.util;

import java.util.concurrent.Semaphore;

public class SemaphoreManager {
	private static final Semaphore semaphore = new Semaphore(1, true);
	
	private SemaphoreManager() {
		// Prevent instantiation
	}
	
	public static Semaphore getSemaphore() {
        return semaphore;
    }
}
