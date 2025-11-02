package jp.co.osaki.sms.bean.sms.server.setting.collect;

import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseSearchCondition;

/**
 * 装置 検索条件
 *
 * @author yoneda_y
 */
@Named("smsServerSettingCollectCondition")
public class Condition extends BaseSearchCondition {

    private boolean devIdSelectEnable;
    private String devId;

    private boolean delFlgSelectEnable;
    private String delFlg;

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

    public boolean isDevIdSelectEnable() {
        return devIdSelectEnable;
    }

    public void setDevIdSelectEnable(boolean devIdSelectEnable) {
        this.devIdSelectEnable = devIdSelectEnable;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public boolean isDelFlgSelectEnable() {
        return delFlgSelectEnable;
    }

    public void setDelFlgSelectEnable(boolean delFlgSelectEnable) {
        this.delFlgSelectEnable = delFlgSelectEnable;
    }

    public String getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
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

    public void setMutchingTypeCd(String mutchingTypeCd) {
        super.setMutchingTypeCd(mutchingTypeCd);

        if (OsolConstants.SEARCH_CONDITION_MUTCHING_TYPE_LIKE.equals(mutchingTypeCd)) {
            setSearchSubjectConjunction(SmsConstants.CONDITION_CONJUNCTION_LIKE);
        }
    }
}
