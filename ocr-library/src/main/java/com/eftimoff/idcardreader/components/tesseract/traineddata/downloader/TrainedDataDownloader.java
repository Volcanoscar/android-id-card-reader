package com.eftimoff.idcardreader.components.tesseract.traineddata.downloader;

import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;

public interface TrainedDataDownloader {

    void downloadTrainedFile(final String language, final GzipFileDownloadListener listener);
}
