package jp.co.osaki.osol.access.filter.datafilter;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.access.filter.resultset.CorpDataFilterResultSet;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;

/**
 *
 * 大崎電気企業の管理者の操作企業のフィルターデータ取得
 *
 * @author take_suzuki
 *
 */
public class CorpOsakiAdminGetFilterData implements GetFilterDataInterface<CorpDataFilterResultSet, PersonDataParam> {

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

        query.select(cb.construct(CorpDataFilterResultSet.class, rootMCorp.get(MCorp_.corpId)))
                .orderBy(cb.asc(rootMCorp.get(MCorp_.corpId)));

        HashMap<String, CorpDataFilterResultSet> map = new HashMap<>();
        for (CorpDataFilterResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getCorpId(), b);
        }
        return map;
    }

}
