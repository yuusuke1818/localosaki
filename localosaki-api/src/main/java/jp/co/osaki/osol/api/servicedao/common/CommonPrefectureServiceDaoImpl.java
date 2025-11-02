package jp.co.osaki.osol.api.servicedao.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.api.result.servicedao.CommonPrefectureResult;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.co.osaki.osol.entity.MPrefecture_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 都道府県情報取得 ServiceDaoクラス
 * @author ya-ishida
 *
 */
public class CommonPrefectureServiceDaoImpl implements BaseServiceDao<CommonPrefectureResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonPrefectureResult> getResultList(CommonPrefectureResult target, EntityManager em) {
        String prefectureCd = target.getPrefectureCd();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CommonPrefectureResult> query = builder.createQuery(CommonPrefectureResult.class);

        Root<MPrefecture> root = query.from(MPrefecture.class);
        List<Predicate> whereList = new ArrayList<>();

        //都道府県コード
        whereList.add(builder.equal(root.get(MPrefecture_.prefectureCd), prefectureCd));

        query = query.select(builder.construct(CommonPrefectureResult.class,
                root.get(MPrefecture_.prefectureCd),
                root.get(MPrefecture_.prefectureName),
                root.get(MPrefecture_.version))).where(builder.and(whereList.toArray(new Predicate[] {})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<CommonPrefectureResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonPrefectureResult> getResultList(List<CommonPrefectureResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CommonPrefectureResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonPrefectureResult find(CommonPrefectureResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persist(CommonPrefectureResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonPrefectureResult merge(CommonPrefectureResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(CommonPrefectureResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
