/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphSettingListDetailResultData;
import jp.co.osaki.osol.entity.MGraph;
import jp.co.osaki.osol.entity.MGraphPK_;
import jp.co.osaki.osol.entity.MGraph_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * グラフ設定情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class DemandGraphSettingListServiceDaoImpl implements BaseServiceDao<DemandGraphSettingListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphSettingListDetailResultData> getResultList(DemandGraphSettingListDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();
        Long graphId = t.getGraphId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandGraphSettingListDetailResultData> query = builder
                .createQuery(DemandGraphSettingListDetailResultData.class);

        Root<MGraph> root = query.from(MGraph.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.buildingId), buildingId));
        //系統グループID
        whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.lineGroupId), lineGroupId));
        //グラフID
        if (graphId != null) {
            whereList.add(builder.equal(root.get(MGraph_.id).get(MGraphPK_.graphId), graphId));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MGraph_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(DemandGraphSettingListDetailResultData.class,
                root.get(MGraph_.id).get(MGraphPK_.corpId),
                root.get(MGraph_.id).get(MGraphPK_.buildingId),
                root.get(MGraph_.id).get(MGraphPK_.lineGroupId),
                root.get(MGraph_.id).get(MGraphPK_.graphId),
                root.get(MGraph_.graphName),
                root.get(MGraph_.initialViewFlg),
                root.get(MGraph_.delFlg),
                root.get(MGraph_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DemandGraphSettingListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphSettingListDetailResultData> getResultList(List<DemandGraphSettingListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphSettingListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandGraphSettingListDetailResultData find(DemandGraphSettingListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandGraphSettingListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandGraphSettingListDetailResultData merge(DemandGraphSettingListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandGraphSettingListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
