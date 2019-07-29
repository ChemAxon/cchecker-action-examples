/*
 * Copyright (c) 1998-2018 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */
package com.chemaxon.ccapiclient.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;

public class CheckWithIdRequest {

    private List<IdentifiedMolecule> inputs;

    private Set<String> categoryGroupIds;

    private String dateOfRegulations;

    private String molFormat;

    public List<IdentifiedMolecule> getInputs() {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        return inputs;
    }
    
    public Set<String> getCategoryGroupIds() {
        if (categoryGroupIds == null) {
            categoryGroupIds = new HashSet<>();
        }
        return categoryGroupIds;
    }

    public String getDateOfRegulations() {
        return dateOfRegulations;
    }

    public void setDateOfRegulations(String dateOfRegulations) {
        this.dateOfRegulations = dateOfRegulations;
    }

    public String getMolFormat() {
        return molFormat;
    }

    public void setMolFormat(String molFormat) {
        this.molFormat = molFormat;
    }
}
