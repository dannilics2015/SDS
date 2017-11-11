package utility;

import java.util.*;

public class Calculator {


    public static List<Double> sortLatency(List<Double> latencyList) {
        Collections.sort(latencyList);
        return latencyList;
    }

    /**
     * Get the median number, given a list
     *
     * @param data a list stores all the latency data
     * @return a double
     */
    public static Double getMedian(List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get((data.size() / 2 - 1)) + data.get((data.size() / 2))) / 2;
        }
        return data.get(data.size() / 2);
    }

    public static Double getMean(List<Double> doubleList) {
        double sum = 0;
        for (Double d: doubleList) {
            sum += d;
        }
        return sum/doubleList.size();
    }

    public static Double get95 (List<Double> data) {
        return data.get((int) (data.size() * 0.95));
    }

    public static Double get99 (List<Double> data) {
        return data.get((int) (data.size() * 0.99));
    }

}
