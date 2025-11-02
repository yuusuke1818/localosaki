package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.util.List;

/**
 * アズビルサーバ計測値送信 Dtoクラス
 *
 * @author akr_iwamoto
 *
 */
public class OcrServerGetMeterListResDto {

    /** 処理結果コード */
    private int result = 0;

    /** HTTPレスポンスコード */
    private int httpResponseCode = 0;

    /** エラー情報 */
    private String errorMessage = null; // null:正常

    // -----

    /** 結果値 0:成功. */
    private String code;    // Number

    /** メッセージ. */
    private String msg;     // String

    private List<Data> data = null; // null:取得失敗  0件:該当データなし

    public class Data {
        /** OCRカメラID. */
        private String device_id; // Number

        /** OCRメータ状態  ※2:未初期化  3:初期化済  4:稼働中  5:インアクティブ  6:校正中. */
        private String meter_status; // Number

        /** 通信規格  ※1:NBIoT  2:Cat.M1  3:ZETA  4:WiSUN  5:LoRaWAN. */
        private String comm_standard; // String

        /** 施設・エリア名称 (装置名称、接続先名称). */
        private String facility_name; // String [optional]

        /** 計量種別の名称. */
        private String weighing_type_name; // String [optional]

        /** 計量種別の単位. */
        private String weighing_type_unit; // String [optional]

        /** 電池残量 0-100. */
        private String battery_level; // Number

        /** 電池電圧 1.0-3.6. */
        private String battery_voltage; // Float

        /** 当月の通信量(総量). */
        private String net_flow; // Number

        /** 当月の通信量(上行). */
        private String net_flow_up; // Number

        /** 当月の通信量(下行). */
        private String net_flow_down; // Number

        /** 管理番号 (メーターID). */
        private String manage_number; // String [optional]

        /** 管理名称. */
        private String manage_name; // String [optional]

        /** 最後検針値. */
        private String last_reading_value; // Number [optional]

        /** 最後検針時間  ※YYYY-MM-DD HH:MI:SS. */
        private String last_reading_time; // String [optional]

        /** 前回使用量. */
        private String pre_use_amount; // Number [optional]

        /** 今回使用量. */
        private String cur_use_amount; // Number [optional]

        /** 証拠写真. */
        private String proof_image; // String [optional]

        /** 電池異常  ※0: 異常なし  1: 異常ある. */
        private String abnormal_battery; // Number [optional]

        /** 使用量異常  ※0: 異常なし  1: 異常ある. */
        private String abnormal_usage; // Number [optional]

        /** 前回比異常  ※0: 異常なし  1: 異常ある. */
        private String abnormal_ratio; // Number [optional]

        /** 検満年月  ※書式:yyyy-mm. */
        private String expiration_date;

        /**
         * @return device_id
         */
        public String getDevice_id() {
            return device_id;
        }

        /**
         * @param device_id セットする device_id
         */
        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        /**
         * @return meter_status
         */
        public String getMeter_status() {
            return meter_status;
        }

        /**
         * @param meter_status セットする meter_status
         */
        public void setMeter_status(String meter_status) {
            this.meter_status = meter_status;
        }

        /**
         * @return comm_standard
         */
        public String getComm_standard() {
            return comm_standard;
        }

        /**
         * @param comm_standard セットする comm_standard
         */
        public void setComm_standard(String comm_standard) {
            this.comm_standard = comm_standard;
        }

        /**
         * @return facility_name
         */
        public String getFacility_name() {
            return facility_name;
        }

        /**
         * @param facility_name セットする facility_name
         */
        public void setFacility_name(String facility_name) {
            this.facility_name = facility_name;
        }

        /**
         * @return weighing_type_name
         */
        public String getWeighing_type_name() {
            return weighing_type_name;
        }

        /**
         * @param weighing_type_name セットする weighing_type_name
         */
        public void setWeighing_type_name(String weighing_type_name) {
            this.weighing_type_name = weighing_type_name;
        }

        /**
         * @return weighing_type_unit
         */
        public String getWeighing_type_unit() {
            return weighing_type_unit;
        }

        /**
         * @param weighing_type_unit セットする weighing_type_unit
         */
        public void setWeighing_type_unit(String weighing_type_unit) {
            this.weighing_type_unit = weighing_type_unit;
        }

        /**
         * @return battery_level
         */
        public String getBattery_level() {
            return battery_level;
        }

        /**
         * @param battery_level セットする battery_level
         */
        public void setBattery_level(String battery_level) {
            this.battery_level = battery_level;
        }

        /**
         * @return battery_voltage
         */
        public String getBattery_voltage() {
            return battery_voltage;
        }

        /**
         * @param battery_voltage セットする battery_voltage
         */
        public void setBattery_voltage(String battery_voltage) {
            this.battery_voltage = battery_voltage;
        }

        /**
         * @return net_flow
         */
        public String getNet_flow() {
            return net_flow;
        }

        /**
         * @param net_flow セットする net_flow
         */
        public void setNet_flow(String net_flow) {
            this.net_flow = net_flow;
        }

        /**
         * @return net_flow_up
         */
        public String getNet_flow_up() {
            return net_flow_up;
        }

        /**
         * @param net_flow_up セットする net_flow_up
         */
        public void setNet_flow_up(String net_flow_up) {
            this.net_flow_up = net_flow_up;
        }

        /**
         * @return net_flow_down
         */
        public String getNet_flow_down() {
            return net_flow_down;
        }

        /**
         * @param net_flow_down セットする net_flow_down
         */
        public void setNet_flow_down(String net_flow_down) {
            this.net_flow_down = net_flow_down;
        }

        /**
         * @return manage_number
         */
        public String getManage_number() {
            return manage_number;
        }

        /**
         * @param manage_number セットする manage_number
         */
        public void setManage_number(String manage_number) {
            this.manage_number = manage_number;
        }

        /**
         * @return manage_name
         */
        public String getManage_name() {
            return manage_name;
        }

        /**
         * @param manage_name セットする manage_name
         */
        public void setManage_name(String manage_name) {
            this.manage_name = manage_name;
        }

        /**
         * @return last_reading_value
         */
        public String getLast_reading_value() {
            return last_reading_value;
        }

        /**
         * @param last_reading_value セットする last_reading_value
         */
        public void setLast_reading_value(String last_reading_value) {
            this.last_reading_value = last_reading_value;
        }

        /**
         * @return last_reading_time
         */
        public String getLast_reading_time() {
            return last_reading_time;
        }

        /**
         * @param last_reading_time セットする last_reading_time
         */
        public void setLast_reading_time(String last_reading_time) {
            this.last_reading_time = last_reading_time;
        }

        /**
         * @return pre_use_amount
         */
        public String getPre_use_amount() {
            return pre_use_amount;
        }

        /**
         * @param pre_use_amount セットする pre_use_amount
         */
        public void setPre_use_amount(String pre_use_amount) {
            this.pre_use_amount = pre_use_amount;
        }

        /**
         * @return cur_use_amount
         */
        public String getCur_use_amount() {
            return cur_use_amount;
        }

        /**
         * @param cur_use_amount セットする cur_use_amount
         */
        public void setCur_use_amount(String cur_use_amount) {
            this.cur_use_amount = cur_use_amount;
        }

        /**
         * @return proof_image
         */
        public String getProof_image() {
            return proof_image;
        }

        /**
         * @param proof_image セットする proof_image
         */
        public void setProof_image(String proof_image) {
            this.proof_image = proof_image;
        }

        /**
         * @return abnormal_battery
         */
        public String getAbnormal_battery() {
            return abnormal_battery;
        }

        /**
         * @param abnormal_battery セットする abnormal_battery
         */
        public void setAbnormal_battery(String abnormal_battery) {
            this.abnormal_battery = abnormal_battery;
        }

        /**
         * @return abnormal_usage
         */
        public String getAbnormal_usage() {
            return abnormal_usage;
        }

        /**
         * @param abnormal_usage セットする abnormal_usage
         */
        public void setAbnormal_usage(String abnormal_usage) {
            this.abnormal_usage = abnormal_usage;
        }

        /**
         * @return abnormal_ratio
         */
        public String getAbnormal_ratio() {
            return abnormal_ratio;
        }

        /**
         * @param abnormal_ratio セットする abnormal_ratio
         */
        public void setAbnormal_ratio(String abnormal_ratio) {
            this.abnormal_ratio = abnormal_ratio;
        }

        public String getExpiration_date() {
            return expiration_date;
        }

        public void setExpiration_date(String expiration_date) {
            this.expiration_date = expiration_date;
        }
    }

    /**
     * @return errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage セットする errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return result
     */
    public int getResult() {
        return result;
    }

    /**
     * @param result セットする result
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * @return httpResponseCode
     */
    public int getHttpResponseCode() {
        return httpResponseCode;
    }

    /**
     * @param httpResponseCode セットする httpResponseCode
     */
    public void setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }

    /**
     * @return data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * @param data セットする data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code セットする code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg セットする msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
