package com.chemaxon.compliancechecker.service.impl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.chemaxon.compliancechecker.dto.AutoUpdateInfo;
import com.chemaxon.compliancechecker.service.DbUpdateCheckerService;
import com.chemaxon.compliancechecker.service.NotificationService;

@Service
public class DbUpdateCheckerServiceImpl implements DbUpdateCheckerService {

	private String host;
	private String user;
	private String password;
	private RestOperations rest;
	private NotificationService notificationService;
	
	@Autowired
	public DbUpdateCheckerServiceImpl(NotificationService notificationService, RestOperations rest) {
		this.notificationService = notificationService;
		this.rest = rest;
	}

	@Override
	public void notifyIfNewDbAvailable() {
		if (getAutoUpdateInfo().isNewDbAvailable()) {
			notificationService.sendNotification();
		}
	}
	
	private AutoUpdateInfo getAutoUpdateInfo() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString((user + ":" + password).getBytes()));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<AutoUpdateInfo> response =
				rest.exchange(host + "/cc-bigdata/db/autoupdate/check",
						HttpMethod.GET, entity, AutoUpdateInfo.class);
		return response.getBody();
	}

	@Value("${bigdata.host}")
	public void setHost(String host) {
		this.host = host;
	}

	@Value("${bigdata.user}")
	public void setUser(String user) {
		this.user = user;
	}

	@Value("${bigdata.password}")
	public void setPassword(String password) {
		this.password = password;
	}
}
