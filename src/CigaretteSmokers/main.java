package CigaretteSmokers;


import javax.swing.plaf.basic.BasicTreeUI;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Smoker extends Thread {

    int myItem;
    int[] tableItems;
    Semaphore table;

    public Smoker(int item, Semaphore table, int[] tableItems){
        this.myItem = item;
        this.table = table;
        this.tableItems = tableItems;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.table.acquire();
                if(this.tableItems[0] + this.tableItems[1] + this.myItem == 6) {
                    this.tableItems[0] = 0;
                    this.tableItems[1] = 0;
                    this.table.release();
                    System.out.println("Smoker with item " + this.myItem + " is smoking...");
                    Thread.sleep(200);
                } else {
                    this.table.release();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Agent extends Thread {

    int[] tableItems;
    Semaphore table;

    public Agent(Semaphore table, int[] tableItems){
        this.table = table;
        this.tableItems = tableItems;
    }

    @Override
    public void run() {
        while (true) {
            try {

                this.table.acquire();
                if( tableItems[0] == 0 && tableItems[1] == 0) {
                    int randNo = (ThreadLocalRandom.current().nextInt(1,4));
                    switch (randNo){
                        case 1:
                            tableItems[0] = 2;
                            tableItems[1] = 3;
                            break;
                        case 2:
                            tableItems[0] = 1;
                            tableItems[1] = 3;
                            break;
                        case 3:
                            tableItems[0] = 1;
                            tableItems[1] = 2;
                            break;
                        default:
                            System.out.println("Wrong Random Number: " + randNo);
                    }
                    Thread.sleep(200);
                    System.out.println("Agent puts " + tableItems[0] + " and " + tableItems[1] + " on the table.");
                }
                this.table.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        Semaphore table = new Semaphore(1);
        int[] tableItems = new int[2];

        Agent agent = new Agent(table, tableItems);
        agent.start();

        Smoker[] smokers = new Smoker[3];
        for (int i = 0; i < 3; i++) {
            smokers[i] = new Smoker(i+1, table, tableItems);
            smokers[i].start();
        }



        try {
            agent.join();
            for(Smoker s : smokers) s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread: main: end");
    }
}
