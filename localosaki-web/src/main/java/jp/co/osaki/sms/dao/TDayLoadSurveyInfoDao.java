package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.TDayLoadSurveyInfoResultSet;
import jp.co.osaki.sms.servicedao.TDayLoadSurveyInfoServiceDaoImpl;
import jp.co.osaki.sms.servicedao.TDayLoadSurveyRevInfoServiceDaoImpl;

/**
 * ロードサーベイ日データ情報Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class TDayLoadSurveyInfoDao extends SmsDao {

    private final TDayLoadSurveyInfoServiceDaoImpl fwdDaoImpl;

    private final TDayLoadSurveyRevInfoServiceDaoImpl revDaoImpl;

    public TDayLoadSurveyInfoDao() {
        fwdDaoImpl = new TDayLoadSurveyInfoServiceDaoImpl();
        revDaoImpl = new TDayLoadSurveyRevInfoServiceDaoImpl();
    }

    /**
     * 指定装置の最古の収集日時を取得.
     *
     * @param devId 装置ID
     * @param isForward 正方向フラグ
     * @param meterMngIdList メーター管理番号List
     * @return 指定装置の最古の収集日時
     */
    public String getOldestGetDate(String devId, boolean isForward, List<Long> meterMngIdList) {
        String oldestGetDate = null;

        Map<String, List<Object>> parameterMap = new HashMap<>();

        TDayLoadSurveyInfoResultSet dbParam = new TDayLoadSurveyInfoResultSet();
        dbParam.setDevId(devId);
        dbParam.setMeterMngIdList(meterMngIdList);

        List<TDayLoadSurveyInfoResultSet> resultList = null;
        if (isForward) {
            parameterMap.put(TDayLoadSurveyInfoServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));
            resultList = getResultList(fwdDaoImpl, parameterMap);

        } else {
            parameterMap.put(TDayLoadSurveyRevInfoServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));
            resultList = getResultList(revDaoImpl, parameterMap);
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            oldestGetDate = resultList.get(0).getGetDate();
        }

        return oldestGetDate;
    }
}
