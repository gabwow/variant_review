package org.mskcc.data.builder;

import java.util.Objects;

public class Variant {
    private CallDecision decision;
    private Chrome chr;
    private String tumorSample;
    private String normalSample;
    private String ref;
    private String alt;
    private long regionStart;
    private long regionEnd;

    public Variant chr(Chrome chr){
        this.chr = chr;
        return this;
    }

    public Variant regionStart(long start){
        this.regionStart = start;
        return this;
    }

    public Variant regionEnd(long end){
        this.regionEnd = end;
        return this;
    }

    public Variant decision(CallDecision decision){
        this.decision = decision;
        return this;
    }

    public Variant ref(String ref){
        this.ref = ref;
        return this;
    }

    public Variant alt(String alt){
        this.alt = alt;
        return this;
    }

    public Variant tumorSample(String sample){
        this.tumorSample = sample;
        return this;
    }

    public Variant normalSample(String sample){
        this.normalSample = sample;
        return this;
    }

    public Chrome getChr() {
        return chr;
    }


    public long getRegionStart() {
        return regionStart;
    }

    public long getRegionEnd() {
        return regionEnd;
    }

    public CallDecision getDecision() {
        return decision;
    }

    public String getTumorSample() {
        return tumorSample;
    }

    public String getRef() {
        return ref;
    }

    public String getAlt() {
        return alt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variant variant = (Variant) o;
        return regionStart == variant.regionStart &&
                regionEnd == variant.regionEnd &&
                decision == variant.decision &&
                chr == variant.chr &&
                Objects.equals(tumorSample, variant.tumorSample) &&
                Objects.equals(ref, variant.ref) &&
                Objects.equals(alt, variant.alt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(decision, chr, tumorSample, ref, alt, regionStart, regionEnd);
    }
}
