package com.eftimoff.idcardreader.ui.camera;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eftimoff.idcardreader.R;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Fragment displaying the camera and the some buttons.
 * <p/>
 * Created by georgi.eftimov on 3/30/2015.
 */
@SuppressWarnings("ALL")
public class CameraFragment extends Fragment {


    ///////////////////////////////////
    ///          CONSTANTS          ///
    ///////////////////////////////////

    private static final String KEY_USE_FRONT_CAMERA = "key_use_front_camera";
    private static final String KEY_USE_DRAWINGS = "key_use_drawings";


    ///////////////////////////////////
    ///            VIEWS            ///
    ///////////////////////////////////

    @Bind(R.id.fragmentCameraPreview)
    FrameLayout mFrameLayout;

    ///////////////////////////////////
    ///            FIELDS           ///
    ///////////////////////////////////

    private Camera camera;
    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private ShowCameraView showCameraViewView;
    private byte[] data;
    private int progressStatus;
    private Handler handler;

    public static CameraFragment getInstance(final boolean frontCamera) {
        return getInstance(frontCamera, Drawings.EMPTY);
    }


    public static CameraFragment getInstance(final boolean frontCamera, final Drawings drawings) {
        final CameraFragment cameraFragment = new CameraFragment();
        final Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_USE_FRONT_CAMERA, frontCamera);
        bundle.putSerializable(KEY_USE_DRAWINGS, drawings);
        cameraFragment.setArguments(bundle);
        return cameraFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCameraFacingPosition();
        camera = getCameraInstance();
        final int numberOfCameras = Camera.getNumberOfCameras();
        if (camera == null || numberOfCameras == 0) {
            thereIsNoCamera();
            return;
        }
        addViewToFrameLayout();
        initIfCameraAvailiable();
    }

    private void initCameraFacingPosition() {
        final boolean shouldUseFrontCamera = getArguments().getBoolean(KEY_USE_FRONT_CAMERA, true);
        if (shouldUseFrontCamera) {
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
    }

    private void addViewToFrameLayout() {
        final Drawings drawings = (Drawings) getArguments().getSerializable(KEY_USE_DRAWINGS);
        showCameraViewView = new ShowCameraView(getActivity(), camera, drawings);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(showCameraViewView);
    }


    private void thereIsNoCamera() {
//        final MessageDialogFragment messageDialogFragment = MessageDialogFragment.getInstance(getString(R.string.fragmentCameraErrorNoCamera));
//        messageDialogFragment.show(getFragmentManager(), MessageDialogFragment.TAG);
    }


    /**
     * Check if there is camera on the phone.
     */
    private void initIfCameraAvailiable() {

        try {
            setCameraDisplayOrientation(getActivity(), mCurrentCameraId, camera);
            final Camera.Parameters parameters = camera.getParameters();
            final List<String> supportedFocusModes = parameters.getSupportedFocusModes();
            final Optional<String> optionalFocusMode = Iterables.tryFind(supportedFocusModes, supportedFocusPredicate);
            if (optionalFocusMode.isPresent()) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            final Camera.Size pictureSize = getBiggestPictureSize(camera);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            final float pictureAspectRatio = getAspectRatio(pictureSize);
            final Camera.Size size = getBiggestPreviewSizeForAspectRatio(camera, pictureAspectRatio);
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
            final Point dispayDimentions = getDispayDimentions(getActivity());
            final float barHeight = convertDpToPixel(120);
            final int x = dispayDimentions.x;
            final int y = (int) (dispayDimentions.y - barHeight);
            final int calculatedX = getCalculatedX(x, y, pictureAspectRatio);
            final int calculatedY = getCalculatedY(x, y, pictureAspectRatio);
            final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(calculatedX, calculatedY);
            params.gravity = Gravity.CENTER;
            showCameraViewView.setLayoutParams(params);
        } catch (Exception e) {
            thereIsNoCamera();
            return;
        }
    }

    private int getCalculatedX(final int x, final int y, final float pictureAspectRatio) {
        final int calculatedAspectRatioX = (int) (x * pictureAspectRatio);
        if (calculatedAspectRatioX > y) {
            return (int) (y / pictureAspectRatio);
        }
        return x;
    }

    private int getCalculatedY(final int x, final int y, final float pictureAspectRatio) {
        final int calculatedAspectRatioX = (int) (x * pictureAspectRatio);
        if (calculatedAspectRatioX > y) {
            return y;
        }
        return calculatedAspectRatioX;
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(mCurrentCameraId); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Point getDispayDimentions(final Activity activity) {
        final Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return screenSize;
    }

    /**
     * Get the biggest preview size from the camera.
     *
     * @return the biggest preview size
     */
    private Camera.Size getBiggestPreviewSizeForAspectRatio(final Camera camera, final float aspectRatio) {
        final Camera.Parameters parameters = camera.getParameters();
        final List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        return new OrderByAspectRatio(aspectRatio).max(supportedPreviewSizes);
    }

    /**
     * Get the biggest picture size from the camera.
     *
     * @return the biggest picture size for better quality
     */
    private Camera.Size getBiggestPictureSize(final Camera camera) {
        final Camera.Parameters parameters = camera.getParameters();
        final List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        return ordering.max(supportedPictureSizes);
    }

    /**
     * Get the aspect ration for given size.
     */
    private float getAspectRatio(final Camera.Size size) {
        return (float) size.width / size.height;
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            CameraFragment.this.data = data;
            camera.stopPreview();
            if (data == null) {
                Toast.makeText(getActivity(), "not taken", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.takePicture(null, null, mPictureCallback);
            }
        }
    };


    public enum Drawings {
        EMPTY, FACE, ID_CARD
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPixel(final float dp) {
        final Resources resources = getActivity().getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value.
     */
    public float convertPixelsToDp(final float px) {
        final Resources resources = getActivity().getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    private final Predicate<String> supportedFocusPredicate = new Predicate<String>() {
        @Override
        public boolean apply(final String input) {
            return input.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
    };

    private final Ordering<Camera.Size> ordering = new com.google.common.collect.Ordering<Camera.Size>() {
        @Override
        public int compare(Camera.Size left, Camera.Size right) {
            if (left.width > right.width && left.height > right.height) {
                return 1;
            } else if (left.width < right.width && left.height > right.height) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    private class OrderByAspectRatio extends Ordering<Camera.Size> {

        private double aspectRatio;

        public OrderByAspectRatio(final double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public int compare(Camera.Size left, Camera.Size right) {
            if (left.width > right.width && left.height > right.height && aspectRatio == (float) left.width / left.height) {
                return 1;
            } else if (left.width < right.width && left.height < right.height && aspectRatio == (float) right.width / right.height) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class PredicateByAspectRatio implements Predicate<Camera.Size> {

        private double aspectRatio;

        public PredicateByAspectRatio(final double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        @Override
        public boolean apply(Camera.Size size) {
            return (float) size.width / size.height == aspectRatio;
        }
    }
}