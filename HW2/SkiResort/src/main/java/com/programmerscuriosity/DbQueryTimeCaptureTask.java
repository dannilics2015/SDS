package com.programmerscuriosity;

import java.util.LinkedList;
import java.util.List;

public class DbQueryTimeCaptureTask {

    protected static List<Double> dbQueryLatencyList = new LinkedList<>();

    public void addLatency (Double latency) {
        dbQueryLatencyList.add(latency);
    }

    public List<Double> getLatencyList() {
        return dbQueryLatencyList;
    }
}
