package com.eftimoff.idcardreader.components.country;

import com.eftimoff.idcardreader.models.Country;

public interface CountryService {

    void addCountry(final Country country);

    void getCountries(final CountryGetterListener countryGetterListener);

}
