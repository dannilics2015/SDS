package com.programmerscuriosity;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MyServletListener implements ServletContextListener {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    private final static Logger LOGGER = Logger.getLogger(MyServletListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("Initialized...");
        executor.submit(new CacheTask());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("Destroyed... Shutting down listening thread ...");
        executor.shutdown();
    }
}
