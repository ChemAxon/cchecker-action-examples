package com.chemaxon.ccapiclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.chemaxon.ccapiclient.service.impl.CheckScheduler;

//@Component
public class Runner implements CommandLineRunner {
    
    @Autowired
    private CheckScheduler sch;

    @Override
    public void run(String... args) throws Exception {
        sch.startDbCheck();
    }

}
