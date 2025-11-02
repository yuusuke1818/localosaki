package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.result.utility.CommonDemandControlTimeTableResult;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 検証：デマンド制御用 TimeTable Resultクラス テストデータクラス
 * @author ya-ishida
 *
 */
public final class CommonDemandControlTimeTableResultTestData
        extends AbstractDemandVerifyUtilityTestData<CommonDemandControlTimeTableResult> {

    /**
     * 1件
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getNotEmptySingleSetResult() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * 2件以上
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getNotEmptyMultiSetResult() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812180015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812180115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812181215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201812181315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812180015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812180115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812181215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201812181315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン1_1結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern1_1() {
        return getEmptySet();
    }

    /**
     * テストパターン1_2結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern1_2() {
        return getEmptySet();
    }

    /**
     * テストパターン1_3結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern1_3() {
        return getEmptySet();
    }

    /**
     * テストパターン1_4結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern1_4() {
        return getEmptySet();
    }

    /**
     * テストパターン2_1結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_1() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_2結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_2() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220124530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_3結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_3() {
        return getEmptySet();
    }

    /**
     * テストパターン2_4結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_4() {
        return getEmptySet();
    }

    /**
     * テストパターン2_5結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_5() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_6結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_6() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220140000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_7結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_7() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_8結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_8() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220124530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220140000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_9結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_9() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_10結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_10() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220124530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220130000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220140000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_11結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_11() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("28"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テストパターン2_12結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTestPattern2_12() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220124530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220130000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220133000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("28"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220141500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220143000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220144530", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン1_1結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern1_1() {
        return getEmptySet();
    }

    /**
     * テスト017パターン1_2結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern1_2() {
        return getEmptySet();
    }

    /**
     * テスト017パターン1_3結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern1_3() {
        return getEmptySet();
    }

    /**
     * テスト017パターン1_4結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern1_4() {
        return getEmptySet();
    }

    /**
     * テスト017パターン2_1
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_1() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_1結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_1() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_2
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_2() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_2結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_2() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_3
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_3() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_3結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_3() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_4
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_4() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_4結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_4() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_5
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_5() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_5結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_5() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("33"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("34"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("34"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("35"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("6"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("37"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("38"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("38"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("39"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("41"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("42"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("42"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("43"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("14"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("14"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("45"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("46"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("46"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("47"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("18"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("18"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("2"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("2"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("201902210100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("22"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("22"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("6"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("6"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("201902210300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("26"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("201902210500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("30"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("14"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("14"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("201902210700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_6
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_6() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_6結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_6() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220160030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("33"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220163000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("34"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220164500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("34"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("20190220170030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("35"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220023000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("6"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220180030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("37"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220183000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("38"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220184500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("38"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("20190220190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("39"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220043000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("41"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220203000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("42"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220204500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("42"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("20190220210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("43"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220063000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("14"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220064500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("14"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220220030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("45"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220223000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("46"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220224500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("46"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("20190220230030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("47"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220080030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("17"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220083000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("18"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220084500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("18"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190220090030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("19"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221000030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221003000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("2"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221004500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("2"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("5"),
                DateUtility.conversionDate("20190221010030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("3"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220100030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("21"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220103000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("22"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220104500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("22"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190220110030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("23"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221020030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("5"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221023000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("6"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221024500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("6"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("6"),
                DateUtility.conversionDate("20190221030030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("7"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220120030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("25"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220123000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220124500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("26"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190220130030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("27"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221040030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("9"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221043000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221044500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS), BigDecimal.TEN,
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("7"),
                DateUtility.conversionDate("20190221050030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("11"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220140030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("29"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220143000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220144500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("30"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190220150030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("31"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221060030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("13"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221063000", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("14"), ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221064500", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("14"), ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("8"),
                DateUtility.conversionDate("20190221070030", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS),
                new BigDecimal("15"), ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }

    /**
     * テスト017パターン2_7
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getTest017_2_7() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("3"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("5"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("7"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("11"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("15"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("21"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));

        return result;
    }

    /**
     * テスト017パターン2_7結果
     * @return
     */
    public LinkedHashSet<CommonDemandControlTimeTableResult> getResultTest017Pattern2_7() {
        LinkedHashSet<CommonDemandControlTimeTableResult> result = new LinkedHashSet<>();

        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("1"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("9"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("12"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("13"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902200730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("16"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("2"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("17"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("19"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902200930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("20"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("23"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("3"),
                DateUtility.conversionDate("201902201130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("24"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("25"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("27"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("28"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201345", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("28"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("29"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("31"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("32"),
                ApiSimpleConstants.LOAD_CONTROL_FREE));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902201545", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), new BigDecimal("32"),
                ApiSimpleConstants.LOAD_CONTROL_CUT));
        result.add(new CommonDemandControlTimeTableResult(Integer.valueOf("4"),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM), BigDecimal.ONE,
                ApiSimpleConstants.LOAD_CONTROL_FREE));

        return result;
    }
}
