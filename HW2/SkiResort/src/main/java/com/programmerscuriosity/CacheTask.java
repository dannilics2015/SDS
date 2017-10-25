package com.programmerscuriosity;

import com.google.common.collect.Queues;
import model.MyRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static utility.DataVariables.maxBatchSize;

public class CacheTask implements Runnable{

    protected static BlockingQueue<MyRecord> myRecordBlockingQueue = new LinkedBlockingQueue<>();


    public void add (MyRecord myRecord) {
        myRecordBlockingQueue.add(myRecord);
    }

    public List<MyRecord> drainToList(BlockingQueue<MyRecord> m) {
        List<MyRecord> list = new ArrayList<>();
        try {
            Queues.drain(m, list, maxBatchSize, 30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       return list;
    }

    @Override
    public void run() {
        while(true) {
            List<MyRecord> list = drainToList(myRecordBlockingQueue);
            if(list.size() != 0) {
                new Thread(new BatchInsertTask(list)).start();
            }
        }
    }
}
