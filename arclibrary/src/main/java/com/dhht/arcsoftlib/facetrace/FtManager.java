package com.dhht.arcsoftlib.facetrace;

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

import com.arcsoft.facetracking.AFT_FSDKEngine;
import com.arcsoft.facetracking.AFT_FSDKError;
import com.arcsoft.facetracking.AFT_FSDKFace;
import com.dhht.arcsoftlib.facedetection.FdManager;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateDate：2018/10/16
 * Creator： VNBear
 * Description:
 **/
public class FtManager {
    private final String TAG = "======" + this.getClass().getSimpleName();

    private AFT_FSDKEngine mFtEngine;
    private List<AFT_FSDKFace> mFtFsdkFaceList;

    private FtManager() {

    }

    private static final class InstanceHolder {
        private static final FtManager INSTANCE = new FtManager();
    }

    public static FtManager getInstance() {
        return InstanceHolder.INSTANCE;
    }


    public static FtManager getNewInstance(String appid, String key) {
        FtManager newInstance = new FtManager();
        newInstance.initEngine(appid, key);
        return newInstance;
    }

    public void initEngine(String appid, String key) {
        mFtEngine = new AFT_FSDKEngine();
        mFtFsdkFaceList = new ArrayList<>();
        int result = mFtEngine.AFT_FSDK_InitialFaceEngine(appid, key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5).getCode();
        if (result == AFT_FSDKError.MOK) {
            Log.d(TAG, "人脸追踪引擎初始化完成");
        } else {
            Log.d(TAG, "人脸追踪引擎初始化失败,error:" + result);
        }
    }

    public void uninitEngine() {
        if (mFtEngine != null) {
            mFtEngine.AFT_FSDK_UninitialFaceEngine();
            mFtEngine = null;
        }
        mFtFsdkFaceList = null;
    }

    /**
     * 检测数据中人脸位置和角度
     * @param data
     * @param width
     * @param height
     * @return
     */
    public List<AFT_FSDKFace> faceFeatureDetect(byte[] data, int width, int height) {
        if (mFtEngine == null) {
            throw new RuntimeException("please init  AFT_FSDKEngine at first");
        }

        AFT_FSDKError mFtError = mFtEngine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, mFtFsdkFaceList);
        if (mFtFsdkFaceList.isEmpty()) {
            mFtFsdkFaceList.clear();
            return null;
        } else {
            List<AFT_FSDKFace> result = new ArrayList<>();
            result.addAll(mFtFsdkFaceList);
            mFtFsdkFaceList.clear();
            return result;
        }
    }
}
