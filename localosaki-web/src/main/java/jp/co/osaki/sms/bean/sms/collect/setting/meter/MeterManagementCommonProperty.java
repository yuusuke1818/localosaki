/**
 *
 */
package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

/**
 * メーター管理 メーター種別共通で使用する情報を持つプロパティ
 * @author kimura.m
 */
@Dependent
public class MeterManagementCommonProperty implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = -6427624090986746039L;

    /** 装置ID */
    private String devId;
    /** 接続先名 */
    private String devName;

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
    /**
     * @return devName
     */
    public String getDevName() {
        return devName;
    }
    /**
     * @param devName セットする devName
     */
    public void setDevName(String devName) {
        this.devName = devName;
    }
}
