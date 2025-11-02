package jp.co.osaki.sms.servicedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MCorp;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 企業情報担当者情報結合DaoImpl
 *
 * @author d-komatsubara
 */
public class MCorpJoinMPersonDaoImpl implements BaseServiceDao<MCorp> {

    @Override
    public List<MCorp> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {

        TypedQuery<MCorp> query;
        // Osaki権限の確認
        if (parameterMap.get("authorityOsakiFlg") == null || parameterMap.get("authorityOsakiFlg").size() <= 0) {
            // 特定後かOsaki権限の管理者ではない場合（担当企業のものだけ取得する）
            query = em.createNamedQuery("MCorp.findForCorpPerson", MCorp.class);
            query.setParameter("authorityPersonId", parameterMap.get("authorityPersonId").get(0).toString());
            query.setParameter("authorityCorpId", parameterMap.get("authorityCorpId").get(0).toString());
        } else {
            // Osaki権限の管理者の場合(全企業が対象)
            query = em.createNamedQuery("MCorp.findJoin", MCorp.class);
        }

        // 企業ID
        if (parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID) == null) {
            query.setParameter("corpId", null);
        } else {
            query.setParameter("corpId", BaseUtility.addSqlWildcard(parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID).get(0).toString()));
        }

        // 企業名
        if (parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_NAME) == null) {
            query.setParameter("corpName", null);
        } else {
            query.setParameter("corpName", BaseUtility.addSqlWildcard(parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_NAME).get(0).toString()));
        }
        // 企業(IDまたは名)
        if (parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME) == null) {
            query.setParameter("corpIdOrName", null);
        } else {
            query.setParameter("corpIdOrName", BaseUtility.addSqlWildcard(parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_ID_OR_NAME).get(0).toString()));
        }

        // 企業種別
        if (parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_TYPE) == null) {
            query.setParameter("corpType", null);
        } else {
            List<Object> a = parameterMap.get(OsolConstants.SEARCH_CONDITION_CORP_TYPE);
            List<String> temp = new ArrayList<>();
            for (Object target : a) {
                temp.add((String) target);
            }
            query.setParameter("corpType", temp);

        }

        // 都道府県
        if (parameterMap.get(OsolConstants.SEARCH_CONDITION_PREFECTURE) == null) {
            query.setParameter("prefectureCd", null);
            query.setParameter("prefectureCdFlg", null);

        } else {
            List<Object> prefectureList = parameterMap.get(OsolConstants.SEARCH_CONDITION_PREFECTURE);
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

        // 自分が所属している企業は表示しない
        if (parameterMap.get("selectCorpId") == null) {
            query.setParameter("selectCorpId", null);
        } else {
            query.setParameter("selectCorpId", parameterMap.get("selectCorpId").get(0).toString());
        }
        List<MCorp> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(MCorp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(List<MCorp> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MCorp> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorp find(MCorp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MCorp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MCorp merge(MCorp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(MCorp target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
