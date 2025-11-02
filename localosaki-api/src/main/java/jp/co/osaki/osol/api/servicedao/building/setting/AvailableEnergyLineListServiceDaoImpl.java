/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.building.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.building.setting.AvailableEnergyLineListResultData;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.entity.TAvailableEnergyLine;
import jp.co.osaki.osol.entity.TAvailableEnergyLinePK_;
import jp.co.osaki.osol.entity.TAvailableEnergyLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 使用エネルギー系統情報取得 ServiceDaoクラス
 *
 * @author y-maruta
 */
public class AvailableEnergyLineListServiceDaoImpl implements BaseServiceDao<AvailableEnergyLineListResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyLineListResultData> getResultList(AvailableEnergyLineListResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long buildingId = t.getBuildingId();
        String engTypeCd = t.getEngTypeCd();
        Long engId = t.getEngId();
        Long contractId = t.getContractId();
        String lineCorpId = t.getLineCorpId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AvailableEnergyLineListResultData> query = builder.createQuery(AvailableEnergyLineListResultData.class);

        Root<TAvailableEnergyLine> root = query.from(TAvailableEnergyLine.class);
        Join<TAvailableEnergyLine, MLine> joinMLine = root.join(TAvailableEnergyLine_.MLine);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.corpId), corpId));
        //建物ID
        if(buildingId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.buildingId), buildingId));
        }

        //エネルギー種類コード
        if (!CheckUtility.isNullOrEmpty(engTypeCd)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engTypeCd), engTypeCd));
        }
        //エネルギーID
        if (engId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engId), engId));
        }
        //契約ID
        if (contractId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.contractId), contractId));
        }

        //系統企業ID
        if (lineCorpId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.corpId), lineCorpId));
        }

        //系統グループID
        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.lineGroupId), lineGroupId));
        }

        //系統No
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(TAvailableEnergyLine_.MLine).get(MLine_.id).get(MLinePK_.lineNo), lineNo));
        }

        query = query.select(builder.construct(AvailableEnergyLineListResultData.class,
                root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.corpId),
                root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.buildingId),
                root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engTypeCd),
                root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.engId),
                root.get(TAvailableEnergyLine_.id).get(TAvailableEnergyLinePK_.contractId),
                joinMLine.get(MLine_.id).get(MLinePK_.corpId),
                joinMLine.get(MLine_.id).get(MLinePK_.lineGroupId),
                joinMLine.get(MLine_.id).get(MLinePK_.lineNo),
                root.get(TAvailableEnergyLine_.summaryUnit),
                root.get(TAvailableEnergyLine_.startTime),
                root.get(TAvailableEnergyLine_.endTime),
                root.get(TAvailableEnergyLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<AvailableEnergyLineListResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyLineListResultData> getResultList(List<AvailableEnergyLineListResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AvailableEnergyLineListResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AvailableEnergyLineListResultData find(AvailableEnergyLineListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(AvailableEnergyLineListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AvailableEnergyLineListResultData merge(AvailableEnergyLineListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(AvailableEnergyLineListResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
