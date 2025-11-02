package jp.co.osaki.osol.access.filter.param;

/**
 *
 * 担当者パラメータ クラス
 *
 * @author take_suzuki
 */
public class PersonDataParam {

    /**
     * 企業ID
     */
    public static final String LOGIN_CORP_ID = "loginCorpId";

    private String loginCorpId;

    /**
     * 担当者ID
     */
    public static final String LOGIN_PERSON_ID = "loginPersonId";

    private String loginPersonId;

    /**
     *
     * コンストラクタ
     *
     * @param loginCorpId ログイン企業ID
     * @param loginPersonId ログイン担当者ID
     */
    public PersonDataParam(String loginCorpId, String loginPersonId) {
        this.loginCorpId = loginCorpId;
        this.loginPersonId = loginPersonId;
    }

    public String getLoginCorpId() {
        return loginCorpId;
    }

    public void setLoginCorpId(String loginCorpId) {
        this.loginCorpId = loginCorpId;
    }

    public String getLoginPersonId() {
        return loginPersonId;
    }

    public void setLoginPersonId(String loginPersonId) {
        this.loginPersonId = loginPersonId;
    }

}
