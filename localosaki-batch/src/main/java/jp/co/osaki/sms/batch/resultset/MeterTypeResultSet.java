package jp.co.osaki.sms.batch.resultset;

public class MeterTypeResultSet {

    private Long meterType;

    private String meterTypeName;


    public MeterTypeResultSet(Long meterType, String meterTypeName) {
        this.meterType = meterType;
        this.meterTypeName = meterTypeName;
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

}
