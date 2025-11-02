package jp.co.osaki.osol.api.dao.sms.collect.setting.meterUser;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolEncipher;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.UpdateSmsMeterUserParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.UpdateSmsMeterUserResult;
import jp.co.osaki.osol.api.servicedao.entity.MCorpPersonAuthServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MCorpPersonServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MPersonApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MCorpPerson;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MCorpPersonAuthPK;
import jp.co.osaki.osol.entity.MCorpPersonPK;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

@Stateless
public class UpdateSmsMeterUserDao extends OsolApiDao<UpdateSmsMeterUserParameter> {

    @Inject
    private OsolEncipher osolEncipher;

    // 担当者情報
    private final MPersonApiServiceDaoImpl mPersonApiServiceDaoImpl;

    // 企業担当者情報
    private final MCorpPersonServiceDaoImpl mCorpPersonServiceDaoImpl;

    // 企業担当者権限情報
    private final MCorpPersonAuthServiceDaoImpl mCorpPersonAuthServiceDaoImpl;

    public UpdateSmsMeterUserDao() {
        mPersonApiServiceDaoImpl = new MPersonApiServiceDaoImpl();
        mCorpPersonServiceDaoImpl = new MCorpPersonServiceDaoImpl();
        mCorpPersonAuthServiceDaoImpl = new MCorpPersonAuthServiceDaoImpl();
    }

    @Override
    public UpdateSmsMeterUserResult query(UpdateSmsMeterUserParameter parameter) throws Exception {

        //担当者情報、企業担当者情報、企業担当者権限情報を登録・更新する
        UpdateSmsMeterUserResult result = new UpdateSmsMeterUserResult();
        Timestamp svDate = getServerDateTime();
        String strDate = DateUtility.changeDateFormat(svDate, DateUtility.DATE_FORMAT_YYYYMMDD);
        Date nowDate = DateUtility.conversionDate(strDate, DateUtility.DATE_FORMAT_YYYYMMDD);

        // API実施者情報
        MPerson user = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = 0L;
        if (user != null) {
            userId = user.getUserId();
        }


        // nullの場合は初期値"1"日として処理する
        if (parameter.getTempPassExpirationDate() == null) {
            parameter.setTempPassExpirationDate("1");
        }

        // 担当者情報
        MPersonPK mPersonPK = new MPersonPK();
        MPerson mPerson = new MPerson();
        mPersonPK.setCorpId(parameter.getCorpId());
        mPersonPK.setPersonId(parameter.getPersonId());
        mPerson.setId(mPersonPK);

        MPerson mPersonEntity = find(mPersonApiServiceDaoImpl, mPerson);

        // 更新
        if (mPersonEntity != null) {
            mPersonEntity.setPersonType(parameter.getPersonType());
            mPersonEntity.setPersonName(parameter.getPersonName());
            mPersonEntity.setPersonKana(parameter.getPersonKana());
            mPersonEntity.setDeptName(parameter.getDeptName());
            mPersonEntity.setPositionName(parameter.getPositionName());
            mPersonEntity.setTelNo(parameter.getTelNo());
            mPersonEntity.setFaxNo(parameter.getFaxNo());
            mPersonEntity.setMailAddress(parameter.getMailAddress());

            // 更新のアカウント停止フラグがONかつ、既存のアカウント停止フラグがOFFの場合
            if (OsolConstants.FLG_ON.equals(parameter.getAccountStopFlg())) {
                if (OsolConstants.FLG_OFF.equals(mPersonEntity.getAccountStopFlg())) {
                    mPersonEntity.setAccountStopFlg(OsolConstants.FLG_ON);
                    mPersonEntity.setAccountStopDate(svDate);
                }
            } else {
                mPersonEntity.setAccountStopFlg(OsolConstants.FLG_OFF);
            }

            // 自分自身の更新
            if (mPersonEntity.equals(user)) {
                if (!CheckUtility.isNullOrEmpty(parameter.getPassword())) {
                    mPersonEntity.setPassword(parameter.getPassword());
                    mPersonEntity.setPassMissCount(parameter.getPassMissCount());
                }
            } else {
                // 仮パスワードの入力がある場合ロック解除と有効期限を更新ミスカウントもリセット
                if (!CheckUtility.isNullOrEmpty(parameter.getTempPassword())) {
                    mPersonEntity.setPassword(parameter.getTempPassword());
                    mPersonEntity.setTempPassword(parameter.getTempPassword());
                    mPersonEntity.setLastLoginDate(null);
                    mPersonEntity.setPassMissCount(parameter.getPassMissCount());

                    // 日付加算して設定する
                    mPersonEntity.setTempPassExpirationDate(DateUtility.plusDay(nowDate, Integer.parseInt(parameter.getTempPassExpirationDate())));
                }
            }
            mPersonEntity.setUpdateUserId(userId);
            mPersonEntity.setUpdateDate(svDate);

            merge(mPersonApiServiceDaoImpl, mPersonEntity);
        }
        // 登録
        else {
            mPerson.setPersonType(OsolConstants.PERSON_TYPE.PERSON.getVal());
            mPerson.setPersonName(parameter.getPersonName());
            mPerson.setPersonKana(parameter.getPersonKana());
            mPerson.setDeptName(parameter.getDeptName());
            mPerson.setPositionName(parameter.getPositionName());
            mPerson.setTelNo(parameter.getTelNo());
            mPerson.setFaxNo(parameter.getFaxNo());
            mPerson.setMailAddress(parameter.getMailAddress());

            // 外部からの呼び出しで暗号化されていない
            if (parameter.getApiPrivateFlg() == null || parameter.getApiPrivateFlg() == Boolean.FALSE) {
                String hash = osolEncipher.hashing(parameter.getPassword());
                mPerson.setPassword(hash);
                mPerson.setTempPassword(hash);
            }
            // 内部からの呼び出しで暗号化されている
            else {
                mPerson.setPassword(parameter.getPassword());
                mPerson.setTempPassword(parameter.getTempPassword());
            }

            // 日付加算して設定する
            mPerson.setTempPassExpirationDate(DateUtility.plusDay(nowDate, Integer.parseInt(parameter.getTempPassExpirationDate())));

            // パスワード入力ミス回数
            mPerson.setPassMissCount(parameter.getPassMissCount());
            mPerson.setUpdatePassDate(svDate);
            // アカウント停止フラグ
            mPerson.setAccountStopFlg(parameter.getAccountStopFlg());
            // 削除フラグ
            mPerson.setDelFlg(OsolConstants.FLG_OFF);
            mPerson.setCreateUserId(userId);
            mPerson.setCreateDate(svDate);
            mPerson.setUpdateUserId(userId);
            mPerson.setUpdateDate(svDate);

            // 権限最終更新日時
            mPerson.setAuthLastUpdateDate(svDate);

            persist(mPersonApiServiceDaoImpl, mPerson);
        }

        // 企業担当者情報
        MCorpPersonPK mCorpPersonPK = new MCorpPersonPK();
        MCorpPerson mCorpPerson = new MCorpPerson();
        mCorpPersonPK.setCorpId(parameter.getCorpId());
        mCorpPersonPK.setPersonCorpId(parameter.getCorpId());
        mCorpPersonPK.setPersonId(parameter.getPersonId());
        mCorpPerson.setId(mCorpPersonPK);

        MCorpPerson mCorpPersonEntity = find(mCorpPersonServiceDaoImpl, mCorpPerson);

        // 更新
        if (mCorpPersonEntity != null) {
            // 更新情報
            mCorpPersonEntity.setUpdateDate(svDate);
            mCorpPersonEntity.setUpdateUserId(userId);
            merge(mCorpPersonServiceDaoImpl, mCorpPersonEntity);
        }
        // 新規
        else {
            mCorpPerson.setAuthorityType(OsolConstants.AUTHORITY_TYPE.BUILDING.getVal());

            // 削除フラグ
            mCorpPerson.setDelFlg(OsolConstants.FLG_OFF);
            mCorpPerson.setCreateDate(svDate);
            mCorpPerson.setCreateUserId(userId);
            mCorpPerson.setUpdateDate(svDate);
            mCorpPerson.setUpdateUserId(userId);

            persist(mCorpPersonServiceDaoImpl, mCorpPerson);
        }


        // 企業担当者権限情報
        for (int i = 1; i <= OsolConstants.USER_AUTHORITY.values().length; i++) {
            MCorpPersonAuthPK mCorpPersonAuthPK = new MCorpPersonAuthPK();
            MCorpPersonAuth mCorpPersonAuth = new MCorpPersonAuth();
            mCorpPersonAuthPK.setCorpId(parameter.getCorpId());
            mCorpPersonAuthPK.setPersonCorpId(parameter.getCorpId());
            mCorpPersonAuthPK.setPersonId(parameter.getPersonId());

            // 権限コード
            mCorpPersonAuthPK.setAuthorityCd(String.valueOf(1000 + i).substring(1));
            mCorpPersonAuth.setId(mCorpPersonAuthPK);
            MCorpPersonAuth mCorpPersonAuthEntity = find(mCorpPersonAuthServiceDaoImpl, mCorpPersonAuth);

            // 更新
            if (mCorpPersonAuthEntity != null) {
                mCorpPersonAuthEntity.setUpdateDate(svDate);
                mCorpPersonAuthEntity.setUpdateUserId(userId);
                merge(mCorpPersonAuthServiceDaoImpl, mCorpPersonAuthEntity);
            }
            // 新規
            else {
                // SMS権限のみON
                if (OsolConstants.USER_AUTHORITY.SMS.getVal().equals(String.valueOf(1000 + i).substring(1))) {
                    mCorpPersonAuth.setAuthorityFlg(OsolConstants.FLG_ON);
                } else {
                    mCorpPersonAuth.setAuthorityFlg(OsolConstants.FLG_OFF);
                }

                // 削除フラグ
                mCorpPersonAuth.setDelFlg(OsolConstants.FLG_OFF);
                mCorpPersonAuth.setCreateDate(svDate);
                mCorpPersonAuth.setCreateUserId(userId);
                mCorpPersonAuth.setUpdateDate(svDate);
                mCorpPersonAuth.setUpdateUserId(userId);

                persist(mCorpPersonAuthServiceDaoImpl, mCorpPersonAuth);
            }
        }

        // 最新の担当者情報を取得
        mPersonPK = new MPersonPK();
        mPerson = new MPerson();
        mPersonPK.setCorpId(parameter.getCorpId());
        mPersonPK.setPersonId(parameter.getPersonId());
        mPerson.setId(mPersonPK);

        mPersonEntity = find(mPersonApiServiceDaoImpl, mPerson);
        result.setCorpId(mPersonEntity.getId().getCorpId());
        result.setPersonId(mPersonEntity.getId().getPersonId());
        result.setUserId(mPersonEntity.getUserId());
        result.setPersonType(mPersonEntity.getPersonType());
        result.setPersonName(mPersonEntity.getPersonName());
        result.setPersonKana(mPersonEntity.getPersonKana());
        result.setDeptName(mPersonEntity.getDeptName());
        result.setPositionName(mPersonEntity.getPositionName());
        result.setTelNo(mPersonEntity.getTelNo());
        result.setFaxNo(mPersonEntity.getFaxNo());
        result.setMailAddress(mPersonEntity.getMailAddress());
        result.setPassword(mPersonEntity.getPassword());
        result.setPassMissCount(mPersonEntity.getPassMissCount());
        result.setUpdatePassDate(mPersonEntity.getUpdatePassDate());
        result.setTempPassExpirationDate(mPersonEntity.getTempPassExpirationDate());
        result.setTempPassword(mPersonEntity.getTempPassword());
        result.setLastLoginDate(mPersonEntity.getLastLoginDate());
        result.setAccountStopFlg(mPersonEntity.getAccountStopFlg());
        result.setAccountStopDate(mPersonEntity.getAccountStopDate());
        result.setAuthLastUpdateDate(mPersonEntity.getAuthLastUpdateDate());
        result.setLastOshiraseCheckTime(mPersonEntity.getLastOshiraseCheckTime());

        return result;
    }
}
