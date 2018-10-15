package b;


import java.util.concurrent.ThreadLocalRandom;

class MyThread extends Thread {

    int id, size, threadNum;
    int[] arr;

    public MyThread(int id, int[] arr, int threadNum){
        this.id = id;
        this.arr = arr;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
//        System.out.println("thread: " + id + ": start");

        for (int i = 0; i < 1000000; i++) {
//            System.out.println(this.id + "\t:" + arr[0]);
            arr[0]++;
        }

//        System.out.println("thread: " + id + ": end");
    }
}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        int testLimit = 100;
        int[] results  = new int[testLimit];

        for (int j = 0; j < testLimit; j++) {

            int[] arr = new int[1];
            arr[0] = 0;

            int threadNum = 2;
            MyThread[] threadArray = new MyThread[threadNum];

            for (int i = 0; i < threadNum; i++) {
                threadArray[i] = new MyThread(i, arr, threadNum);
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
