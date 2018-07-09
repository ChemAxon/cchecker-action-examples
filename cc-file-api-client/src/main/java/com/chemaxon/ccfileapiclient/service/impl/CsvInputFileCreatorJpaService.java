package com.chemaxon.ccfileapiclient.service.impl;

import static com.chemaxon.ccfileapiclient.service.impl.CCheckerServiceImpl.CSV_ID_COLUMN;
import static com.chemaxon.ccfileapiclient.service.impl.CCheckerServiceImpl.CSV_STRUCTURE_COLUMN;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.chemaxon.ccfileapiclient.entity.MoleculeEntity;
import com.chemaxon.ccfileapiclient.exception.CsvInputFileWriteException;
import com.chemaxon.ccfileapiclient.repository.MoleculeRepository;
import com.chemaxon.ccfileapiclient.service.CsvInputFileCreatorService;
import com.google.common.collect.Iterables;

public class CsvInputFileCreatorJpaService implements CsvInputFileCreatorService {
    
    private static final Logger LOG = LoggerFactory.getLogger(CsvInputFileCreatorJpaService.class);
    
    @Autowired
    private MoleculeRepository moleculeRepository;
    
    @Value("${tmp.folder:/home/cc/tmp}")
    private String tmpFolder;
    
    @Value("${file.mols.count:50000}")
    private int maxMolsInFile;

    private int counter = 0;
    
    @Override
    public Stream<File> mapDbDataToInputFiles() throws IOException {

        Iterable<MoleculeEntity> molecules = moleculeRepository.findAll();
        
        return StreamSupport.stream(Iterables.partition(molecules, maxMolsInFile).spliterator(), false)
                .map(this::createFile);
        
    }
    
    private File createFile(List<MoleculeEntity> molecules) {
        try {
            File file = createFileIfNotExist();
            try (FileWriter fw = new FileWriter(file, false); BufferedWriter bufferedWriter = new BufferedWriter(fw)) {
                bufferedWriter.write(CSV_ID_COLUMN + "," + CSV_STRUCTURE_COLUMN);
                bufferedWriter.newLine();
                molecules.forEach(molecule -> {
                    try {
                        bufferedWriter.write(molecule.getId() + "," + molecule.getStructure());
                        bufferedWriter.newLine();
                    } catch (IOException e) {
                        throw new CsvInputFileWriteException("Failed to write to csv file", e);
                    }
                });
            }
            return file;
        } catch (IOException e) {
            throw new CsvInputFileWriteException("Failed to write to csv file", e);
        }
    }
    
    private File createFileIfNotExist() throws IOException {
        Path path = Paths.get(tmpFolder + "/batch_" + ++counter + ".csv");

        Files.createDirectories(path.getParent());

        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            LOG.trace("File already exists", e);
        }
        return path.toFile();
    }
}
