package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "smsServerSettingBuildingDeviceEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 4059467908562837416L;

    // 建物情報
    private BuildingInfo buildingInfo;

    // 装置ID DBデータ
    private List<String> orgDevIdList;

    // 装置ID 追加リスト
    private List<Object> addDevIdList;

    // 装置ID 削除リスト
    private List<Object> deleteDevIdList;

    // 装置ID（追加）
    private String devId;

    // 装置ID（情報）
    private List<String> devIdList;

    // 装置ID 追加フラグ
    private boolean addDevIdFlg;

    public BuildingInfo getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo(BuildingInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public List<String> getOrgDevIdList() {
        return orgDevIdList;
    }

    public void setOrgDevIdList(List<String> orgDevIdList) {
        this.orgDevIdList = orgDevIdList;
    }

    public List<Object> getAddDevIdList() {
        return addDevIdList;
    }

    public void setAddDevIdList(List<Object> addDevIdList) {
        this.addDevIdList = addDevIdList;
    }

    public List<Object> getDeleteDevIdList() {
        return deleteDevIdList;
    }

    public void setDeleteDevIdList(List<Object> deleteDevIdList) {
        this.deleteDevIdList = deleteDevIdList;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public List<String> getDevIdList() {
        return devIdList;
    }

    public void setDevIdList(List<String> devIdList) {
        this.devIdList = devIdList;
    }

    public boolean isAddDevIdFlg() {
        return addDevIdFlg;
    }

    public void setAddDevIdFlg(boolean addDevIdFlg) {
        this.addDevIdFlg = addDevIdFlg;
    }

}
