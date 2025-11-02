@echo off
set HOME_DIR=C:\proj\osol\build\release\
echo %HOME_DIR%のリリース対象環境のosol-projをビルドしモジュールを作成します。
pause

echo まずはじめに必要な情報を入力します。
set /P RELEASE_DATE="リリース日をyyyyMMddで入力してください。"
set /P RELEASE_VERSION="アプリケーションバージョンをピリオドなしで入力してください。　例：3.0.1→301 "

set /P ANSWER1="dev環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER2="stg環境をリリースしますか？(y=yes / n=no)？"
set /P ANSWER3="本番環境をリリースしますか？(y=yes / n=no)？"

set DEV_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_dev\osol-proj\
set STG_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_stg\osol-proj\
set PRO_DIR=%HOME_DIR%%RELEASE_DATE%_release_%RELEASE_VERSION%_pro\osol-proj\

echo JAVA_HOME に C:\Program Files\Java\jdk1.8.0_161 を設定します。

SET JAVA_HOME=C:\Program Files\Java\jdk1.8.0_161
echo JAVA_HOME=%JAVA_HOME%

if /i {%ANSWER1%}=={y} (goto dev)
if /i {%ANSWER1%}=={yes} (goto dev)
if /i {%ANSWER1%}=={n} (goto devExit)
if /i {%ANSWER1%}=={no} (goto devExit)

:dev
echo dev環境をビルドします。

echo osol-proj に移動
cd %DEV_DIR%

echo osol-proj をクリーン
call c:\apache-maven-3.5.0\bin\mvn -B -s C:\apache-maven-3.5.0\conf\settings.xml clean

echo osol-proj をビルド
call c:\apache-maven-3.5.0\bin\mvn -U -B -e -s C:\apache-maven-3.5.0\conf\settings.xml install

goto devExit

:devExit
if /i {%ANSWER2%}=={y} (goto stg)
if /i {%ANSWER2%}=={yes} (goto stg)
if /i {%ANSWER2%}=={n} (goto stgExit)
if /i {%ANSWER2%}=={no} (goto stgExit)

:stg
echo stg環境をビルドします。

echo osol-proj に移動
cd %STG_DIR%

echo osol-proj をクリーン
call c:\apache-maven-3.5.0\bin\mvn -B -s C:\apache-maven-3.5.0\conf\settings.xml clean

echo osol-proj をビルド
call c:\apache-maven-3.5.0\bin\mvn -U -B -e -s C:\apache-maven-3.5.0\conf\settings.xml install

goto stgExit

:stgExit
if /i {%ANSWER3%}=={y} (goto pro)
if /i {%ANSWER3%}=={yes} (goto pro)
if /i {%ANSWER3%}=={n} (goto proExit)
if /i {%ANSWER3%}=={no} (goto proExit)

:pro
echo 本番環境をビルドします。

echo osol-proj に移動
cd %PRO_DIR%

echo osol-proj をクリーン
call c:\apache-maven-3.5.0\bin\mvn -B -s C:\apache-maven-3.5.0\conf\settings.xml clean

echo osol-proj をビルド
call c:\apache-maven-3.5.0\bin\mvn -U -B -e -s C:\apache-maven-3.5.0\conf\settings.xml install
goto proExit

:proExit

pause