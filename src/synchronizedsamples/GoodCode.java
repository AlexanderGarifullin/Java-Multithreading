package synchronizedsamples;

public class GoodCode {
    public static void main(String[] args) throws InterruptedException{
        ICounter counter = new Counter1();
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
        System.out.println("Counter  = " + counter.getValue());
    }

    static class Counter1 implements ICounter {
        int value = 0;
        @Override
        synchronized public void increment() {
            value++;
        }

        @Override
        synchronized public void decrement() {
            value--;
        }

        @Override
        synchronized public int getValue() {
            return value;
        }
    }

    static class Counter2 implements ICounter {
        int value = 0;
        @Override
        public void increment() {
            synchronized (this) {
                value++;
            }
        }

        @Override
        public void decrement() {
            synchronized (this) {
                value--;
            }
        }

        @Override
        public int getValue() {
            synchronized (this) {
                return value;
            }
        }
    }

    static class Counter3 implements ICounter {
        final Object monitor = new Object();
        int value = 0;
        @Override
        public void increment() {
            synchronized (monitor) {
                value++;
            }
        }

        @Override
        public void decrement() {
            synchronized (monitor) {
                value--;
            }
        }

        @Override
        public int getValue() {
            synchronized (monitor) {
                return value;
            }
        }
    }
}


interface ICounter {
    void increment();
    void decrement();
    int getValue();
}
