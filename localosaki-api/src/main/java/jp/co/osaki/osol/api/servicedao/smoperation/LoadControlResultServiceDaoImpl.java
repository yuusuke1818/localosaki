package jp.co.osaki.osol.api.servicedao.smoperation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.LoadControlResult;
import jp.co.osaki.osol.entity.TLoadControlResult;
import jp.co.osaki.osol.entity.TLoadControlResultPK_;
import jp.co.osaki.osol.entity.TLoadControlResult_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 負荷制御実績
 *
 * @author t_hirata
 *
 */
public class LoadControlResultServiceDaoImpl implements BaseServiceDao<LoadControlResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<LoadControlResult> getResultList(LoadControlResult t, EntityManager em) {
        // パラメータ
        Long smId = t.getSmId();
        String targetYm = t.getTargetYm();
        String controlTarget = t.getControlTarget();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LoadControlResult> query = builder.createQuery(LoadControlResult.class);

        Root<TLoadControlResult> root = query.from(TLoadControlResult.class);

        // 条件
        List<Predicate> whereList = new ArrayList<>();
        // 機器ID
        if (smId != null) {
            whereList.add(builder.equal(
                    root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.smId), smId));
        }
        // 記録日時
        if (!CheckUtility.isNullOrEmpty(targetYm)) {
            Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(targetYm.substring(0, 4)), Integer.parseInt(targetYm.substring(4, 6)) - 1, 1);
            Date targetYmdFr = cal.getTime();
            cal.add(Calendar.MONTH, 1);
            Date targetYmdTo = cal.getTime();
            whereList.add(builder.greaterThanOrEqualTo(
                    root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.loadControlDate), targetYmdFr));
            whereList.add(builder.lessThan(
                    root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.loadControlDate), targetYmdTo));
        }
        if (!CheckUtility.isNullOrEmpty(controlTarget)) {
            // 制御対象
            whereList.add(builder.equal(
                    root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.controlTarget), controlTarget));
        }

        // ソート
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.asc(root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.loadControlDate)));
        orderList.add(builder.asc(root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.controlLoad)));

        // 実行
        query = query.select(builder.construct(LoadControlResult.class,
                root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.smId),
                root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.loadControlDate),
                root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.controlTarget),
                root.get(TLoadControlResult_.id).get(TLoadControlResultPK_.controlLoad),
                root.get(TLoadControlResult_.dailyTotalMinute),
                root.get(TLoadControlResult_.version)))
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LoadControlResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<LoadControlResult> getResultList(List<LoadControlResult> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<LoadControlResult> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public LoadControlResult find(LoadControlResult target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(LoadControlResult target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public LoadControlResult merge(LoadControlResult target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(LoadControlResult target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
