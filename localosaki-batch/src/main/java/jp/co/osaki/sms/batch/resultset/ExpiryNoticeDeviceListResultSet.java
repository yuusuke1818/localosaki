package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * 検満通知用装置情報 ResultSetクラス
 *
 * @author sagi_h
 *
 */
public class ExpiryNoticeDeviceListResultSet {
    /** 装置ID */
    private String devId;

    /** 検満通知月 */
    private BigDecimal examNoticeMonth;

    /** 装置名称 */
    private String name;

    /**
     * コンストラクタ
     *
     * @param devId
     * @param examNoticeMonth
     * @param name
     */
    public ExpiryNoticeDeviceListResultSet(String devId, BigDecimal examNoticeMonth, String name) {
        this.devId = devId;
        this.examNoticeMonth = examNoticeMonth;
        this.name = name;
    }

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @return examNoticeMonth
     */
    public BigDecimal getExamNoticeMonth() {
        return examNoticeMonth;
    }

    /**
     * @param examNoticeMonth セットする examNoticeMonth
     */
    public void setExamNoticeMonth(BigDecimal examNoticeMonth) {
        this.examNoticeMonth = examNoticeMonth;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }
}
