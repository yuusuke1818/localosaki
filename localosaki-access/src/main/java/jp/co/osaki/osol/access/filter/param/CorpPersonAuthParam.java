package jp.co.osaki.osol.access.filter.param;

/**
 *
 * 担当者権限検索パラメータクラス
 *
 * @author take_suzuki
 */
public class CorpPersonAuthParam {

    /**
     * ログイン企業ID 定数
     */
    public static final String LOGIN_CORP_ID = "loginCorpId";

    /**
     * ログイン企業ID
     */
    private String loginCorpId;

    /**
     * ログイン担当者ID 定数
     */
    public static final String LOGIN_PERSON_ID = "loginPersonId";

    /**
     * ログイン担当者ID
     */
    private String loginPersonId;

    /**
     * 操作企業ID
     */
    public static final String OPERATION_CORP_ID = "operationCorpId";

    /**
     * 操作企業ID
     */
    private String operationCorpId;

    /**
     *
     * コンストラクタ
     *
     * @param loginCorpId ログイン企業ID
     * @param loginPersonId ログイン担当者ID
     * @param operationCorpId 操作企業ID
     */
    public CorpPersonAuthParam(String loginCorpId, String loginPersonId, String operationCorpId) {
        this.loginCorpId = loginCorpId;
        this.loginPersonId = loginPersonId;
        this.operationCorpId = operationCorpId;
    }

    /**
     *
     * ログイン企業ID取得
     *
     * @return ログイン企業ID
     */
    public String getLoginCorpId() {
        return loginCorpId;
    }

    /**
     *
     * ログイン企業ID設定
     *
     * @param loginCorpId ログイン企業ID
     */
    public void setCorpId(String loginCorpId) {
        this.loginCorpId = loginCorpId;
    }

    /**
     *
     * ログイン担当者ID取得
     *
     * @return ログイン担当者ID
     */
    public String getLoginPersonId() {
        return loginPersonId;
    }

    /**
     *
     * ログイン担当者ID設定
     *
     * @param loginPersonId ログイン担当者ID
     */
    public void setPersonId(String loginPersonId) {
        this.loginPersonId = loginPersonId;
    }

    /**
     *
     * 操作企業ID取得
     *
     * @return 操作企業ID
     */
    public String getOperationCorpId() {
        return operationCorpId;
    }

    /**
     *
     * 操作企業ID設定
     *
     * @param operationCorpId 操作企業ID
     */
    public void setOperationCorpId(String operationCorpId) {
        this.operationCorpId = operationCorpId;
    }

}
