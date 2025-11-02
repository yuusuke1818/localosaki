var isDailyContentsShow;

window.onload = function() {
    setInitAccordion();
    var firstMeterMngId = $("#firstMeterMngId").val();
    if (firstMeterMngId != null) {
        createGraph(firstMeterMngId);
    }
};

function setInitAccordion() {
    $(".contents_accordion_header").parent().children(
            ".contents_accordion_body").show();
    $(".contents_accordion_header").find(".button_opener").hide();
    $(".contents_accordion_header").find(".button_closer").show();
}

$(document).on("click", ".contents_accordion_header", function() {
    $(this).parent().children(".contents_accordion_body").slideToggle();
    $(this).children(".button_icon").toggle();
});

function onBeginCalendar() {
    showBlockModal();
}

function onCompleteCalendar() {
    createGraph($("#firstMeterMngId").val());
    hideBlockModal(true);
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
    var targetDate = new Date();
    targetDate.setFullYear(targetDateObj.date.getFullYear());
    targetDate.setMonth(targetDateObj.date.getMonth());
    targetDate.setDate(targetDateObj.date.getDate());
    targetDate.setHours(0);
    targetDate.setMinutes(0);
    targetDate.setSeconds(0);
    targetDate.setMilliseconds(0);

    var selectableLimitDate = new Date($("#selectableLimitDate").val());

    if (targetDate.getTime() > selectableLimitDate.getTime()) {
        // 対象日付が選択可能限界最新日付を超えている場合
        return false;
    }

    return true;
}

/**
 * グラフ生成.
 *
 * @param valueArray 表示値配列
 */
function createGraph(meterMngId) {
    $("#graph-mng-no").text(meterMngId);
    $("#graph-meter-id").text($("#graphTargetMeterId" + meterMngId).val());
    $("#graph-tenant-name").text($("#graphTargetTenant" + meterMngId).val());
    $("#graph-meter-kind").text($("#graphTargetMeterType" + meterMngId).val());

    $("#graphSpace").html('<canvas id="chart1" width="975" height="450"></canvas>');

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
            datasets : [
                {
                  label : $("#titleCompare").val() + '1',
                  data : $("#graphValues1_" + meterMngId).val().replace("[", "").replace("]", "").split(","),
                  backgroundColor : '#2dc12d',
                  borderWidth : 1
                },
                {
                  label : $("#titleCompare").val() + '2',
                  data : $("#graphValues2_" + meterMngId).val().replace("[", "").replace("]", "").split(","),
                  backgroundColor : '#ffcf0f',
                  borderWidth : 1
                },
            ]
        },
        options : {
            responsive: false,
            legend : {
            position: 'bottom'
            },
            scales : {
                yAxes: [{                           // y軸設定
                    display: true,                 // 表示設定
                    scaleLabel: {                  // 軸ラベル設定
                        display: true,            // 表示設定
                        labelString: "使用量" + $("#graphUnit" + meterMngId).val(),  // ラベル
                    },
                    ticks: {                      // 最大値最小値設定
                        min: 0,                   // 最小値
                    },
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