package com.eftimoff.idcardreader.models;

public class Country {

    private String name;
    private String flagImage;

    private Country(Builder builder) {
        this.name = builder.name;
        this.flagImage = builder.flagImage;
    }

    public static Builder newCountry() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(final String flagImage) {
        this.flagImage = flagImage;
    }


    public static final class Builder {
        private String name;
        private String flagImage;

        private Builder() {
        }

        public Country build() {
            return new Country(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder flagImage(String flagImage) {
            this.flagImage = flagImage;
            return this;
        }
    }
}
