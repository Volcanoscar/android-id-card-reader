package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Rect;

import com.eftimoff.idcardreader.components.tesseract.models.TesseractResult;
import com.eftimoff.idcardreader.components.tesseract.listeners.DownloadListener;

import rx.Observable;

public interface Tesseract {

    void init(final String language, final DownloadListener downloadListener);

    Observable<TesseractResult> getFromBitmap(final byte[] array, final int width, final int height, final Rect rect);
}
