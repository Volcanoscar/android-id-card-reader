package com.eftimoff.idcardreader.ui.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.eftimoff.idcardreader.models.Area;

public class AreaView extends View {

    private Area area;
    private Paint areaPaint;

    public AreaView(final Context context) {
        super(context);
        init();
    }

    public AreaView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AreaView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AreaView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        areaPaint = new Paint();
        areaPaint.setColor(Color.BLACK);
        areaPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    public Area getArea() {
        return area;
    }

    public void setArea(final Area area) {
        this.area = area;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.save();

        drawArea(canvas);

        canvas.restore();
    }

    private void drawArea(final Canvas canvas) {
        if (area == null) {
            return;
        }
        final Rect areaRect = area.getAreaRect();

        fillOutsideRect(canvas, areaRect);
//        canvas.drawRoundRect(areaRect);
//        canvas.drawRect(areaRect, areaPaint);

    }

    private void fillOutsideRect(final Canvas canvas, final Rect rect) {
        final Rect above = new Rect(0, 0, canvas.getWidth(), rect.top);
        final Rect left = new Rect(0, rect.top, rect.left, rect.bottom);
        final Rect right = new Rect(rect.right, rect.top, canvas.getWidth(), rect.bottom);
        final Rect bottom = new Rect(0, rect.bottom, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(above, areaPaint);
        canvas.drawRect(left, areaPaint);
        canvas.drawRect(right, areaPaint);
        canvas.drawRect(bottom, areaPaint);
    }
}
