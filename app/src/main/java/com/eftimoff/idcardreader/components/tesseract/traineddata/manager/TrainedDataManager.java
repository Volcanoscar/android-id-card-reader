package com.eftimoff.idcardreader.components.tesseract.traineddata.manager;

import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;

public interface TrainedDataManager {

    boolean sdCardFileExists(final String language);

    String getTrainedDataPath();

    void downloadFile(final String language, final GzipFileDownloadListener listener);
}
