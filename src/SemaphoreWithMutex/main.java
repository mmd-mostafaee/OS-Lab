package SemaphoreWithMutex;


import sun.misc.Lock;

import java.util.ArrayList;


class MyThread extends Thread {

    int id, size, threadNum;
    int[] arr;
    MySemaphore s;

    public MyThread(int id, int[] arr, int threadNum, MySemaphore s){
        this.id = id;
        this.arr = arr;
        this.threadNum = threadNum;
        this.s = s;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                this.s.acquire();
                arr[0]++;
                Thread.sleep(20);
                this.s.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

class MySemaphore {

    int count = 0;
    int maxCount;
    Lock countLock, limitLock;

    public MySemaphore(int maxCount){
        this.maxCount = maxCount;
        countLock = new Lock();
        limitLock = new Lock();
    }

    public void acquire() throws InterruptedException {
            this.countLock.lock();
            if(this.count < this.maxCount){
                this.count++;
                this.countLock.unlock();
            } else {
                this.countLock.unlock();
                limitLock.lock();
            }
    }

    public void release() throws InterruptedException {
        this.countLock.lock();
        if(this.count > 0) this.count--;
        this.countLock.unlock();
        this.limitLock.unlock();
    }
}

public class main {

    public static void main(String[] args) {
        int threadNum = 2;
        int semaphoreMaxCount = 1;

        MySemaphore s = new MySemaphore(semaphoreMaxCount);
        int[] arr = new int[1];
        arr[0] = 0;

        MyThread[] threadArray = new MyThread[threadNum];

        for (int i = 0; i < threadNum; i++) {
            threadArray[i] = new MyThread(i, arr, threadNum, s);
        }

        for (int i = 0; i < threadNum; i++) {
            threadArray[i].start();
        }

        for (int i = 0; i < threadNum; i++) {
            try {
                threadArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sum result: " + arr[0]);
    }
}
