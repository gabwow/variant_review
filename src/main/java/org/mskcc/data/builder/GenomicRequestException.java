package org.mskcc.data.builder;


public class GenomicRequestException extends RuntimeException {
    public GenomicRequestException(String message) {
        super(message);
    }

    public GenomicRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenomicRequestException(Throwable cause){
        super(cause);
    }
}
