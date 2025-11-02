package jp.co.osaki.osol.access.filter.resultset;

import jp.skygroup.enl.webap.base.BaseResultSet;

/**
 * メーター（SMS）データフィルター用 取得結果
 * @author nishida.t
 *
 */
public class MeterDataFilterResultSet extends BaseResultSet {

    /**
     * メーター管理番号
     */
    private Long meterMngId;

    /**
     * 装置ID
     */
    private String devId;

    /**
     * コンストラクタ
     *
     * @param meterMngId メーター管理番号
     * @param devId 装置ID
     */
    public MeterDataFilterResultSet(Long meterMngId, String devId) {
        this.meterMngId = meterMngId;
        this.devId = devId;
    }

    /**
     * メーター管理番号取得
     * @return
     */
    public Long getMeterMngId() {
        return meterMngId;
    }

    /**
     * メーター管理番号設定
     * @param meterMngId
     */
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    /**
     * 装置ID取得
     * @return
     */
    public String getDevId() {
        return devId;
    }

    /**
     * 装置ID設定
     * @param devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

}
