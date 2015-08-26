package com.eftimoff.idcardreader.components.tesseract.listeners;

public interface SdCardDownloadListener {

    void onStart();

    void onDone();

    void onError(final Throwable throwable);

}
