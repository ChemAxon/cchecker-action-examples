package com.chemaxon.ccapiclient.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.chemaxon.ccapiclient.feign.CCApi;
import com.chemaxon.ccapiclient.resource.Substance;
import com.chemaxon.ccapiclient.service.InitializerService;

@Service
public class InitializerServiceImpl implements InitializerService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private CCApi ccApi;

    @Override
    public void init() {
        jdbcTemplate.execute("TRUNCATE TABLE CC_RESULT");
        jdbcTemplate.execute("TRUNCATE TABLE CC_SUBSTANCE");
        insertSubstances();
    }
    
    private void insertSubstances() {
        
        List<Substance> substanceList = ccApi.getSubstances();
        
        String query = "INSERT INTO CC_SUBSTANCE(SUBSTANCE_ID, CATEGORY_CODE, EXAMPLE, SYNONYMS, CAS_NUMBERS, MOL_NAME, LINKS, DEA_NUMBERS, PUB_CHEM_NUMBERS, GTIN_NUMBERS)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Substance substance = substanceList.get(i);
                ps.setInt(1, substance.getId());
                ps.setInt(2, substance.getCategoryCode());
                ps.setString(3, substance.getExample());
                ps.setString(4, String.join(",", substance.getSynonyms()));
                ps.setString(5, String.join(",", substance.getCasNumbers()));
                ps.setString(6, substance.getMolName());
                ps.setString(7, String.join(",", substance.getCategoryLinks()));
                ps.setString(8, String.join(",", substance.getDeaNumbers()));
                ps.setString(9, String.join(",", substance.getPubChemNumbers()));
                ps.setString(10, String.join(",", substance.getGtinNumbers()));
            }

            @Override
            public int getBatchSize() {
                return substanceList.size();
            }
        });
    }
}
