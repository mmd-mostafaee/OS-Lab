package ProducerConsumer;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Producer extends Thread {

    ArrayList list = new ArrayList<Integer>();
    Semaphore prodSem, consSem, bSem;

    public Producer(Semaphore prodSem, Semaphore consSem, Semaphore bSem, ArrayList list){
        this.prodSem = prodSem;
        this.consSem = consSem;
        this.bSem = bSem;
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.prodSem.acquire();
                this.bSem.acquire();
                this.list.add(1);
                System.out.println("Item produced");
                this.bSem.release();
                this.consSem.release();
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {

    ArrayList list = new ArrayList<Integer>();
    Semaphore prodSem, consSem, bSem;

    public Consumer(Semaphore prodSem, Semaphore consSem, Semaphore bSem, ArrayList list){
        this.prodSem = prodSem;
        this.consSem = consSem;
        this.bSem = bSem;
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                this.consSem.acquire();
                this.bSem.acquire();
                this.list.remove(0);
                System.out.println("Item consumed");
                this.bSem.release();
                this.prodSem.release();
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

        Semaphore prodSem = new Semaphore(5);
        Semaphore consSem = new Semaphore(0);
        Semaphore bSem = new Semaphore(1);


        ArrayList list = new ArrayList<Integer>();

        Producer[] producers = new Producer[3];
        for (int i = 0; i < 3; i++) {
            producers[i] = new Producer(prodSem, consSem, bSem, list);
            producers[i].start();
        }

        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < 3; i++) {
            consumers[i] = new Consumer(prodSem, consSem, bSem, list);
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
