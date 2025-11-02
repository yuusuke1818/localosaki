package jp.co.osaki.sms.bean.common;

/**
 * 企業テーブルのバージョンチェックを保持するPropertyクラス
 * @author ya-ishida
 *
 */
public class CorpVersionCheckProperty {

    /**
     * 企業ID
     */
    private String corpId;

    /**
     * VERSION（企業）
     */
    private Integer version;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
