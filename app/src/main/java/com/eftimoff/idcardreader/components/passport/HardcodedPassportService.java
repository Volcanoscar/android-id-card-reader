package com.eftimoff.idcardreader.components.passport;

import com.eftimoff.idcardreader.components.idcard.constructor.IdCardConstructor;
import com.eftimoff.idcardreader.components.idcard.constructor.bulgaria.BulgarianIdCardConstructor;
import com.eftimoff.idcardreader.components.passport.area.AreaHelper;
import com.eftimoff.idcardreader.models.IdArea;
import com.eftimoff.idcardreader.models.IdAreaField;
import com.eftimoff.idcardreader.models.Passport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class HardcodedPassportService implements PassportService {

    private AreaHelper areaHelper;

    private List<Passport> countries = new ArrayList<Passport>();

    public HardcodedPassportService(final AreaHelper areaHelper) {
        this.areaHelper = areaHelper;
        countries.add(createBulgarianPassport());
//        countries.add(Passport.newPassport().name("Senegal").language("bul").flagImage("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Flag_of_Senegal.svg/1280px-Flag_of_Senegal.svg.png").passportEnums(EnumSet.of(PassportEnum.ID_CARD_FRONT, PassportEnum.ID_CARD_BACK)).build());
//        countries.add(Passport.newPassport().name("Mali").language("bul").flagImage("http://www.crwflags.com/fotw/images/m/ml.gif").passportEnums(EnumSet.of(PassportEnum.PASSPORT)).build());
    }

    @Override
    public void addCountry(final Passport passport) {
        countries.add(passport);
    }

    @Override
    public Observable<List<Passport>> getPassport() {
        return Observable.just(countries);
    }

    private Passport createBulgarianPassport() {
        final IdAreaField idNumber = IdAreaField.newAreaRect()
                .percentageFromParentLeft(15)
                .percentageFromParentTop(24)
                .percentageWidth(19)
                .percentageHeight(7)
                .name("Id Number")
                .field(IdAreaField.Field.ID)
                .build();

        final IdAreaField firstName = IdAreaField.newAreaRect()
                .percentageFromParentLeft(47)
                .percentageFromParentTop(33)
                .percentageWidth(19)
                .percentageHeight(7)
                .name("First Name")
                .field(IdAreaField.Field.FIRST_NAME)
                .build();

        final IdAreaField lastName = IdAreaField.newAreaRect()
                .percentageFromParentLeft(48)
                .percentageFromParentTop(22)
                .percentageWidth(21)
                .percentageHeight(7)
                .name("Last Name")
                .field(IdAreaField.Field.LAST_NAME)
                .build();

        final IdAreaField middleName = IdAreaField.newAreaRect()
                .percentageFromParentLeft(49)
                .percentageFromParentTop(46)
                .percentageWidth(21)
                .percentageHeight(7)
                .name("Middle Name")
                .field(IdAreaField.Field.MIDDLE_NAME)
                .build();

        final IdAreaField gender = IdAreaField.newAreaRect()
                .percentageFromParentLeft(53)
                .percentageFromParentTop(59)
                .percentageWidth(3)
                .percentageHeight(7)
                .name("Gender")
                .field(IdAreaField.Field.GENDER)
                .build();

        final IdAreaField personalNumber = IdAreaField.newAreaRect()
                .percentageFromParentLeft(76)
                .percentageFromParentTop(59)
                .percentageWidth(21)
                .percentageHeight(7)
                .name("Personal Number")
                .field(IdAreaField.Field.PERSONAL_NUMBER)
                .build();

        final IdAreaField dateOfBirth = IdAreaField.newAreaRect()
                .percentageFromParentLeft(67)
                .percentageFromParentTop(72)
                .percentageWidth(21)
                .percentageHeight(7)
                .name("Date of birth")
                .field(IdAreaField.Field.DATE_OF_BIRTH)
                .build();

        final IdAreaField expirationDate = IdAreaField.newAreaRect()
                .percentageFromParentLeft(67)
                .percentageFromParentTop(77)
                .percentageWidth(21)
                .percentageHeight(7)
                .name("Expiration Date")
                .field(IdAreaField.Field.EXPIRATION_DATE)
                .build();


        final IdArea idArea = IdArea.newArea()
                .areaRect(areaHelper.getDefaultIdCardSize())
                .rects(Arrays.asList(idNumber, firstName, lastName, middleName, gender, personalNumber, dateOfBirth, expirationDate))
                .build();

        final List<IdCardConstructor> idCardConstructors = new ArrayList<>();
        idCardConstructors.add(new BulgarianIdCardConstructor());

        return Passport.newPassport()
                .name("Bulgaria")
                .language("bul")
                .flagImage("http://www.mapsofworld.com/images/world-countries-flags/bulgeria-flag.gif")
                .supportedIdCardConstructors(idCardConstructors)
                .idCardConstructor(new BulgarianIdCardConstructor())
                .idArea(idArea)
                .build();
    }

}
