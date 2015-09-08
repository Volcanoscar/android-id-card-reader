package com.eftimoff.idcardreader.components.tesseract;

import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.images.planar.PlanarYUVTesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.logger.android.AndroidTesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.text.numbersletters.NumbersLettersTesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.retrofit.RetrofitTrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.sdcard.SdCardTrainedDataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TessaractModule {

    @Provides
    @Singleton
    TesseractTextCleaner provideTesseractTextCleaner() {
        return new NumbersLettersTesseractTextCleaner();
    }

    @Provides
    @Singleton
    TesseractBitmapConverter provideTesseractBitmapConverter() {
        return new PlanarYUVTesseractBitmapConverter();
    }

    @Provides
    @Singleton
    TesseractLogger provideTesseractLogger() {
        return new AndroidTesseractLogger();
    }

    @Provides
    @Singleton
    TrainedDataDownloader provideTrainedDataDownloader() {
        return RetrofitTrainedDataDownloader.getInstance();
    }

    @Provides
    @Singleton
    TrainedDataManager provideTrainedDataManager(final TrainedDataDownloader trainedDataDownloader) {
        return new SdCardTrainedDataManager(trainedDataDownloader);
    }

    @Provides
    @Singleton
    Tesseract provideTesseract(final TrainedDataManager trainedDataManager, final TesseractLogger tesseractLogger, final TesseractBitmapConverter tesseractBitmapConverter, final TesseractTextCleaner tesseractTextCleaner) {
        return new TesseractImpl(trainedDataManager, tesseractLogger, tesseractBitmapConverter, tesseractTextCleaner);
    }
}