package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

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

import jp.co.osaki.osol.entity.MGroupPrice;
import jp.co.osaki.osol.entity.MGroupPricePK_;
import jp.co.osaki.osol.entity.MGroupPrice_;
import jp.co.osaki.osol.entity.MMeterGroupName;
import jp.co.osaki.osol.entity.MMeterGroupNamePK_;
import jp.co.osaki.osol.entity.MMeterGroupName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class ListSmsMeterGroupServiceDaoImpl implements BaseServiceDao<ListSmsMeterGroup> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {

        return 0;
    }

    @Override
    public List<ListSmsMeterGroup> getResultList(ListSmsMeterGroup target, EntityManager em) {

        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        String inspDate = String.format("%04d%02d", Integer.parseInt(target.getYear()),
                Integer.parseInt(target.getMonth()));

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsMeterGroup> query = builder.createQuery(ListSmsMeterGroup.class);
        Root<MMeterGroupName> root = query.from(MMeterGroupName.class);
        Join<MMeterGroupName, MGroupPrice> joinGroupPrice = root.join(MMeterGroupName_.MGroupPrices, JoinType.LEFT);

        List<Predicate> whereList = new ArrayList<>();

        // 結合条件
        joinGroupPrice.on(
                builder.and(
                        builder.equal(
                                root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId),
                                joinGroupPrice.get(MGroupPrice_.id).get(MGroupPricePK_.buildingId))));

        //企業ID
        whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId), buildingId));
        //集計年月
        whereList.add(builder.equal(joinGroupPrice.get(MGroupPrice_.id).get(MGroupPricePK_.latestInspDate), inspDate));

        query = query.select(builder.construct(ListSmsMeterGroup.class,
                root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId),
                root.get(MMeterGroupName_.meterGroupName),
                joinGroupPrice.get(MGroupPrice_.groupPrice)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .distinct(true)
                .orderBy(builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterGroup> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ListSmsMeterGroup> getResultList(List<ListSmsMeterGroup> entityList, EntityManager em) {

        return null;
    }

    @Override
    public List<ListSmsMeterGroup> getResultList(EntityManager em) {

        return null;
    }

    @Override
    public ListSmsMeterGroup find(ListSmsMeterGroup target, EntityManager em) {

        return null;
    }

    @Override
    public void persist(ListSmsMeterGroup target, EntityManager em) {

    }

    @Override
    public ListSmsMeterGroup merge(ListSmsMeterGroup target, EntityManager em) {

        return null;
    }

    @Override
    public void remove(ListSmsMeterGroup target, EntityManager em) {

    }
}
