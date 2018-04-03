package org.mskcc.data.builder;

import java.io.IOException;
import java.net.UnknownHostException;

public interface BamVisualizer {
    public void init();
    public String loadFile(String filePath);
    public String loadGenome(String genomePath);
    public String gotoRegion(final Chrome r, final long regionStart, final long regionEnd);
    public String savePicture();

}
