package com.chemaxon.ccfileapiclient.service;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public interface CsvInputFileCreatorService {

    /**
     * @return Stream of csv molecule files created from database 
     * @throws IOException
     */
    Stream<File> mapDbDataToInputFiles() throws IOException;

}
