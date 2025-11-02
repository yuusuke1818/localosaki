package jp.co.osaki.sms.batch.dao;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TApiKey;
import jp.co.osaki.osol.entity.TApiKeyPK;
import jp.co.osaki.sms.batch.SmsBatchDao;

/**
 * APIキーDaoクラス
 *
 * @author y.nakamura
 */
public class TApiKeyDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public TApiKeyDao(EntityManager entityManager) {
        super(entityManager);
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

        // APIキー情報を1件取得
        TApiKey result = entityManager.find(TApiKey.class, apikey.getId());

        return result;
    }

    /**
     * APIキー情報 更新処理
     *
     * @param target APIキー情報
     * @return 更新結果
     */
    public TApiKey merge(TApiKey target) {
        TApiKey result = entityManager.merge(target);
        return result;
    }

    /**
     * APIキー情報 新規登録
     *
     * @param target APIキー情報
     */
    public void register(TApiKey target) {
        entityManager.persist(target);
    }

    /**
     * APIキー情報 削除
     *
     * @param target APIキー情報
     */
    public void remove(TApiKey target) {
        TApiKey ms = entityManager.find(TApiKey.class, target.getId());
        entityManager.remove(ms);
    }
}
