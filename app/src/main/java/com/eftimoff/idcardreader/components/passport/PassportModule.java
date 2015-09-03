package com.eftimoff.idcardreader.components.passport;

import android.content.Context;

import com.eftimoff.idcardreader.components.passport.area.AreaHelper;
import com.eftimoff.idcardreader.components.passport.service.PassportService;
import com.eftimoff.idcardreader.components.passport.service.assets.AssetsPassportService;
import com.eftimoff.idcardreader.components.passport.service.assets.converter.PassportConverter;
import com.eftimoff.idcardreader.components.passport.service.assets.reader.FileReader;
import com.eftimoff.idcardreader.components.passport.service.assets.reader.assets.AssetFileReader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PassportModule {

    private Context context;

    public PassportModule(final Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    PassportConverter providePassportConverter() {
        return new PassportConverter();
    }

    @Provides
    @Singleton
    FileReader provideFileReader() {
        return new AssetFileReader(context);
    }

    @Provides
    @Singleton
    AreaHelper provideAreaHelper() {
        return new AreaHelper(context);
    }

    @Provides
    @Singleton
    PassportService provideCountryService(final FileReader fileReader, final AreaHelper areaHelper, final PassportConverter passportConverter) {
        return new AssetsPassportService(fileReader, areaHelper, passportConverter);
    }
}