import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyCustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Thread[] workers;
    private boolean isShutdown = false;

    public MyCustomThreadPool(int numThreads) {
        // 1. Initialize a thread-safe queue to hold tasks
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new Thread[numThreads];

        // 2. Create and start worker threads
        for (int i = 0; i < numThreads; i++) {
            workers[i] = new Worker("Thread-" + i);
            workers[i].start();
        }
    }

    // 3. Method to submit tasks to the pool
    public void execute(Runnable task) {
        if (this.isShutdown) {
            throw new IllegalStateException("ThreadPool is shut down");
        }
        try {
            taskQueue.put(task); // Adds task to queue (blocks if queue is full)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 4. Shutdown the pool
    public void shutdown() {
        this.isShutdown = true;
        for (Thread worker : workers) {
            worker.interrupt(); // Stop the threads even if they are waiting for a task
        }
    }

    // The Worker Class: This defines what each thread does
    private class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        public void run() {
            while (true) {
                try {
                    // Pull a task from the queue.
                    // .take() blocks if the queue is empty until a task arrives.
                    Runnable task = taskQueue.take();
                    System.out.println(getName() + " is processing a task.");
                    task.run();
                } catch (InterruptedException e) {
                    // If interrupted and pool is shutting down, exit the loop
                    if (isShutdown) {
                        break;
                    }
                }
            }
            System.out.println(getName() + " has stopped.");
        }
    }

    // --- Testing the Pool ---
    public static void main(String[] args) {
        MyCustomThreadPool pool = new MyCustomThreadPool(3);

        for (int i = 0; i < 10; i++) {
            int taskId = i;
            pool.execute(() -> {
                System.out.println("Executing Task " + taskId + " inside " + Thread.currentThread().getName());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            });
        }

        // Give it some time then shutdown
        try { Thread.sleep(3000); } catch (InterruptedException e) {}
        pool.shutdown();
    }
}