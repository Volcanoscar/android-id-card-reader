package com.eftimoff.idcardreader.ui.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * View that show the camera preview.
 * <p/>
 * Created by georgi.eftimov on 3/30/2015.
 */
public class ShowCameraView extends SurfaceView implements SurfaceHolder.Callback {

    public static final String TAG = ShowCameraView.class.getSimpleName();
    private final CameraFragment.Drawings drawings;

    private Paint paint = new Paint();
    private Paint textPaint = new Paint();

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private RectF bounds;

    public ShowCameraView(Context context, Camera camera, final CameraFragment.Drawings drawings) {
        super(context);
        mCamera = camera;
        this.drawings = drawings;
        if (drawings.ordinal() != CameraFragment.Drawings.EMPTY.ordinal()) {
            setWillNotDraw(false);
        }
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (CameraFragment.Drawings.FACE.ordinal() == drawings.ordinal()) {
            //It is complicated.
            //The face is oval but the perfect oval face is with proportions 9h/7w ,
            //also it needs to be in the center of the screen.
            //But the orientation is reverse so the left is bottom and etc.
            final float left = w * 0.1f;//bottom
            final float right = w * 0.9f;//top
            final float diff = (right - left) * 7 / 9;
            final float top = h / 2 - diff / 2;//left
            final float bottom = h / 2 + diff / 2;//right
            bounds = new RectF(left, left, right, right);
            return;
        }

        if (CameraFragment.Drawings.ID_CARD.ordinal() == drawings.ordinal()) {
            final float width = h * 0.6f;
            final float top = h * 0.2f;
            final float bottom = h * 0.8f;
            final float height = (float) (width / 1.58);
            final float left = w / 2 - height / 2;
            final float right = w / 2 + height / 2;
            bounds = new RectF(left, top, right, bottom);
        }


    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, 0);
        if (CameraFragment.Drawings.FACE.ordinal() == drawings.ordinal()) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 100, paint);
        } else if (CameraFragment.Drawings.ID_CARD.ordinal() == drawings.ordinal()) {
            canvas.drawRect(bounds, paint);
        }
//        canvas.drawText(text, bounds.left, bounds.bottom - 20, textPaint);
        canvas.restore();
    }
}