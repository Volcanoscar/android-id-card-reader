package com.eftimoff.idcardreader.components.tesseract.traineddata.manager.sdcard;

import android.os.Environment;

import com.eftimoff.idcardreader.components.tesseract.traineddata.downloader.TrainedDataDownloader;
import com.eftimoff.idcardreader.components.tesseract.traineddata.manager.TrainedDataManager;

import java.io.File;

import rx.Observable;

public class SdCardTrainedDataManager implements TrainedDataManager {

    private static final String SD_CARD_TRAINED_DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/tessdata/%s.traineddata";
    private static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/";

    private final TrainedDataDownloader trainedDataDownloader;

    public SdCardTrainedDataManager(final TrainedDataDownloader trainedDataDownloader) {
        this.trainedDataDownloader = trainedDataDownloader;
    }

    @Override
    public boolean sdCardFileExists(final String language) {
        final String sdCardTrainedFilePath = String.format(SD_CARD_TRAINED_DATA_PATH, language);
        final File file = new File(sdCardTrainedFilePath);
        return file.exists() && !file.isDirectory();
    }

    @Override
    public String getTrainedDataPath() {
        return SD_CARD_PATH;
    }

    @Override
    public Observable<String> downloadFile(final String language) {
        return trainedDataDownloader.downloadTrainedFile(language);
    }
}
