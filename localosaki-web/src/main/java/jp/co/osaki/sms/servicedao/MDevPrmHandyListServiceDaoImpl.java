package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 装置情報ServiceDaoクラス.
 *
 * @author yoneda_y
 */
public class MDevPrmHandyListServiceDaoImpl implements BaseServiceDao<MDevPrm> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(MDevPrm target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<MDevPrm> query = builder
                .createQuery(MDevPrm.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> whereList = new ArrayList<>();

        //MUDM2のみ取得(DEV_KINDが1)
        whereList.add(builder.equal(root.get(MDevPrm_.devKind), "1"));

        //ハンディ端末(MHから始まる装置ID)
        whereList.add(builder.like(root.get(MDevPrm_.devId), "MH%"));

        query = query.select(root)
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MDevPrm_.devId)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<MDevPrm> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(List<MDevPrm> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MDevPrm> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm find(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MDevPrm merge(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MDevPrm target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
