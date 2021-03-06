package com.dhht.arcdemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.dhht.arcsoftlib.ArcsoftConfig;
import com.dhht.arcsoftlib.ArcsoftSDK;
import com.dhht.arcsoftlib.camera.ArcCamera;
import com.dhht.arcsoftlib.camera.CameraPreviewListener;

import java.util.ArrayList;
import java.util.List;

public class FaceActivity extends AppCompatActivity {


    private static final int CAMERAID = 1;
    private static final int DEGRESS = 90;

    public static final String KEY = "KEY";
    private static List<Face> faceList = new ArrayList<>();
    private int key;

    SurfaceView surfce_preview, surfce_rect;
    private int mWidth, mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        surfce_preview = findViewById(R.id.surfce_preview);
        surfce_rect = findViewById(R.id.surfce_rect);

        ArcsoftSDK.getInstance().init(getApplicationContext(), new ArcsoftConfig.Builder().builder());
        key = getIntent().getIntExtra(KEY, 0);


        ArcCamera.getInstance().init(CAMERAID, DEGRESS, Color.GREEN);
        ArcCamera.getInstance().openCamera(this, surfce_preview, surfce_rect);
        ArcCamera.getInstance().setCameraPreviewListener(new CameraPreviewListener() {

            @Override
            public void onPreviewSize(int width, int height) {
                mWidth = width;
                mHeight = height;
            }

            @Override
            public void onPreviewData(byte[] data, List<AFT_FSDKFace> fsdkFaces) {
                //人脸注册
                if (key == 0) {
                    if (fsdkFaces != null && fsdkFaces.size() > 0 && faceList.size() == 0) {
                        for (int i = 0; i < fsdkFaces.size(); i++) {
                            AFT_FSDKFace fsdkFace = fsdkFaces.get(i);
                            AFR_FSDKFace frFeature = ArcsoftSDK.getInstance().extractFRFeature(data, mWidth, mHeight, fsdkFace.getRect(), fsdkFace.getDegree());
                            if (frFeature != null) {
                                Face face = new Face();
                                face.setName("张三" + (i + 1) + "号");
                                face.setFaceData(frFeature.getFeatureData());
                                faceList.add(face);
                            }
                        }
                        finish();
                        Toast.makeText(FaceActivity.this, "人脸注册成功：" + faceList.size() + "人", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                //人证比对
                if (key == 1) {

                    return;
                }

                //人脸查询
                if (key == 2) {
                    if (fsdkFaces != null && fsdkFaces.size() > 0 && faceList.size() != 0) {
                        AFT_FSDKFace fsdkFace = fsdkFaces.get(0);
                        AFR_FSDKFace face1 = ArcsoftSDK.getInstance().extractFRFeature(data, mWidth, mHeight, fsdkFace.getRect(), fsdkFace.getDegree());
                        AFR_FSDKFace face2 = new AFR_FSDKFace();
                        face2.setFeatureData(faceList.get(0).getFaceData());
                        float score = ArcsoftSDK.getInstance().match(face1, face2);
                        showToast("相似度:" + score * 100 + "%");
                        Log.d("======", "thread name:" + Thread.currentThread().getName());
                        key = -1;
                        surfce_preview.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                key = 2;
                            }
                        }, 1000 * 1);
                    }
                    return;
                }

            }

            @Override
            public Canvas onDrawFace(Canvas canvas, Rect rect, int width, int height, int degress, int cameraId) {
                return canvas;
            }

        });

    }

    @Override
    protected void onDestroy() {
        ArcsoftSDK.getInstance().uninitEngine();
        super.onDestroy();
    }

    private void showToast(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FaceActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
