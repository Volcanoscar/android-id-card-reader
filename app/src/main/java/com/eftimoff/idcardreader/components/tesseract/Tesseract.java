package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;

import com.eftimoff.idcardreader.components.tesseract.listeners.SdCardDownloadListener;

public interface Tesseract {

    void init(final String language, final SdCardDownloadListener sdCardDownloadListener);

    String getFromBitmap(final Bitmap bitmap);
}
