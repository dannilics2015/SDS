package com.programmerscuriosity;

import com.google.common.collect.Queues;
import model.MyRecord;
import model.Result;
import utility.DataChart;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utility.DataVariables.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MyClient {

    private static BlockingQueue<MyRecord> blockingQueue = new LinkedBlockingQueue<>();
    private final static Logger LOGGER = Logger.getLogger(MyClient.class.getName());

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        //read the input args if there is any. exp: thread_number, file_name, ip, port;
        // otherwise default values are used
        if (args.length != 0 && args.length == 4) {
            threadNumber = Integer.valueOf(args[0]);
            fileName = args[1];
            ipAddress = args[2];
            port = args[3];
            ip = args[2].concat(":").concat(args[3]);
        }

        LOGGER.log(Level.INFO, "Start rolling ......" + "\n" +
                "Contacting server at IP address: " + ipAddress + "\n" +
                "Listening on port: " + port + "\n" +
                "Numbers of threads: " + threadNumber + "\n" +
                "Numbers of iteration: " + iterationNumber);

        LOGGER.log(Level.FINE,"Starting a thread to read the file...");
        new Thread(new MyFileReader(blockingQueue, fileName)).start();

        ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
        Result statisticsFromPost = new Result();
        Result statisticsFromGet = new Result();
        List<Future<Result>> resultsFromPost = new ArrayList<>();
        List<Future<Result>> resultsFromGet = new ArrayList<>();
        List<PostRecordTask> postRecordTasks = new ArrayList<>();
        List<GetVertTask> getVertTasks = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        LOGGER.info("Starting at time: " + System.currentTimeMillis());

        // POST
        for (int i = 0; i < recordCounts / iterationNumber; i++) {
            List<MyRecord> myRecordList = new LinkedList<>();
            Queues.drain(blockingQueue, myRecordList, iterationNumber, 10000, MILLISECONDS);
            postRecordTasks.add(new PostRecordTask(iterationNumber, ip, myRecordList));
        }
        // GET
        for (int i = 0; i < skierCounts / iterationNumber; i++) {
            getVertTasks.add(new GetVertTask(iterationNumber, ip, dayOfRecord,i * 100 + 1));
        }
        // invoke POST and collect results
        try {
            for (Future<Result> task: executor.invokeAll(postRecordTasks)) {
                resultsFromPost.add(task);
            }
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
        // invoke GET and collect results
        try {
            for (Future<Result> task: executor.invokeAll(getVertTasks)) {
                resultsFromGet.add(task);
            }
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        }
        executor.shutdown();
        while(!executor.isTerminated()){}

        long stopTime = System.currentTimeMillis();
        long wallTime = stopTime - startTime;

        //Collect POST statistics
        int numberRequestsFromPost = 0;
        int successfulRequestFromPost = 0;
        Map<Long, Long> latencyListFromPost = new HashMap<>();
        for (Future<Result> result : resultsFromPost) {
            numberRequestsFromPost += result.get().getNumberRequests();
            successfulRequestFromPost += result.get().getSuccessfulRequests();
            latencyListFromPost.putAll(result.get().getLatency());
        }
        statisticsFromPost.setLatency(latencyListFromPost);
        statisticsFromPost.setNumberRequests(numberRequestsFromPost);
        statisticsFromPost.setSuccessfulRequests(successfulRequestFromPost);

        //get mean & median
        OptionalDouble meanLatenciesFromPost = latencyListFromPost.values().stream().mapToDouble(l -> l).average();
        List<Object> sortedLatencyListFromPost = sortLatency(latencyListFromPost.values());
        Long medianFromPost = getMedian(sortedLatencyListFromPost);

        LOGGER.info("Processed number of records: " + recordCounts + "\n" +
                "number of POST requests sent: " + statisticsFromPost.getNumberRequests()+ "\n" +
                "number of successful POST requests sent: " + statisticsFromPost.getSuccessfulRequests() + "\n" +
                "The mean latencies for all POSt requests is: " +
                String.format("%.1f", meanLatenciesFromPost.getAsDouble()) + milliseconds + "\n" +
                "The median latencies for all POST requests is: " + medianFromPost + milliseconds + "\n" +
                "The 95th percentile latency is: " +
                sortedLatencyListFromPost.get((int) (sortedLatencyListFromPost.size() * 0.95)) + milliseconds + "\n" +
                "The 99th percentile latency is: " +
                sortedLatencyListFromPost.get((int) (sortedLatencyListFromPost.size() * 0.99)) + milliseconds);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Collect GET statistics
        int numberRequestsFromGet = 0;
        int successfulRequestFromGet = 0;
        Map<Long, Long> latencyListFromGet = new HashMap<>();
        for (Future<Result> result : resultsFromGet) {
            numberRequestsFromGet += result.get().getNumberRequests();
            successfulRequestFromGet += result.get().getSuccessfulRequests();
            latencyListFromGet.putAll(result.get().getLatency());
        }
        statisticsFromGet.setLatency(latencyListFromGet);
        statisticsFromGet.setNumberRequests(numberRequestsFromGet);
        statisticsFromGet.setSuccessfulRequests(successfulRequestFromGet);

        //get mean & median
        OptionalDouble meanLatenciesFromGet = latencyListFromGet.values().stream().mapToDouble(l -> l).average();
        List<Object> sortedLatencyListFromGet = sortLatency(latencyListFromGet.values());
        Long medianFromGet = getMedian(sortedLatencyListFromGet);

        LOGGER.info("All threads completed..." + "\n" +
                "number of GET requests sent: " + statisticsFromGet.getNumberRequests()+ "\n" +
                "number of successful GET requests sent: " + statisticsFromGet.getSuccessfulRequests() + "\n" +
                "The mean latencies for all GET requests is: " +
                String.format("%.1f", meanLatenciesFromGet.getAsDouble()) + milliseconds + "\n" +
                "The median latencies for all GET requests is: " + medianFromGet + milliseconds + "\n" +
                "The 95th percentile latency is: " +
                sortedLatencyListFromGet.get((int) (sortedLatencyListFromGet.size() * 0.95)) + milliseconds + "\n" +
                "The 99th percentile latency is: " +
                sortedLatencyListFromGet.get((int) (sortedLatencyListFromGet.size() * 0.99)) + milliseconds);

        LOGGER.info("POST all skier data & GET all vert data takes: " + wallTime + milliseconds);

        //create a chart based on the list of latency data
        System.out.println("Generating charts...");
        DataChart dataChart = new DataChart("POST", xAxis,
                yAxis, imageHeight, imageWidth,
                "/Users/danni/Desktop/POSTLineChart.jpeg", latencyListFromPost);
        dataChart.createChart();

        DataChart dataChartForGet = new DataChart("GET", xAxis,
                yAxis, imageHeight, imageWidth,
                "/Users/danni/Desktop/GETLineChart.jpeg", latencyListFromGet);
        dataChartForGet.createChart();

    }


    /**
     * Get the median number, given a list
     *
     * @param data a list stores all the latency data
     * @return a double
     */
    private static Long getMedian(List<Object> data) {
        if (data.size() % 2 == 0) {
            return ((Long)data.get((data.size() / 2 - 1)) + (Long)data.get((data.size() / 2))) / 2;
        }
        return (Long)data.get(data.size() / 2);
    }

    private static List<Object> sortLatency(Collection<Long> latencyList) {
        Object[] data = latencyList.toArray();
        Arrays.sort(data);
        return Arrays.asList(data);
    }
}