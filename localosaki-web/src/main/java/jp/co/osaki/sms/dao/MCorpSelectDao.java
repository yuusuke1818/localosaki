package jp.co.osaki.sms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MCorpSelectServiceDaoImpl;

/**
 * 企業Daoクラス
 *
 * @author y-komano2
 */
@Stateless
public class MCorpSelectDao extends SmsDao {

    private final MCorpSelectServiceDaoImpl impl;

    public MCorpSelectDao() {
        impl = new MCorpSelectServiceDaoImpl();
    }

    /**
     *
     * @param targetPrefectureList
     * @param targetCorpIdOrNameList
     * @param targetCorpTypeList
     * @param selectCorpIdList
     * @param authorityOsakiManegeList 権限処理用Osaki管理者フラグ情報
     * @param authorityPersonIdList 権限処理用担当者ID(MCorpPerson用)
     * @param authorityCorpIdList 権限処理用企業ID(MCorpPerson用)
     * @return
     */
    public List<MCorp> getResultList(
            List<Object> targetPrefectureList,
            List<Object> targetCorpIdOrNameList,
            List<Object> targetCorpTypeList,
            List<Object> selectCorpIdList,
            List<Object> authorityOsakiManegeList,
            List<Object> authorityPersonIdList,
            List<Object> authorityCorpIdList) {

        Map<String, List<Object>> parameterMap = new HashMap<>();

        parameterMap.put("prefectureCd", targetPrefectureList);
        // 2017/07/31 検索条件改善
        parameterMap.put("corpIdOrName", targetCorpIdOrNameList);
        parameterMap.put("corpType", targetCorpTypeList);
        parameterMap.put("selectCorpId", selectCorpIdList);

        // 大崎権限の場合1がリストに入っている、そうでない場合null
        parameterMap.put("authorityOsakiFlg", authorityOsakiManegeList);
        parameterMap.put("authorityPersonId", authorityPersonIdList);
        parameterMap.put("authorityCorpId", authorityCorpIdList);

        //parameterMap.put("orderBy", orderByList);
        return getResultList(impl, parameterMap);
    }

    /**
     * 企業情報取得
     *
     * @param corp_id 企業ID
     * @return 企業情報Bean
     */
    public MCorp find(String corp_id) {
        MCorp mc = new MCorp();
        mc.setCorpId(corp_id);

        MCorp result = find(impl, mc);

        return result;
    }

    /**
     * 企業情報 更新処理
     *
     * @param target 企業情報
     * @return 更新結果
     */
    public MCorp merge(MCorp target) {
        MCorp result = merge(impl, target);
        return result;
    }
}
