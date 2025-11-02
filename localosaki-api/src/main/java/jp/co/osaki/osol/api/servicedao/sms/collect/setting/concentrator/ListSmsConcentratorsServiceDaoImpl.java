/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.concentrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.osol.entity.MConcentrator;
import jp.co.osaki.osol.entity.MConcentratorPK_;
import jp.co.osaki.osol.entity.MConcentrator_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * コンセントレータ管理 コンセントレータ一覧取得 ServiceDaoクラス
 * @author maruta.y
 */
public class ListSmsConcentratorsServiceDaoImpl implements BaseServiceDao<ListSmsConcentratorsResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<ListSmsConcentratorsResultData> getResultList(ListSmsConcentratorsResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsConcentratorsResultData> query = builder.createQuery(ListSmsConcentratorsResultData.class);

        // ■from句の整理
        // [MuDM2]メータ登録用TBL
        Root<MConcentrator> root = query.from(MConcentrator.class);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.devId), target.getDevId()));
        if (target.getConcentId() != null) {
            whereList.add(builder.equal(root.get(MConcentrator_.id).get(MConcentratorPK_.concentId), target.getConcentId()));
        }

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsConcentratorsResultData.class,
                root.get(MConcentrator_.id).get(MConcentratorPK_.concentId),
                root.get(MConcentrator_.id).get(MConcentratorPK_.devId),
                root.get(MConcentrator_.recDate),
                root.get(MConcentrator_.recMan),
                root.get(MConcentrator_.commandFlg),
                root.get(MConcentrator_.srvEnt),
                root.get(MConcentrator_.ifType),
                root.get(MConcentrator_.ipAddr),
                root.get(MConcentrator_.concentSta),
                root.get(MConcentrator_.name),
                root.get(MConcentrator_.memo),
                root.get(MConcentrator_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MConcentrator_.id).get(MConcentratorPK_.concentId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsConcentratorsResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsConcentratorsResultData> getResultList(List<ListSmsConcentratorsResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsConcentratorsResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public ListSmsConcentratorsResultData find(ListSmsConcentratorsResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(ListSmsConcentratorsResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public ListSmsConcentratorsResultData merge(ListSmsConcentratorsResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(ListSmsConcentratorsResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
