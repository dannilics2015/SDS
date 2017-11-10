package com.programmerscuriosity;

import model.MyRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static utility.DataVariables.splitStr;


public class MyFileReader implements Runnable{

    private BlockingQueue<MyRecord> blockingQueue = new LinkedBlockingQueue<>();
    private String fileName;

    public MyFileReader(BlockingQueue<MyRecord> blockingQueue, String fileName) {
        this.blockingQueue = blockingQueue;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        String line = "";
        try {
            bufferedReader = new BufferedReader(
                    new FileReader(fileName));
//            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] record = line.split(splitStr);
                MyRecord myRecord = new MyRecord();
                myRecord.setResortID(record[0]);
                myRecord.setDayNum(record[1]);
                myRecord.setSkierID(record[2]);
                myRecord.setLiftID(record[3]);
                myRecord.setTimestamp(record[4]);
                blockingQueue.add(myRecord);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
