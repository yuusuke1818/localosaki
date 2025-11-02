/**
 *
 */
package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK_;
import jp.co.osaki.osol.entity.MMeterGroup_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ管理 メーターグループ一覧取得 ServiceDaoクラス
 * @author maruta.y
 */
public class ListSmsMeterGroupsServiceDaoImpl implements BaseServiceDao<GetSmsMeterGroupResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<GetSmsMeterGroupResultData> getResultList(GetSmsMeterGroupResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GetSmsMeterGroupResultData> query = builder.createQuery(GetSmsMeterGroupResultData.class);

        // ■from句の整理
        // [MuDM2]メータ登録用TBL
        Root<MMeterGroup> root = query.from(MMeterGroup.class);
        Join<MMeterGroup, MMeter> joinMMeter = root.join(MMeterGroup_.MMeter , JoinType.INNER);
        Join<MMeter, MDevPrm> joinMDevPrm = joinMMeter.join(MMeter_.MDevPrm , JoinType.INNER);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.corpId), target.getCorpId()));
        whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.buildingId), target.getBuildingId()));
        if (target.getMeterGroupId() != null) {
            whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterGroupId), target.getMeterGroupId()));
        }
        if (target.getMeterMngId() != null) {
            whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterMngId), target.getMeterMngId()));
        }
        if (target.getDevId() != null && !target.getDevId().isEmpty()) {
            whereList.add(builder.equal(root.get(MMeterGroup_.id).get(MMeterGroupPK_.devId), target.getDevId()));
        }

        // ■SELECT句の整理
        query = query.select(builder.construct(GetSmsMeterGroupResultData.class,
                root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterGroupId),
                root.get(MMeterGroup_.id).get(MMeterGroupPK_.corpId),
                root.get(MMeterGroup_.id).get(MMeterGroupPK_.buildingId),
                root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterMngId),
                root.get(MMeterGroup_.id).get(MMeterGroupPK_.devId),
                joinMDevPrm.get(MDevPrm_.name),
                root.get(MMeterGroup_.calcType),
                root.get(MMeterGroup_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(root.get(MMeterGroup_.id).get(MMeterGroupPK_.devId)),
                builder.asc(root.get(MMeterGroup_.id).get(MMeterGroupPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<GetSmsMeterGroupResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<GetSmsMeterGroupResultData> getResultList(List<GetSmsMeterGroupResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<GetSmsMeterGroupResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public GetSmsMeterGroupResultData find(GetSmsMeterGroupResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(GetSmsMeterGroupResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public GetSmsMeterGroupResultData merge(GetSmsMeterGroupResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(GetSmsMeterGroupResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
