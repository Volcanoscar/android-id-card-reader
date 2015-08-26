package com.eftimoff.idcardreader.components.tesseract;

public interface SdCardDownloadListener {

    void onStart();

    void onDone();

    void onError(final Throwable throwable);

}
