package jp.co.osaki.osol.mng.param;

import java.util.List;
import java.util.Map;

public class A210011Param extends BaseParam {

    /**
     * 日時
     */
    private String settingDate;

    /**
     * デマンド日報リスト
     */
    private List<Map<String,String>> demandDayReportList;

    public String getSettingDate() {
        return settingDate;
    }

    public void setSettingDate(String settingDate) {
        this.settingDate = settingDate;
    }

    public List<Map<String, String>> getDemandDayReportList() {
        return demandDayReportList;
    }

    public void setDemandDayReportList(List<Map<String, String>> demandDayReportList) {
        this.demandDayReportList = demandDayReportList;
    }



}
