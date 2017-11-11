package com.programmerscuriosity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DbQueryGETCaptureTask {

    public static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    protected static Integer dbQueryErrorsFromGET = 0;

    public void addGETresponseTime(Double latency) {
        queue.offer(latency);
    }

    public ConcurrentLinkedQueue getQueue() {
        return queue;
    }

    public Integer getDbQueryErrorsFromGET() {
        return dbQueryErrorsFromGET;
    }

}
