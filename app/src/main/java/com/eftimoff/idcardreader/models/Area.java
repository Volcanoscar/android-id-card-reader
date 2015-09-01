package com.eftimoff.idcardreader.models;

import android.graphics.Rect;

import java.io.Serializable;
import java.util.List;

public class Area implements Serializable {

    private List<AreaRect> rects;

    private Rect areaRect;

    private Area(Builder builder) {
        this.rects = builder.rects;
        this.areaRect = builder.areaRect;
    }

    public static Builder newArea() {
        return new Builder();
    }

    public List<AreaRect> getRects() {
        return rects;
    }

    public Rect getAreaRect() {
        return areaRect;
    }


    public static final class Builder {
        private List<AreaRect> rects;
        private Rect areaRect;

        private Builder() {
        }

        public Area build() {
            return new Area(this);
        }

        public Builder rects(List<AreaRect> rects) {
            this.rects = rects;
            return this;
        }

        public Builder areaRect(Rect areaRect) {
            this.areaRect = areaRect;
            return this;
        }
    }
}
