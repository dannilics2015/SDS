package model;

import java.util.HashMap;
import java.util.Map;

public class Result {

    private long wallTime;
    private int numberRequests;
    private int successfulRequests;
    private Map<Long, Long> latency;


    public Result() {
        wallTime = 0;
        numberRequests = 0;
        successfulRequests = 0;
        latency = new HashMap<>();
    }

    public void addNumberRequest() {
        this.setNumberRequests(this.getNumberRequests() + 1);
    }

    public void addSuccessfulRequest() {
        this.setSuccessfulRequests(this.getSuccessfulRequests() + 1);
    }

    public long getWallTime() {
        return wallTime;
    }

    public void setWallTime(long wallTime) {
        this.wallTime = wallTime;
    }

    public int getNumberRequests() {
        return numberRequests;
    }

    public int getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setNumberRequests(int numberRequests) {
        this.numberRequests = numberRequests;
    }

    public void setSuccessfulRequests(int successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public Map<Long, Long> getLatency() {
        return latency;
    }

    public void setLatency(Map<Long, Long> latency) {
        this.latency = latency;
    }

}
