class OddEvenPrinter {
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

public class Main {
    public static void main(String[] args) {
        OddEvenPrinter printer = new OddEvenPrinter();

        Thread oddThread = new Thread(() -> printer.printOdd());
        Thread evenThread = new Thread(() -> printer.printEven());

        oddThread.start();
        evenThread.start();
    }
}