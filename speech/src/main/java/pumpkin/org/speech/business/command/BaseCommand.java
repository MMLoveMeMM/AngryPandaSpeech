package pumpkin.org.speech.business.command;

import android.content.Context;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: BaseCommand
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 17:22
 * @Description: java类作用描述
 */
public class BaseCommand {
    protected Context context;
    public BaseCommand(Context context){
        this.context = context;
    }

    public void processor(String command, String data){

    }

}
