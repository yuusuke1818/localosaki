package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;


/**
 *
 * AielMasterスケジュール(設定) Param クラス
 *
 * @author s_sunada
 *
 */
public class A210002Param extends BaseParam {

    /**
     * 日付
     */
    private String dateTime;

    /**
     * 週リスト
     */
    @Size(max=8, min=8)
    private List<Map<String, Object>> weekList;

    /**
     * 指定日リスト
     */
    @Size(max=10, min=10)
    private List<Map<String, Object>> specifiedDateList;


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Map<String, Object>> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Map<String, Object>> weekList) {
        this.weekList = weekList;
    }

    public List<Map<String, Object>> getSpecifiedDateList() {
        return specifiedDateList;
    }

    public void setSpecifiedDateList(List<Map<String, Object>> specifiedDateList) {
        this.specifiedDateList = specifiedDateList;
    }


}
