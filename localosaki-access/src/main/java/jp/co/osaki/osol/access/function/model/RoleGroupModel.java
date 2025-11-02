package jp.co.osaki.osol.access.function.model;

import java.util.ArrayList;
import java.util.List;

import jp.skygroup.enl.webap.base.BaseConstants;

/**
 *
 * ロールグループモデルクラス
 *
 * @author take_suzuki
 */
public final class RoleGroupModel {

    /**
     * キー
     */
    private String key;

    /**
     * 名
     */
    private String name;

    /**
     * ロールリスト
     */
    private List<String> roleList;

    /**
     * コンストラクタ
     */
    public RoleGroupModel() {
        this.key = BaseConstants.STR_EMPTY;
        this.name = BaseConstants.STR_EMPTY;
        this.roleList = new ArrayList<>();
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
     * @param Key キー
     */
    public void setKey(String Key) {
        this.key = Key;
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
     * ロールリスト取得
     * 
     * @return ロールリスト
     */
    public List<String> getRoleList() {
        return roleList;
    }

    /**
     *
     * ロールリスト設定
     * 
     * @param roleList ロールリスト
     */
    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

}
