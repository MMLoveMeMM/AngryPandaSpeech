package pumpkin.org.speech;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

public class SpeechService extends Service {

    private HandlerThread speechThread = new HandlerThread(SpeechService.class.getName());
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


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private static final int AUTH_STATE = 0;
    private static final int AUTH_OK = 1;

    private class SpeechHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int action = msg.what;
            switch (action){
                case AUTH_OK:{

                }
                    break;
                    default:
                        break;
            }

        }
    }

}
