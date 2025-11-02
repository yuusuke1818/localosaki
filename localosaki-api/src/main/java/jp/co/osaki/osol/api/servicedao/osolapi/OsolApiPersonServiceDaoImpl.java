/**
 *
 */
package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * @author take_suzuki
 *
 */
public class OsolApiPersonServiceDaoImpl implements BaseServiceDao<MPerson> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(List<MPerson> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MPerson> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPerson find(MPerson target, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MPerson> cq = cb.createQuery(MPerson.class);
        Root<MPerson> root = cq.from(MPerson.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(root.get(MPerson_.id).get(MPersonPK_.corpId), target.getId().getCorpId()));
        whereList.add(cb.equal(root.get(MPerson_.id).get(MPersonPK_.personId), target.getId().getPersonId()));
        whereList.add(cb.equal(root.get(MPerson_.accountStopFlg), 0));
        whereList.add(cb.equal(root.get(MPerson_.delFlg), 0));
        cq = cq.select(root).where(cb.and(whereList.toArray(new Predicate[]{})));

        List<MPerson> list = em.createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void persist(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MPerson merge(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MPerson target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
