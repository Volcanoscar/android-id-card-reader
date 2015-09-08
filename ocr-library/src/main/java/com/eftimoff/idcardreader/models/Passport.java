package com.eftimoff.idcardreader.models;

import java.io.Serializable;

public class Passport implements Serializable {

    private PassportType type;
    private String name;
    private String language;
    private String flagImage;
    private IdArea idArea;

    private Passport(Builder builder) {
        this.name = builder.name;
        this.language = builder.language;
        this.flagImage = builder.flagImage;
    }

    public static Builder newPassport() {
        return new Builder();
    }

    public PassportType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public IdArea getIdArea() {
        return idArea;
    }

    public static final class Builder {
        private String name;
        private String language;
        private String flagImage;

        private Builder() {
        }

        public Passport build() {
            return new Passport(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder flagImage(String flagImage) {
            this.flagImage = flagImage;
            return this;
        }

    }
}
