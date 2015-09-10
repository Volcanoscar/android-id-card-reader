package com.eftimoff.idcardreader.ui.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.eftimoff.idcardreader.components.tesseract.models.TesseractResult;
import com.eftimoff.idcardreader.models.IdArea;
import com.eftimoff.idcardreader.models.IdAreaField;

public class AreaView extends View {

    private IdArea idArea;
    private Paint areaPaint;
    private Paint innerPaint;
    private Paint textPaint;
    private int currentInnerPosition = -1;
    private AreaViewListener listener;
    private boolean showTempResults;
    private TesseractResult tempResult;
    private int statusBarHeight;

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
        statusBarHeight = getStatusBarHeight();

        areaPaint = new Paint();
        areaPaint.setColor(Color.parseColor("#CC000000"));
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

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                final IdAreaField idAreaField = start();
                notifyOnStart(idAreaField);
                v.setEnabled(false);
            }
        });
    }

    public IdArea getIdArea() {
        return idArea;
    }

    public void setIdArea(final IdArea idArea) {
        this.idArea = idArea;
        invalidate();
        requestLayout();
    }

    public IdAreaField increment(final String text) {
        final IdAreaField idAreaField = idArea.getRects().get(currentInnerPosition);
        idAreaField.setValue(text);
        currentInnerPosition++;
        invalidate();
        requestLayout();
        return getAreaRectForPosition(currentInnerPosition);
    }

    public IdAreaField start() {
        currentInnerPosition = 0;
        invalidate();
        return getAreaRectForPosition(currentInnerPosition);
    }

    private IdAreaField getAreaRectForPosition(final int position) {
        if (position >= idArea.getRects().size()) {
            return null;
        }
        return idArea.getRects().get(position);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.save();

        drawArea(canvas);
        drawInnerAreas(canvas);

        drawTempResult(canvas);

        canvas.restore();
    }

    private void drawTempResult(final Canvas canvas) {
        if (showTempResults && tempResult != null) {
            final String text = "Text : " + tempResult.getText();
            canvas.drawText(text, getRight() - textPaint.measureText(text), getTop() + statusBarHeight, textPaint);
            final String percentage = "Percentage : " + tempResult.getMeanConfidence();
            canvas.drawText(percentage, getRight() - textPaint.measureText(percentage), getTop() + statusBarHeight + textPaint.getTextSize(), textPaint);
        }
    }


    private void drawArea(final Canvas canvas) {
        if (idArea == null) {
            return;
        }
        final Rect areaRect = idArea.getAreaRect();
        // same constants as above except innerREctFillColor is not used. Instead:
        int outerFillColor = 0x77000000;

        // first create an off-screen bitmap and its canvas
        final Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas auxCanvas = new Canvas(bitmap);

        // then fill the bitmap with the desired outside color
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(outerFillColor);
        paint.setStyle(Paint.Style.FILL);
        auxCanvas.drawPaint(paint);

        // then punch a transparent hole in the shape of the rect
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        auxCanvas.drawRoundRect(new RectF(areaRect.left, areaRect.top, areaRect.right, areaRect.bottom), 50, 50, paint);

        // then draw the white rect border (being sure to get rid of the xfer mode!)
        paint.setXfermode(null);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        auxCanvas.drawRoundRect(new RectF(areaRect.left, areaRect.top, areaRect.right, areaRect.bottom), 50, 50, paint);

        // finally, draw the whole thing to the original canvas
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private void drawInnerAreas(final Canvas canvas) {
        if (currentInnerPosition == -1) {
            final Rect parentRect = idArea.getAreaRect();
            canvas.drawText("Place your ID card and click anywhere to start scanning.", parentRect.left, parentRect.bottom + 50, textPaint);
            return;
        }
        final Rect parentRect = idArea.getAreaRect();
        for (int i = 0; i <= currentInnerPosition; i++) {
            if (i >= idArea.getRects().size()) {
                notifyOnFinish();
                return;
            }
            final IdAreaField idAreaField = idArea.getRects().get(i);
            final Rect rectForInnerArea = createRectForInnerArea(parentRect, i);
            if (idAreaField.hasValue()) {
                canvas.drawText(idAreaField.getValue(), rectForInnerArea.left, rectForInnerArea.centerY(), textPaint);
                continue;
            }
            if (rectForInnerArea != null) {
                canvas.drawRect(rectForInnerArea, innerPaint);
                canvas.drawText(getResources().getString(idAreaField.getName()), rectForInnerArea.right + 10, rectForInnerArea.bottom, textPaint);
            }
        }
    }

    private Rect createRectForInnerArea(final Rect parentRect, final int position) {
        if (parentRect == null || position == -1 || position >= idArea.getRects().size()) {
            return null;
        }
        final IdAreaField idAreaField = idArea.getRects().get(position);
        final int left = parentRect.left + idAreaField.getPercentageFromParentLeft() * parentRect.width() / 100;
        final int top = parentRect.top + idAreaField.getPercentageFromParentTop() * parentRect.height() / 100;
        final int right = left + idAreaField.getPercentageWidth() * parentRect.width() / 100;
        final int bottom = top + idAreaField.getPercentageHeight() * parentRect.height() / 100;
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

    public void setListener(final AreaViewListener listener) {
        this.listener = listener;
    }

    private void notifyOnStart(final IdAreaField idAreaField) {
        if (listener != null) {
            listener.onStart(idAreaField);
        }
    }

    private void notifyOnFinish() {
        if (listener != null) {
            listener.onFinish();
        }
    }

    public void setShowTempResults(final boolean showTempResults) {
        this.showTempResults = showTempResults;
    }

    public void tempResult(final TesseractResult tempResult) {
        if (showTempResults) {
            this.tempResult = tempResult;
            invalidate();
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public interface AreaViewListener {

        void onStart(final IdAreaField idAreaField);

        void onFinish();
    }
}
