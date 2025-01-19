package threadcreationsamples;

public class ThreadCreation {
    public static void main(String[] args) throws InterruptedException{
        // 1
        MyThread thread1 = new MyThread();
        thread1.start();
        thread1.join();

        // 2
        MyRunnable myRunnable = new MyRunnable();
        Thread thread2 = new Thread(myRunnable);
        thread2.start();
        thread2.join();

        // 3
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread " +  Thread.currentThread().getName() + " run from Anonymous Runnable!");
            }
        });
        thread3.start();
        thread3.join();

        // 4
        Thread thread4 = new Thread(() -> {
            System.out.println("Thread " +  Thread.currentThread().getName() + " run from Lambda Runnable!");
        });
        thread4.start();;
        thread4.join();
    }
}


class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread " +  Thread.currentThread().getName() + " run from MyThread!");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread " +  Thread.currentThread().getName() + " run from MyRunnable!");
    }
}