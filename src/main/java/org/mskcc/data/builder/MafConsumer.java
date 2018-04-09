package org.mskcc.data.builder;

public class MafConsumer implements VariantProvider {
    private MafReader reader;

    public void setMafReader(MafReader reader){
        this.reader = reader;
    }

    @Override
    public Variant getNextVariant() {
        Variant variantInRow = new Variant();
        if(!reader.isReady()) {
            return variantInRow;
        }
        variantInRow.alt(reader.getAlt()).ref(reader.getRef()).
                decision(reader.getDecision()).chr(reader.getChr()).
                regionStart(reader.getZeroBasedStart()).regionEnd(reader.getZeroBasedExclusiveEnd()).
                tumorSample(reader.getTumor()).normalSample(reader.getNormal());
        reader.loadNext();
        return variantInRow;
    }

}
