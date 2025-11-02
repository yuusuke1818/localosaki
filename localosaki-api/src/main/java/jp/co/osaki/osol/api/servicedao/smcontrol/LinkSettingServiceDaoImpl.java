package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;
import jp.co.osaki.osol.entity.MSmAirSetting;
import jp.co.osaki.osol.entity.MSmAirSettingPK_;
import jp.co.osaki.osol.entity.MSmAirSetting_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器空調設定情報取得
 * @author ya-ishida
 *
 */
public class LinkSettingServiceDaoImpl implements BaseServiceDao<LinkSettingExtractResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkSettingExtractResultData> getResultList(LinkSettingExtractResultData target, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LinkSettingExtractResultData> query = builder.createQuery(LinkSettingExtractResultData.class);

        Root<MSmAirSetting> root = query.from(MSmAirSetting.class);

        List<Predicate> whereList = new ArrayList<>();

        //機器ID
        whereList.add(builder.equal(root.get(MSmAirSetting_.id).get(MSmAirSettingPK_.smId), target.getSmId()));

        query = query.select(builder.construct(LinkSettingExtractResultData.class,
                root.get(MSmAirSetting_.id).get(MSmAirSettingPK_.smId),
                root.get(MSmAirSetting_.id).get(MSmAirSettingPK_.outputPortNo),
                root.get(MSmAirSetting_.linkOutputPortNo),
                root.get(MSmAirSetting_.coolingDifferential),
                root.get(MSmAirSetting_.heatingDifferential),
                root.get(MSmAirSetting_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LinkSettingExtractResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkSettingExtractResultData> getResultList(List<LinkSettingExtractResultData> entityList,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkSettingExtractResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkSettingExtractResultData find(LinkSettingExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(LinkSettingExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkSettingExtractResultData merge(LinkSettingExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(LinkSettingExtractResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
