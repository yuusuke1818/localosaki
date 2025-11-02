package jp.co.osaki.sms.bean.sms.server.setting.buildingdevice;

import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 * 建物装置設定 建物検索条件
 *
 * @author yoneda_y
 */
@Named("smsServerSettingBuildingDeviceCondition")
public class Condition extends BaseSearchCondition {

    private boolean prefectureSelectEnable;
    private String prefectureCd;

    private boolean nyukyoTypeSelectEnable;
    private String nyukyoTypeCd;

    private boolean buildingStatusSelectEnable;
    private String buildingStatusCd;

    //検索条件の接続詞
    private String searchSubjectConjunction;
    private boolean searchSubjectConjunctionEnable;

    public Condition() {
        // 接続詞文言
        setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_EQUAL);
        // 接続詞
        setSearchSubjectConjunctionEnable(true);

        // 削除ボタン利用可否
        setDeleteButtonEnable(true);
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public boolean getPrefectureSelectEnable() {
        return prefectureSelectEnable;
    }

    public void setPrefectureSelectEnable(boolean prefectureSelectEnable) {
        this.prefectureSelectEnable = prefectureSelectEnable;
    }

    public String getPrefectureCd() {
        return prefectureCd;
    }

    public void setPrefectureCd(String prefectureCd) {
        this.prefectureCd = prefectureCd;
    }

    public boolean getNyukyoTypeSelectEnable() {
        return nyukyoTypeSelectEnable;
    }

    public void setNyukyoTypeSelectEnable(boolean nyukyoTypeSelectEnable) {
        this.nyukyoTypeSelectEnable = nyukyoTypeSelectEnable;
    }

    public String getNyukyoTypeCd() {
        return nyukyoTypeCd;
    }

    public void setNyukyoTypeCd(String nyukyoTypeCd) {
        this.nyukyoTypeCd = nyukyoTypeCd;
    }

    public boolean getBuildingStatusSelectEnable() {
        return buildingStatusSelectEnable;
    }

    public void setBuildingStatusSelectEnable(boolean buildingStatusSelectEnable) {
        this.buildingStatusSelectEnable = buildingStatusSelectEnable;
    }

    public String getBuildingStatusCd() {
        return buildingStatusCd;
    }

    public void setBuildingStatusCd(String buildingStatusCd) {
        this.buildingStatusCd = buildingStatusCd;
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

    public boolean isPrefectureSelectEnable() {
        return prefectureSelectEnable;
    }

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }
}