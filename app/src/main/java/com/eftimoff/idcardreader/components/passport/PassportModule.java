package com.eftimoff.idcardreader.components.passport;

import android.content.Context;

import com.eftimoff.idcardreader.components.passport.area.AreaHelper;

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
    AreaHelper provideAreaHelper() {
        return new AreaHelper(context);
    }

    @Provides
    @Singleton
    PassportService provideCountryService(final AreaHelper areaHelper) {
        return new HardcodedPassportService(areaHelper);
    }
}