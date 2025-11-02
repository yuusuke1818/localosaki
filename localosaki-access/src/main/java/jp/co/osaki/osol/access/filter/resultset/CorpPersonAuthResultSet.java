package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * 操作企業の担当者権限 取得結果
 *
 * @author take_suzuki
 */
public class CorpPersonAuthResultSet extends BaseResultSet {

    /**
     * ログイン担当者の企業ID
     */
    private String loginCorpId;

    /**
     * ログイン担当者の企業種別
     */
    private String loginCorpType;

    /**
     * ログイン担当者の担当者ID
     */
    private String loginPersonId;

    /**
     * ログイン担当者の担当者種別
     */
    private String loginPersonType;

    /**
     * 操作企業の企業ID
     */
    private String operationCorpId;

    /**
     * 操作企業の企業種別
     */
    private String operationCorpType;

    /**
     * 操作企業の企業担当者の権限種別
     */
    private String operationAuthorityType;

    /**
     * 操作企業の企業担当者の権限コード
     */
    private String operationAuthorityCd;

    /**
     * 操作企業の企業担当者の権限有効フラグ
     */
    private Integer operationAuthorityFlg;

    public CorpPersonAuthResultSet(String loginCorpId, String loginCorpType, 
                                            String loginPersonId, String loginPersonType, 
                                            String operationCorpId, String operationCorpType, 
                                            String operationAuthorityType, 
                                            String operationAuthorityCd, Integer operationAuthorityFlg) {
        this.loginCorpId = loginCorpId;
        this.loginCorpType = loginCorpType;
        this.loginPersonId = loginPersonId;
        this.loginPersonType = loginPersonType;
        this.operationCorpId = operationCorpId;
        this.operationCorpType = operationCorpType;
        this.operationAuthorityType = operationAuthorityType;
        this.operationAuthorityCd = operationAuthorityCd;
        this.operationAuthorityFlg = operationAuthorityFlg;
    }

    /**
     *
     * ログイン担当者の企業ID 取得
     * 
     * @return ログイン担当者の企業ID
     */
    public String getLoginCorpId() {
        return loginCorpId;
    }

    /**
     *
     * ログイン担当者の企業ID 設定
     * 
     * @param loginCorpId ログイン担当者の企業ID
     */
    public void setLoginCorpId(String loginCorpId) {
        this.loginCorpId = loginCorpId;
    }

    /**
     *
     * ログイン担当者の企業種別 取得
     * 
     * @return ログイン担当者の企業種別
     */
    public String getLoginCorpType() {
        return loginCorpType;
    }

    /**
     *
     * ログイン担当者の企業種別 設定
     * 
     * @param loginCorpType ログイン担当者の企業種別
     */
    public void setLoginCorpType(String loginCorpType) {
        this.loginCorpType = loginCorpType;
    }

    /**
     *
     * ログイン担当者の担当者ID 取得
     * 
     * @return ログイン担当者の担当者ID
     */
    public String getLoginPersonId() {
        return loginPersonId;
    }

    /**
     *
     * ログイン担当者の担当者ID 設定
     * 
     * @param loginPersonId ログイン担当者の担当者ID
     */
    public void setLoginPersonId(String loginPersonId) {
        this.loginPersonId = loginPersonId;
    }

    /**
     *
     * ログイン担当者の担当者種別 取得
     * 
     * @return ログイン担当者の担当者種別
     */
    public String getLoginPersonType() {
        return loginPersonType;
    }

    /**
     *
     * ログイン担当者の担当者種別 設定
     * 
     * @param loginPersonType ログイン担当者の担当者種別
     */
    public void setLoginPersonType(String loginPersonType) {
        this.loginPersonType = loginPersonType;
    }

    /**
     *
     * 操作企業の企業ID 取得
     * 
     * @return 操作企業の企業ID
     */
    public String getOperationCorpId() {
        return operationCorpId;
    }

    /**
     *
     * 操作企業の企業ID 設定
     * 
     * @param operationCorpId 操作企業の企業ID
     */
    public void setOperationCorpId(String operationCorpId) {
        this.operationCorpId = operationCorpId;
    }

    /**
     *
     * 操作企業の企業種別 取得
     * 
     * @return 操作企業の企業種別
     */
    public String getOperationCorpType() {
        return operationCorpType;
    }

    /**
     *
     * 操作企業の企業種別 設定
     * 
     * @param operationCorpType 操作企業の企業種別
     */
    public void setOperationCorpType(String operationCorpType) {
        this.operationCorpType = operationCorpType;
    }

    /**
     *
     * 操作企業の企業担当者の権限種別 取得
     * 
     * @return 操作企業の企業担当者の権限種別
     */
    public String getOperationAuthorityType() {
        return operationAuthorityType;
    }

    /**
     *
     * 操作企業の企業担当者の権限種別 設定
     * 
     * @param operationAuthorityType 操作企業の企業担当者の権限種別
     */
    public void setOperationAuthorityType(String operationAuthorityType) {
        this.operationAuthorityType = operationAuthorityType;
    }

    /**
     *
     * 操作企業の企業担当者の権限コード 取得
     * 
     * @return 操作企業の企業担当者の権限コード
     */
    public String getOperationAuthorityCd() {
        return operationAuthorityCd;
    }

    /**
     *
     * 操作企業の企業担当者の権限コード 設定
     * 
     * @param operationAuthorityCd 操作企業の企業担当者の権限コード
     */
    public void setOperationAuthorityCd(String operationAuthorityCd) {
        this.operationAuthorityCd = operationAuthorityCd;
    }

    /**
     *
     * 操作企業の企業担当者の権限有効フラグ 取得
     * 
     * @return 操作企業の企業担当者の権限有効フラグ
     */
    public Integer getOperationAuthorityFlg() {
        return operationAuthorityFlg;
    }

    /**
     *
     * 操作企業の企業担当者の権限有効フラグ 設定
     * 
     * @param operationAuthorityFlg 操作企業の企業担当者の権限有効フラグ
     */
    public void setOperationAuthorityFlg(Integer operationAuthorityFlg) {
        this.operationAuthorityFlg = operationAuthorityFlg;
    }

}
