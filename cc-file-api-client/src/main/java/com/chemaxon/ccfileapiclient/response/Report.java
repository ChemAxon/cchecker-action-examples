package com.chemaxon.ccfileapiclient.response;

public class Report {
    
    private String url;
    private Status state;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Status getState() {
        return state;
    }
    public void setState(Status state) {
        this.state = state;
    }
}
