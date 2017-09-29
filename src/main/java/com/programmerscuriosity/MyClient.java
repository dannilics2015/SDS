package com.programmerscuriosity;

import java.util.*;
import java.util.concurrent.*;

public class MyClient {
    //default argument values
    private static int NUMBER_THREADS = 100;
    private static int Number_ITERATION = 100;
    private static String IP = "http://34.215.97.129:8080";

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        if(args.length != 0 && args.length == 4) {
            NUMBER_THREADS = Integer.valueOf(args[0]);
            Number_ITERATION = Integer.valueOf(args[1]);
            IP = args[2].concat(":").concat(args[3]);
        }

        System.out.println("Start rolling ...");
        System.out.println("IP address of server is: " + "34.215.97.129");
        System.out.println("Listening on port: " + "8080");
        System.out.println("Number of Thread: " + NUMBER_THREADS);
        System.out.println("Number of Iteration: " + Number_ITERATION);

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_THREADS);
        Result statistics = new Result();
        List<Future<Result>> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_THREADS; i++) {
            Future<Result> task = executor.submit(new MyTask(Number_ITERATION, IP));
            results.add(task);
        }

        executor.shutdown();
        while(!executor.isTerminated()) {

        }
        long stopTime = System.currentTimeMillis();
        long wallTime = stopTime - startTime;


        int numberRequests = 0;
        int successfulRequest = 0;
        List<Double> latencyList = new ArrayList<>();

        for (Future<Result> result : results) {
            numberRequests += result.get().getNumberRequests();
            successfulRequest += result.get().getSuccessfulRequests();
            latencyList.addAll(result.get().getLatency());
        }
        for(Double d: latencyList) {
            System.out.println("latency is: " + d);
        }
        statistics.setLatency(latencyList);
        statistics.setNumberRequests(numberRequests);
        statistics.setSuccessfulRequests(successfulRequest);


        System.out.println("Number of requests is: " + numberRequests);
        System.out.println("Number of successful requests is: " + successfulRequest);
        System.out.println("Number of threads is: " + NUMBER_THREADS);
        System.out.println("Time for all threads to run takes: " + wallTime + " milliseconds");

        //get mean & median
        Double meanLatencies = latencyList.stream().mapToDouble(l -> l).average().getAsDouble();
        System.out.println("The mean latencies for all requests is: " + meanLatencies + " milliseconds");
        Double median = getMedian(latencyList);
        System.out.println("The median latencies for all requests is: " + median + " milliseconds");

        //get 99th & 95th percentage
        System.out.println("The 95th percentile latency is: " + latencyList.get((int) (latencyList.size() * 0.95)) + " milliseconds");
        System.out.println("The 99th percentile latency is: " + latencyList.get((int) (latencyList.size() * 0.99)) + " milliseconds");
    }

    /**
     * Get the median number, given a list
     * @param latencyList a list stores all the latency data
     * @return a double
     */
    private static Double getMedian(List<Double> latencyList) {
        Collections.sort(latencyList);
        if (latencyList.size() % 2 == 0) {
            return (latencyList.get(latencyList.size() / 2 - 1) + latencyList.get(latencyList.size() / 2)) / 2.0;
        }
        return latencyList.get(latencyList.size()) / 2.0;
    }

}