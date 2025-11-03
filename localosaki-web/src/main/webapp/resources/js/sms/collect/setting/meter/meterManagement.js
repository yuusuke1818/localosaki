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
        hideMeterStatusColumns("all");
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

    /**
     * 一覧のチェックボックス操作時
     */
    $(document).on('click', ".tbl input[type='checkbox']", function() {
        dispButton();
        const chk = $(".tbl input[type='checkbox']:checked").length;
        if (chk > 0) {
            if (selectBtn == 4) {
                $(".btn_edit").removeClass('gradation_type_3').addClass('gradation_type_1').removeClass('gradation_type_5');
            } else if (selectBtn == 5) {
                $(".btn_setting").removeClass('gradation_type_3').addClass('gradation_type_1').removeClass('gradation_type_5');
            } else if (selectBtn == 6) {
                $(".btn_current").removeClass('gradation_type_3').addClass('gradation_type_1').removeClass('gradation_type_5');
            } else if (selectBtn == 7) {
                $(".btn_status").removeClass('gradation_type_3').addClass('gradation_type_1').removeClass('gradation_type_5');
            }
        } else {
            selectBtn = 1;
            $(".button_list").removeClass('gradation_type_3').addClass('gradation_type_1');
        }
    })

    /**
     * 削除ボタン活性/非活性の切替チェックボックス操作時
     */
    $(document).on(
        'click',
        ".delBtnAreaS input[type='checkbox']",
        function() {
            if ($(".delBtnAreaS input[type='checkbox']").prop("checked")) {
                // チェックされている場合
                setDeleteBtnDisabled(".s4DeleteBtn", false);
            } else {
                // チェックされていない場合
                setDeleteBtnDisabled(".s4DeleteBtn", true);
            }

        }
    );

    $(document).on(
        'click',
        ".delBtnAreaP input[type='checkbox']",
        function() {
            if ($(".delBtnAreaP input[type='checkbox']").prop("checked")) {
                // チェックされている場合
                setDeleteBtnDisabled(".p4DeleteBtn", false);
            } else {
                // チェックされていない場合
                setDeleteBtnDisabled(".p4DeleteBtn", true);
            }
        }
    );

    $(document).on(
            'click',
            ".delBtnAreaW input[type='checkbox']",
            function() {
                if ($(".delBtnAreaW input[type='checkbox']").prop("checked")) {
                    // チェックされている場合
                    setDeleteBtnDisabled(".w4DeleteBtn", false);
                } else {
                    // チェックされていない場合
                    setDeleteBtnDisabled(".w4DeleteBtn", true);
                }
            }
        );

    $(document).on(
        'click',
        ".delBtnAreaH input[type='checkbox']",
        function() {
            if ($(".delBtnAreaH input[type='checkbox']").prop("checked")) {
                // チェックされている場合
                setDeleteBtnDisabled(".h4DeleteBtn", false);
            } else {
                // チェックされていない場合
                setDeleteBtnDisabled(".h4DeleteBtn", true);
            }

        }
    );

    $(document).on(
        'click',
        ".delBtnAreaO input[type='checkbox']",
        function() {
            if ($(".delBtnAreaO input[type='checkbox']").prop("checked")) {
                // チェックされている場合
                setDeleteBtnDisabled(".o4DeleteBtn", false);
            } else {
                // チェックされていない場合
                setDeleteBtnDisabled(".o4DeleteBtn", true);
            }
        }
    );

    /**
     * 処理選択ボタン押下時
     */
    $(document).on(
        'click',
        ".buttons input[type='button']",
        function() {
            // いったん全部水色にする
            $(".buttons input[type='button']").parent().addClass('gradation_type_3').removeClass('gradation_type_1');
            // 一覧チェックされているか否かでグレーにするやつはグレーにする
            dispButton();
            // 今押されたボタンだけ青色にする
            $(this).parent().addClass('gradation_type_1').removeClass('gradation_type_3');

            // 処理タイプによって各項目の表示/非表示の制御を行う
            const type = $(this).data("type");
            selectBtn = Number(type.substr(1, 1)); // 選択されたボタンを保持しておく
            processingSelectBtnClick(type.substr(0, 1), selectBtn);
            controlExecBtn(); // LTE-Mの場合の設定内容変更にて実行ボタン制御
        }
    );

    /**
     * メッセージエリアを初期化
     */
    $(document).on('click', ".buttons input[data-type]", function () {
      const t = $(this).data('type');
      if (!/^s[2-7]$/.test(t)) return; // s1である一覧表示時は除外 登録等で実行した際に一覧表示にてメッセージ「登録しました。」を表示するため。
      clearServerMsgs();
    });

    // チェックONになっているものを全部OFFにしてリセットしておく
    $(".tbl input[type='checkbox']").prop('checked', false);
    // クリックイベント発火させる前にチェックON状態とする
    $(".delBtnAreaS input[type='checkbox']").prop('checked', true);
    $(".delBtnAreaP input[type='checkbox']").prop('checked', true);
    $(".delBtnAreaW input[type='checkbox']").prop('checked', true);
    $(".delBtnAreaH input[type='checkbox']").prop('checked', true);
    // 削除有効/無効のチェックボックスのクリックイベントを発火させる（OFFにする）
    $(".delBtnAreaS input[type='checkbox']").trigger("click");
    $(".delBtnAreaP input[type='checkbox']").trigger("click");
    $(".delBtnAreaW input[type='checkbox']").trigger("click");
    $(".delBtnAreaH input[type='checkbox']").trigger("click");

    // 処理選択ボタン選択状態：一覧
    selectBtn = 1;
})

/**
 * 削除ボタンの活性非活性切替処理
 */
function setDeleteBtnDisabled(id, disabled) {
    var removeClass = "gradation_type_5";
    var addClass = "gradation_type_1";
    if (disabled) {
        removeClass = "gradation_type_1";
        addClass = "gradation_type_5";
    }

    $(id).addClass(addClass).removeClass(removeClass);
    $(id + " input[type='submit']").prop('disabled', disabled);
}

/**
 * ボタンの活性/非活性切替処理
 */
function dispButton() {
    const cd610Val = $('input[id$="functionCd610Exists"]').val();
    const cd610Exists = String(cd610Val).toLowerCase() === 'true';
    
    const chk = $(".tbl input[type='checkbox']:checked").length;
    if (chk > 0) {
        $(".button_edit").removeClass('gradation_type_5').removeClass('gradation_type_1').addClass('gradation_type_3');
        $(".button_edit input[type='button']").prop('disabled', false);

        if (!cd610Exists) {
            $(".button_edit_2").addClass('gradation_type_5').removeClass('gradation_type_3').removeClass('gradation_type_1');
            $(".button_edit_2 input[type='button']").prop('disabled', true);
        }
        else {
            $(".button_edit_2:not(.gradation_type_1)").removeClass('gradation_type_5').removeClass('gradation_type_1').addClass('gradation_type_3');
            $(".button_edit_2 input[type='button']").prop('disabled', false);
        }
        editDispSwitching();
    }
    else {
        $(".button_edit").addClass('gradation_type_5').removeClass('gradation_type_3').removeClass('gradation_type_1');
        $(".button_edit input[type='button']").prop('disabled', true);
        $(".button_edit_2").addClass('gradation_type_5').removeClass('gradation_type_3').removeClass('gradation_type_1');
        $(".button_edit_2 input[type='button']").prop('disabled', true);
        $(".button_new").removeClass('gradation_type_1').addClass('gradation_type_3');
        if (selectBtn != 1 && selectBtn != 2 && selectBtn != 3) {
            changeDispListMode();
        }
    }
}

/**
 * LTE-Mの場合、タブ「現在値取得」押下後の画面にてボタン「表示更新」を制御
 *
 */
function controlDispUpdateBtn() {
    const btn = document.querySelector("[id$='s6DispUpdateBtn']");
    if (btn) {
      btn.disabled = !btn.disabled; // 反転
    }
}

/**
 * LTE-Mの場合、「設定内容変更」にて実行ボタンを制御
 *
 */
function controlExecBtn() {
    var targetSelector = ".s5ExecBtn";
    //  接続先がLTE-M以外の場合 もしくは 「機器に送信する」チェックボックスにチェック有り
    const isAnyChecked = !devId ||
        devId.slice(0, 2) !== 'LT' ||
//        devId.slice(0, 2) !== 'AQ' ||
        $(".sendFlgCheckBoxClass").is(":checked") ||
        $(".sendFlgLoadlimitCheckBoxClass").is(":checked");
    // チェックがあればボタン活性状態に変更
    setExecuteBtnDisabled(targetSelector, !isAnyChecked);
}

/**
 * 「設定内容変更」にて実行ボタンの活性非活性切替処理
 */
function setExecuteBtnDisabled(targetSelector, disabled) {
    var removeClass = "gradation_type_5";
    var addClass = "gradation_type_1";
    if (disabled) {
        removeClass = "gradation_type_1";
        addClass = "gradation_type_5";
    }
    $(targetSelector).addClass(addClass).removeClass(removeClass);
    $(targetSelector + " input[type='submit']").prop('disabled', disabled);
}

/**
 * 現在値取得画面の選択値をいったん退避する 現在値取得APIの引数用にhidden項目に選択値をセットする
 */
function getCurrentValueSelMeterMngId() {
    var selVal = $("#" + getSelectTab() + "6MeterMngIdSel").val();
    // 現在値取得APIの引数用にhidden項目に選択値をセットする
    var type = getSelectTab();
    if (type == 's' || type == 'p' || type == 'w') {
        $("." + type + "6MeterMngId input[type=hidden]").val(selVal);
    }
}

/**
 * 変更画面の入力項目部分の表示切替を行う
 */
function editDispSwitching() {
    const chk = $(".tbl input[type='checkbox']:checked").length;
    var type = getSelectTab();
    var obj = $('.editSreen');
    for (var i=0;i<obj.length;i++) {
        if (type == obj[i].id.substr(0, 1)) {
            if (chk != 1) {
                // 変更画面が表示されていて、一覧のチェックが1件以外の場合、入力エリアを非表示にする
                $(".editDisp").css("display", "none");
            } else {
                // 変更画面が表示されていて、一覧のチェックが1件の場合、入力エリアを表示する
                $(".editDisp").css("display", "");
            }
        }
    }
    if ($('.editSreen').css('display') == 'block') {
        if (chk != 1) {
            // 変更画面が表示されていて、一覧のチェックが1件以外の場合、入力エリアを非表示にする
            $(".editDisp").css("display", "none");
        } else {
            // 変更画面が表示されていて、一覧のチェックが1件の場合、入力エリアを表示する
            $(".editDisp").css("display", "");
        }
    }
}

/**
 * スマメ選択時、メーター状態登録の停止フラグON/OFFチェックを行う
 */
function smartCheckboxClick() {
  dispButton();
  if (selectBtn == 7) {
      showMeterStatusColumns("s");
  } else {
      hideMeterStatusColumns("s");
  }
  const chk = $(".tbl input[type='checkbox']:checked").length;
    if (chk > 0 && selectBtn == 7) {
        if ($("[name='smsCollectSettingMeterMeterManagementBean:smartMeterStatusForm:s7MeterPresentSituationSelect'] option:selected").val() == "0") {
            $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', true);
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        } else {
            $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', false);
            if ($('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('checked')) {
                $(".alertPause").prop('disabled', false);
                $(".rf-cal-btn").show();
            } else {
                $(".alertPause").prop('disabled', true);
                $(".rf-cal-btn").hide();
            }
        }
    }
}

/**
 * パルメ選択時、メーター状態登録の停止フラグON/OFFチェックを行う
 */
function pulseCheckboxClick() {
  dispButton();
  if (selectBtn == 7) {
      showMeterStatusColumns("p");
  } else {
      hideMeterStatusColumns("p");
  }
  const chk = $(".tbl input[type='checkbox']:checked").length;
    if (chk > 0 && selectBtn == 7) {
        if ($("[name='smsCollectSettingMeterMeterManagementBean:pulseMeterStatusForm:p7MeterPresentSituationSelect'] option:selected").val() == "0") {
            $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', true);
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        } else {
            $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', false);
            if ($('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('checked')) {
                $(".alertPause").prop('disabled', false);
                $(".rf-cal-btn").show();
            } else {
                $(".alertPause").prop('disabled', true);
                $(".rf-cal-btn").hide();
            }
        }
    }
}

/**
 * IoT-R連携用メーター選択時、メーター状態登録の停止フラグON/OFFチェックを行う
 */
function iotRCheckboxClick() {
  dispButton();
  if (selectBtn == 7) {
      showMeterStatusColumns("w");
  } else {
      hideMeterStatusColumns("w");
  }
  const chk = $(".tbl input[type='checkbox']:checked").length;
    if (chk > 0 && selectBtn == 7) {
        if ($("[name='smsCollectSettingMeterMeterManagementBean:iotRMeterStatusForm:w7MeterPresentSituationSelect'] option:selected").val() == "0") {
            $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', true);
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        } else {
            $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', false);
            if ($('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('checked')) {
                $(".alertPause").prop('disabled', false);
                $(".rf-cal-btn").show();
            } else {
                $(".alertPause").prop('disabled', true);
                $(".rf-cal-btn").hide();
            }
        }
    }
}

function moveColumnsForLteMDevice() {
    const devId = $(".selDev.sDevIdSel").val();

    // TODO 中村 最終LTに変更する。
//    if (!devId || devId.slice(0, 2) !== 'AQ') {
    if (!devId || devId.slice(0, 2) !== 'LT') {
        return; // 接続先が取得できない もしくは 設定した接続先ではない場合リターン
    }

    moveColumnsToLteMDeviceLayout();
}

/**
 * 接続先が「LTE-M」装置の場合、表形式を切り替える。
 */
function moveColumnsToLteMDeviceLayout() {

    // 接続先取得
    const devId = $(".selDev.sDevIdSel").val();

    // ヘッダー取得
    const $headerRow = $("#smsCollectSettingMeterMeterManagementBean\\:sTablePanel").find(".rf-edt-hdr table.rf-edt-tbl > tbody > tr").first();
    const $headerTds = $headerRow.find("td > div > table > tbody > tr").children("td");

    // 移動対象の列インデックス取得
    let idxOpen = -1, idxLoad = -1, idxInject = -1, idxOp = -1;
    $headerTds.each(function (i) {
      const text = $(this).text();
      if (text.includes("開閉")) idxOpen = i;
      if (text.includes("負荷電流")) idxLoad = i;
      if (text.includes("自動投入")) idxInject = i;
      if (text.includes("操作状態")) idxOp = i;
    });

    if (idxOpen < 0 || idxLoad < 0 || idxInject < 0 || idxOp < 0) return; // いずれかが見つからなければ中止

    // ヘッダーの処理
    const $hdOpen = $headerTds.eq(idxOpen).detach(); // カラム「開閉状態」削除
    const $hdLoad = $headerTds.eq(idxLoad).detach();
    const $hdInject = $headerTds.eq(idxInject).detach();
    const $hdOp = $headerTds.eq(idxOp).detach();
//    const $hdOp = $headerTds.eq(idxOp);

    // 当該カラムは削除になったためコメントアウト
//    $hdOp.after($hdInject).after($hdLoad); // カラム「操作状態」の後ろに移動

    // データ部の処理
    const $dataRow = $("#smsCollectSettingMeterMeterManagementBean\\:smartListFrom\\:resultMeterData\\:tbn");
    const $dataTrs = $dataRow.find("tr");
    $dataTrs.each(function () {
    const $td = $(this).children("td");

    const $dtOpen = $td.eq(idxOpen - 6).detach(); // カラム「開閉状態」削除
    const $dtLoad = $td.eq(idxLoad - 6).detach();
    const $dtInject = $td.eq(idxInject - 6).detach();
    const $dtOp = $td.eq(idxOp - 6).detach();
//    const $dtOp = $td.eq(idxOp - 6);

    // 当該カラムは削除になったためコメントアウト
//    $dtOp.after($dtInject).after($dtLoad); // カラム「操作状態」の後ろに移動
    });
}

function allCheckboxClick() {
    if (selectBtn == 7) {
        showMeterStatusColumns("s");
    } else {
        hideMeterStatusColumns("s");
    }
    dispButton();
    const chk = $(".tbl input[type='checkbox']:checked").length;
    if (chk > 0) {
        if (selectBtn == 4) {
            $(".btn_edit").removeClass('gradation_type_3').addClass('gradation_type_1');
        } else if (selectBtn == 5) {
            $(".btn_setting").removeClass('gradation_type_3').addClass('gradation_type_1');
        } else if (selectBtn == 6) {
            $(".btn_current").removeClass('gradation_type_3').addClass('gradation_type_1');
        } else if (selectBtn == 7) {
          // メーター状況が「通常」の場合、停止フラグと期間を非活性化させる
            if ($("[name='smsCollectSettingMeterMeterManagementBean:smartMeterStatusForm:s7MeterPresentSituationSelect'] option:selected").val() == "0") {
                $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', true);
                $(".alertPause").prop('disabled', true);
                $(".rf-cal-btn").hide();
            } else {
                $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', false);
                // アラート停止チェックボックスがONの場合、停止期間を活性化させる
                if ($('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('checked')) {
                    $(".alertPause").prop('disabled', false);
                    $(".rf-cal-btn").show();
                } else {
                    $(".alertPause").prop('disabled', true);
                    $(".rf-cal-btn").hide();
                }
            }
            $(".btn_status").removeClass('gradation_type_3').addClass('gradation_type_1');
        }
    } else {
        selectBtn = 1;
        $(".button_list").removeClass('gradation_type_3').addClass('gradation_type_1');
    }
    moveColumnsForLteMDevice();
    controlExecBtn(); // LTE-Mの場合の設定内容変更にて実行ボタン制御
}

/**
 * メータータイプタブ押下時の処理(画面切替（読み込み)前に動作する）
 *
 * @param event
 *            押下したタブの値
 */
function tabClick(event) {
    console.log("-*-*-*-*-*-*-*-*-*-* tabClick START *-*-*-*-*-*-*-*-*-*-");

    // 画面切り替える前に今の画面でチェックONになっているものを全部OFFにしてリセットしておく
    $("." + getSelectTab() + "Tbl input[type='checkbox']").prop('checked', false);

    // 押下したタブの値を取得
    newItem = event.rf.data.newItem;
    if (newItem.id.indexOf("tab1") != -1) {
        console.log("これはスマメ");
        // 再検索用隠し要素発動
        $("#smsCollectSettingMeterMeterManagementBean\\:meterKind").val("A"); // スマートを検索させる用項目
        // $(".meterKindHiddenBtn").click();
    } else if (newItem.id.indexOf("tab2") != -1) {
        console.log("これはパルメ");
        // 再検索用隠し要素発動
        $("#smsCollectSettingMeterMeterManagementBean\\:meterKind").val("P"); // パルスを検索させる用項目
        // $(".meterKindHiddenBtn").click();
    } else if (newItem.id.indexOf("tab3") != -1) {
        console.log("これはIoT-R");
        // 再検索用隠し要素発動
        $("#smsCollectSettingMeterMeterManagementBean\\:meterKind").val("TW"); // IoT-Rを検索させる用項目
        // $(".meterKindHiddenBtn").click();
    } else if (newItem.id.indexOf("tab4") != -1) {
        console.log("これはハンディ");
        $("#smsCollectSettingMeterMeterManagementBean\\:meterKind").val("MH"); // ハンディを検索させる用項目
    } else if (newItem.id.indexOf("tab5") != -1) {
        console.log("これはOCR");
        $("#smsCollectSettingMeterMeterManagementBean\\:meterKind").val("OC"); // OCRを検索させる用項目
    }
    selectBtn = 1;
    console.log("-*-*-*-*-*-*-*-*-*-* tabClick END *-*-*-*-*-*-*-*-*-*-");
}

/**
 * 一覧表示状態に切換
 *
 * @param kind メーター種別  "S":スマートメーター  "P":パルスメーター  "W":IoT-R連携用メーター  "H":ハンディ検針用メーター  "O":AieLink用メーター
 */
function changeDispListMode(kind) {
    processingSelectBtnClick(getSelectTab(), 1);
    selectBtn = 1;
    controlDelBtn(kind, false);
}

/**
 * 処理選択ボタン押下時の処理
 *
 * @param meterType
 *            メータータイプ・・・s:スマートメーター, p:パルスメーター, w:IoT-R連携用メーター, h:ハンディ検針用メーター, o:AieLink用メーター
 * @param procClass
 *            処理種別・・・1:一覧, 2:新規登録（個別）(AieLink用メーターの場合：API取得), 3:新規登録（一括）, 4:登録内容変更・削除, 5:設定内容変更,
 *            6:現在値取得, 7:メーター状態登録
 */
function processingSelectBtnClick(meterType, procClass) {
  console
      .log("-*-*-*-*-*-*-*-*-*-* processingSelectBtnClick START *-*-*-*-*-*-*-*-*-*-");

  // いったん全部非表示にする
  $("#s2").hide(); // [スマートメーター] 新規登録（個別）
  $("#s3").hide(); // [スマートメーター] 新規一括登録
  $("#s4").hide(); // [スマートメーター] 更新・削除
  $("#s5").hide(); // [スマートメーター] 設定変更
  $("#s6").hide(); // [スマートメーター] 現在値取得
  $("#s7").hide(); // [スマートメーター] メーター状態登録

  $("#p2").hide(); // [パルスメーター] 新規登録（個別）
  $("#p3").hide(); // [パルスメーター] 新規一括登録
  $("#p4").hide(); // [パルスメーター] 更新・削除
  $("#p5").hide(); // [パルスメーター] 設定変更
  $("#p6").hide(); // [パルスメーター] 現在値取得
  $("#p7").hide(); // [パルスメーター] メーター状態登録

  $("#w2").hide(); // [IoT-R連携用メーター] 新規登録（個別）
  $("#w3").hide(); // [IoT-R連携用メーター] 新規一括登録
  $("#w4").hide(); // [IoT-R連携用メーター] 更新・削除
  $("#w7").hide(); // [IoT-R連携用メーター] メーター状態登録

  $("#h2").hide(); // [ハンディー検針用メーター] 新規登録（個別）
  $("#h3").hide(); // [ハンディー検針用メーター] 新規一括登録
  $("#h4").hide(); // [ハンディー検針用メーター] 更新・削除

  $("#o2").hide(); // [AieLink用メーター] API取得
  $("#o3").hide(); // [AieLink用メーター] 新規一括登録・更新
  $("#o4").hide(); // [AieLink用メーター] 更新・削除

  // 押下したボタンで表示するパーツを判断する
  $("#" + meterType + procClass).show();

  // 変更画面に遷移するとき、一覧のチェックがいくつあるかによって表示/非表示を切り替える必要がある(全メータータイプ同じ挙動)
  if (procClass == 4) {
    editDispSwitching();
  }
  // 現在値取得画面に遷移するとき、現在値を保持するプロパティのクリア処理を行う
  if (procClass == 6) {
    // 処理を呼ぶための非表示ボタンをクリックする
    $('.nowValResetBtn').click();
  }

  // TODO: kimura それぞれ初期表示時点で処理したいことはここでやる予定
  if (meterType == "s") {
    console.log("スマメ");
    switch (procClass) {
    case 1:
      console.log("一覧");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 2:
      console.log("新規登録（個別）");
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=s2MeterMngIdSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 3:
      console.log("一括登録");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 4:
      console.log("編集・削除");
      // 「ユーザーコード」にフォーカスを当てる
      $('input[id$=s4UserCode]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 5:
      console.log("設定変更");
      // 「負荷制限」にフォーカスを当てる
      $('select[id$=s5LoadlimitSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 6:
      console.log("現在値取得");
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=s6MeterMngIdSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 7:
      console.log("メーター状態登録");
      // 「メーター状況」にフォーカスを当てる
      $('select[id$=s7MeterPresentSituationSelect]').focus();
      // メーター状況が「通常」の場合、停止フラグと期間を非活性化させる
      if ($("[name='smsCollectSettingMeterMeterManagementBean:smartMeterStatusForm:s7MeterPresentSituationSelect'] option:selected").val() == "0") {
          $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', true);
          $(".alertPause").prop('disabled', true);
          $(".rf-cal-btn").hide();
      } else {
          $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', false);
          // アラート停止チェックボックスがONの場合、停止期間を活性化させる
          if ($('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('checked')) {
              $(".alertPause").prop('disabled', false);
              $(".rf-cal-btn").show();
          } else {
              $(".alertPause").prop('disabled', true);
              $(".rf-cal-btn").hide();
          }
      }
      //メーター状態登録項目を表示する
      showMeterStatusColumns(meterType);
      break;
    default:
      break;
    }
  } else if (meterType == "p") {
    console.log("パルメ");
    switch (procClass) {
    case 1:
      console.log("一覧");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 2:
      console.log("新規登録（個別）");
      // 該当チェックボックスの状態によって活性/非活性を切り替える系項目は初期表示一律非活性
      $('.disabledControl').prop('disabled', true);
      // ↑のチェックボックスは初期表示一律OFF
      $('.disabledControlCheck').prop('checked', false);
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=p2MeterMngIdSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 3:
      console.log("一括登録");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 4:
      console.log("編集・削除");
      // 「ユーザーコード」にフォーカスを当てる
      $('input[id$=p4UserCode]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 5:
      console.log("設定変更");
      // 該当チェックボックスの状態によって活性/非活性を切り替える系項目は初期表示一律非活性
      $('.disabledControl').prop('disabled', true);
      // ↑のチェックボックスは初期表示一律OFF
      $('.disabledControlCheck').prop('checked', false);
      if (!$('input[id$=p5PulseWeight]')[0].disabled) {
          // 「パルス重み」にフォーカスを当てる
          $('input[id$=p5PulseWeight]').focus();
      } else {
          // 「乗率」にフォーカスを当てる
          $('input[id$=p5Multi]').focus();
      }
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 6:
      console.log("現在値取得");
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=p6MeterMngIdSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 7:
      console.log("メーター状態登録");
      // 「メーター状況」にフォーカスを当てる
      $('select[id$=p7MeterPresentSituationSelect]').focus();
      // メーター状況が「通常」の場合、停止フラグと期間を非活性化させる
      if ($("[name='smsCollectSettingMeterMeterManagementBean:pulseMeterStatusForm:p7MeterPresentSituationSelect'] option:selected").val() == "0") {
          $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', true);
          $(".alertPause").prop('disabled', true);
          $(".rf-cal-btn").hide();
      } else {
          $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', false);
          // アラート停止チェックボックスがONの場合、停止期間を活性化させる
          if ($('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('checked')) {
              $(".alertPause").prop('disabled', false);
              $(".rf-cal-btn").show();
          } else {
              $(".alertPause").prop('disabled', true);
              $(".rf-cal-btn").hide();
          }
      }
      //メーター状態登録項目を表示する
      showMeterStatusColumns(meterType);
      break;
    default:
      break;
    }
  } else if (meterType == "w") {
    console.log("IoT-R");
    switch (procClass) {
    case 1:
      console.log("一覧");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 2:
      console.log("新規登録（個別）");
      // 該当チェックボックスの状態によって活性/非活性を切り替える系項目は初期表示一律非活性
      $('.disabledControl').prop('disabled', true);
      // ↑のチェックボックスは初期表示一律OFF
      $('.disabledControlCheck').prop('checked', false);
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=w2MeterMngIdSelect]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 3:
      console.log("一括登録");
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 4:
      console.log("編集・削除");
      // 「ユーザーコード」にフォーカスを当てる
      $('input[id$=w4UserCode]').focus();
      //メーター状態登録項目を非表示にする
      hideMeterStatusColumns(meterType);
      break;
    case 7:
        console.log("メーター状態登録");
        // 「メーター状況」にフォーカスを当てる
        $('select[id$=w7MeterPresentSituationSelect]').focus();
        // メーター状況が「通常」の場合、停止フラグと期間を非活性化させる
        if ($("[name='smsCollectSettingMeterMeterManagementBean:iotRMeterStatusForm:w7MeterPresentSituationSelect'] option:selected").val() == "0") {
            $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', true);
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        } else {
            $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', false);
            // アラート停止チェックボックスがONの場合、停止期間を活性化させる
            if ($('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('checked')) {
                $(".alertPause").prop('disabled', false);
                $(".rf-cal-btn").show();
            } else {
                $(".alertPause").prop('disabled', true);
                $(".rf-cal-btn").hide();
            }
        }
        //メーター状態登録項目を表示する
        showMeterStatusColumns(meterType);
        break;
    default:
      break;
    }
  } else if (meterType == "h") {
    console.log("ハンディ");
    switch (procClass) {
    case 1:
      console.log("一覧");
      break;
    case 2:
      console.log("新規登録（個別）");
      // 「メーター管理番号」にフォーカスを当てる
      $('select[id$=h2MeterMngIdSelect]').focus();
      break;
    case 3:
      console.log("一括登録");
      break;
    case 4:
      console.log("編集・削除");
      // 「ユーザーコード」にフォーカスを当てる
      $('input[id$=h4UserCode]').focus();
      break;
    default:
      break;
    }
  } else if (meterType == "o") {
    console.log("OCR");
    switch (procClass) {
    case 1:
      console.log("一覧");
      break;
    case 2:
        console.log("API取得");
        break;
    case 3:
      console.log("一括登録・更新");
      break;
    case 4: // 編集・削除
      console.log("編集・削除");
      // 「ユーザーコード」にフォーカスを当てる
      $('input[id$=o4UserCode]').focus();
      break;
    default:
      break;
  }
  }
  console
      .log("-*-*-*-*-*-*-*-*-*-* processingSelectBtnClick END *-*-*-*-*-*-*-*-*-*-");
}

/**
 * 現在どのメーター種別の処理をしているか取得できる
 */
function getSelectTab() {
  var tab = $(
      "#smsCollectSettingMeterMeterManagementBean\\:meterKind")
      .val();
  if (tab == 'A' || tab == '') { // 初期表示時は何もセットされていない状態になっている
    return 's';
  } else if (tab == 'P') {
    return 'p';
  } else if (tab == 'TW') {
    return 'w';
  } else if (tab == 'MH') {
    return 'h';
  } else { // if (tab == 'OC') {
    return 'o';
  }
}

/**
 * パルスのチェックボックスの状態によって活性/非活性を切り替える処理
 *
 * @param {}
 *            val
 * @param {*}
 *            targetClass
 */
function disabledControl(val, targetClass) {
  if (val.checked) {
    $('.' + targetClass).prop('disabled', false);
  } else {
    $('.' + targetClass).prop('disabled', true);
  }
}

function allCheckBoxDisabled(){
    let targets = document.querySelectorAll(".tbl input[type='checkbox']");
    for (const i of targets) {
      i.checked = false;
    }
}

function backRegistVal(val) {
    if (!val.checked) {
        var id = val.id.replace("Chk", "");
        document.getElementById(id).value = document.getElementById(id + "Hidden").value;
    }
}

$(function() {
  $('.alertPause').prop('disabled', true);
});

/**
 * メーター登録状態 メーター状況の選択肢による活性/非活性切替え（スマート）
 */
function checkSmartMeterPresSitu() {
    if ($("[name='smsCollectSettingMeterMeterManagementBean:smartMeterStatusForm:s7MeterPresentSituationSelect'] option:selected").val() == "0") {
        $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', true);
        $(".alertPause").prop('disabled', true);
        $(".rf-cal-btn").hide();
    } else {
        $('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('disabled', false);
        if ($('input:checkbox[id$=s7AlertPauseFlgCheckbox]').prop('checked')) {
            $(".alertPause").prop('disabled', false);
            $(".rf-cal-btn").show();
        } else {
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        }
    }
}

/**
 * メーター登録状態 メーター状況の選択肢による活性/非活性切替え（パルス）
 */
function checkPulseMeterPresSitu() {
    if ($("[name='smsCollectSettingMeterMeterManagementBean:pulseMeterStatusForm:p7MeterPresentSituationSelect'] option:selected").val() == "0") {
        $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', true);
        $(".alertPause").prop('disabled', true);
        $(".rf-cal-btn").hide();
    } else {
        $('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('disabled', false);
        if ($('input:checkbox[id$=p7AlertPauseFlgCheckbox]').prop('checked')) {
            $(".alertPause").prop('disabled', false);
            $(".rf-cal-btn").show();
        } else {
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        }
    }
}

/**
 * メーター登録状態 メーター状況の選択肢による活性/非活性切替え（IoT-R）
 */
function checkIotRMeterPresSitu() {
    if ($("[name='smsCollectSettingMeterMeterManagementBean:iotRMeterStatusForm:w7MeterPresentSituationSelect'] option:selected").val() == "0") {
        $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', true);
        $(".alertPause").prop('disabled', true);
        $(".rf-cal-btn").hide();
    } else {
        $('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('disabled', false);
        if ($('input:checkbox[id$=w7AlertPauseFlgCheckbox]').prop('checked')) {
            $(".alertPause").prop('disabled', false);
            $(".rf-cal-btn").show();
        } else {
            $(".alertPause").prop('disabled', true);
            $(".rf-cal-btn").hide();
        }
    }
}

/**
 * アラート停止期間テキストボックスの活性/非活性切替え
 * @param val アラート停止チェックボックス
 */
function disabledAlertPause(val) {
  if (val.checked) {
    $(".alertPause").prop('disabled', false);
    $(".rf-cal-btn").show();
  } else {
    $(".alertPause").prop('disabled', true);
    $(".rf-cal-btn").hide();
  }
}

/**
 * 登録、変更処理後一覧表示に遷移
 *
 * @param kind メーター種別  "S":スマートメーター  "P":パルスメーター  "W":IoT-R連携用メーター  "H":ハンディ検針用メーター  "O":AieLink用メーター
 */
function viewList(kind) {
    var errFlg = false;
    var tags = $(".rf-msgs-err");

    // 接続先取得
    const devId = $(".selDev.sDevIdSel").val();

    try {
      for (var i = 0; i < tags.size(); i++) {
          if (tags[i].id.split(":")[1].substring(0, 1) == kind.toLowerCase()) {
              errFlg = true;
              break;
          }
      }

      var isSearch = "1";
      if (errFlg) {
          // 登録・更新 エラーあり
          isSearch = "0";
      } else {
          // 登録・更新 正常終了 → 一覧再表示
          $('input[id$=btn' + kind + '1List]').click();
      }

      setErrMsgArea(isSearch);
      controlDelBtn(kind, false);
    } catch(e) { console.log(e); }
}

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

/**
 * 削除ボタンを制御
 *
 * @param kind メーター種別  "S":スマートメーター  "P":パルスメーター  "W":IoT-R連携用メーター  "H":ハンディ検針用メーター  "O":AieLink用メーター
 * @param isEnable 活性フラグ
 */
function controlDelBtn(kind, isEnable) {
    var targetSelector = ".s4DeleteBtn";
    if (kind == "P") {
        targetSelector = ".p4DeleteBtn";
    } else if (kind == "W") {
        targetSelector = ".w4DeleteBtn";
    } else if (kind == "H") {
        targetSelector = ".h4DeleteBtn";
    } else if (kind == "O") {
        targetSelector = ".o4DeleteBtn";
    }

    // 「削除ボタンを有効にする」チェックボックス
    $(".delCheckBoxClass").prop('checked', isEnable);
    // 削除ボタン
    setDeleteBtnDisabled(targetSelector, !isEnable);
}

/**
 * 一覧表のメーター状態登録項目を非表示にする
 *
 * @param meterType
 */
function hideMeterStatusColumns(meterType) {
  if (meterType == "s") {
      $(".rf-edt-td-sMeterPreSitu").hide();
      $(".rf-edt-td-sAlertPauseFlg").hide();
      $(".rf-edt-td-sAlertPause").hide();
      $(".rf-edt-td-sMeterStatusMemo").hide();
  } else if (meterType == "p") {
      $(".rf-edt-td-pMeterPreSitu").hide();
      $(".rf-edt-td-pAlertPauseFlg").hide();
      $(".rf-edt-td-pAlertPause").hide();
      $(".rf-edt-td-pMeterStatusMemo").hide();
  } else if (meterType == "w") {
      $(".rf-edt-td-wMeterPreSitu").hide();
      $(".rf-edt-td-wAlertPauseFlg").hide();
      $(".rf-edt-td-wAlertPause").hide();
      $(".rf-edt-td-wMeterStatusMemo").hide();
  } else if (meterType == "all") {
      $(".rf-edt-td-sMeterPreSitu").hide();
      $(".rf-edt-td-sAlertPauseFlg").hide();
      $(".rf-edt-td-sAlertPause").hide();
      $(".rf-edt-td-sMeterStatusMemo").hide();
      $(".rf-edt-td-pMeterPreSitu").hide();
      $(".rf-edt-td-pAlertPauseFlg").hide();
      $(".rf-edt-td-pAlertPause").hide();
      $(".rf-edt-td-pMeterStatusMemo").hide();
      $(".rf-edt-td-wMeterPreSitu").hide();
      $(".rf-edt-td-wAlertPauseFlg").hide();
      $(".rf-edt-td-wAlertPause").hide();
      $(".rf-edt-td-wMeterStatusMemo").hide();
  }
}

/**
 * 一覧表のメーター状態登録項目を表示する
 *
 * @param meterType
 */
function showMeterStatusColumns(meterType) {
  if (meterType == "s") {
      $(".rf-edt-td-sMeterPreSitu").show();
      $(".rf-edt-td-sAlertPauseFlg").show();
      $(".rf-edt-td-sAlertPause").show();
      $(".rf-edt-td-sMeterStatusMemo").show();
  } else if (meterType == "p") {
      $(".rf-edt-td-pMeterPreSitu").show();
      $(".rf-edt-td-pAlertPauseFlg").show();
      $(".rf-edt-td-pAlertPause").show();
      $(".rf-edt-td-pMeterStatusMemo").show();
  } else if (meterType == "w") {
      $(".rf-edt-td-wMeterPreSitu").show();
      $(".rf-edt-td-wAlertPauseFlg").show();
      $(".rf-edt-td-wAlertPause").show();
      $(".rf-edt-td-wMeterStatusMemo").show();
  }
}
