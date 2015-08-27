package com.eftimoff.idcardreader.components.passport;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PassportModule.class})
public interface PassportComponent {

    PassportService provideCountryService();

}