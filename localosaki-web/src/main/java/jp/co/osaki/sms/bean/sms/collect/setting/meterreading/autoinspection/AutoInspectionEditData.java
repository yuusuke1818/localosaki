package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.autoinspection;

import java.math.BigDecimal;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.BooleanUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.resultset.BuildingAutoInspResultSet;

/**
 * 自動検針編集用データ.
 *
 * @author ozaki.y
 */
public class AutoInspectionEditData {

    /** 値未取得時表示文字列. */
    private static final String NO_VALUE_STR = "-";

    /** 接頭辞 月毎の自動検針実施有無フラグ. */
    private static final String PREFIX_INSPECTION_MONTH_FLG = "inspectionMonth";

    /** 規定値 月毎の自動検針実施有無. */
    private static final String DEFAULT_VALUE_INSPECTION_MONTH = "000000000000";

    /** メーター種別. */
    private Long meterType;

    /** メーター種別名称. */
    private String meterTypeName;

    /** 月毎の自動検針実施有無. */
    private String inspectionMonth;

    /** 自動検針日. */
    private String inspectionDay;

    /** 自動検針時. */
    private BigDecimal inspectionHour;

    /** 排他制御用カラム. */
    private Integer version;

    /** 自動検針月(表示用). */
    private String inspectionMonthDisp;

    /** 月毎の自動検針実施有無 1月. */
    private boolean inspectionMonth1;

    /** 月毎の自動検針実施有無 2月. */
    private boolean inspectionMonth2;

    /** 月毎の自動検針実施有無 3月. */
    private boolean inspectionMonth3;

    /** 月毎の自動検針実施有無 4月. */
    private boolean inspectionMonth4;

    /** 月毎の自動検針実施有無 5月. */
    private boolean inspectionMonth5;

    /** 月毎の自動検針実施有無 6月. */
    private boolean inspectionMonth6;

    /** 月毎の自動検針実施有無 7月. */
    private boolean inspectionMonth7;

    /** 月毎の自動検針実施有無 8月. */
    private boolean inspectionMonth8;

    /** 月毎の自動検針実施有無 9月. */
    private boolean inspectionMonth9;

    /** 月毎の自動検針実施有無 10月. */
    private boolean inspectionMonth10;

    /** 月毎の自動検針実施有無 11月. */
    private boolean inspectionMonth11;

    /** 月毎の自動検針実施有無 12月. */
    private boolean inspectionMonth12;

    /** 装置ID **/
    private String devId;

    /**
     * コンストラクタ.
     *
     * @param autoInspData 自動検針データ
     */
    public AutoInspectionEditData(BuildingAutoInspResultSet autoInspData) {
        setMeterType(autoInspData.getMeterType());
        setMeterTypeName(autoInspData.getMeterTypeName());
        setInspectionDay(autoInspData.getAutoInspDay());
        setInspectionHour(autoInspData.getAutoInspHour());
    }

    /**
     * コンストラクタ.
     *
     * @param targetData 自動検針データ
     * @throws Exception 例外
     */
    public AutoInspectionEditData(ListSmsAutoInspectionResultData targetData) throws Exception {
        this(targetData.getMeterType(), targetData.getMeterTypeName(), targetData.getInspectionMonth(),
                targetData.getInspectionDay(), targetData.getInspectionHour(), targetData.getVersion());
    }

    /**
     * コンストラクタ.
     *
     * @param meterType メーター種別
     * @throws Exception 例外
     */
    public AutoInspectionEditData(Long meterType) throws Exception {
        this(meterType, null, null, null, null, null);
    }

    /**
     * コンストラクタ.
     *
     * @param meterType メーター種別
     * @param meterTypeName メーター種別名
     * @param inspectionMonth 月毎の自動検針実施有無
     * @param inspectionDay 自動検針日
     * @param inspectionHour 自動検針時
     * @param version 排他制御用カラム
     * @throws Exception 例外
     */
    public AutoInspectionEditData(Long meterType, String meterTypeName, String inspectionMonth, String inspectionDay,
            BigDecimal inspectionHour, Integer version) throws Exception {

        setMeterType(meterType);
        setMeterTypeName(meterTypeName);
        setInspectionMonth(inspectionMonth);
        setInspectionDay(inspectionDay);
        setInspectionHour(inspectionHour);
        setVersion(version);
    }


    public AutoInspectionEditData(AutoInspectionEditData data) throws Exception {
        super();
        this.meterType = data.getMeterType();
        this.meterTypeName = data.getMeterTypeName();
        this.inspectionMonth = data.getInspectionMonth();
        this.inspectionDay = data.getInspectionDay();
        this.inspectionHour = data.getInspectionHour();
        this.devId = data.getDevId();
        this.inspectionMonth1 = data.isInspectionMonth1();
        this.inspectionMonth2 = data.isInspectionMonth2();
        this.inspectionMonth3 = data.isInspectionMonth3();
        this.inspectionMonth4 = data.isInspectionMonth4();
        this.inspectionMonth5 = data.isInspectionMonth5();
        this.inspectionMonth6 = data.isInspectionMonth6();
        this.inspectionMonth7 = data.isInspectionMonth7();
        this.inspectionMonth8 = data.isInspectionMonth8();
        this.inspectionMonth9 = data.isInspectionMonth9();
        this.inspectionMonth10 = data.isInspectionMonth10();
        this.inspectionMonth11 = data.isInspectionMonth11();
        this.inspectionMonth12 = data.isInspectionMonth12();
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

    public String getInspectionMonth() throws Exception {
        updateInspectionMonth();
        return inspectionMonth;
    }

    public void setInspectionMonth(String inspectionMonth) throws Exception {
        String targetInspectionMonth = inspectionMonth;
        if (CheckUtility.isNullOrEmpty(inspectionMonth)) {
            targetInspectionMonth = DEFAULT_VALUE_INSPECTION_MONTH;
        }

        this.inspectionMonth = targetInspectionMonth;

        updateInspectionMonthFlg(targetInspectionMonth);
        setInspectionMonthDisp(targetInspectionMonth);
    }

    public String getInspectionDay() {
        return inspectionDay;
    }

    public void setInspectionDay(String inspectionDay) {
        this.inspectionDay = inspectionDay;
    }

    public BigDecimal getInspectionHour() {
        return inspectionHour;
    }

    public void setInspectionHour(BigDecimal inspectionHour) {
        this.inspectionHour = inspectionHour;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getInspectionMonthDisp() {
        return inspectionMonthDisp;
    }

    public void setInspectionMonthDisp(String inspectionMonth) {
        String inspectionMonthDisp = "";

        for (int i = 1; i <= inspectionMonth.length(); i++) {
            if (String.valueOf(OsolConstants.FLG_ON).equals(inspectionMonth.substring((i - 1), i))) {
                if (!CheckUtility.isNullOrEmpty(inspectionMonthDisp)) {
                    inspectionMonthDisp += ", ";
                }

                inspectionMonthDisp += i + "月";
            }
        }

        this.inspectionMonthDisp = inspectionMonthDisp;
    }

    public void setInspectionMonthDispMail(String inspectionMonthDispMail) {
        this.inspectionMonthDisp = inspectionMonthDispMail;
    }

    public boolean isInspectionMonth1() {
        return inspectionMonth1;
    }

    public void setInspectionMonth1(boolean inspectionMonth1) {
        this.inspectionMonth1 = inspectionMonth1;
    }

    public boolean isInspectionMonth2() {
        return inspectionMonth2;
    }

    public void setInspectionMonth2(boolean inspectionMonth2) {
        this.inspectionMonth2 = inspectionMonth2;
    }

    public boolean isInspectionMonth3() {
        return inspectionMonth3;
    }

    public void setInspectionMonth3(boolean inspectionMonth3) {
        this.inspectionMonth3 = inspectionMonth3;
    }

    public boolean isInspectionMonth4() {
        return inspectionMonth4;
    }

    public void setInspectionMonth4(boolean inspectionMonth4) {
        this.inspectionMonth4 = inspectionMonth4;
    }

    public boolean isInspectionMonth5() {
        return inspectionMonth5;
    }

    public void setInspectionMonth5(boolean inspectionMonth5) {
        this.inspectionMonth5 = inspectionMonth5;
    }

    public boolean isInspectionMonth6() {
        return inspectionMonth6;
    }

    public void setInspectionMonth6(boolean inspectionMonth6) {
        this.inspectionMonth6 = inspectionMonth6;
    }

    public boolean isInspectionMonth7() {
        return inspectionMonth7;
    }

    public void setInspectionMonth7(boolean inspectionMonth7) {
        this.inspectionMonth7 = inspectionMonth7;
    }

    public boolean isInspectionMonth8() {
        return inspectionMonth8;
    }

    public void setInspectionMonth8(boolean inspectionMonth8) {
        this.inspectionMonth8 = inspectionMonth8;
    }

    public boolean isInspectionMonth9() {
        return inspectionMonth9;
    }

    public void setInspectionMonth9(boolean inspectionMonth9) {
        this.inspectionMonth9 = inspectionMonth9;
    }

    public boolean isInspectionMonth10() {
        return inspectionMonth10;
    }

    public void setInspectionMonth10(boolean inspectionMonth10) {
        this.inspectionMonth10 = inspectionMonth10;
    }

    public boolean isInspectionMonth11() {
        return inspectionMonth11;
    }

    public void setInspectionMonth11(boolean inspectionMonth11) {
        this.inspectionMonth11 = inspectionMonth11;
    }

    public boolean isInspectionMonth12() {
        return inspectionMonth12;
    }

    public void setInspectionMonth12(boolean inspectionMonth12) {
        this.inspectionMonth12 = inspectionMonth12;
    }

    /**
     * 月毎の自動検針実施有無フラグを更新.
     *
     * @param inspectionMonth 月毎の自動検針実施有無
     * @throws Exception 例外
     */
    private void updateInspectionMonthFlg(String inspectionMonth) throws Exception {
        for (int i = 1; i <= inspectionMonth.length(); i++) {
            PropertyUtils.setProperty(this, PREFIX_INSPECTION_MONTH_FLG + i,
                    String.valueOf(OsolConstants.FLG_ON).equals(inspectionMonth.substring((i - 1), i)));
        }
    }

    /**
     * 月毎の自動検針実施有無を更新.
     *
     * @throws Exception 例外
     */
    private void updateInspectionMonth() throws Exception {
        StringBuilder inspectionMonthBuf = new StringBuilder();
        for (int i = 1; i <= 12; i++) {
            boolean inspectionMonthFlg = ((Boolean) PropertyUtils.getProperty(this, PREFIX_INSPECTION_MONTH_FLG + i));
            inspectionMonthBuf.append(BooleanUtils.toString(inspectionMonthFlg, String.valueOf(OsolConstants.FLG_ON),
                    String.valueOf(OsolConstants.FLG_OFF)));
        }

        setInspectionMonth(inspectionMonthBuf.toString());
    }

    /**
     * 自動検針日時(表示用).
     */
    public String getAutoInspDateTimeDisp() {
        String autoInspDay = getInspectionDay();
        if (CheckUtility.isNullOrEmpty(autoInspDay) || SmsConstants.AUTO_INSP_DAY_NONE.equals(autoInspDay)) {
            return NO_VALUE_STR;
        }

        String autoInspDateTimeDisp = autoInspDay + "日 ";

        BigDecimal autoInspHour = getInspectionHour();
        if (autoInspHour != null) {
            autoInspDateTimeDisp += autoInspHour.intValue() + "時";
        }

        return autoInspDateTimeDisp;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }
}
