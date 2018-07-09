package com.chemaxon.ccfileapiclient.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.chemaxon.ccfileapiclient.response.BatchInfo;
import com.chemaxon.ccfileapiclient.response.CheckReport;
import com.chemaxon.ccfileapiclient.response.CheckState;
import com.chemaxon.ccfileapiclient.response.Report;
import com.chemaxon.ccfileapiclient.response.Status;
import com.chemaxon.ccfileapiclient.service.CCheckerService;
import com.chemaxon.ccfileapiclient.service.ResultCleanUpService;

@Service
public class CCheckerServiceImpl implements CCheckerService {
    
    private static final Logger LOG = LoggerFactory.getLogger(CCheckerServiceImpl.class);
    
    public static final String CSV_ID_COLUMN = "ID";
    public static final String CSV_STRUCTURE_COLUMN = "STRUCTURE";
    
    private static final String UPLOAD_URL = "/filecheck/upload";
    
    @Autowired
    private RestTemplate jsonRestTemplate;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private Optional<ResultCleanUpService> cleanUpService;
    
    @Value("${bigdata.url:http://localhost:8082/cc-bigdata}")
    private String bigdataUrl;

    @Override
    public List<List<CheckReport>> checkFile(File file) {
        BatchInfo batchInfo = uploadFile(file);
        sendJsonReportRequest(batchInfo.getUrl());
        String reportUrl = waitForReport(batchInfo.getUrl());
        cleanUpService.ifPresent(cleanUp -> cleanUp.init(batchInfo.getJobID(), file));
        return downloadReport(reportUrl);
    }
    
    private BatchInfo uploadFile(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new FileSystemResource(file));
        parts.add("csvStructureHeader", CSV_STRUCTURE_COLUMN);
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);
        return restTemplate.postForObject(bigdataUrl + UPLOAD_URL, requestEntity, BatchInfo.class);
    }
    
    private void sendJsonReportRequest(String url) {
        String request = "{\"containsErrors\": true, \"containsHits\": true, \"containsPasses\": true, \"formats\": [\"JSON\"]}";
        jsonRestTemplate.postForLocation(url, request);
    }
    
    private String waitForReport(String url) {
        Report report = null;
        
        boolean checkFinished = false;
        while (!checkFinished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.info("Thread.sleep() has been interrupted.", e);
                Thread.currentThread().interrupt();
            }
            CheckState checkState = jsonRestTemplate.getForObject(url, CheckState.class);
            if (!checkState.getReports().isEmpty()) {
                report = checkState.getReports().get(0);
                if (report.getState() == Status.FINISHED) {
                    checkFinished = true;
                }
            }
        }
        return report.getUrl();
    }
    
    private List<List<CheckReport>> downloadReport(String reportUrl) {
        return jsonRestTemplate
                .exchange(reportUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<List<CheckReport>>>() {}).getBody();
    }
}
