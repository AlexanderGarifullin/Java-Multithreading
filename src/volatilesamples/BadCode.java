package volatilesamples;

public class BadCode {
    static int i = 0;

    public static void main(String[] args) {
        System.out.println("BadCode");
        new MyThreadWriter().start();
        new MyThreadReader().start();
    }

    static class MyThreadWriter extends Thread {
        @Override
        public void run() {
            while (i < 5) {
                System.out.println("Increment i to " + (++i));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Finished MyThreadWriter");
        }
    }

    static class MyThreadReader extends Thread {
        @Override
        public void start() {
            int li = i;
            while(li < 5) {
                if (li != i) {
                    System.out.println("New value of i is " + i);
                    li = i;
                }
            }
            System.out.println("Finished MyThreadRead");
        }
    }
}
