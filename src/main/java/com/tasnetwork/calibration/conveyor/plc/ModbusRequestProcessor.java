package com.tasnetwork.calibration.conveyor.plc;

import java.util.concurrent.*;

public class ModbusRequestProcessor {
	
	
    private static final ConcurrentHashMap<String, BlockingQueue<Runnable>> requestQueues = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Thread> workerThreads = new ConcurrentHashMap<>();

    public static void addRequest(String serverKey, Runnable task) {
        requestQueues.computeIfAbsent(serverKey, key -> {
            BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
            Thread workerThread = new Thread(() -> {
                while (true) {
                    try {
                        queue.take().run(); // Execute task sequentially
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            workerThread.start();
            workerThreads.put(key, workerThread);
            return queue;
        });

        requestQueues.get(serverKey).offer(task);
    }

    public static void stopServerProcessor(String serverKey) {
        Thread workerThread = workerThreads.remove(serverKey);
        if (workerThread != null) {
            workerThread.interrupt();
        }
        requestQueues.remove(serverKey);
    }
}

