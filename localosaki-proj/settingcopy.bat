@echo off
echo 

echo リリース対象の設定ファイルを各リリースブランチにコピーをします。
echo 設定ファイルが最新になっていることを確認してください。
pause

echo まずはじめに必要な情報を入力します。
set /P RELEASE_DATE="リリース日をyyyyMMddで入力してください。"
set /P RELEASE_VERSION="アプリケーションバージョンをピリオドなしで入力してください。　例：3.0.1→301 "

set /P ANSWER1="dev環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER2="stg環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER3="本番環境をリリースしますか？(y=yes / n=no)？"

set HOME_DIR=C:\＊＊＊＊＊＊＊各自編集＊＊＊＊＊＊＊＊＊＊\
set SETTING_DIR=osol-sms-proj\リリース時差し替え\

set DEV_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_dev\
set STG_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_stg\
set PRO_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_pro\

set DEV_SET_DIR=%DEV_DIR%%SETTING_DIR%dev環境\
set STG_SET_DIR=%STG_DIR%%SETTING_DIR%stg環境\
set PRO_SET_DIR=%PRO_DIR%%SETTING_DIR%本番環境\

if /i {%ANSWER1%}=={y} (goto dev)
if /i {%ANSWER1%}=={yes} (goto dev)
if /i {%ANSWER1%}=={n} (goto devExit)
if /i {%ANSWER1%}=={no} (goto devExit)

:dev
echo dev環境に設定ファイルをコピーします。

rem osol-libを変更
copy /Y %DEV_SET_DIR%osol-lib\OsolConfigs.properties %DEV_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\OsolConfigs.properties
copy /Y %DEV_SET_DIR%osol-lib\OsolBatchConfigs.properties %DEV_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\batch\OsolBatchConfigs.properties
copy /Y %DEV_SET_DIR%osol-lib\SmsConfigs.properties %DEV_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\SmsConfigs.properties
copy /Y %DEV_SET_DIR%osol-lib\SmsBatchConfigs.properties %DEV_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\batch\SmsBatchConfigs.properties

rem osol-sms-webを変更
copy /Y %DEV_SET_DIR%osol-sms-web\web.xml %DEV_DIR%osol-sms-web\src\main\webapp\WEB-INF\web.xml
set "CHECK_TARGET=DEV環境"
goto devExit

:devExit
if /i {%ANSWER2%}=={y} (goto stg)
if /i {%ANSWER2%}=={yes} (goto stg)
if /i {%ANSWER2%}=={n} (goto stgExit)
if /i {%ANSWER2%}=={no} (goto stgExit)

:stg
echo stg環境に設定ファイルをコピーします。

rem libを変更
copy /Y %STG_SET_DIR%osol-lib\OsolConfigs_sms.properties %STG_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\OsolConfigs.properties
copy /Y %STG_SET_DIR%osol-lib\OsolBatchConfigs_sms.properties %STG_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\batch\OsolBatchConfigs.properties
copy /Y %STG_SET_DIR%osol-lib\SmsConfigs.properties %STG_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\SmsConfigs.properties
copy /Y %STG_SET_DIR%osol-lib\SmsBatchConfigs.properties %STG_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\batch\SmsBatchConfigs.properties

rem osol-sms-webを変更
copy /Y %STG_SET_DIR%osol-sms-web\web.xml %STG_DIR%osol-sms-web\src\main\webapp\WEB-INF\web.xml
set "CHECK_TARGET=STG環境"
goto stgExit

:stgExit
if /i {%ANSWER3%}=={y} (goto pro)
if /i {%ANSWER3%}=={yes} (goto pro)
if /i {%ANSWER3%}=={n} (goto proExit)
if /i {%ANSWER3%}=={no} (goto proExit)

:pro
echo 本番環境に設定ファイルをコピーします。

rem libを変更
copy /Y %PRO_SET_DIR%osol-lib\OsolConfigs.properties %PRO_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\OsolConfigs.properties
copy /Y %PRO_SET_DIR%osol-lib\OsolBatchConfigs.properties %PRO_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\osol\batch\OsolBatchConfigs.properties
copy /Y %PRO_SET_DIR%osol-lib\SmsConfigs.properties %PRO_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\SmsConfigs.properties
copy /Y %PRO_SET_DIR%osol-lib\SmsBatchConfigs.properties %PRO_DIR%osol-sms-lib\src\main\resources\jp\co\osaki\sms\batch\SmsBatchConfigs.properties

rem osol-sms-webを変更
copy /Y %PRO_SET_DIR%osol-sms-web\web.xml %PRO_DIR%osol-sms-web\src\main\webapp\WEB-INF\web.xml
copy /Y %PRO_SET_DIR%osol-sms-web\credentials %PRO_DIR%osol-sms-web\src\main\webapp\WEB-INF\template\credentials
set "CHECK_TARGET=本番環境"
goto proExit

:proExit



echo ------------------------------
echo --- 各種パラメータチェック ---
echo ------------------------------

REM ----環境ごとのチェックリストとソースコードの比較処理----
setlocal enabledelayedexpansion

REM ----変数----
set OkCount=0
set NgCount=0
set "AzbilDispFile=***"
set "AzbilDispLine=***"

echo 環境ごとの各種パラメータをチェックします。
echo [対象環境] !CHECK_TARGET!

REM ----環境ごとのチェックリストを読み込む----
if "!CHECK_TARGET!"=="DEV環境" (
	set "TARGET_DIR=%DEV_DIR%
	set "LIST_FILE=%DEV_DIRosol-sms-proj%pramchecklist_dev.txt"
) else if "!CHECK_TARGET!"=="STG環境" (
	set "TARGET_DIR=%STG_DIR%
	set "LIST_FILE=%STG_DIRosol-sms-proj%pramchecklist_stg.txt"
) else if "!CHECK_TARGET!"=="本番環境" (
	set "TARGET_DIR=%PRO_DIR%
	set "LIST_FILE=%PRO_DIRosol-sms-proj%pramchecklist_prod.txt"
)

echo --------------------
echo --- チェック開始 ---
echo --------------------

for /f "usebackq delims=" %%L in ("%LIST_FILE%") do (
    set "PAIR=%%L"
    for /f "tokens=1,2 delims=|" %%A in ("!PAIR!") do (
        set "FILE=!TARGET_DIR!\%%A"
        set "DISP_FILE=%%A"
        set "WORD=%%B"
        set Compare=NG

        REM ---ファイル有無を確認---
        if exist "!FILE!" (
            REM ---ファイルを検索---
            for /f "usebackq delims=" %%X in ("!FILE!") do (
                set "LINE=%%X"
                REM ---文字列が含まれるか検索---
				if "!LINE!"=="!WORD!" (
                    REM ---検索したい文字列に#が含まれている場合---
                    set Compare=OK
                    REM ---アズビル送信先は最終表示用にとっておく---
                    echo !LINE! | findstr /L /C:"AZBIL" >nul
                    if !errorlevel! == 0 (
                        set AzbilDispFile=!DISP_FILE!
                        set AzbilDispLine=!LINE!
                    )
                )
            )
            if "!Compare!"=="OK" (
                REM ---文字列が一致した---
                echo [OK] !DISP_FILE!: !WORD!
                set /a OkCount = OkCount+1
            ) else (
                REM ---文字列が一致しなかった---
                echo [ERROR] !DISP_FILE!: !WORD! に一致しません
                set /a NgCount = NgCount+1
            )
        ) else (
            echo [エラー] ファイルが見つかりません: !DISP_FILE!
            set /a NgCount = NgCount+1
        )
    )
)

echo ---------------------------------
echo --- チェック結果(Teams貼付け) ---
echo ---------------------------------
echo  [対象環境] !CHECK_TARGET!
echo  [OK] %OkCount% 件
echo  [ERROR] %NgCount% 件
echo  [アズビル送信先]!AzbilDispFile!: !AzbilDispLine!
echo  [バージョン(以下でよろしいですか)] 
echo   osol-sms-web\src\main\webapp\WEB-INF\web.xml
set "START=12"
set "END=13"
set /a LINE_COUNT=0
for /f "usebackq delims=" %%L in ("%TARGET_DIR%osol-sms-web\src\main\webapp\WEB-INF\web.xml") do (
    set /a LINE_COUNT+=1
    if !LINE_COUNT! GEQ %START% if !LINE_COUNT! LEQ %END% (
        echo %%L
    )
)

echo ------------
echo --- 終了 ---
echo ------------

endlocal
pause