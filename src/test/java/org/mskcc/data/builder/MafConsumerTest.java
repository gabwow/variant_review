package org.mskcc.data.builder;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class MafConsumerTest {
    MafConsumer consumer;


    @Test
    public void buildsVariants_fromLine(){
        //given active reader
        MafReader reader = new MafReaderStub();
        consumer = new MafConsumer();
        consumer.setMafReader(reader);

        //when a read happens
        Variant variant = consumer.getNextVariant();

        //then
        assertThat(variant.getRef()).isEqualTo("A");
        assertThat(variant.getAlt()).isEqualTo("C");
        assertThat(variant.getTumorSample()).isEqualTo("NOT_BAD_TUMOR");
        assertThat(variant.getChr()).isEqualTo(Chrome.CHR7);
        assertThat(variant.getDecision()).isEqualTo(CallDecision.APPROVED);
        assertThat(variant.getRegionStart()).isEqualTo(10000);
        assertThat(variant.getRegionEnd()).isEqualTo(10001);


    }

    @Test
    public void buildsSecondVariantCorrectly(){
        //given active reader
        MafReader reader = new MafReaderStub();
        consumer = new MafConsumer();
        consumer.setMafReader(reader);

        //when two reads happen
        Variant v1 = consumer.getNextVariant();
        Variant variant2 = consumer.getNextVariant();

        //then
        assertThat(variant2.getRef()).isEqualTo("G");
        assertThat(variant2.getAlt()).isEqualTo("T");
        assertThat(variant2.getTumorSample()).isEqualTo("NOT_BAD_TUMOR");
        assertThat(variant2.getChr()).isEqualTo(Chrome.CHR7);
        assertThat(variant2.getDecision()).isEqualTo(CallDecision.APPROVED);
    }

    @Test
    public void buildEmptyVariantAtEndOfReader(){
        //given active reader
        MafReader reader = new MafReaderStub();
        consumer = new MafConsumer();
        consumer.setMafReader(reader);

        //when all data read
        Variant v1 = consumer.getNextVariant();
        Variant v2 = consumer.getNextVariant();
        Variant v3 = consumer.getNextVariant();
        Variant sentinelVariant = consumer.getNextVariant();

        //then this variant should equal an empty variant
        Variant emptyVariant = new Variant();
        assertThat(sentinelVariant).isEqualTo(emptyVariant);

    }
}
