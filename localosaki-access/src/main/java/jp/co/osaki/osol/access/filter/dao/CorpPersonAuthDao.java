package jp.co.osaki.osol.access.filter.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.access.filter.servicedao.CorpPersonAuthServiceDaoImpl;
import jp.skygroup.enl.webap.base.BaseDao;

/**
 *
 * 担当者権限Dao クラス
 *
 * @author take_suzuki
 */
@Stateless
public class CorpPersonAuthDao extends BaseDao {

    /**
     * 担当者権限データフィルターServiceDao
     */
    private final CorpPersonAuthServiceDaoImpl corpPersonAuthDataServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public CorpPersonAuthDao() {
        corpPersonAuthDataServiceDaoImpl = new CorpPersonAuthServiceDaoImpl();
    }

    public List<CorpPersonAuthResultSet> getCorpPersonAuth(CorpPersonAuthParam corpPersonAuthDataFilterParam) {

        Map<String, List<Object>> keyList = new HashMap<>();
        // ログイン企業ID 必須
        List<Object> loginCorpIdList = new ArrayList<>();
        loginCorpIdList.add(corpPersonAuthDataFilterParam.getLoginCorpId());
        keyList.put(CorpPersonAuthParam.LOGIN_CORP_ID, loginCorpIdList);

        // ログイン担当者ID 必須
        List<Object> loginPersonIdList = new ArrayList<>();
        loginPersonIdList.add(corpPersonAuthDataFilterParam.getLoginPersonId());
        keyList.put(CorpPersonAuthParam.LOGIN_PERSON_ID, loginPersonIdList);

        // 操作企業ID 必須
        List<Object> operationCorpIdList = new ArrayList<>();
        operationCorpIdList.add(corpPersonAuthDataFilterParam.getOperationCorpId());
        keyList.put(CorpPersonAuthParam.OPERATION_CORP_ID, operationCorpIdList);

        List<CorpPersonAuthResultSet> list = getResultList(corpPersonAuthDataServiceDaoImpl, keyList);
        return list;
    }

}
