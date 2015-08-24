package com.eftimoff.idcardreader.components.country;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {CountryModule.class})
public interface CountryComponent {

    CountryService provideCountryService();

}