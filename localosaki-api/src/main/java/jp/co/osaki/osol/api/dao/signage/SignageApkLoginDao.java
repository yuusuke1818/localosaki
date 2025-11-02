package jp.co.osaki.osol.api.dao.signage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolEncipher;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageApkLoginParameter;
import jp.co.osaki.osol.api.result.signage.SignageApkLoginResult;
import jp.co.osaki.osol.api.servicedao.entity.MPersonApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingApiServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * アプリからログインするときに利用 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class SignageApkLoginDao extends OsolApiDao<SignageApkLoginParameter> {

    //TODO できればEntityServideDaoを使わない
    private final MPersonApiServiceDaoImpl personImpl;
    private final TBuildingApiServiceDaoImpl buildingImpl;

    @Inject
    private OsolConfigs osolConfigs;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @Inject
    private OsolEncipher osolEncipher;

    public SignageApkLoginDao() {
        personImpl = new MPersonApiServiceDaoImpl();
        buildingImpl = new TBuildingApiServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SignageApkLoginResult query(SignageApkLoginParameter parameter) throws Exception {

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        SignageApkLoginResult result = new SignageApkLoginResult();

        if (!login(parameter.getLoginCorpId(), parameter.getOperationCorpId(), parameter.getLoginPersonId(),
                parameter.getPassword(), serverDateTime, loginUserId)) {
            //ログインに失敗
            result.setBuildingId(null);
            return result;
        }

        Long buildingId = getBuildingId(parameter.getLoginCorpId(), parameter.getOperationCorpId(),
                parameter.getBuildingNo(), parameter.getLoginPersonId());

        result.setBuildingId(buildingId);
        return result;
    }

    /**
     * ログイン処理を行う
     * @param corpId
     * @param operationCorpId
     * @param personId
     * @param password
     * @param serverDateTime
     * @param loginUserId
     * @return
     */
    private boolean login(String corpId, String operationCorpId, String personId, String password,
            Timestamp serverDateTime, Long loginUserId) {

        MPerson entity = new MPerson();
        MPersonPK id = new MPersonPK();
        id.setCorpId(corpId);
        id.setPersonId(personId);
        entity.setId(id);
        MPerson person = find(personImpl, entity);
        if (person == null) {
            return false;
        }

        // 削除、アカウント停止、10回パスワード間違いを確認
        if (1 == person.getDelFlg() || 1 == person.getAccountStopFlg()
                || OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT <= person.getPassMissCount()) {
            return false;
        }

        // 企業が利用停止期間中かを判定
        if (person.getMCorp().getUseStopStartDate() != null) {

            //日付以下を切り捨てる
            final long now = DateUtility
                    .conversionDate(DateUtility.changeDateFormat(serverDateTime, DateUtility.DATE_FORMAT_YYYYMMDD),
                            DateUtility.DATE_FORMAT_YYYYMMDD)
                    .getTime();

            // 現在日付が利用停止開始日より未来、且つ利用停止終了日より過去であれば利用停止期間中のためログイン不可
            // 現在日付が利用停止開始日より未来で利用停止終了日が空の場合もログイン不可。
            if (now >= person.getMCorp().getUseStopStartDate().getTime()) {
                if (person.getMCorp().getUseStopEndDate() == null ||
                        now <= person.getMCorp().getUseStopEndDate().getTime()) {
                    return false;
                }
            }
        }

        // パスワード状態の判定
        String dbPassword;
        if (null == person.getTempPassword()) {
            // 仮パスワードがNULLの場合を正規パスワードと判断
            // 正規パスワード
            dbPassword = person.getPassword();

        } else {
            return false;
        }

        // パスワードチェック
        if (!osolEncipher.verify(password, dbPassword)) {
            Integer count = person.getPassMissCount() + 1;
            //10回パスワード間違いでアカウントロック。メッセージは理由を特定させない為のふわっとした共通のものを出す。
            if (count == OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {
                person.setPassMissCount(count);
                person.setUpdateDate(serverDateTime);
                person.setUpdateUserId(loginUserId);
                merge(personImpl, person);
                return false;
            }
            // カウントアップし対象ユーザーを更新
            person.setPassMissCount(count);
            person.setUpdateDate(serverDateTime);
            person.setUpdateUserId(loginUserId);
            merge(personImpl, person);

            return false;
        }

        person.setPassMissCount(0);
        person.setLastLoginDate(serverDateTime);
        person.setUpdateDate(serverDateTime);
        person.setUpdateUserId(loginUserId);
        merge(personImpl, person);

        // パスワード最終更新時間チェック
        String passwordLimitDays = osolConfigs.getConfig(OsolConstants.LOGIN_PASS_EXPIRED_DAYS_ON_CONFIG);
        if (!CheckUtility.isNullOrEmpty(passwordLimitDays)
                && checkPassExpirationDate(person.getUpdatePassDate(), Integer.parseInt(passwordLimitDays),
                        serverDateTime)) {
            return false;
        }

        return true;

    }

    /**
     * パスワード有効期間チェック
     *
     * @param timestamp
     * @param passwordStopDays
     * @param serverDateTime
     * @return true:有効期限切れ false:有効期限内
     */
    private boolean checkPassExpirationDate(Timestamp timestamp, int passwordStopDays, Timestamp serverDateTime) {
        boolean passExpirationDate;
        // 今日
        Date today = DateUtility.conversionDate(
                DateUtility.changeDateFormat(serverDateTime, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        // 最終更新日時
        Date lastUpdate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(timestamp, DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);
        // 利用制限日
        Date stopUseDate = DateUtility.plusDay(lastUpdate, passwordStopDays);

        if (stopUseDate.compareTo(today) < 0) {
            passExpirationDate = true;
        } else {
            passExpirationDate = false;
        }
        return passExpirationDate;
    }

    /**
     * 建物IDを取得する
     * @param corpId
     * @param operationCorpId
     * @param buildingNo
     * @param personId
     * @return
     */
    private Long getBuildingId(String corpId, String operationCorpId, String buildingNo, String personId) {
        TBuilding entity = new TBuilding();
        TBuildingPK id = new TBuildingPK();
        id.setCorpId(corpId);
        entity.setId(id);
        entity.setBuildingNo(buildingNo);
        TBuilding building = find(buildingImpl, entity);
        if (building == null) {
            return null;
        }
        // フィルター処理
        List<TBuilding> buildingList = new ArrayList<>();
        buildingList.add(building);
        buildingList = buildingDataFilterDao.applyDataFilter(buildingList, new PersonDataParam(corpId, personId));
        if (buildingList.isEmpty()) {
            return null;
        } else {
            return buildingList.get(0).getId().getBuildingId();
        }
    }

}
