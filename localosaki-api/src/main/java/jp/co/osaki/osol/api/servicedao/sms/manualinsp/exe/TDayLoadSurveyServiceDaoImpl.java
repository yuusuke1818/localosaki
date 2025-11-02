package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TDayLoadSurvey;
import jp.co.osaki.osol.entity.TDayLoadSurveyPK_;
import jp.co.osaki.osol.entity.TDayLoadSurvey_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 任意検針実行 TDayLoadSurvey ServiceDaoクラス
 * @author tominaga.d
 */
public class TDayLoadSurveyServiceDaoImpl implements BaseServiceDao<TDayLoadSurvey>{

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
//  SELECT
//      *
//    FROM
//      t_day_load_survey
//    WHERE
//      dev_id = '' /* $1 */
//      AND meter_mng_id = 1 /* $2 */
//      AND kwh30 is not null
//      AND dmv_kwh is not null
//    ORDER BY
//      get_date desc
//    LIMIT 1
    @Override
    public List<TDayLoadSurvey> getResultList(TDayLoadSurvey target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TDayLoadSurvey> query = builder.createQuery(TDayLoadSurvey.class);

        // ■from句の整理
        // [MuDM2]ロードサーベイ日データTBL
        Root<TDayLoadSurvey> root = query.from(TDayLoadSurvey.class);


        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.devId), target.getId().getDevId()));
        whereList.add(builder.equal(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.meterMngId), target.getId().getMeterMngId()));
        whereList.add(builder.isNotNull(root.get(TDayLoadSurvey_.kwh30)));
        whereList.add(builder.isNotNull(root.get(TDayLoadSurvey_.dmvKwh)));

        // ■SELECT句の整理
        query = query.select(root).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.desc(root.get(TDayLoadSurvey_.id).get(TDayLoadSurveyPK_.getDate)));

        return em.createQuery(query).setMaxResults(1).getResultList();
    }

    @Override
    public List<TDayLoadSurvey> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurvey> getResultList(List<TDayLoadSurvey> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TDayLoadSurvey> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TDayLoadSurvey find(TDayLoadSurvey target, EntityManager em) {
        return em.find(TDayLoadSurvey.class, target.getId());
    }

    @Override
    public void persist(TDayLoadSurvey target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public TDayLoadSurvey merge(TDayLoadSurvey target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(TDayLoadSurvey target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
