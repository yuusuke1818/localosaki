var isDailyContentsShow;

/** 値保持用(装置ID) */
var devId;
/** 値保持用(装置名) */
var devName;
/** 選択中の処理選択ボタン */
var selectBtn = 1;

/**
 * 各イベント時の処理
 */
$(function() {
    devId = $(".selDev").val();
    devName = $(".selDev option:selected").text();

    /**
     * 接続先セレクトボックスの選択状態が変更された時
     */
    $(document).on('change', '.selDev', function() {
        devId = $(".selDev").val();
        devName = $(".selDev option:selected").text();
    })

    window.onload = function() {
        setInitAccordion();
    };

    function setInitAccordion() {
        $(".contents_accordion_header").parent().children(
            ".contents_accordion_body").show();
        $(".contents_accordion_header").find(".button_opener").hide();
        $(".contents_accordion_header").find(".button_closer").show();
    }

    $(document).on('click', '.contents_accordion_header', function() {
        $(this).parent().children(".contents_accordion_body").slideToggle();
        $(this).children(".button_icon").toggle();
    })

})


/**
 * メッセージエリア表示制御
 *
 * @param isSearch "1":検索時メッセージ表示  "0":登録・更新・削除時メッセージ表示
 */
function setErrMsgArea(isSearch) {
    $(".errArea").hide();
    if (isSearch == '1') {
        $(".errSearch").show();
    } else { // if (isSearch == '0') {
        try {
            $(".errNonSearch").show();
        } catch(e) {
            $(".errSearch").show();
        }
    }
}
