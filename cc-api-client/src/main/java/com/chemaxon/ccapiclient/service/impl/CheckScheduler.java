package com.chemaxon.ccapiclient.service.impl;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.log.Log;
import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.service.CheckQueueService;
import com.chemaxon.ccapiclient.service.DataLoaderService;
import com.chemaxon.ccapiclient.service.InitializerService;
import com.chemaxon.ccapiclient.service.ResultQueueService;

@Service
public class CheckScheduler {

    @Log
    private static Logger log;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private DataLoaderService dataLoaderService;

    @Autowired
    private AppProps config;

    @Autowired
    private CheckQueueService queueCheckService;

    @Autowired
    private ResultQueueService resultQueueService;

    @Autowired
    private InitializerService initializerService;

    @Scheduled(cron = "${scheduler.check.db}")
    public void startDbCheck() {
        
        try {
            initializerService.init();
            log.info("Compliance check started");
            taskExecutor.execute(dataLoaderService);
            IntStream.range(0, config.getThreadPoolSize())
                    .forEach(i -> taskExecutor.execute(queueCheckService));
            resultQueueService.process();
            log.info("Compliance check finished");
        } catch (Exception e) {
            log.error("Unexpected error happened during compliance checking. Scheduled db check has been aborted.", e);
        }
    }
}
