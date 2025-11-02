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
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class GetBuildingNameDaoImpl implements BaseServiceDao<String> {

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
//      a.building_name
//    FROM
//      t_building a
//    INNER JOIN
//      m_dev_relation b on b.corp_id = a.corp_id AND b.builing_id = a.building_id
//    INNER JOIN
//      m_dev_prm c on c.dev_id = b.dev_id
//    WHERE
//      c.dev_id = '' /* $1 */
//      c.dev_kind = '1'
    @Override
    public List<String> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        // クエリ生成
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);

        // ■from句の整理
        // 建物
        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MDevRelation> joinDevRel = root
                .join(TBuilding_.MDevRelations);
        Join<MDevRelation, MDevPrm> joinPrm = joinDevRel
                .join(MDevRelation_.MDevPrm);

        // ■WHERE句の整理
        List<Predicate> whereList = new ArrayList<>();
        List<Object> paramList = parameterMap.get("dev_id");
        if (paramList != null) {
            String devId = (String) paramList.get(0);
            whereList.add(builder.equal(joinPrm.get(MDevPrm_.devId), devId));
        }
        whereList.add(builder.equal(joinPrm.get(MDevPrm_.devKind), "1"));

        // ■SELECT句の整理
        query = query.select(builder.construct(String.class,
                root.get(TBuilding_.buildingName)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

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
