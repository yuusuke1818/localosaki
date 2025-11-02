package jp.co.osaki.sms.bean.menu;

import jp.co.osaki.osol.OsolConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

public class Condition extends BaseSearchCondition{

    //文字列指定以外の条件入力
    private boolean prefectureSelectEnable;
    private String prefectureCd;
    private boolean corpTypeSelectEnable;
    private String[] corpType = {"", "", "3"};

    public Condition() {
    }

    @Override
    public boolean isDefaultCondition() {
        return OsolConstants.DEFAULT_SELECT_BOX_VALUE.equals(getSelectConditionCd());
    }

    public String getPrefectureCd() {
        return prefectureCd;
    }

    public void setPrefectureCd(String prefectureCd) {
        this.prefectureCd = prefectureCd;
    }

    public boolean isPrefectureSelectEnable() {
        return prefectureSelectEnable;
    }

    public void setPrefectureSelectEnable(boolean prefectureSelectEnable) {
        this.prefectureSelectEnable = prefectureSelectEnable;
    }

    public String[] getCorpType() {
        return corpType;
    }

    public void setCorpType(String[] corpType) {
        this.corpType = corpType;
    }

    public boolean isCorpTypeSelectEnable() {
        return corpTypeSelectEnable;
    }

    public void setCorpTypeSelectEnable(boolean corpTypeSelectEnable) {
        this.corpTypeSelectEnable = corpTypeSelectEnable;
    }
}
