package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MCoefficientHistoryManage;
import jp.co.osaki.osol.entity.MCoefficientHistoryManagePK_;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author t_hirata
 */
public class MCoefficientHistoryManageServiceDaoImpl implements BaseServiceDao<MCoefficientHistoryManage> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCoefficientHistoryManage> getResultList(MCoefficientHistoryManage t, EntityManager em) {

        // パラメータ
        String engTypeCd = t.getId().getEngTypeCd();
        Long engId = t.getId().getEngId();
        String dayAndNightType = t.getId().getDayAndNightType();

        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MCoefficientHistoryManage> query = builder.createQuery(MCoefficientHistoryManage.class);

        Root<MCoefficientHistoryManage> root = query.from(MCoefficientHistoryManage.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        if (engTypeCd != null) {
            whereList.add(builder.equal(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engTypeCd), engTypeCd));
        }
        if (engId != null) {
            whereList.add(builder.equal(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engId), engId));
        }
        if (dayAndNightType != null) {
            whereList.add(builder.equal(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.dayAndNightType), dayAndNightType));
        }

        // ソート
        List<Order> orderList = new ArrayList<>();
        orderList.add(builder.desc(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engTypeCd)));
        orderList.add(builder.desc(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engId)));
        orderList.add(builder.desc(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.dayAndNightType)));
        orderList.add(builder.desc(root.get(MCoefficientHistoryManage_.startYm)));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(orderList);

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MCoefficientHistoryManage> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCoefficientHistoryManage> getResultList(List<MCoefficientHistoryManage> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCoefficientHistoryManage> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCoefficientHistoryManage find(MCoefficientHistoryManage t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MCoefficientHistoryManage t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCoefficientHistoryManage merge(MCoefficientHistoryManage t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MCoefficientHistoryManage t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
