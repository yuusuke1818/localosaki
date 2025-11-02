package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ClaimantInfoResultData;
import jp.co.osaki.osol.api.servicedao.entity.MClaimantInfoServiceDaoImpl;
import jp.co.osaki.osol.entity.MClaimantInfo;
import jp.co.osaki.osol.entity.MClaimantInfoPK;

/**
 * 請求者情報取得 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class GetSmsClaimantInfoDao extends OsolApiDao<GetSmsClaimantInfoParameter> {

    private final MClaimantInfoServiceDaoImpl mClaimantInfoServiceDaoImpl;

    public GetSmsClaimantInfoDao() {
        mClaimantInfoServiceDaoImpl = new MClaimantInfoServiceDaoImpl();

    }
    @Override
    public GetSmsClaimantInfoResult query(GetSmsClaimantInfoParameter parameter) throws Exception {

        MClaimantInfo mClaimantInfo = new MClaimantInfo();
        MClaimantInfoPK mClaimantInfoPK = new MClaimantInfoPK();

        mClaimantInfoPK.setCorpId(parameter.getCorpId());
        mClaimantInfoPK.setBuildingId(parameter.getBuildingId());
        mClaimantInfoPK.setPersonCorpId(parameter.getPersonCorpId());
        mClaimantInfoPK.setPersonId(parameter.getPersonId());
        mClaimantInfo.setId(mClaimantInfoPK);

        MClaimantInfo findMClaimantInfo = find(mClaimantInfoServiceDaoImpl, mClaimantInfo);

        GetSmsClaimantInfoResult result = new GetSmsClaimantInfoResult();
        ClaimantInfoResultData claimantInfoResultData = new ClaimantInfoResultData(findMClaimantInfo);
        result.setClaimantInfoResultData(claimantInfoResultData);


        return result;
    }
}
