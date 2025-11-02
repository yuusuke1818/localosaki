package jp.co.osaki.osol.api.dao.sms.collect.dataview.meterreadingdata;

import java.sql.Timestamp;
import java.util.Objects;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.result.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ClaimantInfoResultData;
import jp.co.osaki.osol.api.servicedao.entity.MClaimantInfoServiceDaoImpl;
import jp.co.osaki.osol.entity.MClaimantInfo;
import jp.co.osaki.osol.entity.MClaimantInfoPK;
import jp.co.osaki.osol.entity.MPerson;

/**
 * 請求者情報設定 Daoクラス
 *
 * @author hosono.s
 */
@Stateless
public class UpdateSmsClaimantInfoDao extends OsolApiDao<UpdateSmsClaimantInfoParameter> {

    private final MClaimantInfoServiceDaoImpl mClaimantInfoServiceDaoImpl;

    public UpdateSmsClaimantInfoDao() {

        mClaimantInfoServiceDaoImpl = new MClaimantInfoServiceDaoImpl();
    }
    @Override
    public UpdateSmsClaimantInfoResult query(UpdateSmsClaimantInfoParameter parameter) throws Exception {

        //ユーザー識別IDを取得
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getPersonId());
        Long userId = mPerson.getUserId();
        //DBサーバ時刻取得
        Timestamp serverDateTime = getServerDateTime();

        MClaimantInfo mClaimantInfo = new MClaimantInfo();
        MClaimantInfoPK mClaimantInfoPK = new MClaimantInfoPK();

        mClaimantInfoPK.setCorpId(parameter.getCorpId());
        mClaimantInfoPK.setBuildingId(parameter.getBuildingId());
        mClaimantInfoPK.setPersonCorpId(parameter.getPersonCorpId());
        mClaimantInfoPK.setPersonId(parameter.getPersonId());
        mClaimantInfo.setId(mClaimantInfoPK);

        MClaimantInfo findMClaimantInfo = find(mClaimantInfoServiceDaoImpl, mClaimantInfo);

        ClaimantInfoResultData claimantInfoResultData = null;
        if(Objects.isNull(findMClaimantInfo) || Objects.isNull(findMClaimantInfo.getId().getCorpId())){
            mClaimantInfo.setRecDate(serverDateTime);
            mClaimantInfo.setRecMan(parameter.getPersonId());
            mClaimantInfo.setClaimantName1(parameter.getClaimantName());
            mClaimantInfo.setClaimantName2(parameter.getRegNo());
            mClaimantInfo.setUpdateUserId(userId);
            mClaimantInfo.setUpdateDate(serverDateTime);
            mClaimantInfo.setVersion(0);
            mClaimantInfo.setCreateUserId(userId);
            mClaimantInfo.setCreateDate(serverDateTime);

            persist(mClaimantInfoServiceDaoImpl, mClaimantInfo);

            claimantInfoResultData = new ClaimantInfoResultData(mClaimantInfo);
        }else{
            if(findMClaimantInfo.getVersion().equals(parameter.getVersion())){
                findMClaimantInfo.setRecDate(serverDateTime);
                findMClaimantInfo.setRecMan(parameter.getPersonId());
                findMClaimantInfo.setClaimantName1(parameter.getClaimantName());
                findMClaimantInfo.setClaimantName2(parameter.getRegNo());
                findMClaimantInfo.setVersion(findMClaimantInfo.getVersion() + 1);
                findMClaimantInfo.setUpdateUserId(userId);
                findMClaimantInfo.setUpdateDate(serverDateTime);

                merge(mClaimantInfoServiceDaoImpl, findMClaimantInfo);

                claimantInfoResultData = new ClaimantInfoResultData(findMClaimantInfo);
            }else{
                // 排他エラー
                throw new OptimisticLockException();
            }
        }

        UpdateSmsClaimantInfoResult result = new UpdateSmsClaimantInfoResult();
        result.setClaimantInfoResultData(claimantInfoResultData);

        return result;
    }
}
