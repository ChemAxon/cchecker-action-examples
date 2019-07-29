package com.chemaxon.ccapiclient.service.impl;

import java.sql.ResultSet;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.log.Log;
import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;
import com.chemaxon.ccapiclient.service.DataLoaderService;

@Service
public class DbDataLoaderService implements DataLoaderService {
    
    @Log
    private static Logger log;

    @Autowired
    private AppProps config;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    private BlockingQueue<IdentifiedMolecule> inputQueue;
    
    @Autowired
    private IdentifiedMolecule idMolPoisonPill;

    @Override
    public void run() {
        try {
            jdbcTemplate.query(config.getSelect(), (ResultSet rs) -> {
                IdentifiedMolecule idMol = new IdentifiedMolecule(rs.getString(1), rs.getString(2));
                try {
                    inputQueue.put(idMol);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Data loading process has been interrupted, during the processing of " + idMol, e);
                }
            });
            log.info("All data has been fetched from database.");
            for (int i = 0; i < config.getThreadPoolSize(); i++) {
                inputQueue.put(idMolPoisonPill);
            }
            log.debug("Poison pills ({}) have been added to the queue for each consumer.", config.getThreadPoolSize());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Failed to send poison pill to all executors");
        }
    }
}
