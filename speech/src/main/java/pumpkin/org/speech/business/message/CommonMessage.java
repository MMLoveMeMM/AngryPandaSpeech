package pumpkin.org.speech.business.message;

import android.content.Context;
import android.util.Log;

import com.aispeech.dui.dds.agent.MessageObserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pumpkin.org.speech.message.MessageBody;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: CommonMessage
 * @Author: 刘志保
 * @CreateDate: 2019/9/29 10:11
 * @Description: java类作用描述
 */
public class CommonMessage extends BaseMessage{

    private static final String TAG = CommonMessage.class.getName();

    public CommonMessage(Context context) {
        super(context);
    }

    @Override
    public void processor(String message, String data) {
        super.processor(message, data);
        Log.d(TAG, "message : " + message + " data : " + data);
        MessageBody bean = null;
        switch (message) {
            case MessageType.CONTEXT_OUTPUT_TEXT:
                bean = new MessageBody();
                String txt = "";
                try {
                    JSONObject jo = new JSONObject(data);
                    txt = jo.optString("text", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bean.setText(txt);
                bean.setType(MessageBody.TYPE_OUTPUT);
                break;
            case MessageType.CONTEXT_INPUT_TEXT:
                bean = new MessageBody();
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.has("var")) {
                        String var = jo.optString("var", "");
                        bean.setText(var);
                        bean.setType(MessageBody.TYPE_INPUT);
                    }
                    if (jo.has("text")) {
                        String text = jo.optString("text", "");
                        bean.setText(text);
                        bean.setType(MessageBody.TYPE_INPUT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MessageType.CONTEXT_WIDGET_CONTENT:
                bean = new MessageBody();
                try {
                    JSONObject jo = new JSONObject(data);
                    String title = jo.optString("title", "");
                    String subTitle = jo.optString("subTitle", "");
                    String imgUrl = jo.optString("imageUrl", "");
                    bean.setTitle(title);
                    bean.setSubTitle(subTitle);
                    bean.setImgUrl(imgUrl);
                    bean.setType(MessageBody.TYPE_WIDGET_CONTENT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MessageType.CONTEXT_WIDGET_LIST:
                bean = new MessageBody();
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray array = jo.optJSONArray("content");
                    if (array == null || array.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.optJSONObject(i);
                        String title = object.optString("title", "");
                        String subTitle = object.optString("subTitle", "");
                        MessageBody b = new MessageBody();
                        b.setTitle(title);
                        b.setSubTitle(subTitle);
                        bean.addMessageBean(b);
                    }
                    int currentPage = jo.optInt("currentPage");
                    bean.setCurrentPage(currentPage);
                    bean.setType(MessageBody.TYPE_WIDGET_LIST);

                    int itemsPerPage = jo.optInt("itemsPerPage");
                    bean.setItemsPerPage(itemsPerPage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MessageType.CONTEXT_WIDGET_WEB:
                bean = new MessageBody();
                try {
                    JSONObject jo = new JSONObject(data);
                    String url = jo.optString("url");
                    bean.setUrl(url);
                    bean.setType(MessageBody.TYPE_WIDGET_WEB);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case MessageType.CONTEXT_WIDGET_CUSTOM:
                /**
                 * 自定义天气的
                 */
                /*bean = new MessageBody();
                try {
                    JSONObject jo = new JSONObject(data);
                    String name = jo.optString("name");
                    if (name.equals("weather")) {
                        bean.setWeatherBean(mGson.fromJson(data, WeatherBean.class));
                        bean.setType(MessageBody.TYPE_WIDGET_WEATHER);
                        mMessageList.add(bean);
                        if (mMessageCallback != null) {
                            mMessageCallback.onMessage();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                break;
            case MessageType.CONTEXT_WIDGET_MEDIA:
                JSONObject jsonObject;
                int count = 0;
                String name = "";
                try {
                    jsonObject = new JSONObject(data);
                    count = jsonObject.optInt("count");
                    name = jsonObject.optString("widgetName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (count > 0) {
                    /*Intent intent = new Intent(DuiApplication.getContext(), PlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data", data);
                    DuiApplication.getContext().startActivity(intent);*/
                }
                break;
            case MessageType.SYS_DIALOG_STATE:

                break;
            default:
                break;
        }
    }
}
