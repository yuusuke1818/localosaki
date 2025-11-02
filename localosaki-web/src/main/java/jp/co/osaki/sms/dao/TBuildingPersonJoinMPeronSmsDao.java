package jp.co.osaki.sms.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.entity.TBuildingPerson;
import jp.co.osaki.osol.entity.TBuildingPersonPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.bean.sms.collect.setting.meterUser.TenantPersonDispBean;
import jp.co.osaki.sms.servicedao.MPersonServiceDaoImpl;
import jp.co.osaki.sms.servicedao.TBuildingPersonJoinMPeronServiceDaoImpl;

@Stateless
public class TBuildingPersonJoinMPeronSmsDao extends SmsDao {

    private final TBuildingPersonJoinMPeronServiceDaoImpl tBuildingPersonJoinMPeronServiceDaoImpl;
    private final MPersonServiceDaoImpl mpersonImpl;

    public TBuildingPersonJoinMPeronSmsDao() {
        this.tBuildingPersonJoinMPeronServiceDaoImpl = new TBuildingPersonJoinMPeronServiceDaoImpl();
        this.mpersonImpl = new MPersonServiceDaoImpl();
    }

    /**
     * 担当建物（テナント）情報 登録更新処理
     *
     * @param targetList 担当建物情報List
     * @param loginUserId ログインユーザーID
     * @param selectCorpId 登録対象ユーザー企業ID
     * @param selectPersonId 登録対象ユーザーID
     */
    public void execTBuildingPerson(List<TenantPersonDispBean> targetList, Long loginUserId, String selectCorpId, String selectPersonId) {

        Timestamp svDate = getSvDate();

        // 更新対象
        for (TenantPersonDispBean target : targetList) {

            if (target.gettBuildingPerson() == null) {
                // 担当建物情報がない場合
                if (target.isBuildingPersonFlg()) {
                    // 登録処理
                    registTBuildingPerson(target, loginUserId, selectCorpId, selectPersonId, svDate);
                    updateMpersonForAuthLastDate(loginUserId, selectCorpId, selectPersonId, svDate);
                }
            } else {
                // 担当建物情報がある場合
                TBuildingPerson tbp = target.gettBuildingPerson();
                if (!target.isBuildingPersonFlg() && tbp.getDelFlg() == 0) {
                    // 担当設定がされていない 且つ 削除フラグOFFの場合
                    // 更新処理
                    updateDelOnTBuildingPerson(tbp, loginUserId, svDate);
                    updateMpersonForAuthLastDate(loginUserId, selectCorpId, selectPersonId, svDate);

                } else if (target.isBuildingPersonFlg() && tbp.getDelFlg() == 1) {
                    // 担当設定がされている 且つ 削除フラグONの場合
                    // 更新処理
                    updateDelOffTBuildingPerson(tbp, loginUserId, svDate);
                    updateMpersonForAuthLastDate(loginUserId, selectCorpId, selectPersonId, svDate);
                }
            }
        }
    }

    // 担当建物リスト
    public List<TBuildingPerson> getTBuildingPersonList(String selectCorpId, String selectPersonId) {
        TBuildingPerson tBuildingPerson = new TBuildingPerson();
        TBuildingPersonPK tBuildingPersonPK = new TBuildingPersonPK();
        tBuildingPersonPK.setPersonCorpId(selectCorpId);
        tBuildingPersonPK.setPersonId(selectPersonId);
        tBuildingPerson.setId(tBuildingPersonPK);

        List<TBuildingPerson> resultList = getResultList(tBuildingPersonJoinMPeronServiceDaoImpl, tBuildingPerson);
        return resultList;
    }

    /**
     * 担当建物情報登録
     *
     * @param target 担当建物画面Bean
     * @param loginUserId　ログインユーザーID
     * @param selectCorpId 登録対象企業ID
     * @param selectPersonId 登録対象担当者ID
     * @param svDate　現在日時
     */
    private void registTBuildingPerson(TenantPersonDispBean target, Long loginUserId, String selectCorpId, String selectPersonId, Timestamp svDate) {
        // 担当テナントが設定されている場合(新規登録)
        TBuildingPerson tbp = new TBuildingPerson();
        TBuildingPersonPK tbpPk = new TBuildingPersonPK();

        tbpPk.setCorpId(target.gettBuilding().getId().getCorpId());
        tbpPk.setBuildingId(target.gettBuilding().getId().getBuildingId());
        tbpPk.setPersonCorpId(selectCorpId);
        tbpPk.setPersonId(selectPersonId);
        tbp.setId(tbpPk);

        tbp.setCreateDate(svDate);
        tbp.setCreateUserId(loginUserId);
        tbp.setUpdateDate(svDate);
        tbp.setUpdateUserId(loginUserId);

        // 削除フラグ
        tbp.setDelFlg(OsolConstants.FLG_OFF);

        // 登録処理
        persist(tBuildingPersonJoinMPeronServiceDaoImpl, tbp);

    }

    /**
     * 担当建物情報更新(削除フラグON更新)
     *
     * @param tbp 担当建物情報
     * @param loginUserId 担当建物情報
     * @param svDate 現在日時
     */
    private TBuildingPerson updateDelOnTBuildingPerson(TBuildingPerson tbp, Long loginUserId, Timestamp svDate) {
        // 削除フラグON
        tbp.setDelFlg(OsolConstants.FLG_ON);

        // 更新日時
        tbp.setUpdateDate(svDate);
        // 更新ユーザー
        tbp.setUpdateUserId(loginUserId);

        // 更新処理
        return merge(tBuildingPersonJoinMPeronServiceDaoImpl, tbp);

    }

    /**
     * 担当建物情報更新(削除フラグOFF更新)
     *
     * @param tbp 担当建物情報
     * @param loginUserId 担当建物情報
     * @param svDate 現在日時
     */
    private TBuildingPerson updateDelOffTBuildingPerson(TBuildingPerson tbp, Long loginUserId, Timestamp svDate) {
        tbp.setDelFlg(OsolConstants.FLG_OFF);

        // 更新日時
        tbp.setUpdateDate(svDate);
        // 更新ユーザー
        tbp.setUpdateUserId(loginUserId);

        // 更新処理
        return merge(tBuildingPersonJoinMPeronServiceDaoImpl, tbp);
    }

    /**
     * 担当者情報-権限最終更新日時更新
     *
     *
     */
    private MPerson updateMpersonForAuthLastDate(Long loginUserId, String selectCorpId, String selectPersonId, Timestamp svDate) {
        // 担当者の現在の情報を取得
        MPerson mPerson = new MPerson();
        MPersonPK mPersonPK = new MPersonPK();
        mPersonPK.setCorpId(selectCorpId);
        mPersonPK.setPersonId(selectPersonId);
        mPerson.setId(mPersonPK);

        // 取得
        MPerson result = find(mpersonImpl, mPerson);

        // 権限最終更新日時
        result.setAuthLastUpdateDate(svDate);
        // 更新日時
        result.setUpdateDate(svDate);
        // 更新ユーザー
        result.setUpdateUserId(loginUserId);

        // 更新
        return merge(mpersonImpl, result);
    }
}
