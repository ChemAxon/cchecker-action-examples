package com.chemaxon.ccfileapiclient.service;

import java.io.File;

public interface ResultCleanUpService {

    void cleanUp();

    void init(String batchId, File file);
}
