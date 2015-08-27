package com.eftimoff.idcardreader.models;

import java.util.EnumSet;

public class Passport {

    private String name;
    private String flagImage;
    private EnumSet<PassportEnum> passportEnums;
    private PassportEnum chosenPassportEnum;

    private Passport(Builder builder) {
        this.name = builder.name;
        this.flagImage = builder.flagImage;
        this.passportEnums = builder.passportEnums;
        this.chosenPassportEnum = builder.chosenPassportEnum;
    }

    public static Builder newPassport() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getFlagImage() {
        return flagImage;
    }

    public EnumSet<PassportEnum> getPassportEnums() {
        return passportEnums;
    }

    public PassportEnum getChosenPassportEnum() {
        return chosenPassportEnum;
    }

    public void setChosenPassportEnum(final PassportEnum chosenPassportEnum) {
        this.chosenPassportEnum = chosenPassportEnum;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Passport)) return false;

        final Passport passport = (Passport) o;

        if (name != null ? !name.equals(passport.name) : passport.name != null) return false;
        if (flagImage != null ? !flagImage.equals(passport.flagImage) : passport.flagImage != null)
            return false;
        if (passportEnums != null ? !passportEnums.equals(passport.passportEnums) : passport.passportEnums != null)
            return false;
        return chosenPassportEnum == passport.chosenPassportEnum;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (flagImage != null ? flagImage.hashCode() : 0);
        result = 31 * result + (passportEnums != null ? passportEnums.hashCode() : 0);
        result = 31 * result + (chosenPassportEnum != null ? chosenPassportEnum.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Passport{" +
                "name='" + name + '\'' +
                ", flagImage='" + flagImage + '\'' +
                ", passportEnums=" + passportEnums +
                ", chosenPassportEnum=" + chosenPassportEnum +
                '}';
    }

    public static final class Builder {
        private String name;
        private String flagImage;
        private EnumSet<PassportEnum> passportEnums;
        private PassportEnum chosenPassportEnum;

        private Builder() {
        }

        public Passport build() {
            return new Passport(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder flagImage(String flagImage) {
            this.flagImage = flagImage;
            return this;
        }

        public Builder passportEnums(EnumSet<PassportEnum> passportEnums) {
            this.passportEnums = passportEnums;
            return this;
        }

        public Builder chosenPassportEnum(PassportEnum chosenPassportEnum) {
            this.chosenPassportEnum = chosenPassportEnum;
            return this;
        }
    }
}
