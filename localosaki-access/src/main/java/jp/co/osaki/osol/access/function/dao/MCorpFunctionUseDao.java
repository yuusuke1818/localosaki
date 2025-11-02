package jp.co.osaki.osol.access.function.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import jp.co.osaki.osol.access.function.servicedao.MCorpFunctionUseServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorpFunctionUse;
import jp.co.osaki.osol.entity.MCorpFunctionUsePK;
import jp.skygroup.enl.webap.base.BaseDao;

/**
 *
 * 企業機能利用マスタDao
 *
 * @author take_suzuki
 */
@Stateless
public class MCorpFunctionUseDao extends BaseDao {

    /**
     *  企業機能利用マスタサービスDaoクラス
     */
    private final MCorpFunctionUseServiceDaoImpl mCorpFunctionUseServiceDaoImpl;

    /**
     *  コンストラクタ
     */
    public MCorpFunctionUseDao() {
        mCorpFunctionUseServiceDaoImpl = new MCorpFunctionUseServiceDaoImpl();
    }

    /**
     *
     * 企業機能利用マスタ検索
     *
     * @param functionCd 機能コード
     * @param corpId 企業ID
     * @return 企業機能利用マスタエンティティ
     */
    public MCorpFunctionUse find(String functionCd, String corpId) {

        MCorpFunctionUsePK mCorpFunctionUsePK = new MCorpFunctionUsePK();
        mCorpFunctionUsePK.setCorpId(corpId);
        mCorpFunctionUsePK.setFunctionCd(functionCd);
        MCorpFunctionUse mCorpFunctionUse = new MCorpFunctionUse();
        mCorpFunctionUse.setId(mCorpFunctionUsePK);
        mCorpFunctionUse = find(mCorpFunctionUseServiceDaoImpl, mCorpFunctionUse);
        return mCorpFunctionUse;
    }

    public List<MCorpFunctionUse> getResultList(String corpId) {

        Map<String, List<Object>> parameterMap = new HashMap<>();

        List<Object> corpIds = new ArrayList<Object>();
        corpIds.add(corpId);

        parameterMap.put("corpId", corpIds);

        return getResultList(mCorpFunctionUseServiceDaoImpl, parameterMap);
    }
}
