package com.eftimoff.idcardreader.components.tesseract;

import com.eftimoff.idcardreader.components.tesseract.images.TesseractBitmapConverter;
import com.eftimoff.idcardreader.components.tesseract.logger.TesseractLogger;
import com.eftimoff.idcardreader.components.tesseract.text.TesseractTextCleaner;
import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;

public class TessaractComponent {

    private final TessaractModule tessaractModule;

    public TessaractComponent(TessaractModule tessaractModule) {
        this.tessaractModule = tessaractModule;
    }

    public Tesseract provideTesseract() {
        final TesseractBitmapConverter tesseractBitmapConverter = tessaractModule.provideTesseractBitmapConverter();
        final TesseractLogger tesseractLogger = tessaractModule.provideTesseractLogger();
        final TesseractTextCleaner tesseractTextCleaner = tessaractModule.provideTesseractTextCleaner();
        final TrainedDataDownloader trainedDataDownloader = tessaractModule.provideTrainedDataDownloader();
        final TrainedDataManager trainedDataManager = tessaractModule.provideTrainedDataManager(trainedDataDownloader);
        return tessaractModule.provideTesseract(trainedDataManager, tesseractLogger, tesseractBitmapConverter, tesseractTextCleaner);
    }

}