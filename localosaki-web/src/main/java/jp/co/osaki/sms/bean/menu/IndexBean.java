package jp.co.osaki.sms.bean.menu;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.function.bean.OsolAccessBean;
import jp.co.osaki.osol.access.function.resultset.AccessResultSet;
import jp.co.osaki.osol.api.parameter.sample.SampleApiParameter;
import jp.co.osaki.osol.api.request.sample.SampleApiRequest;
import jp.co.osaki.osol.api.request.sample.SampleApiRequestSet;
import jp.co.osaki.osol.api.response.sample.SampleApiResponse;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TOshirase;
import jp.co.osaki.osol.utility.HostInfo;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationEvent;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.dao.MPersonDao;
import jp.co.osaki.sms.dao.TOshiraseDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * メインメニュー
 *
 * @author take_suzuki
 */
@Named(value = "indexBean")
@ConversationScoped
public class IndexBean extends SmsBean implements Serializable {

    //シリアライズID
    private static final long serialVersionUID = 5622838071365230231L;

    //お知らせのリスト
    private List<TOshirase> oshiraseList;

    //お知らせリスト(表示用)
    private List<OshiraseDisp> desp_list;

    //お知らせリスト(表示用・ポップアップ用)
    private List<OshiraseDisp> popupDispList;

    @Inject
    private Event<SmsConversationEvent> event;

    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    //ファイルダウンロード処理
    @Inject
    private SmsFileDownload smsFileDownload;

    @EJB
    TOshiraseDao tOshiraseDao;

    @EJB
    MPersonDao mPersonDao;

    private boolean lastOshiraseCheckFlg;

    @Inject
    private OsolAccessBean osolAccessBean;

    @Override
    @Logged
    public String init() {

        //全OsolConversationBeanの会話を終了させる
        SmsConversationEvent param = new SmsConversationEvent();
        event.fire(param);
        return "top";
    }

    //企業操作終了
    public String corpEditExit() {

        //操作中企業を自企業に戻す
        this.setLoginOperationCorp(this.getLoginCorp());

        return init();
    }

    //お知らせリスト(表示用)の取得
    public List<OshiraseDisp> getDesp_list() {
        eventLogger.debug(this.getClass().getName().concat(".getDesp_list():START"));
        boolean checkFlg = false;
        lastOshiraseCheckFlg = getLoginPerson().getLastOshiraseCheckTime() != null;
        if (oshiraseList == null) {
            Date date = tOshiraseDao.getSvDate();
            desp_list = new ArrayList<>();
            popupDispList = new ArrayList<>();

            //お知らせの取得
            oshiraseList = tOshiraseDao.getMenuDispOshiraseList(this.getLoginOperationCorpId());
            if (oshiraseList == null || oshiraseList.isEmpty()) {
                // お知らせなし
                OshiraseDisp disp = new OshiraseDisp();
                disp.setDispIconFlg(false);
                disp.setNonRecordFlg(true);
                disp.setPublishedDay(beanMessages.getMessage("indexBean.oshirase.nonRecordMessages"));
                disp.setTitle("");
                disp.setExternalSiteFlg(-1);
                desp_list.add(disp);
                popupDispList.add(disp);
            } else {
                for (TOshirase oshirase : oshiraseList) {
                    if (isOshiraseShow(oshirase)) {
                        checkFlg = true;
                    }
                    desp_list.add(convertDespList(oshirase, date));
                }
                if (checkFlg) {
                    popupDispList.addAll(desp_list);
                }
            }

        }
        eventLogger.debug(this.getClass().getName().concat(".getDesp_list():END"));
        return desp_list;
    }

    /**
     * お知らせリストの一件ずつに対して、お知らせポップアップを表示するかどうかを確認する。<br>
     * 一つでも表示条件に当てはまれば、ポップアップを表示する。<br>
     * <br>
     * ・「担当者.最終お知らせ確認日時」が未設定の場合<br>
     * →お知らせポップアップを表示<br>
     * <br>
     * ・「お知らせ.掲載開始日」が、実時間より未来の場合<br>
     * →お知らせポップアップを表示しない<br>
     * <br>
     * ・「お知らせ.掲載終了日」が、実時間より過去の場合<br>
     * →お知らせポップアップを表示しない<br>
     * <br>
     * ・「お知らせ.掲載終了日」が未設定または、実時間より未来の場合<br>
     * かつ、「担当者.最終お知らせ確認日時」が「お知らせ.更新日時」より過去<br>
     * または「担当者.最終お知らせ確認日時」が「お知らせ.掲載開始日」と同じ日の場合<br>
     * →お知らせポップアップを表示<br>
     *
     * @param oshirase
     * @return
     */
    private boolean isOshiraseShow(TOshirase oshirase) {
        MPerson mPerson = getLoginPerson();
        Timestamp now = mPersonDao.getSvDate();
        Timestamp ts = mPerson.getLastOshiraseCheckTime();
        Date startDay = oshirase.getPublishedStartDay();
        Date endDay = oshirase.getPublishedEndDay();
        Timestamp lastUpdateTs = oshirase.getUpdateDate();
        if (ts == null) {
            return true;
        }
        if (now.before(startDay)) {
            return false;
        }
        if (endDay != null && now.after(endDay)) {
            return false;
        }
        if (ts.before(lastUpdateTs)
                || (!ts.after(startDay) && !ts.before(startDay))) {
            return true;
        }
        return false;
    }

    /**
     * お知らせリスト→お知らせリスト(表示用)の変換
     *
     * @param info
     * @param now
     * @return
     */
    private OshiraseDisp convertDespList(TOshirase info, Date now) {

        OshiraseDisp disp = new OshiraseDisp();
        disp.setDispIconFlg(false);
        disp.setExternalSiteFlg(info.getExternalSiteFlg());
        disp.setNonRecordFlg(false);
        disp.setTitle(info.getTitle());
        disp.setMarkCd(info.getMarkCode());
        switch (info.getMarkCode()) {
        case "00": //マークコード 00:掲載開始日から1週間まで、NEW
            long nowTime = now.getTime();
            long startDayTime = info.getPublishedStartDay().getTime();
            long oneDayTime = 1000 * 60 * 60 * 24;
            long diffDays = (nowTime - startDayTime) / oneDayTime;
            // 公開開始から一週間までNewを表示
            if (diffDays <= 7) {
                //アイコン表示フラグ
                disp.setDispIconFlg(true);
                //アイコンファイルパス
                disp.setDispIconFilePath("menu/menu_index011.png");
            }
            break;
        case "01": //01:緊急
            //アイコン表示フラグ
            disp.setDispIconFlg(true);
            //アイコンファイルパス
            disp.setDispIconFilePath("menu/menu_index012.png");
            break;
        case "02": //02:重要
            //アイコン表示フラグ
            disp.setDispIconFlg(true);
            //アイコンファイルパス
            disp.setDispIconFilePath("menu/menu_index013.png");
            break;
        case "03": //03:要確認
            //アイコン表示フラグ
            disp.setDispIconFlg(true);
            //アイコンファイルパス
            disp.setDispIconFilePath("menu/menu_index014.png");
            break;
        default:
            //アイコン表示フラグ
            disp.setDispIconFlg(false);
            break;
        }
        //掲載日
        disp.setPublishedDay(new SimpleDateFormat("yyyy.MM.dd").format(info.getPublishedStartDay()));
        if (info.getExternalSiteFlg() == 0) {
            // ファイルダウンロード
            disp.setFileName(info.getFileName());
            disp.setSaveFileName(info.getSaveFilePath());
        } else if (info.getExternalSiteFlg() == 1) {
            //外部サイトフラグ
            disp.setUrl(info.getUrl());
        }
        return disp;
    }

    /**
     * 掲載開始日のスタイルを取得 <br>
     * 表示できるお知らせが無い場合は、空のスタイルを返却
     * @param externalSiteFlg
     * @return style情報を返却
     */
    public String getPublishedDayStyle(Integer externalSiteFlg) {
        return externalSiteFlg == -1 ? "" : "width: 100px;";
    }

    /**
     * お知らせ項目をファイルダウンロード形式で表示できるか取得
     * @param externalSiteFlg
     * @return true:表示、false:非表示
     */
    public boolean isOshiraseFileDownloadVisible(Integer externalSiteFlg) {
        // ファイルダウンロード
        return externalSiteFlg == 0 ? true : false;
    }

    /**
     * お知らせ項目を外部サイトへのページ遷移（Link）形式で表示できるか取得
     * @param externalSiteFlg
     * @return true:表示、false:非表示
     */
    public boolean isOshiraseExternalSiteVisible(Integer externalSiteFlg) {
        // ページ遷移
        return externalSiteFlg == 1 ? true : false;

    }

    /**
     * お知らせ項目をテキスト形式で表示できるか取得
     * @param externalSiteFlg
     * @return true:表示、false:非表示
     */
    public boolean isOshiraseTextVisible(Integer externalSiteFlg) {
        // テキスト表示
        return externalSiteFlg == 2 ? true : false;
    }

    /**
     * ファイルダウンロード処理
     *
     * @param item
     */
    public void execFileDownload(OshiraseDisp item) {
        // アクセスログ出力
        exportAccessLog("execFileDownload", "お知らせダウンロードリンク押下");

        eventLogger.debug(this.getClass().getName().concat(".execFileDownload():START"));
        int ret = smsFileDownload.S3fileDownload(item.getSaveFileName(), item.getFileName());
        if (ret != RETURN_CODE.SUCCESS.getInt()) {
            eventLogger.debug(this.getClass().getName().concat(".execFileDownload:Error ret(" + ret + ")"));
            addErrorMessage(beanMessages.getMessage("osol.error.fileDownload"));
        }
        eventLogger.debug(this.getClass().getName().concat(".execFileDownload():END"));
    }

    //パスワード暗号化複合化表示有無
    public boolean getEncryptionToolsVisible() {
        return osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE");
    }

    //パスワード暗号化処理(全ユーザー)
    @Deprecated
    public String passwordEncryptionAll() {
        // アクセスログ出力
        exportAccessLog("passwordEncryptionAll", "ボタン「パスワード暗号化(全ユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            List<MPerson> mPersonList = mPersonDao.searchPersonAll();
            for (MPerson a : mPersonList) {
                if (a.getPassword() != null && !a.getPassword().equals(STR_EMPTY)) {
                    a.setPassword(BaseUtility.encryptionString(a.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                if (a.getTempPassword() != null && !a.getTempPassword().equals(STR_EMPTY)) {
                    a.setTempPassword(BaseUtility.encryptionString(a.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                mPersonDao.merge(a);
            }
        }
        return STR_EMPTY;
    }

    //パスワード暗号化処理(ユーザー指定)
    @Deprecated
    public String passwordEncryptionForPerson(String corpId, String personId) {
        // アクセスログ出力
        exportAccessLog("passwordEncryptionForPerson", "ボタン「パスワード暗号化(ログインユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            MPerson mPerson = mPersonDao.find(corpId, personId);
            if (mPerson != null) {
                if (mPerson.getPassword() != null && !mPerson.getPassword().equals(STR_EMPTY)) {
                    mPerson.setPassword(BaseUtility.encryptionString(mPerson.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                if (mPerson.getTempPassword() != null && !mPerson.getTempPassword().equals(STR_EMPTY)) {
                    mPerson.setTempPassword(BaseUtility.encryptionString(mPerson.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                mPersonDao.merge(mPerson);
            }
        }
        return STR_EMPTY;
    }

    //ユーザーパスワード複合化処理(全ユーザー)
    @Deprecated
    public String passwordDecryptionAll() {
        // アクセスログ出力
        exportAccessLog("passwordDecryptionAll", "ボタン「パスワード複合化(全ユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            //パスワード暗号化処理
            List<MPerson> mPersonList = mPersonDao.searchPersonAll();
            for (MPerson b : mPersonList) {
                if (b.getPassword() != null && !b.getPassword().equals(STR_EMPTY)) {
                    b.setPassword(BaseUtility.decryptionString(b.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                if (b.getTempPassword() != null && !b.getTempPassword().equals(STR_EMPTY)) {
                    b.setTempPassword(BaseUtility.decryptionString(b.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                mPersonDao.merge(b);
            }
        }
        return STR_EMPTY;
    }

    //パスワード複合化処理(ユーザー指定)
    @Deprecated
    public String passwordDecryptionForPerson(String corpId, String personId) {
        // アクセスログ出力
        exportAccessLog("passwordDecryptionForPerson", "ボタン「パスワード複合化(ログインユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            MPerson mPerson = mPersonDao.find(corpId, personId);
            if (mPerson != null) {
                if (mPerson.getPassword() != null && !mPerson.getPassword().equals(STR_EMPTY)) {
                    mPerson.setPassword(BaseUtility.decryptionString(mPerson.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                if (mPerson.getTempPassword() != null && !mPerson.getTempPassword().equals(STR_EMPTY)) {
                    mPerson.setTempPassword(BaseUtility.decryptionString(mPerson.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)));
                }
                mPersonDao.merge(mPerson);
            }
        }
        return STR_EMPTY;
    }

    //パスワード暗号化処理-AES-Salt(全ユーザー)
    public String passwordEncryptionAllAESSalt() {
        // アクセスログ出力
        exportAccessLog("passwordEncryptionAllAESSalt", "ボタン「パスワード暗号化-AES-Salt(全ユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            List<MPerson> mPersonList = mPersonDao.searchPersonAll();
            for (MPerson a : mPersonList) {
                if (a.getPassword() != null && !a.getPassword().equals(STR_EMPTY)) {
                    a.setPassword(BaseUtility.encryptionStringAES(a.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(a.getId().getCorpId() + a.getId().getPersonId())));
                }
                if (a.getTempPassword() != null && !a.getTempPassword().equals(STR_EMPTY)) {
                    a.setTempPassword(BaseUtility.encryptionStringAES(a.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(a.getId().getCorpId() + a.getId().getPersonId())));
                }
                mPersonDao.merge(a);
            }
        }
        return STR_EMPTY;
    }

    //パスワード暗号化処理-AES-Salt(ユーザー指定)
    public String passwordEncryptionForPersonAESSalt(String corpId, String personId) {
        // アクセスログ出力
        exportAccessLog("passwordEncryptionForPersonAESSalt", "ボタン「パスワード暗号化-AES-Salt(ログインユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            MPerson mPerson = mPersonDao.find(corpId, personId);
            if (mPerson != null) {
                if (mPerson.getPassword() != null && !mPerson.getPassword().equals(STR_EMPTY)) {
                    mPerson.setPassword(BaseUtility.encryptionStringAES(mPerson.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(mPerson.getId().getCorpId() + mPerson.getId().getPersonId())));
                }
                if (mPerson.getTempPassword() != null && !mPerson.getTempPassword().equals(STR_EMPTY)) {
                    mPerson.setTempPassword(BaseUtility.encryptionStringAES(mPerson.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(mPerson.getId().getCorpId() + mPerson.getId().getPersonId())));
                }
                mPersonDao.merge(mPerson);
            }
        }
        return STR_EMPTY;
    }

    //ユーザーパスワード複合化処理-AES-Salt(全ユーザー)
    public String passwordDecryptionAllAESSalt() {
        // アクセスログ出力
        exportAccessLog("passwordDecryptionAllAESSalt", "ボタン「パスワード複合化-AES-Salt(全ユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            //パスワード暗号化処理
            List<MPerson> mPersonList = mPersonDao.searchPersonAll();
            for (MPerson b : mPersonList) {
                if (b.getPassword() != null && !b.getPassword().equals(STR_EMPTY)) {
                    b.setPassword(BaseUtility.decryptionStringAES(b.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(b.getId().getCorpId() + b.getId().getPersonId())));
                }
                if (b.getTempPassword() != null && !b.getTempPassword().equals(STR_EMPTY)) {
                    b.setTempPassword(BaseUtility.decryptionStringAES(b.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(b.getId().getCorpId() + b.getId().getPersonId())));
                }
                mPersonDao.merge(b);
            }
        }
        return STR_EMPTY;
    }

    //パスワード複合化処理-AES-Salt(ユーザー指定)
    public String passwordDecryptionForPersonAESSalt(String corpId, String personId) {
        // アクセスログ出力
        exportAccessLog("passwordDecryptionForPersonAESSalt", "ボタン「パスワード複合化-AES-Salt(ログインユーザ)」押下");

        if (osolConfigs.getConfig(OsolConstants.ENCRYPTION_TOOLS_VISIBLE).toUpperCase().equals("TRUE")) {
            MPerson mPerson = mPersonDao.find(corpId, personId);
            if (mPerson != null) {
                if (mPerson.getPassword() != null && !mPerson.getPassword().equals(STR_EMPTY)) {
                    mPerson.setPassword(BaseUtility.decryptionStringAES(mPerson.getPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(mPerson.getId().getCorpId() + mPerson.getId().getPersonId())));
                }
                if (mPerson.getTempPassword() != null && !mPerson.getTempPassword().equals(STR_EMPTY)) {
                    mPerson.setTempPassword(BaseUtility.decryptionStringAES(mPerson.getTempPassword(),
                            osolConfigs.getConfig(OsolConstants.ENCRYPTION_KEY_VALUE)
                                    .concat(mPerson.getId().getCorpId() + mPerson.getId().getPersonId())));
                }
                mPersonDao.merge(mPerson);
            }
        }
        return STR_EMPTY;
    }

    /**
     * お知らせポップアップ表示条件
     *
     * @return
     */
    @Logged
    public boolean checkOshirase() {

        if (!isFirstAccess())
            return false;

        List<OshiraseDisp> list = getPopupDispList();
        if (!lastOshiraseCheckFlg) {
            eventLogger.debug("お知らせポップアップ表示");
            return true;
        } else if (list != null && !list.isEmpty()) {
            OshiraseDisp firstItem = list.get(0);
            if (!firstItem.getNonRecordFlg()) {
                //お知らせか、お知らせはありませんメッセージかを切り分ける
                eventLogger.debug("お知らせポップアップ表示");
                return true;
            }
        }
        eventLogger.debug("お知らせポップアップ表示不要");
        setShowOshiraseSessionFlg(false);
        return false;
    }

    public boolean isFirstAccess() {
        return Boolean.valueOf(
                getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.FIRST_ACCESS_FLG.getVal()).toString());
    }

    @Logged
    public void setShowOshiraseSessionFlg(boolean flg) {
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.FIRST_ACCESS_FLG.getVal(), String.valueOf(flg));
    }

    /**
     * お知らせ最終確認時間の書き込み
     */
    public void uploadLastOshiraseCheckTime() {
        // アクセスログ出力
        exportAccessLog("uploadLastOshiraseCheckTime", "ボタン「お知らせ」押下");

        // お知らせ画面を表示させるフラグ
        if (!isFirstAccess()) {
            // リンクからお知らせ画面を見た時は更新しない
            return;
        }
        setShowOshiraseSessionFlg(false);
        Timestamp svdata = mPersonDao.getSvDate();
        MPerson mPerson = getLoginPerson();
        if (lastOshiraseCheckFlg) {
            mPerson.setLastOshiraseCheckTime(svdata);
            mPerson = mPersonDao.merge(mPerson);
            setLoginPerson(mPerson);
        } else if (mPerson.getLastOshiraseCheckTime() != null) {
            // チェックが入っていないのに、時間が入っている場合はnullで上書き
            mPerson.setLastOshiraseCheckTime(null);
            mPerson = mPersonDao.merge(mPerson);
            setLoginPerson(mPerson);
        }
    }

    /**
     * デフォルト画面
     */
    public String getDefaultLoginScreenId() {
        MPerson mPerson = getLoginPerson();
        if (mPerson.getMUiScreen() != null) {
            String screenId = mPerson.getMUiScreen().getUiScreenId();
            // 機能利用が制限されている場合はメニューへ
            screenId = STR_EMPTY;
            return screenId;
        }
        return STR_EMPTY;
    }

    public List<OshiraseDisp> getPopupDispList() {
        return popupDispList;
    }

    public void setPopupDispList(List<OshiraseDisp> popupDispList) {
        this.popupDispList = popupDispList;
    }

    public boolean isLastOshiraseCheckFlg() {
        return lastOshiraseCheckFlg;
    }

    public void setLastOshiraseCheckFlg(boolean lastOshiraseCheckFlg) {
        this.lastOshiraseCheckFlg = lastOshiraseCheckFlg;
    }

    //メニュー項目の表示状態に関わる権限のチェック

    /***
     * データ収集装置表示
     * @return
     */
    public boolean getEquipmentMenuVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.DATA_COLLECT_DEVICE) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /***
     * SMSサーバ設定表示
     * @return
     */
    public boolean getSettingMenuVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.SMS_SERVER_SETTING) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    public boolean getLoginCorpTypeIsMaintenance() {
        return getLoginCorpType().equals(OsolConstants.CORP_TYPE.MAINTENANCE.getVal());
    }

    public String getInqury() {
        return super.getInqury();
    }

    public String userGuide() {
        // アクセスログ出力
        exportAccessLog("userGuide", "ヘルプボタン押下");

        int ret = smsFileDownload.fileDownload(super.getUserGuide(), super.getUserGuidePdf(), true);
        if (ret != RETURN_CODE.SUCCESS.getInt()) {
            eventLogger.debug(this.getClass().getName().concat(".downloadFile():Error ret(" + ret + ")"));
            addErrorMessage(beanMessages.getMessage("osol.error.fileDownload"));
        }
        return STR_EMPTY;
    }

    public String termsOfService() {
        int ret = smsFileDownload.fileDownload(super.getTermsOfService(), super.getTermsOfServicePdf(), true);
        if (ret != RETURN_CODE.SUCCESS.getInt()) {
            eventLogger.debug(this.getClass().getName().concat(".downloadFile():Error ret(" + ret + ")"));
            addErrorMessage(beanMessages.getMessage("osol.error.fileDownload"));
        }
        return STR_EMPTY;
    }

    public String getLogo() {
        return super.getLogo();
    }

    public String getPrivacy() {
        return super.getPrivacy();
    }

    public String getFavicon() {
        return super.getFavicon();
    }

    public String getCopyright() {
        return super.getCopyright();
    }

    public String getHost() {
        return HostInfo.getHostName();
    }

    public boolean isUserGuideBool() {
        /*userガイドのパスが空文字だったら表示しない*/
        if (super.getUserGuide().equals(STR_EMPTY)) {
            return false;
        }
        return true;
    }

    public boolean isInquryBool() {
        if (super.getInqury().equals(STR_EMPTY)) {
            return false;
        }
        return true;
    }

    public boolean getFunctionUse(OsolAccessBean.FUNCTION_CD functionCd) {

        AccessResultSet result = osolAccessBean.getAccessEnable(
                functionCd,
                "none",
                this.getLoginCorpId(),
                this.getLoginPersonId(),
                this.getLoginOperationCorpId());

        return result.getOutput().isFunctionEnable();
    }

    //sampleApiBean呼出
    public void sampleApiBean() {

        SampleApiResponse sampleApiResponse = new SampleApiResponse();
        SampleApiParameter sampleApiParameter = new SampleApiParameter();
        sampleApiParameter.setBean("SampleApiBean");
        sampleApiParameter.setGroupCode("+34");
        sampleApiParameter.setCurlDate(new Date());

        SampleApiRequestSet requestSet;
        requestSet = new SampleApiRequestSet();
        requestSet.setGroupCode("111");
        requestSet.setGroupName("111");
        requestSet.setKbnCode("aaaa");
        List<SampleApiRequestSet> requestSetList = new ArrayList<>();
        requestSetList.add(requestSet);

        requestSet = new SampleApiRequestSet();
        requestSet.setGroupCode("222");
        requestSet.setGroupName("222");
        requestSet.setKbnCode("bbbb");
        HashMap<String, SampleApiRequestSet> requestSetMap = new HashMap<>();
        requestSetMap.put("requestSetMap", requestSet);

        SampleApiRequest request = new SampleApiRequest();
        request.setRequestSetList(requestSetList);
        request.setRequestSetMap(requestSetMap);

        sampleApiParameter.setRequest(request);

        SmsApiGateway smsApiGateway = new SmsApiGateway();
        sampleApiResponse = (SampleApiResponse) smsApiGateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON, sampleApiParameter, sampleApiResponse);
    }
}
