package com.eftimoff.idcardreader.components.passport;

import com.eftimoff.idcardreader.models.Passport;

import java.util.List;

import rx.Observable;

public interface PassportService {

    void addCountry(final Passport passport);

    Observable<List<Passport>> getPassport();

}
