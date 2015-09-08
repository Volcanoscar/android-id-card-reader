package com.eftimoff.idcardreader.components.passport.service.assets;

import com.eftimoff.idcardreader.components.passport.area.AreaHelper;
import com.eftimoff.idcardreader.components.passport.service.PassportService;
import com.eftimoff.idcardreader.components.passport.service.assets.converter.PassportConverter;
import com.eftimoff.idcardreader.components.passport.service.assets.reader.FileReader;
import com.eftimoff.idcardreader.models.IdAreaField;
import com.eftimoff.idcardreader.models.Passport;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class AssetsPassportService implements PassportService {

    private static final String FILE_NAME = "passports/passports.json";

    private final AreaHelper areaHelper;
    private final FileReader fileReader;
    private final PassportConverter converter;

    public AssetsPassportService(final FileReader fileReader, final AreaHelper areaHelper, final PassportConverter converter) {
        this.fileReader = fileReader;
        this.areaHelper = areaHelper;
        this.converter = converter;
    }

    @Override
    public Observable<List<Passport>> getPassports() {
        return fileReader.readFromFile(FILE_NAME)
                .flatMap(new Func1<String, Observable<List<Passport>>>() {
                    @Override
                    public Observable<List<Passport>> call(final String s) {
                        return Observable.just(converter.convertTo(s));
                    }
                }).map(new Func1<List<Passport>, List<Passport>>() {
                    @Override
                    public List<Passport> call(final List<Passport> passports) {
                        for (final Passport passport : passports) {
                            final List<IdAreaField> rects = passport.getIdArea().getRects();
                            for (final IdAreaField fields : rects) {
                                fields.setName(fields.getField().getStringName());
                            }
                            passport.getIdArea().setAreaRect(areaHelper.getDefaultIdCardSize());
                        }
                        return passports;
                    }
                });
    }
}
