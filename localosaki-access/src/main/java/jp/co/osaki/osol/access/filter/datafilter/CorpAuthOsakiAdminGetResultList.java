package jp.co.osaki.osol.access.filter.datafilter;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthAdminDataResultSet;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.entity.MAuth;
import jp.co.osaki.osol.entity.MAuth_;

/**
 *
 * 大崎電気企業の管理者の操作企業の担当者権限のフィルターデータ取得
 *
 * @author take_suzuki
 */
public class CorpAuthOsakiAdminGetResultList implements GetFilterDataInterface<CorpPersonAuthResultSet, CorpPersonAuthParam> {

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
        CriteriaQuery<CorpPersonAuthAdminDataResultSet> query = cb.createQuery(CorpPersonAuthAdminDataResultSet.class);

        Root<MAuth> rootMAuth = query.from(MAuth.class);

        query.select(cb.construct(CorpPersonAuthAdminDataResultSet.class,
                rootMAuth.get(MAuth_.authorityCd)))
                .orderBy(
                        cb.asc(rootMAuth.get(MAuth_.authorityCd)));

        HashMap<String, CorpPersonAuthResultSet> map = new HashMap<>();
        for (CorpPersonAuthAdminDataResultSet b : em.createQuery(query).getResultList()) {
            map.put(b.getAuthorityCd(), new CorpPersonAuthResultSet(
                    param.getLoginCorpId(),
                    "0",            //大崎電気工業
                    param.getLoginPersonId(),
                    "0",            //管理者
                    param.getOperationCorpId(),
                    "0",            //大崎電気工業
                    "0",            //管理担当
                    b.getAuthorityCd(),
                    1));
        }
        return map;
    }

}
