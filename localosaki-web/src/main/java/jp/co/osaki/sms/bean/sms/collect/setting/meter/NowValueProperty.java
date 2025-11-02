/**
 *
 */
package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.Dependent;

/**
 * メーター管理 現在値情報を持つプロパティ
 * @author kimura.m
 */
@Dependent
public class NowValueProperty implements Serializable {

    /** シリアライズID */
    private static final long serialVersionUID = 8767917237388527960L;

    /** 存在フラグ(メッセージ表示用フラグ) 現在値データが存在する場合true */
    private boolean existenceFlag;

    /** メッセージ表示用 現在値データが存在しない場合に画面に表示するメッセージがセットされる */
    private String existenceMsg1;
    private String existenceMsg2;

    /** メータ管理番号 */
    private Long meterMngId;

    /** 装置ID */
    private String devId;

    /** 計器ID */
    private String meterId;

    // 共通
    /** 要求中か否かのフラグ */
    private String demandingFlg;

    /** 要求中メッセージ */
    private String demandingMsg;

    private String srvEnt;

    private String currentKwh1;

    private Date measureDate;

    // パルスのみ
    private BigDecimal multi;

    private String pulseType;

    private BigDecimal pulseWeight;


    // スマートのみ
    private BigDecimal ampere1;

    private BigDecimal ampere3;

    private BigDecimal ifType;

    private String circuitBreaker;

    private String currentKwh2;

    private BigDecimal momentaryPwr;

    private BigDecimal voltage12;

    private BigDecimal voltage23;

    // 表示用
    private String measureDateDisp;

    private String ifTypeDisp;

    private String circuitBreakerDisp;

    private String pulseTypeDisp;

    /**
     * @return meterMngId
     */
    public Long getMeterMngId() {
        return meterMngId;
    }

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @return meterId
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * @return demandingFlg
     */
    public String getDemandingFlg() {
        return demandingFlg;
    }

    /**
     * @return srvEnt
     */
    public String getSrvEnt() {
        return srvEnt;
    }

    /**
     * @return currentKwh1
     */
    public String getCurrentKwh1() {
        return currentKwh1;
    }

    /**
     * @return measureDate
     */
    public Date getMeasureDate() {
        return measureDate;
    }

    /**
     * @return multi
     */
    public BigDecimal getMulti() {
        return multi;
    }

    /**
     * @return pulseType
     */
    public String getPulseType() {
        return pulseType;
    }

    /**
     * @return pulseWeight
     */
    public BigDecimal getPulseWeight() {
        return pulseWeight;
    }

    /**
     * @return ampere1
     */
    public BigDecimal getAmpere1() {
        return ampere1;
    }

    /**
     * @return ampere3
     */
    public BigDecimal getAmpere3() {
        return ampere3;
    }

    /**
     * @return ifType
     */
    public BigDecimal getIfType() {
        return ifType;
    }

    /**
     * @return circuitBreaker
     */
    public String getCircuitBreaker() {
        return circuitBreaker;
    }

    /**
     * @return currentKwh2
     */
    public String getCurrentKwh2() {
        return currentKwh2;
    }

    /**
     * @return momentaryPwr
     */
    public BigDecimal getMomentaryPwr() {
        return momentaryPwr;
    }

    /**
     * @return voltage12
     */
    public BigDecimal getvoltage12() {
        return voltage12;
    }

    /**
     * @return voltage23
     */
    public BigDecimal getVoltage23() {
        return voltage23;
    }

    /**
     * @param meterMngId セットする meterMngId
     */
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @param meterId セットする meterId
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    /**
     * @param demandingFlg セットする demandingFlg
     */
    public void setDemandingFlg(String demandingFlg) {
        this.demandingFlg = demandingFlg;
    }

    /**
     * @param srvEnt セットする srvEnt
     */
    public void setSrvEnt(String srvEnt) {
        this.srvEnt = srvEnt;
    }

    /**
     * @param currentKwh1 セットする currentKwh1
     */
    public void setCurrentKwh1(String currentKwh1) {
        this.currentKwh1 = currentKwh1;
    }

    /**
     * @param measureDate セットする measureDate
     */
    public void setMeasureDate(Date measureDate) {
        this.measureDate = measureDate;
    }

    /**
     * @param multi セットする multi
     */
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }

    /**
     * @param pulseType セットする pulseType
     */
    public void setPulseType(String pulseType) {
        this.pulseType = pulseType;
    }

    /**
     * @param pulseWeight セットする pulseWeight
     */
    public void setPulseWeight(BigDecimal pulseWeight) {
        this.pulseWeight = pulseWeight;
    }

    /**
     * @param ampere1 セットする ampere1
     */
    public void setAmpere1(BigDecimal ampere1) {
        this.ampere1 = ampere1;
    }

    /**
     * @param ampere3 セットする ampere3
     */
    public void setAmpere3(BigDecimal ampere3) {
        this.ampere3 = ampere3;
    }

    /**
     * @param ifType セットする ifType
     */
    public void setIfType(BigDecimal ifType) {
        this.ifType = ifType;
    }

    /**
     * @param circuitBreaker セットする circuitBreaker
     */
    public void setCircuitBreaker(String circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    /**
     * @param currentKwh2 セットする currentKwh2
     */
    public void setCurrentKwh2(String currentKwh2) {
        this.currentKwh2 = currentKwh2;
    }

    /**
     * @param momentaryPwr セットする momentaryPwr
     */
    public void setMomentaryPwr(BigDecimal momentaryPwr) {
        this.momentaryPwr = momentaryPwr;
    }

    /**
     * @param voltage12 セットする voltage12
     */
    public void setvoltage12(BigDecimal voltage12) {
        this.voltage12 = voltage12;
    }

    /**
     * @param voltage23 セットする voltage23
     */
    public void setVoltage23(BigDecimal voltage23) {
        this.voltage23 = voltage23;
    }

    /**
     * @return existenceFlag
     */
    public boolean isExistenceFlag() {
        return existenceFlag;
    }

    /**
     * @param existenceFlag セットする existenceFlag
     */
    public void setExistenceFlag(boolean existenceFlag) {
        this.existenceFlag = existenceFlag;
    }

    /**
     * @return measureDateDisp
     */
    public String getMeasureDateDisp() {
        return measureDateDisp;
    }

    /**
     * @param measureDateDisp セットする measureDateDisp
     */
    public void setMeasureDateDisp(String measureDateDisp) {
        this.measureDateDisp = measureDateDisp;
    }

    /**
     * @return ifTypeDisp
     */
    public String getIfTypeDisp() {
        return ifTypeDisp;
    }

    /**
     * @return circuitBreakerDisp
     */
    public String getCircuitBreakerDisp() {
        return circuitBreakerDisp;
    }

    /**
     * @param ifTypeDisp セットする ifTypeDisp
     */
    public void setIfTypeDisp(String ifTypeDisp) {
        this.ifTypeDisp = ifTypeDisp;
    }

    /**
     * @param circuitBreakerDisp セットする circuitBreakerDisp
     */
    public void setCircuitBreakerDisp(String circuitBreakerDisp) {
        this.circuitBreakerDisp = circuitBreakerDisp;
    }

    /**
     * @return existenceMsg
     */
    public String getExistenceMsg1() {
        return existenceMsg1;
    }

    /**
     * @param existenceMsg セットする existenceMsg
     */
    public void setExistenceMsg1(String existenceMsg1) {
        this.existenceMsg1 = existenceMsg1;
    }

    /**
     * @return pulseTypeDisp
     */
    public String getPulseTypeDisp() {
        return pulseTypeDisp;
    }

    /**
     * @param pulseTypeDisp セットする pulseTypeDisp
     */
    public void setPulseTypeDisp(String pulseTypeDisp) {
        this.pulseTypeDisp = pulseTypeDisp;
    }

    public String getDemandingMsg() {
        return demandingMsg;
    }

    public void setDemandingMsg(String demandingMsg) {
        this.demandingMsg = demandingMsg;
    }

    public String getExistenceMsg2() {
        return existenceMsg2;
    }

    public void setExistenceMsg2(String existenceMsg2) {
        this.existenceMsg2 = existenceMsg2;
    }

}
