package pumpkin.org.speech;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class SpeechService extends Service {

    private HandlerThread speechThread = new HandlerThread(SpeechService.class.getName());

    private AuthService mAuthService;

    public SpeechService() {
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
    }

    public void init() {
        mAuthService = new AuthService(getApplicationContext(), mSpeechHandler);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
            switch (action) {
                case HandlerState.AUTH_DO: {

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
                    // 可以唤醒DDS

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

}
