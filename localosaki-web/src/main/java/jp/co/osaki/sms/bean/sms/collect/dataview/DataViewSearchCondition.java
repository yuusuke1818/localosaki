package jp.co.osaki.sms.bean.sms.collect.dataview;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import jp.co.osaki.sms.SmsConstants;

/**
 * データ収集装置 データ表示機能 検索条件クラス.
 *
 * @author ozaki.y
 */
@Dependent
public class DataViewSearchCondition implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = 8809103222439720283L;

    /** 選択値 接続先(装置ID). */
    private String devId;

    /** 選択値 表示方向. */
    private String dispDirect;

    /** 選択値 表示種別. */
    private String dispType;

    /** 選択値 表示時間単位. */
    private String dispTimeUnit;

    /** 入力値 テナント名. */
    private String tenantName;

    /** 対象メーター管理番号List. */
    private List<Long> meterMngIdList;

    /** 装置ID別 対象メーター管理番号List. */
    private Map<String, List<Long>> devIdMeterMngIdListMap;

    /** 対象日時List. */
    private List<String> targetDateTimeList;

    /** 対象日時フォーマット. */
    private String targetDateTimeFormat;

    public boolean isUse() {
        return SmsConstants.DISP_TYPE.USE.getVal().equals(getDispType());
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDispDirect() {
        return dispDirect;
    }

    public void setDispDirect(String dispDirect) {
        this.dispDirect = dispDirect;
    }

    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }

    public String getDispTimeUnit() {
        return dispTimeUnit;
    }

    public void setDispTimeUnit(String dispTimeUnit) {
        this.dispTimeUnit = dispTimeUnit;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public List<Long> getMeterMngIdList() {
        return meterMngIdList;
    }

    public void setMeterMngIdList(List<Long> meterMngIdList) {
        this.meterMngIdList = meterMngIdList;
    }

    public List<String> getTargetDateTimeList() {
        return targetDateTimeList;
    }

    public void setTargetDateTimeList(List<String> targetDateTimeList) {
        this.targetDateTimeList = targetDateTimeList;
    }

    public String getTargetDateTimeFormat() {
        return targetDateTimeFormat;
    }

    public void setTargetDateTimeFormat(String targetDateTimeFormat) {
        this.targetDateTimeFormat = targetDateTimeFormat;
    }

    public Map<String, List<Long>> getDevIdMeterMngIdListMap() {
        return devIdMeterMngIdListMap;
    }

    public void setDevIdMeterMngIdListMap(Map<String, List<Long>> devIdMeterMngIdListMap) {
        this.devIdMeterMngIdListMap = devIdMeterMngIdListMap;
    }

}
