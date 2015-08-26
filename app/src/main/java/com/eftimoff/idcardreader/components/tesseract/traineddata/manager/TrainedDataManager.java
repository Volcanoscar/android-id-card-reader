package com.eftimoff.idcardreader.components.tesseract.traineddata.manager;

import rx.Observable;

public interface TrainedDataManager {

    boolean sdCardFileExists(final String language);

    String getTrainedDataPath();

    Observable<String> downloadFile(final String language);
}
