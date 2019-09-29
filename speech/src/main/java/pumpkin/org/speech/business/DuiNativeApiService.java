package pumpkin.org.speech.business;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.ContentWidget;
import com.aispeech.dui.dsk.duiwidget.ListWidget;
import com.aispeech.dui.dsk.duiwidget.NativeApiObserver;

import org.json.JSONException;
import org.json.JSONObject;

import pumpkin.org.speech.observer.DuiNativeApiObserver;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: DuiNativeApiService
 * @Author: 刘志保
 * @CreateDate: 2019/9/27 15:30
 * @Description: 用户和Dui服务交互业务处理
 */
public class DuiNativeApiService extends DuiService implements NativeApiObserver {

    private static final String TAG = DuiNativeApiService.class.getName();
    private static final String NATIVE_API_CONTACT = "sys.query.contacts";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_FLAG = "flag";
    private static final String DB_LOCATION = "location.db";

    private DuiNativeApiObserver mDuiNativeApiObserver;
    public DuiNativeApiService(Context context) {
        super(context);
        mDuiNativeApiObserver = new DuiNativeApiObserver(this);
    }

    @Override
    public void onQuery(String nativeApi, String data) {
        Log.e(TAG, "nativeApi: " + nativeApi + "  data: " + data);
        if (NATIVE_API_CONTACT.equals(nativeApi)) {
            String searchName = null;
            ListWidget searchNums = null;
            try {
                JSONObject obj = null;
                obj = new JSONObject(data);
                searchName = obj.optString("联系人");
                searchNums = searchContacts(searchName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "query back:" + searchName + "-" + (searchNums != null ? searchNums.toString() : "null"));
            DDS.getInstance().getAgent().feedbackNativeApiResult(nativeApi, searchNums);
        }
    }

    @Override
    public void duiRegister() {
        mDuiNativeApiObserver.regist();
    }

    @Override
    public void unDuiRegister() {
        mDuiNativeApiObserver.unregist();
    }

    private ListWidget searchContacts(String searchName) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (null == cursor) {
            Log.e(TAG, "contacts null");
            return null;
        }
        ListWidget listWidget = new ListWidget();
        SQLiteDatabase db = context.openOrCreateDatabase(DB_LOCATION, Context.MODE_PRIVATE, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("+86", "");
            String type = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
            ), null).toString();
            String flag = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
            if (name.contains(searchName)) {
                Log.e(TAG, name + ":" + number);
                ContentWidget widget = new ContentWidget()
                        .setTitle(name)
                        .setSubTitle(type + ":" + number)
                        .addExtra(PARAM_NAME, name)
                        .addExtra(PARAM_PHONE, number)
                        .addExtra(PARAM_TYPE, type)
                        .addExtra(PARAM_FLAG, flag);
                listWidget.addContentWidget(widget);
            }
        }
        db.close();
        cursor.close();
        return listWidget;
    }
}
