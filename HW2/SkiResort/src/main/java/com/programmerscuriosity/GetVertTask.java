package com.programmerscuriosity;

import model.Result;
import model.VertRequest;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utility.DataVariables.GETURL;

public class GetVertTask implements Callable<Result> {

    private final static Logger LOGGER = Logger.getLogger(GetVertTask.class.getName());
    private int iterationTimes;
    private Result statistics;
    private String ipAddress;
    private String dayNum;
    private int begin;

    public GetVertTask(int iterationTimes, String ipAddress, String dayNum, int begin) {
        this.iterationTimes = iterationTimes;
        this.ipAddress = ipAddress;
        this.dayNum = dayNum;
        this.begin = begin;
        statistics = new Result();
    }

    //call GET method
    public Response callGetVert(WebTarget webTarget, VertRequest vertRequest) {
        Response response = null;
        try {
            String getURL = GETURL + "/" + vertRequest.getSkierID() + "/" + vertRequest.getDayNum();
            long threadStartTime = System.currentTimeMillis();
            response = webTarget.path(getURL).request().get();
            long threadEndTime = System.currentTimeMillis();
            long latency = threadEndTime - threadStartTime;
            statistics.getLatency().put(threadStartTime, latency);
            response.close();

        } catch (ProcessingException e) {
            LOGGER.warning("Error message: " + e.getMessage());
            LOGGER.warning("Stack trace: " + e.getStackTrace());
        } catch (OutOfMemoryError e) {
            LOGGER.warning("Error message: " + e.getMessage());
            LOGGER.warning("Stack trace: " + e.getStackTrace());
        }
        return response;
    }
    @Override
    public Result call() throws Exception {
        LOGGER.log(Level.FINE, "Calling GET...");
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        client.property(ClientProperties.CONNECT_TIMEOUT, 120000);
        WebTarget webTarget = client.target(ipAddress);
        //calling GET
        for (int i = 0; i < iterationTimes; i++) {
            VertRequest vertRequest = new VertRequest();
            vertRequest.setDayNum(dayNum);
            vertRequest.setSkierID(String.valueOf(begin + i));
            Response response = callGetVert(webTarget, vertRequest);
            statistics.addNumberRequest();
            if (response.getStatus() == 200) {
                statistics.addSuccessfulRequest();
            }
        }
        client.close();
        return statistics;
    }
}
