package VectorMul;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class MulThread extends Thread{
    int[] firstArray;
    int[] secondArray;
    int[] resultArray;
    int id;
    int sum= 0;
    int threadNum;
    public MulThread(int id,int threadNum, int[] a, int[] b,int[] c){
        this.id=id;
        this.threadNum = threadNum;
        this.firstArray = a;
        this.secondArray = b;
        this.resultArray = c;
    }
    @Override
    public void run() {
        for(int i=id;i<firstArray.length ;i+=threadNum){
            sum += firstArray[i] * secondArray[i];
        }
        resultArray[id] = sum;
    }
}

public class main {
    public static void main(String[] args) {
        int[] threadNums = {1, 2, 5, 10, 20, 50, 100};
        int[] sizes = {10, 1000, 10000, 100000};
//        int threadNum = 10;
        long t;
        for (int threadNum: threadNums) {
            System.out.print(threadNum + " Threads: \t");
            for (int size: sizes) {
                t = main.mulVector(size, threadNum);
                System.out.print(t + " ms\t");
            }
            System.out.println();

        }

    }

    private static long mulVector(int size, int threadNum) {
        ArrayList<Thread> threads = new ArrayList<>();
        int[] firstArray = new int[size];
        int[] secondArray = new int[size];
        int[] resultArray = new int[threadNum];
        int result = 0;

        long startTime = System.nanoTime();

        for (int i = 0; i < size; i++) {
            firstArray[i] = ThreadLocalRandom.current().nextInt(6);
            secondArray[i] = ThreadLocalRandom.current().nextInt(6);
        }

        for (int i = 0; i < threadNum; i++) {
            threads.add(new MulThread(i,threadNum,firstArray,secondArray,resultArray));
        }
        for (int i = 0; i < threadNum; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < threadNum; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(int i=0;i<threadNum;i++){
            result += resultArray[i];
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        return totalTime/1000000;

    }
}


/*
Results:

1 Threads: 	1600ms	5925ms	12011ms	25072ms	62138ms
2 Threads: 	1232ms	6093ms	12099ms	25042ms	62265ms
5 Threads: 	1271ms	7503ms	12009ms	23709ms	59430ms
10 Threads: 	1378ms	6035ms	11827ms	23873ms	59851ms
20 Threads: 	1226ms	5887ms	12557ms	24226ms	60610ms
50 Threads: 	1180ms	6178ms	12255ms	24312ms	61327ms
100 Threads: 	1257ms	6279ms	12146ms	24299ms	63955ms
 */
