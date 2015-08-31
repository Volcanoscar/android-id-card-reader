package com.eftimoff.idcardreader.ui.camera;

import com.eftimoff.idcardreader.models.Passport;

import java.io.Serializable;

public class ShowCameraSettings implements Serializable {

    private Passport passport;

    private ShowCameraSettings(Builder builder) {
        this.passport = builder.passport;
    }

    public static Builder newShowCameraSettings() {
        return new Builder();
    }

    public Passport getPassport() {
        return passport;
    }


    public static final class Builder {
        private Passport passport;

        private Builder() {
        }

        public ShowCameraSettings build() {
            return new ShowCameraSettings(this);
        }

        public Builder passport(Passport passport) {
            this.passport = passport;
            return this;
        }
    }
}
