package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MCorp_;
import jp.co.osaki.osol.entity.MPrefecture;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 企業情報
 *
 * @author t-shibata
 */
public class MCorpServiceDaoImpl implements BaseServiceDao<MCorp> {

    @Override
    public List<MCorp> getResultList(MCorp target, EntityManager em) {
        return null;
    }

    /**
     * 企業情報取得処理
     *
     * @param target 企業キー情報Bean
     * @param em エンティティマネージャ
     * @return 企業情報(MCorp)
     */
    @Override
    public MCorp find(MCorp target, EntityManager em) {

        // 企業情報を1件取得
        MCorp reseltObject = em.find(MCorp.class, target.getCorpId());

        return reseltObject;
    }

    @Override
    public void persist(MCorp target, EntityManager em) {

    }

    @Override
    public MCorp merge(MCorp target, EntityManager em) {
        MCorp reseltObject = em.merge(target);
        return reseltObject;
    }

    @Override
    public void remove(MCorp target, EntityManager em) {

    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MCorp> p = cb.createQuery(MCorp.class);
        Root<MCorp> mCorpRoot = p.from(MCorp.class);
        Join<MCorp, MPrefecture> mPrefectureJoin = mCorpRoot.join(MCorp_.MPrefecture);

        List<Predicate> paramList = new ArrayList<>();

        List<Object> corpIdList = parameterMap.get("corpId");
        if (corpIdList != null) {
            for (Object corpId : corpIdList) {
                paramList.add(cb.like(mCorpRoot.get(MCorp_.corpId), corpId.toString()));
            }
        }
        List<Object> prefectureCdList = parameterMap.get("prefectureCd");
        if (prefectureCdList != null) {
            for (Object prefectureCd : prefectureCdList) {
                paramList.add(cb.equal(mPrefectureJoin.<String>get("prefectureCd"), prefectureCd.toString()));
            }
        }
        List<Object> corpNameList = parameterMap.get("corpName");
        if (corpNameList != null) {
            for (Object corpName : corpNameList) {
                paramList.add(cb.like(mCorpRoot.get(MCorp_.corpName), corpName.toString()));
            }
        }
        List<Object> corpTypeList = parameterMap.get("corpType");
        if (corpTypeList != null) {
            for (Object corpType : corpTypeList) {
                paramList.add(cb.equal(mCorpRoot.get(MCorp_.corpType), corpType.toString()));
            }
        }
        p = p.select(mCorpRoot).where(cb.and(paramList.toArray(new Predicate[]{})));

        return em.createQuery(p).getResultList();

    }

    @Override
    public List<MCorp> getResultList(List<MCorp> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
