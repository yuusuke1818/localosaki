package jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterUser;

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
import jp.co.osaki.osol.api.resultdata.sms.meterUser.SearchMeterUserResultData;
import jp.co.osaki.osol.entity.MAuth;
import jp.co.osaki.osol.entity.MAuth_;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MCorpPersonAuth_;
import jp.co.osaki.osol.entity.MCorpPerson_;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

public class MeterUserSearchServiceDaoImpl implements BaseServiceDao<SearchMeterUserResultData> {

    @Override
    public List<SearchMeterUserResultData> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<SearchMeterUserResultData> query = builder.createQuery(SearchMeterUserResultData.class);

        Root<MCorp> root = query.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson = root.join(MCorp_.MPersons , JoinType.INNER);
        Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons , JoinType.INNER);
        Join<MCorpPerson, MCorpPersonAuth> joinMCorpPersonAuth = joinMCorpPerson.join(MCorpPerson_.MCorpPersonAuths , JoinType.INNER);
        Join<MCorpPersonAuth, MAuth> joinMAuth = joinMCorpPersonAuth.join(MCorpPersonAuth_.MAuth , JoinType.INNER);

        List<Predicate> whereList = new ArrayList<>();

        // SMS権限を持つ担当者に絞り込み
        whereList.add(builder.equal(joinMAuth.get(MAuth_.authorityCd), OsolConstants.USER_AUTHORITY.SMS.getVal()));

        // 企業ID（企業IDは１件のみ）
        if (!parameterMap.get("corpId").isEmpty() && parameterMap.get("corpId").get(0) != null) {
            whereList.add(builder.equal(root.get(MCorp_.corpId), parameterMap.get("corpId").get(0).toString()));
        }

        // 担当者名(ユーザー名)
        if (!parameterMap.get("personIdOrName").isEmpty()) {
            whereList.add(
                    builder.or(
                            builder.or(
                                    builder.isNull(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId)),

                                    builder.like(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), BaseUtility.addSqlWildcard(parameterMap.get("personIdOrName").get(0).toString())),
                              builder.or(
                                    builder.isNull(joinMPerson.get(MPerson_.personName)),
                                    builder.like(joinMPerson.get(MPerson_.personName), BaseUtility.addSqlWildcard(parameterMap.get("personIdOrName").get(0).toString()))))));
        }

        // ふりがな
        if (!parameterMap.get("personKana").isEmpty()) {
            whereList.add(
                    builder.or(
                            builder.like(joinMPerson.get(MPerson_.personKana), BaseUtility.addSqlWildcard(parameterMap.get("personKana").get(0).toString()))));
        }

        // 部署名
        if (!parameterMap.get("deptName").isEmpty()) {
            whereList.add(
                    builder.or(
                            builder.like(joinMPerson.get(MPerson_.deptName), BaseUtility.addSqlWildcard(parameterMap.get("deptName").get(0).toString()))));
        }

        // 役職名
        if (!parameterMap.get("positionName").isEmpty()) {
            whereList.add(
                    builder.or(
                            builder.like(joinMPerson.get(MPerson_.positionName), BaseUtility.addSqlWildcard(parameterMap.get("positionName").get(0).toString()))));
        }

        //削除フラグ
        whereList.add(builder.equal(joinMPerson.get(MPerson_.delFlg), OsolConstants.FLG_OFF));
        whereList.add(builder.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), OsolConstants.FLG_OFF));
        whereList.add(builder.equal(joinMCorpPersonAuth.get(MCorpPersonAuth_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(SearchMeterUserResultData.class,
                root.get(MCorp_.corpId),
                root.get(MCorp_.corpType),
                joinMPerson.get(MPerson_.id).get(MPersonPK_.personId),
                joinMPerson.get(MPerson_.userId),
                joinMPerson.get(MPerson_.personType),
                joinMPerson.get(MPerson_.personName),
                joinMPerson.get(MPerson_.personKana),
                joinMPerson.get(MPerson_.deptName),
                joinMPerson.get(MPerson_.positionName),
                joinMPerson.get(MPerson_.telNo),
                joinMPerson.get(MPerson_.faxNo),
                joinMPerson.get(MPerson_.mailAddress),
                joinMPerson.get(MPerson_.password),
                joinMPerson.get(MPerson_.passMissCount),
                joinMPerson.get(MPerson_.updatePassDate),
                joinMPerson.get(MPerson_.tempPassExpirationDate),
                joinMPerson.get(MPerson_.tempPassword),
                joinMPerson.get(MPerson_.lastLoginDate),
                joinMPerson.get(MPerson_.accountStopFlg),
                joinMPerson.get(MPerson_.accountStopDate),
                joinMPerson.get(MPerson_.authLastUpdateDate),
                joinMPerson.get(MPerson_.lastOshiraseCheckTime),
                joinMPerson.get(MPerson_.version))).where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(
                        builder.asc(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchMeterUserResultData> getResultList(SearchMeterUserResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchMeterUserResultData> getResultList(List<SearchMeterUserResultData> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SearchMeterUserResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SearchMeterUserResultData find(SearchMeterUserResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(SearchMeterUserResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SearchMeterUserResultData merge(SearchMeterUserResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(SearchMeterUserResultData target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
