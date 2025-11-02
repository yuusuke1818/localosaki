package jp.co.osaki.osol.api.dao.apikeyissue;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyCreateParameter;
import jp.co.osaki.osol.api.result.apikeyissue.OsolApiKeyCreateResult;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * OSOL用APIキー発行 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class OsolApiKeyCreateDao extends OsolApiDao<OsolApiKeyCreateParameter> {

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public OsolApiKeyCreateResult query(OsolApiKeyCreateParameter parameter) throws Exception {
        OsolApiKeyCreateResult result = new OsolApiKeyCreateResult();

        //APIキーを発行する
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();
        Timestamp issueDateTime = getServerDateTime();
        String apiKey = OsolApiAuthUtil.createPrivateApiKey(loginUserId,
                parameter.getLoginPersonId(), parameter.getLoginCorpId(), parameter.getOperationCorpId(),
                issueDateTime);

        if (CheckUtility.isNullOrEmpty(apiKey)) {
            result.setApiKey(null);
            result.setIssueDateTime(null);
        } else {
            result.setApiKey(apiKey);
            result.setIssueDateTime(issueDateTime);
        }

        return result;
    }

}
