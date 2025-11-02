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
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.entity.MLineTimeStandard;
import jp.co.osaki.osol.entity.MLineTimeStandardPK_;
import jp.co.osaki.osol.entity.MLineTimeStandard_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物系統時限標準値取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class BuildingLineTimeStandardListServiceDaoImpl
        implements BaseServiceDao<BuildingLineTimeStandardListTimeResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingLineTimeStandardListTimeResultData> getResultList(BuildingLineTimeStandardListTimeResultData t,
            EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<BuildingLineTimeStandardListTimeResultData> query = builder
                .createQuery(BuildingLineTimeStandardListTimeResultData.class);

        Root<MLineTimeStandard> root = query.from(MLineTimeStandard.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.corpId), corpId));

        //建物ID
        whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.buildingId), buildingId));

        //系統グループID
        whereList
                .add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineGroupId), lineGroupId));

        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineNo), lineNo));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MLineTimeStandard_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(BuildingLineTimeStandardListTimeResultData.class,
                root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.corpId),
                root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.buildingId),
                root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineGroupId),
                root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.lineNo),
                root.get(MLineTimeStandard_.id).get(MLineTimeStandardPK_.jigenNo),
                root.get(MLineTimeStandard_.lineLimitStandardKw),
                root.get(MLineTimeStandard_.lineLowerStandardKw),
                root.get(MLineTimeStandard_.delFlg),
                root.get(MLineTimeStandard_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<BuildingLineTimeStandardListTimeResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingLineTimeStandardListTimeResultData> getResultList(
            List<BuildingLineTimeStandardListTimeResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingLineTimeStandardListTimeResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingLineTimeStandardListTimeResultData find(BuildingLineTimeStandardListTimeResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingLineTimeStandardListTimeResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingLineTimeStandardListTimeResultData merge(BuildingLineTimeStandardListTimeResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(BuildingLineTimeStandardListTimeResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
