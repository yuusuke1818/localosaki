package jp.co.osaki.osol.api.dao.osolapi;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.resultdata.osolapi.OsolApiUseResultListDetailResultData;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiAlertMailSettingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiUseResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.osolapi.OsolApiUseSettingServiceDaoImpl;
import jp.co.osaki.osol.entity.TApiAlertMailSetting;
import jp.co.osaki.osol.entity.TApiAlertMailSettingPK;
import jp.co.osaki.osol.entity.TApiUseSetting;
import jp.co.osaki.osol.entity.TApiUseSettingPK;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiDao;

/**
 * API利用関連
 *
 */
@Stateless
public class OsolApiUseManagementDao extends BaseApiDao {

    private final OsolApiUseResultServiceDaoImpl osolApiUseResultServiceDaoImpl;
    private final OsolApiUseSettingServiceDaoImpl osolApiUseSettingServiceDaoImpl;
    private final OsolApiAlertMailSettingServiceDaoImpl osolApiAlertMailSettingServiceDaoImpl;

    /**
     * コンストラクタ
     */
    public OsolApiUseManagementDao() {
        osolApiUseResultServiceDaoImpl = new OsolApiUseResultServiceDaoImpl();
        osolApiUseSettingServiceDaoImpl = new OsolApiUseSettingServiceDaoImpl();
        osolApiAlertMailSettingServiceDaoImpl = new OsolApiAlertMailSettingServiceDaoImpl();
    }

    /**
     * API利用実績を取得
     *
     * @param corpId
     * @param apiKind
     * @param userId
     */
    public List<OsolApiUseResultListDetailResultData> getApiUseResultList(String corpId, String apiKind, Date useDateFrom, Date useDateTo) {

        OsolApiUseResultListDetailResultData apiUseResult = new OsolApiUseResultListDetailResultData();
        apiUseResult.setCorpId(corpId);
        apiUseResult.setApiKind(apiKind);
        apiUseResult.setUseDateFrom(useDateFrom);
        apiUseResult.setUseDateTo(useDateTo);

        List<OsolApiUseResultListDetailResultData> result = getResultList(osolApiUseResultServiceDaoImpl, apiUseResult);

        return result;
    }

    /**
     * API利用実績を作成、また更新する。
     *
     * @param corpId
     * @param apiKind
     * @param userId
     */
    public void createApiUseResult(String corpId, String apiKind, Long userId) {

        OsolApiUseResultListDetailResultData apiUseResult = new OsolApiUseResultListDetailResultData();

        Timestamp svDate = getServerDateTime();
        Date useDate = DateUtility.conversionDate(
                DateUtility.changeDateFormat(new Date(svDate.getTime()), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD);

        apiUseResult.setCorpId(corpId);
        apiUseResult.setUseDate(useDate);
        apiUseResult.setApiKind(apiKind);

        // 対象レコードを取得
        OsolApiUseResultListDetailResultData updTApiUseResult = find(osolApiUseResultServiceDaoImpl, apiUseResult);

        // レコードが存在する（更新）
        if (updTApiUseResult != null) {
            updTApiUseResult.setApiCount(new Integer(updTApiUseResult.getApiCount().intValue() + 1));
            updTApiUseResult.setVersion(new Integer(updTApiUseResult.getVersion().intValue() + 1));
            updTApiUseResult.setUpdateUserId(userId);
            updTApiUseResult.setUpdateDate(new Date(svDate.getTime()));
            merge(osolApiUseResultServiceDaoImpl, updTApiUseResult);
        }
        // レコードが存在しない（新規追加）
        else {
            OsolApiUseResultListDetailResultData createApiUseResult = new OsolApiUseResultListDetailResultData();
            createApiUseResult.setCorpId(corpId);
            createApiUseResult.setUseDate(useDate);
            createApiUseResult.setApiKind(apiKind);
            createApiUseResult.setApiCount(1);
            createApiUseResult.setVersion(0);
            createApiUseResult.setCreateUserId(userId);
            createApiUseResult.setUpdateUserId(userId);
            createApiUseResult.setCreateDate(new Date(svDate.getTime()));
            createApiUseResult.setUpdateDate(new Date(svDate.getTime()));
            persist(osolApiUseResultServiceDaoImpl, createApiUseResult);
        }
    }

    /**
     * API利用設定を取得する
     *
     * @param corpId
     * @param apiKind
     */
    public TApiUseSetting getApiUseSetting(String corpId, String apiKind) {

        TApiUseSettingPK tApiUseSettingPK = new TApiUseSettingPK();
        TApiUseSetting tApiUseSetting = new TApiUseSetting();

        tApiUseSettingPK.setCorpId(corpId);
        tApiUseSettingPK.setApiKind(apiKind);
        tApiUseSetting.setId(tApiUseSettingPK);

        return find(osolApiUseSettingServiceDaoImpl, tApiUseSetting);
    }

    /**
     * API警告メール送信設定を取得する
     *
     * @param corpId
     * @param apiKind
     */
    public List<TApiAlertMailSetting> getApiAlertMailSettingList(String corpId) {

        TApiAlertMailSettingPK tApiAlertMailSettingPK = new TApiAlertMailSettingPK();
        TApiAlertMailSetting tApiAlertMailSetting = new TApiAlertMailSetting();

        tApiAlertMailSettingPK.setCorpId(corpId);
        tApiAlertMailSetting.setId(tApiAlertMailSettingPK);

        return getResultList(osolApiAlertMailSettingServiceDaoImpl, tApiAlertMailSetting);
    }
}
