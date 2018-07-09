package com.chemaxon.ccfileapiclient.service;

import java.io.File;
import java.util.List;

import com.chemaxon.ccfileapiclient.response.CheckReport;

public interface CCheckerService {

    /**
     * @param file to be checked by Compliance Checker
     * @return check results
     */
    List<List<CheckReport>> checkFile(File file);

}
