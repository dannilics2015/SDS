package com.programmerscuriosity;

import model.MyRecord;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Pipeline;
import utility.DataVariables;

import java.util.List;
import java.util.logging.Logger;

import static utility.DataVariables.jedisHost;
import static utility.DataVariables.jedisPort;
import static utility.DataVariables.tableIndex;

public class BatchInsertTask implements Runnable {

    private List<MyRecord> list;

    DbQueryPOSTCaptureTask dbQueryPOSTCaptureTask = new DbQueryPOSTCaptureTask();

    public BatchInsertTask(List<MyRecord> list) {
        this.list = list;
    }

    private final static Logger LOGGER = Logger.getLogger(BatchInsertTask.class.getName());
    @Override
    public void run() {
//        JedisCluster jedisCluster = new JedisCluster(new HostAndPort(DataVariables.jedisHost, DataVariables.jedisPort));
        Jedis jedis = new Jedis(jedisHost, jedisPort, 10000);
        jedis.select(tableIndex);
//        Pipeline p = jedis.pipelined();
        for (int i=0; i < list.size(); i++) {
            double start = System.nanoTime();
            try {
                jedis.rpush(list.get(i).getSkierID(), list.get(i).toString());
//                jedisCluster.rpush(list.get(i).getSkierID(), list.get(i).toString());
            } catch (Exception e) {
                synchronized (this) {
                    DbQueryPOSTCaptureTask.dbQueryErrorsFromPOST++;
                }
                e.printStackTrace();
            }
            dbQueryPOSTCaptureTask.addPOSTresponseTime((System.nanoTime() - start) / 1000000);
        }
//        p.sync();
        LOGGER.info("POST list of data, counts: " + list.size() +
                " at time: " + System.currentTimeMillis() +
                " on thread: " + Thread.currentThread());
        jedis.close();
    }
}
