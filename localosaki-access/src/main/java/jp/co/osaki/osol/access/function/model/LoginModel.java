package jp.co.osaki.osol.access.function.model;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * ログイン情報モデルクラス
 *
 * @author take_suzuki
 */
public final class LoginModel {

    /**
     * キー
     */
    private String key;

    /**
     * 名
     */
    private String name;

    /**
     * ログイン担当者の企業種別
     */
    private String corpType;

    /**
     * ログイン担当者の担当者種別
     */
    private String personType;

    /**
     * コンストラクタ
     */
    public LoginModel() {
        this.key = BaseConstants.STR_EMPTY;
        this.name = BaseConstants.STR_EMPTY;
        this.corpType = BaseConstants.STR_EMPTY;
        this.personType = BaseConstants.STR_EMPTY;
    }

    /**
     *
     * キー 取得
     * 
     * @return キー
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * キー 設定
     * 
     * @param key キー
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * 名称 取得
     * 
     * @return 名称
     */
    public String getName() {
        return name;
    }

    /**
     *
     * 名称 設定
     * 
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ログイン担当者の企業種別取得
     *
     * @return ログイン担当者の企業種別
     */
    public String getCorpType() {
        return corpType;
    }

    /**
     *
     * ログイン担当者の企業種別設定
     *
     * @param corpType ログイン担当者の企業種別
     */
    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    /**
     *
     * ログイン担当者の担当者種別取得
     *
     * @return ログイン担当者の担当者種別
     */
    public String getPersonType() {
        return personType;
    }

    /**
     *
     * ログイン担当者の担当者種別設定
     *
     * @param personType ログイン担当者の担当者種別
     */
    public void setPersonType(String personType) {
        this.personType = personType;
    }

}
