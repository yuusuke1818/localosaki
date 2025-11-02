package jp.co.osaki.sms.bean.sms.server.setting.handy;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "smsServerSettingHandyEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 8353377599991406510L;

    /**
     * 更新処理フラグ(更新:true/新規作成:false)
     */
    private Boolean updateProcessFlg;

    /**
     * ハンディ端末ID
     */
    private String devId;

    /**
     * HTTPヘッダ内 BASIC認証で使用
     */
    private String devPw;

    /**
     * 装置名称（機器名称）
     */
    private String name;

    /**
     * 削除フラグ
     */
    private Boolean delFlg;

    /**
     * 排他制御用カラム
     */
    private Integer version;

    public Boolean getUpdateProcessFlg() {
        return updateProcessFlg;
    }

    public void setUpdateProcessFlg(Boolean updateProcessFlg) {
        this.updateProcessFlg = updateProcessFlg;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevPw() {
        return devPw;
    }

    public void setDevPw(String devPw) {
        this.devPw = devPw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
