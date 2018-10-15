package a;


import java.util.concurrent.ThreadLocalRandom;

class MyThread extends Thread {

    int id, size, threadNum;
    int[] arr;

    public MyThread(int id,int size, int[] arr, int threadNum){
        this.id = id;
        this.size = size;
        this.arr = arr;
        this.threadNum = threadNum;

    }

    @Override
    public void run() {
        System.out.println("thread: " + id + ": start");

        for (int i = this.id; i < this.size; i += this.threadNum) {
            this.arr[i] = (int)(ThreadLocalRandom.current().nextInt() * 200);
        }

        System.out.println("thread: " + id + ": end");
    }
}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        int size = 320000000;
        int[] arr = new int[size];

        int threadNum = 1;
        MyThread[] threadArray = new MyThread[threadNum];

        for (int i = 0; i < threadNum; i++) {
            threadArray[i] = new MyThread(i, size, arr, threadNum);
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
        System.out.println(totalTime/1000000);

        System.out.println("Thread: main: end");
    }
}
