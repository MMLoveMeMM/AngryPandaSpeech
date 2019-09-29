package pumpkin.org.speech.business.message;

import android.content.Context;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: BaseMessage
 * @Author: 刘志保
 * @CreateDate: 2019/9/29 10:37
 * @Description: java类作用描述
 */
public class BaseMessage {
    protected Context context;
    public BaseMessage(Context context){
        this.context = context;
    }
    public void processor(String command, String data){}
}
