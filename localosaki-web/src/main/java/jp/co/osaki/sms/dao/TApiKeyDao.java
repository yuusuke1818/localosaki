package jp.co.osaki.sms.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.TApiKeyServiceDaoImpl;

/**
 * APIキーマスタDaoクラス
 *
 * @author y-maruta
 */
@Stateless
public class TApiKeyDao extends SmsDao {

    private final TApiKeyServiceDaoImpl impl;

    public TApiKeyDao() {
        impl = new TApiKeyServiceDaoImpl();
    }

    /**
     * APIキー情報取得
     *
     * @param corpId 企業番号
     * @param personId 担当者番号
     * @return APIキー情報
     */
    public TApiKey find(String corpId,String personId) {

        // APIキーBeanにキーをセットする
        TApiKey apikey = new TApiKey();
        TApiKeyPK apikeyPk = new TApiKeyPK();
        apikeyPk.setCorpId(corpId);
        apikeyPk.setPersonId(personId);
        apikey.setId(apikeyPk);

        // 親クラスのメソッドに実行対象のクラスを指定し処理を委譲する
        TApiKey result = find(impl, apikey);

        return result;
    }

    /**
     * APIキー情報 更新処理
     *
     * @param target APIキー情報
     * @return 更新結果
     */
    public TApiKey merge(TApiKey target) {
        TApiKey result = merge(impl, target);
        return result;
    }

    /**
     * APIキー情報 新規登録
     *
     * @param target APIキー情報
     */
    public void register(TApiKey target) {
        persist(impl, target);
    }

    /**
     * APIキー情報 削除
     *
     * @param target APIキー情報
     */
    public void remove(TApiKey target) {
        remove(impl, target);
    }
}
