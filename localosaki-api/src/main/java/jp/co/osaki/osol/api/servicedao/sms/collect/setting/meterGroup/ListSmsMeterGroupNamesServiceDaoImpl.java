package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResultData;
import jp.co.osaki.osol.entity.MMeterGroupName;
import jp.co.osaki.osol.entity.MMeterGroupNamePK_;
import jp.co.osaki.osol.entity.MMeterGroupName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ名取得
 * @author maruta.y
 *
 */
public class ListSmsMeterGroupNamesServiceDaoImpl implements BaseServiceDao<ListSmsMeterGroupNamesResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<ListSmsMeterGroupNamesResultData> getResultList(ListSmsMeterGroupNamesResultData target, EntityManager em) {
      String corpId = target.getCorpId();
      Long buildingId = target.getBuildingId();

      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<ListSmsMeterGroupNamesResultData> query = builder.createQuery(ListSmsMeterGroupNamesResultData.class);

      Root<MMeterGroupName> root = query.from(MMeterGroupName.class);

      List<Predicate> whereList = new ArrayList<>();

      // 企業ID
      whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId), corpId));
      // 建物ID
      whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId), buildingId));

      query = query.select(builder.construct(ListSmsMeterGroupNamesResultData.class,
              root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId),
              root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId),
              root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId),
              root.get(MMeterGroupName_.meterGroupName),
              root.get(MMeterGroupName_.version)))
              .where(builder.and(whereList.toArray(new Predicate[] {})))
              .orderBy(
                      builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId)));

      return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsMeterGroupNamesResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<ListSmsMeterGroupNamesResultData> getResultList(List<ListSmsMeterGroupNamesResultData> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<ListSmsMeterGroupNamesResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public ListSmsMeterGroupNamesResultData find(ListSmsMeterGroupNamesResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(ListSmsMeterGroupNamesResultData target, EntityManager em) {

    }

    @Override
    public ListSmsMeterGroupNamesResultData merge(ListSmsMeterGroupNamesResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(ListSmsMeterGroupNamesResultData target, EntityManager em) {

    }

}
