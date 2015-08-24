package com.eftimoff.idcardreader.components.country;

import com.eftimoff.idcardreader.models.Country;

import java.util.List;

import rx.Observable;

public interface CountryService {

    void addCountry(final Country country);

    Observable<List<Country>> getCountries();

}
