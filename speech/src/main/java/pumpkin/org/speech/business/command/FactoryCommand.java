package pumpkin.org.speech.business.command;

import android.content.Context;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: FactoryCommand
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 17:24
 * @Description: 根据不同Command返回不同对象处理
 */
public class FactoryCommand {

    public BaseCommand createCommand(Context context,String command){

        if(CommandType.COMMAND_CALL.equals(command) || CommandType.COMMAND_SELECT.equals(command)){
            return new PhoneCommand(context);
        }

        return null;
    }

}
