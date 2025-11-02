package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 * 装置情報 データフィルター用 取得結果
 * @author nishida.t
 *
 */
public class DevPrmDataFilterResultSet extends BaseResultSet {

    /**
     * 装置ID
     */
    private String devId;

    /**
     * コンストラクタ
     *
     * @param devId 装置ID
     */
    public DevPrmDataFilterResultSet(String devId) {
        this.devId = devId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

}
