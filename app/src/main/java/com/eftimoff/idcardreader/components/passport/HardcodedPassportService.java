package com.eftimoff.idcardreader.components.passport;

import com.eftimoff.idcardreader.components.passport.area.AreaHelper;
import com.eftimoff.idcardreader.models.Area;
import com.eftimoff.idcardreader.models.AreaRect;
import com.eftimoff.idcardreader.models.Passport;
import com.eftimoff.idcardreader.models.PassportEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import rx.Observable;

public class HardcodedPassportService implements PassportService {

    private AreaHelper areaHelper;

    private List<Passport> countries = new ArrayList<Passport>();

    public HardcodedPassportService(final AreaHelper areaHelper) {
        this.areaHelper = areaHelper;
        countries.add(createBulgarianPassport());
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

    private Passport createBulgarianPassport() {
        final AreaRect number = AreaRect.newAreaRect()
                .percentageFromParentLeft(15)
                .percentageFromParentTop(24)
                .percentageWidth(20)
                .percentageHeight(7)
                .name("Id Number")
                .build();

        final Area area = Area.newArea()
                .areaRect(areaHelper.getDefaultIdCardSize())
                .rects(Collections.singletonList(number))
                .build();

        return Passport.newPassport()
                .name("Bulgaria")
                .language("bul")
                .flagImage("http://www.mapsofworld.com/images/world-countries-flags/bulgeria-flag.gif")
                .passportEnums(EnumSet.of(PassportEnum.PASSPORT, PassportEnum.ID_CARD_BACK))
                .area(area)
                .build();
    }

}
