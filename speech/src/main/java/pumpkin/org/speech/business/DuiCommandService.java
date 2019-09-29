package pumpkin.org.speech.business;

import android.content.Context;

import com.aispeech.dui.dsk.duiwidget.CommandObserver;

import pumpkin.org.speech.business.command.BaseCommand;
import pumpkin.org.speech.business.command.FactoryCommand;
import pumpkin.org.speech.observer.DuiCommandObserver;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: DuiCommandService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 15:29
 * @Description: 处理指令Command对应的业务,所有的Command由该类张开
 */
public class DuiCommandService extends DuiService implements CommandObserver {

    private static final String TAG = DuiCommandService.class.getName();

    private DuiCommandObserver mDuiCommandObserver;
    private FactoryCommand mFactoryCommand;
    private Context mContext;
    private BaseCommand mBaseCommand;
    public DuiCommandService(Context context) {
        super(context);
        mContext = context;
        mDuiCommandObserver= new DuiCommandObserver(this);
        mFactoryCommand = new FactoryCommand();
    }

    @Override
    public void duiRegister() {
        mDuiCommandObserver.regist();
    }

    @Override
    public void unDuiRegister() {
        mDuiCommandObserver.unregist();
    }

    @Override
    public void onCall(String command, String data) {

        mBaseCommand = mFactoryCommand.createCommand(mContext,command);
        mBaseCommand.processor(command, data);

    }



}
