package jp.co.osaki.sms.bean.sms.collect.setting.meterreading;

/**
 * メーター種別従量値 検索結果
 * @author kobayashi.sho
 */
public class MeterRange {

    /** 従量値(Key) ※null:新規(DB未登録). */
    private Long rangeValue;

    /** メーター種別(Key). */
    private Long meterType;

    /** 企業ID(Key). */
    private String corpId;

    /** 建物ID(Key). */
    private Long buildingId;

    /** メニュー番号(Key)(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2). */
    private Long menuNo;

    /** 排他制御用カラム. */
    private Integer version;

    /** 従量値(編集用). */
    private String rangeValueEdit;

    /**
     * @param rangeValue 従量値(Key) ※null:新規(DB未登録)
     * @param meterType メーター種別(Key)
     * @param corpId 企業ID(Key)
     * @param buildingId 建物ID(Key)
     * @param menuNo メニュー番号(Key)(1:従量電灯A  2:従量電灯B  3:ファミリータイムプラン2)
     * @param version 排他制御用カラム
     * @param rangeValueEdit 従量値(編集用)
     */
    public MeterRange(Long rangeValue, Long meterType, String corpId, Long buildingId, Long menuNo, Integer version, String rangeValueEdit) {
        this.rangeValue = rangeValue;
        this.meterType = meterType;
        this.corpId = corpId;
        this.buildingId = buildingId;
        this.menuNo = menuNo;
        this.version = version;
        this.rangeValueEdit = zeroSuppression(rangeValueEdit); // 先頭が0の場合は0を削除する(重複チェックで 01 と 1 を同じ値と判断させるため)
    }

    /**
     * 先頭が0の場合は0を削除する
     * @return ゼロサプレスした値
     */
    private String zeroSuppression(String val) {
        return (val == null ? null : val.replaceAll("^0+([1-9])", "$1"));
    }

    public Long getMeterType() {
        return meterType;
    }

    public void setMeterType(Long meterType) {
        this.meterType = meterType;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getMenuNo() {
        return menuNo;
    }

    public void setMenuNo(Long menuNo) {
        this.menuNo = menuNo;
    }

    public Long getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(Long rangeValue) {
        this.rangeValue = rangeValue;
    }

    public String getRangeValueEdit() {
        return rangeValueEdit;
    }

    public void setRangeValueEdit(String rangeValueEdit) {
        this.rangeValueEdit = zeroSuppression(rangeValueEdit); // 先頭が0の場合は0を削除する(重複チェックで 01 と 1 を同じ値と判断させるため)
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
