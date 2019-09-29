package pumpkin.org.angrypandaspeech;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import pumpkin.org.speech.SpeechService;
/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: DDSApplication
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 14:44
 * @Description: java类作用描述
 */
public class DDSApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        startService(new Intent(this, SpeechService.class));

    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        DDSApplication.mContext = mContext;
    }
}
