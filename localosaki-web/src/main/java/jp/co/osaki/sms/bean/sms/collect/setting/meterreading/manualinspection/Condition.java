package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.manualinspection;

import java.util.Date;

import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 * 任意検針 検索条件
 *
 * @author yonezawa.a
 */
@Named("smsCollectSettingMeterreadingManualInspectionCondition")
public class Condition extends BaseSearchCondition {

    // ユーザコード選択
    private boolean userCdSelectEnable;
    private String userCd;

    // 種別選択
    private boolean meterTypeNameSelectEnable;
    private String meterTypeName;

    // 予約検針日時
    private boolean reservationDtSelectEnable;
    private Date reserveInspYMD;
    private String reserveInspH;
    private String reserveInspM;

    // 接続詞表示部分 検索条件の接続詞
    private String searchSubjectConjunction;
    private boolean searchSubjectConjunctionEnable;

    public Condition() {
        // 接続詞文言
        setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_EQUAL);

        // 削除ボタン利用可否
        setDeleteButtonEnable(true);
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public boolean isUserCdSelectEnable() {
        return userCdSelectEnable;
    }

    public void setUserCdSelectEnable(boolean userCdSelectEnable) {
        this.userCdSelectEnable = userCdSelectEnable;
    }

    public String getUserCd() {
        return userCd;
    }

    public void setUserCd(String userCd) {
        this.userCd = userCd;
    }

    public boolean isMeterTypeNameSelectEnable() {
        return meterTypeNameSelectEnable;
    }

    public void setMeterTypeNameSelectEnable(boolean meterTypeNameSelectEnable) {
        this.meterTypeNameSelectEnable = meterTypeNameSelectEnable;
    }

    public String getMeterTypeName() {
        return meterTypeName;
    }

    public void setMeterTypeName(String meterTypeName) {
        this.meterTypeName = meterTypeName;
    }

    public String getSearchSubjectConjunction() {
        return searchSubjectConjunction;
    }

    public void setSearchSubjectConjunction(String searchSubjectConjunction) {
        this.searchSubjectConjunction = searchSubjectConjunction;
    }

    public boolean isSearchSubjectConjunctionEnable() {
        return searchSubjectConjunctionEnable;
    }

    public void setSearchSubjectConjunctionEnable(boolean searchSubjectConjunctionEnable) {
        this.searchSubjectConjunctionEnable = searchSubjectConjunctionEnable;
    }

    public boolean isReservationDtSelectEnable() {
        return reservationDtSelectEnable;
    }

    public void setReservationDtSelectEnable(boolean reservationDtSelectEnable) {
        this.reservationDtSelectEnable = reservationDtSelectEnable;
    }

    public Date getReserveInspYMD() {
        return reserveInspYMD;
    }

    public void setReserveInspYMD(Date reserveInspYMD) {
        this.reserveInspYMD = reserveInspYMD;
    }

    public String getReserveInspH() {
        return reserveInspH;
    }

    public void setReserveInspH(String reserveInspH) {
        this.reserveInspH = reserveInspH;
    }

    public String getReserveInspM() {
        return reserveInspM;
    }

    public void setReserveInspM(String reserveInspM) {
        this.reserveInspM = reserveInspM;
    }

    public String getReserveInspYMDFmt() {
        if (this.reserveInspYMD == null) {
            return "----年--月--日";
        }
        return DateUtility.changeDateFormat(this.reserveInspYMD, DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER);
    }

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }
}
