/**
 * 全選択チェックボックスクリック時
 *
 * @param checked 全選択チェックボックスチェック状態
 * @param index 行インデックス
 */
function clickAllCheck(checked, index) {
    $(".inspectionMonth" + index).prop('checked', checked);
}

/**
 * ページトップに遷移
 */
function scrollPageTop() {
    $("body, html").animate({
        scrollTop: 0
    }, 0);
}