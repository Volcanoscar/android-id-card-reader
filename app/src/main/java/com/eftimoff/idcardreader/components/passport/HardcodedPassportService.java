package com.eftimoff.idcardreader.components.passport;

import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.models.PassportEnum;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import rx.Observable;

public class HardcodedPassportService implements PassportService {

    private List<Passport> countries = new ArrayList<Passport>();

    public HardcodedPassportService() {
        countries.add(Passport.newPassport().name("Bulgaria").language("bul").flagImage("http://www.mapsofworld.com/images/world-countries-flags/bulgeria-flag.gif").passportEnums(EnumSet.of(PassportEnum.PASSPORT, PassportEnum.ID_CARD_BACK)).build());
        countries.add(Passport.newPassport().name("Senegal").language("bul").flagImage("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Flag_of_Senegal.svg/1280px-Flag_of_Senegal.svg.png").passportEnums(EnumSet.of(PassportEnum.ID_CARD_FRONT, PassportEnum.ID_CARD_BACK)).build());
        countries.add(Passport.newPassport().name("Mali").language("bul").flagImage("http://www.crwflags.com/fotw/images/m/ml.gif").passportEnums(EnumSet.of(PassportEnum.PASSPORT)).build());
    }

    @Override
    public void addCountry(final Passport passport) {
        countries.add(passport);
    }

    @Override
    public Observable<List<Passport>> getCountries() {
        return Observable.just(countries);
    }

}
