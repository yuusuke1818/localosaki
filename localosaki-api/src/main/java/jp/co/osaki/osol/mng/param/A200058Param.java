package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
*
* デマンド(設定) Eα Param クラス.
*
* @autho t_hayama
*
*/
public class A200058Param extends BaseParam {

    /**
     * SmartLEDZ接続
     */
    @NotNull
    @Pattern(regexp="[0-9]")
    private String connectionSmartLedz;

    /**
     * 照明制御切替
     */
    @NotNull
    @Pattern(regexp="[0-9]")
    private String switchLightControl;

    /**
     * システムID
     */
    @NotNull
    private String systemId;

    /**
     * ゲートウェイIP
     */
    @NotNull
    private Map<String, Object> gatewayIp;

    /**
     * グループリスト
     */
    @Size(max=50, min=0)
    private List<Map<String, String>> groupList;

    /**
     * ゾーンリスト
     */
    @Size(max=50, min=20)
    private List<Map<String, String>> zoneList;

    /**
     * パターンリスト
     */
    @Size(max=16, min=16)
    private List<Map<String, Object>> patternList;


    public String getConnectionSmartLedz() {
        return connectionSmartLedz;
    }

    public void setConnectionSmartLedz(String connectionSmartLedz) {
        this.connectionSmartLedz = connectionSmartLedz;
    }

    public String getSwitchLightControl() {
        return switchLightControl;
    }

    public void setSwitchLightControl(String switchLightControl) {
        this.switchLightControl = switchLightControl;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public Map<String, Object> getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(Map<String, Object> gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    public List<Map<String, String>> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Map<String, String>> groupList) {
        this.groupList = groupList;
    }

    public List<Map<String, String>> getZoneList() {
        return zoneList;
    }

    public void setZoneList(List<Map<String, String>> zoneList) {
        this.zoneList = zoneList;
    }

    public List<Map<String,Object>> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<Map<String,Object>> patternList) {
        this.patternList = patternList;
    }

}
