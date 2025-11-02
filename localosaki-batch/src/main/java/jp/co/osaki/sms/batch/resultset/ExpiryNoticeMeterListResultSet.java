package jp.co.osaki.sms.batch.resultset;

/**
 * 検満通知用メーター登録用 ResultSetクラス
 *
 * @author sagi_h
 *
 */
public class ExpiryNoticeMeterListResultSet {
    /** メーターID */
    private String meterId;

    /** メーター管理番号 */
    private Long meterMngId;

    /** 検満年月 */
    private String examEndYm;

    /**
     * コンストラクタ
     *
     * @param meterId
     * @param meterMngId
     * @param examEndYm
     */
    public ExpiryNoticeMeterListResultSet(String meterId, Long meterMngId, String examEndYm) {
        this.meterId = meterId;
        this.meterMngId = meterMngId;
        this.examEndYm = examEndYm;
    }

    /**
     * @return meterId
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * @param meterId セットする meterId
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    /**
     * @return meterMngId
     */
    public Long getMeterMngId() {
        return meterMngId;
    }

    /**
     * @param meterMngId セットする meterMngId
     */
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }

    /**
     * @return examEndYm
     */
    public String getExamEndYm() {
        return examEndYm;
    }

    /**
     * @param examEndYm セットする examEndYm
     */
    public void setExamEndYm(String examEndYm) {
        this.examEndYm = examEndYm;
    }

}