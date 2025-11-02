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

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMetersResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK_;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK_;
import jp.co.osaki.osol.entity.MMeter_;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ管理 メーター一覧取得 ServiceDaoクラス
 * @author maruta.y
 */
public class ListSmsMetersServiceDaoImpl implements BaseServiceDao<ListSmsMetersResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return 0;
    }

    @Override
    public List<ListSmsMetersResultData> getResultList(ListSmsMetersResultData target, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMetersResultData> query = builder.createQuery(ListSmsMetersResultData.class);

        // ■from句の整理
        // [MuDM2]メータ登録用TBL
        Root<MDevRelation> root = query.from(MDevRelation.class);
        Join<MDevRelation, MDevPrm> joinMDevPrm = root.join(MDevRelation_.MDevPrm , JoinType.INNER);
        Join<MDevPrm, MMeter> joinMMeters = joinMDevPrm.join(MDevPrm_.MMeters , JoinType.INNER);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.corpId), target.getCorpId()));
        whereList.add(builder.equal(root.get(MDevRelation_.id).get(MDevRelationPK_.buildingId), target.getBuildingId()));
        whereList.add(builder.equal(joinMMeters.get(MMeter_.delFlg), 0));

        // ■SELECT句の整理
        query = query.select(builder.construct(ListSmsMetersResultData.class,
                root.get(MDevRelation_.TBuilding).get(TBuilding_.id).get(TBuildingPK_.corpId),
                root.get(MDevRelation_.TBuilding).get(TBuilding_.id).get(TBuildingPK_.buildingId),
                joinMMeters.get(MMeter_.id).get(MMeterPK_.meterMngId),
                joinMMeters.get(MMeter_.id).get(MMeterPK_.devId))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        // ■ORDER BY句の整理
        query.orderBy(builder.asc(joinMMeters.get(MMeter_.id).get(MMeterPK_.meterMngId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMetersResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsMetersResultData> getResultList(List<ListSmsMetersResultData> entityList, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public List<ListSmsMetersResultData> getResultList(EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public ListSmsMetersResultData find(ListSmsMetersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void persist(ListSmsMetersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public ListSmsMetersResultData merge(ListSmsMetersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public void remove(ListSmsMetersResultData target, EntityManager em) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
