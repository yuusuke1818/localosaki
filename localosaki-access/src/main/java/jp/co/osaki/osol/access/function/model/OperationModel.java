package jp.co.osaki.osol.access.function.model;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * 操作企業情報モデルクラス
 *
 * @author take_suzuki
 */
public final class OperationModel {

    /**
     * キー
     */
    private String key;

    /**
     * 名
     */
    private String name;

    /*
     *   企業種別
     */
    private String corpType;

    /*
     *   権限種別
     */
    private String authorityType;

    /*
     *   担当者権限コード
     */
    private String authorityCd;

    /**
     * コンストラクタ
     */
    public OperationModel() {
        this.key = BaseConstants.STR_EMPTY;
        this.name = BaseConstants.STR_EMPTY;
        this.corpType = BaseConstants.STR_EMPTY;
        this.authorityType = BaseConstants.STR_EMPTY;
        this.authorityCd = BaseConstants.STR_EMPTY;
    }

    /**
     *
     * キー取得
     * 
     * @return キー
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * キー設定
     * 
     * @param key キー
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * 名取得
     * 
     * @return 名
     */
    public String getName() {
        return name;
    }

    /**
     *
     * 名設定
     * 
     * @param name 名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * 企業種別取得
     * 
     * @return 企業種別
     */
    public String getCorpType() {
        return corpType;
    }

    /**
     *
     * 企業種別設定
     * 
     * @param corpType 企業種別
     */
    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    /**
     *
     * 権限種別取得
     * 
     * @return 権限種別
     */
    public String getAuthorityType() {
        return authorityType;
    }

    /**
     *
     * 権限種別設定
     * 
     * @param authorityType 権限種別
     */
    public void setAuthorityType(String authorityType) {
        this.authorityType = authorityType;
    }

    /**
     *
     * 担当者権限コード 取得
     * 
     * @return 担当者権限コード
     */
    public String getAuthorityCd() {
        return authorityCd;
    }

    /**
     *
     * 担当者権限コード 設定
     * 
     * @param authorityCd 担当者権限コード
     */
    public void setAuthorityCd(String authorityCd) {
        this.authorityCd = authorityCd;
    }

}
