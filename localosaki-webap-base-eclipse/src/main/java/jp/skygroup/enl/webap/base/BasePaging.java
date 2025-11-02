package jp.skygroup.enl.webap.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * Baseページングクラス
 *
 * 画面Beanに指定された検索結果Listのページング機能を提供するクラス
 *
 * @author take_suzuki
 * @param <T> 表示するEntityのList
 */
@Named(value ="basePaging")
@Dependent
public class BasePaging<T> extends BaseConstants implements Serializable {

    private static final long serialVersionUID = 2590247609935562326L;

    /**
     * 表示対象のリスト
     */
    private List<T> pageList;

    /**
     * 検索結果のリスト
     */
    private List<T> resultList;

    /**
     * 1ページ辺りの表示件数
     */
    private int pageSize = 10;

    /**
     * カレントページ
     */
    private int currentPage = 0;

    /**
     * 最大ページ数
     */
    private int maxPage = 0;

    /**
     * コンストラクタ
     */
    public BasePaging() {
        // 表示対象のリストを初期化
        pageList = new ArrayList<>();
        resultList = new ArrayList<>();
        pageSize = 10;
        maxPage = 1;
        currentPage = 1;
    }

    /**
     *
     * 初期処理
     *
     * @param paramResultList 検索結果のリスト
     */
    public void init(List<T> paramResultList) {

        //1ページ辺りの表示件数を設定
        if (FacesContext.getCurrentInstance().getExternalContext().getInitParameter("DEFAULT_PER_PAGE") != null){
            this.init(paramResultList,Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("DEFAULT_PER_PAGE")));
        } else {
            this.init(paramResultList,this.pageSize);
        }
    }

    /**
     *
     * 初期処理
     *
     * @param resultList 検索結果のリスト
     * @param paramPageSize 1ページ辺りの最大表示件数
     */
    public void init(List<T> paramResultList, int paramPageSize) {

        this.resultList = paramResultList;
        this.pageSize = paramPageSize;
        maxPage = 0;
        if (paramPageSize >= 1) {
            // 1ページ以上の時
            //件数を1ページの表示件数で割った数 = 最大ページ数
            maxPage = resultList.size() / paramPageSize;
            // 余りを計算
            if ((resultList.size() % paramPageSize) > 0) {
                // 余りがある場合+1ページ
                maxPage++;
            }
        } else {
            maxPage = 1;
        }
        if (this.getMaxPage() <= 0) {
            this.currentPage = 0;
            // 初期化
            pageList.clear();
        } else {
            // 1ページ目を設定
            setCurrentPage(1);
        }
    }

    /**
     *
     * カレントページを取得
     *
     * @return カレントページ
     */
    public int getCurrentPage() {
        return this.currentPage;
    }

    /**
     *
     * カレントページを設定
     *
     * @param currentPage 設定するページ
     */
    public void setCurrentPage(int currentPage) {
        if (1 <= currentPage && currentPage <= maxPage) {
            this.currentPage = currentPage;
            exchangeList();
        }
    }

    /**
     *
     * 1ページ辺りの最大表示件数設定
     *
     * @param pageSize 表示件数
     */
    public void setPageSize(int pageSize) {

        this.pageSize = pageSize;
        maxPage = 0;
        if (pageSize >= 1) {
            // 1ページ以上の時
            //件数を1ページの表示件数で割った数 = 最大ページ数
            maxPage = resultList.size() / pageSize;
            // 余りを計算
            if ((resultList.size() % pageSize) > 0) {
                // 余りがある場合+1ページ
                maxPage++;
            }
        } else {
            maxPage = 1;
        }
        if (this.getMaxPage() <= 0) {
            this.currentPage = 0;
            // 初期化
            pageList.clear();
        } else {
            setCurrentPage(1);
        }
    }

    /**
     *
     * 1ページ辺りの最大表示件数取得
     *
     * @return 表示件数
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     *
     * 検索結果件数取得
     *
     * @return maxPage 件数取得
     */
    public int getResultCount() {
        if (resultList == null) {
            return 0;
        }
        return this.resultList.size();
    }

    /**
     *
     * 最大ページ数取得
     *
     * @return maxPage 最大ページ数
     */
    public int getMaxPage() {
        return this.maxPage;
    }

    /**
     * ページ値取得
     *
     * @return "[currentPage]/[maxPage]"
     */
    public String getPageValue() {
        return currentPage + "/" + maxPage;
    }

    /**
     *
     * ページング処理　次のページへ
     *
     */
    public void nextPage() {
        if (this.isNextPage()) {
            this.currentPage++;
            exchangeList();
        }
    }

    /**
     *
     * 次ページ有効判定
     *
     * @return true:次のページ有効 false:次のページ無効
     */
    public boolean isNextPage() {
        return this.currentPage < maxPage;
    }

    /**
     *
     * ページング処理　前のページへ
     *
     */
    public void prevPage() {
        if (this.isPrevPage()) {
            this.currentPage--;
            exchangeList();
        }
    }

    /**
     *
     * 前ページ有効有効判定
     *
     * @return true:前のページへ有効 false:前のページ無効
     */
    public boolean isPrevPage() {
        return this.currentPage > 1;
    }

    /**
     *
     * ページング処理 指定ページ移動
     *
     * @param targePage 指定ページ番号
     */
    public void setPage(int targePage) {
        if (targePage >= 1 || targePage <= this.getMaxPage()) {
            this.currentPage = targePage;
            exchangeList();
        }
    }

    /**
     *
     * 現在の情報リストを表示情報リストへ追加する
     *
     * @return 1ページに表示するリスト
     */
    private List<T> exchangeList() {

        // 初期化
        pageList.clear();
        // リストのindexは0始まり
        int target = ((currentPage - 1) * pageSize);
        int endIndex = target + pageSize;

        // 検索結果が1ページ表示分より少ない場合
        if (endIndex > resultList.size()) {
            endIndex = resultList.size();
        }
        // 1ページ数に表示する検索結果の表示内容に詰め込む
        for (int index = target; index < endIndex; index++) {
            // 詰め込み処理
            T exchage = resultList.get(index);
            pageList.add(exchage);
        }
        return pageList;
    }

    /**
     * 表示対象のリスト取得
     *
     * @return 表示対象のリスト
     */
    public List<T> getPageList() {
        return pageList;
    }

    public int getResultListSize(){
        return resultList.size();
    }

    protected List<T> getResultList() {
        return resultList;
    }

    protected void setResultList(List<T> targetResultList) {
        this.resultList = targetResultList;
    }
}
