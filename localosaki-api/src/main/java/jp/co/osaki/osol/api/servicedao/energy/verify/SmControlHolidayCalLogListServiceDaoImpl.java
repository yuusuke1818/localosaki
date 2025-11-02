package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLog;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLogPK_;
import jp.co.osaki.osol.entity.TSmControlHolidayCalLog_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御祝日設定カレンダ履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlHolidayCalLogListServiceDaoImpl
        implements BaseServiceDao<SmControlHolidayCalLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getResultList(
            SmControlHolidayCalLogListDetailResultData target, EntityManager em) {
        Long smControlHolidayLogId = target.getSmControlHolidayLogId();
        Long smId = target.getSmId();
        String holidayMmDdFrom = target.getHolidayMmDdFrom();
        String holidayMmDdTo = target.getHolidayMmDdTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlHolidayCalLogListDetailResultData> query = builder
                .createQuery(SmControlHolidayCalLogListDetailResultData.class);

        Root<TSmControlHolidayCalLog> root = query.from(TSmControlHolidayCalLog.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器制御祝日設定履歴ID
        if (smControlHolidayLogId != null) {
            whereList.add(builder.equal(
                    root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smControlHolidayLogId),
                    smControlHolidayLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(
                    builder.equal(root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smId), smId));
        }
        //祝日月日From、祝日月日To
        if (!CheckUtility.isNullOrEmpty(holidayMmDdFrom) && !CheckUtility.isNullOrEmpty(holidayMmDdTo)) {
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.holidayMmdd),
                    holidayMmDdFrom));
            whereList.add(builder.lessThanOrEqualTo(
                    root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.holidayMmdd), holidayMmDdTo));
        }

        query = query.select(builder.construct(SmControlHolidayCalLogListDetailResultData.class,
                root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smControlHolidayLogId),
                root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.smId),
                root.get(TSmControlHolidayCalLog_.id).get(TSmControlHolidayCalLogPK_.holidayMmdd),
                root.get(TSmControlHolidayCalLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getResultList(
            List<SmControlHolidayCalLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlHolidayCalLogListDetailResultData find(SmControlHolidayCalLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlHolidayCalLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlHolidayCalLogListDetailResultData merge(SmControlHolidayCalLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlHolidayCalLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
