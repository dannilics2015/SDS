package com.programmerscuriosity;

import com.google.common.collect.Queues;
import model.MyRecord;
import model.Result;
import utility.Calculator;
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
//        for (int i = 0; i < recordCounts / iterationNumber; i++) {
//            List<MyRecord> myRecordList = new LinkedList<>();
//            Queues.drain(blockingQueue, myRecordList, iterationNumber, 10000, MILLISECONDS);
//            postRecordTasks.add(new PostRecordTask(iterationNumber, ip, myRecordList));
//        }
//        // GET
        for (int i = 0; i < skierCounts / iterationNumber; i++) {
            System.out.print("hi");
            getVertTasks.add(new GetVertTask(iterationNumber, ip, dayOfRecord,i * 100 + 1));
        }
        // invoke POST and collect results
//        try {
//            for (Future<Result> task: executor.invokeAll(postRecordTasks)) {
//                resultsFromPost.add(task);
//            }
//        } catch (InterruptedException e) {
//            LOGGER.warning(e.getMessage());
//        }
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
//        int numberRequestsFromPost = 0;
//        int successfulRequestFromPost = 0;
//        int failedRequestFromPost = 0;
//        Map<Long, Double> latencyListFromPost = new HashMap<>();
//        Map<Long, Double> requestsPerSecFromPOST = new HashMap<>();
//        for (Future<Result> result : resultsFromPost) {
//            try {
//                numberRequestsFromPost += result.get().getNumberRequests();
//                successfulRequestFromPost += result.get().getSuccessfulRequests();
//                latencyListFromPost.putAll(result.get().getLatency());
//                failedRequestFromPost += result.get().getFailedRequest();
//                requestsPerSecFromPOST.putAll(result.get().getRequestsPerSecond());
//            } catch (Exception e) {
//                System.out.print("Errors: " + e.getMessage());
//            }
//        }
//        statisticsFromPost.setLatency(latencyListFromPost);
//        statisticsFromPost.setNumberRequests(numberRequestsFromPost);
//        statisticsFromPost.setSuccessfulRequests(successfulRequestFromPost);
//        statisticsFromPost.setFailedRequest(failedRequestFromPost);
//
//        String postStatisticsMSG =
//                outputStatistics(collectionToList(latencyListFromPost.values()), "POST", statisticsFromPost);
//
//        LOGGER.info(postStatisticsMSG);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Collect GET statistics
        int numberRequestsFromGet = 0;
        int successfulRequestFromGet = 0;
        int failedRequestFromGet = 0;
        Map<Long, Double> latencyListFromGet = new HashMap<>();
        Map<Long, Double> requestsPerSecFromGET= new HashMap<>();
        for (Future<Result> result : resultsFromGet) {
            if(result.get() != null) {
                numberRequestsFromGet += result.get().getNumberRequests();
                successfulRequestFromGet += result.get().getSuccessfulRequests();
                latencyListFromGet.putAll(result.get().getLatency());
                failedRequestFromGet += result.get().getFailedRequest();
                requestsPerSecFromGET.putAll(result.get().getRequestsPerSecond());
            }
        }
        statisticsFromGet.setLatency(latencyListFromGet);
        statisticsFromGet.setNumberRequests(numberRequestsFromGet);
        statisticsFromGet.setSuccessfulRequests(successfulRequestFromGet);
        statisticsFromGet.setFailedRequest(failedRequestFromGet);

        //get mean & median
        String getStatisticsMSG =
                outputStatistics(collectionToList(latencyListFromGet.values()), "GET", statisticsFromGet);

        LOGGER.info(getStatisticsMSG);

//        LOGGER.info("POST all skier data & GET all vert data takes: " + wallTime + milliseconds);
        LOGGER.info("GET all vert data takes: " + wallTime + milliseconds);
        //create a chart based on the list of latency data
        System.out.println("Generating charts...");
//        DataChart dataChart = new DataChart("POST", "Time in Milliseconds",
//                "Latency", imageHeight, imageWidth,
//                "/Users/danni/Desktop/POSTLineChart.jpeg", latencyListFromPost);
//        dataChart.createChart();
//
//        DataChart dataChartOne = new DataChart("Requests per Second", "Time in Seconds",
//                "Requests", imageHeight, imageWidth,
//                "/Users/danni/Desktop/POSTRequestPerSecChart.jpeg", requestsPerSecFromPOST);
//        dataChartOne.createChart();

        DataChart dataChartForGet = new DataChart("GET", "Time in Milliseconds",
                "Latency", imageHeight, imageWidth,
                "/Users/danni/Desktop/GETLineChart.jpeg", latencyListFromGet);
        dataChartForGet.createChart();

        DataChart dataChartTwo = new DataChart("Requests per Second", "Time in Seconds",
                "Requests", imageHeight, imageWidth,
                "/Users/danni/Desktop/GETRequestPerSecChart.jpeg", requestsPerSecFromGET);
        dataChartTwo.createChart();

    }


    private static String outputStatistics(List<Double> unsortedDataList, String requestType, Result statistics) {
        //Calculate statics
        List<Double> sortedDataList = Calculator.sortLatency(unsortedDataList);
        Double meanLatencies = Calculator.getMean(sortedDataList);
        Double median = Calculator.getMedian(sortedDataList);
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("All threads completed..." + "\n")
                .append("Operation type: " + requestType)
                .append("number of requests sent: " + statistics.getNumberRequests()+ "\n")
                .append("number of successful requests sent: " + statistics.getSuccessfulRequests() + "\n")
                .append("The mean latencies for all requests is: " +
                        String.format("%.1f", meanLatencies) + milliseconds + "\n")
                .append("The median latencies for all GET requests is: " + median + milliseconds + "\n")
                .append("The 95th percentile latency is: " +
                        sortedDataList.get((int) (sortedDataList.size() * 0.95)) + milliseconds + "\n")
                .append("The 99th percentile latency is: " +
                        sortedDataList.get((int) (sortedDataList.size() * 0.99)) + milliseconds + "\n")
                .append("number of error requests is: " + statistics.getFailedRequest()).toString();
    }

    private static List<Double> collectionToList(Collection<Double> collection) {
        return new ArrayList<>(collection);
    }
}