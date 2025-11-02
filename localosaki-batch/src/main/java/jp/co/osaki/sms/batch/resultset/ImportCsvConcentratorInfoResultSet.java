package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

public class ImportCsvConcentratorInfoResultSet {

    private Long meterMngId;
    private BigDecimal multi;

    public ImportCsvConcentratorInfoResultSet() {
    }

    public ImportCsvConcentratorInfoResultSet(Long meterMngId, BigDecimal multi) {
        super();
        this.meterMngId = meterMngId;
        this.multi = multi;
    }
    public Long getMeterMngId() {
        return meterMngId;
    }
    public void setMeterMngId(Long meterMngId) {
        this.meterMngId = meterMngId;
    }
    public BigDecimal getMulti() {
        return multi;
    }
    public void setMulti(BigDecimal multi) {
        this.multi = multi;
    }


}
