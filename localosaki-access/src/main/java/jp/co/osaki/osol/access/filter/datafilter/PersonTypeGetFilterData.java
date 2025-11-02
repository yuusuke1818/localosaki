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
import jp.co.osaki.osol.access.filter.resultset.PersonTypeResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK_;
import jp.co.osaki.osol.entity.MPerson_;

/**
 *
 * 企業種別/担当者種別 判定データ取得
 *
 * @author take_suzuki
 *
 */
public class PersonTypeGetFilterData implements GetFilterDataInterface<PersonTypeResultSet, PersonDataParam> {

    /**
     *
     * 該当する担当者の企業種別/担当者種別の判定データ取得
     *
     * @param em エンティティマネージャ
     * @param param 担当者パラメータ
     * @return 企業種別/担当者種別の判定データ取得
     */
    @Override
    public Map<String, PersonTypeResultSet> getFilterData(EntityManager em, PersonDataParam param) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PersonTypeResultSet> query = cb.createQuery(PersonTypeResultSet.class);

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

        query.select(cb.construct(PersonTypeResultSet.class,
                rootMCorp.get(MCorp_.corpType),
                joinMPerson.get(MPerson_.personType)))
                .where(cb.and(conditionList.toArray(new Predicate[]{})));

        HashMap<String, PersonTypeResultSet> map = new HashMap<>();
        map.clear();
        map.put("0", em.createQuery(query).getSingleResult());

        return map;
    }
}
