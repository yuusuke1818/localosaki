package jp.co.osaki.osol.api.dao.weather;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.weather.TimelyWeatherListParameter;
import jp.co.osaki.osol.api.result.weather.TimelyWeatherListResult;
import jp.co.osaki.osol.api.resultdata.weather.TimelyWeatherListDetailResultData;
import jp.co.osaki.osol.api.servicedao.weather.TimelyWeatherListServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 時間別天気情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class TimelyWeatherListDao extends OsolApiDao<TimelyWeatherListParameter> {

    private final TimelyWeatherListServiceDaoImpl timelyWeatherListServiceDaoImpl;

    public TimelyWeatherListDao() {
        timelyWeatherListServiceDaoImpl = new TimelyWeatherListServiceDaoImpl();
    }



    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public TimelyWeatherListResult query(TimelyWeatherListParameter parameter) throws Exception {

        TimelyWeatherListResult result = new TimelyWeatherListResult();

        Date targetDate = super.getServerDateTime();
        // 当日以降が対象
        targetDate = DateUtility.conversionDate(DateUtility.changeDateFormat(targetDate, DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);

        TimelyWeatherListDetailResultData param = new TimelyWeatherListDetailResultData();
        param.setTargetDateTime(targetDate);
        List<TimelyWeatherListDetailResultData> detailList = getResultList(timelyWeatherListServiceDaoImpl, param);
        result.setDetailList(detailList);
        return result;

    }

}
