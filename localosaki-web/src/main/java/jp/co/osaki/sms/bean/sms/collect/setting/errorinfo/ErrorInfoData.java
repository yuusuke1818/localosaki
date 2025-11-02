package jp.co.osaki.sms.bean.sms.collect.setting.errorinfo;

import java.math.BigDecimal;
import java.util.Map;

import jp.co.osaki.osol.utility.MapUtility;
import jp.co.osaki.sms.SmsConstants.CONCENT_STA;
import jp.co.osaki.sms.SmsConstants.DEV_STA;
import jp.co.osaki.sms.SmsConstants.METER_STA;
import jp.co.osaki.sms.SmsConstants.TERM_STA;

/**
 * 異常情報画面表示データクラス.
 *
 * @author ozaki.y
 */
public class ErrorInfoData {

    /** 装置ID. */
    private String devId;

    /** 装置名称. */
    private String devName;

    /** 装置ステータス. */
    private BigDecimal devSta;

    /** コンセントレータ管理番号. */
    private Long concentId;

    /** コンセントレータステータス. */
    private BigDecimal concentSta;

    /** メーター管理番号. */
    private Long meterMngId;

    /** メーター状態. */
    private BigDecimal meterSta;

    /** 通信端末ステータス. */
    private BigDecimal termSta;

    /** コンセントレータ管理番号(表示用). */
    private String concentIdDisp;

    /** メーター管理番号(表示用). */
    private String meterMngIdDisp;

    /** ステータス表示Map. */
    private Map<String, String> statusDispMap;

    public ErrorInfoData() {
    }

    public ErrorInfoData(Map<String, String> statusDispMap) {
        setStatusDispMap(statusDispMap);
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public BigDecimal getDevSta() {
        return devSta;
    }

    public void setDevSta(BigDecimal devSta) {
        this.devSta = devSta;
    }

    public boolean isDevError() {
        BigDecimal devSta = getDevSta();
        return (devSta != null && DEV_STA.ERROR.getVal().compareTo(devSta) == 0);
    }

    public Long getConcentId() {
        return concentId;
    }

    public void setConcentId(Long concentId) {
        this.concentId = concentId;
        if (concentId != null) {
            setConcentIdDisp(String.format("%02d", concentId));
        }
    }

    public BigDecimal getConcentSta() {
        return concentSta;
    }

    public void setConcentSta(BigDecimal concentSta) {
        this.concentSta = concentSta;
    }

    public boolean isConcentError() {
        BigDecimal concentSta = getConcentSta();
        return (concentSta != null && CONCENT_STA.ERROR.getVal().compareTo(concentSta) == 0);
    }

    public Long getMeterMngId() {
        return meterMngId;
    }

    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
        if (meterMngId != null) {
            setMeterMngIdDisp(String.format("%03d", meterMngId));
        }
    }

    public BigDecimal getMeterSta() {
        return meterSta;
    }

    public void setMeterSta(BigDecimal meterSta) {
        this.meterSta = meterSta;
    }

    public boolean isMeterError() {
        BigDecimal meterSta = getMeterSta();
        return (meterSta != null && METER_STA.ERROR.getVal().compareTo(meterSta) == 0);
    }

    public BigDecimal getTermSta() {
        return termSta;
    }

    public void setTermSta(BigDecimal termSta) {
        this.termSta = termSta;
    }

    public boolean isTermError() {
        BigDecimal termSta = getTermSta();
        return (termSta != null && TERM_STA.ERROR.getVal().compareTo(termSta) == 0);
    }

    public String getConcentIdDisp() {
        return concentIdDisp;
    }

    public void setConcentIdDisp(String concentIdDisp) {
        this.concentIdDisp = concentIdDisp;
    }

    public String getMeterMngIdDisp() {
        return meterMngIdDisp;
    }

    public void setMeterMngIdDisp(String meterMngIdDisp) {
        this.meterMngIdDisp = meterMngIdDisp;
    }

    public Map<String, String> getStatusDispMap() {
        return statusDispMap;
    }

    public void setStatusDispMap(Map<String, String> statusDispMap) {
        this.statusDispMap = statusDispMap;
    }

    public String getDevStaDisp() {
        return getStatusDisp(getDevSta());
    }

    public String getConcentStaDisp() {
        return getStatusDisp(getConcentSta());
    }

    public String getMeterStaDisp() {
        return getStatusDisp(getMeterSta());
    }

    public String getTermStaDisp() {
        return getStatusDisp(getTermSta());
    }

    private String getStatusDisp(BigDecimal status) {
        if (status == null) {
            return "";
        }

        return MapUtility.searchValueOfKeyName(getStatusDispMap(), status.toPlainString());
    }
}
