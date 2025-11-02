package jp.co.osaki.sms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MPerson;
import jp.skygroup.enl.webap.base.BaseDao;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * OsolDaoクラス
 *
 * persist DelFlg,CreateUserId,CreateDate,UpdateUserId,UpdateDate
 * mergeで、UpdateUserId,UpdateDate
 * を自動的にセットしてからBaseDaoのpersist,mergeを呼び出す。
 *
 * @author take_suzuki
 */
public abstract class SmsDao extends BaseDao {

    /**
     * 新規登録処理
     *
     * @param <T>
     * @param impl 実行対象のインターフェース
     * @param target 登録対象のEntity
     */
    @Override
    protected <T> void persist(BaseServiceDao<T> impl, T target) {

        if (this.getWrapped() != null) {
            HttpSession session = (HttpSession) this.getWrapped().getSession(false);
            if (session != null){
                MPerson loginPerson = (MPerson)getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());
                if (loginPerson != null){
                    try {
                        Timestamp serverTime = this.getSvDate();
                        //作成者IDを設定
                        Method mSetCreateUserId = target.getClass().getMethod("setCreateUserId",Long.class);
                        mSetCreateUserId.setAccessible(true);
                        mSetCreateUserId.invoke(target,loginPerson.getUserId());
                        //作成日時を設定
                        Method mSetCreateDate = target.getClass().getMethod("setCreateDate",java.sql.Timestamp.class);
                        mSetCreateDate.setAccessible(true);
                        mSetCreateDate.invoke(target, serverTime);
                        //更新者IDを設定
                        Method mSetUpdateUserId = target.getClass().getMethod("setUpdateUserId",Long.class);
                        mSetUpdateUserId.setAccessible(true);
                        mSetUpdateUserId.invoke(target,loginPerson.getUserId());
                        //更新日時を設定
                        Method mSetUpdateDate = target.getClass().getMethod("setUpdateDate",java.sql.Timestamp.class);
                        mSetUpdateDate.setAccessible(true);
                        mSetUpdateDate.invoke(target, serverTime);
                    } catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
                         errorLogger.error(BaseUtility.getStackTraceMessage(e));
                   }
                }
            }
        }
        //BaseDaoの新規登録処理を呼び出す
        super.persist(impl, target);
    }
    /**
     * 更新処理
     *
     * @param impl 実行対象のインターフェース
     * @param target 更新対象のEntity
     * @return 更新後のEntity
     */
    @Override
    protected <T> T merge(BaseServiceDao<T> impl, T target) {

        if (this.getWrapped() != null) {
            HttpSession session = (HttpSession) this.getWrapped().getSession(false);
            if (session != null){
                MPerson loginPerson = (MPerson)getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());
                if (loginPerson != null){
                    try {
                        //更新者IDを設定
                        Method mSetUpdateUserId = target.getClass().getMethod("setUpdateUserId",Long.class);
                        mSetUpdateUserId.setAccessible(true);
                        mSetUpdateUserId.invoke(target,loginPerson.getUserId());
                        //更新日時を設定
                        Method mSetUpdateDate = target.getClass().getMethod("setUpdateDate",java.sql.Timestamp.class);
                        mSetUpdateDate.setAccessible(true);
                        mSetUpdateDate.invoke(target, this.getSvDate());
                    } catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
                        errorLogger.error(BaseUtility.getStackTraceMessage(e));
                    }
                }
            }
        }
        //BaseDaoの更新処理を呼び出す
        return super.merge(impl, target);
    }

}
