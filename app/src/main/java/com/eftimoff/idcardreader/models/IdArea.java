package com.eftimoff.idcardreader.models;

import android.graphics.Rect;

import java.io.Serializable;
import java.util.List;

public class IdArea implements Serializable {

    private List<IdAreaField> rects;

    private transient Rect areaRect;

    private IdArea(Builder builder) {
        this.rects = builder.rects;
        this.areaRect = builder.areaRect;
    }

    public static Builder newArea() {
        return new Builder();
    }

    public List<IdAreaField> getRects() {
        return rects;
    }

    public Rect getAreaRect() {
        return areaRect;
    }


    public static final class Builder {
        private List<IdAreaField> rects;
        private Rect areaRect;

        private Builder() {
        }

        public IdArea build() {
            return new IdArea(this);
        }

        public Builder rects(List<IdAreaField> rects) {
            this.rects = rects;
            return this;
        }

        public Builder areaRect(Rect areaRect) {
            this.areaRect = areaRect;
            return this;
        }
    }
}
