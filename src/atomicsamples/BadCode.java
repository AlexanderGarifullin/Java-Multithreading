package atomicsamples;

public class BadCode {
    static int value = 0;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10_000; i++) {
            new MyThread().start();
        }
        Thread.sleep(2_000);
        System.out.println("Value = " + value);
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            value++; // Несогласованное изменение переменной
        }
    }
}
