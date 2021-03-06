package com.programmerscuriosity;

import model.MyRecord;
import model.VertData;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import utility.Calculator;
import utility.DataVariables;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static utility.DataVariables.*;


@Path("myresource")
public class MyResource {

    private CacheTask cacheTask = new CacheTask();
    private DbQueryPOSTCaptureTask dbQueryPOSTCaptureTask = new DbQueryPOSTCaptureTask();
    private DbQueryGETCaptureTask dbQueryGETCaptureTask = new DbQueryGETCaptureTask();

    /**
     * To retrieve a vert record
     * @param skierID a skier ID
     * @param dayNum a day number
     * @return a response
     */
    @GET
    @Path("/myvert/{skierID}/{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyVert(@PathParam("skierID") String skierID, @PathParam("dayNum") String dayNum) {
        VertData myVert = new VertData();
        List<String> myRecords = new ArrayList<>();
        try {
            Jedis jedis = new Jedis(jedisHost, jedisPort, 600000);
//            JedisCluster jedisCluster = new JedisCluster(new HostAndPort(DataVariables.jedisHost, DataVariables.jedisPort));
            jedis.select(Integer.valueOf(dayNum));
            double start = System.nanoTime();
            myRecords = jedis.lrange(skierID, 0, -1);
//            myRecords = jedisCluster.lrange(skierID, 0, -1);
            dbQueryGETCaptureTask.addGETresponseTime((System.nanoTime() - start) / 1000000);
            jedis.close();
        } catch(Exception e) {
            synchronized (this) {
                DbQueryGETCaptureTask.dbQueryErrorsFromGET++;
            }
            e.printStackTrace();
        }
        //calculate the vertical
        int vertical = 0;
        for (String str: myRecords) {
            String[] record = str.split(splitStr);
            int lift = Integer.valueOf(record[2]);
            if (lift > 10) {
                vertical += 200 + ((lift - 11) / 10 + 1) * 100;
            } else vertical += 200;
        }
        myVert.setTotalVertical(vertical);
        myVert.setNumOfRides(myRecords.size());
        GenericEntity<VertData> entity = new GenericEntity<VertData>(myVert, VertData.class);
        return Response.ok().entity(entity).build();
    }

    /**
     * Post a record
     * @param myRecord my record
     * @return a Response
     */
    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postRecord(MyRecord myRecord) throws URISyntaxException {
        cacheTask.add(myRecord);
        String result = "Record created" + myRecord.toString();
        return Response.status(201).entity(result).build();
    }

    @GET
    @Path("/myserver/dbquerytime/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDbQTMetrics() {
        //POST
        List<Double> latencyList = Calculator.sortLatency(Calculator.toList(dbQueryPOSTCaptureTask.getQueue()));
        Double median = Calculator.getMedian(latencyList);
        Double mean = Calculator.getMean(latencyList);
        Double ninetyFiveP = Calculator.get95(latencyList);
        Double ninetyNineP = Calculator.get99(latencyList);
        //GET
        List<Double> latencyListGET = Calculator.sortLatency(Calculator.toList(dbQueryGETCaptureTask.getQueue()));
        Double medianForGET = Calculator.getMedian(latencyListGET);
        Double meanForGET = Calculator.getMean(latencyListGET);
        Double ninetyFivePForGET = Calculator.get95(latencyListGET);
        Double ninetyNinePForGET = Calculator.get99(latencyListGET);
        String metrics = "Database Query Time Metrics\n" +
                "POST Method\n" +
                "Mean: " + mean + milliseconds + "\n" +
                "Median: " + median + milliseconds + "\n" +
                "95 percentile: " + ninetyFiveP + milliseconds+ "\n" +
                "99 percentile: " + ninetyNineP + milliseconds+ "\n" +
                "Numbers of errors: " + dbQueryPOSTCaptureTask.getDbQueryErrors() + "\n\n" +
                "GET Method\n" +
                "Mean: " + meanForGET + milliseconds + "\n" +
                "Median: " + medianForGET + milliseconds + "\n" +
                "95 percentile: " + ninetyFivePForGET + milliseconds+ "\n" +
                "99 percentile: " + ninetyNinePForGET + milliseconds+ "\n" +
                "Numbers of errors: " + dbQueryGETCaptureTask.getDbQueryErrorsFromGET() + "\n";
        return Response.status(201).entity(metrics).build();
    }

    @DELETE
    @Path("/myserver/dbquerytime/metrics/clear")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearMetrics() {
        dbQueryPOSTCaptureTask.getQueue().clear();
        dbQueryGETCaptureTask.getQueue().clear();
        String result = "Clear buffered statistics!";
        return Response.status(201).entity(result).build();
    }
}