package jp.co.osaki.osol.mng.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;


/**
 *
 * AielMaster店舗設定(設定) Param クラス
 *
 * @author s_sunada
 *
 */
public class A210004Param extends BaseParam {

    /**
     * 日付
     */
    private String dateTime;

    /**
     * 計算式リスト
     */
    @Size(max=256, min=256)
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

    @AssertTrue
    public boolean isValidateCalcList() {
        return calcList != null && validateList(calcList, new HashMap<String, String>() {{
            put("measurePoint", "[\\s+-]");    // 計測ポイント(符号)
        }});
    }

}
