package com.programmerscuriosity;

import model.MyRecord;
import model.VertData;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static utility.DataVariables.jedisHost;
import static utility.DataVariables.jedisPort;
import static utility.DataVariables.splitStr;


@Path("myresource")
public class MyResource {

    CacheTask cacheTask = new CacheTask();

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
            myRecords = jedis.lrange(skierID, 0, -1);
            jedis.close();
        } catch(Exception e) {
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

}