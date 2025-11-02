package jp.co.osaki.osol.api.dao.apikeyissue;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyCreateParameter;
import jp.co.osaki.osol.api.result.apikeyissue.ApiKeyCreateResult;
import jp.co.osaki.osol.api.servicedao.entity.TApiKeyServiceDaoImpl;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 外部アクセス用APIキー発行　Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class ApiKeyCreateDao extends OsolApiDao<ApiKeyCreateParameter> {

    private final TApiKeyServiceDaoImpl tApiKeyServiceDaoImpl;

    public ApiKeyCreateDao() {
        tApiKeyServiceDaoImpl = new TApiKeyServiceDaoImpl();
    }

    @Override
    public ApiKeyCreateResult query(ApiKeyCreateParameter parameter) throws Exception {

        ApiKeyCreateResult result = new ApiKeyCreateResult();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();
        Timestamp limitDate = new Timestamp(
                DateUtility.plusMinute(new Date(serverDateTime.getTime()), OsolConstants.API_PUBLIC_KEY_LIFE_TIME)
                        .getTime());

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        TApiKey param = new TApiKey();
        TApiKeyPK pkParam = new TApiKeyPK();
        pkParam.setCorpId(parameter.getLoginCorpId());
        pkParam.setPersonId(parameter.getLoginPersonId());
        param.setId(pkParam);

        TApiKey updateData = find(tApiKeyServiceDaoImpl, param);

        if (updateData != null) {
            if (parameter.getVersion() == null || !updateData.getVersion().equals(parameter.getVersion())) {
                throw new OptimisticLockException();
            }
        } else {
            if (parameter.getVersion() != null) {
                throw new OptimisticLockException();
            }
        }

        //APIキーを発行する
        String apiKey = OsolApiAuthUtil.createPublicApiKey(loginUserId, parameter.getLoginPersonId(),
                parameter.getLoginCorpId(), parameter.getOperationCorpId(), serverDateTime);

        String refreshKey = OsolApiAuthUtil.createRefreshKey(loginUserId, parameter.getLoginPersonId(),
                parameter.getLoginCorpId(), parameter.getOperationCorpId(), serverDateTime);

        if (CheckUtility.isNullOrEmpty(apiKey) || CheckUtility.isNullOrEmpty(refreshKey)) {
            return new ApiKeyCreateResult();
        } else {
            result.setApiKey(apiKey);
            result.setIssueDateTime(serverDateTime);
            result.setRefreshKey(refreshKey);
        }

        if (updateData == null) {

            TApiKey insertParam = new TApiKey();
            TApiKeyPK pkInsertParam = new TApiKeyPK();

            pkInsertParam.setCorpId(parameter.getLoginCorpId());
            pkInsertParam.setPersonId(parameter.getLoginPersonId());
            insertParam.setId(pkInsertParam);
            insertParam.setApiKey(apiKey);
            insertParam.setIssuedDate(serverDateTime);
            insertParam.setRefreshKey(refreshKey);
            insertParam.setExpirationDate(limitDate);
            insertParam.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
            insertParam.setDelFlg(OsolConstants.FLG_OFF);
            insertParam.setVersion(0);
            insertParam.setCreateDate(serverDateTime);
            insertParam.setCreateUserId(loginUserId);
            insertParam.setUpdateDate(serverDateTime);
            insertParam.setUpdateUserId(loginUserId);

            persist(tApiKeyServiceDaoImpl, insertParam);

        } else {

            updateData.setApiKey(apiKey);
            updateData.setIssuedDate(serverDateTime);
            updateData.setExpirationDate(limitDate);
            updateData.setRefreshKey(refreshKey);
            updateData.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
            updateData.setDelFlg(OsolConstants.FLG_OFF);
            updateData.setUpdateDate(serverDateTime);
            updateData.setUpdateUserId(loginUserId);

            merge(tApiKeyServiceDaoImpl, updateData);
        }

        return result;
    }

}
