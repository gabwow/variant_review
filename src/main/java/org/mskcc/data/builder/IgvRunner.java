package org.mskcc.data.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class IgvRunner implements BamVisualizer {
    int socketNumber;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    private static final Logger LOGGER = LogManager.getLogger(IgvRunner.class);
    
    public IgvRunner(int socketNumber){
        this.socketNumber = socketNumber;
    }

    public IgvRunner(){}

    protected Map<String, String> processConfigureFile(String filePath){
        Map<String, String> properties = new HashMap<>();
        try(Stream<String> lines =  Files.lines(Paths.get(filePath))){
            properties = lines.collect(Collectors.toMap(s -> s.split(":")[0], s-> s.split(":")[1] ));
        } catch (IOException e){
            throw new GenomicRequestException(e);
        }
        if(socketNumber == 0){
            socketNumber = Integer.parseInt(properties.get("socket_number"));
        }
        return properties;
    }

    @Override
    public void init() {
        Map<String, String> properties = processConfigureFile("configure.properties");
        try {
            socket = new Socket("127.0.0.1", socketNumber);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("snapshotDirectory " + properties.get("snapshot_directory"));
            String response = input.readLine();
            LOGGER.info(response);
        } catch (IOException e){
            LOGGER.info("Failed to initialize IgvRunner");
            throw new GenomicRequestException(e);
        }
    }

    @Override
    public String loadGenome(String genomePath){
        String response = "";
        try {
            output.println("genome " + genomePath);
            response = input.readLine();
            LOGGER.info(response);
        } catch (IOException e){
            LOGGER.info("Could not load the genome at " + genomePath );
            throw new GenomicRequestException(e);
        }
        return response;
    }

    @Override
    public String loadFile(String filePath) {
        String response = "";
        try {
            output.println("load " + filePath);
            response = input.readLine();
            LOGGER.info(response);
        } catch (IOException e){
            LOGGER.info("Count not load file at " + filePath);
            throw new GenomicRequestException(e);
        }
        return response;
    }

    String buildGoto(Chrome chr, final long regionStart, final long regionEnd) {
        if(regionStart > regionEnd){
            throw new GenomicRequestException("Region start is greater than the region end");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("goto ").append(chr).append(":").append(regionStart).append("-").append(regionEnd);
        return sb.toString();
    }
    
    @Override
    public String gotoRegion(Chrome chr, final long regionStart, final long regionEnd){
        String response = "";
        try {
            output.println(buildGoto(chr, regionStart, regionEnd));
            response = input.readLine();
            LOGGER.info(response);
        } catch (IOException e){
            LOGGER.info("Failure in going to region: " + chr.toString() + ":" + Long.toString(regionStart));
            throw new GenomicRequestException(e);
        }
        return response;
    }

    @Override
    public String savePicture() {
        String response = "";
        try {
            output.println("snapshot");
            response = input.readLine();
            LOGGER.info(response);
        } catch (IOException e){
            LOGGER.info("Failed to save picture");
            throw new GenomicRequestException(e);
        }
        return response;
    }

    public static void main(String[] args){
       BamVisualizer runner = new IgvRunner(60151);
       try{
          runner.init();
          runner.loadGenome("~/igv/genomes/hg19.genome");
          runner.loadFile("~/temp/vcf/vcf.js-master/test/data/Proj_06945_B/bams/s_HCV29R17_6945B_bc0023_Proj_06945_B_L000_mrg_cl_aln_srt_MD_IR_BR.bam");
          runner.gotoRegion(Chrome.CHR17, 38447838L, 38449838L);
          runner.savePicture();
       } catch(Exception e){
          e.printStackTrace();
       }
    }

}
