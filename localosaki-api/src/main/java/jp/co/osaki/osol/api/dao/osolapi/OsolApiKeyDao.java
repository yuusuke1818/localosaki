package jp.co.osaki.osol.api.dao.osolapi;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiKeyServiceDaoImpl;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

/**
 * APIキーテーブル Daoクラス
 *
 * @author take_suzuki
 */
@Stateless
public class OsolApiKeyDao extends BaseApiDao {

    private final OsolApiKeyServiceDaoImpl osolApiKeyServiceDaoImpl;

    public OsolApiKeyDao() {
        this.osolApiKeyServiceDaoImpl = new OsolApiKeyServiceDaoImpl();
    }

    /**
     * ApiKeyを取得する.
     *
     * @param corpId
     * @param personId
     *
     * @return
     */
    public TApiKey getApiKey(String corpId, String personId) {

        TApiKey tApiKey = new TApiKey();
        TApiKeyPK tApiKeyPk = new TApiKeyPK();
        tApiKeyPk.setCorpId(corpId);
        tApiKeyPk.setPersonId(personId);
        tApiKey.setId(tApiKeyPk);
        return find(osolApiKeyServiceDaoImpl, tApiKey);
    }

    /**
     * ApiKeyを論理削除する
     *
     * @param param
     * @param loginUserId
     * @return
     */
    public TApiKey removeApiKey(TApiKey tApiKey, Long loginUserId) {

        tApiKey.setDelFlg(OsolConstants.FLG_ON);
        tApiKey.setUpdateDate(getServerDateTime());
        tApiKey.setUpdateUserId(loginUserId);
        return merge(osolApiKeyServiceDaoImpl, tApiKey);

    }

    /**
     * ApiKeyを作成、また更新する。
     *
     * @param corpId
     * @param personId
     * @param apiKey
     * @param loginUserId
     */
    public void createApiKey(String corpId, String personId, String apiKey, Long loginUserId) {

        TApiKey tApiKey = new TApiKey();
        TApiKeyPK tApiKeyPK = new TApiKeyPK();
        tApiKeyPK.setCorpId(corpId);
        tApiKeyPK.setPersonId(personId);
        tApiKey.setId(tApiKeyPK);
        Timestamp svDate = getServerDateTime();
        Timestamp limitDate = new Timestamp(DateUtility.plusMinute(new Date(svDate.getTime()),
                OsolConstants.API_PUBLIC_KEY_LIFE_TIME).getTime());
        TApiKey updTApiKey = find(osolApiKeyServiceDaoImpl, tApiKey);
        if (updTApiKey != null) {
            updTApiKey.setApiKey(apiKey);
            updTApiKey.setIssuedDate(svDate);
            updTApiKey.setExpirationDate(limitDate);
            updTApiKey.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
            updTApiKey.setDelFlg(OsolConstants.FLG_OFF);
            updTApiKey.setUpdateDate(svDate);
            updTApiKey.setUpdateUserId(loginUserId);
            merge(osolApiKeyServiceDaoImpl, updTApiKey);
        } else {
            TApiKey newTApiKey = new TApiKey();
            TApiKeyPK newTApiKeyPK = new TApiKeyPK();
            newTApiKeyPK.setCorpId(corpId);
            newTApiKeyPK.setPersonId(personId);
            newTApiKey.setId(tApiKeyPK);
            newTApiKey.setApiKey(apiKey);
            newTApiKey.setIssuedDate(svDate);
            newTApiKey.setExpirationDate(limitDate);
            newTApiKey.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
            newTApiKey.setDelFlg(OsolConstants.FLG_OFF);
            newTApiKey.setVersion(0);
            newTApiKey.setCreateDate(svDate);
            newTApiKey.setCreateUserId(loginUserId);
            newTApiKey.setUpdateDate(svDate);
            newTApiKey.setUpdateUserId(loginUserId);
            persist(osolApiKeyServiceDaoImpl, newTApiKey);
        }
    }

}
