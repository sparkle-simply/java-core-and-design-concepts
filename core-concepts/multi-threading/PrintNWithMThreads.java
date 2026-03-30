import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class PrintNumbers {

    private int number = 1;
    private final int N;
    private final int M;
    private final Semaphore[] semaphores;

    public PrintNumbers(int N, int M) {
        this.N = N;
        this.M = M;

        semaphores = new Semaphore[M];

        for (int i = 0; i < M; i++) {
            semaphores[i] = new Semaphore(0);
        }

        semaphores[0].release(); // Start with first thread
    }

    public void print(int threadId) {
        try {
            while (true) {
                semaphores[threadId].acquire();

                if (number > N) {
                    // Release next thread so it can exit
                    semaphores[(threadId + 1) % M].release();
                    break;
                }

                System.out.println("Thread-" + threadId + ": " + number);
                number++;

                // Release next thread
                semaphores[(threadId + 1) % M].release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {

        int N = 10;
        int M = 3;

        PrintNumbers printer = new PrintNumbers(N, M);

        ExecutorService executor = Executors.newFixedThreadPool(M);

        for (int i = 0; i < M; i++) {
            int threadId = i;
            executor.execute(() -> printer.print(threadId));
        }

        executor.shutdown();
    }
}