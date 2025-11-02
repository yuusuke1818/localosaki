package jp.co.osaki.osol.api.servicedao.energy.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmLineControlLoadVerifyListDetailResultData;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerifyPK_;
import jp.co.osaki.osol.entity.MSmLineControlLoadVerify_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 機器系統制御負荷検証取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class SmLineControlLoadVerifyListServiceDaoImpl
        implements BaseServiceDao<SmLineControlLoadVerifyListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLineControlLoadVerifyListDetailResultData> getResultList(
            SmLineControlLoadVerifyListDetailResultData target, EntityManager em) {
        String corpId = target.getCorpId();
        Long buildingId = target.getBuildingId();
        Long lineGroupId = target.getLineGroupId();
        Long smId = target.getSmId();
        String lineNo = target.getLineNo();
        BigDecimal controlLoad = target.getControlLoad();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SmLineControlLoadVerifyListDetailResultData> query = builder
                .createQuery(SmLineControlLoadVerifyListDetailResultData.class);

        Root<MSmLineControlLoadVerify> root = query.from(MSmLineControlLoadVerify.class);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(
                builder.equal(root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.corpId), corpId));
        //建物ID
        if (buildingId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.buildingId), buildingId));
        }
        //系統グループID
        if (lineGroupId != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineGroupId), lineGroupId));
        }
        //機器ID
        if (smId != null) {
            whereList.add(
                    builder.equal(root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.smId), smId));
        }

        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineNo),
                    lineNo));
        }

        //制御負荷
        if (controlLoad != null) {
            whereList.add(builder.equal(
                    root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.controlLoad), controlLoad));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MSmLineControlLoadVerify_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SmLineControlLoadVerifyListDetailResultData.class,
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.corpId),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineGroupId),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.lineNo),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.buildingId),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.smId),
                root.get(MSmLineControlLoadVerify_.id).get(MSmLineControlLoadVerifyPK_.controlLoad),
                root.get(MSmLineControlLoadVerify_.dmLoadShutOffCapacity),
                root.get(MSmLineControlLoadVerify_.event1LoadShutOffCapacity),
                root.get(MSmLineControlLoadVerify_.event2LoadShutOffCapacity),
                root.get(MSmLineControlLoadVerify_.delFlg),
                root.get(MSmLineControlLoadVerify_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<SmLineControlLoadVerifyListDetailResultData> getResultList(Map<String, List<Object>> parameterMap,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLineControlLoadVerifyListDetailResultData> getResultList(
            List<SmLineControlLoadVerifyListDetailResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SmLineControlLoadVerifyListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLineControlLoadVerifyListDetailResultData find(SmLineControlLoadVerifyListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SmLineControlLoadVerifyListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SmLineControlLoadVerifyListDetailResultData merge(SmLineControlLoadVerifyListDetailResultData target,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SmLineControlLoadVerifyListDetailResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
