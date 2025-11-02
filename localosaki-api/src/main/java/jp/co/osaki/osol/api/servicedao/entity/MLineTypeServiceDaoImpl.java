package jp.co.osaki.osol.api.servicedao.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統種別
 *
 * @author t_hirata
 */
public class MLineTypeServiceDaoImpl implements BaseServiceDao<MLineType> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineType> getResultList(MLineType t, EntityManager em) {
        // パラメータ
        String lineType = t.getLineType();
        //
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MLineType> query = builder.createQuery(MLineType.class);

        Root<MLineType> root = query.from(MLineType.class);
        List<Predicate> whereList = new ArrayList<>();

        // 条件
        if (lineType != null) {
            whereList.add(builder.equal(root.get(MLineType_.lineType), lineType));
        }
        whereList.add(builder.equal(root.get(MLineType_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MLineType> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineType> getResultList(List<MLineType> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MLineType> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineType find(MLineType t, EntityManager em) {

        MLineType ret = em.find(MLineType.class, t.getLineType());

        return ret;
    }

    @Override
    public void persist(MLineType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MLineType merge(MLineType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MLineType t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
