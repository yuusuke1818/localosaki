package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpTargetAlarmSelectDetailResultData;
import jp.co.osaki.osol.entity.MCorpTargetAlarm;
import jp.co.osaki.osol.entity.MCorpTargetAlarm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業目標超過警報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CorpTargetAlarmSelectServiceDaoImpl implements BaseServiceDao<CorpTargetAlarmSelectDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CorpTargetAlarmSelectDetailResultData> getResultList(CorpTargetAlarmSelectDetailResultData target,
            EntityManager em) {

        String corpId = target.getCorpId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CorpTargetAlarmSelectDetailResultData> query = builder
                .createQuery(CorpTargetAlarmSelectDetailResultData.class);

        Root<MCorpTargetAlarm> root = query.from(MCorpTargetAlarm.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MCorpTargetAlarm_.corpId), corpId));
        //削除フラグ
        whereList.add(builder.equal(root.get(MCorpTargetAlarm_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(CorpTargetAlarmSelectDetailResultData.class,
                root.get(MCorpTargetAlarm_.corpId),
                root.get(MCorpTargetAlarm_.detectTime),
                root.get(MCorpTargetAlarm_.alertMailAddress1),
                root.get(MCorpTargetAlarm_.alertMailAddress2),
                root.get(MCorpTargetAlarm_.alertMailAddress3),
                root.get(MCorpTargetAlarm_.alertMailAddress4),
                root.get(MCorpTargetAlarm_.alertMailAddress5),
                root.get(MCorpTargetAlarm_.alertMailAddress6),
                root.get(MCorpTargetAlarm_.alertMailAddress7),
                root.get(MCorpTargetAlarm_.alertMailAddress8),
                root.get(MCorpTargetAlarm_.alertMailAddress9),
                root.get(MCorpTargetAlarm_.alertMailAddress10),
                root.get(MCorpTargetAlarm_.mailWillSendTime),
                root.get(MCorpTargetAlarm_.mailLastSendDate),
                root.get(MCorpTargetAlarm_.monthlyAlarmFlg),
                root.get(MCorpTargetAlarm_.periodAlarmFlg),
                root.get(MCorpTargetAlarm_.yearAlarmFlg),
                root.get(MCorpTargetAlarm_.monthlyAlarmLockDate),
                root.get(MCorpTargetAlarm_.periodAlarmLockDate),
                root.get(MCorpTargetAlarm_.yearAlarmLockDate),
                root.get(MCorpTargetAlarm_.sharePeriod),
                root.get(MCorpTargetAlarm_.targetKwhMonthlyOverRate),
                root.get(MCorpTargetAlarm_.targetKwhPeriodOverRate),
                root.get(MCorpTargetAlarm_.targetKwhYearOverRate),
                root.get(MCorpTargetAlarm_.targetKwhMonth1),
                root.get(MCorpTargetAlarm_.targetKwhMonth2),
                root.get(MCorpTargetAlarm_.targetKwhMonth3),
                root.get(MCorpTargetAlarm_.targetKwhMonth4),
                root.get(MCorpTargetAlarm_.targetKwhMonth5),
                root.get(MCorpTargetAlarm_.targetKwhMonth6),
                root.get(MCorpTargetAlarm_.targetKwhMonth7),
                root.get(MCorpTargetAlarm_.targetKwhMonth8),
                root.get(MCorpTargetAlarm_.targetKwhMonth9),
                root.get(MCorpTargetAlarm_.targetKwhMonth10),
                root.get(MCorpTargetAlarm_.targetKwhMonth11),
                root.get(MCorpTargetAlarm_.targetKwhMonth12),
                root.get(MCorpTargetAlarm_.delFlg),
                root.get(MCorpTargetAlarm_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CorpTargetAlarmSelectDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CorpTargetAlarmSelectDetailResultData> getResultList(List<CorpTargetAlarmSelectDetailResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CorpTargetAlarmSelectDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CorpTargetAlarmSelectDetailResultData find(CorpTargetAlarmSelectDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(CorpTargetAlarmSelectDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CorpTargetAlarmSelectDetailResultData merge(CorpTargetAlarmSelectDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(CorpTargetAlarmSelectDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
