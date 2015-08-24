package com.eftimoff.idcardreader.components.country;

import com.eftimoff.idcardreader.models.Country;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MockCountryService implements CountryService {

    private List<Country> countries = new ArrayList<Country>();

    public MockCountryService() {
        countries.add(Country.newCountry().name("Bulgaria").flagImage("http://www.mapsofworld.com/images/world-countries-flags/bulgeria-flag.gif").build());
        countries.add(Country.newCountry().name("Senegal").flagImage("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Flag_of_Senegal.svg/1280px-Flag_of_Senegal.svg.png").build());
        countries.add(Country.newCountry().name("Mali").flagImage("http://www.crwflags.com/fotw/images/m/ml.gif").build());
    }

    @Override
    public void addCountry(final Country country) {
        countries.add(country);
    }

    @Override
    public Observable<List<Country>> getCountries() {
        return Observable.just(countries);
    }

}
