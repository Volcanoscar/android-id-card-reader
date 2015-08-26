package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;

import com.google.common.base.Preconditions;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Service for all methods with TesseractAPI.
 */
public class TesseractImpl implements Tesseract {

    private final TessBaseAPI tessBaseAPI;
    private final TrainedDataManager trainedDataManager;

    public TesseractImpl(final TrainedDataManager trainedDataManager) {
        this.trainedDataManager = trainedDataManager;

        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
    }

    @Override
    public void init(final String language, final SdCardDownloadListener sdCardDownloadListener) {
        Preconditions.checkNotNull(language, "Language must not be null.");
        final boolean sdCardFileExists = trainedDataManager.sdCardFileExists(language);
        if (sdCardDownloadListener != null) {
            sdCardDownloadListener.onStart();
        }
        if (sdCardFileExists) {
            final String trainedDataPath = trainedDataManager.getTrainedDataPath();
            try {
                tessBaseAPI.init(trainedDataPath, language);
            } catch (IllegalArgumentException e) {
                if (sdCardDownloadListener != null) {
                    sdCardDownloadListener.onError(e);
                }
            }
            if (sdCardDownloadListener != null) {
                sdCardDownloadListener.onDone();
            }
        }
    }

    @Override
    public String getFromBitmap(final Bitmap bitmap) {
        tessBaseAPI.setImage(bitmap);
        return tessBaseAPI.getUTF8Text();
    }
}
