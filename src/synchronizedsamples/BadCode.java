package synchronizedsamples;

public class BadCode {
    public static void main(String[] args) throws InterruptedException{
        Counter counter = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.decrement();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Counter  = " + counter.getCounter());
    }

    static class Counter {
        int cnt = 0;

        void increment() {
            cnt++;
        }

        void decrement() {
            cnt--;
        }

        int getCounter() {
            return cnt;
        }
    }
}