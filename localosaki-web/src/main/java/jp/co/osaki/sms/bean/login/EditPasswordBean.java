package jp.co.osaki.sms.bean.login;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolEncipher;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.menu.IndexBean;
import jp.co.osaki.sms.dao.MPersonDao;

/**
 *
 * パスワード変更画面
 *
 * @author h-shiba
 */
@Named(value = "loginEditPasswordBean")
@RequestScoped
public class EditPasswordBean extends SmsBean implements Serializable {

    private static final long serialVersionUID = 2833849881554420032L;

    private String tempPassword;

    private String newPassword;

    private String confirmPassword;

    private String passwordLimitDays;

    @EJB
    MPersonDao mPersonDao;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    @Inject
    private IndexBean indexBean;

    @Inject
    private LoginBean loginBean;

    @Inject
    private OsolEncipher osolEncipher;

    @Override
    public String init() {
        // アクセスログ出力
        exportAccessLog("init", "パスワード期限通知画面にてボタン「変更する」押下");

        passwordLimitDays = this.getWrapped().getInitParameter(OsolConstants.LOGIN_PASS_EXPIRED_DAYS);
        return "editPassword";
    }

    public String changePassword() {
        // アクセスログ出力
        exportAccessLog("changePassword", "ボタン「登録」押下");

        eventLogger.info(LoginBean.class.getPackage().getName().concat(":START"));

        // バリデーション(newPasswordとconfirmPasswordの一致)
        if (!newPassword.equals(confirmPassword)) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password unmatch error."));
            addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.passwordUnmatchError"));
            return "";
        }

        // セッションからユーザー情報取得
        String corp_id = this.getLoginCorpId();
        String person_id = this.getLoginPersonId();

        MPerson dbPerson = mPersonDao.find(corp_id, person_id);
        if (dbPerson == null) {
            eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: User is not found in m_person."));
            addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.userIsNotFoundInMPerson"));
            return loginBean.init();
        }

        String dbPassword = dbPerson.getPassword();
        String dbTempPassword = dbPerson.getTempPassword();

        if (getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.SET_PASSWORD.getVal()) == null) {
            // 画面の仮パスワードとDBの仮パスワードとの不一致チェック
            if (!osolEncipher.verify(tempPassword, dbTempPassword)) {
                eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password is not equal temp password."));
                addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.notEqualTempPassword"));
                return "";
            }

            // 画面の新パスワードとDBの現在パスワードとの一致チェック
            if (osolEncipher.verify(newPassword, dbPassword) || osolEncipher.verify(newPassword, dbTempPassword)) {
                eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password is the same as the previous password."));
                addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.PasswordIsTheSameAsThePreviousPassword"));
                return "";
            }
        } else {
            if (!osolEncipher.verify(tempPassword, dbPassword)) {
                eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password is not equal temp password."));
                addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.notEqualTempPassword"));
                return "";
            }

            // 画面の新パスワードとDBの現在パスワードとの一致チェック
            if (osolEncipher.verify(newPassword, dbPassword) || osolEncipher.verify(newPassword, dbTempPassword)) {
                eventLogger.debug(LoginBean.class.getPackage().getName().concat(":: Password is the same as the previous password."));
                addErrorMessage(beanMessages.getMessage("loginEditPasswordBean.error.PasswordIsTheSameAsThePreviousPassword"));
                return "";
            }
        }
        // DB更新処理
        Timestamp timestamp = mPersonDao.getSvDate();
        dbPerson.setPassword(osolEncipher.hashing(newPassword));
        dbPerson.setTempPassword(null);
        dbPerson.setTempPassExpirationDate(null);
        dbPerson.setUpdateDate(timestamp);
        dbPerson.setUpdateUserId(dbPerson.getUserId());
        dbPerson.setUpdatePassDate(timestamp);
        dbPerson = mPersonDao.merge(dbPerson);
        setLoginPerson(dbPerson);
        setSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.SET_PASSWORD.getVal(), SESSION_VAL_OK);
        eventLogger.debug(LoginBean.class.getPackage().getName().concat(":END"));

        return indexBean.init();
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getPasswordLimitDays() {
        if (passwordLimitDays == null) {
            passwordLimitDays = this.getWrapped().getInitParameter(OsolConstants.LOGIN_PASS_EXPIRED_DAYS);
        }
        return passwordLimitDays;
    }
}
