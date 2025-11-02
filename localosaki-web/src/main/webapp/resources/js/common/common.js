/**
 * "登録"や"削除"ボタンなどを2度押しできないようにする
 * 該当フォームに class="guard" の指定をする必要がある
 *
 * @returns {undefined}
 */
function guardSubmit() {

    // disabled を即時 true にすると、onclick 後の action 属性が発動されなくなるので一瞬遅らす
    setTimeout(function () {
        var guards = document.getElementsByClassName("guard");

        Array.prototype.forEach.call(guards, function (element) {
            element.disabled = true;

            // ブラウザから読み込み停止をした場合、ボタンが二度と押せなくなる
            // 一定時間後にボタンを有効にする
            setTimeout(function () {
                element.disabled = false;
            }, 10000);
        });
    }, 50);

}

/**
 * 対象ブラウザの判定
 *
 * @returns {undefined}
 */
function targetBrowser() {

    var _ua = (function () {
        return {
            ltIE6: typeof window.addEventListener === "undefined" && typeof document.documentElement.style.maxHeight === "undefined",
            ltIE7: typeof window.addEventListener === "undefined" && typeof document.querySelectorAll === "undefined",
            ltIE8: typeof window.addEventListener === "undefined" && typeof document.getElementsByClassName === "undefined",
            ltIE9: document.uniqueID && !window.matchMedia,
            gtIE10: document.uniqueID && document.documentMode === 10,
            gtIE11: document.uniqueID && document.documentMode >= 11,
            Trident: document.uniqueID,
            Gecko: window.sidebar,
            Presto: window.opera,
            Blink: window.chrome,
            Webkit: !window.chrome && typeof document.webkitIsFullScreen !== undefined,
            Touch: typeof document.ontouchstart !== "undefined",
            Mobile: typeof window.orientation !== "undefined"
        };
    })();

    console.log(_ua);

    var isNotSupport = false;

    if (_ua.gtIE11) {
        // IE11
        console.log("IE11");

    } else if (_ua.Blink) {
        // chrome, Blink版Operaも判定されるらしい
        // chromeのバージョンは user agent から判定する
        console.log("chrome");

        var ua = window.navigator.userAgent.toLowerCase();

        // chromeの文字列とメインバージョンまでを取得
        var match = ua.match(/chrome\/.*?\./);

        // 不要な文字列を除去
        var version = match[0].replace("chrome/", "").replace(".", "");
        console.log(version);

        // 有効なバージョンは 48.0 以上
        if (version < 48) {
            isNotSupport = true;
        }

    } else {
        // 上記以外のブラウザ
        isNotSupport = true;

    }

    if (isNotSupport) {
        window.location.href = "/osol-sms/f/html/error/notSupport.xhtml";
    }

}

/*ファイルアップロード選択時のファイル名表示*/
$(document).on("click", '.upload_button', function () {
    $('.file input[type=file]').change(function () {
        var file = $(this).prop('files')[0];
        if (file.name) {
            $('.filename').html(htmlEscape(file.name));
        }
    });
});

/*TOPへ戻るボタン*/
//$(function() {
//        var topBtn = $('#page-top');
//        topBtn.hide();
//        //スクロールが100に達したらボタン表示
//        $(window).scroll(function () {
//                if ($(this).scrollTop() > 100) {
//                        topBtn.fadeIn();
//                } else {
//                        topBtn.fadeOut();
//                }
//        });
//        //スクロールしてトップ
//    topBtn.click(function () {
//                $('body,html').animate({
//                        scrollTop: 0
//                }, 500);
//                return false;
//    });
//});

/* 複数要素のどれかが想定外の高さになった時にそろえる */
(function($) {
        var sets = [];
        var flatHeights = function(set) {
                var maxHeight = 0;
                set.each(function(){
                        var height = this.offsetHeight;
                        if (height > maxHeight) maxHeight = height;
                });
                set.css('height', maxHeight + 'px');
        };
        jQuery.fn.flatHeights = function() {
                if (this.length > 1) {
                        flatHeights(this);
                        sets.push(this);
                }
                return this;
        };
})(jQuery);

//画面を開いた時、タブを切り替えた時に、
//autofocusが設定されているものの中で可視・enableな最初のものにフォーカスを当てる
$(document).ready(function(){
    checkAutoFocus();
});
function checkAutoFocus(){
    $(".autofocus").each( function(index, elem){
        if ($(elem).is(':disabled') === false && $(elem).is(':hidden') === false) {
            $(elem).focus();
            return false;
        }
    });
}
function checkAutoFocusOnShowRichPanel(event){
    var flagFocus = false;
    $("div[id='"+event.rf.component.cdiv[0].id+"']").find(".autofocus").each( function(index, elem){
        if ($(elem).is(':disabled') === false && $(elem).is(':hidden') === false) {
            $(elem).focus();
            flagFocus = true;
            return false;
        }
    });
    //フォーカス指定が無い場合でも、Panel開く前にあったフォーカスを外しておく
    if(!flagFocus){
        if ($(":focus")[0] != undefined) {
            $(":focus")[0].blur();
        }
    }
}

/* 課金：機能利用不可の場合のダイアログ */
function functionCheckDialog(check) {
    if(!check){
        return functionNoUseDialogOpen();
    }
    return true;
}

//変更確認
var isChange = false;
$(document).on("change", ".check_change", function(){
    isChange = true;
});

function htmlEscape(string) {
  if(typeof string !== 'string') {
    return string;
  }
  return string.replace(/[&'`"<>]/g, function(match) {
    return {
      '&': '&amp;',
      "'": '&#x27;',
      '`': '&#x60;',
      '"': '&quot;',
      '<': '&lt;',
      '>': '&gt;',
    }[match]
  });
}