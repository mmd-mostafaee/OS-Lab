package ProducerConsumerWithoutSemaphore.ProducerConsumer;

import java.io.*;
import java.util.ArrayList;

class Producer extends Thread {

    ArrayList list = new ArrayList<Integer>();

    public Producer(ArrayList list){
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.list.add(1);
                System.out.println("Item produced");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {

    ArrayList list = new ArrayList<Integer>();

    public Consumer(ArrayList list){
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.list.remove(0);
                System.out.println("Item consumed");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        ArrayList list = new ArrayList<Integer>();

        Producer[] producers = new Producer[3];
        for (int i = 0; i < 3; i++) {
            producers[i] = new Producer(list);
            producers[i].start();
        }

        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < 3; i++) {
            consumers[i] = new Consumer(list);
            consumers[i].start();
        }


        try {
            for(Producer p : producers) p.join();
            for(Consumer c : consumers) c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread: main: end");
    }
}
