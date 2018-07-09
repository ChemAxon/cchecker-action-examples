package com.chemaxon.ccfileapiclient.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.chemaxon.ccfileapiclient.repository.MoleculeRepository;
import com.chemaxon.ccfileapiclient.response.CheckReport;
import com.chemaxon.ccfileapiclient.service.CheckResultProcessorService;
import com.chemaxon.ccfileapiclient.service.ResultCleanUpService;

public class CheckResultProcessorJpaService implements CheckResultProcessorService {

    @Autowired
    private MoleculeRepository moleculeRepo;
    
    @Autowired
    private Optional<ResultCleanUpService> cleanUpService;

    @Override
    @Transactional
    public void saveCheckResults(List<List<CheckReport>> results) {
        results.stream().flatMap(Collection::stream).forEach(this::saveCheckResult);
        cleanUpService.ifPresent(ResultCleanUpService::cleanUp);
    }

    private void saveCheckResult(CheckReport result) {
        moleculeRepo.setCheckResult(result.getId(), result.getCheckResult());
    }
}
