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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.energy.verify.DemandOrgKensyoVerifyResult;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.MSmControlLoad;
import jp.co.osaki.osol.entity.MSmControlLoad_;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify_;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.entity.MSmLineVerify_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 *
 * @author n-takada
 */
public class DemandOrgKensyoVerifyServiceDaoImpl implements BaseServiceDao<DemandOrgKensyoVerifyResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoVerifyResult> getResultList(DemandOrgKensyoVerifyResult t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        Long lineGroupId = t.getLineGroupId();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DemandOrgKensyoVerifyResult> query = builder.createQuery(DemandOrgKensyoVerifyResult.class);

        Root<MSmLineControlLoadVerify> root = query.from(MSmLineControlLoadVerify.class);
        Join<MSmLineControlLoadVerify, MSmControlLoad> joinMSmLineControlLoad
                = root.join(MSmLineControlLoadVerify_.MSmControlLoad);
        Join<MSmLineControlLoadVerify, MSmLineVerify> joinMSmLineVerify
                = root.join(MSmLineControlLoadVerify_.MSmLineVerify);
        Join<MSmLineVerify, MLine> joinMLine = joinMSmLineVerify.join(MSmLineVerify_.MLine);
        Join<MLine, MLineType> joinMLineType = joinMLine.join(MLine_.MLineType);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.corpId), corpId));
        whereList.add(builder.equal(
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.buildingId), buildingId));

        if(lineGroupId != null){
            whereList.add(builder.equal(
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineGroupId), lineGroupId));
        }

        whereList.add(builder.notEqual(root.get(MSmLineControlLoadVerify_.delFlg), 1));
        whereList.add(builder.notEqual(joinMSmLineControlLoad.get(MSmControlLoad_.delFlg), 1));
        whereList.add(builder.equal(joinMLineType.get(MLineType_.dmValidFlg), 1));

        query = query.select(builder.construct(DemandOrgKensyoVerifyResult.class,
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.controlLoad),
                joinMSmLineControlLoad.get(MSmControlLoad_.controlLoadName),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineNo),
                root.get(MSmLineControlLoadVerify_.dmLoadShutOffCapacity)))
                .where(builder.and(whereList.toArray(new Predicate[]{})))
                .orderBy(builder.asc(joinMSmLineControlLoad.get(MSmControlLoad_.controlLoadName)),
                        builder.asc(root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineNo)));

        List<DemandOrgKensyoVerifyResult> lineNoList = em.createQuery(query).getResultList();

        return lineNoList;
    }

    @Override
    public List<DemandOrgKensyoVerifyResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoVerifyResult> getResultList(List<DemandOrgKensyoVerifyResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DemandOrgKensyoVerifyResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoVerifyResult find(DemandOrgKensyoVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(DemandOrgKensyoVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DemandOrgKensyoVerifyResult merge(DemandOrgKensyoVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(DemandOrgKensyoVerifyResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
