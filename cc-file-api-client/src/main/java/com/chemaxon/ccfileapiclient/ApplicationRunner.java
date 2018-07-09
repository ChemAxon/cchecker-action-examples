package com.chemaxon.ccfileapiclient;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.chemaxon.ccfileapiclient.entity.CCheckResult;
import com.chemaxon.ccfileapiclient.repository.MoleculeRepository;
import com.chemaxon.ccfileapiclient.service.CCheckerService;
import com.chemaxon.ccfileapiclient.service.CheckResultProcessorService;
import com.chemaxon.ccfileapiclient.service.CsvInputFileCreatorService;

/**
 * Runner class
 * run method executes (checking starts) once the spring context is set up.
 */
@Component
public class ApplicationRunner implements CommandLineRunner {
    
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRunner.class);
    
    @Autowired
    private CsvInputFileCreatorService csvInputFileCreatorService;
    
    @Autowired
    private CCheckerService cCheckerService;
    
    @Autowired
    private CheckResultProcessorService checkResultProcessorService;
    
    @Autowired
    private MoleculeRepository moleculeRepo;

    @Override
    public void run(String... args) throws Exception {
        
        Instant start = Instant.now();
        LOG.info("Check started: {}", start);
        
        csvInputFileCreatorService.mapDbDataToInputFiles()
                .map(cCheckerService::checkFile)
                .forEach(checkResultProcessorService::saveCheckResults);

        Instant finish = Instant.now();
        LOG.info("Check finished: {}", finish);
        LOG.info("Check took: {} minutes.", Duration.between(start, finish).toMinutes());
        
        Long hitCount = moleculeRepo.countByCCheckResult(CCheckResult.HIT);
        Long passCount = moleculeRepo.countByCCheckResult(CCheckResult.PASS);
        Long errorCount = moleculeRepo.countByCCheckResult(CCheckResult.ERROR);
        LOG.info("Hits: {}, Passes: {}, Errors: {}", hitCount, passCount, errorCount);
        
    }
}
