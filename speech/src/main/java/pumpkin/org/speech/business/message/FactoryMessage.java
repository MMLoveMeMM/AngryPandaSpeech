package pumpkin.org.speech.business.message;

import android.content.Context;
import android.text.TextUtils;

import com.aispeech.dui.dds.agent.MessageObserver;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: FactoryMessage
 * @Author: 刘志保
 * @CreateDate: 2019/9/29 10:06
 * @Description: java类作用描述
 */
public class FactoryMessage {

    public BaseMessage createMessage(Context context, String message){

        if (TextUtils.isEmpty(message)){
            return null;
        }

        if(message.equalsIgnoreCase(MessageType.SYS_RESOURCE_UPDATED)){
            return new UpdateMessage(context);
        }else {
            return new CommonMessage(context);
        }

    }

}
