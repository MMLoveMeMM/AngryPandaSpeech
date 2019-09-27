package pumpkin.org.speech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSAuthListener;
import com.aispeech.dui.dds.DDSInitListener;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: AuthService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 10:56
 * @Description: 思必驰鉴权管理服务类
 */
public class AuthService {

    private static final String TAG = AuthService.class.getName();
    private Handler mAuthHandler;

    public AuthService(Context context,Handler handler) {
        mAuthHandler = handler;
        initDDS(context);
    }

    public void initDDS(Context context) {
        DDS.getInstance().setDebugMode(2); //在调试时可以打开sdk调试日志，在发布版本时，请关闭
        DDS.getInstance().init(context, SpeechConfig.createConfig(context), mInitListener, mAuthListener);
    }

    // dds初始状态监听器,监听init是否成功
    private DDSInitListener mInitListener = new DDSInitListener() {
        @Override
        public void onInitComplete(boolean isFull) {
            Log.d(TAG, "onInitComplete");
            if (isFull) {
                //  初始化成功后,可以开始唤醒思必驰服务
                mAuthHandler.sendEmptyMessage(HandlerState.DDS_INIT_OK);
            }
        }

        @Override
        public void onError(int what, final String msg) {
            Log.e(TAG, "Init onError: " + what + ", error: " + msg);
            mAuthHandler.sendEmptyMessage(HandlerState.DDS_INIT_FAIL);
        }
    };

    // dds认证状态监听器,监听auth是否成功
    private DDSAuthListener mAuthListener = new DDSAuthListener() {
        @Override
        public void onAuthSuccess() {
            // 发送一个认证成功的广播
            mAuthHandler.sendEmptyMessage(HandlerState.AUTH_OK);
        }

        @Override
        public void onAuthFailed(final String errId, final String error) {
            Log.e(TAG, "onAuthFailed: " + errId + ", error:" + error);
            // 发送一个认证失败的广播
            mAuthHandler.sendEmptyMessage(HandlerState.AUTH_FAIL);
        }
    };

    public void startAuth() {
        doAutoAuth();
    }

    // 执行自动授权
    private void doAutoAuth() {
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
        try {
            DDS.getInstance().doAuth();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

}
