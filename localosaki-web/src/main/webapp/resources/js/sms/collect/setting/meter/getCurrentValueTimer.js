(function() {
    var lastFetchAt = null;
    var timerId = null;
    var THRESHOLD_SEC = 180; // TODO 中村 本番は 180 (=3分)。動作確認中は10で確認実施
    var IN_PROGRESS_LABEL = "取得中...";
    var FAILED_LABEL = "取得失敗";
    var notifiedTimeout = false; // サーバ通知の一回のみ制御
    var autoClicked = false;

    // 経過秒を分秒にフォーマット
    function formatElapsed(sec) {
        if (sec < 60)
            return sec + "秒";
        var m = Math.floor(sec / 60), s = sec % 60;
        return s === 0 ? (m + "分")
                : (m + "分" + String(s).padStart(2, "0") + "秒");
    }

    // 失敗の有無を判定
    function hasFailed() {
      var tableRoot = document.querySelector('[id$="resultMeterData"]');
      if (!tableRoot) {
          return false;
      }
      var spans = tableRoot.querySelectorAll('.rf-edt-c-cnt > span, td > span');
      for (var i = 0; i < spans.length; i++) {
        var t = (spans[i].textContent || "").trim();
        if (t === FAILED_LABEL || spans[i].classList.contains('fetch-failed')) {
          return true;
        }
      }
      return false;
    }

    // 取得中…が残っているか判定
    function hasInProgress() {
      var tableRoot = document.querySelector('[id$="resultMeterData"]');
      if (!tableRoot) return false;
      var spans = tableRoot.querySelectorAll('.rf-edt-c-cnt > span, td > span');
      for (var i = 0; i < spans.length; i++) {
        if ((spans[i].textContent || "").trim() === IN_PROGRESS_LABEL) return true;
      }
      return false;
    }

    // 定格電流未取得のメッセージが表示されているか判定
    function hasRatedCurrentMissingMessage() {
        const elements = document.querySelectorAll('.ratedCurrentMissingMessage');
        for (const el of elements) {
            const text = (el.textContent || '').trim();
            if (text.length > 0) {
                return true;
            }
        }
        return false;
    }

    // API実行判定
    function isApiExecEnabled() {
        var el = document.getElementById("apiExecSuccessFlgText");
        if (!el) {
            return true;
        }
        var t = (el.textContent || el.innerText || "").trim().toLowerCase();
        return t === "true";
      }

    // 「状態表示更新」ボタンの活性/非活性を切替を遅延実行（DOMが揃うまで）
    function safeSetDispUpdateEnabled(enabled) {
        setTimeout(() => setDispUpdateEnabled(enabled), 0);
    }

    // 「状態表示更新」ボタンの活性/非活性を切替
    function setDispUpdateEnabled(enabled) {
        var btn = document.querySelector('[id$="s6DispValueLteMDeviceBtn"]');
        if (!btn) {
            return;
        }
        // 見た目のクラス切替（親のボックスに付与）
        var box = btn.closest('.button_large_2');
        if (box) {
            box.classList.toggle('gradation_type_1', enabled);
            box.classList.toggle('gradation_type_5', !enabled);
        }
        // クリック自体も止める
        if (enabled) {
            btn.removeAttribute('disabled');
            btn.disabled = false;
        } else {
            btn.setAttribute('disabled', 'disabled');
            btn.disabled = true;
        }
    }

    // しきい値超過時、表の「取得中...」を「取得失敗」にする
    function markFailed() {
        // RichFacesの拡張テーブルのルート（id末尾一致で取る）
        var tableRoot = document.querySelector('[id$="resultMeterData"]');
        if (!tableRoot) {
            return;
        }
        // データセルを走査（rf-edt-cはRichFacesのセルdiv）
        var replaced = false;
        // 最内側の表示用spanのみに限定
        var spans = tableRoot.querySelectorAll('.rf-edt-c-cnt > span, td > span');
        spans.forEach(function(el) {
            var txt = (el.textContent || "").trim();
            if (txt === IN_PROGRESS_LABEL
                    && !el.classList.contains('fetch-failed')) {
                el.textContent = FAILED_LABEL;
                el.classList.add('fetch-failed');
                replaced = true;
            }
        });
        // ボタン「状態表示更新」を非活性
        safeSetDispUpdateEnabled(false);
        return replaced;
    }

    function paint() {
        var lastFetchAt = localStorage.getItem("lastFetchAt");
        var el = document.getElementById("lastFetchMessage");
        if (!el) {
            return; // DOMが無ければスキップ
        }
        // lastFetchAtが未設定 かつ タイマーメッセージが「未取得」以外の場合、リターン
        if (lastFetchAt == null && (el.textContent || "").trim() !== "未取得") {
            return;
        }

        var sec = Math.floor((Date.now() - lastFetchAt) / 1000);
        var el = document.getElementById("lastFetchMessage");
        if (!el) {
            // 要素がまだ無ければ再試行
            console.debug("[paint] lastFetchMessage not yet found, retrying...");
            setTimeout(paint, 1000);
            return;
        }

        // 「状態表示更新」ボタンの現在の活性状態を確認
        var dispBtn = document.querySelector('[id$="s6DispValueLteMDeviceBtn"]');
        var btnDisabled = dispBtn ? dispBtn.disabled === true : false;

        // 「取得中…なし」 かつ 「取得失敗なし」 かつ 「ボタン「状態表示更新」非活性」 かつ 「定格電流未取得メッセージが表示されていない」の場合、固定メッセージ＆タイマー停止
        if (!hasInProgress() && !hasFailed()
                && btnDisabled && !hasRatedCurrentMissingMessage()) {
            el.textContent = "取得完了しました。設定値をご確認ください。";
            el.className = "timer ok"; // 緑色
            // 経過時間は非表示
            if (timerId) {
                clearInterval(timerId);
                timerId = null;
            }
            lastFetchAt = null; // 次回startFetchTimerまで描画更新しない
            return;
        }

        // タイムアウト設定時間の5秒前に自動で一度だけボタン「状態表示更新」押下
        if (sec > (THRESHOLD_SEC - 5)
                && sec < THRESHOLD_SEC) {
            if (!autoClicked) {
                var btn = document.querySelector('[id$="s6DispValueLteMDeviceBtn"]');
                if (btn && btn.click && !btn.disabled) {
                    btn.click(); // ボタン「状態表示更新」押下
                    autoClicked = true;
                }
            }
        }

        if (!el) {
            // DOMがまだ無いなら100ms後に再試行
            setTimeout(paint, 100);
            return;
        }
        if (sec < THRESHOLD_SEC) {
            el.textContent = "前回「現在値取得」から " + formatElapsed(sec);
            el.className = "timer ok";
        } else {
            el.textContent = "3分経過 再度「現在値取得」を押下してください。";
            el.className = "timer warn";

            var changed = markFailed(); // しきい値超過時に失敗表示をする
            // サーバ側へ通知
            if (!notifiedTimeout) {
                try {
                    if (typeof window.notifyTimeoutToServer === "function") {
                        window.notifyTimeoutToServer(); // a4j:jsFunctionを呼び出し
                        notifiedTimeout = true; // 初めてサーバ通知できた場合trueにする
                        console.debug("[timer] timeout notified");
                    } else {
                        // jsFunctionがまだ未生成（rendered条件や描画順の都合）
                        // フラグは立てず、次tickで再試行させる
                        console.debug("[timer] notifyTimeoutToServer not ready yet");
                    }
                } catch (e) {
                    // 呼び出し失敗時もフラグは立てない（次tickで再試行）
                    console.warn("[paint] サーバ通知に失敗:", e);
                }
            }
        }
    }

    window.startFetchTimer = function(epochMillis) {
        // タイマーの起点となる時刻を決める
        lastFetchAt = epochMillis ? Number(epochMillis) : Date.now();
        // 起点時刻をブラウザへ永続保存（ページ再読込後の復元用）
        try {
            localStorage.setItem("lastFetchAt", String(lastFetchAt));
        } catch (e) {
            // ここは通常到達しない想定だが、念のため
            console.warn("[startFetchTimer] 保存に失敗。永続化されない:", e);
        }
        // もし以前のタイマーが動いていれば止める（多重起動防止）
        if (timerId) {
            clearInterval(timerId);
        }
        autoClicked = false; // タイマー開始ごとに自動押下フラグをリセット
        // まず1回だけ即時に描画更新（待たせない）
        paint();
        // 以後は1秒ごとにpaint()を呼び続ける
        notifiedTimeout = false;
        // DOM描画完了を待ってからpaintを開始
        setTimeout(function() {
            paint();
            timerId = setInterval(paint, 1000);
        }, 1000); // 1000ms待機
    };

    document.addEventListener("DOMContentLoaded", function() {
        try {
            var saved = localStorage.getItem("lastFetchAt");
            if (saved) {
                // 前回保存した開始時刻があれば、それを使ってタイマーを再始動
                startFetchTimer(Number(saved));
            }
        } catch (e) {
            console.warn("[DOMContentLoaded] lastFetchAt の読み出しに失敗。復元せず続行:", e);
        }
    });
})();
