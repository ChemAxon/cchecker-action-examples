package com.chemaxon.ccfileapiclient.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chemaxon.ccfileapiclient.service.ResultCleanUpService;

@Service
@ConditionalOnProperty(name = "check.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public class ResultCleanUpServiceImpl implements ResultCleanUpService {

    private static final Logger LOG = LoggerFactory.getLogger(ResultCleanUpServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bigdata.url:http://localhost:8082/cc-bigdata}")
    private String bigdataUrl;

    private String batchId;
    
    private File file;

    @Override
    public void cleanUp() {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            LOG.error("Failed to delete file: " + file.getName(), e);
        }
        restTemplate.delete(bigdataUrl + "/job/" + batchId);
    }

    @Override
    public void init(String batchId, File file) {
        this.file = file;
        this.batchId = batchId;
    }
}
