package com.eftimoff.idcardreader.components.tesseract.listeners;

public interface GzipFileDownloadListener {

    void onStart();

    void onDone(final String sdCardFilePath);

    void onError(final Throwable throwable);

}
