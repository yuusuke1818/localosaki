package jp.co.osaki.osol.api.dao.smcontrol;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterPowerDemandForecastSelectParameter;
import jp.co.osaki.osol.api.servicedao.entity.TDemandPowerForecastDayServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TDemandPowerForecastTimeServiceDaoImpl;
import jp.co.osaki.osol.entity.TDemandPowerForecastDay;
import jp.co.osaki.osol.entity.TDemandPowerForecastDayPK;
import jp.co.osaki.osol.entity.TDemandPowerForecastTime;
import jp.co.osaki.osol.entity.TDemandPowerForecastTimePK;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A210009Param;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 需要電力予測データ（取得） Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class AielMasterPowerDemandForecastSelectDao extends BaseSmControlDao {

    private final TDemandPowerForecastDayServiceDaoImpl tDemandPowerForecastDayServiceDaoImpl;
    private final TDemandPowerForecastTimeServiceDaoImpl tDemandPowerForecastTimeServiceDaoImpl;


    public AielMasterPowerDemandForecastSelectDao() {
        tDemandPowerForecastDayServiceDaoImpl = new TDemandPowerForecastDayServiceDaoImpl();
        tDemandPowerForecastTimeServiceDaoImpl = new TDemandPowerForecastTimeServiceDaoImpl();
    }

    /**
     * 需要電力1日値データを削除する
     * @param param
     */
    public void deleteDemandPowerForecastDay(Long smId) {
        TDemandPowerForecastDay param = new TDemandPowerForecastDay();
        TDemandPowerForecastDayPK pkParam = new TDemandPowerForecastDayPK();
        pkParam.setSmId(smId);
        param.setId(pkParam);
        List<TDemandPowerForecastDay> deleteList = getResultList(tDemandPowerForecastDayServiceDaoImpl, param);

        if(deleteList != null && !deleteList.isEmpty()) {
            for(TDemandPowerForecastDay deleteData : deleteList) {
                remove(tDemandPowerForecastDayServiceDaoImpl, deleteData);
            }
        }
    }

    /**
     * 需要電力30分値データを削除する
     * @param smId
     */
    public void deleteDemandPowerForecastTime(Long smId) {
        TDemandPowerForecastTime param = new TDemandPowerForecastTime();
        TDemandPowerForecastTimePK pkParam = new TDemandPowerForecastTimePK();
        pkParam.setSmId(smId);
        param.setId(pkParam);
        List<TDemandPowerForecastTime> deleteList = getResultList(tDemandPowerForecastTimeServiceDaoImpl, param);

        if(deleteList != null && !deleteList.isEmpty()) {
            for(TDemandPowerForecastTime deleteData : deleteList) {
                remove(tDemandPowerForecastTimeServiceDaoImpl, deleteData);
            }
        }
    }

    /**
     * 需要電力1日値データを登録する
     * @param parameter
     * @param res
     * @param serverDateTime
     */
    public void insertDemandPowerForecastDay(AielMasterPowerDemandForecastSelectParameter parameter, FvpCtrlMngResponse<A210009Param> res, Timestamp serverDateTime) {
        if(res.getParam().getForecastDayList() != null
                && !res.getParam().getForecastDayList().isEmpty()
                && DateUtility.conversionDate(res.getParam().getSelectDate(), DateUtility.DATE_FORMAT_YYMMDDHHMM) != null) {
            Date currentDate = DateUtility.conversionDate(res.getParam().getSelectDate().substring(0, 6), DateUtility.DATE_FORMAT_YYMMDD);

            //ログインユーザーIDを取得
            Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

            for(Map<String, String> forecastDay : res.getParam().getForecastDayList()) {
                TDemandPowerForecastDay param = new TDemandPowerForecastDay();
                TDemandPowerForecastDayPK pkParam = new TDemandPowerForecastDayPK();
                pkParam.setSmId(res.getSmId());
                pkParam.setForecastDate(currentDate);
                param.setId(pkParam);
                if (CheckUtility.isNullOrEmpty(forecastDay.get("airDemand").trim())) {
                    param.setAirDemandForecastValue(null);
                }
                else {
                    param.setAirDemandForecastValue(BigDecimal.valueOf(Long.valueOf(forecastDay.get("airDemand"))));
                }
                if (CheckUtility.isNullOrEmpty(forecastDay.get("totalDemand").trim())) {
                    param.setTotalDemandValue(null);
                }
                else {
                    param.setTotalDemandValue(BigDecimal.valueOf(Long.valueOf(forecastDay.get("totalDemand"))));
                }
                param.setCreateUserId(loginUserId);
                param.setCreateDate(serverDateTime);
                param.setUpdateUserId(loginUserId);
                param.setUpdateDate(serverDateTime);
                persist(tDemandPowerForecastDayServiceDaoImpl, param);
                currentDate = DateUtility.plusDay(currentDate, 1);
            }

        }
    }

    /**
     * 需要電力30分値データを登録する
     * @param parameter
     * @param res
     * @param serverDateTime
     */
    public void insertDemandPowerForecastTime(AielMasterPowerDemandForecastSelectParameter parameter, FvpCtrlMngResponse<A210009Param> res, Timestamp serverDateTime) {
        if(res.getParam().getForecastTimeList() != null
                && !res.getParam().getForecastTimeList().isEmpty()
                && DateUtility.conversionDate(res.getParam().getSelectDate(), DateUtility.DATE_FORMAT_YYMMDDHHMM) != null) {
            Date currentDate = DateUtility.conversionDate(res.getParam().getSelectDate().substring(0, 6), DateUtility.DATE_FORMAT_YYMMDD);
            BigDecimal currentJigenNo = BigDecimal.ONE;

            //ログインユーザーIDを取得
            Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

            for(Map<String, String> forecastTime : res.getParam().getForecastTimeList()) {
                TDemandPowerForecastTime param = new TDemandPowerForecastTime();
                TDemandPowerForecastTimePK pkParam = new TDemandPowerForecastTimePK();
                pkParam.setSmId(res.getSmId());
                pkParam.setForecastDate(currentDate);
                pkParam.setJigenNo(currentJigenNo);
                param.setId(pkParam);
                if (CheckUtility.isNullOrEmpty(forecastTime.get("standardDemand").trim())) {
                    param.setNormalForecastValue(null);
                }
                else {
                    param.setNormalForecastValue(BigDecimal.valueOf(Long.valueOf(forecastTime.get("standardDemand"))));
                }
                if (CheckUtility.isNullOrEmpty(forecastTime.get("comfortableDemand").trim())) {
                    param.setComfortableForecastValue(null);
                }
                else {
                    param.setComfortableForecastValue(BigDecimal.valueOf(Long.valueOf(forecastTime.get("comfortableDemand"))));
                }
                if (CheckUtility.isNullOrEmpty(forecastTime.get("ecoDemand").trim())) {
                    param.setEcoForecastValue(null);
                }
                else {
                    param.setEcoForecastValue(BigDecimal.valueOf(Long.valueOf(forecastTime.get("ecoDemand"))));
                }
                if (CheckUtility.isNullOrEmpty(forecastTime.get("autoDemand").trim())) {
                    param.setAiForecastValue(null);
                }
                else {
                    param.setAiForecastValue(BigDecimal.valueOf(Long.valueOf(forecastTime.get("autoDemand"))));
                }
                param.setCreateUserId(loginUserId);
                param.setCreateDate(serverDateTime);
                param.setUpdateUserId(loginUserId);
                param.setUpdateDate(serverDateTime);
                persist(tDemandPowerForecastTimeServiceDaoImpl, param);
                if(currentJigenNo.compareTo(BigDecimal.valueOf(48)) == 0) {
                    currentDate = DateUtility.plusDay(currentDate, 1);
                    currentJigenNo = BigDecimal.ONE;
                } else {
                    currentJigenNo = currentJigenNo.add(BigDecimal.ONE);
                }
            }
        }
    }

}
