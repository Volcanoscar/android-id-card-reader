package com.eftimoff.idcardreader.components.country;

import com.eftimoff.idcardreader.models.Country;

import java.util.ArrayList;
import java.util.List;

public class MockCountryService implements CountryService {

    private List<Country> countries = new ArrayList<Country>();

    public MockCountryService() {
        countries.add(Country.newCountry().name("Bulgaria").build());
    }

    @Override
    public void addCountry(final Country country) {

    }

    @Override
    public void getCountries(final CountryGetterListener countryGetterListener) {

    }
}
