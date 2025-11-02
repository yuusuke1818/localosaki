package jp.co.osaki.osol.access.filter.datafilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
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

/**
 *
 * パートナー企業の操作企業の担当者権限のフィルターデータ取得
 *
 * @author take_suzuki
 */
public class CorpAuthPartnerGetResultList implements GetFilterDataInterface<CorpPersonAuthResultSet, CorpPersonAuthParam> {

    /**
     *
     * 操作企業の担当者権限フィルターのデータ取得
     *
     * @param em エンティティマネージャ
     * @param param 権限情報検索パラメータ
     * @return 操作企業の担当者権限のフィルター用データ
     */
    @Override
    public Map<String, CorpPersonAuthResultSet> getFilterData(EntityManager em, CorpPersonAuthParam param) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CorpPersonAuthResultSet> query = cb.createQuery(CorpPersonAuthResultSet.class);

        Root<MCorp> rootMCorp = query.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson = rootMCorp.join(MCorp_.MPersons, JoinType.INNER);
        Join<MPerson, MCorpPerson> joinMCorpPerson = joinMPerson.join(MPerson_.MCorpPersons, JoinType.INNER);
        Join<MCorpPerson, MCorpPersonAuth> joinMCorpPersonAuth = joinMCorpPerson.join(MCorpPerson_.MCorpPersonAuths, JoinType.INNER);
        Join<MCorpPerson, MCorp> joinMCorpOp = joinMCorpPerson.join(MCorpPerson_.MCorp, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();
        //企業ID
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpId), param.getLoginCorpId()));
        //担当者ID
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
        //操作企業ID
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.MCorp).get(MCorp_.corpId), param.getOperationCorpId()));
        //パートナー
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpType), "1"));
        //アカウント停止フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMCorpPerson.get(MCorpPerson_.delFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMCorpPersonAuth.get(MCorpPersonAuth_.delFlg), 0));

        query.select(cb.construct(CorpPersonAuthResultSet.class,
                joinMPerson.get(MPerson_.id).get(MPersonPK_.corpId),
                rootMCorp.get(MCorp_.corpType),
                joinMPerson.get(MPerson_.id).get(MPersonPK_.personId),
                joinMPerson.get(MPerson_.personType),
                joinMCorpPerson.get(MCorpPerson_.MCorp).get(MCorp_.corpId),
                joinMCorpOp.get(MCorp_.corpType),
                joinMCorpPerson.get(MCorpPerson_.authorityType),
                joinMCorpPersonAuth.get(MCorpPersonAuth_.MAuth).get(MAuth_.authorityCd),
                joinMCorpPersonAuth.get(MCorpPersonAuth_.authorityFlg)))
                .where(cb.and(conditionList.toArray(new Predicate[]{})))
                .orderBy(
                        cb.asc(joinMPerson.get(MPerson_.id).get(MPersonPK_.corpId)),
                        cb.asc(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId)),
                        cb.asc(joinMCorpPerson.get(MCorpPerson_.MCorp).get(MCorp_.corpId)),
                        cb.asc(joinMCorpPersonAuth.get(MCorpPersonAuth_.MAuth).get(MAuth_.authorityCd)));

        HashMap<String, CorpPersonAuthResultSet> map = new HashMap<>();
        for (CorpPersonAuthResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getOperationAuthorityCd() , b);
        }
        return map;
    }

}
