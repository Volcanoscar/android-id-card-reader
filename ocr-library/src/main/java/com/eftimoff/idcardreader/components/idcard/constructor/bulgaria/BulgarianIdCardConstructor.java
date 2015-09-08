package com.eftimoff.idcardreader.components.idcard.constructor.bulgaria;

import com.eftimoff.idcardreader.R;
import com.eftimoff.idcardreader.components.idcard.constructor.EnumIdCardConstructor;
import com.eftimoff.idcardreader.models.IdCard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BulgarianIdCardConstructor extends EnumIdCardConstructor {

    private static final String DATE_FORMATTER = "ddMMyyyy";
    private final DateFormat dateFormat;

    public BulgarianIdCardConstructor() {
        dateFormat = new SimpleDateFormat(DATE_FORMATTER);
    }

    @Override
    public int name() {
        return R.string.constructor_bulgarian_old;
    }

    @Override
    public String parseIdNumber(final String text) {
        return text;
    }

    @Override
    public String parseFirstName(final String text) {
        return text;
    }

    @Override
    public String parseLastName(final String text) {
        return text;
    }

    @Override
    public String parseMiddleName(final String text) {
        return text;
    }

    @Override
    public IdCard.Gender parseGender(final String text) {
        if (text.equalsIgnoreCase("M") || text.equalsIgnoreCase("М")) {
            return IdCard.Gender.MALE;
        }
        return IdCard.Gender.FEMALE;
    }

    @Override
    public String parsePersonalNumber(final String text) {
        return text;
    }

    @Override
    public long parseDateOfBirth(final String text) {
        return parseDateQuietly(text);
    }

    @Override
    public long parseExpirationDate(final String text) {
        return parseDateQuietly(text);
    }

    private long parseDateQuietly(final String text) {
        try {
            return dateFormat.parse(text).getTime() / 1000;
        } catch (Exception e) {
            return 0;
        }
    }

}
