package com.eftimoff.idcardreader.components.passport;

import android.app.Activity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PassportModule.class})
public interface PassportComponent {

    PassportService provideCountryService();

}