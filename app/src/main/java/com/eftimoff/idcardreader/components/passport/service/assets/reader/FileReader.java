package com.eftimoff.idcardreader.components.passport.service.assets.reader;

import rx.Observable;

public interface FileReader {

    Observable<String> readFromFile(final String fileName);
}
