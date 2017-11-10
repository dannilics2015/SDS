package com.programmerscuriosity;

import model.MyRecord;
import model.Result;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import static utility.DataVariables.POSTURL;

public class PostRecordTask implements Callable<Result> {

    private int iterationTimes;
    private Result statistics;
    private String ipAddress;
    private List<MyRecord> myRecordList;


    private final static Logger LOGGER = Logger.getLogger(PostRecordTask.class.getName());

    public PostRecordTask(int iteratedTimes, String ip, List<MyRecord> myRecords) {
        iterationTimes = iteratedTimes;
        statistics = new Result();
        ipAddress = ip;
        myRecordList = myRecords;
    }

    //call POST method
    public Response callPostRecord(WebTarget webTarget, List<MyRecord> myRecords, int index) {
        Response response = null;
        try {
            long threadStartTime = System.currentTimeMillis();
            response = webTarget.path(POSTURL).request().post(Entity.json(myRecords.get(index)));
            long threadEndTime = System.currentTimeMillis();
            long latency = threadEndTime - threadStartTime;
            statistics.getLatency().put(threadStartTime, latency);
            response.close();
        } catch (Exception e) {
            synchronized (this) {
                ServerErrorCaptureTask.errorsFromPost++;
            }
            LOGGER.warning("Exception Error message: " + e.getMessage());
//            e.printStackTrace();
        }
        return response;

    }

    public Result call() throws Exception {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);
        client.property(ClientProperties.CONNECT_TIMEOUT, 240000);
        WebTarget webTarget = client.target(ipAddress);
        Response response = null;
        for (int i = 0; i < iterationTimes; i++) {
            try {
                statistics.addNumberRequest();
                response = callPostRecord(webTarget, myRecordList, i);
            } catch (ProcessingException e) {
                System.out.print(e.getMessage());
            }

            if (response != null && response.getStatus() == 201) {
                statistics.addSuccessfulRequest();
            }
        }
        client.close();
        return statistics;
    }
}