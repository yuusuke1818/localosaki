package jp.co.osaki.sms.deviceCtrl.resultset;

public class MDevRelationResultSet {

    private String dev_kind;
    private String dev_id;
    private Long dm2_id;
    private String parent_dev_id;
    private String corp_id;
    private Long building_id;
    private String ip_addr;

    public MDevRelationResultSet(String dev_kind, String dev_id, Long dm2_id, String parent_dev_id, String corp_id, Long building_id, String ip_addr) {
        this.dev_kind = dev_kind;
        this.dev_id = dev_id;
        this.dm2_id = dm2_id;
        this.parent_dev_id = parent_dev_id;
        this.corp_id = corp_id;
        this.building_id = building_id;
        this.ip_addr = ip_addr;
    }

    public MDevRelationResultSet() {

    }

    public String getDev_kind() {
        return dev_kind;
    }

    public void setDev_kind(String dev_kind) {
        this.dev_kind = dev_kind;
    }

    public String getDev_id() {
        return dev_id;
    }

    public void setDev_id(String dev_id) {
        this.dev_id = dev_id;
    }

    public Long getDm2_id() {
        return dm2_id;
    }

    public void setDm2_id(Long dm2_id) {
        this.dm2_id = dm2_id;
    }

    public String getParent_dev_id() {
        return parent_dev_id;
    }

    public void setParent_dev_id(String parent_dev_id) {
        this.parent_dev_id = parent_dev_id;
    }

    public String getCorp_id() {
        return corp_id;
    }

    public void setCorp_id(String corp_id) {
        this.corp_id = corp_id;
    }

    public Long getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Long building_id) {
        this.building_id = building_id;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }
}
