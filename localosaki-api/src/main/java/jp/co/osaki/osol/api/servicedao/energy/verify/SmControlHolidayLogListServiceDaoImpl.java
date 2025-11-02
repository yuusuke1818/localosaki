package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlHolidayLog;
import jp.co.osaki.osol.entity.TSmControlHolidayLogPK_;
import jp.co.osaki.osol.entity.TSmControlHolidayLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御祝日設定履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlHolidayLogListServiceDaoImpl implements BaseServiceDao<SmControlHolidayLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayLogListDetailResultData> getResultList(SmControlHolidayLogListDetailResultData target,
            EntityManager em) {
        Long smControlHolidayLogId = target.getSmControlHolidayLogId();
        Long smId = target.getSmId();
        Date settingUpdateDateTimeFrom = target.getSettingUpdateDateTimeFrom();
        Date settingUpdateDateTimeTo = target.getSettingUpdateDateTimeTo();
        Date settingUpdateDateTimeOutRange = target.getSettingUpdateDateTimeOutRange();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlHolidayLogListDetailResultData> query = builder
                .createQuery(SmControlHolidayLogListDetailResultData.class);

        Root<TSmControlHolidayLog> root = query.from(TSmControlHolidayLog.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器制御祝日設定履歴ID
        if (smControlHolidayLogId != null) {
            whereList.add(
                    builder.equal(root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smControlHolidayLogId),
                            smControlHolidayLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smId), smId));
        }
        //設定変更日時From、設定変更日時To
        if (settingUpdateDateTimeFrom != null && settingUpdateDateTimeTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TSmControlHolidayLog_.settingUpdateDatetime),
                    settingUpdateDateTimeFrom));
            whereList.add(builder.lessThan(root.get(TSmControlHolidayLog_.settingUpdateDatetime),
                    settingUpdateDateTimeTo));
        }

        //範囲外の設定日時取得
        if (settingUpdateDateTimeOutRange != null) {
            whereList.add(builder.lessThan(root.get(TSmControlHolidayLog_.settingUpdateDatetime),
                    settingUpdateDateTimeOutRange));
        }

        query = query.select(builder.construct(SmControlHolidayLogListDetailResultData.class,
                root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smControlHolidayLogId),
                root.get(TSmControlHolidayLog_.id).get(TSmControlHolidayLogPK_.smId),
                root.get(TSmControlHolidayLog_.settingUpdateDatetime),
                root.get(TSmControlHolidayLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlHolidayLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayLogListDetailResultData> getResultList(
            List<SmControlHolidayLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlHolidayLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlHolidayLogListDetailResultData find(SmControlHolidayLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlHolidayLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlHolidayLogListDetailResultData merge(SmControlHolidayLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlHolidayLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
