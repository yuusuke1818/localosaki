/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.THopidHistory;
import jp.co.osaki.osol.entity.THopidHistoryPK_;
import jp.co.osaki.osol.entity.THopidHistory_;
import jp.co.osaki.sms.SmsConstants.HOPID_HISTORY_EXECUPD;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * リレー無線ID履歴テーブル ServiceDaoクラス
 * @author sagi_h
 *
 */
public class THopidHistoryServiceDaoImpl implements BaseServiceDao<THopidHistory> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        if (parameterMap.containsKey(HOPID_HISTORY_EXECUPD.DELETE_HISTORY.getVal())) {
            final String devId = (String) parameterMap
                    .get(HOPID_HISTORY_EXECUPD.DELETE_HISTORY.getVal()).get(0);
            final int deleteAmount = (int) parameterMap
                    .get(HOPID_HISTORY_EXECUPD.MAXAMOUNT.getVal()).get(0);

            // 取得クエリ生成
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<THopidHistory> query = builder.createQuery(THopidHistory.class);

            Root<THopidHistory> root = query.from(THopidHistory.class);

            // 条件リスト
            List<Predicate> whereList = new ArrayList<>();

            // 装置IDが引数と一致
            whereList
                    .add(builder.equal(root.get(THopidHistory_.id).get(THopidHistoryPK_.devId), devId));

            // 順序: 更新日時で降順(新しい順)
            query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})))
                    .orderBy(builder.desc(root.get(THopidHistory_.updateDate)));

            // 削除対象の履歴エンティティを取得
            List<THopidHistory> deleteItems = em.createQuery(query).setFirstResult(deleteAmount).getResultList();
            for(THopidHistory tHopidHistory : deleteItems) {
                remove(tHopidHistory, em);
            }
        }
        return 0;
    }

    @Override
    public List<THopidHistory> getResultList(THopidHistory target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<THopidHistory> query = builder.createQuery(THopidHistory.class);

        Root<THopidHistory> root = query.from(THopidHistory.class);

        // 条件リスト
        List<Predicate> whereList = new ArrayList<>();

        // 装置IDが引数と一致
        whereList
                .add(builder.equal(root.get(THopidHistory_.id).get(THopidHistoryPK_.devId), target.getId().getDevId()));

        // 順序: 更新日時で降順(新しい順)
        query.select(root).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.desc(root.get(THopidHistory_.updateDate)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<THopidHistory> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<THopidHistory> getResultList(List<THopidHistory> entityList, EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<THopidHistory> getResultList(EntityManager em) {
        // 何もしない
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public THopidHistory find(THopidHistory target, EntityManager em) {
        return em.find(THopidHistory.class, target.getId());
    }

    @Override
    public void persist(THopidHistory target, EntityManager em) {
        em.persist(target);
        return;
    }

    @Override
    public THopidHistory merge(THopidHistory target, EntityManager em) {
        return em.merge(target);
    }

    @Override
    public void remove(THopidHistory target, EntityManager em) {
        em.remove(target);
    }

}
