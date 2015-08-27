package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;

import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;
import com.eftimoff.idcardreader.components.tesseract.listeners.DownloadListener;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;
import com.google.common.base.Preconditions;
import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Service for all methods with TesseractAPI.
 */
public class TesseractImpl implements Tesseract {

    private final TessBaseAPI tessBaseAPI;
    private final TrainedDataManager trainedDataManager;
    private final TesseractLogger tesseractLogger;

    public TesseractImpl(final TrainedDataManager trainedDataManager, final TesseractLogger tesseractLogger) {
        this.trainedDataManager = trainedDataManager;
        this.tesseractLogger = tesseractLogger;

        tesseractLogger.log("Constructing Tesseract api.");

        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
    }

    @Override
    public void init(final String language, final DownloadListener downloadListener) {
        Preconditions.checkNotNull(language, "Language must not be null.");

        tesseractLogger.log("Tesseract.init() for language %s", language);

        final boolean sdCardFileExists = trainedDataManager.sdCardFileExists(language);
        tesseractLogger.log("Tesseract.init() Does path of trained data exists : %b", sdCardFileExists);
        if (sdCardFileExists) {
            final String trainedDataPath = trainedDataManager.getTrainedDataPath();
            notifyOnStart(downloadListener);
            try {
                tessBaseAPI.init(trainedDataPath, language);
            } catch (IllegalArgumentException e) {
                notifyOnError(e, downloadListener);
                return;
            }
            notifyOnDone(downloadListener);
            return;
        }

        trainedDataManager.downloadFile(language, new GzipFileDownloadListener() {
            @Override
            public void onStart() {
                tesseractLogger.log("Tesseract.init() Start downloading the file to sd card.");
                notifyOnStart(downloadListener);
            }

            @Override
            public void onDone(final String sdCardFilePath) {
                tesseractLogger.log("Tesseract.init() Done downloading the file to sd card : %s", sdCardFilePath);
                final String trainedDataPath = trainedDataManager.getTrainedDataPath();
                tessBaseAPI.init(trainedDataPath, language);
                notifyOnDone(downloadListener);
            }

            @Override
            public void onError(final Throwable e) {
                tesseractLogger.error(e);
                notifyOnError(e, downloadListener);
            }
        });
    }

    private void notifyOnError(final Throwable e, final DownloadListener downloadListener) {
        if (downloadListener != null) {
            downloadListener.onError(e);
        }
    }

    private void notifyOnDone(final DownloadListener downloadListener) {
        if (downloadListener != null) {
            downloadListener.onDone();
        }
    }

    private void notifyOnStart(final DownloadListener downloadListener) {
        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    @Override
    public String getFromBitmap(final Bitmap bitmap) {
        tesseractLogger.log("Tesseract.getFromBitmap() Getting text from bitmap.");
        tessBaseAPI.setImage(bitmap);
        return tessBaseAPI.getUTF8Text();
    }
}
