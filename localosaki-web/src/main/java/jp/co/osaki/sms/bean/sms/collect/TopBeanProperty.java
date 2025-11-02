package jp.co.osaki.sms.bean.sms.collect;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.co.osaki.sms.bean.building.info.ListInfo;

/**
 * メインメニュー画面
 * データ収集装置用Property
 *
 * @author yoneda_y
 */
@Named(value = "smsCollectTopBeanProperty")
@Dependent
public class TopBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 2995955343667744259L;

    /**
     * 選択したボタンのID
     * デザイン付与のターゲット
     */
    private String selectId;

    /**
     * データ収集装置 チェックフラグ
     */
    private Boolean selectCollectFlg;

    /**
     * 機器管理 チェックフラグ
     */
    private Boolean selectSettingFlg;

    /**
     * 検針設定 チェックフラグ
     */
    private Boolean selectMeterFlg;

    /**
     * 選択された施設情報
     */
    private ListInfo listInfo;

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

    public Boolean getSelectSettingFlg() {
        return selectSettingFlg;
    }

    public void setSelectSettingFlg(Boolean selectSettingFlg) {
        this.selectSettingFlg = selectSettingFlg;
    }

    public Boolean getSelectMeterFlg() {
        return selectMeterFlg;
    }

    public void setSelectMeterFlg(Boolean selectMeterFlg) {
        this.selectMeterFlg = selectMeterFlg;
    }

    public ListInfo getListInfo() {
        return listInfo;
    }

    public void setListInfo(ListInfo listInfo) {
        this.listInfo = listInfo;
    }

}
