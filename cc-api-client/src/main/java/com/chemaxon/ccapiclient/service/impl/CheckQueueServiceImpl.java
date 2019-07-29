package com.chemaxon.ccapiclient.service.impl;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.exception.PoisonedException;
import com.chemaxon.ccapiclient.log.Log;
import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;
import com.chemaxon.ccapiclient.resource.Result;
import com.chemaxon.ccapiclient.service.CheckQueueService;
import com.chemaxon.ccapiclient.service.CheckService;

@Service
public class CheckQueueServiceImpl implements CheckQueueService {

    @Log
    private static Logger log;
    
    @Autowired
    protected AppProps config;

    @Resource
    private BlockingQueue<IdentifiedMolecule> inputQueue;
    
    @Resource
    private BlockingQueue<Result> resultQueue;
    
    @Autowired
    private CheckService checkService;

    @Autowired
    private Result resultPoisonPill;

    @Autowired
    private IdentifiedMolecule idMolPoisonPill;
    
    @Override
    public void run() {
        try {
            while (true) { //NOSONAR poison pill will be received at one point -> loop will end  
                Set<IdentifiedMolecule> idMolSet = new HashSet<>();
                try {
                    initIdMolSet(idMolSet);
                    checkAndAddResultsToQueue(idMolSet);
                } catch (PoisonedException e) {
                    if (!idMolSet.isEmpty()) {
                        checkAndAddResultsToQueue(idMolSet);
                    }
                    resultQueue.put(resultPoisonPill);
                    return;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Checking process has been interrupted.", e);
        }
    }
    
    private void initIdMolSet(Collection<IdentifiedMolecule> idMolSet) throws InterruptedException, PoisonedException {
        for (int i = 0; i < config.getChunkSize(); i++) {
            IdentifiedMolecule idMol = inputQueue.take();
            if (idMol != idMolPoisonPill) {
                idMolSet.add(idMol);
            } else {
                throw new PoisonedException();
            }
        }
    }
    
    private void checkAndAddResultsToQueue(Collection<IdentifiedMolecule> idMolSet) throws InterruptedException {
        Set<Result> results = checkService.checkIdMols(idMolSet).collect(toSet());
        for (Result result : results) {
            resultQueue.put(result);
        }
    }
}
