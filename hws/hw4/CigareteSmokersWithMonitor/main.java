package CigareteSmokersWithMonitor;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Smoker extends Thread {

    int myItem;
    TableMonitor table;

    public Smoker(int item, TableMonitor table){
        this.myItem = item;
        this.table = table;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if(table.canCreateCigarette(this.myItem)) {
                    table.clean();
                    Thread.sleep(200);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Agent extends Thread {

    TableMonitor table;

    public Agent(TableMonitor table){
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(this.table.isClean()) {
                    int randNo = (ThreadLocalRandom.current().nextInt(1,4));
                    int x = 0, y = 0;
                    switch (randNo){
                        case 1:
                            x = 2;
                            y = 3;
                            break;
                        case 2:
                            x = 1;
                            y = 3;
                            break;
                        case 3:
                            x = 1;
                            y = 2;
                            break;
                        default:
                            System.out.println("Wrong Random Number: " + randNo);
                    }
                    table.put(x, y);
                    Thread.sleep(200);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}

class TableMonitor {
    private int[] tableItems = new int[2];
    public synchronized void put(int x, int y){
        this.tableItems[0] = x;
        this.tableItems[1] = y;
        System.out.println("Agent puts " + x + " and " + y + " on the table.");
    }

    public synchronized boolean isClean(){
        if(this.tableItems[0] == 0 && this.tableItems[1] == 0) return true;
        else return false;
    }

    public synchronized void clean(){
        this.tableItems[0] = 0;
        this.tableItems[1] = 0;
    }

    public synchronized boolean canCreateCigarette(int item){
        boolean flag = this.tableItems[0] + this.tableItems[1] + item == 6;
        if(flag) System.out.println("Smoker with item " + item + " is smoking...");
        return flag;
    }

}

public class main {

    public static void main(String[] args) {

        System.out.println("Thread: main: start");

        TableMonitor table = new TableMonitor();

        Agent agent = new Agent(table);
        agent.start();

        Smoker[] smokers = new Smoker[3];
        for (int i = 0; i < 3; i++) {
            smokers[i] = new Smoker(i+1, table);
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
