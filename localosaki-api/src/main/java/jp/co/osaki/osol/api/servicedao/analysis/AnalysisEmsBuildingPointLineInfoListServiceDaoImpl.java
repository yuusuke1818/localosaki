/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointLineInfoListResult;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLine_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 共通：デマンド日報ポイントテーブルデータ取得用 ServiceDaoクラス
 *
 * @author yonezawa.a
 */
public class AnalysisEmsBuildingPointLineInfoListServiceDaoImpl implements BaseServiceDao<AnalysisEmsBuildingPointLineInfoListResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointLineInfoListResult> getResultList(AnalysisEmsBuildingPointLineInfoListResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long lineGroupId = t.getLineGroupId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AnalysisEmsBuildingPointLineInfoListResult> query = builder
                .createQuery(AnalysisEmsBuildingPointLineInfoListResult.class);

        Root<MLine> root = query.from(MLine.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(MLine_.id).get(MLinePK_.corpId), corpId));
        whereList.add(builder.equal(root.get(MLine_.id).get(MLinePK_.lineGroupId), lineGroupId));
        whereList.add(builder.equal(root.get(MLine_.lineEnableFlg), OsolConstants.FLG_ON));
        whereList.add(builder.equal(root.get(MLine_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(AnalysisEmsBuildingPointLineInfoListResult.class,
                root.get(MLine_.id).get(MLinePK_.lineNo), root.get(MLine_.lineName)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MLine_.id).get(MLinePK_.lineNo)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AnalysisEmsBuildingPointLineInfoListResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointLineInfoListResult> getResultList(List<AnalysisEmsBuildingPointLineInfoListResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnalysisEmsBuildingPointLineInfoListResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsBuildingPointLineInfoListResult find(AnalysisEmsBuildingPointLineInfoListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AnalysisEmsBuildingPointLineInfoListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AnalysisEmsBuildingPointLineInfoListResult merge(AnalysisEmsBuildingPointLineInfoListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AnalysisEmsBuildingPointLineInfoListResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
