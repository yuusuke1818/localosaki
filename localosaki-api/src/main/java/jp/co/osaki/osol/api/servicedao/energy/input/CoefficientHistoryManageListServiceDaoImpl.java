/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.resultdata.energy.input.CoefficientHistoryManageListDetailResultData;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage;
import jp.co.osaki.osol.entity.MCoefficientHistoryManagePK_;
import jp.co.osaki.osol.entity.MCoefficientHistoryManage_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 係数履歴管理マスタ取得 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class CoefficientHistoryManageListServiceDaoImpl
        implements BaseServiceDao<CoefficientHistoryManageListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CoefficientHistoryManageListDetailResultData> getResultList(
            CoefficientHistoryManageListDetailResultData t,
            EntityManager em) {
        String engTypeCd = t.getEngTypeCd();
        Long engId = t.getEngId();
        String dayAndNightType = t.getDayAndNightType();
        String calYmFrom = t.getCalYmFrom();
        String calYmTo = t.getCalYmTo();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CoefficientHistoryManageListDetailResultData> query = builder
                .createQuery(CoefficientHistoryManageListDetailResultData.class);

        Root<MCoefficientHistoryManage> root = query.from(MCoefficientHistoryManage.class);

        List<Predicate> whereList = new ArrayList<>();

        //エネルギー種類コード
        whereList.add(builder.equal(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engTypeCd),
                engTypeCd));
        //エネルギーID
        whereList.add(
                builder.equal(root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engId), engId));
        //昼夜区分
        if (!CheckUtility.isNullOrEmpty(dayAndNightType)) {
            whereList.add(builder.equal(
                    root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.dayAndNightType),
                    dayAndNightType));
        }
        //カレンダ年月
        if (!CheckUtility.isNullOrEmpty(calYmFrom) && !CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(builder.lessThanOrEqualTo(root.get(MCoefficientHistoryManage_.startYm), calYmFrom));
            whereList.add(builder.or(builder.isNull(root.get(MCoefficientHistoryManage_.endYm)),
                    builder.greaterThanOrEqualTo(root.get(MCoefficientHistoryManage_.endYm), calYmTo)));
        } else if (!CheckUtility.isNullOrEmpty(calYmFrom) && CheckUtility.isNullOrEmpty(calYmTo)) {
            whereList.add(builder.lessThanOrEqualTo(root.get(MCoefficientHistoryManage_.startYm), calYmFrom));
        }

        query = query.select(builder.construct(CoefficientHistoryManageListDetailResultData.class,
                root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engTypeCd),
                root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.engId),
                root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.dayAndNightType),
                root.get(MCoefficientHistoryManage_.id).get(MCoefficientHistoryManagePK_.coefficientHistoryId),
                root.get(MCoefficientHistoryManage_.startYm),
                root.get(MCoefficientHistoryManage_.endYm),
                root.get(MCoefficientHistoryManage_.stdFirstEngCoefficient),
                root.get(MCoefficientHistoryManage_.stdFirstEngUnit),
                root.get(MCoefficientHistoryManage_.stdCo2Coefficient),
                root.get(MCoefficientHistoryManage_.stdCo2Unit),
                root.get(MCoefficientHistoryManage_.adjustCo2Coefficient),
                root.get(MCoefficientHistoryManage_.adjustCo2Unit),
                root.get(MCoefficientHistoryManage_.gasCoefficient),
                root.get(MCoefficientHistoryManage_.cityGasStandard),
                root.get(MCoefficientHistoryManage_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();

    }

    @Override
    public List<CoefficientHistoryManageListDetailResultData> getResultList(Map<String, List<Object>> map,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CoefficientHistoryManageListDetailResultData> getResultList(
            List<CoefficientHistoryManageListDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CoefficientHistoryManageListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CoefficientHistoryManageListDetailResultData find(CoefficientHistoryManageListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(CoefficientHistoryManageListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CoefficientHistoryManageListDetailResultData merge(CoefficientHistoryManageListDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(CoefficientHistoryManageListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
