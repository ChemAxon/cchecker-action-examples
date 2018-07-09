package com.chemaxon.ccfileapiclient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MoleculeEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "TEXT")    
    private String structure;
    @Enumerated(EnumType.STRING)
    private CCheckResult cCheckResult;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStructure() {
        return structure;
    }
    public void setStructure(String structure) {
        this.structure = structure;
    }
    public CCheckResult getcCheckResult() {
        return cCheckResult;
    }
    public void setcCheckResult(CCheckResult cCheckResult) {
        this.cCheckResult = cCheckResult;
    }
}
