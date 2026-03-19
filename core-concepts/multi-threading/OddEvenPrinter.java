import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

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

/**
 * ReentrantLock gives explicit locking control.
 * Condition works like wait()/notify() but allows multiple wait queues.
 * await() → releases lock & waits.
 * signal() → wakes one waiting thread.
 * finally { lock.unlock(); } ensures lock release.
 */
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

/**
 * Here with two semaphores,
 * One starts with 1 permit to allow the odd thread to run first,
 * and the other starts with 0 to block the even thread.
 * After printing, each thread releases the other semaphore to maintain strict alternation.
 */
class OddEvenPrinterApproach4 {

    private int number = 1;
    private final int MAX = 20;

    private final Semaphore oddSemaphore = new Semaphore(1);  // Start with Odd
    private final Semaphore evenSemaphore = new Semaphore(0); // Even waits initially

    public void printOdd() {
        try {
            while (number <= MAX) {
                oddSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("Odd: " + number);
                    number++;
                }

                evenSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printEven() {
        try {
            while (number <= MAX) {
                evenSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("Even: " + number);
                    number++;
                }

                oddSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        ExecutorService executor2 = Executors.newFixedThreadPool(2);
        executor2.execute(() -> printer2.printOdd());
        executor2.execute(() -> printer2.printEven());
        executor2.shutdown();

        OddEvenPrinterApproach3 printer3 = new OddEvenPrinterApproach3();
        ExecutorService executor3 = Executors.newFixedThreadPool(2);
        executor3.execute(printer3::printOdd);
        executor3.execute(printer3::printEven);
        executor3.shutdown();

        OddEvenPrinterApproach4 printer4 = new OddEvenPrinterApproach4();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(printer4::printOdd);
        executor.execute(printer4::printEven);
        executor.shutdown();
    }
}