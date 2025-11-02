package jp.co.osaki.osol.api.dao.weather;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.weather.AmedasWeatherListParameter;
import jp.co.osaki.osol.api.result.weather.AmedasWeatherListResult;
import jp.co.osaki.osol.api.resultdata.weather.AmedasWeatherListDetailResultData;
import jp.co.osaki.osol.api.servicedao.weather.AmedasWeatherListServiceDaoImpl;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * アメダス気象情報取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class AmedasWeatherListDao extends OsolApiDao<AmedasWeatherListParameter> {

    private final AmedasWeatherListServiceDaoImpl amedasWeatherListServiceDaoImpl;

    public AmedasWeatherListDao() {
        amedasWeatherListServiceDaoImpl = new AmedasWeatherListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public AmedasWeatherListResult query(AmedasWeatherListParameter parameter) throws Exception {

        AmedasWeatherListResult result = new AmedasWeatherListResult();

        Date observationDateFrom = super.getServerDateTime();
        Date observationDateTo = super.getServerDateTime();

        // 前日の1:00～当日の0:00までが取得範囲
        observationDateFrom = DateUtility.plusHour(DateUtility.plusDay(
                DateUtility.conversionDate(DateUtility.changeDateFormat(observationDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD), -1), 1);
        observationDateTo = DateUtility.conversionDate(DateUtility.changeDateFormat(observationDateTo, DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD);

        AmedasWeatherListDetailResultData param = new AmedasWeatherListDetailResultData();
        param.setObservationDateFrom(observationDateFrom);
        param.setObservationDateTo(observationDateTo);

        List<AmedasWeatherListDetailResultData> detailList = getResultList(amedasWeatherListServiceDaoImpl, param);
        result.setDetailList(detailList);
        return result;
    }

}
