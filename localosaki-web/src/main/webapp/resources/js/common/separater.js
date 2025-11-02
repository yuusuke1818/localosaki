/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**************************
  3桁カンマ区切りの追加削除
 **************************/
$(function() {
    /* テキスト表示用 */
    $(document).ready(function() {
       $('.numeric_co2,.numeric_kwh,.numeric_bill,.numeric_days,.numeric_m2').each(function(i){
            var num = $(this).val();
            if ($(this).is('.numeric_co2')){
                $(this).val(addZeroAtHead(addComma(num)));
            }else if($(this).is('.numeric_kwh')){
                $(this).val(addZeroAtHead(zeroPadding2(addComma(num))));
            }else if($(this).is('.numeric_bill')){
                $(this).val(addZeroAtHead(addComma(num)));
            }else if($(this).is('.numeric_days')){
                $(this).val(addComma(num));
            }else if($(this).is('.numeric_m2')){
                $(this).val(addZeroAtHead(addComma(num)));
            }
        });
    });

    /* 入力フォーム用 */
    /* (CO2) */
    $('.numeric_co2').on('focus', function(){
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_co2').on('blur', function(){
        var num = $(this).val();
        $(this).val(addZeroAtHead(addComma(num)));
    });

    /* (電気) */
    $('.numeric_kwh').on('focus', function(){
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_kwh').on('blur', function(){
        var num = $(this).val();
        $(this).val(addZeroAtHead(zeroPadding2(addComma(num))));
    });
   
    /* (金額) */
    $('.numeric_bill').on('focus', function(){
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_bill').on('blur', function(){
        var num = $(this).val();
        $(this).val(addZeroAtHead(addComma(num)));
    });

  /* (日数) */
    $('.numeric_days').on('focus', function(){
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_days').on('blur', function(){
        var num = $(this).val();
        $(this).val(addComma(num));
    });  

    /* (面積) */
    $('.numeric_m2').on('focus', function(){
        var num = $(this).val();
        $(this).val(delComma(num));
        $(this).select();
    });
    $('.numeric_m2').on('blur', function(){
        var num = $(this).val();
        $(this).val(addZeroAtHead(addComma(num)));
    }); 
});

/* 登録前には3桁カンマを削除する必要がある */
function allDeleteComma(){
  $('.numeric_co2,.numeric_kwh,.numeric_bill,.numeric_days,.numeric_m2').each(function(i){
    var num = $(this).val();
    $(this).val(delComma(num));
  });
}

/******** 流用部分 ********/
/* カンマ除去 */
function delComma(num3comma){
	var vRet = num3comma;
	while ( vRet.indexOf(",") >= 0 ) {
		vRet= vRet.replace(",","");
	}
	return vRet;

}

/* 3桁カンマ区切り追加*/
function addComma(num3comma){
	// カンマが含まれている場合はRETURN
	while( num3comma.indexOf(",") >= 0){
		num3comma = num3comma.replace(",","");
	}

	var vRet = "";
	var vDigitStr  = "";
	var vTmpStr = num3comma;
	var vTmpLen = num3comma.length;
	var vMinus = "";
	if ( vTmpStr.indexOf("-") == 0 ) {
		vMinus = "-";
		vTmpStr = vTmpStr.substring(1,vTmpLen);
		vTmpLen = vTmpStr.length;
	}
	if ( vTmpStr.indexOf(".") >= 0 ) {
		vDigitStr = vTmpStr.substring(vTmpStr.indexOf("."), vTmpStr.length);
		vTmpStr = vTmpStr.substring(0,vTmpStr.indexOf("."));
		vTmpLen = vTmpStr.length;
	}
	while ( vTmpLen > 3 ) {
		vTmpLen	= vTmpLen - 3;
		vRet	= "," + vTmpStr.substring(vTmpLen) + vRet;
		vTmpStr	= vTmpStr.substring(0,vTmpLen);
	}
	vRet= vMinus + vTmpStr + vRet + vDigitStr;
	return vRet;

}

//小数点3桁目を切り捨て
function floorNum3(val){
	n = val;
	n = n * 100;
	n = n + 0.0001;//丸め誤差補正値
	n = Math.floor(n);
	n = n / 100;
	return(n);
}

/* 小数点以下2桁になるように0パディング */
function zeroPadding2(argNum){
	var zeroPadNum = "";
	var tempNum = 0;
	try{
		tempNum = parseFloat(delComma(argNum));
		//数値の時
		if(! isNaN(tempNum)){
			if ( argNum.indexOf(".") >= 0 ) {
				//小数点あり
				//小数点以下の桁数が1のとき
				if( argNum.substring(argNum.indexOf(".") + 1).length == 1){
					zeroPadNum = argNum + "0";
				}else{
					zeroPadNum = argNum;
				}
			}else{
				//小数点なし
				zeroPadNum = argNum + ".00";
			}
		}else{
			//数値以外はそのまま
			zeroPadNum = argNum;
		}
	} catch(e){
		//その他はそのまま
		zeroPadNum = argNum;
	}
	return zeroPadNum;
}

// ".99"等のような入力に0を埋めて、"0.99"とするJS
function addZeroAtHead(numStr) {
	var pattern = /^\.[0-9]+$/;
	if (pattern.test(numStr)) {
		numStr = "0" + numStr;
	}
	return numStr;
}


