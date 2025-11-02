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
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandGraphElementListDetailResultData;
import jp.co.osaki.osol.entity.MGraphElement;
import jp.co.osaki.osol.entity.MGraphElementPK_;
import jp.co.osaki.osol.entity.MGraphElement_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * グラフ要素設定情報取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class DemandGraphElementListServiceDaoImpl implements BaseServiceDao<DemandGraphElementListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphElementListDetailResultData> getResultList(DemandGraphElementListDetailResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();
        Long graphId = t.getGraphId();
        Long graphElementId = t.getGraphElementId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandGraphElementListDetailResultData> query = builder
                .createQuery(DemandGraphElementListDetailResultData.class);

        Root<MGraphElement> root = query.from(MGraphElement.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.corpId), corpId));
        //建物ID
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId), buildingId));
        //系統グループID
        whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId), lineGroupId));
        //グラフID
        if (graphId != null) {
            whereList.add(builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.graphId), graphId));
        }
        //グラフ要素ID
        if (graphElementId != null) {
            whereList.add(
                    builder.equal(root.get(MGraphElement_.id).get(MGraphElementPK_.graphElementId), graphElementId));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MGraphElement_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(DemandGraphElementListDetailResultData.class,
                root.get(MGraphElement_.id).get(MGraphElementPK_.corpId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.buildingId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.lineGroupId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.graphId),
                root.get(MGraphElement_.id).get(MGraphElementPK_.graphElementId),
                root.get(MGraphElement_.graphElementType),
                root.get(MGraphElement_.graphLineGroupId),
                root.get(MGraphElement_.graphLineNo),
                root.get(MGraphElement_.graphSmId),
                root.get(MGraphElement_.graphPointNo),
                root.get(MGraphElement_.graphColorCode),
                root.get(MGraphElement_.displayOrder),
                root.get(MGraphElement_.delFlg),
                root.get(MGraphElement_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<DemandGraphElementListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphElementListDetailResultData> getResultList(List<DemandGraphElementListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandGraphElementListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandGraphElementListDetailResultData find(DemandGraphElementListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandGraphElementListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandGraphElementListDetailResultData merge(DemandGraphElementListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandGraphElementListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
