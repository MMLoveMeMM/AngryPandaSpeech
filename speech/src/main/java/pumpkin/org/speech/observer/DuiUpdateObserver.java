package pumpkin.org.speech.observer;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.dds.update.DDSUpdateListener;

import pumpkin.org.speech.business.message.MessageType;

/**
 * 更新Observer,用于更新当前dds组件
 */
public class DuiUpdateObserver implements MessageObserver {
    private static final String Tag = "DuiUpdateObserver";

    private MessageObserver mMessageObserver;

    public DuiUpdateObserver(MessageObserver messageObserver) {
        mMessageObserver = messageObserver;
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(MessageType.SYS_RESOURCE_UPDATED, this);
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
