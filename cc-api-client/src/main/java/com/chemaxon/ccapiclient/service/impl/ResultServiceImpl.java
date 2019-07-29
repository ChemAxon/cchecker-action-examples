package com.chemaxon.ccapiclient.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.resource.Result;
import com.chemaxon.ccapiclient.service.ResultService;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void process(Result result) {
        String sql = "INSERT INTO CC_RESULT(STRUCTURE_ID, SUBSTANCE_ID, ERROR_MESSAGE) VALUES "
                + "(:molId, :substanceId, :error) ";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("molId", result.getMolId());
        parameters.put("substanceId", result.getSubstanceId());
        parameters.put("error", result.getErrorMessage());
        jdbcTemplate.update(sql, parameters);
    }
}
