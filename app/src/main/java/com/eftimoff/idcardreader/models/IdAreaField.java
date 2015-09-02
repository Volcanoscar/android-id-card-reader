package com.eftimoff.idcardreader.models;

import java.io.Serializable;

public class IdAreaField implements Serializable {

    private String name;
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

    public String getName() {
        return name;
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
        private String name;
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

        public Builder name(String name) {
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
        ID, FIRST_NAME, MIDDLE_NAME, GENDER, PERSONAL_NUMBER, DATE_OF_BIRTH, EXPIRATION_DATE, LAST_NAME
    }
}
