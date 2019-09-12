package com.chemaxon.compliancechecker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInfoResponse {
    
    private boolean newKnowledgeBaseAvailable;

	public boolean isNewKnowledgeBaseAvailable() {
		return newKnowledgeBaseAvailable;
	}
}
