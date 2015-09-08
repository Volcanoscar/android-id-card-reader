package com.eftimoff.idcardreader.ui.camera;

import com.eftimoff.idcardreader.models.Passport;

import java.io.Serializable;

public class ShowCameraSettings implements Serializable {

    private Passport passport;
    private boolean enableLogging;

    private ShowCameraSettings(Builder builder) {
        this.passport = builder.passport;
        this.enableLogging = builder.enableLogging;
    }

    public static Builder newShowCameraSettings() {
        return new Builder();
    }

    public Passport getPassport() {
        return passport;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public static final class Builder {
        private Passport passport;
        private boolean enableLogging;

        private Builder() {
        }

        public ShowCameraSettings build() {
            return new ShowCameraSettings(this);
        }

        public Builder passport(Passport passport) {
            this.passport = passport;
            return this;
        }

        public Builder enableLogging(boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }
    }
}
