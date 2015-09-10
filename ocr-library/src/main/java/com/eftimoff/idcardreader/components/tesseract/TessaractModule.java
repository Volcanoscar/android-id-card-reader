package com.eftimoff.idcardreader.components.tesseract;

import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.images.yuv.YUVTesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.logger.android.AndroidTesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.text.numbersletters.NumbersLettersTesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.retrofit.RetrofitTrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.sdcard.SdCardTrainedDataManager;

public class TessaractModule {

    private boolean enableLogging;

    public TessaractModule(final boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    TesseractTextCleaner provideTesseractTextCleaner() {
        return new NumbersLettersTesseractTextCleaner();
    }

    TesseractBitmapConverter provideTesseractBitmapConverter() {
        return new YUVTesseractBitmapConverter();
    }

    TesseractLogger provideTesseractLogger() {
        return new AndroidTesseractLogger(enableLogging);
    }

    TrainedDataDownloader provideTrainedDataDownloader() {
        return RetrofitTrainedDataDownloader.getInstance();
    }

    TrainedDataManager provideTrainedDataManager(final TrainedDataDownloader trainedDataDownloader) {
        return new SdCardTrainedDataManager(trainedDataDownloader);
    }

    Tesseract provideTesseract(final TrainedDataManager trainedDataManager, final TesseractLogger tesseractLogger, final TesseractBitmapConverter tesseractBitmapConverter, final TesseractTextCleaner tesseractTextCleaner) {
        return new TesseractImpl(trainedDataManager, tesseractLogger, tesseractBitmapConverter, tesseractTextCleaner);
    }
}