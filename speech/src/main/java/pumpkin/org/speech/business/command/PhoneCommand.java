package pumpkin.org.speech.business.command;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: PhoneCommand
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 17:22
 * @Description: 电话command具体业务处理
 */
public class PhoneCommand extends BaseCommand {

    private static final String TAG = PhoneCommand.class.getName();

    private String mSelectedPhone = null;
    public PhoneCommand(Context context) {
        super(context);
    }

    @Override
    public void processor(String command, String data) {
        super.processor(command, data);

        try {
            if (CommandType.COMMAND_CALL.equals(command)) {
                String number = new JSONObject(data).optString("phone");
                if (number == null) {
                    dialPhone(mSelectedPhone);
                    mSelectedPhone = null;
                } else {
                    dialPhone(number);
                }
            } else if (CommandType.COMMAND_SELECT.equals(command)) {
                mSelectedPhone = new JSONObject(data).optString("phone");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 拨打电话
    private void dialPhone(String number) {
        if (number == null) {
            return;
        }
        Log.e(TAG, "phoneDial:" + number);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("tel:" + number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

}
