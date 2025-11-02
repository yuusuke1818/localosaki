package jp.co.osaki.sms.resultset;

import jp.co.osaki.osol.api.OsolApiResultData;

/**
 * 任意検針 ユーザーコードプルダウン ResultSetクラス.
 *
 * @author yonezawa.a
 */
public final class UserCdListResultSet extends OsolApiResultData {

    /**
     * テナントID（ユーザーコード）
     */
    private Long tenantId;

    /**
     * テナント名
     */
    private String buildingName;

    public UserCdListResultSet() {
    }

    public UserCdListResultSet(Long tenantId, String buildingName) {
        this.tenantId = tenantId;
        this.buildingName = buildingName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

}
