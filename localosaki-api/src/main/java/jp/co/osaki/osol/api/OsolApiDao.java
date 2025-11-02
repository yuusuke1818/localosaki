package jp.co.osaki.osol.api;

import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiPersonServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.skygroup.enl.webap.base.api.BaseApiDao;
import jp.skygroup.enl.webap.base.api.BaseApiParameter;

/**
 *
 * OSOL API Dao 共通クラス.
 *
 * @author take_suzuki
 *
 */
public abstract class OsolApiDao<P extends BaseApiParameter> extends BaseApiDao {

    private final OsolApiPersonServiceDaoImpl osolApiPersonServiceDaoImpl;

    public OsolApiDao() {
        osolApiPersonServiceDaoImpl = new OsolApiPersonServiceDaoImpl();
    }

    /**
     * Dao実行メソッド
     *
     * @param parameter
     * @return
     */
    public abstract Object query(P parameter) throws Exception;

    /**
     * 担当者取得メソッド
     *
     * @param parameter OsolApiParameter
     * @return UserId
     */
    protected MPerson getMPerson(String corpId, String personId) {

        MPerson mPerson = new MPerson();
        MPersonPK mPersonPk = new MPersonPK();
        mPersonPk.setCorpId(corpId);
        mPersonPk.setPersonId(personId);
        mPerson.setId(mPersonPk);
        return this.find(osolApiPersonServiceDaoImpl, mPerson);
    }

}
