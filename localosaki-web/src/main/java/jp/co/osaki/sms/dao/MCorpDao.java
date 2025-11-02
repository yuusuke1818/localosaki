package jp.co.osaki.sms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MCorpServiceDaoImpl;

/**
 * 企業Daoクラス
 *
 * @author t-shibata
 */
@Stateless
public class MCorpDao extends SmsDao {

    private final MCorpServiceDaoImpl impl;

    public MCorpDao() {
        impl = new MCorpServiceDaoImpl();
    }

    /**
     *
     * @param targetPrefectureList
     * @param targetCorpIdList
     * @param targetCorpNameList
     * @param targetCorpTypeList
     * @return
     */
    public List<MCorp> getResultList(
            List<Object> targetPrefectureList,
            List<Object> targetCorpIdList,
            List<Object> targetCorpNameList,
            List<Object> targetCorpTypeList) {

        Map<String, List<Object>> parameterMap = new HashMap<>();

        parameterMap.put("prefectureCd", targetPrefectureList);
        parameterMap.put("corpId", targetCorpIdList);
        parameterMap.put("corpName", targetCorpNameList);
        parameterMap.put("corpType", targetCorpTypeList);

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
