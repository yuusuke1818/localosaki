package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.SensorNameExtractResultData;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingName;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingNamePK_;
import jp.co.osaki.osol.entity.TAielMasterAreaSettingName_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * センサ名称取得 ServiceDaoクラス
 * @author s-sunada
 *
 */
public class SensorNameListServiceDaoImpl implements BaseServiceDao<SensorNameExtractResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SensorNameExtractResultData> getResultList(SensorNameExtractResultData target, EntityManager em) {
        Long smId = target.getSmId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SensorNameExtractResultData> query = builder.createQuery(SensorNameExtractResultData.class);

        Root<TAielMasterAreaSettingName> root = query.from(TAielMasterAreaSettingName.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(TAielMasterAreaSettingName_.id).get(TAielMasterAreaSettingNamePK_.smId), smId));

        query = query.select(builder.construct(SensorNameExtractResultData.class,
                root.get(TAielMasterAreaSettingName_.id).get(TAielMasterAreaSettingNamePK_.areaNo),
                root.get(TAielMasterAreaSettingName_.sensorName1),
                root.get(TAielMasterAreaSettingName_.sensorName2),
                root.get(TAielMasterAreaSettingName_.sensorName3),
                root.get(TAielMasterAreaSettingName_.sensorName4)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SensorNameExtractResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SensorNameExtractResultData> getResultList(List<SensorNameExtractResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SensorNameExtractResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SensorNameExtractResultData find(SensorNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SensorNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SensorNameExtractResultData merge(SensorNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SensorNameExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
