package jp.co.osaki.osol.api.utility.energy.ems;

import java.util.Date;

import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastDayListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandPowerForecastTimeListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonWeekWeatherListResult;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;

/**
 * エネルギー使用状況（個別）関係のUtilityクラス
 * @author ya-ishida
 *
 */
public class DemandEmsOrgUtility {

    /**
     * 建物情報取得のパラメータを設定する
     * @param corpId
     * @param buildingId
     * @return
     */
    public static AllBuildingListDetailResultData getOrgBuildingInfoParam(String corpId, Long buildingId) {
        AllBuildingListDetailResultData param = new AllBuildingListDetailResultData();
        param.setCorpId(corpId);
        param.setBuildingId(buildingId);
        return param;
    }

    /**
     * 需要電力予測30分値データ取得のパラメータを設定する
     * @param smId
     * @param forecastDate
     * @return
     */
    public static CommonDemandPowerForecastTimeListResult getDemandPowerForecastTimeParam(Long smId, Date forecastDate) {
        CommonDemandPowerForecastTimeListResult param = new CommonDemandPowerForecastTimeListResult();
        param.setSmId(smId);
        param.setForecastDate(forecastDate);
        return param;
    }

    /**
     * 需要電力予測1日値データ取得のパラメータを設定する
     * @param smId
     * @param forecastDateFrom
     * @param forecastDateTo
     * @return
     */
    public static CommonDemandPowerForecastDayListResult getDemandPowerForecastDayParam(Long smId, Date forecastDateFrom, Date forecastDateTo) {
        CommonDemandPowerForecastDayListResult param = new CommonDemandPowerForecastDayListResult();
        param.setSmId(smId);
        param.setForecastDateFrom(forecastDateFrom);
        if(forecastDateTo == null) {
            param.setForecastDateTo(forecastDateFrom);
        } else {
            param.setForecastDateTo(forecastDateTo);
        }
        return param;
    }

    /**
     * 週間天気データ取得のパラメータを設定する
     * @param cityCd
     * @param targetDateFrom
     * @param targetDateTo
     * @return
     */
    public static CommonWeekWeatherListResult getWeekWeatherParam(String cityCd, Date targetDateFrom, Date targetDateTo) {
        CommonWeekWeatherListResult param = new CommonWeekWeatherListResult();
        param.setCityCd(cityCd);
        param.setTargetDateFrom(targetDateFrom);
        if(targetDateTo == null) {
            param.setTargetDateTo(targetDateFrom);
        } else {
            param.setTargetDateTo(targetDateTo);
        }
        return param;
    }

}
