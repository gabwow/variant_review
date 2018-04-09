package org.mskcc.data.builder;

public interface MafReader {
    public Chrome getChr();
    public CallDecision getDecision();
    public String getAlt();
    public String getRef();
    public String getTumor();
    public String getNormal();
    public boolean isReady();
    public long getZeroBasedStart();
    public long getZeroBasedExclusiveEnd();
    public void loadNext();
}
