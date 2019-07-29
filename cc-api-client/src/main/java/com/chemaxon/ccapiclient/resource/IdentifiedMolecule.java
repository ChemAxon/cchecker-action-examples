package com.chemaxon.ccapiclient.resource;

public class IdentifiedMolecule {
    
    private String id;
    private String structure;
    
    public IdentifiedMolecule() {}

    public IdentifiedMolecule(String id, String structure) {
        this.id = id;
        this.structure = structure;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStructure() {
        return structure;
    }
    public void setStructure(String structure) {
        this.structure = structure;
    }
 }
