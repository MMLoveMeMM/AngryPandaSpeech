package pumpkin.org.speech.business;

import android.content.Context;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: DuiService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 17:00
 * @Description: java类作用描述
 */
public abstract class DuiService {
    protected Context context;
    public DuiService(Context context){
        this.context = context;
    }

    public abstract void duiRegister();
    public abstract void unDuiRegister();

    public void release(){
        unDuiRegister();
    }
}
