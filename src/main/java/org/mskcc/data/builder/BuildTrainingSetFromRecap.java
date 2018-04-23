package org.mskcc.data.builder;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class BuildTrainingSetFromRecap {
    enum Environment { DESKTOP,CLUSTSER;}

    private BamVisualizer visualizer;
    private Environment runEnvironment;


    public BuildTrainingSetFromRecap(){
        visualizer = new IgvRunner();
        runEnvironment = Environment.CLUSTSER;
    }

    public void processFiles(String inputMafDirectory, String inputBamDirectory){
        Map<String, String> mafToBam = new HashMap<>();
        Path bamDir = Paths.get(inputBamDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(bamDir)) {
            for (Path bam:  stream) {
                mafToBam.put(bam.toString().replace(".bam", ".maf"), bam.toString());
            }
        } catch (IOException | DirectoryIteratorException x) {
            throw new GenomicRequestException("Cannot open directory " + inputBamDirectory);
        }

        Path mafDir = Paths.get(inputMafDirectory);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(mafDir)) {
            for (Path maf:  stream) {
                MafReader mafReader = new BasicMafReader(maf.toString());
                String bamPath = mafToBam.get(maf.toString());
                while (mafReader.isReady()){
                    mafReader.loadNext();
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            throw new GenomicRequestException("Cannot open directory " + inputMafDirectory);
        }



    }

    public void executeBuildStep(){
        switch (runEnvironment) {
            case DESKTOP:
                break;
            case CLUSTSER:

        }
    }
}
