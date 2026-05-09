import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

/**
 * Problem Statement: FizzBuzz using 4 threads
 * The FizzBuzz problem requires printing numbers from 1 to 20, but for multiples of three, print "Fizz" instead of the number and for the multiples of five print "Buzz".
 * For numbers which are multiples of both three and five print "FizzBuzz".
 *
 * Approach1:
 * Shared variable number keeps track of current value.
 * synchronized ensures only one thread executes at a time.
 * wait() pauses the thread if it’s not its turn.
 * notify() wakes up the other thread.
 *
 * Life Cycle with wait/notify
 * 1. Thread acquires lock
 * 2. Calls wait() → releases lock → goes to WAITING
 * 3. Another thread calls notify()
 * 4. Waiting thread competes for lock again
 * 5. Continues execution
 */
class FizzBuzzPrinterApproach1 {
    private int number = 1;
    private final int MAX = 20;

    public synchronized void printNumber() {
        while (number <= MAX) {
            if (number % 3 != 0 && number % 5 != 0) {
                System.out.println("Number: " + number);
                number++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void printFizz() {
        while (number <= MAX) {
            if (number % 3 == 0 && number % 5 != 0) {
                System.out.println("Fizz");
                number++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void printBuzz() {
        while (number <= MAX) {
            if (number % 5 == 0 && number % 3 != 0) {
                System.out.println("Buzz");
                number++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void printFizzBuzz() {
        while (number <= MAX) {
            if (number % 3 == 0 && number % 5 == 0) {
                System.out.println("FizzBuzz");
                number++;
                notifyAll();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/**
 * Approach2:
 * Using ExecutorService
 * ExecutorService is a framework for managing and reusing threads via thread pools.
 * It separates task submission from thread management, improves performance,
 * supports asynchronous execution, and provides lifecycle control like shutdown and result retrieval using Future
 *
 * notifyAll() safer in multi-thread environments to avoid missed signals
 */
class FizzBuzzPrinterApproach2 {
    private int number = 1;
    private final int MAX = 20;

    public synchronized void printNumber() {
        while (number <= MAX) {
            while (number % 3 != 0 && number % 5 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("Number: " + number);
                number++;
                notifyAll();
            }
        }
    }

    public synchronized void printFizz() {
        while (number <= MAX) {
            while (number % 3 == 0 && number % 5 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("Fizz");
                number++;
                notifyAll();
            }
        }
    }

    public synchronized void printBuzz() {
        while (number <= MAX) {
            while (number % 5 == 0 && number % 3 != 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("Buzz");
                number++;
                notifyAll();
            }
        }
    }

    public synchronized void printFizzBuzz() {
        while (number <= MAX) {
            while (number % 3 == 0 && number % 5 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (number <= MAX) {
                System.out.println("FizzBuzz");
                number++;
                notifyAll();
            }
        }
    }
}

/**
 * ReentrantLock gives explicit locking control.
 * ReentrantLock is an advanced locking mechanism in java.util.concurrent that provides more flexibility than synchronized.
 * It supports reentrancy, fairness policy, interruptible locks, tryLock for timeout handling, and multiple condition variables.
 * It is implemented using AbstractQueuedSynchronizer.
 * Condition works like wait()/notify() but allows multiple wait queues.
 * await() → releases lock & waits.
 * signal() → wakes one waiting thread.
 * finally { lock.unlock(); } ensures lock release preventing deadlock in case of exception flows.
 */
class FizzBuzzPrinterApproach3 {

    private int number = 1;
    private final int MAX = 20;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition numberCondition = lock.newCondition();
    private final Condition fizzCondition = lock.newCondition();
    private final Condition buzzCondition = lock.newCondition();
    private final Condition fizzBuzzCondition = lock.newCondition();

    public void printNumber() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 3 != 0 && number % 5 != 0) {
                    numberCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("Number: " + number);
                    number++;
                    fizzCondition.signal();
                    buzzCondition.signal();
                    fizzBuzzCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void printFizz() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 3 == 0 && number % 5 != 0) {
                    fizzCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("Fizz");
                    number++;
                    numberCondition.signal();
                    buzzCondition.signal();
                    fizzBuzzCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void printBuzz() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 5 == 0 && number % 3 != 0) {
                    buzzCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("Buzz");
                    number++;
                    numberCondition.signal();
                    fizzCondition.signal();
                    fizzBuzzCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void printFizzBuzz() {
        lock.lock();
        try {
            while (number <= MAX) {
                while (number % 3 == 0 && number % 5 == 0) {
                    fizzBuzzCondition.await();
                }

                if (number <= MAX) {
                    System.out.println("FizzBuzz");
                    number++;
                    numberCondition.signal();
                    fizzCondition.signal();
                    buzzCondition.signal();
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
 * Semaphore is a concurrency utility that controls access to a shared resource using permits.
 * Threads must acquire a permit before proceeding and release it afterward. Unlike locks, semaphores can allow
 * multiple threads simultaneously.
 * They are commonly used for resource pooling, rate limiting, and controlling concurrency.
 *
 * Here with four semaphores,
 * The number semaphore starts with one permit so the number thread executes first.
 * Each thread acquires its semaphore, prints the number, increments it, and then releases the other semaphores.
 * This guarantees deterministic alternation without using synchronized or wait/notify.
 */
class FizzBuzzPrinterApproach4 {

    private int number = 1;
    private final int MAX = 20;

    private final Semaphore numberSemaphore = new Semaphore(1);  // Start with Number
    private final Semaphore fizzSemaphore = new Semaphore(0); // Fizz waits initially
    private final Semaphore buzzSemaphore = new Semaphore(0); // Buzz waits initially
    private final Semaphore fizzBuzzSemaphore = new Semaphore(0); // FizzBuzz waits initially

    public void printNumber() {
        try {
            while (number <= MAX) {
                numberSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("Number: " + number);
                    number++;
                }

                if (number % 3 == 0 && number % 5 == 0) {
                    fizzBuzzSemaphore.release();
                } else if (number % 3 == 0) {
                    fizzSemaphore.release();
                } else if (number % 5 == 0) {
                    buzzSemaphore.release();
                } else {
                    numberSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printFizz() {
        try {
            while (number <= MAX) {
                fizzSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("Fizz");
                    number++;
                }

                if (number % 3 == 0 && number % 5 == 0) {
                    fizzBuzzSemaphore.release();
                } else if (number % 3 == 0) {
                    fizzSemaphore.release();
                } else if (number % 5 == 0) {
                    buzzSemaphore.release();
                } else {
                    numberSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printBuzz() {
        try {
            while (number <= MAX) {
                buzzSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("Buzz");
                    number++;
                }

                if (number % 3 == 0 && number % 5 == 0) {
                    fizzBuzzSemaphore.release();
                } else if (number % 3 == 0) {
                    fizzSemaphore.release();
                } else if (number % 5 == 0) {
                    buzzSemaphore.release();
                } else {
                    numberSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printFizzBuzz() {
        try {
            while (number <= MAX) {
                fizzBuzzSemaphore.acquire();

                if (number <= MAX) {
                    System.out.println("FizzBuzz");
                    number++;
                }

                if (number % 3 == 0 && number % 5 == 0) {
                    fizzBuzzSemaphore.release();
                } else if (number % 3 == 0) {
                    fizzSemaphore.release();
                } else if (number % 5 == 0) {
                    buzzSemaphore.release();
                } else {
                    numberSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class App {
    public static void main(String[] args) {

        FizzBuzzPrinterApproach1 printer1 = new FizzBuzzPrinterApproach1();
        Thread numberThread = new Thread(() -> printer1.printNumber());
        Thread fizzThread = new Thread(() -> printer1.printFizz());
        Thread buzzThread = new Thread(() -> printer1.printBuzz());
        Thread fizzBuzzThread = new Thread(() -> printer1.printFizzBuzz());
        numberThread.start();
        fizzThread.start();
        buzzThread.start();
        fizzBuzzThread.start();

        FizzBuzzPrinterApproach2 printer2 = new FizzBuzzPrinterApproach2();
        ExecutorService executor2 = Executors.newFixedThreadPool(4);
        executor2.execute(() -> printer2.printNumber());
        executor2.execute(() -> printer2.printFizz());
        executor2.execute(() -> printer2.printBuzz());
        executor2.execute(() -> printer2.printFizzBuzz());
        executor2.shutdown();

        FizzBuzzPrinterApproach3 printer3 = new FizzBuzzPrinterApproach3();
        ExecutorService executor3 = Executors.newFixedThreadPool(4);
        executor3.execute(printer3::printNumber);
        executor3.execute(printer3::printFizz);
        executor3.execute(printer3::printBuzz);
        executor3.execute(printer3::printFizzBuzz);
        executor3.shutdown();

        FizzBuzzPrinterApproach4 printer4 = new FizzBuzzPrinterApproach4();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.execute(printer4::printNumber);
        executor.execute(printer4::printFizz);
        executor.execute(printer4::printBuzz);
        executor.execute(printer4::printFizzBuzz);
        executor.shutdown();
    }
}