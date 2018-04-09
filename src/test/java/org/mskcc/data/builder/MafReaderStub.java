package org.mskcc.data.builder;

public class MafReaderStub implements MafReader {
    private int index = 0;

    @Override
    public boolean isReady() {
        if(index > 2){
            return false;
        }
        return true;
    }

    @Override
    public void loadNext(){
        index++;
    }

    @Override
    public Chrome getChr(){
        return Chrome.CHR7;
    }

    @Override
    public CallDecision getDecision(){
        return CallDecision.APPROVED;
    }

    @Override
    public String getTumor(){
        return "NOT_BAD_TUMOR";
    }

    @Override
    public String getNormal(){
        return "TOTALLY_NORMAL";
    }


    @Override
    public String getRef(){
        if(index < 1){
            return "A";
        }
        return "G";
    }

    @Override
    public String getAlt(){
        if(index < 1){
            return "C";
        }
        return "T";
    }

    @Override
    public long getZeroBasedStart(){
        if(index < 1){
            return 10000;
        }
        return 20000;
    }

    @Override
    public long getZeroBasedExclusiveEnd(){
        if(index < 1){
            return 10001;
        }
        return 20001;
    }
}
