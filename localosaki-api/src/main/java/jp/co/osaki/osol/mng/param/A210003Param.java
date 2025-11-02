package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;


/**
 *
 * AielMaster店舗設定(取得) Param クラス
 *
 * @author s_sunada
 *
 */
public class A210003Param extends BaseParam {

    /**
     * 日付
     */
    private String dateTime;

    /**
     * 計算式リスト
     */
    private List<Map<String, String>> calcList;



    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Map<String, String>> getCalcList() {
        return calcList;
    }

    public void setCalcList(List<Map<String, String>> calcList) {
        this.calcList = calcList;
    }

}
