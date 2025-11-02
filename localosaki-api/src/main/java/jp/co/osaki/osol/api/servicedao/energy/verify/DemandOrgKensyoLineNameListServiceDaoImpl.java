/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.energy.verify.DemandOrgKensyoNameListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineGroup;
import jp.co.osaki.osol.entity.MLineGroupPK_;
import jp.co.osaki.osol.entity.MLineGroup_;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class DemandOrgKensyoLineNameListServiceDaoImpl implements BaseServiceDao<DemandOrgKensyoNameListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(List<DemandOrgKensyoNameListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoNameListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoNameListResult find(DemandOrgKensyoNameListResult t, EntityManager em) {

        DemandOrgKensyoNameListResult resultSet = new DemandOrgKensyoNameListResult();
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String lineGroupType = t.getLineGroupType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LineListDetailResultData> query = builder.createQuery(LineListDetailResultData.class);

        Root<MLineGroup> root = query.from(MLineGroup.class);
        Join<MLineGroup, MLine> joinMLine = root.join(MLineGroup_.MLines, JoinType.INNER);
        Join<MLine, MLineGroup> joinMLineGroup = joinMLine.join(MLine_.MLineGroup, JoinType.INNER);
        Join<MLine, MLineType> joinMLineType = joinMLine.join(MLine_.MLineType, JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();
        //企業ID
        whereList.add(builder.equal(root.get(MLineGroup_.id).get(MLineGroupPK_.corpId), corpId));
        whereList.add(builder.or(builder.equal(root.get(MLineGroup_.buildingId), buildingId),
                builder.isNull(root.get(MLineGroup_.buildingId))));
        whereList.add(builder.notEqual(root.get(MLineGroup_.delFlg), 1));
        if (!CheckUtility.isNullOrEmpty(lineGroupType)) {
            whereList.add(builder.equal(joinMLineGroup.get(MLineGroup_.lineGroupType), lineGroupType));
        }

        query = query.select(builder.construct(LineListDetailResultData.class,
                joinMLine.get(MLine_.id).get(MLinePK_.corpId),
                joinMLine.get(MLine_.id).get(MLinePK_.lineGroupId),
                joinMLine.get(MLine_.id).get(MLinePK_.lineNo),
                joinMLine.get(MLine_.lineName),
                builder.coalesce(joinMLine.get(MLine_.lineUnit), ""),
                joinMLine.get(MLine_.lineTarget),
                joinMLineType.get(MLineType_.lineType),
                joinMLineType.get(MLineType_.dmValidFlg),
                joinMLineType.get(MLineType_.eventValidFlg),
                joinMLineType.get(MLineType_.airValidFlg),
                joinMLine.get(MLine_.lineEnableFlg),
                joinMLine.get(MLine_.delFlg),
                joinMLine.get(MLine_.version)))
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(joinMLine.get(MLine_.id).get(MLinePK_.lineNo)));

        List<LineListDetailResultData> lineResultSetList = em.createQuery(query).getResultList();
        resultSet.setmLineList(lineResultSetList);

        return resultSet;
    }

    @Override
    public void persist(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoNameListResult merge(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandOrgKensyoNameListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
