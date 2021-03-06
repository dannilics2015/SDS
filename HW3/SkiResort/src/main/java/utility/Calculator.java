package utility;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Calculator {


    public static List<Double> sortLatency(List<Double> latencyList) {
        Collections.sort(latencyList);
        return latencyList;
    }

    public static List<Double> toList(ConcurrentLinkedQueue queue) {
        List<Double> list = new LinkedList<>();
        Iterator iterator = queue.iterator();
        while(iterator.hasNext()) {
            list.add((Double)iterator.next());
        }
        return list;
    }

    /**
     * Get the median number, given a list
     *
     * @param latencyList a list stores all the latency data
     * @return a double
     */
    public static Double getMedian(List<Double> latencyList) {
        List<Double> data = sortLatency(latencyList);
        if (data.size() % 2 == 0) {
            return (data.get((data.size() / 2 - 1)) + data.get((data.size() / 2))) / 2;
        }
        return data.get(data.size() / 2);
    }

    public static OptionalDouble getMean(Map<Long, Long> latencyList) {
        return latencyList.values().stream().mapToDouble(l -> l).average();
    }

    public static Double getMean(List<Double> dbQueryLatencyList) {
        double sum = 0;
        for (Double d: dbQueryLatencyList) {
            sum += d;
        }
        return sum/dbQueryLatencyList.size();
    }

    public static Double get95 (List<Double> data) {
        return data.get((int) (data.size() * 0.95));
    }

    public static Double get99 (List<Double> data) {
        return data.get((int) (data.size() * 0.99));
    }

}
