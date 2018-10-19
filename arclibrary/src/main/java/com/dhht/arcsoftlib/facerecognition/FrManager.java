package com.dhht.arcsoftlib.facerecognition;

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

import android.graphics.Rect;
import android.util.Log;

import com.arcsoft.facedetection.AFD_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKEngine;
import com.arcsoft.facerecognition.AFR_FSDKError;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKMatching;

/**
 * CreateDate：2018/10/16
 * Creator： VNBear
 * Description:
 **/
public class FrManager {
    private final String TAG = "======" + this.getClass().getSimpleName();

    private AFR_FSDKEngine mFrEngine;

    private FrManager() {

    }

    private static final class InstanceHolder {
        private static final FrManager INSTANCE = new FrManager();
    }

    public static FrManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static FrManager getNewInstance(String appid, String key) {
        FrManager newInstance = new FrManager();
        newInstance.initEngine(appid, key);
        return newInstance;
    }

    public void initEngine(String appid, String key) {
        mFrEngine = new AFR_FSDKEngine();
        int result = mFrEngine.AFR_FSDK_InitialEngine(appid, key).getCode();
        if (result == AFD_FSDKError.MOK) {
            Log.d(TAG, "人脸识别引擎初始化完成");
        } else {
            Log.d(TAG, "人脸识别引擎初始化失败,error:" + result);
        }
    }

    public void uninitEngine() {
        if (mFrEngine != null) {
            mFrEngine.AFR_FSDK_UninitialEngine();
            mFrEngine = null;
        }
    }

    /**
     * 检测输入图像中指定位置的人脸特征
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public AFR_FSDKFace extractFRFeature(byte[] data, int width, int height, Rect rect, int degree) {

        AFR_FSDKFace userFace = new AFR_FSDKFace();
        //检测输入图像中的人脸特征信息，输出结果保存在userFace
        AFR_FSDKError mFrError = mFrEngine.AFR_FSDK_ExtractFRFeature(data, width, height, AFR_FSDKEngine.CP_PAF_NV21,
                rect,
                degree,
                userFace);
        if (mFrError.getCode() == AFR_FSDKError.MOK) {
            //保存一下获取到的人脸信息
            return userFace.clone();
        } else {
            return null;
        }
    }

    /**
     * 对比两张人脸的相似度
     *
     * @param face1
     * @param face2
     * @return 相似度0-1
     */
    public float match(AFR_FSDKFace face1, AFR_FSDKFace face2) {
        AFR_FSDKMatching score = new AFR_FSDKMatching();
        AFR_FSDKError error = mFrEngine.AFR_FSDK_FacePairMatching(face1, face2, score);
        if (error.getCode() == AFR_FSDKError.MOK) {
            return score.getScore();
        } else {
            return 0;
        }
    }
}
