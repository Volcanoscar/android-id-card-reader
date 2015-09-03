package com.eftimoff.idcardreader.components.tesseract;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.eftimoff.idcardreader.components.tesseract.models.TesseractResult;
import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.listeners.ProgressListener;
import com.eftimoff.idcardreader.components.tesseract.listeners.GzipFileDownloadListener;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;
import com.google.common.base.Preconditions;
import com.googlecode.leptonica.android.Pixa;
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
    private final TesseractBitmapConverter tesseractBitmapConverter;
    private final TesseractTextCleaner tesseractTextCleaner;

    public TesseractImpl(final TrainedDataManager trainedDataManager, final TesseractLogger tesseractLogger, final TesseractBitmapConverter tesseractBitmapConverter, final TesseractTextCleaner tesseractTextCleaner) {
        this.trainedDataManager = trainedDataManager;
        this.tesseractLogger = tesseractLogger;
        this.tesseractBitmapConverter = tesseractBitmapConverter;
        this.tesseractTextCleaner = tesseractTextCleaner;

        tesseractLogger.log("Constructing Tesseract api.");

        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "°");
    }

    @Override
    public void init(final String language, final ProgressListener progressListener) {
        Preconditions.checkNotNull(language, "Language must not be null.");

        tesseractLogger.log("Tesseract.init() for language %s", language);

        final boolean sdCardFileExists = trainedDataManager.sdCardFileExists(language);
        tesseractLogger.log("Tesseract.init() Does path of trained data exists : %b", sdCardFileExists);
        if (sdCardFileExists) {
            final String trainedDataPath = trainedDataManager.getTrainedDataPath();
            notifyOnStart(progressListener);
            try {
                tessBaseAPI.init(trainedDataPath, language);
            } catch (IllegalArgumentException e) {
                notifyOnError(e, progressListener);
                return;
            }
            notifyOnDone(progressListener);
            return;
        }

        trainedDataManager.downloadFile(language, new GzipFileDownloadListener() {
            @Override
            public void onStart() {
                tesseractLogger.log("Tesseract.init() Start downloading the file to sd card.");
                notifyOnStart(progressListener);
            }

            @Override
            public void onDone(final String sdCardFilePath) {
                tesseractLogger.log("Tesseract.init() Done downloading the file to sd card : %s", sdCardFilePath);
                final String trainedDataPath = trainedDataManager.getTrainedDataPath();
                tessBaseAPI.init(trainedDataPath, language);
                notifyOnDone(progressListener);
            }

            @Override
            public void onError(final Throwable e) {
                tesseractLogger.error(e);
                notifyOnError(e, progressListener);
            }
        });

    }

    private void notifyOnError(final Throwable e, final ProgressListener progressListener) {
        if (progressListener != null) {
            progressListener.onError(e);
        }
    }

    private void notifyOnDone(final ProgressListener progressListener) {
        if (progressListener != null) {
            progressListener.onDone();
        }
    }

    private void notifyOnStart(final ProgressListener progressListener) {
        if (progressListener != null) {
            progressListener.onStart();
        }
    }

    @Override
    public Observable<TesseractResult> getFromBitmap(final byte[] array, final int width, final int height, final Rect rect) {
        return Observable.create(new Observable.OnSubscribe<TesseractResult>() {
            @Override
            public void call(final Subscriber<? super TesseractResult> subscriber) {

                tesseractLogger.log("Tesseract.getFromBitmap() Getting text from bitmap ");
                try {
                    final Bitmap bitmap = tesseractBitmapConverter.convertToBitmap(array, width, height, rect);

                    final long start = System.currentTimeMillis();
                    tessBaseAPI.setImage(bitmap);
                    final String utf8Text = tessBaseAPI.getUTF8Text();
                    final String cleanText = tesseractTextCleaner.cleanText(utf8Text);
                    final long timeRequired = System.currentTimeMillis() - start;

                    final Pixa regions = tessBaseAPI.getRegions();
                    final Pixa textlines = tessBaseAPI.getTextlines();
                    final Pixa words = tessBaseAPI.getWords();
                    final Pixa strips = tessBaseAPI.getStrips();

                    final TesseractResult tesseractResult = new TesseractResult();
                    tesseractResult.setText(cleanText);
                    tesseractResult.setWordConfidences(tessBaseAPI.wordConfidences());
                    tesseractResult.setMeanConfidence(tessBaseAPI.meanConfidence());
                    tesseractResult.setRegionBoundingBoxes(regions.getBoxRects());

                    tesseractResult.setTextlineBoundingBoxes(textlines.getBoxRects());
                    tesseractResult.setWordBoundingBoxes(words.getBoxRects());
                    tesseractResult.setStripBoundingBoxes(strips.getBoxRects());
                    tesseractResult.setBitmap(bitmap);
                    tesseractResult.setRecognitionTimeRequired(timeRequired);

                    subscriber.onNext(tesseractResult);
                    subscriber.onCompleted();

                    regions.recycle();
                    textlines.recycle();
                    words.recycle();
                    strips.recycle();
                    tesseractLogger.log("Tesseract.getFromBitmap() Getting text from bitmap : " + utf8Text + " , time required : " + timeRequired + " ms.");
                } catch (Exception e) {
                    tesseractLogger.error(e);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
