package com.eftimoff.idcardreader.models;

import java.io.Serializable;

public class AreaRect implements Serializable {

    private String name;
    private int percentageFromParentLeft;
    private int percentageFromParentTop;
    private int percentageWidth;
    private int percentageHeight;

    private AreaRect(Builder builder) {
        this.name = builder.name;
        this.percentageFromParentLeft = builder.percentageFromParentLeft;
        this.percentageFromParentTop = builder.percentageFromParentTop;
        this.percentageWidth = builder.percentageWidth;
        this.percentageHeight = builder.percentageHeight;
    }

    public static Builder newAreaRect() {
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

    public static final class Builder {
        private String name;
        private int percentageFromParentLeft;
        private int percentageFromParentTop;
        private int percentageWidth;
        private int percentageHeight;

        private Builder() {
        }

        public AreaRect build() {
            return new AreaRect(this);
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
    }
}
