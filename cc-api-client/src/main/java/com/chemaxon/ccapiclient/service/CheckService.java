package com.chemaxon.ccapiclient.service;

import java.util.Collection;
import java.util.stream.Stream;

import com.chemaxon.ccapiclient.resource.IdentifiedMolecule;
import com.chemaxon.ccapiclient.resource.Result;

public interface CheckService {

    Stream<Result> checkIdMols(Collection<IdentifiedMolecule> idMols);
}
