package com.tasnetwork.calibration.conveyor.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class ChannelQueueRequestProcessor {
    private static final Map<String, BlockingQueue<PriorityTask<?>>> requestQueues = new ConcurrentHashMap<>();
    private static final Map<String, Thread> workerThreads = new ConcurrentHashMap<>();

    enum Status {
        QUEUED, RUNNING, COMPLETED, TIMEOUT, FAILED
    }

    static class PriorityTask<T> implements Comparable<PriorityTask<?>> {
        final Callable<T> task;
        final int priority;
        final CompletableFuture<T> future = new CompletableFuture<>();
        final AtomicReference<Status> status = new AtomicReference<>(Status.QUEUED);
        final long timeout;
        final TimeUnit unit;

        PriorityTask(Callable<T> task, int priority, long timeout, TimeUnit unit) {
            this.task = task;
            this.priority = priority;
            this.timeout = timeout;
            this.unit = unit;
        }

        void execute() {
            status.set(Status.RUNNING);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<T> internalFuture = executor.submit(task);

            try {
                T result = internalFuture.get(timeout, unit); // Timeout starts from execution time
                status.set(Status.COMPLETED);
                future.complete(result);
            } catch (TimeoutException e) {
                status.set(Status.TIMEOUT);
                internalFuture.cancel(true);
                future.completeExceptionally(new TimeoutException("Task timed out during execution"));
            } catch (Exception e) {
                status.set(Status.FAILED);
                future.completeExceptionally(e);
            } finally {
                executor.shutdownNow();
            }
        }

        @Override
        public int compareTo(PriorityTask<?> other) {
            return Integer.compare(other.priority, this.priority); // higher priority first
        }
    }

    public static <T> Future<T> addRequest(String serverKey, Callable<T> task, int priority, long timeout,
            TimeUnit unit) {
        PriorityTask<T> priorityTask = new PriorityTask<>(task, priority, timeout, unit);

        BlockingQueue<PriorityTask<?>> queue = requestQueues.computeIfAbsent(serverKey, key -> {
            PriorityBlockingQueue<PriorityTask<?>> newQueue = new PriorityBlockingQueue<>();
            Thread worker = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        PriorityTask<?> next = newQueue.take();
                        next.execute();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            worker.setDaemon(true);
            worker.start();
            workerThreads.put(key, worker);
            return newQueue;
        });

        queue.offer(priorityTask);

        return priorityTask.future;
    }

    public static void stopServerProcessor(String serverKey) {
        Thread thread = workerThreads.remove(serverKey);
        if (thread != null)
            thread.interrupt();
        requestQueues.remove(serverKey);
    }

}
