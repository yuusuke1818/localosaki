var meterReadingFlg = true;
var searchOpenFlg = false;
var printOpenFlg = false;

window.onload = function() {
    setInitAccordion();
    btnMeterReadingData();
    meterReadingFlg = true;
    scrollEvent();
};

function setInitAccordion() {
    $(".contents_accordion_header").parent().children(".contents_accordion_body").show();
    $(".contents_accordion_header").find(".button_opener").hide();
    $(".contents_accordion_header").find(".button_closer").show();
}

$(document).on("click", ".contents_accordion_header", function() {
    $(this).parent().children(".contents_accordion_body").slideToggle();
    $(".contents_accordion_header").find(".button_opener").toggle();
    $(".contents_accordion_header").find(".button_closer").toggle();
});

$(document).on("click", ".contents_accordion_printing_header", function() {
    $(this).parent().children(".contents_accordion_printing_body").slideToggle();
    $(".contents_accordion_printing_header").find(".button_opener").toggle();
    $(".contents_accordion_printing_header").find(".button_closer").toggle();
});

$(document).on("click", ".button_reload", function() {
    if (sessionStorage.scrollLeft != "undefined") {
        $(".table_daily_comp").scrollLeft(sessionStorage.scrollLeft);
    }
});

function scrollEvent() {
    var value = sessionStorage.getItem('scrollLeftValue');
    if (value != null) {
      $(".rf-edt-scrl").scrollLeft(value);
    }
    $(".rf-edt-scrl").scroll(function(){
      console.log($(".rf-edt-scrl").scrollLeft());
      sessionStorage.setItem('scrollLeftValue', $(".rf-edt-scrl").scrollLeft());
    });
}

function btnMeterReadingData() {
    meterReadingFlg = true;
    $(".contents_accordion_printing_header").parent().children(".contents_accordion_printing_body").hide();
    $(".contents_accordion_printing_header").hide();
    $(".printing").hide();
};

function btnBillingAmountData() {
    meterReadingFlg = false;
    $(".printing").show();
    setInitBillingAmountAccordion();
};

function setInitBillingAmountAccordion() {
    if (meterReadingFlg) {
        $(".printing").hide();
    } else {
        $(".printing").show();
    }
    $(".contents_accordion_printing_header").parent().children(".contents_accordion_printing_body").hide();
    $(".contents_accordion_printing_header").find(".button_opener").show();
    $(".contents_accordion_printing_header").find(".button_closer").hide();
}

function searchAreaVisible() {
    if (searchOpenFlg) {
        $(".contents_accordion_header").parent().children(".contents_accordion_body").show();
        $(".contents_accordion_header").find(".button_opener").hide();
        $(".contents_accordion_header").find(".button_closer").show();
    } else {
        $(".contents_accordion_header").parent().children(".contents_accordion_body").hide();
        $(".contents_accordion_header").find(".button_opener").show();
        $(".contents_accordion_header").find(".button_closer").hide();
    }
}

function printAreaVisible() {
    if (printOpenFlg) {
        $(".contents_accordion_printing_header").parent().children(".contents_accordion_printing_body").show();
        $(".contents_accordion_printing_header").find(".button_opener").hide();
        $(".contents_accordion_printing_header").find(".button_closer").show();
    } else {
        $(".contents_accordion_printing_header").parent().children(".contents_accordion_printing_body").hide();
        $(".contents_accordion_printing_header").find(".button_opener").show();
        $(".contents_accordion_printing_header").find(".button_closer").hide();
    }
}

function pagingData() {
    searchAreaVisible();
    if (meterReadingFlg) {
        $(".printing").hide();
    } else {
        printAreaVisible();
    }
}

function beginPaging() {
    searchOpenFlg = $(".contents_accordion_header").parent().children(".contents_accordion_body").is(':visible');
    if (!meterReadingFlg) {
        printOpenFlg = $(".contents_accordion_printing_header").parent().children(".contents_accordion_printing_body").is(':visible');
    }
}

function reload() {
    setInitBillingAmountAccordion();
}

function setErrorMsgArea(target) {
    if (target == 'search') {
        $('.errSearch').show();
        $('.errPrint').hide();
    } else if (target == 'print') {
        $('.errSearch').hide();
        $('.errPrint').show();
    }
}

function changeBtnReloadDisabled(flg) {
  try {
    var obj = document.getElementById("meterReadingDataBeanForm:reloadButton");
    obj.disabled = flg;
  }
  catch(e) {
    alert(e);
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

