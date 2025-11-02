package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterTenant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sms.meterTenant.MeterGroupNameResultData;
import jp.co.osaki.osol.entity.MMeterGroupName;
import jp.co.osaki.osol.entity.MMeterGroupNamePK_;
import jp.co.osaki.osol.entity.MMeterGroupName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * メーターグループ名称設定
 * @author nishida.t
 *
 */
public class MeterGroupNameServiceDaoImpl implements BaseServiceDao<MeterGroupNameResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<MeterGroupNameResultData> getResultList(MeterGroupNameResultData target, EntityManager em) {
      String corpId = target.getCorpId();
      Long buildingId = target.getBuildingId();

      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<MeterGroupNameResultData> query = builder.createQuery(MeterGroupNameResultData.class);

      Root<MMeterGroupName> root = query.from(MMeterGroupName.class);

      List<Predicate> whereList = new ArrayList<>();

      // 企業ID
      whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId), corpId));
      // 建物ID
      whereList.add(builder.equal(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId), buildingId));

      query = query.select(builder.construct(MeterGroupNameResultData.class,
              root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId),
              root.get(MMeterGroupName_.meterGroupName)))
              .where(builder.and(whereList.toArray(new Predicate[] {})))
              .orderBy(
                      builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId)));

        /*      query = query.select(root)
              .where(builder.and(whereList.toArray(new Predicate[]{})))
              .orderBy(
                      builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.corpId)),
                      builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.buildingId)),
                      builder.asc(root.get(MMeterGroupName_.id).get(MMeterGroupNamePK_.meterGroupId))
              );*/

      return em.createQuery(query).getResultList();
    }

    @Override
    public List<MeterGroupNameResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterGroupNameResultData> getResultList(List<MeterGroupNameResultData> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<MeterGroupNameResultData> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public MeterGroupNameResultData find(MeterGroupNameResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void persist(MeterGroupNameResultData target, EntityManager em) {

    }

    @Override
    public MeterGroupNameResultData merge(MeterGroupNameResultData target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(MeterGroupNameResultData target, EntityManager em) {

    }

}
