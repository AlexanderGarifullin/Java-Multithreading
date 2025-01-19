package atomicsamples;

import java.util.concurrent.atomic.AtomicInteger;

public class GoodCode {
    static AtomicInteger value = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10_000; i++) {

            new MyThread().start();
        }
        Thread.sleep(2_000);
        System.out.println("Value = " + value.get());
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            value.incrementAndGet();
        }
    }
}
