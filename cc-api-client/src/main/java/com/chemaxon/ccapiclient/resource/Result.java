package com.chemaxon.ccapiclient.resource;

public class Result {
    
    private String molId;
    private String substanceId;
    private String errorMessage;
    
    public String getMolId() {
        return molId;
    }
    public void setMolId(String molId) {
        this.molId = molId;
    }
    public String getSubstanceId() {
        return substanceId;
    }
    public void setSubstanceId(String substanceId) {
        this.substanceId = substanceId;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
