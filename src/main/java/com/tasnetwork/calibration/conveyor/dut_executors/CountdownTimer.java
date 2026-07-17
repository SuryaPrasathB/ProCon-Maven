package com.tasnetwork.calibration.conveyor.dut_executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;


public class CountdownTimer {
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> countdownFuture;
    private AtomicInteger remainingTimeSec;
    private Consumer<String> timeUpdateCallback;
    
    public CountdownTimer(Consumer<String> timeUpdateCallback) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.timeUpdateCallback = timeUpdateCallback;
    }
    
    public void startCountdown(int totalTimeSec) {
        stopCountdown(); // Stop any existing countdown
        
        remainingTimeSec = new AtomicInteger(totalTimeSec);
        
        // Update immediately with initial time
        updateDisplay();
        
        // Schedule updates every 1 second (changed from 5 seconds)
        countdownFuture = scheduler.scheduleAtFixedRate(() -> {
            int currentTime = remainingTimeSec.decrementAndGet(); // Subtract 1 second
            
            if (currentTime >= 0) {
                updateDisplay();
            } else {
                stopCountdown();
                updateDisplay(); // Final update to show 00:00
            }
        }, 1, 1, TimeUnit.SECONDS); // Initial delay 1 sec, repeat every 1 sec
    }
    
    private void updateDisplay() {
        String formattedTime = formatExecutionTime(remainingTimeSec.get());
        if (timeUpdateCallback != null) {
            timeUpdateCallback.accept(formattedTime);
        }
    }
    
    public void pauseCountdown() {
        if (countdownFuture != null && !countdownFuture.isDone()) {
            countdownFuture.cancel(false);
        }
    }
    
    public void resumeCountdown() {
        if (remainingTimeSec != null && remainingTimeSec.get() > 0) {
            startCountdown(remainingTimeSec.get());
        }
    }
    
    public void stopCountdown() {
        if (countdownFuture != null && !countdownFuture.isDone()) {
            countdownFuture.cancel(false);
        }
    }
    
    public void shutdown() {
        stopCountdown();
        scheduler.shutdown();
    }
    
    public int getRemainingTime() {
        return remainingTimeSec != null ? remainingTimeSec.get() : 0;
    }
    
    private String formatExecutionTime(int totalSeconds) {
        if (totalSeconds < 0) return "00:00";
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}