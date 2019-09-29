package pumpkin.org.speech.business;

import android.content.Context;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pumpkin.org.speech.business.message.BaseMessage;
import pumpkin.org.speech.business.message.FactoryMessage;
import pumpkin.org.speech.business.message.MessageType;
import pumpkin.org.speech.business.message.UpdateMessage;
import pumpkin.org.speech.message.MessageBody;
import pumpkin.org.speech.observer.DuiMessageObserver;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: DuiMessageService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 15:30
 * @Description: /**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 * 响应内置消息:类似于Android的广播接收器，您可以在需要的地方注册和注销MessageObserver，同一个MessageObserver可以处理多个message
 */
public class DuiMessageService extends DuiService implements MessageObserver {

    private static final String TAG = DuiMessageService.class.getName();

    private DuiMessageObserver mDuiMessageObserver;
    private FactoryMessage mFactoryMessage;

    // 更新请求
    private UpdateMessage mUpdateMessage;

    public DuiMessageService(Context context) {
        super(context);
        mDuiMessageObserver = new DuiMessageObserver(this);
        mFactoryMessage = new FactoryMessage();
        mUpdateMessage = new UpdateMessage(context);
    }

    @Override
    public void onMessage(String message, String data) {

        BaseMessage baseMessage = mFactoryMessage.createMessage(context, message);

        if (baseMessage != null && baseMessage instanceof UpdateMessage) {
            mUpdateMessage.processor(message, data);
        } else if (baseMessage != null) {
            baseMessage.processor(message, data);
        }

    }

    @Override
    public void duiRegister() {
        mDuiMessageObserver.regist();
    }

    @Override
    public void unDuiRegister() {
        mDuiMessageObserver.unregist();
    }
}
