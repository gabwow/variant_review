package org.mskcc.data.builder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class IgvRunner implements BamVisualizer {
    int socketNumber;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    private static final Logger DEV_LOGGER = Logger.getLogger("devLogger");
    
    public IgvRunner(int socketNumber){
        this.socketNumber = socketNumber;
    }
    
    public IgvRunner(){
        this.socketNumber = 60151;
    }

    @Override
    public void init() throws IOException, UnknownHostException{
        socket = new Socket("127.0.0.1", socketNumber);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        output.println("snapshotDirectory /Users/gabow/work/variant_review/screenshots");
        String response = input.readLine();
        DEV_LOGGER.info(response);
    }

    @Override
    public String loadGenome(String genomePath) throws IOException{
        output.println("genome " + genomePath);
        String response = input.readLine();
        DEV_LOGGER.info(response);
        return response;
    }

    @Override
    public String loadFile(String filePath) throws IOException{
        output.println("load " + filePath);
        String response = input.readLine();
        DEV_LOGGER.info(response);
        return response;
    }

    String buildGoto(Chrome chr, final long regionStart, final long regionEnd) throws GenomicRequestException{
        if(regionStart > regionEnd){
            throw new GenomicRequestException("Region start is greater than the region end");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("goto ").append(chr).append(":").append(regionStart).append("-").append(regionEnd);
        return sb.toString();
    }
    
    @Override
    public String gotoRegion(Chrome chr, final long regionStart, final long regionEnd) throws IOException, GenomicRequestException{
        output.println(buildGoto(chr, regionStart, regionEnd));
        String response = input.readLine();
        DEV_LOGGER.info(response);
        return response;
    }

    @Override
    public String savePicture() throws IOException{
        output.println("snapshot");
        String response = input.readLine();
        DEV_LOGGER.info(response);
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
