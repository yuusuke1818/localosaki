package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolEncipher;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.GetSmsMeterUserParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterUser.UpdateSmsMeterUserParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.GetSmsMeterUserResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterUser.UpdateSmsMeterUserResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.GetSmsMeterUserResult;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterUser.UpdateSmsMeterUserResult;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsApiGateway;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.dao.SearchMPersonDao;
import jp.co.osaki.sms.validation.PasswordValidation;

/**
 * メーターユーザー情報編集画面
 *
 * @author nishida.t
 */
@Named(value = "smsCollectSettingMeterUserEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    private static final long serialVersionUID = 1138040240718510735L;

    private String corpId;
    private String personId;
    private String furigana;
    private String personName;

    private String departmentName;
    private String positionName;

    private String nowPassword;
    private String newPassword;
    private String newPasswordConfirm;

    private String yourPassword;
    private String tempPassword;
    private String tempPasswordConfirm;

    private String expiryDate;

    private String mailAddress;
    private String mailAddressConfirm;
    private String tel;
    private String fax;

    private String selectPage;

    private int account_stop_flg_state;

    private boolean account_self_flg;

    private boolean account_stop_flg;

    private String accountStatus;

    // 新規登録から来た際のメッセージ表示フラグ
    private boolean fromRegisterMessageFlg;
    private String fromRegisterMessage;

    // 2017/07/13 編集モードフラグ(True:編集,Flase 新規登録モード)
    private boolean editMode;

    private String titleMsg;

    @Inject
    private EditBeanProperty meterUserEditBeanProperty;

    // メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private TenantPersonBean tenantPersonBean;

    @Inject
    private OsolEncipher osolEncipher;

    // 担当企業、担当建物設定用
    private String userCorpId;
    // 担当設定不要
    private boolean personDisabled;

    private List<String> infoMessages;
    private List<String> errorMessages;
    private List<String> invalidComponent;

    // DBサーバー時刻
    private Timestamp svDate;

    private String passwordLimitDays;

    @EJB
    SearchMPersonDao searchMPersonDao;

    @Override
    public String init() {
        conversationStart();
        passwordLimitDays = this.getWrapped().getInitParameter(OsolConstants.LOGIN_PASS_EXPIRED_DAYS);

        // 初期化
        initInfoMessages();
        initErrorMessages();
        initInvalidComponent();

        if (STR_EMPTY.equals(getPersonId())) {
            // 新規登録
            personId = null;
            personName = null;
            furigana = null;
            mailAddress = null;
            mailAddressConfirm = null;
            tel = null;
            fax = null;
            departmentName = null;
            positionName = null;
            // チェックボックス数分 初期化しておく
            meterUserEditBeanProperty.initPersonAuthCheck();

            // 企業種別を取得
            String corpType = this.getLoginOperationCorpType();
            meterUserEditBeanProperty.setCorpType(corpType);
            if (corpType.equals(OsolConstants.CORP_TYPE.PARTNER.getVal())
                    || corpType.equals(OsolConstants.CORP_TYPE.CONTRACT.getVal())) {
                meterUserEditBeanProperty.setAuthorityFlg(true);
            }
            meterUserEditBeanProperty.setMainteFlg(Boolean.TRUE); //メンテ発行不可
            meterUserEditBeanProperty.setEnergyPointInputFlg(Boolean.TRUE); //手入力ポイント不可
            meterUserEditBeanProperty.setSelfFlg(Boolean.FALSE);

            // 新規登録モード
            setEditMode(false);
            setTitleMsg("ユーザー新規登録");
            // 初期値（登録・編集後の画面では送信直前の値）
            expiryDate = SmsConstants.METER_USER_EXPIRY_DATE.DAY.getName();
        } else {
            //         MPerson person = mPersonDao.find(this.getLoginOperationCorpId(), getPersonId());
            userDetailSelect(corpId, personId);

            setEditMode(true);
            setTitleMsg("ユーザー編集");
            expiryDate = SmsConstants.METER_USER_EXPIRY_DATE.DAY.getName();
        }

        return "meterUserEdit";
    }

    /**
        ユーザー編集画面の初期表示

        @param person
        @return

    */
    public void userDetailSelect(String corpId, String personId) {

        // セッションからユーザー情報取得
        String person_id = this.getLoginPersonId();
        String corp_id = this.getLoginCorpId();

        if (!STR_EMPTY.equals(corpId) && !STR_EMPTY.equals(personId)) {

            GetSmsMeterUserResult result = getApi();

            // 取得結果を表示
            if (result != null) {
                this.personId = result.getPersonId();
                this.userCorpId = result.getCorpId();
                this.personName = result.getPersonName();
                this.furigana = result.getPersonKana();
                this.mailAddress = result.getMailAddress();
                this.mailAddressConfirm = result.getMailAddress();
                this.tel = result.getTelNo();
                this.fax = result.getFaxNo();
                this.departmentName = result.getDeptName();
                this.positionName = result.getPositionName();
                this.account_stop_flg_state = result.getAccountStopFlg();

                // パスワード系
                this.tempPassword = STR_EMPTY;
                this.nowPassword = STR_EMPTY;

                if (!CheckUtility.isNullOrEmpty(userCorpId)) {
                    //             userEditBeanProperty.setPerson(person);
                    meterUserEditBeanProperty.setPersonCorpId(userCorpId);
                    meterUserEditBeanProperty.setPersonId(personId);
                    meterUserEditBeanProperty.setPersonName(personName);
                    meterUserEditBeanProperty.setPersonType(result.getPersonType());
                }

                // 自分自身の編集画面かどうか判定
                if (person_id.equals(this.personId) && corp_id.equals(this.userCorpId)) {
                    meterUserEditBeanProperty.setSelfFlg(Boolean.TRUE);
                } else {
                    meterUserEditBeanProperty.setSelfFlg(Boolean.FALSE);
                }

                // アカウント停止フラグが設定されている場合
                if (OsolConstants.FLG_ON.equals(result.getAccountStopFlg())) {
                    this.setAccount_stop_flg(Boolean.TRUE);
                } else {
                    this.setAccount_stop_flg(Boolean.FALSE);
                }

                // アカウント状態
                this.setAccountStatus(getAcountStatusName(result.getTempPassExpirationDate(),
                        result.getAccountStopFlg(), result.getPassMissCount(), svDate));

                // ロック(期限 or 入力ミス)の場合
                meterUserEditBeanProperty.setUnlockMsgFlg(Boolean.FALSE);
                if (this.accountStatus.equals(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName())
                        || this.accountStatus.equals(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName())) {
                    meterUserEditBeanProperty.setUnlockMsgFlg(Boolean.TRUE);
                }
            }
        }

        return;
    }

    // 登録・更新処理
    @Logged
    public void meterUserUpdate() {

        // バリデーションチェック
        if (!validateInputParam()) {
            return;
        }

        // 担当者情報、企業担当者情報、企業担当者権限情報を登録する
        UpdateSmsMeterUserResult result = updateApi();

        // 更新後の最新値を設定
        if (result != null) {
            this.personId = result.getPersonId();
            this.userCorpId = result.getCorpId();
            this.personName = result.getPersonName();
            this.furigana = result.getPersonKana();
            this.mailAddress = result.getMailAddress();
            this.mailAddressConfirm = result.getMailAddress();
            this.tel = result.getTelNo();
            this.fax = result.getFaxNo();
            this.departmentName = result.getDeptName();
            this.positionName = result.getPositionName();
            this.account_stop_flg_state = result.getAccountStopFlg();

            // パスワード系
            this.tempPassword = result.getTempPassword();
            this.nowPassword = result.getPassword();

            if (!CheckUtility.isNullOrEmpty(userCorpId)) {
                //             userEditBeanProperty.setPerson(person);
                meterUserEditBeanProperty.setPersonCorpId(userCorpId);
                meterUserEditBeanProperty.setPersonId(personId);
                meterUserEditBeanProperty.setPersonName(personName);
                meterUserEditBeanProperty.setPersonType(result.getPersonType());
            }

            // 自分自身の編集画面かどうか判定（更新後は自分自身の編集画面かチェックいらない？）
            if (this.getLoginPersonId().equals(this.personId) && this.getLoginCorpId().equals(this.userCorpId)) {
                meterUserEditBeanProperty.setSelfFlg(Boolean.TRUE);
            } else {
                meterUserEditBeanProperty.setSelfFlg(Boolean.FALSE);
            }

            // アカウント停止フラグが設定されている場合
            if (OsolConstants.FLG_ON.equals(result.getAccountStopFlg())) {
                this.setAccount_stop_flg(Boolean.TRUE);
            } else {
                this.setAccount_stop_flg(Boolean.FALSE);
            }

            // アカウント状態
            this.setAccountStatus(getAcountStatusName(result.getTempPassExpirationDate(),
                    result.getAccountStopFlg(), result.getPassMissCount(), this.svDate));

            // ロック(期限 or 入力ミス)の場合
            meterUserEditBeanProperty.setUnlockMsgFlg(Boolean.FALSE);
            if (this.accountStatus.equals(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName())
                    || this.accountStatus.equals(OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName())) {
                meterUserEditBeanProperty.setUnlockMsgFlg(Boolean.TRUE);
            }

            // Redmine#618 編集時は初期値、登録後は前回値
            setExpiryDate(this.getExpiryDate());
            setEditMode(true);
            setTitleMsg("ユーザー編集");
        }

        return;
    }

    /**
     * GetAPI実行
     */
    private GetSmsMeterUserResult getApi () {
        GetSmsMeterUserParameter parameter = new GetSmsMeterUserParameter();
        GetSmsMeterUserResponse response = new GetSmsMeterUserResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("GetSmsMeterUserBean");
        parameter.setCorpId(corpId);
        parameter.setPersonId(personId);

        response = (GetSmsMeterUserResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (response == null) {
            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterUserEditBean.error.noData"));
            return null;
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
             addErrorMessages(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE + response.getResultCode()));
            return null;
        } else if (response.getResult() == null) {
            addErrorMessages(beanMessages.getMessage("userEditBean.error.alreadyPerson"));
//            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
            return null;
        }

        // ResponseのDBサーバー時刻を保持しておく
        this.svDate = response.getServerDateTime();

        return response.getResult();
    }

    /**
     * UpdateAPI実行
     */
    private UpdateSmsMeterUserResult updateApi () {

        UpdateSmsMeterUserParameter parameter = new UpdateSmsMeterUserParameter();
        UpdateSmsMeterUserResponse response = new UpdateSmsMeterUserResponse();
        SmsApiGateway gateway = new SmsApiGateway();
        parameter.setBean("UpdateSmsMeterUserBean");
        parameter.setCorpId(this.corpId);
        parameter.setPersonId(this.personId);

        if (editMode) {
            parameter.setPersonType(meterUserEditBeanProperty.getPersonType());
        } else {
            // メーターユーザー管理画面で新規登録される担当者は全て担当者権限
            parameter.setPersonType(OsolConstants.PERSON_TYPE.PERSON.getVal());
        }

        parameter.setPersonName(this.personName);
        parameter.setPersonKana(this.furigana);
        parameter.setDeptName(this.departmentName);
        parameter.setPositionName(this.positionName);
        parameter.setTelNo(this.tel);
        parameter.setFaxNo(this.fax);
        parameter.setMailAddress(this.mailAddress);

//        parameter.setPassword(osolEncipher.encryptionString(this.tempPassword, this.corpId, this.personId));
//        parameter.setTempPassword(osolEncipher.encryptionString(this.tempPassword, this.corpId, this.personId));

        /*
         if (SmsConstants.METER_USER_EXPIRY_DATE.DAY.getName().equals(this.getExpiryDate())) {
             parameter.setTempPassExpirationDate(SmsConstants.METER_USER_EXPIRY_DATE.DAY.getVal());
         } else {
             parameter.setTempPassExpirationDate(SmsConstants.METER_USER_EXPIRY_DATE.WEEK.getVal());
         }
        */

         // 仮パスワード
         if (!meterUserEditBeanProperty.isSelfFlg()) {
             // 仮パスワードの入力がある場合ロック解除と有効期限を更新ミスカウントもリセット
             if (!CheckUtility.isNullOrEmpty(this.getTempPassword())) {
                 String hash = osolEncipher.hashing(this.tempPassword);
                 parameter.setPassword(hash);
                 parameter.setTempPassword(hash);
                 // パスワード入力ミス回数
                 parameter.setPassMissCount(0);

                 if (SmsConstants.METER_USER_EXPIRY_DATE.DAY.getName().equals(this.getExpiryDate())) {
                     parameter.setTempPassExpirationDate(SmsConstants.METER_USER_EXPIRY_DATE.DAY.getVal());
                 } else {
                     parameter.setTempPassExpirationDate(SmsConstants.METER_USER_EXPIRY_DATE.WEEK.getVal());
                 }
             }
         } else {
             // 自分自身の情報を更新
             if (!CheckUtility.isNullOrEmpty(this.newPassword)) {
                 parameter.setPassword(osolEncipher.hashing(this.newPassword));
                 // パスワード入力ミス回数
                 parameter.setPassMissCount(0);
             }

         }

         // アカウントストップ
         if (this.account_stop_flg) {
             parameter.setAccountStopFlg(OsolConstants.FLG_ON);
         } else {
             parameter.setAccountStopFlg(OsolConstants.FLG_OFF);
         }

         // 内部呼び出しの場合、true
         parameter.setApiPrivateFlg(Boolean.TRUE);

        response = (UpdateSmsMeterUserResponse) gateway.osolApiPost(
                osolConfigs.getConfig(OsolConstants.OSOL_API_SERVER_ENDPOINT),
                SmsApiGateway.PATH.JSON,
                parameter,
                response);

        if (response == null) {
            addErrorMessages(beanMessages.getMessage("smsCollectSettingMeterUserEditBean.error.noData"));
            return null;
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
             addErrorMessages(beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE + response.getResultCode()));
            return null;
        } else if (response.getResult() == null) {
            addErrorMessages(beanMessages.getMessage("userEditBean.error.alreadyPerson"));
//            invalidComponent.add("smsCollectSettingMeterTenantEditBean:buildingNo");
            return null;
        }

        // ResponseのDBサーバー時刻を保持しておく
        this.svDate = response.getServerDateTime();


        // 更新と新規/複製でメッセージを変更
        if (this.editMode) {
            addInfoMessages(beanMessages.getMessage("osol.info.UpdateSuccess"));
        }
        else {
            addInfoMessages(beanMessages.getMessage("smsCollectSettingMeterUserEditBean.info.nextTenant"));
        }

        return response.getResult();
    }

    /**
     * 入力項目のチェック
     * @return
     */
    private boolean validateInputParam() {

        // 初期化
        initInvalidComponent();
        initInfoMessages();
        initErrorMessages();
        boolean errorFlg = false;

        String personIdCheckPattern = "^[a-zA-Z0-9]+$";

        // ユーザーID 未入力チェック
        if (CheckUtility.isNullOrEmpty(personId)) {
            addErrorMessages(beanMessages.getMessage("osol.name.personId") + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterUserEditBean:personId");
            errorFlg = true;
        } else if (!CheckUtility.checkRegex(personId, personIdCheckPattern)) {
            // ユーザーID 禁則文字チェック
            addErrorMessages(beanMessages.getMessage("userEditBean.error.personIdValidation"));
            invalidComponent.add("smsCollectSettingMeterUserEditBean:personId");
            errorFlg = true;
        }

        // ユーザー名 未入力チェック
        if (CheckUtility.isNullOrEmpty(personName)) {
            addErrorMessages(beanMessages.getMessage("osol.name.personName") + beanMessages.getMessage("osol.error.required"));
            invalidComponent.add("smsCollectSettingMeterUserEditBean:userName");
            errorFlg = true;
        }

        // ユーザー名振り仮名のチェック
        if (!CheckUtility.isNullOrEmpty(furigana)
                && !CheckUtility.checkFurigana(furigana)) {
            addErrorMessages(beanMessages.getMessage("userEditBean.error.furiganaValidation"));
            invalidComponent.add("smsCollectSettingMeterUserEditBean:furigana");
            errorFlg = true;
        }

        // メールアドレス
        if (!CheckUtility.isNullOrEmpty(mailAddress)
                || !CheckUtility.isNullOrEmpty(mailAddressConfirm)) {
            // どちらかに入力があった場合確認を行う

            if (!CheckUtility.isNullOrEmpty(mailAddress)
                    && CheckUtility.isNullOrEmpty(mailAddressConfirm)) {
                // メールアドレス確認が未入力の場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notInputMailAddressConfirm"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddressConfirm");
                errorFlg = true;

            } else if (CheckUtility.isNullOrEmpty(mailAddress)
                    && !CheckUtility.isNullOrEmpty(mailAddressConfirm)) {
                // メールアドレスが未入力の場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notInputMailAddress"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddress");
                errorFlg = true;
            }

            // メールアドレスの禁則文字チェック
            if (!CheckUtility.isNullOrEmpty(mailAddress)
                    && !CheckUtility.checkMailAddress(mailAddress)) {
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notTypeMailAddress"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddress");
                errorFlg = true;
            }

            // メールアドレス確認の禁則文字チェック
            if (!CheckUtility.isNullOrEmpty(mailAddressConfirm)
                    && !CheckUtility.checkMailAddress(mailAddressConfirm)) {
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notTypeMailAddressConfirm"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddressConfirm");
                errorFlg = true;
            }

            if (!invalidComponent.contains("smsCollectSettingMeterUserEditBean:mailAddressConfirm")
                    && !invalidComponent.contains("smsCollectSettingMeterUserEditBean:mailAddress")
                    && !mailAddress.equals(mailAddressConfirm)) {
                // 同じではない場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualMailAddress"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddress");
                invalidComponent.add("smsCollectSettingMeterUserEditBean:mailAddressConfirm");
                errorFlg = true;
            }
        }

        // パスワード、仮パスワード確認
        // ログインしたユーザー情報を取得
        MPerson loginUser = searchMPersonDao.find(getLoginCorpId(), getLoginPersonId());

        // 新規
        if (!editMode) {
            // パスワードをハッシュ化して、あなたのパスワードと一致しているか
            if (CheckUtility.isNullOrEmpty(yourPassword)) {
                // あなたのパスワードが入力されていない場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notInputYourPassword"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                errorFlg = true;

            } else if (!osolEncipher.verify(yourPassword, loginUser.getPassword())) {
                // 一致していない場合エラーとする
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualyourPassword"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                errorFlg = true;
            }

            if (CheckUtility.isNullOrEmpty(tempPassword)) {
                // 仮パスワードが入力されていない場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.tempPassword"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPassword");
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                errorFlg = true;
            }

            if (CheckUtility.isNullOrEmpty(tempPasswordConfirm)) {
                // 仮パスワード確認が入力されていない場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.tempPasswordConfirm"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirm");
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirmSecret");
                errorFlg = true;
            }

            // 仮パスワードと仮パスワード確認が一致しない場合
            if (!CheckUtility.isNullOrEmpty(yourPassword)
                    && !CheckUtility.isNullOrEmpty(tempPassword)
                    && yourPassword.equals(tempPassword)) {
                // あなたのパスワードと仮パスワードが同じ場合
                addErrorMessages(beanMessages.getMessage("userEditBean.error.equalPassword"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                errorFlg = true;

            } else if (!CheckUtility.isNullOrEmpty(tempPassword)
                    && !CheckUtility.isNullOrEmpty(tempPasswordConfirm)
                    && !tempPassword.equals(tempPasswordConfirm)) {
                // 仮パスワード同士が一致していない場合エラーとする
                addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualtempPassword"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirmSecret");
                errorFlg = true;
            }
        } else {
        // 更新

            // 自分自身の編集画面
            if (meterUserEditBeanProperty.isSelfFlg()) {
                // 現在のパスワード入力
                if (!CheckUtility.isNullOrEmpty(nowPassword)
                        || !CheckUtility.isNullOrEmpty(newPassword)
                        || !CheckUtility.isNullOrEmpty(newPasswordConfirm)) {

                    if (CheckUtility.isNullOrEmpty(nowPassword)) {
                        // 現在のパスワード未入力
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.nowPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:nowPassword");
                        errorFlg = true;
                    } else if (!osolEncipher.verify(nowPassword, loginUser.getPassword())) {
                        // 現在のパスワードが違う場合
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.invalidNowPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:nowPassword");
                        errorFlg = true;
                    }

                    if (CheckUtility.isNullOrEmpty(newPassword)) {
                        // 新パスワード未入力
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.newPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPassword");
                        errorFlg = true;
                    } else if (false == PasswordValidation.passwordCheck(newPassword, 8, 20)) {
                        // 新パスワードチェック
                        addErrorMessages(beanMessages.getMessage("userEditBean.warn.newPasswordValidation"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPassword");
                        errorFlg = true;
                    }

                    if (CheckUtility.isNullOrEmpty(newPasswordConfirm)) {
                        // パスワード確認
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.newPasswordConfirm"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPasswordConfirm");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:confirmPassword");
                        errorFlg = true;
                    }

                    if (!CheckUtility.isNullOrEmpty(nowPassword)
                            && !CheckUtility.isNullOrEmpty(newPassword)
                            && nowPassword.equals(newPassword)) {
                        // 現在のパスワードと新パスワードが同じ
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.equalPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:nowPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPassword");
                        errorFlg = true;
                    } else if (!CheckUtility.isNullOrEmpty(newPassword)
                            && !CheckUtility.isNullOrEmpty(newPasswordConfirm)
                            && !newPassword.equals(newPasswordConfirm)) {
                        // 新パスワードと新パスワード確認が同じではない場合
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualNewPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:newPasswordConfirm");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:confirmPassword");
                        errorFlg = true;
                    }
                }

            } else {
                // 他ユーザーの編集画面
                if (!CheckUtility.isNullOrEmpty(yourPassword)
                        || !CheckUtility.isNullOrEmpty(tempPassword)
                        || !CheckUtility.isNullOrEmpty(tempPasswordConfirm)
                        || (!account_stop_flg
                                && OsolConstants.FLG_ON.equals(account_stop_flg_state))) {
                    // いずれかに入力がある場合　もしくは　アカウント停止状態のユーザーを停止解除をしようとした場合

                    if (CheckUtility.isNullOrEmpty(yourPassword)) {
                        // 仮パスワード
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.yourPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:yourPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                        errorFlg = true;
                    } else {
                        if (!osolEncipher.verify(yourPassword, loginUser.getPassword())) {
                            // あなたのパスワードが一致していない場合エラーとする
                            addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualyourPassword"));
                            invalidComponent.add("smsCollectSettingMeterUserEditBean:yourPassword");
                            invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                            errorFlg = true;
                        }
                    }

                    if (CheckUtility.isNullOrEmpty(tempPassword)) {
                        // 仮パスワード
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.tempPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                        errorFlg = true;
                    }

                    if (CheckUtility.isNullOrEmpty(tempPasswordConfirm)) {
                        // 仮パスワード確認
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.tempPasswordConfirm"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirm");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirmSecret");
                        errorFlg = true;
                    }

                    if (!CheckUtility.isNullOrEmpty(yourPassword)
                            && !CheckUtility.isNullOrEmpty(tempPassword)
                            && yourPassword.equals(tempPassword)) {
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.equalPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:yourPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:passwordSecret");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                        errorFlg = true;
                    } else if (!CheckUtility.isNullOrEmpty(tempPassword)
                            && !CheckUtility.isNullOrEmpty(tempPasswordConfirm)
                            && !tempPassword.equals(tempPasswordConfirm)) {
                        addErrorMessages(beanMessages.getMessage("userEditBean.error.notEqualtempPassword"));
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPassword");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirm");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordSecret");
                        invalidComponent.add("smsCollectSettingMeterUserEditBean:tempPasswordConfirmSecret");
                        errorFlg = true;
                    }
                }
            }
        }

        if (!editMode) {
            //担当者重複チェック
            MPerson tergetPerson;
            tergetPerson = searchMPersonDao.find(this.corpId, this.personId);

            //担当者がすでに存在している場合、
            if (tergetPerson != null) {
                addErrorMessages(beanMessages.getMessage("userEditBean.error.alreadyPerson"));
                invalidComponent.add("smsCollectSettingMeterUserEditBean:personId");
                errorFlg = true;
            }
        }

        return !errorFlg;
       }

    @Logged
    public String onclickTenantPerson() {
        tenantPersonBean.init();
        // 一覧画面からの関係で企業IDを2個渡している
        return tenantPersonBean.initBuilding(meterUserEditBeanProperty.getPersonId(),
                meterUserEditBeanProperty.getPersonName(),
                meterUserEditBeanProperty.getPersonCorpId(), meterUserEditBeanProperty.getPersonCorpId());
    }

    /**
     * アカウントステータスの取得
     *
     * @param target
     * @param svDate 現在日時
     * @return
     */
    public String getAcountStatusName(Date tempPassExpirationDate,  Integer accountStopFlg, Integer passMissCount, Date svDate) {
        Date _tempPassExpirationDate = null;
        if (tempPassExpirationDate != null) {
            String day = DateUtility
                    .changeDateFormat(tempPassExpirationDate, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                    .concat(" 23:59:59");
            _tempPassExpirationDate = DateUtility.conversionDate(day, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH);
        }
        if (accountStopFlg.equals(OsolConstants.FLG_ON)) {
            // "アカウント停止中"
            return OsolConstants.ACCOUNT_STATUS_INFO.STOP_ACCOUNT.getName();
        } else if (passMissCount >= OsolConstants.LOGIN_PASS_MISS_LOCK_COUNT) {
            // "ロック（入力ミス）"
            return OsolConstants.ACCOUNT_STATUS_INFO.LOCK_INPUTFAILURE.getName();
        } else if (_tempPassExpirationDate != null
                && (svDate.compareTo(_tempPassExpirationDate) > 0)) {
            // "ロック（期限切れ）"
            return OsolConstants.ACCOUNT_STATUS_INFO.LOCK_EXPIRATION.getName();
        } else if (_tempPassExpirationDate != null
                && svDate.compareTo(_tempPassExpirationDate) <= 0) {
            // "未ログイン"
            return OsolConstants.ACCOUNT_STATUS_INFO.NOT_LOGIN.getName();
        } else {
            // "正常"
            return OsolConstants.ACCOUNT_STATUS_INFO.NORMAL.getName();
        }
    }

    /**
     *  ユーザー新規登録後のメッセージを設定する。
     */
    public void setRegisterMessage() {
        if (fromRegisterMessageFlg) {
            // 更新結果のメッセージを表示
            if (null != fromRegisterMessage) {
                addMessage(beanMessages.getMessage(fromRegisterMessage));
            } else {
                addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
            }
        }
        fromRegisterMessage = null;
        fromRegisterMessageFlg = false;
    }

    /**
     * 登録確認メッセージ
     * @return
     */
    public String getBeforeRegisterMessage() {
        return beanMessages.getMessage("osol.warn.beforeRegisterMessage");
    }

    public String getDeletePopupMsg() {
        return beanMessages.getMessage("userEditBean.warn.beforeDeleteMessage");
    }

    /*
    public String doAction() {
        tenantPersonBean.init();
        // 一覧画面からの関係で企業IDを2個渡している
        return tenantPersonBean.initBuilding(meterUserEditBeanProperty.getPersonId(),
                meterUserEditBeanProperty.getPersonName(),
                meterUserEditBeanProperty.getPersonCorpId(), meterUserEditBeanProperty.getPersonCorpId(), "ID20");
    }
    */

    @Override
    public String getInvalidStyle(String id) {
        if (invalidComponent != null && invalidComponent.contains(id)) {
            return OsolConstants.INVALID_STYLE;
        }
        return super.getInvalidStyle(id);
    }

    // 仮パスワード有効期限_１日の識別名
    public String getExpiryDateDay() {
        return SmsConstants.METER_USER_EXPIRY_DATE.DAY.getName();
    }

    // 仮パスワード有効期限_１週間の識別名
    public String getExpiryDateWeek() {
        return SmsConstants.METER_USER_EXPIRY_DATE.WEEK.getName();
    }

    public EditBeanProperty getMeterUserEditBeanProperty() {
        return meterUserEditBeanProperty;
    }

    public void setMeterUserEditBeanProperty(EditBeanProperty meterUserEditBeanProperty) {
        this.meterUserEditBeanProperty = meterUserEditBeanProperty;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getTitleMsg() {
        return titleMsg;
    }

    public void setTitleMsg(String titleMsg) {
        this.titleMsg = titleMsg;
    }

    public boolean isAccount_stop_flg() {
        return account_stop_flg;
    }

    public void setAccount_stop_flg(boolean account_stop_flg) {
        this.account_stop_flg = account_stop_flg;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getFurigana() {
        return furigana;
    }

    public void setFurigana(String furigana) {
        this.furigana = furigana;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getNowPassword() {
        return nowPassword;
    }

    public void setNowPassword(String nowPassword) {
        this.nowPassword = nowPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getYourPassword() {
        return yourPassword;
    }

    public void setYourPassword(String yourPassword) {
        this.yourPassword = yourPassword;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getTempPasswordConfirm() {
        return tempPasswordConfirm;
    }

    public void setTempPasswordConfirm(String tempPasswordConfirm) {
        this.tempPasswordConfirm = tempPasswordConfirm;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getMailAddressConfirm() {
        return mailAddressConfirm;
    }

    public void setMailAddressConfirm(String mailAddressConfirm) {
        this.mailAddressConfirm = mailAddressConfirm;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSelectPage() {
        return selectPage;
    }

    public void setSelectPage(String selectPage) {
        this.selectPage = selectPage;
    }

    public int getAccount_stop_flg_state() {
        return account_stop_flg_state;
    }

    public void setAccount_stop_flg_state(int account_stop_flg_state) {
        this.account_stop_flg_state = account_stop_flg_state;
    }

    public boolean isAccount_self_flg() {
        return account_self_flg;
    }

    public void setAccount_self_flg(boolean account_self_flg) {
        this.account_self_flg = account_self_flg;
    }

    public boolean isFromRegisterMessageFlg() {
        return fromRegisterMessageFlg;
    }

    public void setFromRegisterMessageFlg(boolean fromRegisterMessageFlg) {
        this.fromRegisterMessageFlg = fromRegisterMessageFlg;
    }

    public String getFromRegisterMessage() {
        return fromRegisterMessage;
    }

    public void setFromRegisterMessage(String fromRegisterMessage) {
        this.fromRegisterMessage = fromRegisterMessage;
    }

    public SmsMessages getBeanMessages() {
        return beanMessages;
    }

    public void setBeanMessages(SmsMessages beanMessages) {
        this.beanMessages = beanMessages;
    }

    public String getUserCorpId() {
        return userCorpId;
    }

    public void setUserCorpId(String userCorpId) {
        this.userCorpId = userCorpId;
    }

    public boolean isPersonDisabled() {
        return personDisabled;
    }

    public void setPersonDisabled(boolean personDisabled) {
        this.personDisabled = personDisabled;
    }

    public List<String> getInvalidComponent() {
        return invalidComponent;
    }

    public void setInvalidComponent(List<String> invalidComponent) {
        this.invalidComponent = invalidComponent;
    }

    public void initInvalidComponent() {

        if (this.invalidComponent == null) {
            this.invalidComponent = new ArrayList<>();
        } else {
            this.invalidComponent.clear();
        }
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void addInfoMessages(String infoMessages) {
        if (this.infoMessages == null) {
            this.infoMessages = new ArrayList<>();
        }
        this.infoMessages.add(infoMessages);
    }

    public void addErrorMessages(String errorMessages) {
        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        }
        this.errorMessages.add(errorMessages);
    }

    public void initInfoMessages() {
        if (this.infoMessages == null) {
            this.infoMessages = new ArrayList<>();
        } else {
            this.infoMessages.clear();
        }
    }

    public void initErrorMessages() {

        if (this.errorMessages == null) {
            this.errorMessages = new ArrayList<>();
        } else {
            this.errorMessages.clear();
        }
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(List<String> infoMessages) {
        this.infoMessages = infoMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Timestamp getSvDate() {
        return svDate;
    }

    public void setSvDate(Timestamp svDate) {
        this.svDate = svDate;
    }

    public String getPasswordLimitDays() {
        return passwordLimitDays;
    }

    public void setPasswordLimitDays(String passwordLimitDays) {
        this.passwordLimitDays = passwordLimitDays;
    }

}
