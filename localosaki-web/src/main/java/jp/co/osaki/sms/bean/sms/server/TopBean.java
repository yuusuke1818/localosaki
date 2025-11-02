package jp.co.osaki.sms.bean.sms.server;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.sms.SmsConversationBean;

/**
 * メインメニュー画面
 * SMSサーバ設定用
 *
 * @author yoneda_y
 */
@Named(value = "smsServerTopBean")
@ConversationScoped
public class TopBean extends SmsConversationBean implements Serializable {

    //シリアライズID
    private static final long serialVersionUID = -2430920005828752046L;

    @Inject
    private TopBeanProperty topBeanProperty;

    @Override
    public String init() {
        // アクセスログ出力
        exportAccessLog("init", "ボタン「SMSサーバー設定」押下");

        conversationStart();
        topBeanProperty.setSelectCollectFlg(false);
        return "serverTop";
    }

    /* Getter Setter */
    public TopBeanProperty getTopBeanProperty() {
        return topBeanProperty;
    }

    public void setTopBeanProperty(TopBeanProperty topBeanProperty) {
        this.topBeanProperty = topBeanProperty;
    }

}
