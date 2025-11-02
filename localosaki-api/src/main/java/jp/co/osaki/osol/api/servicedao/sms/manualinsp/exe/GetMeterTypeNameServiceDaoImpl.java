package jp.co.osaki.osol.api.servicedao.sms.manualinsp.exe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelation_;
import jp.co.osaki.osol.entity.MMeterType;
import jp.co.osaki.osol.entity.MMeterTypePK_;
import jp.co.osaki.osol.entity.MMeterType_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GetMeterTypeNameServiceDaoImpl implements BaseServiceDao<String> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getResultList(String target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    // 【生成したいSQL】
//  SELECT
//      a.meter_type_name
//    FROM
//      m_meter_type a
//    INNER JOIN
//      t_building b on a.corp_id = b.corp_id AND a.building_id = b.building_id
//    INNER JOIN
//      m_dev_relation c on b.corp_id = c.corp_id AND b.building_id = c.building_id
//    INNER JOIN
//      m_dev_prm d on c.dev_id = d.dev_id
//    WHERE
//      d.dev_id = '' /* $1 */
//      AND d.dev_kind = '' /* $3 */
//      AND a.meter_type = '' /* $4 */
//    GROUP BY
//      a.meter_type_name
    @Override
    public List<String> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);

        // ■from句の整理
        // [MuDM2] メーター種別設定
        Root<MMeterType> root = query.from(MMeterType.class);
        Join<MMeterType, TBuilding> joinBuiding = root
                .join(MMeterType_.TBuilding);
        Join<TBuilding, MDevRelation> joinDevRel = joinBuiding
                .join(TBuilding_.MDevRelations);
        Join<MDevRelation, MDevPrm> joinPrm = joinDevRel
                .join(MDevRelation_.MDevPrm);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        List<Object> paramList = parameterMap.get("dev_id");
        if (paramList != null) {
            String devId = (String)paramList.get(0);
            whereList.add(builder.equal(joinPrm.get(MDevPrm_.devId),devId ));
        }
        paramList = parameterMap.get("dev_kind");
        if (paramList != null) {
            String devKind = (String)paramList.get(0);
            whereList.add(builder.equal(joinPrm.get(MDevPrm_.devKind),devKind ));
        }
        paramList = parameterMap.get("meter_type");
        if (paramList != null) {
            Long meterType = (Long)paramList.get(0);
            whereList.add(builder.equal(root.get(MMeterType_.id).get(MMeterTypePK_.meterType),meterType ));
        }

        // ■SELECT句の整理
        query = query.select(builder.construct(String.class,
                root.get(MMeterType_.meterTypeName)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        // ■GOUP BY句の整理
        query.groupBy(root.get(MMeterType_.meterTypeName));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<String> getResultList(List<String> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String find(String target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(String target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String merge(String target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
    }

}
