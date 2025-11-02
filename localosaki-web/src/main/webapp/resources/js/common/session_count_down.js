// セッションカウントダウンタイマーID
var common_sessionCountDownTimerId;

/**
 * セッションカウントダウン開始
 *
 * セッションカウントダウン設定処理を5秒後に実行します。
 */
function common_sessionCountDownStart() {
	// 5秒後に実行
	setTimeout('common_sessionCountDown()',5000);
}

/**
 * セッションカウントダウン設定
 *
 * 60秒毎にセッションカウントダウン処理を行うよう設定します。
 */
function common_sessionCountDown() {
	// 初回実行
	common_sessionCount();
	// 以後、60秒毎に実行
	common_sessionCountDownTimerId = setInterval('common_sessionCount()',60000);
}

/**
 * セッションカウントダウン
 *
 * セッションカウントダウンを行います。
 */
function common_sessionCount() {
	try {
		// 現在カウント文字列
		var countText = $('#sessionCount').text();
		// 現在カウント数値
		var countNum  = parseInt(countText);
		// デクリメント
		countNum--;

		if (1 < countNum) {
			// カウント中
			$('#sessionCount').text(countNum);
		} else if (countNum === 1) {
			// 残り1分以内
			$('#sessionCountDiv').html('残り <strong style="color:red">'
				+ '<span id="sessionCount">' + countNum + '</span></strong> 分以内にタイムアウトになります。');
		} else if(countNum <= 0) {
			// タイムアウト
			$('#sessionCountDiv').html('<strong style="color:red">'
				+ 'まもなくタイムアウトになります。</strong>');
			// 60秒毎のカウントを終了
			clearInterval(common_sessionCountDownTimerId);
		}
	} catch(e) {
		$('#sessionCountDiv').html('<strong style="color:red">セッションカウントエラー</strong>');
		// 60秒毎のカウントを終了
		clearInterval(common_sessionCountDownTimerId);
	}
}