package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * AI設定(設定) param クラス
 * @author nishida.t
 *
 */
public class A200068Param extends BaseParam {

    /**
     * AielMaster接続
     */
    @NotNull
    private String aielMasterConnection;

    /**
     * IPアドレス
     */
    @NotNull
    private List<Map<String, String>> ipAddress;

    /**
     * 異常判定時間
     */
    @NotNull
    private String alertTime;

    /**
     * 予約1
     */
    private String reservation1;

    /**
     * 予約2
     */
    private String reservation2;

    public String getAielMasterConnection() {
        return aielMasterConnection;
    }

    public void setAielMasterConnection(String aielMasterConnection) {
        this.aielMasterConnection = aielMasterConnection;
    }

    public List<Map<String, String>> getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(List<Map<String, String>> ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getReservation1() {
        return reservation1;
    }

    public void setReservation1(String reservation1) {
        this.reservation1 = reservation1;
    }

    public String getReservation2() {
        return reservation2;
    }

    public void setReservation2(String reservation2) {
        this.reservation2 = reservation2;
    }






}
