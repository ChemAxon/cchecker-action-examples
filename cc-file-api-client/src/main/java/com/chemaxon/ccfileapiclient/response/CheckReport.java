/*
 * Copyright (c) 1998-2017 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */
package com.chemaxon.ccfileapiclient.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.chemaxon.ccfileapiclient.entity.CCheckResult;

public class CheckReport {

    private List<Segment> segments;
    private Map<String, String> inputProperties;
    private String error;

    public List<Segment> getSegments() {
        if (segments == null) {
            segments = new ArrayList<>();
        }
        return segments;
    }
    public Map<String, String> getInputProperties() {
        if (inputProperties == null) {
            inputProperties = new HashMap<>();
        }
        return inputProperties;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
    public Long getId() {
        return Long.valueOf(inputProperties.get("ID"));
    }
    
    public CCheckResult getCheckResult() {
        if (!StringUtils.isEmpty(error)) {
            return CCheckResult.ERROR;
        }
        else if (segments.isEmpty()) {
            return CCheckResult.PASS;
        }
        return CCheckResult.HIT;
    }
}
