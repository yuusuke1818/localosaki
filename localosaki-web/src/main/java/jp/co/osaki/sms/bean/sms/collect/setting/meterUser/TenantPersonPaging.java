package jp.co.osaki.sms.bean.sms.collect.setting.meterUser;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import jp.skygroup.enl.webap.base.BasePaging;

/**
 * 担当テナントページング
 *
 * @author nishida.t
 */
@Named("smsCollectSettingMeterUserTenantPersonPaging")
@Dependent
public class TenantPersonPaging extends BasePaging<TenantPersonDispBean> implements Serializable {

    private static final long serialVersionUID = 4274198851857134041L;

    /**
     *
     * ページング処理　次のページへ
     *
     */
    @Override
    public void nextPage() {
        // 現在ページの取得
        List<TenantPersonDispBean> targetCurentList = getPageList();

        // 現在ページの情報をマージする
        mergeCurrentPage(targetCurentList);

        // 親のネクスト
        super.nextPage();
    }

        /**
     * 指定ページ遷移
     *
     */
    public void pageJump() {
        // 現在ページの取得
        List<TenantPersonDispBean> targetCurentList = getPageList();

        // 現在ページの情報をマージする
        mergeCurrentPage(targetCurentList);
    }

    /**
     *
     * ページング処理　前のページへ
     *
     */
    @Override
    public void prevPage() {
        // 現在ページの取得
        List<TenantPersonDispBean> targetCurentList = getPageList();

        // 現在表示情報をマージする
        mergeCurrentPage(targetCurentList);

        // 親のプレビュー
        super.prevPage();
    }

    /**
     * 現在ページ選択
     */
    public void allSelected() {
        // 一括用選択
        List<TenantPersonDispBean> resultList = super.getResultList();
        for (TenantPersonDispBean target : resultList) {
            target.setBuildingPersonFlg(Boolean.TRUE);
        }
        // 設定する
        super.setResultList(resultList);
    }

    /**
     * 現在ページ選択解除
     */
    public void allUnSelected() {
        // 一括用選択
        List<TenantPersonDispBean> resultList = getResultList();
        for (TenantPersonDispBean target : resultList) {
            target.setBuildingPersonFlg(Boolean.FALSE);
        }
        // 設定する
        super.setResultList(resultList);

    }

    /**
     * 全部ページ選択
     */
    public void currentAllSelected() {

        // 現在ページ情報を取得
        List<TenantPersonDispBean> currentList = super.getPageList();

        // 現在前ページ情報を取得
        List<TenantPersonDispBean> resultList = super.getResultList();

        // 現在ページ情報分処理する
        for (TenantPersonDispBean targetBean : currentList) {
            int index = resultList.indexOf(targetBean);
            // セットする前に選択状態にする
            targetBean.setBuildingPersonFlg(Boolean.TRUE);
            resultList.set(index, targetBean);

        }

        // 設定する
        super.setResultList(resultList);
    }

    /**
     * 全部ページ選択解除
     */
    public void currentAllUnSelected() {
        // 現在ページ情報を取得
        List<TenantPersonDispBean> currentList = super.getPageList();

        // 現在前ページ情報を取得
        List<TenantPersonDispBean> resultList = super.getResultList();

        // 現在ページ情報分処理する
        for (TenantPersonDispBean targetBean : currentList) {
            int index = resultList.indexOf(targetBean);
            // セットする前に選択状態にする
            targetBean.setBuildingPersonFlg(Boolean.FALSE);
            resultList.set(index, targetBean);

        }

        // 設定する
        super.setResultList(resultList);

    }

    // ------
    /**
     * 担当テナント設定画面は登録ボタンを押すと全ての情報を登録する
     *
     * @return 担当テナント表示用BeanList
     */
    public List<TenantPersonDispBean> getAllPageInfoList() {
        return super.getResultList();
    }

    /**
     * 表示ページマージ処理
     *
     * @param targetCurentList 現在ページ表示情報
     */
    private void mergeCurrentPage(List<TenantPersonDispBean> targetCurentList) {
        for (TenantPersonDispBean target : targetCurentList) {
            // 現在情報の探す
            int index = getResultList().indexOf(target);
            getResultList().set(index, target);
        }
    }
}
