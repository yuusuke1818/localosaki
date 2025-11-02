package jp.co.osaki.osol.api.dao.signage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkWeatherParameter;
import jp.co.osaki.osol.api.result.signage.SignageApkWeatherResult;
import jp.co.osaki.osol.api.resultdata.signage.SignageApkWeatherTimeResultData;
import jp.co.osaki.osol.api.resultdata.signage.SignageApkWeatherWeekResultData;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TTimeSlotWeatherServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TWeekWeatherServiceDaoImpl;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.entity.TTimeSlotWeather;
import jp.co.osaki.osol.entity.TTimeSlotWeatherPK;
import jp.co.osaki.osol.entity.TWeekWeather;
import jp.co.osaki.osol.entity.TWeekWeatherPK;

/**
 * アプリから天気を取得する時に利用 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class SignageApkWeatherDao extends OsolApiDao<SignageApkWeatherParameter> {

    private final TTimeSlotWeatherServiceDaoImpl tTimeSlotWeatherServiceDaoImpl;
    private final TWeekWeatherServiceDaoImpl tWeekWeatherServiceDaoImpl;
    private final TBuildingApiServiceDaoImpl tBuildingServiceDaoImpl;

    public SignageApkWeatherDao() {
        tWeekWeatherServiceDaoImpl = new TWeekWeatherServiceDaoImpl();
        tTimeSlotWeatherServiceDaoImpl = new TTimeSlotWeatherServiceDaoImpl();
        tBuildingServiceDaoImpl = new TBuildingApiServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SignageApkWeatherResult query(SignageApkWeatherParameter parameter) throws Exception {
        SignageApkWeatherResult result = new SignageApkWeatherResult();
        String cityName = null;

        //本日の年月日を取得
        Timestamp serverDateTime = getServerDateTime();

        //都市名を取得する
        TBuilding searchBuilding = new TBuilding();
        TBuildingPK pkBuilding = new TBuildingPK();
        pkBuilding.setCorpId(parameter.getOperationCorpId());
        pkBuilding.setBuildingId(parameter.getBuildingId());
        searchBuilding.setId(pkBuilding);
        TBuilding building = find(tBuildingServiceDaoImpl, searchBuilding);
        if (building == null) {
            return new SignageApkWeatherResult();
        } else {
            cityName = building.getMWeatherCity().getCityName();
        }

        //時間帯別天気を取得する
        TTimeSlotWeather searchTime = new TTimeSlotWeather();
        TTimeSlotWeatherPK pkTime = new TTimeSlotWeatherPK();
        pkTime.setCityCd(building.getMWeatherCity().getCityCd());
        pkTime.setTargetDate(serverDateTime);
        searchTime.setId(pkTime);
        List<TTimeSlotWeather> timeWeatherList = getResultList(tTimeSlotWeatherServiceDaoImpl, searchTime);
        List<SignageApkWeatherTimeResultData> timeList = new ArrayList<>();
        if (timeWeatherList != null && !timeWeatherList.isEmpty()) {
            for (TTimeSlotWeather timeWeather : timeWeatherList) {
                timeList.add(new SignageApkWeatherTimeResultData(timeWeather.getId().getCityCd(),
                        timeWeather.getId().getTargetDate(), timeWeather.getId().getWeatherTimeSlot(),
                        timeWeather.getWeatherState(), timeWeather.getTemperature(), timeWeather.getRainAmount()));
            }
        }

        //週間天気を取得する
        TWeekWeather searchWeek = new TWeekWeather();
        TWeekWeatherPK pkWeek = new TWeekWeatherPK();
        pkWeek.setCityCd(building.getMWeatherCity().getCityCd());
        pkWeek.setTargetDate(serverDateTime);
        searchWeek.setId(pkWeek);
        List<TWeekWeather> weekWeatherList = getResultList(tWeekWeatherServiceDaoImpl, searchWeek);
        List<SignageApkWeatherWeekResultData> weekList = new ArrayList<>();
        if (weekWeatherList != null && !weekWeatherList.isEmpty()) {
            for (TWeekWeather weekWeather : weekWeatherList) {
                weekList.add(new SignageApkWeatherWeekResultData(weekWeather.getId().getCityCd(),
                        weekWeather.getId().getTargetDate(), weekWeather.getWeatherState(), weekWeather.getHighTemp(),
                        weekWeather.getLowTemp(), weekWeather.getRainProbability()));
            }
        }

        result.setYmd(serverDateTime);
        result.setCityName(cityName);
        result.setTimeWeatherList(timeList);
        result.setWeekWeatherList(weekList);
        return result;
    }

}
