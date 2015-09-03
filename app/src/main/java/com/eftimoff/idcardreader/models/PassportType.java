package com.eftimoff.idcardreader.models;

import com.eftimoff.idcardreader.components.idcard.constructor.IdCardConstructor;
import com.eftimoff.idcardreader.components.idcard.constructor.bulgaria.BulgarianIdCardConstructor;

public enum PassportType {

    BULGARIAN_ID_CARD_OLD(new BulgarianIdCardConstructor());

    private final IdCardConstructor idCardConstructor;

    PassportType(final IdCardConstructor idCardConstructor) {
        this.idCardConstructor = idCardConstructor;
    }

    public IdCardConstructor getIdCardConstructor() {
        return idCardConstructor;
    }
}
