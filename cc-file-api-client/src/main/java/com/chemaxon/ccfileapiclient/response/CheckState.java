package com.chemaxon.ccfileapiclient.response;

import java.util.ArrayList;
import java.util.List;

public class CheckState {
    private Status status;
    private List<Report> reports; 

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public List<Report> getReports() {
        if (reports == null) {
            reports = new ArrayList<>();
        }
        return reports;
    }
}
