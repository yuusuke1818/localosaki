package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * 企業データフィルター用 取得結果
 *
 * @author take_suzuki
 */
public class CorpDataFilterResultSet extends BaseResultSet {

    /**
     * 企業ID
     */
    private String corpId;

    /**
     *
     * コンストラクタ
     *
     * @param corpId 企業ID
     */
    public CorpDataFilterResultSet(String corpId) {
        this.corpId = corpId;
    }

    /**
     *
     * 企業ID 取得
     * 
     * @return 企業ID
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     *
     * 企業ID 設定
     * 
     * @param corpId 企業ID
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

}
