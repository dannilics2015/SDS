package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {

    private long wallTime;
    private int numberRequests;
    private int successfulRequests;
    private Map<Long, Double> latency;
    private int failedRequest;
    private List<MyRecord> failedRequestDetail;
    private Map<Long, Double> requestsPerSecond;


    public Result() {
        wallTime = 0;
        numberRequests = 0;
        successfulRequests = 0;
        latency = new HashMap<>();
        failedRequest = 0;
        failedRequestDetail = new ArrayList<>();
        requestsPerSecond = new HashMap<>();
    }

    public void addNumberRequest() {
        this.setNumberRequests(this.getNumberRequests() + 1);
    }

    public void addSuccessfulRequest() {
        this.setSuccessfulRequests(this.getSuccessfulRequests() + 1);
    }

    public void addFailedRequest() {
        this.setFailedRequest(this.getFailedRequest() + 1);
    }

    public void addFailedRequestDetail(MyRecord myRecord) {
        this.getFailedRequestDetail().add(myRecord);
        this.setFailedRequestDetail(this.getFailedRequestDetail());
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

    public Map<Long, Double> getLatency() {
        return latency;
    }

    public void setLatency(Map<Long, Double> latency) {
        this.latency = latency;
    }

    public int getFailedRequest() {
        return failedRequest;
    }

    public void setFailedRequest(int failedRequest) {
        this.failedRequest = failedRequest;
    }

    public List<MyRecord> getFailedRequestDetail() {
        return failedRequestDetail;
    }

    public void setFailedRequestDetail(List<MyRecord> failedRequestDetail) {
        this.failedRequestDetail = failedRequestDetail;
    }

    public Map<Long, Double> getRequestsPerSecond() {
        return requestsPerSecond;
    }

    public void setRequestsPerSecond(Map<Long, Double> requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
    }
}
