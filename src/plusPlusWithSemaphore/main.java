package plusPlusWithSemaphore;


import java.util.concurrent.Semaphore;

class MyThread extends Thread {

    int id, size, threadNum;
    int[] arr;
    Semaphore s;

    public MyThread(int id, int[] arr, int threadNum, Semaphore s){
        this.id = id;
        this.arr = arr;
        this.threadNum = threadNum;
        this.s = s;
    }

    @Override
    public void run() {
//        System.out.println("thread: " + id + ": start");

        for (int i = 0; i < 100; i++) {
//            System.out.println(this.id + "\t:" + arr[0]);
            try {
                this.s.acquire();
                arr[0]++;
                Thread.sleep(20);
                this.s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

//        System.out.println("thread: " + id + ": end");
    }
}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        int testLimit = 10;
        int[] results  = new int[100 * 2 + 1];
        Semaphore s = new Semaphore(2);

        for (int j = 0; j < testLimit; j++) {

            int[] arr = new int[1];
            arr[0] = 0;

            int threadNum = 2;
            MyThread[] threadArray = new MyThread[threadNum];

            for (int i = 0; i < threadNum; i++) {
                threadArray[i] = new MyThread(i, arr, threadNum, s);
            }

            for (int i = 0; i < threadNum; i++) {
                threadArray[i].start();
            }

            long startTime = System.nanoTime();

            for (int i = 0; i < threadNum; i++) {
                try {
                    threadArray[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
//            System.out.println(totalTime/1000000);

            System.out.println("Thread: main: result: " + arr[0]);
            results[arr[0]]++;
        }

        System.out.println("Thread: main: end");
    }
}
