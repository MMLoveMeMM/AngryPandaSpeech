package pumpkin.org.speech;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;

import pumpkin.org.speech.business.DuiCommandService;
import pumpkin.org.speech.business.DuiMessageService;
import pumpkin.org.speech.business.DuiNativeApiService;

public class SpeechService extends Service {

    private static final String TAG = SpeechService.class.getName();
    private HandlerThread speechThread = new HandlerThread(SpeechService.class.getName());

    private AuthService mAuthService;
    private DuiCommandService mDuiCommandService;
    private DuiMessageService mDuiMessageService;
    private DuiNativeApiService mDuiNativeApiService;

    public SpeechService() {
        Log.e(TAG, "SpeechService start .............");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        speechThread.start();
        mSpeechHandler = new SpeechHandler(speechThread.getLooper());
        init();
        Log.e(TAG, "SpeechService onCreate .............");
    }

    public void init() {
        mDuiCommandService = new DuiCommandService(getApplicationContext());
        mDuiMessageService = new DuiMessageService(getApplicationContext());
        mDuiNativeApiService = new DuiNativeApiService(getApplicationContext());

        mAuthService = new AuthService(getApplicationContext(), mSpeechHandler);
        mAuthService.startAuth();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "SpeechService onStartCommand .............");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDuiService();
    }

    private SpeechHandler mSpeechHandler;

    private class SpeechHandler extends Handler {
        public SpeechHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int action = msg.what;
            Log.e(TAG, "SpeechService handleMessage action : " + action);
            switch (action) {
                case HandlerState.DDS_KEYAPI_INVALID: {
                    // todo nothing , stop dds service
                }
                break;
                case HandlerState.AUTH_DO: {
                    mAuthService.startAuth();
                }
                break;
                case HandlerState.AUTH_FAIL: {
                    mSpeechHandler.removeMessages(HandlerState.AUTH_DO);
                    mSpeechHandler.sendEmptyMessageAtTime(HandlerState.AUTH_DO, 5000);
                }
                break;
                case HandlerState.AUTH_OK: {

                }
                break;
                case HandlerState.DDS_INIT_OK: {
                    startDuiService();
                    // 可以唤醒DDS
                    enableWakeup();
                    sendHiMessage();
                }
                break;
                case HandlerState.DDS_INIT_FAIL: {
                    mSpeechHandler.removeMessages(HandlerState.AUTH_DO);
                    mSpeechHandler.sendEmptyMessageAtTime(HandlerState.AUTH_DO, 5000);
                }
                break;
                default:
                    break;
            }

        }
    }

    // 打开唤醒，调用后才能语音唤醒
    void enableWakeup() {
        try {
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // 关闭唤醒, 调用后将无法语音唤醒
    void disableWakeup() {
        try {
            DDS.getInstance().getAgent().stopDialog();
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    // dds初始化成功之后,展示一个打招呼消息,告诉用户可以开始使用
    public void sendHiMessage() {
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            // 获取主唤醒词
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            // 获取副唤醒词
            minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        String hiStr = "";
        if (wakeupWords != null && minorWakeupWord != null) {
            hiStr = getString(R.string.sayhi2, wakeupWords[0], minorWakeupWord);
        } else if (wakeupWords != null && wakeupWords.length == 2) {
            hiStr = getString(R.string.sayhi2, wakeupWords[0], wakeupWords[1]);
        } else if (wakeupWords != null && wakeupWords.length > 0) {
            hiStr = getString(R.string.sayhi1, wakeupWords[0]);
        }
        try {
            DDS.getInstance().getAgent().getTTSEngine().speak(hiStr, 1, "100", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    private void startDuiService() {
        mDuiCommandService.duiRegister();
        mDuiMessageService.duiRegister();
        mDuiNativeApiService.duiRegister();
    }

    private void stopDuiService() {
        disableWakeup();
        mDuiCommandService.unDuiRegister();
        mDuiMessageService.unDuiRegister();
        mDuiNativeApiService.unDuiRegister();
    }

}
