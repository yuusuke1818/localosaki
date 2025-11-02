package jp.co.osaki.osol.mng.param;

import java.util.Map;

/**
*
* 装置情報(取得) Param クラス
*
* @author t_sakamoto
*
*/
public class A200034Param extends BaseParam {

    /**
     * 取得日時(設定変更日時)
     */
    private String settingDate;

    /**
     * 製品識別情報
     */
    private String productDiscriminationInfo;

    /**
     * バージョン情報
     */
    private String versionInfo;

    /**
     * 時計同期状態
     */
    private String clockSyncState;

    /**
     * パルス入力モニタ
     */
    private String pulseInputMonitor;

    /**
     * エラー情報
     */
    private String errorInfo;

    /**
     * エラー情報2
     */
    private String errorInfo2;

    /**
     * 電源周波数
     */
    private String powerFrequency;

    /**
     * LONROMバージョン
     */
    private String versionLONROM;

    /**
     * 予備（装置情報）
     */
    private String reserve;

    /**
     * サブROMバージョン情報
     */
    private String versionInfoSubROM;

    /**
     * 無線モジュールバージョン
     */
    private Map<String, Object> versionWirelessModule;

    /**
     * 予約領域 (レスポンスに含めない情報)
     */
    private String reserveArea;


    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getProductDiscriminationInfo() {
        return productDiscriminationInfo;
    }

    public void setProductDiscriminationInfo(String productDiscriminationInfo) {
        this.productDiscriminationInfo = productDiscriminationInfo;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getClockSyncState() {
        return clockSyncState;
    }

    public void setClockSyncState(String clockSyncState) {
        this.clockSyncState = clockSyncState;
    }

    public String getPulseInputMonitor() {
        return pulseInputMonitor;
    }

    public void setPulseInputMonitor(String pulseInputMonitor) {
        this.pulseInputMonitor = pulseInputMonitor;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorInfo2() {
        return errorInfo2;
    }

    public void setErrorInfo2(String errorInfo2) {
        this.errorInfo2 = errorInfo2;
    }

    public String getPowerFrequency() {
        return powerFrequency;
    }

    public void setPowerFrequency(String powerFrequency) {
        this.powerFrequency = powerFrequency;
    }

    public String getVersionLONROM() {
        return versionLONROM;
    }

    public void setVersionLONROM(String versionLONROM) {
        this.versionLONROM = versionLONROM;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getVersionInfoSubROM() {
        return versionInfoSubROM;
    }

    public void setVersionInfoSubROM(String versionInfoSubROM) {
        this.versionInfoSubROM = versionInfoSubROM;
    }

    public Map<String, Object> getVersionWirelessModule() {
        return versionWirelessModule;
    }

    public void setVersionWirelessModule(Map<String, Object> versionWirelessModule) {
        this.versionWirelessModule = versionWirelessModule;
    }

    public String getReserveArea() {
        return reserveArea;
    }

    public void setReserveArea(String reserveArea) {
        this.reserveArea = reserveArea;
    }

}
