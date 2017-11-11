package com.programmerscuriosity;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DbQueryPOSTCaptureTask {

//    protected static List<Double> dbPOSTQueryLatencyList = new LinkedList<>();
//    protected static List<Double> dbGETQueryLatencyList = new LinkedList<>();
    protected static Integer dbQueryErrorsFromPOST = 0;


    public static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();

    public void addPOSTresponseTime(Double latency) {
        queue.offer(latency);
    }

    public ConcurrentLinkedQueue getQueue() {
        return queue;
    }

//    public void addLatency (Double latency) {
//        dbPOSTQueryLatencyList.add(latency);
//    }
//
//    public void addGETLatency(Double latency) {
//        dbGETQueryLatencyList.add(latency);
//    }

//    public List<Double> getDbPOSTQueryLatencyList() {
//        return dbPOSTQueryLatencyList;
//    }
//
//    public List<Double> getDbGETQueryLatencyList() {
//        return dbGETQueryLatencyList;
//    }

    public Integer getDbQueryErrors() {
        return dbQueryErrorsFromPOST;
    }


}
