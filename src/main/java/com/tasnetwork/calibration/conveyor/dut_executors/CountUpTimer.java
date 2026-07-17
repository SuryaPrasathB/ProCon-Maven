package com.tasnetwork.calibration.conveyor.dut_executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class CountUpTimer {
	    private ScheduledExecutorService scheduler;
	    private ScheduledFuture<?> timerFuture;
	    private AtomicInteger elapsedTimeSec;
	    private Consumer<String> timeUpdateCallback;
	    private boolean isRunning;
	    
	    public CountUpTimer(Consumer<String> timeUpdateCallback) {
	        this.scheduler = Executors.newScheduledThreadPool(1);
	        this.timeUpdateCallback = timeUpdateCallback;
	        this.elapsedTimeSec = new AtomicInteger(0);
	        this.isRunning = false;
	    }
	    
	    public void startTimer() {
	        stopTimer(); // Stop any existing timer
	        
	        elapsedTimeSec.set(0);
	        isRunning = true;
	        
	        // Update immediately with initial time (00:00)
	        updateDisplay();
	        
	        // Schedule updates every 1 second
	        timerFuture = scheduler.scheduleAtFixedRate(() -> {
	            int currentTime = elapsedTimeSec.incrementAndGet();
	            updateDisplay();
	        }, 1, 1, TimeUnit.SECONDS);
	    }
	    
	    public void resumeTimer() {
	        if (!isRunning) {
	            isRunning = true;
	            
	            // Schedule updates every 1 second from current time
	            timerFuture = scheduler.scheduleAtFixedRate(() -> {
	                int currentTime = elapsedTimeSec.incrementAndGet();
	                updateDisplay();
	            }, 1, 1, TimeUnit.SECONDS);
	        }
	    }
	    
	    public void pauseTimer() {
	        if (isRunning) {
	            stopTimer();
	            isRunning = false;
	        }
	    }
	    
	    public void stopTimer() {
	        if (timerFuture != null && !timerFuture.isDone()) {
	            timerFuture.cancel(false);
	        }
	        isRunning = false;
	    }
	    
	    public void resetTimer() {
	        stopTimer();
	        elapsedTimeSec.set(0);
	        updateDisplay();
	    }
	    
	    private void updateDisplay() {
	        String formattedTime = formatElapsedTime(elapsedTimeSec.get());
	        if (timeUpdateCallback != null) {
	            timeUpdateCallback.accept(formattedTime);
	        }
	    }
	    
	    public int getElapsedTime() {
	        return elapsedTimeSec.get();
	    }
	    
	    public boolean isRunning() {
	        return isRunning;
	    }
	    
	    public void shutdown() {
	        stopTimer();
	        scheduler.shutdown();
	    }
	    
	    private String formatElapsedTime(int totalSeconds) {
	        int hours = totalSeconds / 3600;
	        int minutes = (totalSeconds % 3600) / 60;
	        int seconds = totalSeconds % 60;
	        
	        if (hours > 0) {
	            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	        } else {
	            return String.format("%02d:%02d", minutes, seconds);
	        }
	    }
	    
	    // Optional: Format with milliseconds for higher precision
	    public String formatElapsedTimeWithMs(long totalMilliseconds) {
	        long totalSeconds = totalMilliseconds / 1000;
	        long hours = totalSeconds / 3600;
	        long minutes = (totalSeconds % 3600) / 60;
	        long seconds = totalSeconds % 60;
	        long milliseconds = totalMilliseconds % 1000;
	        
	        if (hours > 0) {
	            return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
	        } else {
	            return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
	        }
	    }
}
	

