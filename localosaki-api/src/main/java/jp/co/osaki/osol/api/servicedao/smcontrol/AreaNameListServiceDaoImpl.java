package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingName;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingNamePK_;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * エリア名称,センサ名称取得 ServiceDaoクラス
 * @author s-sunada
 *
 */
public class AreaNameListServiceDaoImpl implements BaseServiceDao<AreaNameExtractResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AreaNameExtractResultData> getResultList(AreaNameExtractResultData target, EntityManager em) {
        Long smId = target.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AreaNameExtractResultData> query = builder.createQuery(AreaNameExtractResultData.class);

        Root<TAielMasterAreaSettingName> root = query.from(TAielMasterAreaSettingName.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(TAielMasterAreaSettingName_.id).get(TAielMasterAreaSettingNamePK_.smId), smId));

        query = query.select(builder.construct(AreaNameExtractResultData.class,
                root.get(TAielMasterAreaSettingName_.id).get(TAielMasterAreaSettingNamePK_.areaNo),
                root.get(TAielMasterAreaSettingName_.areaName),
                root.get(TAielMasterAreaSettingName_.sensorName1),
                root.get(TAielMasterAreaSettingName_.sensorName2),
                root.get(TAielMasterAreaSettingName_.sensorName3),
                root.get(TAielMasterAreaSettingName_.sensorName4),
                root.get(TAielMasterAreaSettingName_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(TAielMasterAreaSettingName_.id)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AreaNameExtractResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AreaNameExtractResultData> getResultList(List<AreaNameExtractResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AreaNameExtractResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AreaNameExtractResultData find(AreaNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AreaNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AreaNameExtractResultData merge(AreaNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AreaNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
