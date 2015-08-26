package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;

public interface Tesseract {

    void init(final String language, final SdCardDownloadListener sdCardDownloadListener);

    String getFromBitmap(final Bitmap bitmap);
}
