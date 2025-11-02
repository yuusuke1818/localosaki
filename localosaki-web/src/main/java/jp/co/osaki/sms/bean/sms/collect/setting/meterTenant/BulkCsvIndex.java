package jp.co.osaki.sms.bean.sms.collect.setting.meterTenant;

public enum BulkCsvIndex {
    USER_CODE(0),
    NO(1),
    NAME(2),
    KANA(3),
    CONTRACTION(4),
    ZIP_CODE(5),
    PREFECTURE(6),
    ADDRESS(7),
    ADDR_BLDG(8),
    TEL_NUM(9),
    FAX_NUM(10),
    BIKO(11),
    START_YM(12),
    END_YM(13),
    PUBLIC_FLG(14),
    FIXED1_NAME(15),
    FIXED1_PRICE(16),
    FIXED2_NAME(17),
    FIXED2_PRICE(18),
    FIXED3_NAME(19),
    FIXED3_PRICE(20),
    FIXED4_NAME(21),
    FIXED4_PRICE(22),
    PRICE_MENU(23),
    DIV_RATE1(24),
    DIV_RATE2(25),
    DIV_RATE3(26),
    DIV_RATE4(27),
    DIV_RATE5(28),
    DIV_RATE6(29),
    DIV_RATE7(30),
    DIV_RATE8(31),
    DIV_RATE9(32),
    DIV_RATE10(33);

    private BulkCsvIndex(int index) {
        this.index = index;
    }

    private int index;

    public int getIndex() {
        return index;
    }

}
