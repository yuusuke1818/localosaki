package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import jp.co.osaki.osol.mng.constants.SmControlConstants;


/**
*
* FOMA電波状態(取得) Param クラス
*
* @author t_sakamoto
*
*/
public class A200033Param extends BaseParam {

    /**
     * 取得日時(設定変更日時)
     */
    private String settingDate;

    /**
     * アンテナ強度履歴No.X
     */
    private List<Map<String, String>> antennaStrengthHistList;

    public String getSettingDate() {
        String ret = settingDate;
        String productCd = getProductCd();

        if (SmControlConstants.PRODUCT_CD_E_ALPHA.equals(productCd)
                || SmControlConstants.PRODUCT_CD_E_ALPHA_2.equals(productCd)) {
            // 機器からの応答データに"秒"が含まれないため、
            // Getのタイミングで"秒"(00秒固定)を指定して取得できるようにする
            ret += "00";
        }

        return ret;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public List<Map<String, String>> getAntennaStrengthHistList() {
        return antennaStrengthHistList;
    }

    public void setAntennaStrengthHistList(List<Map<String, String>> antennaStrengthHistList) {
        this.antennaStrengthHistList = antennaStrengthHistList;
    }

}
