package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * メーター表示切替
 *
 * @author y.nakamura
 */
public class MeterDispChangeInfo {

    /** メーター表示切替のMap */
    private Map<Long, Integer> pagerMap;

    /** メーター表示切替のPage */
    private int page;

    public MeterDispChangeInfo() {
        this.pagerMap = new LinkedHashMap<>();
        this.page = 0;
    }

    public MeterDispChangeInfo(Map<Long, Integer> pagerMap, int page) {
        this.pagerMap = pagerMap;
        this.page = page;
    }

    public Map<Long, Integer> getPagerMap() {
        return pagerMap;
    }

    public void setPagerMap(Map<Long, Integer> pagerMap) {
        this.pagerMap = pagerMap;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
