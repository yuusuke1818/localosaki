/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.mng.constants;

import java.math.BigDecimal;

/**
 * 機器制御APIで使う定数値を扱うクラス
 *
 * @author yasu_shimizu
 */
public class SmControlConstants {

    /**
     * 一括系での個別レコードのレスポンスコード
     */
    public static final String RECORD_RESULT = "recordResult";

    /**
     * 一括系で新旧設定値に変更がない場合の結果コード
     */
    public static final String RECORD_NO_CHANGE = "recordNoChange";

    /**
     * 機器から応答データがない場合の結果コード
     */
    public static final String RESPONSE_NO_DATA = "responseNoData";

    /**
     * BeanIO 定義 リクエスト種別
     */
    public static final String TO_FIXEDSTRING_STREAM_NAME_POSTFIX = ".req";

    /**
     * BeanIO 定義 レスポンス種別
     */
    public static final String FROM_FIXEDSTRING_STREAM_NAME_POSTFIX = ".res";

    /**
     * BeanIO 定義 レスポンス種別
     */
    public static final String FROM_FIXEDSTRING_STREAM_NAME_OLD_POSTFIX = ".res_old";

    /**
     * BeanIO 定義ファイル リソースフォルダ
     */
    public static final String MAPPING_FILE_RESOURCES_FOLDER = "/mapping/";

    /**
     * BeanIO 定義 レスポンス種別
     */
    public static final String MAPPING_FILE_EXTENSION = ".xml";

    /**
     * BeanIO 定義 応答データなし用マップ名
     */
    public static final String MAPPING_NAME_SM_RESPONSE_NONE = "res_data.none";

    /**
     * 機器通信ポート番号
     */
    public static final int SM_CONTROL_UDP_PORT = 3000;
    public static final int SM_CONTROL_UDP_PORT_AIEL_MASTER = 3001;
    public static final int SM_CONTROL_UDP_PORT_T = 3002;

    /**
     * 機器通信ステータス 未更新時 フラグリセット時間
     */
    public static final int SM_CONTROL_ACTIVE_RESET_TIME = 1000 * 60 * 10;

    /**
     * 機器通信ステータス 更新処理識別文字列
     */
    public final static String EXECUTEUPDATE_MAP_KEY_UPDATE = "Update";

    /**
     * 機器通信ステータス 初期化処理識別文字列
     */
    public final static String EXECUTEUPDATE_MAP_KEY_INIT = "Init";

    /**
     * 機器制御をする製品：SM-FV2
     */
    public final static String PRODUCT_CD_FV2 = "00";

    /**
     * 機器制御をする製品：FVP(D)
     */
    public final static String PRODUCT_CD_FVP_D = "03";

    /**
     * 機器制御をする製品：FVPα(D)
     */
    public final static String PRODUCT_CD_FVP_ALPHA_D = "06";

    /**
     * 機器制御をする製品：FVPα(G2)
     */
    public final static String PRODUCT_CD_FVP_ALPHA_G2 = "12";

    /**
     * 機器制御をする製品：Eα
     */
    public final static String PRODUCT_CD_E_ALPHA = "21";

    /**
     * 機器制御をする製品：Eα2
     */
    public final static String PRODUCT_CD_E_ALPHA_2 = "22";

    /**
     * 機器通信仕様
     */
    public final static int SM_COMM_SEQ_LENGTH = 2; // シーケンス番号
    public final static int SM_COMM_TAG1_LENGTH = 1; // タグ1 (総パケット数)
    public final static int SM_COMM_TAG2_LENGTH = 1; // タグ2 (パケット番号)
    public final static int SM_COMM_DATABYTE_LENGTH = 4; // データバイト数
    public final static int SM_COMM_RESPONSECODE_LENGTH = 1; // 応答コード
    public final static int SM_COMM_PRODUCT_CD_LENGTH = 2; // 製品コード
    public final static int SM_COMM_ADDRESS_LENGTH = 2; // アドレス
    public final static int SM_COMM_COMMAND_LENGTH = 5; // コマンド
    public final static int SM_COMM_DATE = 10; // 日付
    public final static int SM_COMM_COMPRESSDATA_LENGTH = 6; // 圧縮データ長
    public final static int SM_COMM_HEAD_LENGTH = SM_COMM_SEQ_LENGTH +
            SM_COMM_TAG1_LENGTH +
            SM_COMM_TAG2_LENGTH +
            SM_COMM_DATABYTE_LENGTH +
            SM_COMM_RESPONSECODE_LENGTH;
    public final static int SM_COMM_DATA_LENGTH_MAX = 1400; // 1パケット当たりの最大データ長

    public final static int SM_COMM_DATAPOINT_TAG1 = SM_COMM_SEQ_LENGTH;
    public final static int SM_COMM_DATAPOINT_TAG2 = SM_COMM_DATAPOINT_TAG1 + SM_COMM_TAG1_LENGTH;
    public final static int SM_COMM_DATAPOINT_DATABYTE_LENGTH = SM_COMM_DATAPOINT_TAG2 + SM_COMM_TAG2_LENGTH;
    public final static int SM_COMM_DATAPOINT_RESPONSECODE = SM_COMM_DATAPOINT_DATABYTE_LENGTH + SM_COMM_DATABYTE_LENGTH;
    public final static int SM_COMM_DATAPOINT_PRODUCT_CD = SM_COMM_HEAD_LENGTH;

    public final static int SM_COMM_DATAPOINT_RESPONSECODE_T = SM_COMM_SEQ_LENGTH + 2;
    public final static int SM_COMM_DATAPOINT_PRODUCT_CD_T = SM_COMM_DATAPOINT_RESPONSECODE_T + 1;

    public final static int SM_COMM_DATA_HEAD_LENGTH = SM_COMM_PRODUCT_CD_LENGTH +
            SM_COMM_ADDRESS_LENGTH +
            SM_COMM_COMMAND_LENGTH;

    public final static int SM_COMM_DATA_HEAD_LENGTH_COMPRESS = SM_COMM_DATA_HEAD_LENGTH +
            SM_COMM_COMPRESSDATA_LENGTH;

    public final static int SM_COMM_DATA_HEAD_LENGTH_INC_DATE = SM_COMM_DATA_HEAD_LENGTH + SM_COMM_DATE;

    public final static int SM_COMM_DATA_HEAD_LENGTH_COMPRESS_INC_DATE = SM_COMM_DATA_HEAD_LENGTH_INC_DATE +
            SM_COMM_COMPRESSDATA_LENGTH;

    // 新機種用 (byte数など異なるもののみ定義)
    public final static int SM_COMM_TAG1_LENGTH_PTN2 = 2;        // タグ1 (総パケット数)
    public final static int SM_COMM_TAG2_LENGTH_PTN2 = 2;        // タグ2 (パケット番号)
    public final static int SM_COMM_DATABYTE_LENGTH_PTN2 = 6;    // データバイト数
    public final static int SM_COMM_HEAD_LENGTH_PTN2 = SM_COMM_SEQ_LENGTH +
            SM_COMM_TAG1_LENGTH_PTN2 +
            SM_COMM_TAG2_LENGTH_PTN2 +
            SM_COMM_DATABYTE_LENGTH_PTN2 +
            SM_COMM_RESPONSECODE_LENGTH;

    public final static int SM_COMM_DATAPOINT_TAG1_PTN2 = SM_COMM_SEQ_LENGTH;
    public final static int SM_COMM_DATAPOINT_TAG2_PTN2 = SM_COMM_DATAPOINT_TAG1_PTN2 + SM_COMM_TAG1_LENGTH_PTN2;
    public final static int SM_COMM_DATAPOINT_DATABYTE_LENGTH_PTN2 = SM_COMM_DATAPOINT_TAG2_PTN2 + SM_COMM_TAG2_LENGTH_PTN2;
    public final static int SM_COMM_DATAPOINT_RESPONSECODE_PTN2 = SM_COMM_DATAPOINT_DATABYTE_LENGTH_PTN2 + SM_COMM_DATABYTE_LENGTH_PTN2;

    /**
     * 機器通信 応答コード：正常受信
     */
    public final static byte SM_COMM_RESPONSE_SUCCESS = '0';

    /**
     * 機器通信 応答コード：受信異常
     */
    public final static byte SM_COMM_RESPONSE_ERROR = '1';

    /**
     * 機器通信 応答コード：他電文処理中
     */
    public final static byte SM_COMM_RESPONSE_BUSY = '2';

    /**
     * 機器通信 応答コード：応答データなし
     */
    public final static byte SM_COMM_RESPONSE_NONE = '3';

    /**
     * 機器通信ログ 送信タグ
     */
    public final static String SM_COMM_LOG_TAG_SEND = "SEND";

    /**
     * 機器通信ログ 受信タグ
     */
    public final static String SM_COMM_LOG_TAG_RECV = "RECV";

    /**
     * 機器通信ログ 圧縮データ16進数文字列タグ
     */
    public final static String SM_COMM_LOG_TAG_COMP = "COMP";

    /**
     * 機器通信でJSON→固定長化するXMLを読み込むAPIID
     */
    public static enum BEANIO_MAPPING_API_ID {
        //        A200001,    // pingのため変換無し
        A200002, A200003, A200004, A200005, A200006, A200007, A200008, A200009, A200010, A200011, A200012, A200013, A200014, A200015, A200016, A200017, A200018, A200019, A200020, A200021, A200022, A200023, A200024, A200025,
        //        A200026,    // 任意電文のため変換なし
        A200027, A200028, A200029, A200030, A200031, A200032, A200033, A200034, A200035, A200036, A200037, A200038, A200039, A200039D, A200039H, A200040, A200041, A200042,
        // A200043 A000006を使用するため不要
        // A200044 A000007を使用するため不要
        // A200045 A000004を使用するため不要
        // A200046 A000005を使用するため不要
        A200048, A200049, A200050, A200051, A200052, A200053, A200054, A200055, A200056, A200057, A200058, A200059, A200060, A200061, A200062,A200067,A200068,
        A200063, A200064, A200065, A200066, A200069, A200070, A200071, A200072,
        A200202,
        A210001, A210002, A210003, A210004, A210005, A210006, A210010,
        A210007, A210008, A210009, A210011, A210012, A210013, A210014, A210015
    }

    /**
     * 機器制御で使用する定数値
     */
    /*** A200001_PING(取得) ***/
    public final static String DEFAULT_COUNT = "5"; // pingデフォルト回数
    public final static String PING_RESULT = "pingResult"; // ping表示結果
    public final static String PING_CHECK = "pingCheck"; // ping実行結果

    /*** A200002_温度制御(取得) ***/
    // -- 未使用 --

    /*** A200003_温度制御(設定) ***/
    public final static int CTRL_TIME_ZONE_LIST = 6; // 温湿度時限リストサイズ
    public final static int CTRL_PORT_LIST = 16; // 制御ポートリストサイズ

    /*** A200004_スケジュール(取得) ***/
    // -- 未使用 --

    /*** A200005_スケジュール(設定) ***/
    public final static String SCHEDULE_COMMAND = "A200005"; // スケジュール設定特定用コード
    public final static int SCHEDULE_LOAD_LIST_FV2 = 6; // 機器(FV2)における負荷リストサイズ
    public final static int SCHEDULE_LOAD_LIST_FVP_ALPHA_D = 12; // 機器(FVPα(D))における負荷リストサイズ
    public final static int SCHEDULE_LOAD_LIST_FVP_ALPHA_G2 = 12; // 機器(FVPα(G2))における負荷リストサイズ
    public final static int SCHEDULE_LOAD_LIST_MM_SCHEDULELIST = 12; // 機器(FV2/FVPα)における負荷MM月スケジュール設定サイズ

    /*** A200006_デマンド(取得) ***/
    // -- 未使用 --

    /*** A200007_デマンド(設定) ***/
    public final static String DEMAND_COMMAND = "A200007"; // デマンド設定特定用コード
    public final static int DEMAND_LOAD_LIST_FV2 = 6; // 機器(FV2)における負荷リストサイズ
    public final static int DEMAND_LOAD_LIST_FVPD = 6; // 機器(FVP(D))における負荷リストサイズ
    public final static int DEMAND_LOAD_LIST_FVP_ALPHA_D = 12; // 機器(FVPα(D))における負荷リストサイズ
    public final static int DEMAND_LOAD_LIST_FVP_ALPHA_G2 = 12; // 機器(FVPα(G2))における負荷リストサイズ
    public final static int DEMAND_LOAD_LIST_E_ALPHA = 16; // 機器(Eα)における負荷リストサイズ
    public final static int DEMAND_LOAD_LIST_E_ALPHA_2 = 16; // 機器(Eα2)における負荷リストサイズ

    /*** A200008_手動負荷制御(取得) ***/
    // -- 未使用 --

    /*** A200009_手動負荷制御(設定) ***/
    public final static int MANUAL_CTRL_LOAD_LIST_FV2 = 6; // 機器(FV2)における負荷リストサイズ
    public final static int MANUAL_CTRL_LOAD_LIST_FVPD = 6; // 機器(FVP(D))における負荷リストサイズ
    public final static int MANUAL_CTRL_LOAD_LIST_FVP_ALPHA_D = 12; // 機器(FVPα(D))における負荷リストサイズ
    public final static int MANUAL_CTRL_LOAD_LIST_FVP_ALPHA_G2 = 36; // 機器(FVPα(G2))における負荷リストサイズ
    public final static int MANUAL_CTRL_LOAD_LIST_E_ALPHA = 96; // 機器(Eα)における負荷リストサイズ
    public final static int MANUAL_CTRL_LOAD_LIST_E_ALPHA_2 = 96; // 機器(Eα2)における負荷リストサイズ

    /*** A200010_出力端末制御(取得) ***/
    // -- 未使用 --

    /*** A200011_出力端末制御(設定) ***/
    // -- 未使用 --

    /*** A200012_ユニット(取得) ***/
    // -- 未使用 --

    /*** A200013_ユニット(設定) ***/
    public final static int UNIT_LOAD_LIST_FV2 = 20; // 機器(FV2)における負荷リストサイズ
    public final static int UNIT_LOAD_LIST_FVPD = 20; // 機器(FVP(D))における負荷リストサイズ
    public final static int UNIT_LOAD_LIST_FVP_ALPHA_D = 20; // 機器(FVPα(D))における負荷リストサイズ
    public final static int UNIT_LOAD_LIST_FVP_ALPHA_G2 = 58; // 機器(FVPα(G2))における負荷リストサイズ
    //    public final static String DM_VALUE = "1";            // ユニット取得と同値
    //    public final static String OFF_SET_VALUE = "0";        // ユニット取得と同値
    //    public final static String FACTOR_VALUE= "1";        // ユニット取得と同値

    /*** A200014_FVx間管理情報(取得) ***/
    // -- 未使用 --

    /*** A200015_FVx間管理情報(設定) ***/
    public final static int ERROR_ALERT_MEASUREPOINT_LIST = 20; // 異常警報発呼計測ポイントXリストサイズ

    /*** A200016_通信動作モード(取得) ***/
    // -- 未使用 --

    /*** A200017_通信動作モード(設定) ***/
    // -- 未使用 --

    /*** A200018_FVPα用追加基本(取得) ***/
    // -- 未使用 --

    /*** A200019_FVPα用追加基本(設定) ***/
    // -- 未使用 --

    /*** A200020_イベント制御設定(取得) ***/
    // -- 未使用 --

    /*** A200021_イベント制御設定(設定) ***/
    public final static String SETTING_EVENT_COMMAND = "A200021"; // イベント制御設定 特定用コード
    public final static int SETTING_EVENT_LOAD_LIST = 36; // 機器(FVPα(G2))における負荷リストサイズ
    public final static int SETTING_EVENT_LIST = 3; // 機器(FVPα(G2))におけるイベントリストサイズ
    public final static int SETTING_EVENT_LOAD_LIST_E_ALPHA = 80; // 機器(Eα)における負荷リストサイズ
    public final static int SETTING_EVENT_LOAD_LIST_E_ALPHA_2 = 80; // 機器(Eα2)における負荷リストサイズ

    /*** A200022_アンサーバック設定(取得) ***/
    // -- 未使用 --

    /*** A200023_アンサーバック設定(設定) ***/
    public final static int ANSWER_BACK_LOAD_INFO_LIST = 36; // 機器(FVPα(G2))における負荷リストサイズ

    /*** A200024_デマンドスタート(取得) ***/
    // -- 未使用 --

    /*** A200025_デマンドスタート(設定) ***/
    // -- 未使用 --

    /*** A200026_任意電文送信 ***/
    public final static int TELEGRAM_MAX_LENGTH = 9999; // 任意電文 最大長

    /*** A200027_負荷制御履歴(取得) ***/
    public final static int LOAD_CONTROL_LOG_RETRY_CNT = 19; // 最大履歴指定数
    public final static int LOAD_CONTROL_LOG_RETRY_CNT_OTHER_ALPHA = 9; // 最大履歴指定数(αシリーズ以外)

    /*** A200028_温度制御履歴(取得) ***/
    public final static int TEMPERATURE_CONTROL_LOG_RETRY_CNT = 9; // 最大履歴指定数

    /*** A200029_デマンド日報(取得) ***/
    // -- 未使用 --

    /*** A200030_デマンド任意(取得) ***/
    // -- 未使用 --

    /*** A200031_常時監視(取得) ***/
    // -- 未使用 --

    /*** A200032_無線装置情報(取得) ***/
    // -- 未使用 --

    /*** A200033_FOMA電波状態(取得) ***/
    // -- 未使用 --

    /*** A200034_装置情報(取得) ***/
    // -- 未使用 --

    /*** A200035_イベント制御履歴(FVP)(取得) ***/
    // -- 未使用 --

    /*** A200036_RS485回線異常履歴(取得) ***/
    // -- 未使用 --

    /*** A200037_無線温度ロガー履歴(取得) ***/
    // -- 未使用 --

    /*** A200038_負荷制御実績(取得) ***/
    // -- 未使用 --

    /*** A200039_日報(取得) ***/
    public static final String DAY_REPORT_COMMAND = "command"; // Result構成キー値
    public static final String MEASURE_DAY_TIME = "measureDayTime"; // Result構成キー値
    public static final String AGGREGATE_HOUR = "aggregateHour"; // Result構成キー値
    public static final String POINT_LIST = "pointList"; // Result構成キー値
    public static final String MEASURE_POINT_NUM = "measurePointNum"; // Result構成キー値
    public static final String DAY_MAX_DEMAND = "dayMaxDemand"; // Result構成キー値
    public static final String DAY_MAX_DEMAND_TIME = "dayMaxDemandTime"; // Result構成キー値
    public static final String MONTH_MAX_DEMAND = "monthMaxDemand"; // Result構成キー値
    public static final String MONTH_MAX_DEMANDTIME = "monthMaxDemandTime"; // Result構成キー値
    public static final String YEAR_MAX_DEMAND = "yearMaxDemand"; // Result構成キー値
    public static final String YEAR_MAX_DEMAND_TIME = "yearMaxDemandTime"; // Result構成キー値
    public static final String TIME_NUM_LIST = "timeNumList"; // Result構成キー値
    //コマンド別定数値
    public static final String COMMAND_DB = "DB0000"; // コマンド定数値 日報(FVPα(G2)以外)
    public static final String COMMAND_HD = "HD0000"; // コマンド定数値 日報(FVPα(G2))
    public static final String COMMAND_D0 = "D00000"; // コマンド定数値 日報(FVPα(G2))
    public static final String COMMAND_INITIALS_D = "D"; // コマンド頭文字
    public static final String COMMAND_INITIALS_H = "H"; // コマンド頭文字
    //HD000用定数値
    public static final int TIME_LIST_SIZE = 12; // 回数リストサイズ

    public static final int HALF_HOUR_DEMAND_DATA_BYTE = 4; // 30分間デマンド値データバイト数
                                                            // 全30分間デマンド値 抽出用インデックス
    public static final int ALL_HALF_HOUR_DEMAND_DATA_BYTE_INDEX = TIME_LIST_SIZE * HALF_HOUR_DEMAND_DATA_BYTE;

    public static final int SAVING_DEMAND_BYTE = 4; // 節電可能電力バイト数
                                                    // 全節電可能電力 抽出用インデックス
    public static final int ALL_SAVING_DEMAND_DATA_BYTE_INDEX = (TIME_LIST_SIZE * SAVING_DEMAND_BYTE)
            + ALL_HALF_HOUR_DEMAND_DATA_BYTE_INDEX;

    public static final int POINT_NUM_BYTE = 2; // ポイントNoバイト数
    public static final int DEMAND_BYTE = 4; // デマンドデータバイト数
                                             // 全デマンドデータ 抽出用インデックス
    public static final int DEMAND_DATA_BYTE_INDEX = ALL_SAVING_DEMAND_DATA_BYTE_INDEX;

    /*** A200040_最大デマンド(取得) ***/
    // -- 未使用 --

    /*** A200041_複数建物・テナント一括温度制御(取得) ***/
    // -- 未使用 --

    /*** A200042_複数建物・テナント一括温度制御(設定) ***/
    public final static String BULK_TEMPERATURE_COMMAND = "A200042"; // 温度制御特定用コード
    public final static String TEMP_CONTROL = "0"; // 温度制御
    public final static String COOLER = "1"; // 冷房設定
    public final static String HEATER = "2"; // 暖房設定
    public final static String TEMPERATURE_COMMAND = "temperature"; // 温度制御設定
    public final static String EVENT_COMMAND = "event"; // イベント制御設定
    public final static String DISABLE_PORT = "0"; // 無効ポート
    public final static String ABLE_PORT = "1"; // 有効ポート
    public final static String TEMP_CONTROL_ENABLED_TO_DISABLED = "1"; // 有効→無効
    public final static String TEMP_CONTROL_NONE = "0"; // 設定なし

    /*** A200043_複数建物・テナント一括目標電力(取得) ***/
    // -- 未使用 --

    /*** A200044_複数建物・テナント一括目標電力(設定) ***/
    public final static String BULK_TARGET_POWER_COMMAND = "A200044"; // 目標電力特定用コード

    /*** A200045_複数建物・テナント一括 スケジュール(取得) ***/
    // -- 未使用 --

    /*** A200046_複数建物・テナント一括 スケジュール(設定) ***/
    public final static String BULK_SCHEDULE_COMMAND = "A200046"; // スケジュール特定用コード
    public final static String SCHEDULE_MANAGE_ASSINGMENT_NO_SET = "0"; // 管理指定無
    public final static String SCHEDULE_MANAGE_ASSINGMENT_WITH_SET = "1"; // 管理指定有
    public final static String NUM_TO_WORD_NO_SET = "なし"; // 管理指定 0 -> なし
    public final static String NUM_TO_WORD_WITH_SET = "あり"; // 管理指定 1 -> あり

    /*** A200047_複数建物・テナント一括 制御 ***/
    public final static String BULK_CTRL_PARAM_SMID = "smId";    // 機器ID
    public final static String BULK_CTRL_PARAM_SMADDR = "smAddress";    // 機器アドレス
    public final static String BULK_CTRL_PARAM_CLASS = "class";    // クラス名
    public final static String BULK_CTRL_PARAM_ADDR = "address";    // アドレス
    public final static String BULK_CTRL_PARAM_PRODUCTCD = "productCd";    // 製品コード
    public final static String BULK_CTRL_PARAM_BUILDING_ID = "buildingId";    // 建物ID
    public final static String BULK_CTRL_PARAM_BUILDING_LIST = "buildingList";    // 建物IDリスト
    public final static String BULK_CTRL_PARAM_TARGET_POWER = "targetPower";    // 目標電力
    public final static String BULK_CTRL_PARAM_TEMPERATURE_FLG = "temperatureFlg"; // 温度制御
    public final static String BULK_CTRL_PARAM_TEMPERATURE_MAX = "temperatureMax";    // 温度上限
    public final static String BULK_CTRL_PARAM_TEMPERATURE_MIN = "temperatureMin";    // 温度下限
    public final static String BULK_CTRL_PARAM_CTRL_THRESHOLD = "ctrlThreshold";    // 制御閾値
    public final static String BULK_CTRL_PARAM_SCHEDULE_MNG_ASSIGNMENT = "scheduleManageAssignment";    // スケジュール管理指定
    public final static String BULK_CTRL_PARAM_SETTING_CHANGE_HIST = "settingChangeHist";    // 設定変更履歴
    public final static String BULK_CTRL_PARAM_UPDATE_DB_FLG = "updateDBflg";    // DB登録フラグ
    public final static String BULK_CTRL_PARAM_PAGE_ASSIGNMENT = "pageAssignment";    // ページ指定
    public final static String BULK_CTRL_PARAM_SETTING_MONTH_SCHEDULE_LIST = "settingMonthScheduleList";    // 月スケジュール設定リスト
    public final static String BULK_CTRL_PARAM_GET_CUR_TEMPERATURE_FLG = "getCurrentTemperatureFlg";    // 現在温度取得フラグ
    public final static String BULK_CTRL_PARAM_CTRL_PERMISSION_TH = "ctrlPermissionTH";    // 温湿度制御許可
    public final static String BULK_CTRL_PARAM_EVENT_CTRL_FLG = "eventCtrlFlg";    // イベント制御フラグ
    public final static String BULK_CTRL_PARAM_SETTING_EVENT_CTRL_LIST = "settingEventCtrlList";    // イベント制御設定リスト
    public final static String BULK_CTRL_PARAM_DEMAND_GANG_PERMISSION = "demandGangCtrlPermission";    // デマンド連動制御許可
    public final static String BULK_CTRL_PARAM_SWITCH_CHOICE_CW = "switchChoiceCW";    // 冷暖房切替SW選択
    public final static String BULK_CTRL_PARAM_EVENT_TERMS = "eventTerms";    // イベント条件
    public final static String BULK_CTRL_PARAM_CTRL_TERMS = "ctrlTerms";    // 制御条件
    public final static String BULK_CTRL_PARAM_LOAD_INFO_LIST = "loadInfoList";    // 負荷情報リスト
    public final static String BULK_CTRL_PARAM_TEMP_LOAD_INFO_LIST = "_loadInfoList";    // 負荷情報リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_MONTH_LIST = "monthList";    // 月リスト
    public final static String BULK_CTRL_PARAM_TEMP_MONTH_LIST = "_monthList";    // 月リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_TIME_LIST = "timeList";    // 時限リスト
    public final static String BULK_CTRL_PARAM_COMMAND = "command";    // コマンド
    public final static String BULK_CTRL_PARAM_COMMANDCD = "commandCd";    // コマンドコード
    public final static String BULK_CTRL_PARAM_MIN_LOAD_BLOCK_TIME_LIST = "minLoadBlockTimeList";    // 複数遮断時間リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_LOAD_BLOCK_METHOD_LIST = "loadBlockMethodList";    // 遮断方法リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_LOAD_BLOCK_RANK_LIST = "loadBlockRankList";    // 遮断順位リスト (温度参照ポイントリスト)
    public final static String BULK_CTRL_PARAM_TEMPERATURE_REF_POINT_LIST = "temperatureRefPointList";    // 温度参照ポイントリスト (一時格納用)
    public final static String BULK_CTRL_PARAM_LOAD_BLOCK_CAP_LIST = "loadBlockCapacityList";    // 負荷遮断容量リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_TEMP_MAX_DEMAND_MONTH_LIST = "_maxDemandMonthList";    // 月最大デマンドリスト (一時格納用)
    public final static String BULK_CTRL_PARAM_TEMP_TARGET_POWER_MONTH_LIST = "_maxTargetPowerMonthList";    // 月目標電力リスト (一時格納用)
    public final static String BULK_CTRL_PARAM_SETTING_DATE = "settingDate";    // 取得日時
    public final static String BULK_CTRL_PARAM_SCHEDULE_CONTROL_INFO = "scheduleControlInfo";    // スケジュール制御情報
    public final static String BULK_CTRL_PARAM_TEMP_CTRL_HIST = "tempControlHistory";    // 温度制御履歴
    public final static String BULK_CTRL_PARAM_TARGET_POWER_HIST = "targetPowerHistory";    // 目標電力履歴
    public final static String BULK_CTRL_PARAM_TARGET_POWER_DIFF = "targetPowerDiff";    // 目標電力差分（設定値）
    public final static String BULK_CTRL_PARAM_TARGET_POWER_DIFF_SIGN = "targetPowerDiffSign";    // 目標電力差分（符号）
    public final static String BULK_CTRL_PARAM_TEMP_COOL_HIST = "tempCoolHistory";    // 冷房温度設定履歴
    public final static String BULK_CTRL_PARAM_TEMP_COOL_DIFF = "tempCoolDiff";    // 冷房温度差分（設定値）
    public final static String BULK_CTRL_PARAM_TEMP_COOL_DIFF_SIGN = "tempCoolDiffSign";    // 冷房温度差分（符号）
    public final static String BULK_CTRL_PARAM_TEMP_HEAT_HIST = "tempHeatHistory";    // 暖房温度設定履歴
    public final static String BULK_CTRL_PARAM_TEMP_HEAT_DIFF = "tempHeatDiff";    // 暖房温度差分（設定値）
    public final static String BULK_CTRL_PARAM_TEMP_HEAT_DIFF_SIGN = "tempHeatDiffSign";    // 暖房温度差分（符号）
    public final static String BULK_CTRL_CAST_TARGET_POWER_SELECT = "A200006Param";    // デマンド(取得)
    public final static String BULK_CTRL_CAST_TARGET_POWER_SELECT_EA = "A200049Param";    // デマンド(取得) Eα
    public final static String BULK_CTRL_CAST_TEMPERATURE_CTRL_SELECT= "A200041Param";    // 温度制御（取得）


    public final static String BULK_CTRL_EVENT_CONTROL_DISABLE = "0"; // イベント制御 (無効)
    public final static String BULK_CTRL_EVENT_CONTROL_ENABLE = "1"; // イベント制御 (有効)
    public final static String BULK_CTRL_EVENT_CONTROL_TEMPERATURE = "2"; // イベント制御 (温度制御有効)
    public final static String BULK_CTRL_EVENT_CONTROL_LIGHTING = "3"; // イベント制御 (照明制御有効)
    public final static String BULK_CTRL_EVENT_CONTROL_DISABLE_NAME = "無効"; // イベント制御 (無効)
    public final static String BULK_CTRL_EVENT_CONTROL_ENABLE_NAME= "有効"; // イベント制御 (有効)
    public final static String BULK_CTRL_EVENT_CONTROL_TEMPERATURE_NAME = "温度制御有効"; // イベント制御 (温度制御有効)
    public final static String BULK_CTRL_EVENT_CONTROL_LIGHTING_NAME = "照明制御有効"; // イベント制御 (照明制御有効)


    /*** A200050_デマンド(設定) Eα ***/
    public final static String DEMAND_COMMAND_E_ALPHA = "A200050"; // デマンド設定 Eα 特定用コード

    /*** A200052_祝日(設定) ***/
    public final static String NATIONAL_HOLIDAY_UPDATE_COMMAND = "A200052"; // 祝日設定 特定用コード

    /*** A210006_AielMasterエリア設定(設定) ***/
    public final static String AIELMASTER_AREACONFIG_UPDATE = "A210006"; // AielMasterエリア設定 設定 特定用コード


    /*** A200055_計測ポイント(取得/設定) 共通 ***/
    public final static String MEASURE_POINT_INFO_1_CMD = "051";    // 計測ポイント設定情報 コマンド (1情報)
    public final static String MEASURE_POINT_INFO_2_CMD = "052";    // 計測ポイント設定情報 コマンド (2情報)
    public final static String MEASURE_POINT_INFO_1 = "1";           // 計測ポイント情報 1
    public final static String MEASURE_POINT_INFO_2 = "2";           // 計測ポイント情報 2

    /*** A200056_計測ポイント(設定) ***/
    public final static int MEASURE_POINT_UPDATE_INFO_LIST_E_ALPHA = 128;       // 機器(Eα)における計測ポイント情報リストサイズ
    public final static int MEASURE_POINT_UPDATE_INFO_LIST_E_ALPHA_2 = 128;     // 機器(Eα2)における計測ポイント情報リストサイズ

    /*** A200059_スケジュール(取得/設定)Eα 共通 ***/
    public final static String SCHEDULE_CONTROL_INFO_1_CMD = "048";    // スケジュール制御設定コマンド (1情報)
    public final static String SCHEDULE_CONTROL_INFO_2_CMD = "049";    // スケジュール制御設定コマンド (2情報)
    public final static String SCHEDULE_CONTROL_INFO_1 = "1";          // スケジュール制御設定 1情報
    public final static String SCHEDULE_CONTROL_INFO_2 = "2";          // スケジュール制御設定 2情報
    public final static int SCHEDULE_LOAD_LIST_E_ALPHA = 8;            // 機器(Eα)における負荷リストサイズ
    public final static int SCHEDULE_LOAD_LIST_E_ALPHA_2 = 8;          // 機器(Eα2)における負荷リストサイズ
    public final static int SCHEDULE_MONTH_SCHEDULE_LIST = 12;         // 月スケジュール設定サイズ

    /*** A200060_スケジュール(設定)Eα ***/
    public final static String SCHEDULE_UPDATE_COMMAND_E_ALPHA = "A200060"; // スケジュール設定Eα 特定用コード

    /*** A200062_スケジュールパターン(設定) ***/
    public final static String SCHEDULE_PATTERN_UPDATE_COMMAND = "A200062"; // スケジュールパターン設定 特定用コード
    public final static int SCHEDULE_PATTERN_UPDATE_TIME_ZONE_LIST = 4;    // 時間帯リストサイズ

    /*** A200069_出力ポイント設定(取得) ***/
    public final static String OUTPUT_POINT_PATTERN_SETTING_CHANGE_HIST_EA = "^[0-4]{1}$"; // 設定変更履歴Eα：0～4

    /*** A200070_出力ポイント設定(設定) ***/
    public final static int OUTPUT_POINT_LOAD_EX_LIST = 80; // 機器(Eα/Eα２)における負荷(EX1～EX80)リストサイズ
    public final static int OUTPUT_POINT_LOAD_LIST = 16; // 機器(Eα/Eα２)における負荷(A～P)リストサイズ

    /*** A200202_警報メール送信 ***/
    public final static String COMMAND_HA = "HA"; // 警報コマンド
    public final static String COMMAND_T9 = "T9"; // 警報コマンド
    public final static String COMMAND_HM = "HM"; // 警報コマンド

    public final static String COMMAND_HM_ALERM_STATE_3 = "3"; // 警報状態：蓄電池異常
    public final static String COMMAND_HM_ALERM_STATE_5 = "5"; // 警報状態：防災発生状況

    public final static String PRODUCT_CODE_HM = "23"; // HMのプロダクトコード
    public final static String PRODUCT_CODE_HM_STATE3 = "24"; // HMのプロダクトコード 蓄電池異常
    public final static String PRODUCT_CODE_HM_STATE5 = "25"; // HMのプロダクトコード 防災発生状況
    public final static String ALERM_STATE_HM_SETTINGREQ = "1"; // 1：Aiel Master設定要求
    public final static String ALERM_STATE_HM_FAIL = "2"; // 2：Aiel Master異常


    public final static String NO_ALERT = "0"; // 警報変化なし

    public final static String ALERT_OFF = "1"; // 警報解除

    /*** A210001_AielMasterスケジュール(取得) ***/
    public final static String AIEL_MASTER_SCHEDULE_SELECT_COMMAND = "A210001"; // AielMasterスケジュール(取得) 特定用コード

    /*** A210002_AielMasterスケジュール(設定) ***/
    public final static String AIEL_MASTER_SCHEDULE_UPDATE_COMMAND = "A210002"; // AielMasterスケジュール(設定) 特定用コード

    /*** A210003_AielMaster店舗設定(取得) ***/
    public final static String AIEL_MASTER_STORE_CONF_SELECT_COMMAND = "A210003"; // AielMaster店舗設定(取得) 特定用コード

    /*** A210004_AielMaster店舗設定(設定) ***/
    public final static String AIEL_MASTER_STORE_CONF_UPDATE_COMMAND = "A210004"; // AielMaster店舗設定(設定) 特定用コード

    /*** A210005_AielMasterエリア設定(取得) ***/
    public final static String AIEL_MASTER_AREA_CONF_SELECT_COMMAND = "A210005"; // AielMasterエリア設定(取得) 特定用コード

    /*** A210006_AielMasterエリア設定(設定) ***/
    public final static String AIEL_MASTER_AREA_CONF_UPDATE_COMMAND = "A210006"; // AielMasterエリア設定(設定) 特定用コード

    /*** A210010_AielMasterログ収集 ***/
    public final static String AIEL_MASTER_LOG_COLLECT_COMMAND = "A210010"; // AielMasterログ収集 特定用コード

    public final static String AIEL_MASTER_DEMAND_FORECAST_COLLECT = "A210009"; // 需要電力予測データ（取得）

    /*** A210011_AielMaster日報データ送信 ***/
    public final static String AIEL_MASTER_DAILY_REPORT_DATA_SEND = "A210011"; // AielMaster日報データ送信

    /** A210007_AielMaster気象予報データ送信 ***/
    public final static String AIEL_MASTER_WEATHER_FORECAST_SEND = "A210007"; // AielMaster気象予報データ送信


    /**
     * デマンド警報 T9 メール送信要因
     */
    public static enum DEMAND_ALERT_T9 {
        CAUTION("デマンド警報(注意)", "1"), BLOCK("デマンド警報(遮断)", "2"), LIMIT("デマンド警報(限界)", "3");

        private final String name;
        private final String val;

        private DEMAND_ALERT_T9(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            DEMAND_ALERT_T9[] types = DEMAND_ALERT_T9.values();
            for (DEMAND_ALERT_T9 type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * デマンド警報 HA メール送信要因
     */
    public static enum DEMAND_ALERT_HA {
        CAUTION("デマンド警報(注意)[発生]", "1"), CAUTION_RETURN("デマンド警報(注意)[復帰]", "2"), BLOCK("デマンド警報(遮断)[発生]",
                "3"), BLOCK_RETURN("デマンド警報(遮断)[復帰]",
                        "4"), LIMIT("デマンド警報(限界)[発生]", "5"), LIMIT_RETURN("デマンド警報(限界)[復帰]", "6");

        private final String name;
        private final String val;

        private DEMAND_ALERT_HA(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            DEMAND_ALERT_HA[] types = DEMAND_ALERT_HA.values();
            for (DEMAND_ALERT_HA type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * 移報警報 T9 メール送信要因
     */
    public static enum TRANSFER_ALERT_T9 {
        ALERT("移報警報", "1");

        private final String name;
        private final String val;

        private TRANSFER_ALERT_T9(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            TRANSFER_ALERT_T9[] types = TRANSFER_ALERT_T9.values();
            for (TRANSFER_ALERT_T9 type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * 計測ポイント異常 T9 メール送信要因
     */
    public static enum POINT_ALERT_T9 {
        INPUT_ON("接点入力(OFF)", "1"), INPUT_OFF("接点入力(ON)", "2"), ANALOG_MAX("アナログ上限超", "6"), ANALOG_MIN("アナログ下限超", "7");

        private final String name;
        private final String val;

        private POINT_ALERT_T9(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            POINT_ALERT_T9[] types = POINT_ALERT_T9.values();
            for (POINT_ALERT_T9 type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * デマンド警報 HM Aiel Master設定要求
     */
    public static enum DEMAND_ALERT_HM_SETTINGREG {
        NO("設定要求[なし]", "0"), YES("設定要求[あり]", "1");

        private final String name;
        private final String val;

        private DEMAND_ALERT_HM_SETTINGREG(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            DEMAND_ALERT_HM_SETTINGREG[] types = DEMAND_ALERT_HM_SETTINGREG.values();
            for (DEMAND_ALERT_HM_SETTINGREG type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * デマンド警報 HM Aiel Master異常
     */
    public static enum DEMAND_ALERT_HM_FAIL {
        CAUTION_RETURN("Aiel Master異常[復帰]", "0"), CAUTION("Aiel Master異常[発生]", "1");

        private final String name;
        private final String val;

        private DEMAND_ALERT_HM_FAIL(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            DEMAND_ALERT_HM_FAIL[] types = DEMAND_ALERT_HM_FAIL.values();
            for (DEMAND_ALERT_HM_FAIL type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /**
     * 複数建物・テナント一括制御 温度差分（符号）
     */
    public static enum BULK_CTRL_TEMP_DIFF_SIGN {
        PLUS("+", "0"),
        MINUS("-", "1");

        private final String name;
        private final String val;

        private BULK_CTRL_TEMP_DIFF_SIGN(final String str, String val) {
            this.name = str;
            this.val = val;
        }

        public String getName() {
            return this.name;
        }

        public String getVal() {
            return this.val;
        }

        public static String getName(final String val) {
            BULK_CTRL_TEMP_DIFF_SIGN[] types = BULK_CTRL_TEMP_DIFF_SIGN.values();
            for (BULK_CTRL_TEMP_DIFF_SIGN type : types) {
                if (type.getVal().equals(val)) {
                    return type.getName();
                }
            }
            return null;
        }
    }

    /*** 負荷制御履歴・温度制御履歴 共通定数 ***/
    public final static String ERROR_VALUE = "errorValue"; // 想定しない値

    /*** 複数建物・テナント一括機器制御系共通 ***/
    public final static String MAIL_SETTING_OK = "0"; // 設定成功
    public final static String MAIL_SETTING_NO_CHANGE = "1"; // 未送信
    public final static String MAIL_SETTING_NG = "2"; // 設定失敗

    /*** 機器制御 共通 ***/
    public final static String SETTING_CHG_HIST_LATEST = "0";   // 設定変更履歴（最新）
    public final static String SETTING_CHG_HIST_OLD = "9";   // 設定変更履歴 （最古）
    public final static String SETTING_CHG_HIST_OLD_EA = "4";   // 設定変更履歴 （最古）Eα用

    /**
     * DB登録時の定数
     */
    /*** 共通 ***/
    public final static int DEL_FLG_ON = 1; // 削除フラグ
    public final static int VERSION = 0; // VERSION

    /*** ユニット取得設定 ***/
    public final static String POINT_TYPE_2 = "02"; // ポイント種別_2
    public final static String POINT_TYPE_3 = "03"; // ポイント種別_3
    public final static String POINT_TYPE_4 = "04"; // ポイント種別_4
    public final static String POINT_TYPE_5 = "05"; // ポイント種別_5
    public final static String POINT_TYPE_6 = "06"; // ポイント種別_6
    public final static String POINT_TYPE_7 = "07"; // ポイント種別_7
    public final static String POINT_TYPE_A = "A"; // ポイント種別設定値_A
    public final static String POINT_TYPE_P = "P"; // ポイント種別設定値_P
    public final static String POINT_TYPE_0 = "0"; // ポイント種別設定値_0
    public final static String POINT_TYPE_K = "K"; // ポイント種別設定値_K
    public final static String DM_VALUE = "1"; // デマンド補正係数 定数
    public final static String OFF_SET_VALUE = "0"; // アナログオフセット値 定数
    public final static String FACTOR_VALUE = "1"; // アナログ換算係数 定数
    public final static int POINT_FLG = 0; // ポイント集計フラグ
    public final static String POINT_TYPE = "0"; // ポイント算出方式
    public final static String YEAR_NO = "0"; // デマンド年俸table_年度No
    public final static BigDecimal POINT_AVG = null; // デマンド年俸ポイントtable_ポイント平均値
    public final static BigDecimal POINT_MAX = null; // デマンド年俸ポイントtable_ポイント最大値

    /*** ユニット取得設定 Eα ***/
    // 端末種別
    public final static int TERMINAL_KIND_00 = 0;
    public final static int TERMINAL_KIND_01 = 1;
    public final static int TERMINAL_KIND_02 = 2;
    public final static int TERMINAL_KIND_03 = 3;
    public final static int TERMINAL_KIND_04 = 4;
    public final static int TERMINAL_KIND_05 = 5;
    public final static int TERMINAL_KIND_06 = 6;
    public final static int TERMINAL_KIND_07 = 7;
    public final static int TERMINAL_KIND_20 = 20;
    public final static int TERMINAL_KIND_21 = 21;
    public final static int TERMINAL_KIND_22 = 22;

    /*** ユニット設定 ***/
    // DB一括更新
    public final static String UNITUPDATE_SM_LINE_POINT ="smLinePointList";
    public final static String UNITUPDATE_GRAPH_ELEMENT ="graphElementList";
    public final static String UNITUPDATE_DM_YEAR_REP_POINT ="dmYearRepPointList";

    /*** 計測ポイント取得設定 ***/
    // ポート番号
    public final static String MEASURE_POINT_PORT_00 = "00";
    public final static String MEASURE_POINT_PORT_01 = "01";
    public final static String MEASURE_POINT_PORT_02 = "02";
    public final static String MEASURE_POINT_PORT_03 = "03";
    public final static String MEASURE_POINT_PORT_04 = "04";
    public final static String MEASURE_POINT_PORT_05 = "05";
    public final static String MEASURE_POINT_PORT_06 = "06";
    public final static String MEASURE_POINT_PORT_07 = "07";
    public final static String MEASURE_POINT_PORT_08 = "08";
    public final static String MEASURE_POINT_PORT_09 = "09";
    public final static String MEASURE_POINT_PORT_10 = "10";
    public final static String MEASURE_POINT_PORT_11 = "11";
    public final static String MEASURE_POINT_PORT_12 = "12";
    public final static String MEASURE_POINT_PORT_13 = "13";
    public final static String MEASURE_POINT_PORT_14 = "14";
    public final static String MEASURE_POINT_PORT_15 = "15";
    public final static String MEASURE_POINT_PORT_16 = "16";

    // ポイント種別
    public final static String MEASURE_POINT_TYPE_1 = "1";
    public final static String MEASURE_POINT_TYPE_2 = "2";
    public final static String MEASURE_POINT_TYPE_3 = "3";
    public final static String MEASURE_POINT_TYPE_4 = "4";

    // 機器ポイント種別
    public final static String TARGET_SM_POINT_TYPE_0 = "0";
    public final static String TARGET_SM_POINT_TYPE_A = "A";
    public final static String TARGET_SM_POINT_TYPE_P = "P";
    public final static String TARGET_SM_POINT_TYPE_K = "K";

    // 機器制御マネージャ Optionフラグ
    public final static int FVP_CTRL_MNG_OPT_OUTPUT_TEMP_FILE = (1 << 0);
    public final static int FVP_CTRL_MNG_OPT_BIT_MASK = (FVP_CTRL_MNG_OPT_OUTPUT_TEMP_FILE);

    /**
     *  メール送信
     */
    /**
     * テンプレートファイル ディレクトリ
     */
    public final static String TEMPLATE_DIR = "/home/wildfly/osol/template";

    /**
     * ファイル出力 ディレクトリ
     */
    public final static String OUTPUT_TEMP_DIR = "/home/wildfly/osol/tempfile";

    /**
     * 一括メール 設定ファイル
     */
    public final static String BULK_MAIL_SETTING = TEMPLATE_DIR.concat("/BulkMail.properties");

    /**
     * 警報メール 設定ファイル
     */
    public final static String ALERT_MAIL_SETTING = TEMPLATE_DIR.concat("/AlertMail.properties");

    /**
     * 日報未収集アラートメール 設定ファイル
     */
    public final static String DAILY_NO_COLLECTED_MAIL_SETTING = TEMPLATE_DIR.concat("/DailyNoCollectedMail.properties");

    /**
     * 日報未収集アラートメール 設定ファイル（件名取得用）
     */
    public final static String DAILY_NO_COLLECTED_MAIL_SUBJECT = "DailyNoCollectedMail.properties";

    /**
     * 一括メール テンプレートファイル
     */
    public final static String BULK_MAIL_BODY = "BulkMailBody.vm";
    public final static String MAIL_TEMPLATE_BODY = "templateBody"; // メールテンプレート内変数(実行者情報)
    public final static String MAIL_RECORD_LIST = "recordList"; // メールテンプレート内変数(レコード情報)
    public final static String MAIL_COMMAND = "command"; // メールテンプレート内変数(コマンド情報)
    public final static String MAIL_TEMPERATURE_CONDITION = "temperatureCondition"; // メールテンプレート内変数(温度設定情報)
    public final static String MAIL_ALL_COUNT = "allCount"; // メールテンプレート内変数(設定件数)
    public final static String MAIL_OK_COUNT = "okCount"; // メールテンプレート内変数(設定OK件数)
    public final static String MAIL_NG_COUNT = "ngCount"; // メールテンプレート内変数(設定NG件数)
    public final static String MAIL_NO_CHANGE_COUNT = "noChangeCount"; // メールテンプレート内変数(未設定件数)

    /**
     * 警報メール テンプレートファイル
     */
    public final static String ALERT_MAIL_BODY = "AlertMailBody.vm";
    public final static String ALERT_HM_MAIL_BODY = "AlertMailBodyHM.vm";

    /**
     * 日報未収集アラートメール テンプレートファイル
     */
    public final static String DAILY_NO_COLLECTED_MAIL_BODY = "DailyNoCollectedMailBody.vm";

    /**
     * 一括機器制御系 最大制御数
     */
    public final static int BULK_CONTROL_MAX = 11;

    /**
     * AielMasterログ収集 ファイル保存ディレクトリ
     */
    public final static String AIELMASTER_COLLECT_LOG_UPLOAD_DIR = "/home/wildfly/osol/AielMaster/";
    public final static String AIELMASTER_COLLECT_LOG_UPLOAD_DIR_WIN = "C:\\home\\wildfly\\osol\\AielMaster\\";

    /**
     * ロガー名称(SmControl用)
     */
    public static enum SMCONTROL_LOGGER_NAME {

        /**
         * 機器通信ログ"SmControlLog"
         */
        SMCONTROL("SmControlLog");

        private final String str;

        private SMCONTROL_LOGGER_NAME(final String str) {
            this.str = str;
        }

        public String getVal() {
            return this.str;
        }
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    /**
     * FVPα用追加基本(取得)Bean
     */
    public final static String ADD_BASIC_FVPA_SELECT = "AddBasicFVPaSelect";

    /**
     * FVPα用追加基本(設定) Bean
     */
    public final static String ADD_BASIC_FVPA_UPDATE = "AddBasicFVPaUpdate";

    /**
     * アンサーバック設定(取得) Bean
     */
    public final static String ANSWER_BACK_SELECT = "AnswerBackSelect";

    /**
     * アンサーバック設定(設定) Bean
     */
    public final static String ANSWER_BACK_UPDATE = "AnswerBackUpdate";

    /**
     * 複数建物・テナント一括 スケジュール(取得) Bean
     */
    public final static String BULK_SCHEDULE_SELECT = "BulkScheduleSelect";

    /**
     * 複数建物・テナント一括 スケジュール(取得)Ea Bean
     */
    public final static String BULK_SCHEDULE_SELECT_EA = "BulkScheduleSelectEa";

    /**
     * 複数建物・テナント一括 スケジュール(設定) Bean
     */
    public final static String BULK_SCHEDULE_UPDATE = "BulkScheduleUpdate";

    /**
     * 複数建物・テナント一括 目標電力(取得) Bean
     */
    public final static String BULK_TARGET_POWER_SELECT = "BulkTargetPowerSelect";

    /**
     * 複数建物・テナント一括 目標電力(取得)Ea Bean
     */
    public final static String BULK_TARGET_POWER_SELECT_EA = "BulkTargetPowerSelectEa";

    /**
     * 複数建物・テナント一括 目標電力(設定) Bean
     */
    public final static String BULK_TARGET_POWER_UPDATE = "BulkTargetPowerUpdate";

    /**
     * 複数建物・テナント一括 温度制御(取得) Bean
     */
    public final static String BULK_TEMPERATURE_CTRL_SELECT = "BulkTemperatureCtrlSelect";

    /**
     * 複数建物・テナント一括 温度制御(設定) Bean
     */
    public final static String BULK_TEMPERATURE_CTRL_UPDATE = "BulkTemperatureCtrlUpdate";

    /**
     * RS485回線異常履歴(取得) Bean
     */
    public final static String CIRCUIT_ERROR_HIST_RS485_SELECT = "CircuitErrorHistRS485Select";

    /**
     * 通信動作モード(取得) Bean
     */
    public final static String COMMUNICATION_OP_MODE_SELECT = "CommunicationOpModeSelect";

    /**
     * 通信動作モード(設定) Bean
     */
    public final static String COMMUNICATION_OP_MODE_UPDATE = "CommunicationOpModeUpdate";

    /**
     * 常時監視(取得) Bean
     */
    public final static String COMMUNICATION_MONITORRING_SELECT = "ContinuousMonitorringSelect";

    /**
     * 常時監視(取得) Bean
     */
    public final static String DAY_REPORT_SELECT = "DayReportSelect";

    /**
     * デマンド任意(取得) Bean
     */
    public final static String DEMAND_OPTION_SELECT = "DemandOptionSelect";

    /**
     * デマンド日報(取得) Bean
     */
    public final static String DEMAND_REPORT_SELECT = "DemandReportSelect";

    /**
     * デマンド(取得) Bean
     */
    public final static String DEMAND_SELECT = "DemandSelect";

    /**
     * デマンド(設定) Bean
     */
    public final static String DEMAND_UPDATE = "DemandUpdate";

    /**
     * デマンドスタート(取得) Bean
     */
    public final static String DEMAND_START_SELECT = "DemandStartSelect";

    /**
     * デマンドスタート(設定) Bean
     */
    public final static String DEMAND_START_UPDATE = "DemandStartUpdate";

    /**
     * 装置情報(取得) Bean
     */
    public final static String DEVICE_INFO_SELECT = "DeviceInfoSelect";

    /**
     * ベント制御履歴(FVP)(取得) Bean
     */
    public final static String EVENT_CTRL_HIST_FVP_SELECT = "EventCtrlHistFVPSelect";

    /**
     * FVx間管理情報(取得) Bean
     */
    public final static String FVX_MANAGE_INFO_SELECT = "FVxManageInfoSelect";

    /**
     * FVx間管理情報(設定) Bean
     */
    public final static String FVX_MANAGE_INFO_UPDATE = "FVxManageInfoUpdate";

    /**
     * 負荷制御履歴(取得) Bean
     */
    public final static String LOAD_CTRL_HIST_SELECT = "LoadCtrlHistSelect";

    /**
     * 負荷制御実績(取得) Bean
     */
    public final static String LOAD_CTRL_RESULT_SELECT = "LoadCtrlResultSelect";

    /**
     * 手動負荷制御(取得) Bean
     */
    public final static String MANUAL_LOAD_CTRL_SELECT = "ManualLoadCtrlSelect";

    /**
     * 手動負荷制御(設定) Bean
     */
    public final static String MANUAL_LOAD_CTRL_UPDATE = "ManualLoadCtrlUpdate";

    /**
     * 最大デマンド(取得) Bean
     */
    public final static String MANUAL_DEMAND_SELECT = "MaxDemandSelect";

    /**
     * 出力端末制御(取得) Bean
     */
    public final static String OUTPUT_TERMINAL_CTRL_SELECT = "OutputTerminalCtrlSelect";

    /**
     * 出力端末制御(設定) Bean
     */
    public final static String OUTPUT_TERMINAL_CTRL_UPDATE = "OutputTerminalCtrlUpdate";

    /**
     * PING(取得) Bean
     */
    public final static String PING_SELECT = "PingSelect";

    /**
     * FOMA電波状態(取得) Bean
     */
    public final static String RADIO_WAVE_STATE_FOMA_SELECT = "RadioWaveStateFOMASelect";

    /**
     * スケジュール(取得) Bean
     */
    public final static String SCHEDULE_SELECT = "ScheduleSelect";

    /**
     * スケジュール(設定) Bean
     */
    public final static String SCHEDULE_UPDATE = "ScheduleUpdate";

    /**
     * 任意電文送信 Bean
     */
    public final static String SEND_ARBITRARY_TELEGRAM = "SendArbitraryTelegram";

    /**
     * イベント制御設定(取得) Bean
     */
    public final static String SETTING_EVENT_CTRL_SELECT = "SettingEventCtrlSelect";

    /**
     * イベント制御設定(設定) Bean
     */
    public final static String SETTING_EVENT_CTRL_UPDATE = "SettingEventCtrlUpdate";

    /**
     * 温度制御履歴(取得) Bean
     */
    public final static String TEMPERATURE_CTRL_HIST_SELECT = "TemperatureCtrlHistSelect";

    /**
     * 温度制御(取得) Bean
     */
    public final static String TEMPERATURE_CTRL_SELECT = "TemperatureCtrlSelect";

    /**
     * 温度制御(設定) Bean
     */
    public final static String TEMPERATURE_CTRL_UPDATE = "TemperatureCtrlUpdate";

    /**
     * 無線温度ロガー履歴(取得) Bean
     */
    public final static String TEMPERATURE_LOGGER_HIST_SELECT = "TemperatureLoggerHistSelect";

    /**
     * ユニット(取得) Bean
     */
    public final static String UNIT_SELECT = "UnitSelect";

    /**
     * ユニット(設定) Bean
     */
    public final static String UNIT_UPDATE = "UnitUpdate";

    /**
     * 無線装置情報(取得) Bean
     */
    public final static String WIRELESS_DEVICE_INFO_SELECT = "WirelessDeviceInfoSelect";

    /**
     * 警報メール送信 Bean
     */
    public final static String ALERM_MAIL_SEND = "AlermMailSend";

    /**
     * 警報警報解除 Bean
     */
    public final static String ALARM_CLEAR = "AlarmClearBean";

    /**
     * 複数建物・テナント一括 制御 Bean
     */
    public final static String BULK_CTRL = "BulkCtrl";

    /**
     * 照明任意(取得) Bean
     */
    public final static String LIGHTING_OPTION_SELECT = "LightingOptionSelect";

    /**
     * デマンド(取得) Eα Bean
     */
    public final static String DEMAND_SELECT_EA = "DemandSelectEa";

    /**
     * デマンド(設定) Eα Bean
     */
    public final static String DEMAND_UPDATE_EA = "DemandUpdateEa";

    /**
     * 祝日(取得) Bean
     */
    public final static String NATIONAL_HOLIDAY_SELECT = "NationalHolidaySelect";

    /**
     * 祝日(設定) Bean
     */
    public final static String NATIONAL_HOLIDAY_UPDATE = "NationalHolidayUpdate";

    /**
     * ユニット(取得) Eα Bean
     */
    public final static String UNIT_SELECT_EA = "UnitSelectEa";

    /**
     * ユニット(設定) Eα Bean
     */
    public final static String UNIT_UPDATE_EA = "UnitUpdateEa";

    /**
     * 計測ポイント(取得) Bean
     */
    public final static String MEASURE_POINT_SELECT = "MeasurePointSelect";

    /**
     * 計測ポイント(設定) Bean
     */
    public final static String MEASURE_POINT_UPDATE = "MeasurePointUpdate";

    /**
     * 出力ポイント(取得) Bean
     */
    public final static String OUTPUT_POINT_SELECT = "OutputPointSelect";

    /**
     * 出力ポイント(設定) Bean
     */
    public final static String OUTPUT_POINT_UPDATE = "OutputPointUpdate";

    /**
     * 動作モード(取得) Bean
     */
    public final static String ACTION_SETTING_SELECT = "ActionSettingSelect";

    /**
     * 動作モード(設定) Bean
     */
    public final static String ACTION_SETTING_UPDATE = "ActionSettingUpdate";

    /**
     * SmartLEDZ情報(取得) Bean
     */
    public final static String SMART_LEDZ_INFO_SELECT = "SmartLedzInfoSelect";

    /**
     * SmartLEDZ情報(設定) Bean
     */
    public final static String SMART_LEDZ_INFO_UPDATE = "SmartLedzInfoUpdate";

    /**
     * スケジュール(取得) Eα Bean
     */
    public final static String SCHEDULE_SELECT_EA = "ScheduleSelectEa";

    /**
     * スケジュール(設定) Eα Bean
     */
    public final static String SCHEDULE_UPDATE_EA = "ScheduleUpdateEa";

    /**
     * スケジュールパターン(取得) Bean
     */
    public final static String SCHEDULE_PATTERN_SELECT = "SchedulePatternSelect";

    /**
     * スケジュールパターン(設定) Bean
     */
    public final static String SCHEDULE_PATTERN_UPDATE = "SchedulePatternUpdate";

    /**
     * AielMasterスケジュール(取得) Bean
     */
    public final static String AIELMASTER_SCHEDULE_SELECT = "AielMasterScheduleSelect";


    /**
     * AielMasterスケジュール(設定) Bean
     */
    public final static String AIELMASTER_SCHEDULE_UPDATE = "AielMasterScheduleUpdate";

    /**
     * AielMaster店舗設定(取得) Bean
     */
    public final static String AIELMASTER_STORECONFIG_SELECT = "AielMasterStoreConfigSelect";


    /**
     * AielMaster店舗設定(設定) Bean
     */
    public final static String AIELMASTER_STORECONFIG_UPDATE = "AielMasterStoreConfigUpdate";

    /**
     * AielMasterエリア設定(取得) Bean
     */
    public final static String AIELMASTER_AREA_CONFIG_SELECT = "AielMasterAreaConfigSelect";


    /**
     * AielMasterエリア設定(設定) Bean
     */
    public final static String AIELMASTER_AREA_CONFIG_UPDATE = "AielMasterAreaConfigUpdate";

    /**
     * AielMasterログ収集 Bean
     */
    public final static String AIELMASTER_LOG_COLLECT = "AielMasterLogCollect";

    /**
     * AielMaster不快指数基準値設定（取得）
     */
    public final static String AIELMASTER_DISCOMFORT_INDEX_STANDARD_SELECT = "AielMasterDiscomfortIndexStandardSelect";

    /**
     * AielMaster不快指数基準値設定（設定）
     */
    public final static String AIELMASTER_DISCOMFORT_INDEX_STANDARD_UPDATE = "AielMasterDiscomfortIndexStandardUpdate";

    /**
     * 日報未収集アラートメール Bean
     */
    public final static String DAILY_NO_COLLECTED_MAIL_SEND = "DailyNoCollectedMailSend";

}
