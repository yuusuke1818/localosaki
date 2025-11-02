package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;

/**
*
* AielMasterエリア設定(設定) Param クラス
*
* @author s_sunada
*
*/
public class A210006Param extends BaseParam {

    /**
     * 日付
     */
    private String dateTime;

    /**
     * 週リスト
     */
    @Size(max=80, min=80)
    private List<Map<String, Object>> areaList;

    /**
     * エリア名称リスト
     */
    private List<AreaNameExtractResultData> areaNameList;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Map<String, Object>> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Map<String, Object>> areaList) {
        this.areaList = areaList;
    }

    public List<AreaNameExtractResultData> getAreaNameList() {
        return areaNameList;
    }

    public void setAreaNameList(List<AreaNameExtractResultData> areaNameList) {
        this.areaNameList = areaNameList;
    }

}
