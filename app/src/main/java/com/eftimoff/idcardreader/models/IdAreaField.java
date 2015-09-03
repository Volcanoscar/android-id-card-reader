package com.eftimoff.idcardreader.models;

import com.eftimoff.idcardreader.R;

import java.io.Serializable;

public class IdAreaField implements Serializable {

    private int name;
    private int percentageFromParentLeft;
    private int percentageFromParentTop;
    private int percentageWidth;
    private int percentageHeight;
    private Field field;
    private String value;

    private IdAreaField(Builder builder) {
        this.name = builder.name;
        this.percentageFromParentLeft = builder.percentageFromParentLeft;
        this.percentageFromParentTop = builder.percentageFromParentTop;
        this.percentageWidth = builder.percentageWidth;
        this.percentageHeight = builder.percentageHeight;
        this.field = builder.field;
    }

    public static Builder newAreaRect() {
        return new Builder();
    }

    public static Builder newIdAreaField() {
        return new Builder();
    }

    public int getName() {
        return name;
    }

    public void setName(final int name) {
        this.name = name;
    }

    public int getPercentageFromParentLeft() {
        return percentageFromParentLeft;
    }

    public int getPercentageFromParentTop() {
        return percentageFromParentTop;
    }

    public int getPercentageWidth() {
        return percentageWidth;
    }

    public int getPercentageHeight() {
        return percentageHeight;
    }

    public Field getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean hasValue() {
        return value != null;
    }

    public static final class Builder {
        private int name;
        private int percentageFromParentLeft;
        private int percentageFromParentTop;
        private int percentageWidth;
        private int percentageHeight;
        private Field field;

        private Builder() {
        }

        public IdAreaField build() {
            return new IdAreaField(this);
        }

        public Builder name(int name) {
            this.name = name;
            return this;
        }

        public Builder percentageFromParentLeft(int percentageFromParentLeft) {
            this.percentageFromParentLeft = percentageFromParentLeft;
            return this;
        }

        public Builder percentageFromParentTop(int percentageFromParentTop) {
            this.percentageFromParentTop = percentageFromParentTop;
            return this;
        }

        public Builder percentageWidth(int percentageWidth) {
            this.percentageWidth = percentageWidth;
            return this;
        }

        public Builder percentageHeight(int percentageHeight) {
            this.percentageHeight = percentageHeight;
            return this;
        }

        public Builder field(Field field) {
            this.field = field;
            return this;
        }
    }

    public enum Field {
        ID(R.string.field_id_card), FIRST_NAME(R.string.field_first_name), LAST_NAME(R.string.field_last_name), MIDDLE_NAME(R.string.field_middle_name), GENDER(R.string.field_gender), PERSONAL_NUMBER(R.string.field_personal_number), DATE_OF_BIRTH(R.string.field_date_of_birth), EXPIRATION_DATE(R.string.field_expiration_date);

        private final int stringName;

        Field(final int stringName) {
            this.stringName = stringName;
        }

        public int getStringName() {
            return stringName;
        }
    }
}
