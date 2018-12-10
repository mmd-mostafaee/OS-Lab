package SleepingBarber;


import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Barber extends Thread {
    public boolean isSleep;

    public Barber(){
        isSleep = false;
    }
}

class Client extends Thread {

    int id;

    public Client(int id){
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Client enters the shop");

    }
}

class BarberShop {

//    private boolean isChairFull;
    private Client activeClient;

    private int waitingRoomClients;
    private int waitingRoomCapacity;

    private boolean isBarberSleep;

    public BarberShop(int waitingRoomCapacity){
        isChairFull = false;
        waitingRoomClients = 0;
        this.waitingRoomCapacity = waitingRoomCapacity;
    }

    public boolean checkTheShop(Client client){
        if(isBarberSleep) {
            isBarberSleep = false;
            return true;
        } else if(waitingRoomClients != waitingRoomCapacity) {
            waitingRoomClients++;
            return true;
        }
        return false;
    }

    public void cutMyHair(Client client){
        try {
            waitingRoomClients--;
            System.out.println("Cutting Client No. " + client.id + " hairs");
            Thread.sleep(700);
            System.out.println("Hair cutting is Done");
            if(waitingRoomClients == 0) isBarberSleep = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

public class main {

    static Semaphore chairSemaphore = new Semaphore(1);
    static Semaphore waitingRoomSemaphore = new Semaphore(4);
    static Semaphore checkingSemaphore = new Semaphore(1);

    public static void main(String[] args) {

        System.out.println("Thread: main: start");


        for (int j = 0; j < testLimit; j++) {

            int[] arr = new int[1];
            arr[0] = 0;

            int threadNum = 2;
            MyThread[] threadArray = new MyThread[threadNum];

            for (int i = 0; i < threadNum; i++) {
                threadArray[i] = new MyThread(i, arr, threadNum, s);
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
