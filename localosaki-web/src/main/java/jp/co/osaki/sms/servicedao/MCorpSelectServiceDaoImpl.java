package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.entity.MCorp;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 企業情報
 *
 * @author y-komano2
 */
public class MCorpSelectServiceDaoImpl implements BaseServiceDao<MCorp> {

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

        TypedQuery<MCorp> query = null;
        // Osaki権限の確認
        if (parameterMap.get("authorityOsakiFlg") == null || parameterMap.get("authorityOsakiFlg").size() <= 0) {
            // 特定後かOsaki権限の管理者ではない場合（担当企業のものだけ取得する、かつ自企業を除外）
            query = em.createNamedQuery("MCorp.findForCorpSelect", MCorp.class);
            query.setParameter("authorityPersonId", parameterMap.get("authorityPersonId").get(0).toString());
            query.setParameter("authorityCorpId", parameterMap.get("authorityCorpId").get(0).toString());
        } else {
            // Osaki権限の管理者の場合(全企業が対象)
            query = em.createNamedQuery("MCorp.findJoinCorpType", MCorp.class);
        }

        // 2017/07/31 検査改善
        // 企業(IDまたは名)
        if (parameterMap.get("corpIdOrName") == null || parameterMap.get("corpIdOrName").size() <= 0) {
            query.setParameter("corpId", null);
            query.setParameter("corpName", null);
        } else {
            query.setParameter("corpId", BaseUtility.addSqlWildcard(parameterMap.get("corpIdOrName").get(0).toString()));
            query.setParameter("corpName", BaseUtility.addSqlWildcard(parameterMap.get("corpIdOrName").get(0).toString()));
        }

        // 企業種別
        if (parameterMap.get("corpType") == null || parameterMap.get("corpType").size() <= 0) {
            query.setParameter("corpType", null);
            query.setParameter("corpTypeFlg", null);
        } else {
            List<String> corpTypeList = new ArrayList<>();
            for (int cnt = 0; cnt < parameterMap.get("corpType").size(); cnt++) {
                corpTypeList.add(parameterMap.get("corpType").get(cnt).toString());
            }
            query.setParameter("corpType", corpTypeList);
            query.setParameter("corpTypeFlg", "1");
        }

        // 都道府県
        List<Object> prefectureList = parameterMap.get("prefectureCd");
        if (prefectureList == null || prefectureList.isEmpty()) {
            query.setParameter("prefectureCd", null);
            query.setParameter("prefectureCdFlg", null);
        } else {
            List<String> prefectureCdParamList = new ArrayList<>();
            for (Object prefectureCd : prefectureList) {
                prefectureCdParamList.add(prefectureCd.toString());
            }
            if (!prefectureCdParamList.isEmpty()) {
                query.setParameter("prefectureCd", prefectureCdParamList);
                query.setParameter("prefectureCdFlg", "1");
            } else {
                query.setParameter("prefectureCd", null);
                query.setParameter("prefectureCdFlg", null);
            }
        }

        // 選択中企業
        if (parameterMap.get("selectCorpId") == null || parameterMap.get("selectCorpId").size() <= 0) {
            query.setParameter("selectCorpId", null);
        } else {
            query.setParameter("selectCorpId", parameterMap.get("selectCorpId").get(0).toString());
        }

        List<MCorp> resultList = query.getResultList();

        return resultList;
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
