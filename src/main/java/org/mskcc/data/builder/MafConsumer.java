package org.mskcc.data.builder;

import java.io.BufferedReader;

public class MafConsumer implements VariantProvider {
    private MafReader reader;

    public void setMafReader(MafReader reader){
        this.reader = reader;
    }

    @Override
    public Variant getNextVariant() {
        Variant variantInLine = new Variant();
        if(!reader.isReady()) {
            return variantInLine;
        }
        variantInLine.alt(reader.getAlt()).ref(reader.getRef()).
                decision(reader.getDecision()).chr(reader.getChr()).
                regionStart(reader.getStart()).regionEnd(reader.getEnd()).
                tumorSample(reader.getTumor()).normalSample(reader.getNormal());
        reader.loadNext();
        return variantInLine;
    }

}
