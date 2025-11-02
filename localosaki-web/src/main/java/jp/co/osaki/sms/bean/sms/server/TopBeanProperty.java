package jp.co.osaki.sms.bean.sms.server;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * メインメニュー画面
 * SMSサーバ設定用Property
 *
 * @author yoneda_y
 */
@Named(value = "smsServerTopBeanProperty")
@Dependent
public class TopBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 4643657874062250707L;

    /**
     * 選択したボタンのID
     * デザイン付与のターゲット
     */
    private String selectId;

    /**
     * データ収集装置 チェックフラグ
     */
    private Boolean selectCollectFlg;

    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public Boolean getSelectCollectFlg() {
        return selectCollectFlg;
    }

    public void setSelectCollectFlg(Boolean selectCollectFlg) {
        this.selectCollectFlg = selectCollectFlg;
    }

}
