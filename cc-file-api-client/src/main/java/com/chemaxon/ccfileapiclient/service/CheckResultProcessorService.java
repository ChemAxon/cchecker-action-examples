package com.chemaxon.ccfileapiclient.service;

import java.util.List;

import com.chemaxon.ccfileapiclient.response.CheckReport;

public interface CheckResultProcessorService {

    /**
     * Persists Compliance Checker check results 
     * @param results
     */
    void saveCheckResults(List<List<CheckReport>> results);

}
