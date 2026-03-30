import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class PrintABC {

    private final int TIMES;

    private final Semaphore semA = new Semaphore(1); // A starts first
    private final Semaphore semB = new Semaphore(0);
    private final Semaphore semC = new Semaphore(0);

    public PrintABC(int TIMES) {
        this.TIMES = TIMES;
    }

    public void printA() {
        try {
            for (int i = 0; i < TIMES; i++) {
                semA.acquire();
                System.out.println("A");
                semB.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printB() {
        try {
            for (int i = 0; i < TIMES; i++) {
                semB.acquire();
                System.out.println("B");
                semC.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printC() {
        try {
            for (int i = 0; i < TIMES; i++) {
                semC.acquire();
                System.out.println("C");
                semA.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {

        PrintABC printer = new PrintABC(5);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(printer::printA);
        executor.execute(printer::printB);
        executor.execute(printer::printC);

        executor.shutdown();
    }
}
