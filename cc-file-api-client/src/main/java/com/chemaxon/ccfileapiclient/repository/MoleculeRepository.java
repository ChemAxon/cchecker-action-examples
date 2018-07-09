package com.chemaxon.ccfileapiclient.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chemaxon.ccfileapiclient.entity.CCheckResult;
import com.chemaxon.ccfileapiclient.entity.MoleculeEntity;

public interface MoleculeRepository extends CrudRepository<MoleculeEntity, Long> {
    
    @Modifying
    @Query("update MoleculeEntity mol set mol.cCheckResult = ?2 where mol.id = ?1")
    void setCheckResult(Long id, CCheckResult result);
    
    public Long countByCCheckResult(CCheckResult result);

}
