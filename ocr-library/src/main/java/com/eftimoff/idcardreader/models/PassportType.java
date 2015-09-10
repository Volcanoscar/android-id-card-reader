package com.eftimoff.idcardreader.models;

import com.eftimoff.idcardreader.components.idcard.constructor.IdCardConstructor;
import com.eftimoff.idcardreader.components.idcard.constructor.bulgaria.BulgarianIdCardConstructor;
import com.eftimoff.idcardreader.components.idcard.constructor.senegal.SenegalIdCardConstructor;

public enum PassportType {

    BULGARIAN_ID_CARD_OLD(new BulgarianIdCardConstructor()), SENEGAL_ID_CARD(new SenegalIdCardConstructor());

    private final IdCardConstructor idCardConstructor;

    PassportType(final IdCardConstructor idCardConstructor) {
        this.idCardConstructor = idCardConstructor;
    }

    public IdCardConstructor getIdCardConstructor() {
        return idCardConstructor;
    }
}
