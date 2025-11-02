/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.repeater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.repeater.ListSmsRepeatersResultData;
import jp.co.osaki.osol.entity.MRepeater;
import jp.co.osaki.osol.entity.MRepeaterPK_;
import jp.co.osaki.osol.entity.MRepeater_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 中継装置管理 中継装置一覧取得 ServiceDaoクラス
 * @author maruta.y
 */
public class ListSmsRepeaterServiceDaoImpl implements BaseServiceDao<ListSmsRepeatersResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<ListSmsRepeatersResultData> getResultList(ListSmsRepeatersResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsRepeatersResultData> query = builder.createQuery(ListSmsRepeatersResultData.class);

        // ■from句の整理
        Root<MRepeater> root = query.from(MRepeater.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.devId), target.getDevId()));
        if (target.getRepeaterMngId() != null) {
            whereList.add(builder.equal(root.get(MRepeater_.id).get(MRepeaterPK_.repeaterMngId), target.getRepeaterMngId()));
        }

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsRepeatersResultData.class,
                root.get(MRepeater_.id).get(MRepeaterPK_.repeaterMngId),
                root.get(MRepeater_.id).get(MRepeaterPK_.devId),
                root.get(MRepeater_.recDate),
                root.get(MRepeater_.recMan),
                root.get(MRepeater_.commandFlg),
                root.get(MRepeater_.srvEnt),
                root.get(MRepeater_.repeaterId),
                root.get(MRepeater_.memo),
                root.get(MRepeater_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MRepeater_.id).get(MRepeaterPK_.repeaterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsRepeatersResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsRepeatersResultData> getResultList(List<ListSmsRepeatersResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsRepeatersResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public ListSmsRepeatersResultData find(ListSmsRepeatersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(ListSmsRepeatersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public ListSmsRepeatersResultData merge(ListSmsRepeatersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(ListSmsRepeatersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
