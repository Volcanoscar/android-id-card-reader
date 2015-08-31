package com.eftimoff.idcardreader.ui.camera;

import android.annotation.TargetApi;
import android.hardware.Camera;

public class CameraEnumeration {

    private int cameraId;
    private boolean frontCamera;
    private Camera.CameraInfo cameraInfo;

    @TargetApi(11)
    public CameraEnumeration(final int cameraId, final Camera.CameraInfo cameraInfo) {
        this.cameraId = cameraId;
        this.cameraInfo = cameraInfo;
        frontCamera = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    public int getCameraId() {
        return cameraId;
    }

    public Camera.CameraInfo getCameraInfo() {
        return cameraInfo;
    }

    public boolean isFrontCamera() {
        return frontCamera;
    }
}