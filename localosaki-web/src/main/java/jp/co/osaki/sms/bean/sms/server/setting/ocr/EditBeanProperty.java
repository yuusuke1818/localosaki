package jp.co.osaki.sms.bean.sms.server.setting.ocr;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "smsServerSettingOcrEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 2747238651980505745L;

    /**
     * 更新処理フラグ(更新:true/新規作成:false)
     */
    private Boolean updateProcessFlg;

    /**
     * AieLinkID
     */
    private String devId;

    /**
     * 装置名称（機器名称）
     */
    private String name;

    /**
     * 検満通知月(Nヶ月前)
     */
    private String examNoticeMonth;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamNoticeMonth() {
        return examNoticeMonth;
    }

    public void setExamNoticeMonth(String examNoticeMonth) {
        this.examNoticeMonth = examNoticeMonth;
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
