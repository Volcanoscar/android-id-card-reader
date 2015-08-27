package com.eftimoff.idcardreader.components.passport;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PassportModule {

    @Provides
    @Singleton
    PassportService provideCountryService() {
        return new HardcodedPassportService();
    }
}