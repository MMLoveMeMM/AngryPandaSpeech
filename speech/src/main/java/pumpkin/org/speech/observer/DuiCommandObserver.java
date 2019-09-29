package pumpkin.org.speech.observer;

import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;

/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 * 详细参考 : https://www.duiopen.com/docs/ct_skill_command
 */
public class DuiCommandObserver implements CommandObserver {

    private String TAG = "DuiCommandObserver";
    private static final String COMMAND_CALL = "sys.action.call";
    private static final String COMMAND_SELECT = "sys.action.call.select";
    private CommandObserver mCommandObserver;

    public DuiCommandObserver(CommandObserver commandObserver) {
        mCommandObserver = commandObserver;
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{COMMAND_CALL, COMMAND_SELECT},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onCall(String command, String data) {
        Log.e(TAG, "command: " + command + "  data: " + data);

        if(mCommandObserver!=null){
            mCommandObserver.onCall(command, data);
        }


    }

}
