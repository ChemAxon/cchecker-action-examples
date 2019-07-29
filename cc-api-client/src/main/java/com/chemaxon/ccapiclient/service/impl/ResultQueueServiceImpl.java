package com.chemaxon.ccapiclient.service.impl;

import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.log.Log;
import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.resource.Result;
import com.chemaxon.ccapiclient.service.ResultQueueService;
import com.chemaxon.ccapiclient.service.ResultService;

@Service
public class ResultQueueServiceImpl implements ResultQueueService {

    @Log
    private static Logger log;

    private int counter = 0;

    @Autowired
    private ResultService resultService;

    @Autowired
    private Result poisonPill;

    @Autowired
    private AppProps config;

    @Resource
    private BlockingQueue<Result> resultQueue;

    @Override
    public void process() {

        Result result = null;
        try {
            result = resultQueue.take();
            while (true) {
                if (result == poisonPill) {
                    if (++counter >= config.getThreadPoolSize()) {
                        return;
                    }
                } else {
                    resultService.process(result);
                }
                result = resultQueue.take();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn(String.format("Result writing to database has been interrupted, last processed result is: %s)", result), e);
        } finally {
            counter = 0;
            resultQueue.clear();
        }
    }
}
