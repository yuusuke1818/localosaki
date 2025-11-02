package jp.co.osaki.sms.bean.sms.server.setting.collect;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named(value = "smsServerSettingCollectEditBeanProperty")
@Dependent
public class EditBeanProperty implements Serializable {

    /**
     * シリアライズID
     */
    private static final long serialVersionUID = 8186390823707746670L;

    /**
     * 更新処理フラグ(更新:true/新規作成:false)
     */
    private Boolean updateProcessFlg;

    /**
     * 装置ID
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
     * コメント
     */
    private String memo;

    /**
     * IPアドレス
     */
    private String ipAddr;

    /**
     * ホームディレクトリ名
     */
    private String homeDirectory;

    /**
     * 検満通知月(Nヶ月前)
     */
    private String examNoticeMonth;

    /**
     * 警報メール対象外 = 1 警報メール対象 = 0
     */
    private Boolean alertDisableFlg;

    /**
     * 逆潮対応機能（0：未使用　1：使用）
     */
    private Boolean revFlg;

    /**
     * 通信間隔
     */
    private String commInterval;

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public String getExamNoticeMonth() {
        return examNoticeMonth;
    }

    public void setExamNoticeMonth(String examNoticeMonth) {
        this.examNoticeMonth = examNoticeMonth;
    }

    public Boolean getAlertDisableFlg() {
        return alertDisableFlg;
    }

    public void setAlertDisableFlg(Boolean alertDisableFlg) {
        this.alertDisableFlg = alertDisableFlg;
    }

    public Boolean getRevFlg() {
        return revFlg;
    }

    public void setRevFlg(Boolean revFlg) {
        this.revFlg = revFlg;
    }

    public String getCommInterval() {
        return commInterval;
    }

    public void setCommInterval(String commInterval) {
        this.commInterval = commInterval;
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
