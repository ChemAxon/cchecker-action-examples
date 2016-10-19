package com.chemaxon.compliancechecker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoUpdateInfo {
    
    private boolean newDbAvailable;
    private String currentVersion;
    private String autoUpdateVersion;
    
	public boolean isNewDbAvailable() {
		return newDbAvailable;
	}
	public void setNewDbAvailable(boolean newDbAvailable) {
		this.newDbAvailable = newDbAvailable;
	}
	public String getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}
	public String getAutoUpdateVersion() {
		return autoUpdateVersion;
	}
	public void setAutoUpdateVersion(String autoUpdateVersion) {
		this.autoUpdateVersion = autoUpdateVersion;
	}
}
