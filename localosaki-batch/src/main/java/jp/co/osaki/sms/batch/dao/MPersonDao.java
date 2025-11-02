package jp.co.osaki.sms.batch.dao;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.sms.batch.SmsBatchDao;

/**
 * 担当者Daoクラス
 *
 * @author y.nakamura
 */
public class MPersonDao extends SmsBatchDao {

    /**
     * コンストラクタ
     *
     * @param entityManager
     */
    public MPersonDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * 担当者情報取得
     *
     * @param corpId 企業ID(第一キー)
     * @param personId 担当者ID(第二キー)
     * @return 担当者情報Bean
     */
    public MPerson find(String corpId, String personId) {

        MPersonPK mpk = new MPersonPK();
        mpk.setCorpId(corpId);
        mpk.setPersonId(personId);
        MPerson result = entityManager.find(MPerson.class, mpk);

        return result;

    }

    /**
     * 担当者情報 新規登録
     *
     * @param targetBean 担当者情報Bean
     */
    public void register(MPerson targetBean) {

        entityManager.persist(targetBean);

    }

    /**
     * 担当者情報 更新処理
     *
     * @param target 担当者情報
     * @return 更新結果()
     */
    public MPerson merge(MPerson target) {

        MPerson result = entityManager.merge(target);

        return result;
    }

}