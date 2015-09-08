package com.eftimoff.idcardreader.components.passport.service;

import com.eftimoff.idcardreader.models.Passport;

import java.util.List;

import rx.Observable;

public interface PassportService {

    Observable<List<Passport>> getPassports();

}
