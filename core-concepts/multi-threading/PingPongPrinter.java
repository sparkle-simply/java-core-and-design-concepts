import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class PingPong {

    private final int TIMES;
    private final Semaphore pingSemaphore = new Semaphore(1); // PING starts
    private final Semaphore pongSemaphore = new Semaphore(0); // PONG waits

    public PingPong(int TIMES) {
        this.TIMES = TIMES;
    }

    public void printPing() {
        try {
            for (int i = 0; i < TIMES; i++) {
                pingSemaphore.acquire();
                System.out.println("PING");
                pongSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printPong() {
        try {
            for (int i = 0; i < TIMES; i++) {
                pongSemaphore.acquire();
                System.out.println("PONG");
                pingSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {

        PingPong pingPong = new PingPong(5);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(pingPong::printPing);
        executor.execute(pingPong::printPong);

        executor.shutdown();
    }
}