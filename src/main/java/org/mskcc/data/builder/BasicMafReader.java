package org.mskcc.data.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BasicMafReader implements MafReader {
    private BufferedReader mafFile;
    private Map<String, Integer> headerToIndex = new HashMap<>();
    private List<String> values;
    private String filePath;

    private static final Logger LOGGER = LogManager.getLogger(BasicMafReader.class);

    public BasicMafReader(String filePath){
        this.filePath = filePath;
        try{
            mafFile = new BufferedReader(new FileReader(filePath));
            while (mafFile.readLine().startsWith("#")){

            }
            String[] header = mafFile.readLine().split("\t");
            for (int i = 0; i < header.length; i++){
                headerToIndex.put(header[i], i);
            }
            values = Arrays.asList(mafFile.readLine().split("\t"));
        } catch(IOException e){
            LOGGER.info("Failed to initialize maf reader for file " + filePath);
            throw new GenomicRequestException(e);
        }

    }

    @Override
    public Chrome getChr() {
        return Chrome.fromString(values.get(headerToIndex.get("Chromosome")));
    }

    private boolean statusFailed(String status){
        if(status.startsWith("somatic_")){
            return true;
        }
        return false;
    }

    @Override
    public CallDecision getDecision() {
        String status = values.get(headerToIndex.get("validation_status"));
        if(statusFailed(status)){
            return CallDecision.REDACTED;
        }
        return CallDecision.APPROVED;
    }

    @Override
    public String getAlt() {
        String alt = values.get(headerToIndex.get("Tumor_Seq_Allele1"));
        if(getRef().equals(alt)){
            alt =  values.get(headerToIndex.get("Tumor_Seq_Allele2"));
        }
        return alt;
    }

    @Override
    public String getRef() {
        return values.get(headerToIndex.get("Reference_Allele"));
    }

    @Override
    public String getTumor() {
        return values.get(headerToIndex.get("Tumor_Sample_Barcode"));
    }

    @Override
    public String getNormal() {
        return values.get(headerToIndex.get("Matched_Norm_Sample_Barcode"));
    }

    @Override
    public boolean isReady() {
        try {
            return mafFile.ready();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public long getZeroBasedStart() {
        long startPos = Integer.parseInt(values.get(headerToIndex.get("Start_Position"))) - 1;
        return startPos;
    }

    @Override
    public long getZeroBasedExclusiveEnd() {
        long endPos = Integer.parseInt(values.get(headerToIndex.get("End_Position")));
        return  endPos;
    }
    @Override
    public void loadNext() {
        try {
            if (isReady()) {
                values = Arrays.asList(mafFile.readLine().split("\t"));
            } else {
                LOGGER.info("BasicMafReader trying to parse lines after EOF");
                throw new GenomicRequestException("Already at end-of-file");
            }
        } catch (IOException e) {
            LOGGER.info("BasicMafReader failed to get new line for file " + filePath);
            throw new GenomicRequestException(e);
        }
    }
}
