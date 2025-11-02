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

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.entity.TSmControlScheduleLog;
import jp.co.osaki.osol.entity.TSmControlScheduleLogPK_;
import jp.co.osaki.osol.entity.TSmControlScheduleLog_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器制御スケジュール履歴取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmControlScheduleLogListServiceDaoImpl
        implements BaseServiceDao<SmControlScheduleLogListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleLogListDetailResultData> getResultList(SmControlScheduleLogListDetailResultData target,
            EntityManager em) {
        Long smControlScheduleLogId = target.getSmControlScheduleLogId();
        Long smId = target.getSmId();
        Date settingUpdateDateTimeFrom = target.getSettingUpdateDateTimeFrom();
        Date settingUpdateDateTimeTo = target.getSettingUpdateDateTimeTo();
        Date settingUpdateDateTimeOutRange = target.getSettingUpdateDateTimeOutRange();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmControlScheduleLogListDetailResultData> query = builder
                .createQuery(SmControlScheduleLogListDetailResultData.class);

        Root<TSmControlScheduleLog> root = query.from(TSmControlScheduleLog.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器制御スケジュール履歴ID
        if (smControlScheduleLogId != null) {
            whereList.add(builder.equal(
                    root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smControlScheduleLogId),
                    smControlScheduleLogId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(builder.equal(root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smId), smId));
        }
        //設定変更日時From、設定変更日時To
        if (settingUpdateDateTimeFrom != null && settingUpdateDateTimeTo != null) {
            whereList.add(builder.greaterThanOrEqualTo(root.get(TSmControlScheduleLog_.settingUpdateDatetime),
                    settingUpdateDateTimeFrom));
            whereList.add(builder.lessThan(root.get(TSmControlScheduleLog_.settingUpdateDatetime),
                    settingUpdateDateTimeTo));
        } else if (settingUpdateDateTimeFrom != null && settingUpdateDateTimeTo == null) {
            whereList.add(
                    builder.equal(root.get(TSmControlScheduleLog_.settingUpdateDatetime), settingUpdateDateTimeFrom));
        }

        //範囲外の設定日時取得
        if (settingUpdateDateTimeOutRange != null) {
            whereList.add(builder.lessThan(root.get(TSmControlScheduleLog_.settingUpdateDatetime),
                    settingUpdateDateTimeOutRange));
        }

        query = query.select(builder.construct(SmControlScheduleLogListDetailResultData.class,
                root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smControlScheduleLogId),
                root.get(TSmControlScheduleLog_.id).get(TSmControlScheduleLogPK_.smId),
                root.get(TSmControlScheduleLog_.settingUpdateDatetime),
                root.get(TSmControlScheduleLog_.scheduleManageDesignationFlg),
                root.get(TSmControlScheduleLog_.version)));

        if (!whereList.isEmpty()) {
            query = query.where(builder.and(whereList.toArray(new Predicate[] {})));
        }

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmControlScheduleLogListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleLogListDetailResultData> getResultList(
            List<SmControlScheduleLogListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmControlScheduleLogListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleLogListDetailResultData find(SmControlScheduleLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmControlScheduleLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmControlScheduleLogListDetailResultData merge(SmControlScheduleLogListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmControlScheduleLogListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
