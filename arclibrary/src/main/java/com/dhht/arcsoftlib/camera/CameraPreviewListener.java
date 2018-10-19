package com.dhht.arcsoftlib.camera;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.arcsoft.facetracking.AFT_FSDKFace;

import java.util.List;

public interface CameraPreviewListener {

    void onPreviewSize(int width, int height);

    void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces);

    Canvas onDrawFace(Canvas canvas, Rect rect, int width, int height, int degress, int cameraId);
}
