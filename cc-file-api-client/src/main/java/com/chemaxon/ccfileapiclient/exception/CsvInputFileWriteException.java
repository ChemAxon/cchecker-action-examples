package com.chemaxon.ccfileapiclient.exception;

public class CsvInputFileWriteException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public CsvInputFileWriteException(String msg, Throwable t) {
        super(msg, t);
    }
}
