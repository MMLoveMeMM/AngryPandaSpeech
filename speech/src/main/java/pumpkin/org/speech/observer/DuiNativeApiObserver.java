package pumpkin.org.speech.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.NativeApiObserver;

/*
 * 注册NativeApiObserver, 用于客户端响应DUI平台技能配置里的资源调用指令, 同一个NativeApiObserver可以处理多个native api.
 * 目前demo中实现了打电话的功能逻辑
 * 详细参考 : https://www.duiopen.com/docs/API
 */
public class DuiNativeApiObserver implements NativeApiObserver {
    private String TAG = "DuiNativeApiObserver";
    private static final String NATIVE_API_CONTACT = "sys.query.contacts";

    private NativeApiObserver mNativeApiObserver;

    public DuiNativeApiObserver(NativeApiObserver nativeApiObserver) {
        mNativeApiObserver = nativeApiObserver;
    }

    // 注册当前更新消息
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{NATIVE_API_CONTACT},
                this);
    }

    // 注销当前更新消息
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /*
     * onQuery方法执行时，需要调用feedbackNativeApiResult来向DUI平台返回执行结果，表示一个native api执行结束。
     * native api的执行超时时间为10s
     */
    @Override
    public void onQuery(String nativeApi, String data) {
        if (mNativeApiObserver != null) {
            mNativeApiObserver.onQuery(nativeApi, data);
        }
    }


}
