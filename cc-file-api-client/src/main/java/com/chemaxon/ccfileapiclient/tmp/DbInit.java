package com.chemaxon.ccfileapiclient.tmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.chemaxon.ccfileapiclient.entity.MoleculeEntity;
import com.chemaxon.ccfileapiclient.repository.MoleculeRepository;
import com.chemaxon.ccfileapiclient.service.CCheckerService;
import com.chemaxon.ccfileapiclient.service.CheckResultProcessorService;
import com.chemaxon.ccfileapiclient.service.CsvInputFileCreatorService;

//@Component
public class DbInit implements CommandLineRunner {
    
    @Autowired
    private MoleculeRepository moleculeRepo;
    
    @Autowired
    private CsvInputFileCreatorService molFileCreator;
    
    @Autowired
    private CCheckerService checkFileService;
    
    @Autowired
    private CheckResultProcessorService checkResultProcessorService;

    @Override
    public void run(String... args) throws Exception {
        //persistTo10KDb();
        sysoutDb();
        
/*        Stream<File> files = molFileCreator.createInputFilesFromDb();
        
        List<List<CheckReport>> result = checkFileService.checkFile(files.get(0));
        checkResultProcessorService.saveCheckResults(result);
        sysoutDb();*/
        
    }
    
    private void persistTo10KDb() throws IOException {
        try (InputStream inputStream = new ClassPathResource("nci10000.smiles").getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.lines().map(line -> {
                MoleculeEntity molecule = new MoleculeEntity();
                molecule.setStructure(line);
                return molecule;
            })
                    .forEach(moleculeRepo::save);
        }
    }
    
    private void persistTo7MilDb() throws IOException {
        for (int i = 0; i < 140; i++) {
            try (InputStream inputStream = new ClassPathResource("slow_50000.csv").getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                bufferedReader.lines().map(line -> {
                    String[] l = line.split(",");
                    MoleculeEntity molecule = new MoleculeEntity();
                    molecule.setStructure(l[0]);
                    return molecule;
                })
                .forEach(moleculeRepo::save);
            }
        }
    }
    
    private void sysoutDb() {
        moleculeRepo.findAll().forEach(mol -> {
            System.out.println(mol.getId() + "  " + mol.getStructure() + " " + mol.getcCheckResult());
        });
    }
    
}
