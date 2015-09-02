package com.eftimoff.idcardreader.components.idcard.constructor;

import android.support.annotation.StringRes;

import com.eftimoff.idcardreader.models.IdAreaField;
import com.eftimoff.idcardreader.models.IdCard;

import java.io.Serializable;

public interface IdCardConstructor extends Serializable {

    @StringRes
    int name();

    void setText(final String text, final IdAreaField idAreaField);

    IdCard construct();
}
