/**
 * 
 * @param {type} data
 * @returns {Boolean}
 */
function autoFocusOnSelectCondition(data) {
    var ajaxStatus = data.status;
    if (ajaxStatus === "success") {
        var parentNext = $(data.source.parentElement).next()[0];	//親のnext
        var nextid = $(parentNext).next()[0].id;					//さらにnext（キーワードやドロップダウンが入るtdタグ
        var sp = nextid.split(":");
        var id = sp[sp.length - 1];			//idの末尾
        var idx = sp[sp.length - 2];			//id末尾の手前(dataTableのindex)
        var nodes = $("td[id$=" + id + "]");		//id末尾でセレクト(条件の数分見つかる)
        var targettd = nodes[idx];
        var target = $(targettd).children()[0];		//tdの子(inputだったりselectだったり)
        target.focus();
    }
    return true;
}

/**
 * 
 * @returns {Boolean}
 */
function searchConditionKeydown() {
    if (event.keyCode === 13) {
        //$('input[id$=searchButton]').focus(); //ここでfocus移してもrender=@formなので消える問題
        $('input[id$=searchButton]').click();
        return false;
    }
}
/**
 * 汎用建物検索用
 */
function searchPopupConditionKeydown() {
    if (event.keyCode === 13) {
        $('input[id$=searchBtn]').click();
        return false;
    }
}