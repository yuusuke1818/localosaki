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

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.CorpDataFilterResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;

/**
 *
 * 指定された担当者の操作企業のフィルターデータ取得
 *
 * @author take_suzuki
 *
 *
 */
public class CorpMyCorpGetFilterData implements GetFilterDataInterface<CorpDataFilterResultSet, PersonDataParam> {

    /**
     *
     * 操作企業フィルターのデータ取得
     *
     * @param em エンティティマネージャ
     * @param param 担当者パラメータ
     * @return 操作企業のフィルター用データ
     */
    @Override
    public Map<String, CorpDataFilterResultSet> getFilterData(EntityManager em, PersonDataParam param) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CorpDataFilterResultSet> query = cb.createQuery(CorpDataFilterResultSet.class);

        Root<MCorp> rootMCorp = query.from(MCorp.class);
        Join<MCorp, MPerson> joinMPerson = rootMCorp.join(MCorp_.MPersons, JoinType.INNER);

        List<Predicate> conditionList = new ArrayList<>();

        // 企業ID
        conditionList.add(cb.equal(rootMCorp.get(MCorp_.corpId), param.getLoginCorpId()));
        // 担当者ID
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.id).get(MPersonPK_.personId), param.getLoginPersonId()));
        //アカウント停止フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.accountStopFlg), 0));
        //削除フラグ
        conditionList.add(cb.equal(joinMPerson.get(MPerson_.delFlg), 0));

        query.select(cb.construct(CorpDataFilterResultSet.class,
                rootMCorp.get(MCorp_.corpId)))
                .where(cb.and(conditionList.toArray(new Predicate[]{})));

        HashMap<String, CorpDataFilterResultSet> map = new HashMap<>();
        for (CorpDataFilterResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getCorpId(), b);
        }
        return map;
    }

}
