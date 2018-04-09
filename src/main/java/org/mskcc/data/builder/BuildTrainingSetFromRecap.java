package org.mskcc.data.builder;

public class BuildTrainingSetFromRecap {
    enum Environment { DESKTOP,CLUSTSER;}

    BamVisualizer visualizer;
    Environment runEnvironment;


    public BuildTrainingSetFromRecap(){
        visualizer = new IgvRunner();
        runEnvironment = Environment.CLUSTSER;
    }

    public void processFiles(String inputMafDirectory, String inputBamDirectory){

    }

    public void executeBuildStep(){
        switch (runEnvironment) {
            case DESKTOP:
                break;
            case CLUSTSER:

        }
    }
}
