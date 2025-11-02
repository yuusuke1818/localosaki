
$(function() {
    //アコーディオンメニューの初期設定
    setInitAccordion();

    //読み込み時に削除ボタンは無効化
    $(".delete_button input[type='submit']").prop('disabled', true);

    /**
     * 一覧のチェックボックス操作時
     * .search_result_area内のcheckbox on/off時、チェックが一つでもあれば初期化/削除チェックボックスを有効化する
     */
    $(".search_result_area input[type='checkbox']").change(checkChecked);

     /**
     * 初期化/削除のチェックボックス操作時
     * .del_toggle_check_area内のcheckbox on/offに対応
     */
    $(".del_toggle_check_area input[type='checkbox']").change(function() {
        if ($(".del_toggle_check_area input[type='checkbox']").prop('checked')) {
            $(".delete_button input[type='submit']").prop('disabled', false);
        } else {
            $(".delete_button input[type='submit']").prop('disabled', true);
        }
    })

     /**
     * 選択ボタン クリック時の全チェックリストトグル
     */
    $(".table_header .all_check").click(function() {
        all_check = true;
        $(".search_result_area input[type='checkbox']").each(function(index,element) {
            if (!$(element).prop('checked')) {
                all_check = false;
            }
        })
        if (all_check) {
            $(".search_result_area input[type='checkbox']").each(function(index,element) {
                $(element).prop('checked', false);
            })
        } else {
            $(".search_result_area input[type='checkbox']").each(function(index,element) {
                $(element).prop('checked', true);
            })
        }
        checkChecked();
    })
})

function setInitAccordion() {
    $(".contents_accordion_header").parent().children(
            ".contents_accordion_body").show();
    $(".contents_accordion_header").find(".button_opener").hide();
    $(".contents_accordion_header").find(".button_closer").show();
}

function checkChecked() {
    check_contain = false;
    $(".search_result_area input[type='checkbox']").each(function(index,element) {
        if ( $(element).prop('checked')) {
            check_contain = true;
        }
    })
    if (check_contain) {
        $(".del_toggle_check_area input[type='checkbox']").prop('disabled', false);
    } else {
        //無効化時はチェックも外す
        $(".del_toggle_check_area input[type='checkbox']").prop('disabled', true);
        $(".del_toggle_check_area input[type='checkbox']").prop('checked', false).change();
    }
}