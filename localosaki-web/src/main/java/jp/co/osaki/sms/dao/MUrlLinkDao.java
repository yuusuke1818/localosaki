package jp.co.osaki.sms.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MUrlLink;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MUrlLinkServiceDaoImpl;

/**
 * URLリンクマスタDaoクラス
 *
 * @author y-maruta
 */
@Stateless
public class MUrlLinkDao extends SmsDao {

    private final MUrlLinkServiceDaoImpl impl;

    public MUrlLinkDao() {
        impl = new MUrlLinkServiceDaoImpl();
    }

    /**
     * URLリンク情報取得
     *
     * @param url_link_code URLリンクコード
     * @return URLリンク情報Bean
     */
    public MUrlLink find(String url_link_code) {

        // 細分類情報キーBeanにキーをセットする
        MUrlLink ul = new MUrlLink();
        ul.setUrlLink(url_link_code);

        // 親クラスのメソッドに実行対象のクラスを指定し処理を以上する
        MUrlLink result = find(impl, ul);

        return result;
    }

    /**
     * URLリンク情報 更新処理
     *
     * @param target 細分類情報
     * @return 更新結果
     */
    public MUrlLink merge(MUrlLink target) {
        MUrlLink result = merge(impl, target);
        return result;
    }

    /**
     * URLリンク情報 新規登録
     *
     * @param target 細分類情報
     */
    public void register(MUrlLink target) {
        persist(impl, target);
    }

    /**
     * URLリンク情報 削除
     *
     * @param target 細分類情報
     */
    public void remove(MUrlLink target) {
        remove(impl, target);
    }
}
