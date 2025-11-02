package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MLoginIpAddr;
import jp.co.osaki.osol.entity.MLoginIpAddrPK;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.LoginIpAddrCheckFilterDaoImpl;
import jp.co.osaki.sms.servicedao.MPersonServiceDaoImpl;

/**
 *
 * @author take_suzuki
 */
@Stateless
public class LoginIpAddrCheckFilterDao extends SmsDao {

    private final MPersonServiceDaoImpl mpersonImpl;
    private final LoginIpAddrCheckFilterDaoImpl loginIpAddrCheckFilterDaoImpl;

    public LoginIpAddrCheckFilterDao() {
        this.mpersonImpl = new MPersonServiceDaoImpl();
        this.loginIpAddrCheckFilterDaoImpl = new LoginIpAddrCheckFilterDaoImpl();
    }

    /**
     * 担当者情報取得
     *
     * @param corp_id 企業ID(第一キー)
     * @param personId 担当者ID(第二キー)
     * @return 担当者情報Bean
     */
    public MPerson getPerson(String corp_id, String personId) {

        // 担当者情報キーBeanにキーをセットする
        MPerson mp = new MPerson();
        MPersonPK mpk = new MPersonPK();
        mpk.setCorpId(corp_id);
        mpk.setPersonId(personId);
        mp.setId(mpk);
        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        MPerson result = find(mpersonImpl, mp);

        return result;

    }
    /**
     * ログイン許可IPアドレス取得
     *
     * @param corp_id            企業ID
     * @param ipAddress          IPアドレス
     * @param loginPermitStatus  ログイン許可ステータス
     * @return ログイン許可IPアドレスリスト
     */
    public List<MLoginIpAddr> getLoginIpAddrList(String corp_id, String ipAddress, String loginPermitStatus) {

        // 担当者情報キーBeanにキーをセットする
        MLoginIpAddr mipa = new MLoginIpAddr();
        MLoginIpAddrPK mipapk = new MLoginIpAddrPK();
        mipapk.setCorpId(corp_id);
        mipa.setId(mipapk);
        mipa.setIpAddress(ipAddress);
        mipa.setLoginPermitStatus(loginPermitStatus);
        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        List<MLoginIpAddr> result = getResultList(loginIpAddrCheckFilterDaoImpl,mipa);

        return result;

    }
}
