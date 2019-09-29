package pumpkin.org.speech.business.message;

/**
 * @ProjectName: AngryPandaSpeech
 * @ClassName: MessageType
 * @Author: 刘志保
 * @CreateDate: 2019/9/29 10:13
 * @Description: java类作用描述
 */
public class MessageType {

    public static final String SYS_DIALOG_STATE = "sys.dialog.state";
    public static final String CONTEXT_OUTPUT_TEXT = "context.output.text";
    public static final String CONTEXT_INPUT_TEXT = "context.input.text";
    public static final String CONTEXT_WIDGET_CONTENT = "context.widget.content";
    public static final String CONTEXT_WIDGET_LIST = "context.widget.list";
    public static final String CONTEXT_WIDGET_WEB = "context.widget.web";
    public static final String CONTEXT_WIDGET_MEDIA = "context.widget.media";
    public static final String CONTEXT_WIDGET_CUSTOM = "context.widget.custom";

    public static final String SYS_RESOURCE_UPDATED = "sys.resource.updated";

    public static String[] SUBSCRIBE_KEYS = new String[]{
            SYS_DIALOG_STATE,
            CONTEXT_OUTPUT_TEXT,
            CONTEXT_INPUT_TEXT,
            CONTEXT_WIDGET_CONTENT,
            CONTEXT_WIDGET_LIST,
            CONTEXT_WIDGET_WEB,
            CONTEXT_WIDGET_MEDIA,
            CONTEXT_WIDGET_CUSTOM
    };
}
