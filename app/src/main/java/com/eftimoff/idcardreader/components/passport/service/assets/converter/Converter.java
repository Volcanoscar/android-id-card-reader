package com.eftimoff.idcardreader.components.passport.service.assets.converter;

public interface Converter<T> {

    T convertTo(final String text);

    String convertFrom(final T t);
}
