package ProducerConsumerWithFile;


import java.io.*;
import java.util.concurrent.Semaphore;



class MyFile {

    public void add(int x) {
        try {
            File f = new File("./db.txt");
            FileWriter fw = new FileWriter(f, true);
            PrintWriter pw = new PrintWriter(fw, true);
            pw.print(x);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(int x) {
        try {
            File f = new File("./db.txt");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String s = br.readLine();
            PrintWriter pw = new PrintWriter(f);
            s = s.substring(0, s.length());
            pw.print(s);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

class Producer extends Thread {

    MyFile list = new MyFile();
    Semaphore prodSem, consSem, bSem;
    int totalOp;

    public Producer(Semaphore prodSem, Semaphore consSem, Semaphore bSem, MyFile list, int totalOp){
        this.prodSem = prodSem;
        this.consSem = consSem;
        this.bSem = bSem;
        this.list = list;
        this.totalOp = totalOp;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalOp; i++) {
            try {
                this.prodSem.acquire();
                this.bSem.acquire();
                this.list.add(1);
//                System.out.println("Item produced");
                this.bSem.release();
                this.consSem.release();
//                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {

    MyFile list = new MyFile();
    Semaphore prodSem, consSem, bSem;
    int totalOp;

    public Consumer(Semaphore prodSem, Semaphore consSem, Semaphore bSem, MyFile list, int totalOp){
        this.prodSem = prodSem;
        this.consSem = consSem;
        this.bSem = bSem;
        this.list = list;
        this.totalOp = totalOp;
    }

    @Override
    public void run() {
        for (int i = 0; i < totalOp; i++) {
            try {
                this.consSem.acquire();
                this.bSem.acquire();
                this.list.remove(0);
//                System.out.println("Item consumed");
                this.bSem.release();
                this.prodSem.release();
//                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class main {

    public static void main(String[] args) {
        int[] totalOps = {100, 500, 1000, 2000, 5000};
        int[] threadNos = {1, 2, 5, 10, 20, 50, 100};
        long t;

        for (int threadNo: threadNos) {
            System.out.print(threadNo + " Threads: \t");
            for (int totalOp: totalOps) {
                t = main.start(threadNo, totalOp);
                System.out.print(t + "ms\t");
            }
            System.out.println();
        }

    }

    public static long start(int threadNo, int totalOp) {

        long startTime = System.nanoTime();

        Semaphore prodSem = new Semaphore(threadNo);
        Semaphore consSem = new Semaphore(0);
        Semaphore bSem = new Semaphore(1);
        MyFile list = new MyFile();
        Producer[] producers = new Producer[threadNo];
        for (int i = 0; i < threadNo; i++) {
            producers[i] = new Producer(prodSem, consSem, bSem, list, totalOp/threadNo);
            producers[i].start();
        }

        Consumer[] consumers = new Consumer[threadNo];
        for (int i = 0; i < threadNo; i++) {
            consumers[i] = new Consumer(prodSem, consSem, bSem, list, totalOp/threadNo);
            consumers[i].start();
        }


        try {
            for(Producer p : producers) p.join();
            for(Consumer c : consumers) c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        return totalTime/1000000;
    }
}

/*
Result:

1 Threads: 	1048ms	4352ms	8597ms	17492ms	43113ms
2 Threads: 	903ms	4234ms	8781ms	17521ms	43697ms
5 Threads: 	889ms	4753ms	10036ms	18846ms	43914ms
10 Threads: 	871ms	4381ms	8821ms	17483ms	44011ms
20 Threads: 	876ms	4734ms	8987ms	17861ms	45455ms
50 Threads: 	941ms	4536ms	9109ms	18295ms	45631ms
100 Threads: 	935ms	5540ms	9803ms	20159ms	49062ms
 */


