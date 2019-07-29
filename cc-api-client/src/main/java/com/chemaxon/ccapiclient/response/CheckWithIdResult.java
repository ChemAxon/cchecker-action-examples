/*
 * Copyright (c) 1998-2018 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */
package com.chemaxon.ccapiclient.response;

import java.util.HashSet;
import java.util.Set;

public class CheckWithIdResult {
    
    private String inputId;
    private Set<Long> substanceIds;
    private String errorMsg;

    public String getInputId() {
        return inputId;
    }
    public void setInputId(String inputId) {
        this.inputId = inputId;
    }
    public Set<Long> getSubstanceIds() {
        if (substanceIds == null) {
            substanceIds = new HashSet<>();
        }
        return substanceIds;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
