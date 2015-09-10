package com.eftimoff.idcardreader.components.passport;

import com.eftimoff.idcardreader.components.passport.service.PassportService;

public class PassportComponent {

    private final PassportModule passportModule;

    public PassportComponent(final PassportModule passportModule) {
        this.passportModule = passportModule;
    }


    public PassportService providePassportService() {
        return passportModule.provideCountryService(passportModule.provideFileReader(), passportModule.provideAreaHelper(), passportModule.providePassportConverter());
    }

}