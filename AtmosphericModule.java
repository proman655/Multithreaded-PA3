// Pedro Roman
// COP4520
// PA3, Problem 2

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

public class AtmosphericModule extends Thread {
    public static ArrayList<Sensor> threads = new ArrayList<>();
    public ArrayList<Integer> recordings = new ArrayList<>();
    public PriorityQueue<Integer> minFive = new PriorityQueue<>();
    public PriorityQueue<Integer> maxFive = new PriorityQueue<>(Collections.reverseOrder());
    ReentrantLock lock = new ReentrantLock();

    int numOfThreads;
    int hoursToIter, hours;

    AtmosphericModule(int hoursToIter)
    {
        this.numOfThreads = 8;
        this.hoursToIter = hoursToIter * this.numOfThreads * 60;
        this.hours = hoursToIter;
    }

    Sensor getThread(int index)
    {
        return threads.get(index - 1);
    }

    void printReport(int hour)
    {
        System.out.println("\n======== Sensory Report After " + hour + " hours ========\n");
        System.out.println("Top 5 temperatures: " + printMaxFive());
        System.out.println("Min 5 temperatures: " + printMinFive());
        this.recordings.clear();
    }

// david
    ArrayList<Integer> printMaxFive()
    {
        ArrayList<Integer> topFive = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            int num = maxFive.poll();
            if (topFive.contains(num))
            {
                i--;
                continue;
            }

            topFive.add(num);
        }

        maxFive.clear();
        return topFive;
    }

    ArrayList<Integer> printMinFive()
    {
        ArrayList<Integer> topFive = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            int num = minFive.poll();
            if (topFive.contains(num))
            {
                i--;
                continue;
            }

            topFive.add(num);
        }

        minFive.clear();
        return topFive;
    }

// pedros
    ArrayList<Integer> getTopFive(ArrayList<Integer> recordings) 
    {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>();
        ArrayList<Integer> fresh = new ArrayList<Integer>(); 

        for(Integer element : recordings){
            if(!fresh.contains(element)) {
                fresh.add(element);
            }
        }

        fresh.forEach(number -> {
            maxHeap.add(number);

            if(maxHeap.size() > 5){
                maxHeap.poll();
            }
        });

        ArrayList<Integer> top5List = new ArrayList<>(maxHeap);
        Collections.reverse(top5List);

        return top5List;
    }

    
    void runSensors(AtmosphericModule mainThread) throws InterruptedException {
        // Threads are created, added to arraylist, and run
        for (int i = 1; i <= this.numOfThreads; i++)
        {
            if (i == 1)
                threads.add(new Sensor(i, mainThread, true));
            else
            threads.add(new Sensor(i, mainThread, false));
        }

        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).start();

        for (int i = 0; i < mainThread.numOfThreads; i++)
            threads.get(i).join();

        System.out.println(this.recordings.size());
    }

    public static void main(String[] args) throws InterruptedException
    {
        //Scanner object
        Scanner input = new Scanner(System.in);

        System.out.print("Enter how many hours you wish to simulate [MAX 45 hours, more takes longer than 30s]: ");

        while(!input.hasNextInt())
        {
            input.nextLine();
            System.out.print("Please input an integer value");
        }

        int hours = input.nextInt();

        input.close();

        if (hours <= 0 || hours > 45)
        {
            System.out.println("Must be more than 0 hours");
            return;
        }
        // Instance of mainthread created
        AtmosphericModule mainThread = new AtmosphericModule(hours);

        final long startTime = System.currentTimeMillis();

        mainThread.runSensors(mainThread);

        final long endTime = System.currentTimeMillis();
        final long runTime = endTime - startTime;
        System.out.println("Execution time: " + runTime + " ms");
    }
}

class Sensor extends Thread {
    static AtomicInteger iterations = new AtomicInteger();
    static AtomicBoolean hourPassed = new AtomicBoolean();
    int threadNum, hour = 1;
    boolean checked;
    boolean isReporter = false;
    AtmosphericModule mainThread;

    Sensor(int threadNum, AtmosphericModule mainThread, boolean isReporter)
    {
        this.threadNum = threadNum;
        this.mainThread = mainThread;
        this.isReporter = isReporter;
        this.checked = false;
    }

    int genRandTemp() {
        return (int) (Math.floor(Math.random() * (70 - (-100) + 1) + (-100)));
    }

    boolean checkThreads()
    {
        for (int i = 0; i < 8; i++)
            if (!this.mainThread.getThread(this.threadNum).checked)
                return false;

        return true;
    }

    @Override
    public void run()
    {
        while (iterations.get() < mainThread.hoursToIter)
        {
            // System.out.println("minutes.get " + minutes.get());
            // System.out.println("mainThread.hoursToIter " +  mainThread.hoursToIter);

            while (!checkThreads())
            {
                if (!this.checked) 
                {
                    int rand = genRandTemp();
                    this.mainThread.lock.lock();
                    try 
                    {
                        this.mainThread.recordings.add(rand);
                        this.mainThread.maxFive.add(rand);
                        this.mainThread.minFive.add(rand);
                        this.checked = true;
                        // System.out.println("Thread " + this.threadNum + " has added " + rand + " to the list");
                    }
                    finally 
                    {
                        this.mainThread.lock.unlock();
                    }
                }
            }

            this.checked = false;
            iterations.getAndIncrement();

            if (iterations.get() == 480) System.out.println("woo");
            if (iterations.get()/this.hour == 480)
                hourPassed.set(true);

            try 
            {
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //System.out.println(this.mainThread.recordings.size());
            if (this.isReporter && hourPassed.get())
            {
                //System.out.println(mainThread.getTopFive(mainThread.recordings));
                this.mainThread.lock.lock();
                try
                {
                    this.mainThread.printReport(this.hour++);
                    hourPassed.set(false);
                }
                finally
                {

                    this.mainThread.lock.unlock();
                }   
            }

            try 
            {
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}