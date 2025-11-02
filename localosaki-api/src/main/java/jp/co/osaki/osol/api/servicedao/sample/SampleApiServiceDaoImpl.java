package jp.co.osaki.osol.api.servicedao.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.sample.SampleApiResultData;
import jp.co.osaki.osol.entity.MGenericType;
import jp.co.osaki.osol.entity.MGenericTypePK_;
import jp.co.osaki.osol.entity.MGenericType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * Sample Api ServiceDao クラス.
 *
 * @author take_suzuki
 *
 */
public class SampleApiServiceDaoImpl implements BaseServiceDao<SampleApiResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SampleApiResultData> getResultList(SampleApiResultData t, EntityManager em) {

        String groupCode = t.getGroupCode();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SampleApiResultData> query = cb.createQuery(SampleApiResultData.class);
        Root<MGenericType> mGenericType = query.from(MGenericType.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(cb.equal(mGenericType.get(MGenericType_.id).get(MGenericTypePK_.groupCode), groupCode));

        query = query.select(cb.construct(SampleApiResultData.class,
                mGenericType.get(MGenericType_.id).get(MGenericTypePK_.groupCode),
                mGenericType.get(MGenericType_.id).get(MGenericTypePK_.kbnCode),
                mGenericType.get(MGenericType_.groupName),
                mGenericType.get(MGenericType_.kbnName)))
                .where(cb.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SampleApiResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SampleApiResultData> getResultList(List<SampleApiResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SampleApiResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SampleApiResultData find(SampleApiResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SampleApiResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SampleApiResultData merge(SampleApiResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SampleApiResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
