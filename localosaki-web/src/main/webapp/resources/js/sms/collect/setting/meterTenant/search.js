/* global buildinggroupingListBean */
window.onload = function () {
    pushShowOshiraseButton();
};

function selectTab(tabNum) {
    var tabsCount = 5;
    for (var i = 1; i <= tabsCount; i++) {
        tabName = 'tab' + i;
        contentName = 'content' + i;
        elTab = document.getElementById(tabName);
        elContent = document.getElementById(contentName);
        if (i == tabNum) {
            elContent.style.display = "block";
            elTab.style.background = "#191970";
            elTab.style.color = "#FFF";
        } else {
            elContent.style.display = "none";
            elTab.style.background = "rgb(238,237,237)";
            elTab.style.color = "#000";
        }
    }
}

$(function () {
    /* テキスト表示用 */
    $(document).ready(function () {
        $('.numeric_convert').each(function (i) {
            var num = $(this).val();
            if ($(this).is('.numeric_convert')) {
                $(this).val(addZeroAtHead(addComma(num)));
            }
        });
    });

    $('.numeric_convert').on('focus', function () {
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_convert').on('blur', function () {
        var num = $(this).val();
        $(this).val(addZeroAtHead(addComma(num)));
    });
});

/* 関数名 入力待ち操作                */
/* 内容 テキスト入力中はイベントを停止 */
/* 引数 なし                         */
function inputwait() {

    event.stopPropagation();
    event.cancelBubble = true;
}

/* 関数名 削除ボタンの押下            */
/* 内容 削除ボタン押下で親グループ削除 */
/* 引数 親グループの表示順            */
function deleteAct() {

    event.stopPropagation();
//    event.cancelBubble = true;
//
//    document.getElementById("buildinggroupingListBean:repeat:"+ parent_num +":collapsiblePanel").style="display: none;";
//    document.getElementById("buildinggroupingListBean:repeat:"+ parent_num +":panelDropTargetBig1").style="display: none;";
//
//    var parent_group_num = parseInt(document.getElementById("buildinggroupingListBean:parent_group_number").textContent);
//    parent_group_num = parent_group_num -1;
//    if( parent_group_num < 0 )  {
//        parent_group_num = 0;
//    }
//    document.getElementById("buildinggroupingListBean:parent_group_number").textContent = parent_group_num;
}

/* 関数名 削除ボタンの押下            */
/* 内容 削除ボタン押下で子グループ削除 */
/* 引数 親グループの表示順            */
/* 引数 子グループの表示順            */
function deleteChildAct(parent_num, child_num) {

    event.stopPropagation();
    event.cancelBubble = true;

    var child = document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":energyCdTree").children;
    //child[0].children[0].children[0].children[child_num*2   ].style="display: none;";
    //child[0].children[0].children[0].children[child_num*2 +1].style="display: none;";

    var unreg_num = parseInt(document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":unregistered_building_number").textContent);
    var del_child_num = parseInt(document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":repeatChild:" + child_num + ":child_building_number").textContent);
    unreg_num = unreg_num + del_child_num;

    document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":unregistered_building_number").textContent = unreg_num;

    var child_group_num = parseInt(document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":child_group_number_open").textContent);
    child_group_num = child_group_num - 1;
    if (child_group_num < 0) {
        child_group_num = 0;
    }
    document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":child_group_number_open").textContent = child_group_num;
    document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":child_group_number_close").textContent = child_group_num;

    if (child_group_num === 0) {

        // var child_droptop = document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":panelDropTargetSmall2");
        // child_droptop.style="display: none;";

        //var table_child = document.getElementById("buildinggroupingListBean:repeat:" + parent_num + ":energyCdTree");
        //table_child.style="display: none;";

    }

}


/* 関数名 削除ボタンの押下            */
/* 内容 削除ボタン押下で使用エネルギーの削除 */
/* 引数 エネルギーの表示順            */
/* 引数 使用エネルギーの表示順            */
/* 引数 使用エネルギーの表示数            */
function deleteAvailableEnergyAct(parent_num, child_num, child_groups_size) {

    event.stopPropagation();
    event.cancelBubble = true;

    var child = document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":energyCdTree").children;
    child[0].children[0].children[0].children[child_num * 2   ].style = "display: none;";
    child[0].children[0].children[0].children[child_num * 2 + 1].style = "display: none;";

    var reg_num = parseInt(document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":energyTypeList_open").textContent);
    reg_num = reg_num - 1;
    if (reg_num < 0) {
        reg_num = 0;
    }
    document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":energyTypeList_open").textContent = reg_num;
    document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":energyTypeList_close").textContent = reg_num;

    // 登録が無くなったら、親とそのドラッグエリア含めて消去対象
    if (child_groups_size === 1) {

        var parent_drop = document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":panelDropTargetBig1");
        parent_drop.style = "display: none;";

        var collapsible_panel = document.getElementById("buildingInfoEditBean:repeat:" + parent_num + ":collapsiblePanel");
        collapsible_panel.style = "display: none;";

        var total_num = parseInt(document.getElementById("buildingInfoEditBean:usedEnergyTypeSize").textContent);
        total_num = total_num - 1;
        if (total_num < 0) {
            total_num = 0;
        }
        document.getElementById("buildingInfoEditBean:usedEnergyTypeSize").textContent = total_num;
    }

}


/* 関数名 子グループの操作          */
/* 内容 子グループのオンオフ操作     */
/* 引数 child_num: 子グループの指定 */
/* 引数 action: オン操作、オフ操作  */
function checkbox_all_act(child_num, action) {
    // チェックボックスの取得
    var input_nodes = document.getElementById("buildinggroupingEditCategoryBean:result_child" + child_num).getElementsByTagName("input");
    for (count = 0; count < input_nodes.length; count++) {
        if (action === "on") {
            input_nodes[count].checked = true;
        } else {
            input_nodes[count].checked = false;
        }
    }
    // 子グループチェック背景色 判定
    check_chg(child_num);
}

/* 関数名 子グループチェック背景色 判定    */
/* 内容 子グループの選択した行の背景色変更 */
/* 引数 child_num: 子グループの指定      */
function check_chg(child_num) {
    // チェックボックスのテーブルbodyを取得
    var tbody = document.getElementById("buildinggroupingEditCategoryBean:result_child" + child_num).getElementsByTagName("tbody")[0].children;
    var select_count = 0;
    for (count = 0; count < tbody.length; count++) {
        if (tbody[count].children[0].children[0].checked === true) {
            select_count++;
            // 行の個数だけ同一色を展開
            for (drw = 0; drw < tbody[count].childElementCount; drw++) {
                tbody[count].children[drw].style.background = "rgb(235,248,254)";
            }
        } else {
            // 行の個数だけ同一色を展開
            for (drw = 0; drw < tbody[count].childElementCount; drw++) {
                tbody[count].children[drw].style.background = "#FFFFFF";
            }
        }
    }
    // 子グループ選択数
    select_child(select_count, child_num);
}

/* 関数名 子グループ選択数             */
/* 内容 子グループの選択した数を決定   */
/* 引数 select_count: 選択した数       */
/* 引数 child_num: 子グループの指定   */
function select_child(select_count, child_num) {
    document.getElementById("buildinggroupingEditCategoryBean:selectchild" + child_num).textContent = select_count;
}