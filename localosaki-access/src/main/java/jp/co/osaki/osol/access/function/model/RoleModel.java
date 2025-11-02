package jp.co.osaki.osol.access.function.model;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * ロールモデルクラス
 *
 * @author take_suzuki
 */
public final class RoleModel {

    /**
     * キー
     */
    private String key;

    /**
     * 名
     */
    private String name;

    /**
     * ログイン担当者情報
     */
    private String login;

    /**
     * 操作企業情報
     */
    private String operation;

    /**
     * コンストラクタ
     */
    public RoleModel() {
        this.key = BaseConstants.STR_EMPTY;
        this.name = BaseConstants.STR_EMPTY;
        this.login = BaseConstants.STR_EMPTY;
        this.operation = BaseConstants.STR_EMPTY;
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
     * ロール名取得
     *
     * @return ロール名
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
     * ログイン担当者情報取得
     *
     * @return ログイン担当者情報
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * ログイン担当者情報設定
     *
     * @param login ログイン担当者情報
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * 操作企業情報取得
     *
     * @return 操作企業情報
     */
    public String getOperation() {
        return operation;
    }

    /**
     *
     * 操作企業情報設定
     *
     * @param operation 操作企業情報
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

}
