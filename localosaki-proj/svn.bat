@echo off
echo 

set HOME_DIR=C:\proj\osol\build\release\

echo SVNから最新のソースを %HOME_DIRECTORY% に取得します。
echo 後でコピーする設定ファイルが最新になっていることを確認してください。
pause

echo まずはじめに必要な情報を入力します。
set /P RELEASE_DATE="リリース日をyyyyMMddで入力してください。"
set /P RELEASE_VERSION="アプリケーションバージョンをピリオドなしで入力してください。　例：3.0.1→301 "
set CHECKOUT_DIRECTORY=file://s59022755/eng_all/00_svn/osol/sms_phase1/branches/release/%RELEASE_DATE%_%RELEASE_VERSION%
echo チェックアウトするパスは %CHECKOUT_DIRECTORY% です。

set /P ANSWER1="dev環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER2="stg環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER3="本番環境をリリースしますか？(y=yes / n=no)？"


echo 作業フォルダ %HOME_DIRECTORY% を削除
rmdir /s /q %HOME_DIRECTORY%

echo 作業フォルダ %HOME_DIRECTORY% を作成
mkdir %HOME_DIRECTORY%
echo 作業フォルダ %HOME_DIRECTORY% に移動
cd %HOME_DIRECTORY%
echo SVNから作業フォルダ  %HOME_DIRECTORY% にチェックアウト

call "C:\Program Files\TortoiseSVN\bin\svn" checkout %CHECKOUT_DIRECTORY% .
echo 作業フォルダ %HOME_DIRECTORY% を最新に更新
call "C:\Program Files\TortoiseSVN\bin\svn" update

echo 作業フォルダ %HOME_DIRECTORY%\webap-base-eclipse を作成
mkdir %HOME_DIRECTORY%\webap-base-eclipse
echo 作業フォルダ %HOME_DIRECTORY%\webap-base-eclipse に移動
cd %HOME_DIRECTORY%\webap-base-eclipse
echo SVNから作業フォルダ %HOME_DIRECTORY%\webap-base-eclipse にチェックアウト
call "C:\Program Files\TortoiseSVN\bin\svn" checkout file://s59022755/eng_all/00_svn/skyweb/base/trunk/webap-base-eclipse-forWildFly17 . 
echo 作業フォルダ %HOME_DIRECTORY%\webap-base-eclipse を最新に更新
call "C:\Program Files\TortoiseSVN\bin\svn" update

echo webap-base-eclipseを各リリースブランチにコピー

if /i {%ANSWER1%}=={y} (goto dev)
if /i {%ANSWER1%}=={yes} (goto dev)
if /i {%ANSWER1%}=={n} (goto devExit)
if /i {%ANSWER1%}=={no} (goto devExit)

:dev
echo dev環境にコピーします。
xcopy /C /E /Q %HOME_DIRECTORY%\webap-base-eclipse %HOME_DIRECTORY%\%RELEASE_DATE%_release_%RELEASE_VERSION%_dev\webap-base-eclipse\
goto devExit

:devExit
if /i {%ANSWER2%}=={y} (goto stg)
if /i {%ANSWER2%}=={yes} (goto stg)
if /i {%ANSWER2%}=={n} (goto stgExit)
if /i {%ANSWER2%}=={no} (goto stgExit)

:stg
echo stg環境にコピーします。
xcopy /C /E /Q %HOME_DIRECTORY%\webap-base-eclipse %HOME_DIRECTORY%\%RELEASE_DATE%_release_%RELEASE_VERSION%_stg\webap-base-eclipse\
goto stgExit

:stgExit
if /i {%ANSWER3%}=={y} (goto pro)
if /i {%ANSWER3%}=={yes} (goto pro)
if /i {%ANSWER3%}=={n} (goto proExit)
if /i {%ANSWER3%}=={no} (goto proExit)

:pro
echo 本番環境にコピーします。
xcopy /C /E /Q %HOME_DIRECTORY%\webap-base-eclipse %HOME_DIRECTORY%\%RELEASE_DATE%_release_%RELEASE_VERSION%_pro\webap-base-eclipse\
goto proExit

:proExit

pause
