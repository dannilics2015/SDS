package com.programmerscuriosity;

import java.util.LinkedList;
import java.util.List;

public class DbQueryTimeCaptureTask {

    protected static List<Double> dbPOSTQueryLatencyList = new LinkedList<>();
    protected static List<Double> dbGETQueryLatencyList = new LinkedList<>();
    protected static Integer dbQueryErrors = 0;
    protected static Integer dbQueryErrorsFromGET = 0;

    public void addLatency (Double latency) {
        dbPOSTQueryLatencyList.add(latency);
    }

    public void addGETLatency(Double latency) {
        dbGETQueryLatencyList.add(latency);
    }

    public List<Double> getDbPOSTQueryLatencyList() {
        return dbPOSTQueryLatencyList;
    }

    public List<Double> getDbGETQueryLatencyList() {
        return dbGETQueryLatencyList;
    }

    public Integer getDbQueryErrors() {
        return dbQueryErrors;
    }

    public Integer getDbQueryErrorsFromGET() {
        return dbQueryErrorsFromGET;
    }

}
