package jp.co.osaki.osol.api.dao.apikeyissue;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.apikeyissue.OsolApiKeyUpdateParameter;
import jp.co.osaki.osol.api.result.apikeyissue.OsolApiKeyUpdateResult;
import jp.co.osaki.osol.api.util.OsolApiAuthUtil;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * OSOL用APIキー更新 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class OsolApiKeyUpdateDao extends OsolApiDao<OsolApiKeyUpdateParameter> {

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public OsolApiKeyUpdateResult query(OsolApiKeyUpdateParameter parameter) throws Exception {
        OsolApiKeyUpdateResult result = new OsolApiKeyUpdateResult();

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
