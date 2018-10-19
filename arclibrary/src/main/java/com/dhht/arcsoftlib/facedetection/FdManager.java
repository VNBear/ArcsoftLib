package com.dhht.arcsoftlib.facedetection;

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

import android.util.Log;

import com.arcsoft.facedetection.AFD_FSDKEngine;
import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facedetection.AFD_FSDKFace;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateDate：2018/10/16
 * Creator： VNBear
 * Description:
 **/
public class FdManager {

    private final String TAG = "======" + this.getClass().getSimpleName();

    private AFD_FSDKEngine mFdEngine;

    private FdManager() {

    }

    private static final class InstanceHolder {
        private static final FdManager INSTANCE = new FdManager();
    }

    public static FdManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static FdManager getNewInstance(String appid, String key) {
        FdManager newInstance = new FdManager();
        newInstance.initEngine(appid, key);
        return newInstance;
    }

    public void initEngine(String appid, String key) {
        mFdEngine = new AFD_FSDKEngine();
        int result = mFdEngine.AFD_FSDK_InitialFaceEngine(appid, key, AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5).getCode();
        if (result == AFD_FSDKError.MOK) {
            Log.d(TAG, "人脸检测引擎初始化完成");
        } else {
            Log.d(TAG, "人脸检测引擎初始化失败,error:" + result);
        }
    }

    public void uninitEngine() {
        if (mFdEngine != null) {
            mFdEngine.AFD_FSDK_UninitialFaceEngine();
            mFdEngine = null;
        }
    }

    /**
     * 检测输入的图像中存在的人脸
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public List<AFD_FSDKFace> stillImageFaceDetection(byte[] image, int width, int height) {
        //检测输入的图像中是否存在的人脸,输出结果保存在faceList
        List<AFD_FSDKFace> faceList = new ArrayList<>();
        AFD_FSDKError mFdError = mFdEngine.AFD_FSDK_StillImageFaceDetection(image, width, height,
                AFD_FSDKEngine.CP_PAF_NV21, faceList);
        return faceList;
    }
}
