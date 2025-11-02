/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.sms.servicedao;

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

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MTenantSm;
import jp.co.osaki.osol.entity.MTenantSm_;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK_;
import jp.co.osaki.osol.entity.TBuilding_;
import jp.co.osaki.sms.resultset.UserCdListResultSet;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * ユーザーコード取得 serviceクラス
 *
 * @author yonezawa.a
 */
public class UserCdListSearchServiceDaoImpl implements BaseServiceDao<UserCdListResultSet> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserCdListResultSet> getResultList(UserCdListResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserCdListResultSet> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<UserCdListResultSet> query = builder.createQuery(UserCdListResultSet.class);
        Root<TBuilding> root = query.from(TBuilding.class);
        Join<TBuilding, MTenantSm> joinMTenantSm = root.join(TBuilding_.MTenantSms, JoinType.LEFT);

        // 企業に紐づくテナントを全件取得
        List<Predicate> whereList = new ArrayList<>();
        whereList.add(
                builder.equal(root.get(TBuilding_.id).get(TBuildingPK_.corpId), parameterMap.get("corpId").get(0)));
        whereList.add(builder.equal(root.get(TBuilding_.delFlg), OsolConstants.FLG_OFF));
        whereList.add(builder.equal(joinMTenantSm.get(MTenantSm_.delFlg), OsolConstants.FLG_OFF));

        query = query
                .select(builder.construct(UserCdListResultSet.class, joinMTenantSm.get(MTenantSm_.tenantId),
                        root.get(TBuilding_.buildingName)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(joinMTenantSm.get(MTenantSm_.tenantId)));

        List<UserCdListResultSet> resultList = em.createQuery(query).getResultList();
        return resultList;
    }

    @Override
    public List<UserCdListResultSet> getResultList(List<UserCdListResultSet> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserCdListResultSet find(UserCdListResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(UserCdListResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserCdListResultSet merge(UserCdListResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(UserCdListResultSet target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserCdListResultSet> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
