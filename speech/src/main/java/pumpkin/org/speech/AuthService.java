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

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: AuthService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 10:56
 * @Description: 思必驰鉴权管理服务类
 */
public class AuthService {

    private static final String AUTH_SUCCESS_INTENT = "ddsdemo.intent.action.auth_success";
    private static final String AUTH_FAIL_INTENT = "ddsdemo.intent.action.auth_failed";

    private Looper mLooper;
    private Context mContext;
    public AuthService(Context context, Looper looper){
        mLooper = looper;
        mContext = context;
        mAuthHandler = new AuthHandler(mLooper);
    }

    private void registerAuthBroadcast(){
        // 注册一个广播,接收service中发送的dds初始状态广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AUTH_SUCCESS_INTENT);// 认证成功的广播
        intentFilter.addAction(AUTH_FAIL_INTENT);// 认证失败的广播
        mContext.registerReceiver(authReceiver, intentFilter);
    }

    private void unregisterAuthBroadcast(){
        mContext.unregisterReceiver(authReceiver);
    }

    public void startAuth(){
        mAuthHandler.sendEmptyMessage(AUTH_DO);
    }

    // 认证广播
    private BroadcastReceiver authReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), AUTH_SUCCESS_INTENT)) {
                // 鉴权成功
            } else if (TextUtils.equals(intent.getAction(), AUTH_FAIL_INTENT)) {
                mAuthHandler.sendEmptyMessage(AUTH_FAIL);
            }
        }
    };

    // 执行自动授权
    private void doAutoAuth(){
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
        try {
            DDS.getInstance().doAuth();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private static final int AUTH_DO = 1000;
    private static final int AUTH_FAIL = 1001;
    private AuthHandler mAuthHandler;
    private class AuthHandler extends Handler{
        public AuthHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;

            switch (action){
                case AUTH_DO:
                    doAutoAuth();
                    break;
                case AUTH_FAIL:
                    // 如果失败,5s以后重新鉴权
                    mAuthHandler.sendEmptyMessageAtTime(AUTH_DO,5000);
                    break;
                    default:
                        break;
            }

        }
    }


}
