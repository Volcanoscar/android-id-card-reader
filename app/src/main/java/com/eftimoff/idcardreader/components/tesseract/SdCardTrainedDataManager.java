package com.eftimoff.idcardreader.components.tesseract;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class SdCardTrainedDataManager implements TrainedDataManager {

    //https://tesseract-ocr.googlecode.com/files/tesseract-ocr-3.02.eng.tar.gz

    private static final String SD_CARD_TRAINED_DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/tessdata/%s.traineddata";
    private static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tesseract/";

    private Context context;

    public SdCardTrainedDataManager(final Context context) {
        this.context = context;
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
}
