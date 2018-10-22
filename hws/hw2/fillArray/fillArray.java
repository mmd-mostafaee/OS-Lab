package a;


import java.util.ArrayList;
import java.util.Collections;
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
        for (int i = this.id; i < this.size; i += this.threadNum) {
            this.arr[i] = (int)(ThreadLocalRandom.current().nextInt() * 200);
        }
    }
}

public class main {

    public static void main(String[] args) {

        int[] threadNums = {1,2,3,4,5,6,7,8,16,32,64};
        int testsCount = 10;
        int size = 320000000;

        int[] arr = new int[size];

        for (int threadNum: threadNums) {
            System.out.print("\n" + threadNum + " Thread(s):");

            ArrayList times = new ArrayList();

            for (int t = 0; t < testsCount; t++) {

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
                times.add(totalTime);
                System.out.print("\t" + totalTime/1000000 + "ms");
            }
            System.out.print("\tMin:" + (long)(Collections.min(times))/1000000 + "ms");


        }
    }
}
