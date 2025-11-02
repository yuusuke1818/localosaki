package jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.GetSmsMeterUserParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.GetSmsMeterUserResult;
import jp.co.osaki.osol.api.servicedao.entity.MPersonApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;

@Stateless
public class GetSmsMeterUserDao extends OsolApiDao<GetSmsMeterUserParameter> {

    // 担当者
    private final MPersonApiServiceDaoImpl mPersonApiServiceDaoImpl;

    public GetSmsMeterUserDao() {
        mPersonApiServiceDaoImpl = new MPersonApiServiceDaoImpl();
    }

    @Override
    public GetSmsMeterUserResult query(GetSmsMeterUserParameter parameter) throws Exception {
        MPersonPK mPersonPK = new MPersonPK();
        MPerson mPerson = new MPerson();
        mPersonPK.setCorpId(parameter.getCorpId());
        mPersonPK.setPersonId(parameter.getPersonId());
        mPerson.setId(mPersonPK);
        MPerson entity = find(mPersonApiServiceDaoImpl, mPerson);

        if (entity == null) {
            return null;
        }

        GetSmsMeterUserResult result = new GetSmsMeterUserResult();
        result.setCorpId(entity.getId().getCorpId());
        result.setPersonId(entity.getId().getPersonId());
        result.setUserId(entity.getUserId());
        result.setPersonType(entity.getPersonType());
        result.setPersonName(entity.getPersonName());
        result.setPersonKana(entity.getPersonKana());
        result.setDeptName(entity.getDeptName());
        result.setPositionName(entity.getPositionName());
        result.setTelNo(entity.getTelNo());
        result.setFaxNo(entity.getFaxNo());
        result.setMailAddress(entity.getMailAddress());
        result.setPassword(entity.getPassword());
        result.setPassMissCount(entity.getPassMissCount());
        result.setUpdatePassDate(entity.getUpdatePassDate());
        result.setTempPassExpirationDate(entity.getTempPassExpirationDate());
        result.setTempPassword(entity.getTempPassword());
        result.setLastLoginDate(entity.getLastLoginDate());
        result.setAccountStopFlg(entity.getAccountStopFlg());
        result.setAccountStopDate(entity.getAccountStopDate());
        result.setAuthLastUpdateDate(entity.getAuthLastUpdateDate());
        result.setLastOshiraseCheckTime(entity.getLastOshiraseCheckTime());

        return result;
    }
}
