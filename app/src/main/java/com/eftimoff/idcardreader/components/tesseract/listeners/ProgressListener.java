package com.eftimoff.idcardreader.components.tesseract.listeners;

public interface ProgressListener {

    void onStart();

    void onDone();

    void onError(final Throwable throwable);
}
