package org.mskcc.data.builder;

import java.util.HashMap;
import java.util.Map;

public enum Chrome {
    CHR1("chr1"),
    CHR2("chr2"),
    CHR3("chr3"),
    CHR4("chr4"),
    CHR5("chr5"),
    CHR6("chr6"),
    CHR7("chr7"),
    CHR8("chr8"),
    CHR9("chr9"),
    CHR10("chr10"),
    CHR11("chr11"),
    CHR12("chr12"),
    CHR13("chr13"),
    CHR14("chr14"),
    CHR15("chr15"),
    CHR16("chr16"),
    CHR17("chr17"),
    CHR18("chr18"),
    CHR19("chr19"),
    CHR20("chr20"),
    CHR21("chr21"),
    CHR22("chr22"),
    CHRX("chrX"),
    CHRY("chrY"),
    CHRM("chrM"),
    CHRFAIL("CHR_ERROR");
    private static final Map<String, Chrome> valueToEnum = new HashMap<>();
    static {
        for (Chrome c : Chrome.values()) {
            valueToEnum.put(c.value, c);
        }
    }


    private String value;
    Chrome(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return value;
    }

    public static Chrome fromString(String word){
        if(!valueToEnum.containsKey(word)){
            throw new GenomicRequestException(word + " isn't a valid chromosome");
        }
        return valueToEnum.get(word);
    }

}
