package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * 操作企業の担当者権限のフィルターデータ用 取得結果
 *
 * @author take_suzuki
 */
public class CorpPersonAuthAdminDataResultSet extends BaseResultSet {

    /**
     * 担当者権限コード
     */
    private String authorityCd;

    /**
     *
     * コンストラクタ
     *
     * @param authorityCd 担当者権限コード
     */
    public CorpPersonAuthAdminDataResultSet(String authorityCd) {
        this.authorityCd = authorityCd;
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
