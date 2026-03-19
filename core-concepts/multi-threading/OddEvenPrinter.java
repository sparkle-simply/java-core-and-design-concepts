import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Approach1:
 * Shared variable number keeps track of current value.
 * synchronized ensures only one thread executes at a time.
 * wait() pauses the thread if it’s not its turn.
 * notify() wakes up the other thread.
 */
class OddEvenPrinterApproach1 {
    private int number = 1;
    private final int MAX = 20;

    public synchronized void printOdd() {
        while (number <= MAX) {
            if (number % 2 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Odd: " + number);
                number++;
                notify();
            }
        }
    }

    public synchronized void printEven() {
        while (number <= MAX) {
            if (number % 2 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Even: " + number);
                number++;
                notify();
            }
        }
    }
}

/**
 * Approach2:
 * Using ExecutorService
 * notifyAll() safer in multi-thread environments to avoid missed signals
 */
class OddEvenPrinterApproach2 {
    private int number = 1;
    private final int MAX = 20;

    public synchronized void printOdd() {
        while (number <= MAX) {
            while (number % 2 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("Odd: " + number);
                number++;
                notifyAll();
            }
        }
    }

    public synchronized void printEven() {
        while (number <= MAX) {
            while (number % 2 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("Even: " + number);
                number++;
                notifyAll();
            }
        }
    }
}

class OddEvenPrinterApproach3 {

    private int number = 1;
    private final int MAX = 20;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition oddCondition = lock.newCondition();
    private final Condition evenCondition = lock.newCondition();

    public void printOdd() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 2 == 0) {
                    oddCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("Odd: " + number);
                    number++;
                    evenCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void printEven() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 2 != 0) {
                    evenCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("Even: " + number);
                    number++;
                    oddCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

public class App {
    public static void util(String[] args) {

        OddEvenPrinterApproach1 printer1 = new OddEvenPrinterApproach1();
        Thread oddThread = new Thread(() -> printer1.printOdd());
        Thread evenThread = new Thread(() -> printer1.printEven());
        oddThread.start();
        evenThread.start();

        OddEvenPrinterApproach2 printer2 = new OddEvenPrinterApproach2();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> printer2.printOdd());
        executor.execute(() -> printer2.printEven());
        executor.shutdown();

        OddEvenPrinterApproach3 printer3 = new OddEvenPrinterApproach3();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(printer3::printOdd);
        executor.execute(printer3::printEven);
        executor.shutdown();
    }
}