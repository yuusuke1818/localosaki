package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

/**
 * メーター管理画面 一覧 プロパティクラス
 *
 * @author kimura.m
 */
@Dependent
public class MeterManagementSmartProperty implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 5286577893240907971L;

    /** 選択値 接続先. */
    private String devId;

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

}
