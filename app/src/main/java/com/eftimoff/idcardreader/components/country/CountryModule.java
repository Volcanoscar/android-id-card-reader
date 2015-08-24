package com.eftimoff.idcardreader.components.country;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CountryModule {

    @Provides
    @Singleton
    CountryService provideCountryService() {
        return new MockCountryService();
    }
}