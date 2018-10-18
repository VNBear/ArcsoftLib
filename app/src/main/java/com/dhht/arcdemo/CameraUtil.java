package com.dhht.arcdemo;

// ┏┓　　　┏┓
// ┏┛┻━━━┛┻┓
// ┃　　　　　　　┃ 　
// ┃　　　━　　　┃
// ┃　┳┛　┗┳　┃
// ┃　　　　　　　┃
// ┃　　　┻　　　┃
// ┃　　　　　　　┃
// ┗━┓　　　┏━┛
// ┃　　　┃ 神兽保佑　　　　　　　　
// ┃　　　┃ 代码无BUG！
// ┃　　　┗━━━┓
// ┃　　　　　　　┣┓
// ┃　　　　　　　┏┛
// ┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.arcsoft.facetracking.AFT_FSDKFace;
import com.dhht.arcsoftlib.ArcsoftSDK;
import com.dhht.arcsoftlib.util.DrawUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * CreateDate：2018/10/18
 * Creator： VNBear
 * Description:
 **/
public class CameraUtil implements SurfaceHolder.Callback {

    private SurfaceView mPreSurface, mRectSurface;
    private Camera mCamera;

    private CameraPreviewListener mCameraPreviewListener;
    private WeakReference<Activity> mActivity;


    //相机的位置
    private int mCameraId;
    //相机的方向
    private int mDegress;
    //相机预览的宽高
    private int mPreviewSizeX, mPreviewSizeY;
    //人脸框的颜色
    private int mColor;

    private CameraUtil() {
    }

    private static final class InstanceHolder {
        private static CameraUtil INSTANCE = new CameraUtil();
    }

    public static CameraUtil getInstance() {
        return InstanceHolder.INSTANCE;
    }


    /**
     * 初始化相机参数
     *
     * @param cameraId 相机id
     * @param degress  画面旋转角度
     * @param color    人脸框颜色
     */
    public void init(int cameraId, int degress, int color) {
        init(cameraId, degress, 0, 0, color);
    }

    public void init(int cameraId, int degress, int width, int height, int color) {
        this.mCameraId = cameraId;
        this.mDegress = degress;
        this.mPreviewSizeX = width;
        this.mPreviewSizeY = height;
        this.mColor = color;
    }

    /**
     * 开始预览
     *
     * @param activity
     * @param surfacePreview
     * @param surfaceViewRect
     */
    public void openCamera(Activity activity, SurfaceView surfacePreview, SurfaceView surfaceViewRect) {
        this.mActivity = new WeakReference<>(activity);
        this.mPreSurface = surfacePreview;
        this.mRectSurface = surfaceViewRect;
        mPreSurface.getHolder().addCallback(this);
        mRectSurface.setZOrderMediaOverlay(true);
        mRectSurface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //打开指定相机
        mCamera = Camera.open(mCameraId);
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //设置相机预览大小
            Camera.Parameters parameters = mCamera.getParameters();
            if (mPreviewSizeX == 0 || mPreviewSizeY == 0) {
                Camera.Size previewSize = getBestSupportedSize(parameters.getSupportedPreviewSizes(), metrics);
                mPreviewSizeX = previewSize.width;
                mPreviewSizeY = previewSize.height;
            }
            if (mCameraPreviewListener != null) {
                mCameraPreviewListener.onPreviewSize(mPreviewSizeX, mPreviewSizeY);
            }
            parameters.setPreviewSize(mPreviewSizeX, mPreviewSizeY);
            mCamera.setParameters(parameters);
            //设置画面旋转角度
            mCamera.setDisplayOrientation(mDegress);
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //视频数据
                    List<AFT_FSDKFace> ftList = ArcsoftSDK.getInstance().videoFaceFeatureDetect(data, mPreviewSizeX, mPreviewSizeY);

                    //回调接口给外部处理
                    if (mCameraPreviewListener != null && ftList != null) {
                        mCameraPreviewListener.onPreviewData(data.clone(), ftList);
                    }

                    //在另一个SurfaceView上绘制人脸框
                    drawFace(ftList);
                }
            });
            mCamera.startPreview();
        } catch (Exception e) {
            mCamera = null;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mActivity = null;
        mCameraPreviewListener = null;
    }

    public void setCameraPreviewListener(CameraPreviewListener mCameraPreviewListener) {
        this.mCameraPreviewListener = mCameraPreviewListener;
    }

    private Activity getActivity() {
        return mActivity.get();
    }

    private void drawFace(List<AFT_FSDKFace> ftList) {
        if (mRectSurface != null && ftList != null && ftList.size() > 0) {
            Canvas canvas = mRectSurface.getHolder().lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(5);
            paint.setTextSize(80);

            for (AFT_FSDKFace fsdkFace : ftList) {
                Rect rect = fsdkFace.getRect();
                if (rect != null) {
                    //根据相机ID和画面预览大小调整人脸框位置
                    Rect adjustedRect = DrawUtils.adjustRect(rect, mPreviewSizeX, mPreviewSizeY,
                            canvas.getWidth(), canvas.getHeight(), mDegress, mCameraId);

                    //画人脸框
                    DrawUtils.drawFaceRect(canvas, adjustedRect, mColor, 5);

                    //画人员姓名
                    if (rect.right < mPreviewSizeX - 100) {
                        canvas.drawText("张三", rect.right + 30, rect.bottom, paint);
                    } else {
                        canvas.drawText("张三", rect.left - 30, rect.bottom, paint);
                    }

                    //回调接口给外部人脸的位置信息
                    if (mCameraPreviewListener != null) {
                        Canvas temp = mCameraPreviewListener.onDrawFace(canvas, adjustedRect, mPreviewSizeX, mPreviewSizeY, mDegress, mCameraId);
                        if (temp != null) {
                            canvas = temp;
                        }
                    }
                }
            }
            mRectSurface.getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 获取最佳的预览大小
     *
     * @param sizes
     * @param metrics
     * @return
     */
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, DisplayMetrics metrics) {
        Camera.Size bestSize = sizes.get(0);
        float screenRatio = (float) metrics.widthPixels / (float) metrics.heightPixels;
        if (screenRatio > 1) {
            screenRatio = 1 / screenRatio;
        }

        for (Camera.Size s : sizes) {
            if (Math.abs((s.height / (float) s.width) - screenRatio) < Math.abs(bestSize.height /
                    (float) bestSize.width - screenRatio)) {
                bestSize = s;
            }
        }
        return bestSize;
    }
}
