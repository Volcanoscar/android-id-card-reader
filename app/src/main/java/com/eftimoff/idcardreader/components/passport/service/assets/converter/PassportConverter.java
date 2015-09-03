package com.eftimoff.idcardreader.components.passport.service.assets.converter;

import com.eftimoff.idcardreader.models.Passport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PassportConverter implements Converter<List<Passport>> {

    private final Gson gson;

    public PassportConverter() {
        gson = new Gson();
    }


    @Override
    public List<Passport> convertTo(final String text) {
        final Type listType = new TypeToken<ArrayList<Passport>>() {
        }.getType();
        return gson.fromJson(text, listType);
    }

    @Override
    public String convertFrom(final List<Passport> passports) {
        final Type listType = new TypeToken<ArrayList<Passport>>() {
        }.getType();
        return gson.toJson(passports, listType);
    }

}
