package com.chemaxon.ccapiclient.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.feign.CCApi;
import com.chemaxon.ccapiclient.log.Log;
import com.chemaxon.ccapiclient.props.AppProps;
import com.chemaxon.ccapiclient.request.CheckWithIdRequest;
import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;
import com.chemaxon.ccapiclient.resource.Result;
import com.chemaxon.ccapiclient.response.CheckWithIdResult;
import com.chemaxon.ccapiclient.service.CheckService;

@Service
public class CheckServiceImpl implements CheckService {

    @Log
    private static Logger log;

    @Autowired
    protected AppProps config;

    @Autowired
    private CCApi ccapi;

    @Override
    public Stream<Result> checkIdMols(Collection<IdentifiedMolecule> idMols) {
        CheckWithIdRequest checkRequest = new CheckWithIdRequest();
        checkRequest.getInputs().addAll(idMols);
        checkRequest.getCategoryGroupIds().addAll(config.getCategoryGroupIds());
        try {
            return ccapi.check(checkRequest).stream()
                    .flatMap(this::mapToResult);
        } catch (Exception e) {
            log.warn("Error happened during compliance checking. Executing checks for each structue one by one.", e);
            return checkMoleculesOneByOne(idMols);
        }
    }

    private Stream<Result> checkMoleculesOneByOne(Collection<IdentifiedMolecule> idMols) {

        return idMols.stream()
                .map(idMol -> {
                    CheckWithIdRequest checkRequest = new CheckWithIdRequest();
                    checkRequest.getInputs().add(idMol);
                    checkRequest.getCategoryGroupIds().addAll(config.getCategoryGroupIds());
                    return checkRequest;
                })
                .map(checkRequest -> {
                    try {
                        return ccapi.check(checkRequest);
                    } catch (Exception e) {
                        CheckWithIdResult result = new CheckWithIdResult();
                        result.setInputId(checkRequest.getInputs().get(0).getId());
                        result.setErrorMsg(
                                "Unexpected error happened during compliance checking. Please check logs for details.");
                        log.error("Error happened during compliance checking. (id:" + result.getInputId() + ")", e);
                        return Collections.singletonList(result);
                    }
                })
                .flatMap(Collection::stream)
                .flatMap(this::mapToResult);
    }

    private Stream<Result> mapToResult(CheckWithIdResult ccResult) {
        if (ccResult.getErrorMsg() != null) {
            Result result = new Result();
            result.setMolId(ccResult.getInputId());
            result.setErrorMessage(ccResult.getErrorMsg());
            return Stream.of(result);
        } else if (!ccResult.getSubstanceIds().isEmpty()) {
            return ccResult.getSubstanceIds().stream()
                    .map(id -> {
                        Result result = new Result();
                        result.setMolId(ccResult.getInputId());
                        result.setSubstanceId(String.valueOf(id));
                        return result;
                    });
        }
        return Stream.empty();
    }
}
