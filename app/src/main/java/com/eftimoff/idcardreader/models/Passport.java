package com.eftimoff.idcardreader.models;

import com.eftimoff.idcardreader.components.idcard.constructor.IdCardConstructor;

import java.io.Serializable;
import java.util.List;

public class Passport implements Serializable {

    private String name;
    private String language;
    private String flagImage;
    private IdArea idArea;
    private IdCardConstructor idCardConstructor;
    private List<IdCardConstructor> supportedIdCardConstructors;

    private Passport(Builder builder) {
        this.name = builder.name;
        this.language = builder.language;
        this.flagImage = builder.flagImage;
        this.idArea = builder.idArea;
        this.idCardConstructor = builder.idCardConstructor;
        this.supportedIdCardConstructors = builder.supportedIdCardConstructors;
    }

    public static Builder newPassport() {
        return new Builder();
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

    public IdCardConstructor getIdCardConstructor() {
        return idCardConstructor;
    }

    public List<IdCardConstructor> getSupportedIdCardConstructors() {
        return supportedIdCardConstructors;
    }

    public void setIdCardConstructor(final IdCardConstructor idCardConstructor) {
        this.idCardConstructor = idCardConstructor;
    }

    public static final class Builder {
        private String name;
        private String language;
        private String flagImage;
        private IdArea idArea;
        private IdCardConstructor idCardConstructor;
        private List<IdCardConstructor> supportedIdCardConstructors;

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

        public Builder idArea(IdArea idArea) {
            this.idArea = idArea;
            return this;
        }

        public Builder idCardConstructor(IdCardConstructor idCardConstructor) {
            this.idCardConstructor = idCardConstructor;
            return this;
        }

        public Builder supportedIdCardConstructors(List<IdCardConstructor> supportedIdCardConstructors) {
            this.supportedIdCardConstructors = supportedIdCardConstructors;
            return this;
        }
    }
}
