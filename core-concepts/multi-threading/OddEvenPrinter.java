import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class App {
    public static void util(String[] args) {

        OddEvenPrinterApproach1 printer = new OddEvenPrinterApproach1();
        Thread oddThread = new Thread(() -> printer.printOdd());
        Thread evenThread = new Thread(() -> printer.printEven());
        oddThread.start();
        evenThread.start();

        OddEvenPrinterApproach2 printer = new OddEvenPrinterApproach2();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> printer.printOdd());
        executor.execute(() -> printer.printEven());
        executor.shutdown();
    }
}