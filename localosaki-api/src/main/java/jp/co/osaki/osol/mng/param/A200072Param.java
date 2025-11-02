package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 動作モード(設定) param クラス
 * @author nakao.h
 *
 */
public class A200072Param extends BaseParam {

    /**
     * 音量
     */
    @NotNull
    @Pattern(regexp="[0-3]")
    private String volumeLevel;

    /**
     * RS-485 通信待ち時間
     */
    @NotNull
    @Pattern(regexp="[0-9]")
    private String communicationWait;

    /**
     *  RS-485 通信速度
     */
    @NotNull
    @Pattern(regexp="[0-1]")
    private String communicationSpeed;

    /**
     * 予約1
     */
    private String reservation1;

    /**
     * CO2換算係数
     */
    @NotNull
    @Pattern(regexp="[0-9]{1,3}")
    private String convartCoefficientCO2;

    /**
     * 基本電気料金
     */
    @NotNull
    @Pattern(regexp="[0-9]{1,4}")
    private String basicCharge;

    /**
     * 使用料金単価
     */
    @NotNull
    @Pattern(regexp="[0-9]{1,4}")
    private String useUnitCost;

    /**
     * パルス・Bルート検出器接続
     */
    @NotNull
    @Pattern(regexp="[0-1]")
    private String pulseRoutebConnection;

    /**
     * 無線チャンネル
     */
    @NotNull
    @Pattern(regexp="[1-4]")
    private String wirelessChannel;

    /**
     * 表示装置接続
     */
    @NotNull
    @Pattern(regexp="[0-4]")
    private String displayConnection;

    /**
     * 予約2
     */
    private String reservation2;


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

    public String getReservation2() {
        return reservation2;
    }

    public void setReservation2(String reservation2) {
        this.reservation2 = reservation2;
    }

}
