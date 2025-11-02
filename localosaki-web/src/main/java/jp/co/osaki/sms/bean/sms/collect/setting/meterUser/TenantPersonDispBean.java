package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPerson;

/**
 * 担当テナント表示用Bean
 *
 * @author nishida.t
 */
public class TenantPersonDispBean {

    // 一覧表示用
    // 企業ID
    private String corpId;
    // 建物ID
    private Long buildingId;
    // 建物番号
    private String buildingNo;
    // 建物名
    private String buildingName;
    // テナントフラグ
    private boolean tenantFlg;
    // 都道府県
    private String prefectureName;
    // 住所
    private String address;
    // 建物名
    private String addressBuilding;
    // 建物状況　
    private String buildingStatus;

    // テナントID（ユーザーコード）
    private String tenantId;

    // 担当メーターテナント(テーブルではレコードが存在するとチェックが入る)
    private boolean buildingPersonFlg;

    // 現在情報を保持しておく
    private TBuildingPerson tBuildingPerson;
    private TBuilding tBuilding;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public boolean getTenantFlg() {
        return tenantFlg;
    }

    public void setTenantFlg(boolean tenantFlg) {
        this.tenantFlg = tenantFlg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isBuildingPersonFlg() {
        return buildingPersonFlg;
    }

    public void setBuildingPersonFlg(boolean buildingPersonFlg) {
        this.buildingPersonFlg = buildingPersonFlg;
    }

    public TBuildingPerson gettBuildingPerson() {
        return tBuildingPerson;
    }

    public void settBuildingPerson(TBuildingPerson tBuildingPerson) {
        this.tBuildingPerson = tBuildingPerson;
    }

    public TBuilding gettBuilding() {
        return tBuilding;
    }

    public void settBuilding(TBuilding tBuilding) {
        this.tBuilding = tBuilding;
    }

    public String getPrefectureName() {
        return prefectureName;
    }

    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

}
