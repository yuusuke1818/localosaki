package jp.co.osaki.sms.bean.sms.collect.setting.meter;

public class MeterBulkCsvIndex {

    /**
     * スマートメーターのCSVファイルINDEX
     */
    public static enum SMART_METER_BULK_CSV_INDEX {
        UPDATE_KBN(0),
        METER_MNG_ID(1),
        METER_ID(2),
        METER_TYPE(3),
        TENANT_ID(4),
        SEND_DEVICE(5),
        OPEN_MODE(6),
        LOADLIMIT_MODE(7),
        LOAD_CURRENT(8),
        AUTO_INJECTION(9),
        BREAKER_ACT_COUNT(10),
        COUNT_CLEAR(11),
        TEMP_LOAD_CURRENT(12),
        TEMP_AUTO_INJECTION(13),
        TEMP_BREAKER_ACT_COUNT(14),
        TEMP_COUNT_CLEAR(15),
        DISP_YEAR_FLG(16),
        EXAM_END_YM(17),
        EXAM_NOTICE(18),
        MEMO(19);

        private SMART_METER_BULK_CSV_INDEX(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }
    }

    /**
     * パルスメーターのCSVファイルINDEX
     */
    public static enum PULSE_METER_BULK_CSV_INDEX {
        UPDATE_KBN(0),
        METER_MNG_ID(1),
        METER_ID(2),
        METER_TYPE(3),
        TENANT_ID(4),
        SEND_DEVICE(5),
        MULTI(6),
        PULSE_TYPE_CHG(7),
        PULSE_TYPE(8),
        PULSE_WEIGHT_CHG(9),
        PULSE_WEIGHT(10),
        CURRENT_DATA_CHG(11),
        CURRENT_DATA(12),
        DISP_YEAR_FLG(13),
        EXAM_END_YM(14),
        EXAM_NOTICE(15),
        MEMO(16);

        private PULSE_METER_BULK_CSV_INDEX(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }
    }

    /**
     * Iot-R連携用メーターのCSVファイルINDEX
     */
    public static enum IOTR_METER_BULK_CSV_INDEX {
        UPDATE_KBN(0),
        METER_MNG_ID(1),
        METER_ID(2),
        METER_TYPE(3),
        TENANT_ID(4),
        DISP_YEAR_FLG(5),
        EXAM_END_YM(6),
        EXAM_NOTICE(7),
        MEMO(8);

        private IOTR_METER_BULK_CSV_INDEX(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }
    }

    /**
     * ハンディ検針用メーターのCSVファイルINDEX
     */
    public static enum HANDY_METER_BULK_CSV_INDEX {
        UPDATE_KBN(0),
        METER_MNG_ID(1),
        METER_ID(2),
        METER_TYPE(3),
        TENANT_ID(4),
        MULTI(5),
        WIRELESS_TYPE(6),
        WIRELESS_ID(7),
        HOP1_ID(8),
        HOP2_ID(9),
        HOP3_ID(10),
        POLLING_ID(11),
        DISP_YEAR_FLG(12),
        EXAM_END_YM(13),
        EXAM_NOTICE(14),
        MEMO(15);

        private HANDY_METER_BULK_CSV_INDEX(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }
    }

    /**
     * AieLink用メーターのCSVファイルINDEX
     * 「OCR検針」→「AieLink」へ変更
     */
    public static enum OCR_METER_BULK_CSV_INDEX {
        UPDATE_KBN(0),
        METER_MNG_ID(1),
        METER_ID(2),
        METER_TYPE(3),
        TENANT_ID(4),
        MULTI(5),
        DISP_YEAR_FLG(6),
        EXAM_NOTICE(7),
        MEMO(8);

        private OCR_METER_BULK_CSV_INDEX(int index) {
            this.index = index;
        }

        private int index;

        public int getIndex() {
            return index;
        }
    }
}
