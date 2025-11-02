var isDailyContentsShow;
var selectMeterIdOrMeterMngNo;

window.onload = function() {
    setInitAccordion();
    onExDataTableComplete();

    // 装置名がある場合は全装置用グラフ
    var devName = $(".meterId").text();
    if(devName != ""){
        createGraphAllDevice($("#firstMeterId").val());
    } else{
        createGraph($("#firstMeterMngId").val());
    }
    // スクロール位置を保存＆復元用
    if (sessionStorage.getItem("scrollX") != "") {
        sessionStorage.clear();
    }
};

function setInitAccordion() {
    $(".contents_accordion_header").parent().children(
            ".contents_accordion_body").show();
    $(".contents_accordion_header").find(".button_opener").hide();
    $(".contents_accordion_header").find(".button_closer").show();
}

function onExDataTableComplete() {
    // ヘッダをマウスドラッグすると、列移動されるイベントを削除
    $(".rf-edt-hdr-c").unbind("mousedown");
}

$(document).on("click", ".contents_accordion_header", function() {
    $(this).parent().children(".contents_accordion_body").slideToggle();
    $(this).children(".button_icon").toggle();
});

$(document).on("click", ".sw_table", function() {
    var swGraphImgObj = $(".sw_graph img");
    if (swGraphImgObj[0]) {
        $(".contents_table").show();
        $(".contents_graph").hide();
    }
    document.getElementById("graphKind").value = "0";
});

$(document).on("click", ".sw_graph", function() {
    var swGraphImgObj = $(".sw_graph img");
    if (swGraphImgObj[0]) {
        $(".contents_table").hide();
        $(".contents_graph").show();
    }
    document.getElementById("graphKind").value = "1";
});

/**
 * 処理選択ボタン押下時
 */
$(document).on('click', ".buttons input[type='button']", function() {
    // いったん全部水色にする
    $(".buttons input[type='button']").parent().addClass('gradation_type_3').removeClass('gradation_type_1');
    // 今押されたボタンだけ青色にする
    $(this).parent().addClass('gradation_type_1').removeClass('gradation_type_3');

})

$(document).on("click", ".contents_graph .graph_header .items", function() {
    $(".items").removeClass("selected");
    $(this).addClass("selected");
    // 装置名がある場合は全装置用グラフ関数
    var devName = $(".meterId").text();
    if(devName != ""){
      var selectMeterId = $(this).find(".meterId").text().trim();
      createGraphAllDevice(selectMeterId);
    } else{
      var selectMeterMngId = $(this).find(".no").text();
      createGraph(selectMeterMngId);
    }
});

/**
 * グラフボタン押下時
 */
function onClickGraphButton() {
      // 前回のスクロール位置を復元
    var scrollContainer = document.getElementById("scrollContainer");
    if (sessionStorage.getItem("scrollX") != "" && sessionStorage.getItem("scrollX") != null) {
        scrollContainer.scrollLeft = Number(sessionStorage.getItem("scrollX"));
    }
}

function onBeginCalendarDaily() {
    // グラフの計器ID or 管理番号選択時はそのまま保持（接続先にて全て選択時は計器ID、一部選択時は管理番号取得）
    selectMeterIdOrMeterMngNo = $(".contents_graph .graph_header .items.selected").text();
    if(selectMeterIdOrMeterMngNo != ""){
        selectMeterIdOrMeterMngNo = $(".contents_graph .graph_header .items.selected").attr("id").replace("graphSelect","").trim();
    }

    // スクロール位置を保存＆復元用
    var scrollContainer = document.getElementById("scrollContainer");
    if (scrollContainer.scrollLeft !== 0) {
        sessionStorage.setItem("scrollX", scrollContainer.scrollLeft);
    }

    getDailyContentsStatus();
    showBlockModal();
}

function onCompleteCalendarDaily() {
    // 装置名がある場合は全装置用グラフ関数
    var devName = $(".meterId").text();
    if(devName != ""){
        if(selectMeterIdOrMeterMngNo != ""){
            // グラフの計器ID or 管理番号選択時はそのまま保持（接続先にて全て選択時は計器ID、一部選択時は管理番号を保持）
            createGraphAllDevice(selectMeterIdOrMeterMngNo);
        } else {
            createGraphAllDevice($("#firstMeterId").val());
        }
    } else{
        if(selectMeterIdOrMeterMngNo != ""){
            // グラフの計器ID or 管理番号選択時はそのまま保持（接続先にて全て選択時は計器ID、一部選択時は管理番号を保持）
            createGraph(selectMeterIdOrMeterMngNo);
        } else {
            createGraph($("#firstMeterMngId").val());
        }
    }
    setDailyContents();

    // スクロール位置を保存＆復元用
    var scrollContainer = document.getElementById("scrollContainer");
    // 前回のスクロール位置を復元
    scrollContainer.scrollLeft = Number(sessionStorage.getItem("scrollX"));

    // スクロール位置を保存
    scrollContainer.addEventListener("scroll", function () {
        sessionStorage.setItem("scrollX", scrollContainer.scrollLeft);
    });

    hideBlockModal(true);
}

// 日報のアコーディオン制御
function getDailyContentsStatus() {
    isDailyContentsShow = $(".contents_daily").parent().children(
            ".contents_accordion_body").is(":visible");
}

function setDailyContents() {
    var contentsObj = $(".contents_title").parent();
    var contentsAccordionBodyObj = contentsObj
            .children(".contents_accordion_body");
    var contentsButtonOpenerObj = contentsObj.find(".button_opener");
    var contentsButtonCloserObj = contentsObj.find(".button_closer");

    if (isDailyContentsShow) {
        contentsAccordionBodyObj.show();
        contentsButtonOpenerObj.hide();
        contentsButtonCloserObj.show();
    } else {
        contentsAccordionBodyObj.hide();
        contentsButtonOpenerObj.show();
        contentsButtonCloserObj.hide();
    }

    // 表示切替
    var swGraphImgObj = $(".sw_graph img");
    if (swGraphImgObj[0]) {
        var _graphKind = document.getElementById("graphKind").value;
        if (_graphKind == "0") {
            $(".contents_table").show();
            $(".contents_graph").hide();
            $(".sw_table").addClass('gradation_type_1').removeClass('gradation_type_3');
            $(".sw_graph").addClass('gradation_type_3').removeClass('gradation_type_1');
        } else {
            $(".contents_table").hide();
            $(".contents_graph").show();
            $(".sw_table").addClass('gradation_type_3').removeClass('gradation_type_1');
            $(".sw_graph").addClass('gradation_type_1').removeClass('gradation_type_3');
        }
    }
}

/**
 * カレンダー表示部のCSSクラスを設定
 *
 * @param index インデックス
 * @param selected 選択値
 */
function changeCalendarClass(index, selected) {
    if (index == selected) {
        $("#td_calendar_day" + index).addClass("td_targetday");
    }
}

/**
 * カレンダー日付選択のスタイルを設定
 *
 * @param targetDateObj 対象日付オブジェクト
 * @return {undefined}
 */
function setCalendarDisableStyle(targetDateObj) {
    if (isSelectableDate(targetDateObj)) {
        return "";
    }

    return "caldisable";
}

/**
 * カレンダー日付選択要否を判断
 *
 * @param targetDateObj 対象日付オブジェクト
 * @return true:選択可能
 */
function isSelectableDate(targetDateObj) {
    // TODO 当面制御未実施
//    var targetDate = new Date();
//    targetDate.setFullYear(targetDateObj.date.getFullYear());
//    targetDate.setMonth(targetDateObj.date.getMonth());
//    targetDate.setDate(targetDateObj.date.getDate());
//    targetDate.setHours(0);
//    targetDate.setMinutes(0);
//    targetDate.setSeconds(0);
//    targetDate.setMilliseconds(0);
//
//    var selectableLimitDate = new Date($("#selectableLimitDate").val());
//
//    if (targetDate.getTime() > selectableLimitDate.getTime()) {
//        // 対象日付が選択可能限界最新日付を超えている場合
//        return false;
//    }

    return true;
}

/**
 * グラフ生成.(全装置用).
 *
 * @param valueArray 表示値配列
 */
function createGraphAllDevice(meterId) {
    if (!$("#graphSpace")[0]) {
        return;
    }

    var selectedClass = "selected";
    var targetGraphHeader = $("#graphSelect" + meterId);
    if (!targetGraphHeader.hasClass(selectedClass)) {
        targetGraphHeader.addClass(selectedClass);
    }

    $("#graph-mng-no").text($("#graphTargetMeterMngId" + meterId).val());
    $("#graph-meter-id").text($("#graphTargetMeterId" + meterId).val());
    $("#graph-dev-name").text($("#graphTargetDevName" + meterId).val());
    $("#graph-tenant-name").text($("#graphTargetTenant" + meterId).val());
    $("#graph-meter-kind").text($("#graphTargetMeterType" + meterId).val());
    $("#graph-this-date").text($("#graphMaxDemandPeriod" + meterId).val());
    $("#graph-last-year").text($("#graphMaxDemandLastYear" + meterId).val());
    $("#graph-past").text($("#graphMaxDemandPast" + meterId).val());


    $("#graphSpace").html('<canvas id="chart1" width="850" height="400"></canvas>');

    var ctx = $("#chart1").get(0).getContext("2d");
    var myChart = null;
    if (myChart) {
        myChart.destroy();
        myChart = null;
    }
    myChart = new Chart(ctx, {
        type : 'bar',
        data : {
            labels : $("#targetTimes").val().replace("[", "").replace("]", "").split(","),
            datasets : [ {
                label : '最大デマンド',
                lineTension: 0,
                data : $("#graphMaxDemandValues" + meterId).val().replace("[", "").replace("]", "").split(","),
                type: "line",
                fill : false,
                backgroundColor : '#f37167',
                borderColor :  '#f37167',
                borderWidth : 2,
                yAxisID: "y-axis-right",
            }, {
                label : '使用量',
                data : $("#graphValues" + meterId).val().replace("[", "").replace("]", "").split(","),
                backgroundColor : '#2dc12d',
                borderWidth : 1,
                yAxisID: "y-axis-left",
            }]
        },
        options : {
            responsive: false,
            legend : {
              position: 'bottom',
              onClick: function () { return false; }
            },
            scales : {
                yAxes: [{                           // y軸設定
                    id: "y-axis-left",
                  position: "left",
                    display: true,                 // 表示設定
                    scaleLabel: {                  // 軸ラベル設定
                        display: true,            // 表示設定
                        labelString: "使用量" + $("#graphUnit" + meterId).val(),  // ラベル
                    },
                    ticks: {                      // 最大値最小値設定
                      beginAtZero: true                   // 最小値
                    },
                },{
                    id: "y-axis-right",
                    type: "linear",
                    position: "right",
                    scaleLabel: {                  // 軸ラベル設定
                        display: true,            // 表示設定
                        labelString: "最大デマンドkW",  // ラベル
                    },
                    gridLines: {
                        drawOnChartArea: false,
                    },
                    ticks: {
                      beginAtZero: true
                    }
                }],
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        maxRotation: 90,          // 自動的に回転する角度を固定
                        minRotation: 90,
                    }
                }],
            }
        }
    });
}

/**
 * グラフ生成(装置個別用)
 *
 * @param valueArray 表示値配列
 */
function createGraph(meterMngId) {
    if (!$("#graphSpace")[0]) {
        return;
    }

    var selectedClass = "selected";
    var targetGraphHeader = $("#graphSelect" + meterMngId);
    if (!targetGraphHeader.hasClass(selectedClass)) {
        targetGraphHeader.addClass(selectedClass);
    }

    $("#graph-mng-no").text(meterMngId);
    $("#graph-meter-id").text($("#graphTargetMeterId" + meterMngId).val());
    $("#graph-tenant-name").text($("#graphTargetTenant" + meterMngId).val());
    $("#graph-meter-kind").text($("#graphTargetMeterType" + meterMngId).val());
    $("#graph-this-date").text($("#graphMaxDemandPeriod" + meterMngId).val());
    $("#graph-last-year").text($("#graphMaxDemandLastYear" + meterMngId).val());
    $("#graph-past").text($("#graphMaxDemandPast" + meterMngId).val());


    $("#graphSpace").html('<canvas id="chart1" width="850" height="400"></canvas>');

    var ctx = $("#chart1").get(0).getContext("2d");
    var myChart = null;
    if (myChart) {
        myChart.destroy();
        myChart = null;
    }
    myChart = new Chart(ctx, {
        type : 'bar',
        data : {
            labels : $("#targetTimes").val().replace("[", "").replace("]", "").split(","),
            datasets : [ {
                label : '最大デマンド',
                lineTension: 0,
                data : $("#graphMaxDemandValues" + meterMngId).val().replace("[", "").replace("]", "").split(","),
                type: "line",
                fill : false,
                backgroundColor : '#f37167',
                borderColor :  '#f37167',
                borderWidth : 2,
                yAxisID: "y-axis-right",
            }, {
                label : '使用量',
                data : $("#graphValues" + meterMngId).val().replace("[", "").replace("]", "").split(","),
                backgroundColor : '#2dc12d',
                borderWidth : 1,
                yAxisID: "y-axis-left",
            }]
        },
        options : {
            responsive: false,
            legend : {
              position: 'bottom',
              onClick: function () { return false; }
            },
            scales : {
                yAxes: [{                           // y軸設定
                    id: "y-axis-left",
                  position: "left",
                    display: true,                 // 表示設定
                    scaleLabel: {                  // 軸ラベル設定
                        display: true,            // 表示設定
                        labelString: "使用量" + $("#graphUnit" + meterMngId).val(),  // ラベル
                    },
                    ticks: {                      // 最大値最小値設定
                      beginAtZero: true                   // 最小値
                    },
                },{
                    id: "y-axis-right",
                    type: "linear",
                    position: "right",
                    scaleLabel: {                  // 軸ラベル設定
                        display: true,            // 表示設定
                        labelString: "最大デマンドkW",  // ラベル
                    },
                    gridLines: {
                        drawOnChartArea: false,
                    },
                    ticks: {
                      beginAtZero: true
                    }
                }],
                xAxes: [{
                    display: true,
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        maxRotation: 90,          // 自動的に回転する角度を固定
                        minRotation: 90,
                    }
                }],
            }
        }
    });
}