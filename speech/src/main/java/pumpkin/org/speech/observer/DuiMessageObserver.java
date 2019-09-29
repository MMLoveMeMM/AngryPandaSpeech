package pumpkin.org.speech.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;

import pumpkin.org.speech.business.message.MessageType;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 * 响应内置消息:类似于Android的广播接收器，您可以在需要的地方注册和注销MessageObserver，同一个MessageObserver可以处理多个message
 */
public class DuiMessageObserver implements MessageObserver {
    private final String Tag = "DuiMessageObserver";

    private MessageObserver mMessageObserver;

    public DuiMessageObserver(MessageObserver messageObserver) {
        mMessageObserver = messageObserver;
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(MessageType.SUBSCRIBE_KEYS, this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onMessage(String message, String data) {
        if (mMessageObserver != null) {
            mMessageObserver.onMessage(message, data);
        }
    }

}
