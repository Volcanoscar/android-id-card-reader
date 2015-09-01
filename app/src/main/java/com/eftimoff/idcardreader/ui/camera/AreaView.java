package com.eftimoff.idcardreader.ui.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.eftimoff.idcardreader.models.Area;
import com.eftimoff.idcardreader.models.AreaRect;

public class AreaView extends View {

    private Area area;
    private Paint areaPaint;
    private Paint innerPaint;
    private Paint textPaint;
    private int currentInnerPosition = -1;

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
        areaPaint.setAntiAlias(true);
        areaPaint.setDither(true);
        areaPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setAntiAlias(true);
        innerPaint.setDither(true);
        innerPaint.setStyle(Paint.Style.STROKE);
        innerPaint.setStrokeWidth(3);
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(42);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(final Area area) {
        this.area = area;
        invalidate();
        requestLayout();
    }

    public AreaRect incrementPosition() {
        currentInnerPosition = 0;
        invalidate();
        requestLayout();
        return getAreaRectForPosition(currentInnerPosition);
    }

    private AreaRect getAreaRectForPosition(final int position) {
        if (position >= area.getRects().size()) {
            return null;
        }
        return area.getRects().get(position);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.save();

        drawArea(canvas);
        drawInnerAreas(canvas);

        canvas.restore();
    }


    private void drawArea(final Canvas canvas) {
        if (area == null) {
            return;
        }
        final Rect areaRect = area.getAreaRect();

        fillOutsideRect(canvas, areaRect);
    }

    private void drawInnerAreas(final Canvas canvas) {
        if (area == null || currentInnerPosition == -1) {
            return;
        }
        final Rect parentRect = area.getAreaRect();
        for (int i = 0; i <= currentInnerPosition; i++) {
            final Rect rectForInnerArea = createRectForInnerArea(parentRect, i);
            if (rectForInnerArea != null) {
                final AreaRect areaRect = area.getRects().get(i);
                canvas.drawRect(rectForInnerArea, innerPaint);
                canvas.drawText(areaRect.getName(), rectForInnerArea.left, rectForInnerArea.top - 30, textPaint);
            }
        }
    }

    private Rect createRectForInnerArea(final Rect parentRect, final int position) {
        if (parentRect == null || position == -1 || position >= area.getRects().size()) {
            return null;
        }
        final AreaRect areaRect = area.getRects().get(position);
        final int left = parentRect.left + areaRect.getPercentageFromParentLeft() * parentRect.width() / 100;
        final int top = parentRect.top + areaRect.getPercentageFromParentTop() * parentRect.height() / 100;
        final int right = left + areaRect.getPercentageWidth() * parentRect.width() / 100;
        final int bottom = top + areaRect.getPercentageHeight() * parentRect.height() / 100;
        return new Rect(left, top, right, bottom);
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
