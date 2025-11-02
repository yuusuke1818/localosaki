package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * アズビルメーター実績データ ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class AzbilMeterDataResultSet {
    /** 指針値データ */
	private BigDecimal dmvKwh;

    /** 30分使用電力量 */
	private BigDecimal kwh30;


    /**
     * コンストラクタ
     *
     * @param dmvKwh
     * @param kwh30
     */
    public AzbilMeterDataResultSet(BigDecimal dmvKwh, BigDecimal kwh30) {
        this.dmvKwh = dmvKwh;
        this.kwh30 = kwh30;
    }

    /**
     * @return dmvKwh
     */
	public BigDecimal getDmvKwh() {
		return this.dmvKwh;
	}

    /**
     * @param dmvKwh セットする dmvKwh
     */
	public void setDmvKwh(BigDecimal dmvKwh) {
		this.dmvKwh = dmvKwh;
	}

    /**
     * @return kwh30
     */
	public BigDecimal getKwh30() {
		return this.kwh30;
	}

    /**
     * @param kwh30 セットする kwh30
     */
	public void setKwh30(BigDecimal kwh30) {
		this.kwh30 = kwh30;
	}

}
