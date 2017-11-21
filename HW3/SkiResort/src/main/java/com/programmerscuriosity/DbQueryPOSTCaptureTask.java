package com.programmerscuriosity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DbQueryPOSTCaptureTask {

    protected static Integer dbQueryErrorsFromPOST = 0;
    public static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();

    public void addPOSTresponseTime(Double latency) {
        queue.offer(latency);
    }

    public ConcurrentLinkedQueue getQueue() {
        return queue;
    }

    public Integer getDbQueryErrors() {
        return dbQueryErrorsFromPOST;
    }


}
