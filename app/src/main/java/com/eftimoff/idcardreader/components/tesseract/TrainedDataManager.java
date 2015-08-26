package com.eftimoff.idcardreader.components.tesseract;

public interface TrainedDataManager {

    boolean sdCardFileExists(final String language);

    String getTrainedDataPath();
}
