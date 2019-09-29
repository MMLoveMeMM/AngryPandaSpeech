package pumpkin.org.speech.business.message;

import android.content.Context;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.dds.update.DDSUpdateListener;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: UpdateMessage
 * @Author: 刘志保
 * @CreateDate: 2019/9/29 10:05
 * @Description: java类作用描述
 */
public class UpdateMessage extends BaseMessage {

    private static final String TAG = UpdateMessage.class.getName();

    public UpdateMessage(Context context) {
        super(context);
    }

    @Override
    public void processor(String command, String data) {
        super.processor(command, data);
        initUpdate(ddsUpdateListener);

    }

    // 初始化更新
    private void initUpdate(DDSUpdateListener listener) {
        try {
            DDS.getInstance().getUpdater().update(listener);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private DDSUpdateListener ddsUpdateListener = new DDSUpdateListener() {
        @Override
        public void onUpdateFound(String detail) {
            try {
                DDS.getInstance().getAgent().getTTSEngine().speak("发现新版本,正在为您更新", 1);
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpdateFinish() {
            // 更新成功后不要立即调用speak提示用户更新成功, 这个时间DDS正在初始化
        }

        @Override
        public void onDownloadProgress(float progress) {

        }

        @Override
        public void onError(int what, String error) {

            Log.e(TAG, "what = " + what + ", error = " + error);
        }

        @Override
        public void onUpgrade(String version) {
        }
    };

}
