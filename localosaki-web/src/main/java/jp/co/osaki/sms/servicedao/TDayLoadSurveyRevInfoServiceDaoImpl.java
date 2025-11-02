package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.entity.TDayLoadSurveyRev;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK;
import jp.co.osaki.osol.entity.TDayLoadSurveyRevPK_;
import jp.co.osaki.osol.entity.TDayLoadSurveyRev_;
import jp.co.osaki.osol.utility.CriteriaUtility;
import jp.co.osaki.sms.resultset.TDayLoadSurveyInfoResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * ロードサーベイ日データ(逆方向)情報ServiceDaoクラス.
 *
 * @author ozaki.y
 */
public class TDayLoadSurveyRevInfoServiceDaoImpl implements BaseServiceDao<TDayLoadSurveyInfoResultSet> {

    /** パラメータキー */
    public static final String PARAM_KEY = "dbParam";

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurveyInfoResultSet> getResultList(TDayLoadSurveyInfoResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurveyInfoResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        TDayLoadSurveyInfoResultSet param = (TDayLoadSurveyInfoResultSet) parameterMap.get(PARAM_KEY).get(0);
        return getOldestGetDateRecord(param, em);
    }

    /**
     * 指定装置の最古の収集日時データを取得.
     *
     * @param param 検索パラメータ
     * @param em エンティティマネージャ
     * @return 指定装置の最古の収集日時データ
     */
    private List<TDayLoadSurveyInfoResultSet> getOldestGetDateRecord(TDayLoadSurveyInfoResultSet param, EntityManager em) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurveyRev> criteria = cb.createQuery(TDayLoadSurveyRev.class);

        // ロードサーベイ日データ(T_DAY_LOAD_SURVEY)
        Root<TDayLoadSurveyRev> dayLoadSurvey = criteria.from(TDayLoadSurveyRev.class);

        Collection<Predicate> whereClct = new ArrayList<>();

        // 装置ID
        whereClct.add(
                cb.equal(dayLoadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.devId), param.getDevId()));

        List<Long> meterMngIdList = param.getMeterMngIdList();
        if (CollectionUtils.isNotEmpty(meterMngIdList)) {
            // メーター管理番号
            whereClct.addAll(CriteriaUtility.createInCollection(
                    dayLoadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.meterMngId), meterMngIdList));
        }

        criteria = criteria.select(dayLoadSurvey).where(whereClct.toArray(new Predicate[0]))
                .orderBy(cb.asc(dayLoadSurvey.get(TDayLoadSurveyRev_.id).get(TDayLoadSurveyRevPK_.getDate)));

        List<TDayLoadSurveyRev> resultList = em.createQuery(criteria).setMaxResults(1).getResultList();

        List<TDayLoadSurveyInfoResultSet> resultSetList = new ArrayList<>();
        for (TDayLoadSurveyRev result : resultList) {
            TDayLoadSurveyRevPK idValue = result.getId();

            TDayLoadSurveyInfoResultSet resultSet = new TDayLoadSurveyInfoResultSet();
            resultSet.setDevId(idValue.getDevId());
            resultSet.setGetDate(idValue.getGetDate());

            resultSetList.add(resultSet);
        }

        return resultSetList;
    }

    @Override
    public List<TDayLoadSurveyInfoResultSet> getResultList(List<TDayLoadSurveyInfoResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurveyInfoResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDayLoadSurveyInfoResultSet find(TDayLoadSurveyInfoResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(TDayLoadSurveyInfoResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDayLoadSurveyInfoResultSet merge(TDayLoadSurveyInfoResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(TDayLoadSurveyInfoResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
