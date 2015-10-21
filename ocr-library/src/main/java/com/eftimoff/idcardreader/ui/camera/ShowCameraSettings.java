package com.eftimoff.idcardreader.ui.camera;

import com.eftimoff.idcardreader.models.Passport;

import java.io.Serializable;

public class ShowCameraSettings implements Serializable {

    private Passport passport;
    private boolean enableLogging;
    private boolean showTempResults;
    private int percentageToCapture;
    private String sdCardPath;

    private ShowCameraSettings(Builder builder) {
        this.passport = builder.passport;
        this.enableLogging = builder.enableLogging;
        this.showTempResults = builder.showTempResults;
        this.percentageToCapture = builder.percentageToCapture;
        this.sdCardPath = builder.sdCardPath;
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

    public boolean isShowTempResults() {
        return showTempResults;
    }

    public int getPercentageToCapture() {
        return percentageToCapture;
    }

    public String getSdCardPath() {
        return sdCardPath;
    }

    public boolean shouldWriteToSdCard() {
        return sdCardPath != null;
    }

    public static final class Builder {
        private Passport passport;
        private boolean enableLogging;
        private boolean showTempResults;
        private int percentageToCapture;
        private String sdCardPath;

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

        public Builder showTempResults(boolean showTempResults) {
            this.showTempResults = showTempResults;
            return this;
        }

        public Builder percentageToCapture(int percentageToCapture) {
            this.percentageToCapture = percentageToCapture;
            return this;
        }

        public Builder sdCardPath(final String sdCardPath) {
            this.sdCardPath = sdCardPath;
            return this;
        }
    }
}
