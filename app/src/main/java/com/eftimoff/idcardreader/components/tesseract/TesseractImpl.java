package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.eftimoff.idcardreader.components.tesseract.listeners.DownloadListener;
import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;
import com.eftimoff.idcardreader.components.tesseract.traineddata.models.PlanarYUVLuminanceSource;
import com.google.common.base.Preconditions;
import com.googlecode.tesseract.android.TessBaseAPI;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "АБВГДЕЖЗИЙКЛМНОПРСУФХЦЧШЩЪЮЯабвгдежзийклмнопрстуфхцчшщъюя0123456789");
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
    public Observable<TesseractResult> getFromBitmap(final byte[] array, final Rect rect) {

        return Observable.create(new Observable.OnSubscribe<TesseractResult>() {
            @Override
            public void call(final Subscriber<? super TesseractResult> subscriber) {
                Bitmap bitmap = buildLuminanceSource(array, rect.width(), rect.height(), rect).renderCroppedGreyscaleBitmap();
                tesseractLogger.log("Tesseract.getFromBitmap() Getting text from bitmap : " + bitmap.toString());
                try {
                    final long start = System.currentTimeMillis();
                    tessBaseAPI.setImage(bitmap);
                    final String utf8Text = tessBaseAPI.getUTF8Text();
                    final long timeRequired = System.currentTimeMillis() - start;

                    final TesseractResult tesseractResult = new TesseractResult();
                    tesseractResult.setText(utf8Text);
                    tesseractResult.setWordConfidences(tessBaseAPI.wordConfidences());
                    tesseractResult.setMeanConfidence(tessBaseAPI.meanConfidence());
                    tesseractResult.setRegionBoundingBoxes(tessBaseAPI.getRegions().getBoxRects());
                    tesseractResult.setTextlineBoundingBoxes(tessBaseAPI.getTextlines().getBoxRects());
                    tesseractResult.setWordBoundingBoxes(tessBaseAPI.getWords().getBoxRects());
                    tesseractResult.setStripBoundingBoxes(tessBaseAPI.getStrips().getBoxRects());
                    tesseractResult.setBitmap(bitmap);
                    tesseractResult.setRecognitionTimeRequired(timeRequired);

                    subscriber.onNext(tesseractResult);
                    subscriber.onCompleted();
                    tesseractLogger.log("Tesseract.getFromBitmap() Getting text from bitmap : " + utf8Text);
                } catch (Exception e) {
                    tesseractLogger.error(e);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height, final Rect rect) {
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
    }
}
