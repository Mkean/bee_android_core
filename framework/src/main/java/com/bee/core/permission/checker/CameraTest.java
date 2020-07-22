package com.bee.core.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * 相机权限测试
 */
public class CameraTest implements PermissionTest {

    @Override
    public boolean test(Context context) {
        Camera camera = null;
        try {
            int cameraCount = Camera.getNumberOfCameras();
            if (cameraCount <= 0) return true;

            camera = Camera.open(cameraCount - 1);
            Camera.Parameters parameters = camera.getParameters();
            camera.setParameters(parameters);
            camera.setPreviewCallback(PREVIEW_CALLBACK);
            camera.startPreview();
            return true;
        } catch (Throwable e) {
            PackageManager packageManager = context.getPackageManager();
            return !packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        } finally {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
            }
        }
    }

    private final Camera.PreviewCallback PREVIEW_CALLBACK = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
        }
    };
}
