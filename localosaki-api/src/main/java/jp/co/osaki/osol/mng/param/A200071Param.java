package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.Pattern;

/**
 * 動作モード(取得) param クラス
 * @author nakao.h
 *
 */
public class A200071Param extends BaseParam {

    /**
     * 設定変更履歴
     */
    @Pattern(regexp="[0-9]")
    private String settingChangeHist;

    /**
     * 設定変更日時
     */
    private String settingDate;

    /**
     * 音量
     */
    private String volumeLevel;

    /**
     * RS-485 通信待ち時間
     */
    private String communicationWait;

    /**
     *  RS-485 通信速度
     */
    private String communicationSpeed;

    /**
     *  予約1
     */
    private String reservation1;

    /**
     * CO2換算係数
     */
    private String convartCoefficientCO2;

    /**
     * 基本電気料金
     */
    private String basicCharge;

    /**
     * 使用料金単価
     */
    private String useUnitCost;

    /**
     * パルス・Bルート検出器接続
     */
    private String pulseRoutebConnection;

    /**
     * 無線チャンネル
     */
    private String wirelessChannel;

    /**
     * 表示装置接続
     */
    private String displayConnection;

    public String getSettingChangeHist() {
        return settingChangeHist;
    }

    public void setSettingChangeHist(String settingChangeHist) {
        this.settingChangeHist = settingChangeHist;
    }

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public String getVolumeLevel() {
		return volumeLevel;
	}

	public void setVolumeLevel(String volumeLevel) {
		this.volumeLevel = volumeLevel;
	}

	public String getCommunicationWait() {
		return communicationWait;
	}

	public void setCommunicationWait(String communicationWait) {
		this.communicationWait = communicationWait;
	}

	public String getCommunicationSpeed() {
		return communicationSpeed;
	}

	public void setCommunicationSpeed(String communicationSpeed) {
		this.communicationSpeed = communicationSpeed;
	}

	public String getReservation1() {
		return reservation1;
	}

	public void setReservation1(String reservation1) {
		this.reservation1 = reservation1;
	}

	public String getConvartCoefficientCO2() {
		return convartCoefficientCO2;
	}

	public void setConvartCoefficientCO2(String convartCoefficientCO2) {
		this.convartCoefficientCO2 = convartCoefficientCO2;
	}

	public String getBasicCharge() {
		return basicCharge;
	}

	public void setBasicCharge(String basicCharge) {
		this.basicCharge = basicCharge;
	}

	public String getUseUnitCost() {
		return useUnitCost;
	}

	public void setUseUnitCost(String useUnitCost) {
		this.useUnitCost = useUnitCost;
	}

	public String getPulseRoutebConnection() {
		return pulseRoutebConnection;
	}

	public void setPulseRoutebConnection(String pulseRoutebConnection) {
		this.pulseRoutebConnection = pulseRoutebConnection;
	}

	public String getWirelessChannel() {
		return wirelessChannel;
	}

	public void setWirelessChannel(String wirelessChannel) {
		this.wirelessChannel = wirelessChannel;
	}

	public String getDisplayConnection() {
		return displayConnection;
	}

	public void setDisplayConnection(String displayConnection) {
		this.displayConnection = displayConnection;
	}


}
