package com.eftimoff.idcardreader.models;

import android.graphics.Rect;

public class AreaRect {

    private String name;
    private Rect rect;

    private AreaRect(Builder builder) {
        this.name = builder.name;
        this.rect = builder.rect;
    }

    public static Builder newAreaRect() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Rect getRect() {
        return rect;
    }


    public static final class Builder {
        private String name;
        private Rect rect;

        private Builder() {
        }

        public AreaRect build() {
            return new AreaRect(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder rect(Rect rect) {
            this.rect = rect;
            return this;
        }
    }
}
