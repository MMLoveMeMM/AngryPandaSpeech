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
    private Context mContext;

    public AuthService(Context context, Handler handler) {
        mAuthHandler = handler;
        initDDS(context);
        mContext = context;
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
                if (checkDDSReady()) {
                    mAuthHandler.sendEmptyMessage(HandlerState.DDS_INIT_OK);
                } else {
                    // 重新开始鉴权
                    mAuthHandler.sendEmptyMessage(HandlerState.DDS_INIT_FAIL);
                }
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
            if (errId.equalsIgnoreCase("070603")) {
                /**
                 * key值配错了,直接停止鉴权
                 */
                Log.e(TAG, "reset keyapi serial !");
                mAuthHandler.sendEmptyMessage(HandlerState.DDS_KEYAPI_INVALID);
            } else {
                mAuthHandler.sendEmptyMessage(HandlerState.AUTH_FAIL);
            }
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

    /**
     * 检查dds是否初始成功
     *
     * @return
     */
    public boolean checkDDSReady() {

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
            try {
                if (DDS.getInstance().isAuthSuccess()) {
                    return true;
                } else {
                    // 自动授权
                    return false;
                }
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            Log.w(TAG, "waiting  init complete finish...");
            return false;
        }

    }

}
