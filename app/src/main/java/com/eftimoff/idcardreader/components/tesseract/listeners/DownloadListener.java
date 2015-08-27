package com.eftimoff.idcardreader.components.tesseract.listeners;

public interface DownloadListener {

    void onStart();

    void onDone();

    void onError(final Throwable throwable);
}
