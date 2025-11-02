package jp.co.osaki.sms.bean.building.info;

import jp.co.osaki.sms.bean.common.DatabaseParameter;

/**
 * 建物情報検索パラメータクラス
 *
 * @author ozaki.y
 */
public class BuildingInfoSearchParameter extends DatabaseParameter<BuildingInfoSearchCondition> {

    /** 大崎ユーザフラグ */
    private boolean isOsakiUser;

    /** 管理者ユーザフラグ */
    private boolean isAdminUser;

    /** 担当企業検索条件追加フラグ */
    private boolean personCorpFlg = true;

    /** 担当建物検索条件追加フラグ */
    private boolean personBuildingFlg = true;

    public boolean isOsakiUser() {
        return isOsakiUser;
    }

    public void setOsakiUser(boolean isOsakiUser) {
        this.isOsakiUser = isOsakiUser;
    }

    public boolean isAdminUser() {
        return isAdminUser;
    }

    public void setAdminUser(boolean isAdminUser) {
        this.isAdminUser = isAdminUser;
    }

    public boolean isPersonCorpFlg() {
        return personCorpFlg;
    }

    public void setPersonCorpFlg(boolean personCorpFlg) {
        this.personCorpFlg = personCorpFlg;
    }

    public boolean isPersonBuildingFlg() {
        return personBuildingFlg;
    }

    public void setPersonBuildingFlg(boolean personBuildingFlg) {
        this.personBuildingFlg = personBuildingFlg;
    }
}
