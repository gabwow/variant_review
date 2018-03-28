package org.mskcc.data.builder;

import java.io.IOException;
import java.net.UnknownHostException;

public interface BamVisualizer {
    public void init() throws IOException, UnknownHostException;
    public String loadFile(String filePath) throws IOException;
    public String loadGenome(String genomePath) throws IOException;
    public String gotoRegion(final Chrome r, final long regionStart, final long regionEnd) throws IOException;
    public String savePicture() throws IOException;

}
