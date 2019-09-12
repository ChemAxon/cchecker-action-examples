package com.chemaxon.compliancechecker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chemaxon.compliancechecker.dto.SystemInfoResponse;
import com.chemaxon.compliancechecker.service.DbUpdateCheckerService;
import com.chemaxon.compliancechecker.service.NotificationService;

@Service
public class DbUpdateCheckerServiceImpl implements DbUpdateCheckerService {

    private RestTemplate rest;
    private NotificationService notificationService;

    @Autowired
    public DbUpdateCheckerServiceImpl(NotificationService notificationService, RestTemplate rest) {
        this.notificationService = notificationService;
        this.rest = rest;
    }

    @Override
    public void notifyIfNewDbAvailable() {
        if (getAutoUpdateInfo().isNewKnowledgeBaseAvailable()) {
            notificationService.sendNotification();
        }
    }

    private SystemInfoResponse getAutoUpdateInfo() {
        return rest.getForEntity("/cc-api/system-info/", SystemInfoResponse.class).getBody();
    }
}
