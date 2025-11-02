function clickDefaultPageButton() {
    showBlockModal();
    var screenId = $('span[id$=defaultLoginScreenId]').text();
    var disabledFlg = false;
    var maintenanceCd = $('span[id$=loginCorpTypeIsMaintenance]').text();
    
    // メンテナンスユーザー以外
    if(maintenanceCd === "false") {
        if (screenId === '04') {
            // 担当企業操作
            disabledFlg = $('input[id$=corpSelectBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=corpSelectBtn]').click();
            }
        } else if (screenId === '05') {
            // エネルギー入力建物・テナント一覧
            disabledFlg = $('input[id$=energyInputSearchBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=energyInputSearchBtn]').click();
            }
        } else if (screenId === '15') {
            // 行政報告
            disabledFlg = $('input[id$=reportBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=reportBtn]').click();
            }
        } else if (screenId === '18') {
            // ユーザー情報一覧
            disabledFlg = $('input[id$=userInfoListBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=userInfoListBtn]').click();
            }
        } else if (screenId === '22') {
            // 企業情報
            disabledFlg = $('input[id$=companyEditBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=companyEditBtn]').click();
            }
        } else if (screenId === '27') {
            // 入力建物・情報一覧
            disabledFlg = $('input[id$=buildingInfoSearchBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=buildingInfoSearchBtn]').click();
            }
        } else if (screenId === '33') {
            // 建物・テナントグルーピング設定
            disabledFlg = $('input[id$=buildinggroupingListBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=buildinggroupingListBtn]').click();
            }
        } else if (screenId === '43') {
            // 設備情報一覧
            disabledFlg = $('input[id$=facilitySearchBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=facilitySearchBtn]').click();
            }
        } else if (screenId === '48') {
            disabledFlg = $('input[id$=maintenanceListBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=maintenanceListBtn]').click();
            }
        } else if (screenId === '51') {
            // ログイン許可IP
            disabledFlg = $('input[id$=systemManagementBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=systemManagementBtn]').click();
            }
        } else if (screenId === '52') {
            // 集計・分析
            disabledFlg = $('input[id$=analysisAggregateBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=analysisAggregateBtn]').click();
            }
        } else if (screenId === '60') {
            // 計画履行　入力建物・テナント一覧
            disabledFlg = $('input[id$=planDoBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=planDoBtn]').click();
            }
        } else if (screenId === '101') {
            // サイネージ
            disabledFlg = $('input[id$=signageBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=signageBtn]').click();
            }
        } else if (screenId === '103') {
            // エネルギー使用状況
            disabledFlg = $('input[id$=demandInfoBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=demandInfoBtn]').click();
            }
        } else if (screenId === '200') {
            // 機器制御
            disabledFlg = $('input[id$=deviceControlBtn]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=deviceControlBtn]').click();
            }
        } else {
            disabledFlg = true;
        }
    }
    // メンテナンスユーザー
    else {
        if (screenId === '18') {
            // ユーザー情報一覧
            disabledFlg = $('input[id$=userInfoListBtnToMaintenance]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=userInfoListBtnToMaintenance]').click();
            }
        } else if (screenId === '43') {
            // 設備情報一覧
            disabledFlg = $('input[id$=facilitySearchBtnToMaintenance]').prop("disabled");
            if (!disabledFlg) {

                $('input[id$=facilitySearchBtnToMaintenance]').click();
            }
        } else if (screenId === '48') {
            disabledFlg = $('input[id$=maintenanceListBtnToMaintenance]').prop("disabled");
            if (!disabledFlg) {
                $('input[id$=maintenanceListBtnToMaintenance]').click();
            }
        } else {
            disabledFlg = true;
        }
    }

    if (disabledFlg) {
        showMenu();
        $('input[id$=canselShowOshiraseButton]').click();
    }
}
function showMenu() {
    $("#content").show();
    hideBlockModal();
}
// template.xhtmlと同じ内容
// 読み込むタイミングが違うのでこちらにも記載
function showBlockModal() {
    if ($("#block-modal-overlay")[0])
        return false;
    //オーバーレイ用のHTMLコードを、[body]内の最後に生成する
    $("body").append('<div id="block-modal-overlay"></div>');
    $("#block-modal-overlay").show();
    $("#block-modal-content").show();
    var w = $(window).width();
    var h = $(window).height();
    var cw = $("#block-modal-content").width();
    var ch = $("#block-modal-content").height();
    var pxleft = ((w - cw) / 2);
    var pxtop = ((h - ch) / 2);
    $("#block-modal-content").css({"left": pxleft + "px"});
    $("#block-modal-content").css({"top": pxtop + "px"});
    keyBlock = true;
    return true;
}
function hideBlockModal(nonScrollFlg) {
    $("#block-modal-overlay").hide(0, function () {
        $("#block-modal-overlay").remove();
    });
    $("#block-modal-content").hide();
    keyBlock = false;
    if (!nonScrollFlg) {
        window.scrollTo(0, 0);
    }
}
