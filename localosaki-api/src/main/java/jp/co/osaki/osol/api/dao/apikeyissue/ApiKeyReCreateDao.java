package jp.co.osaki.osol.api.dao.apikeyissue;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.ApiKeyReCreateParameter;
import jp.co.osaki.osol.api.result.apikeyissue.ApiKeyReCreateResult;
import jp.co.osaki.osol.api.servicedao.entity.TApiKeyServiceDaoImpl;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

@Stateless
public class ApiKeyReCreateDao extends OsolApiDao<ApiKeyReCreateParameter> {

    private final TApiKeyServiceDaoImpl tApiKeyServiceDaoImpl;

    public ApiKeyReCreateDao() {
        this.tApiKeyServiceDaoImpl = new TApiKeyServiceDaoImpl();
    }

    @Override
    public ApiKeyReCreateResult query(ApiKeyReCreateParameter parameter) throws Exception {
        ApiKeyReCreateResult result = new ApiKeyReCreateResult();

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

        if (updateData == null) {
            throw new OptimisticLockException();
        }
        else {
            if (
                    CheckUtility.isNullOrEmpty(parameter.getRefreshKey()) ||
                    CheckUtility.isNullOrEmpty(parameter.getApiKey()) ||
                    !parameter.getRefreshKey().equals(updateData.getRefreshKey()) ||
                    !parameter.getApiKey().equals(updateData.getApiKey())
            ) {
                return result;
            }
        }

        //APIキーを発行する
        String apiKey = OsolApiAuthUtil.createPublicApiKey(loginUserId, parameter.getLoginPersonId(),
                parameter.getLoginCorpId(), parameter.getOperationCorpId(), serverDateTime);

        if (CheckUtility.isNullOrEmpty(apiKey)) {
            return new ApiKeyReCreateResult();
        } else {
            result.setApiKey(apiKey);
        }

        updateData.setApiKey(apiKey);
        updateData.setIssuedDate(serverDateTime);
        updateData.setExpirationDate(limitDate);
        updateData.setValidityPeriodMin(OsolConstants.API_PUBLIC_KEY_LIFE_TIME);
        updateData.setDelFlg(OsolConstants.FLG_OFF);
        updateData.setUpdateDate(serverDateTime);
        updateData.setUpdateUserId(loginUserId);

        merge(tApiKeyServiceDaoImpl, updateData);



        return result;
    }

}
