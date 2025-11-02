package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 *
 * 企業種別/担当者種別 判定データ用 取得結果
 *
 * @author take_suzuki
 */
public class PersonTypeResultSet extends BaseResultSet {

    /**
     * 企業種別
     */
    private String corpType;

    /**
     * 担当者種別
     */
    private String personType;

    /**
     *
     * コンストラクタ
     *
     * @param corpType 企業種別
     * @param personType 担当者種別
     */
    public PersonTypeResultSet(String corpType, String personType) {
        this.corpType = corpType;
        this.personType = personType;
    }

    /**
     *
     * 企業種別 取得
     * 
     * @return 企業種別
     */
    public String getCorpType() {
        return corpType;
    }

    /**
     *
     * 企業種別 設定
     * 
     * @param corpType 企業種別
     */
    public void setCorpType(String corpType) {
        this.corpType = corpType;
    }

    /**
     *
     * 担当者種別 取得
     * 
     * @return 担当者種別
     */
    public String getPersonType() {
        return personType;
    }

    /**
     *
     * 担当者種別 設定
     * 
     * @param personType 担当者種別
     */
    public void setPersonType(String personType) {
        this.personType = personType;
    }

}
