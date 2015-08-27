package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;

import com.eftimoff.idcardreader.components.tesseract.listeners.DownloadListener;

public interface Tesseract {

    void init(final String language, final DownloadListener downloadListener);

    String getFromBitmap(final Bitmap bitmap);
}
