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
            case ADDRESS:
                idCard.setAddress(parseAddress(text));
                break;
            case ID_CARD_CREATED_DATE:
                idCard.setIdCreatedDate(parseIdCreatedDate(text));
                break;
            case HEIGHT:
                idCard.setHeight(parseHeight(text));
                break;
            case PLACE_OF_BIRTH:
                idCard.setPlaceOfBirth(parsePlaceOfBirth(text));
                break;
            default:
                break;
        }

    }

    @Override
    public IdCard construct() {
        return idCard;
    }

    protected abstract String parseIdNumber(final String text);

    protected abstract String parseFirstName(final String text);

    protected abstract String parseLastName(final String text);

    protected abstract String parseMiddleName(final String text);

    protected abstract IdCard.Gender parseGender(final String text);

    protected abstract String parsePersonalNumber(final String text);

    protected abstract long parseDateOfBirth(final String text);

    protected abstract long parseExpirationDate(final String text);

    protected abstract int parseHeight(final String text);

    protected abstract long parseIdCreatedDate(final String text);

    protected abstract String parseAddress(final String text);

    protected abstract String parsePlaceOfBirth(final String text);
}
