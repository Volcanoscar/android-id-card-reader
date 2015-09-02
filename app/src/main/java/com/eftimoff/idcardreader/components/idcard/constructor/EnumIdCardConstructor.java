package com.eftimoff.idcardreader.components.idcard.constructor;

import com.eftimoff.idcardreader.models.IdAreaField;
import com.eftimoff.idcardreader.models.IdCard;

public abstract class EnumIdCardConstructor implements IdCardConstructor {

    private final IdCard idCard;

    public EnumIdCardConstructor() {
        idCard = new IdCard();
    }

    @Override
    public void setText(final String text, final IdAreaField idAreaField) {
        final IdAreaField.Field field = idAreaField.getField();
        switch (field) {
            case ID:
                idCard.setId(parseIdNumber(text));
                break;
            case FIRST_NAME:
                idCard.setFirstName(parseFirstName(text));
                break;
            case LAST_NAME:
                idCard.setLastName(parseLastName(text));
                break;
            case MIDDLE_NAME:
                idCard.setMiddleName(parseMiddleName(text));
                break;
            case GENDER:
                idCard.setGender(parseGender(text));
                break;
            case PERSONAL_NUMBER:
                idCard.setPersonalNumber(parsePersonalNumber(text));
                break;
            case DATE_OF_BIRTH:
                idCard.setDateOfBirth(parseDateOfBirth(text));
                break;
            case EXPIRATION_DATE:
                idCard.setExpirationDate(parseExpirationDate(text));
                break;
            default:
                break;
        }

    }

    @Override
    public IdCard construct() {
        return idCard;
    }

    public abstract String parseIdNumber(final String text);

    public abstract String parseFirstName(final String text);

    public abstract String parseLastName(final String text);

    public abstract String parseMiddleName(final String text);

    public abstract IdCard.Gender parseGender(final String text);

    public abstract String parsePersonalNumber(final String text);

    public abstract long parseDateOfBirth(final String text);

    public abstract long parseExpirationDate(final String text);
}
