package com.chemaxon.ccapiclient.resource;

import java.util.HashSet;
import java.util.Set;

public class Substance {

    private Integer id;
    private Integer categoryCode;
    private String molName;
    private String example;
    private Set<String> synonyms;
    private Set<String> categoryLinks;
    private Set<String> casNumbers;
    private Set<String> deaNumbers;
    private Set<String> pubChemNumbers;
    private Set<String> gtinNumbers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getMolName() {
        return molName;
    }

    public void setMolName(String molName) {
        this.molName = molName;
    }

    public Set<String> getSynonyms() {
        if (synonyms == null) {
            synonyms = new HashSet<>();
        }
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public Set<String> getCategoryLinks() {
        if (categoryLinks == null) {
            categoryLinks = new HashSet<>();
        }
        return categoryLinks;
    }

    public void setCategoryLinks(Set<String> categoryLinks) {
        this.categoryLinks = categoryLinks;
    }

    public Set<String> getCasNumbers() {
        if (casNumbers == null) {
            casNumbers = new HashSet<>();
        }
        return casNumbers;
    }

    public void setCasNumbers(Set<String> casNumbers) {
        this.casNumbers = casNumbers;
    }

    public Set<String> getDeaNumbers() {
        if (deaNumbers == null) {
            deaNumbers = new HashSet<>();
        }
        return deaNumbers;
    }

    public void setDeaNumbers(Set<String> deaNumbers) {
        this.deaNumbers = deaNumbers;
    }

    public Set<String> getPubChemNumbers() {
        if (pubChemNumbers == null) {
            pubChemNumbers = new HashSet<>();
        }
        return pubChemNumbers;
    }

    public void setPubChemNumbers(Set<String> pubChemNumbers) {
        this.pubChemNumbers = pubChemNumbers;
    }

    public Set<String> getGtinNumbers() {
        if (gtinNumbers == null) {
            gtinNumbers = new HashSet<>();
        }
        return gtinNumbers;
    }

    public void setGtinNumbers(Set<String> gtinNumbers) {
        this.gtinNumbers = gtinNumbers;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
