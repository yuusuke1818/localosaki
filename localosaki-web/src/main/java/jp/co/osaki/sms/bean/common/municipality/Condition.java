package jp.co.osaki.sms.bean.common.municipality;

import jp.co.osaki.osol.OsolConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

public class Condition extends BaseSearchCondition {

    private boolean prefectureSelectEnable;
    private String prefectureCd;

    public Condition() {
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public boolean isPrefectureSelectEnable() {
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

}
