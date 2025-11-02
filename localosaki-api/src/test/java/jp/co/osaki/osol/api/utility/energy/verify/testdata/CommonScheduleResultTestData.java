package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

import jp.co.osaki.osol.api.result.utility.CommonScheduleResult;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * スケジュールの開始時間、終了時間をまとめるResultクラス テストデータ
 * @author ya-ishida
 *
 */
public final class CommonScheduleResultTestData extends AbstractDemandVerifyUtilityTestData<CommonScheduleResult> {

    /**
     * 1件
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getNotEmptySingleSet() {

        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * 2件以上
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getNotEmptyMultiSet() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201812181215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812181315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201812180015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812180115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201812181215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812181315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201812171215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812171315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201812180015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201812180115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * テストパターン1_1結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getResultTestPattern1_1() {
        return getEmptySet();
    }

    /**
     * テストパターン1_2結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getResultTestPattern1_2() {
        return getEmptySet();
    }

    public LinkedHashSet<CommonScheduleResult> getResultTestPattern2_2() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        return result;
    }

    /**
     * Test015_2_1結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_1() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_2結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_2() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_3結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_3() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902201615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902201815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902200830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902201030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902201230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_4結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_4() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_5結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_5() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_6結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_6() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902201615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902201815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902200830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902201030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902201230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902202230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902192230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902200600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902200800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902202000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902202200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_7結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_7() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_8結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_8() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test015_2_8結果
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest015_2_9() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902191600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902190015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902190815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902191015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191515", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191615", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191715", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902191815", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191915", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192015", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192115", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902192215", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902192315", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902190630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902191430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902190600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902190800", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902190900", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902191400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902191500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_3
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_3() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_4
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_4() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_5
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_5() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_6
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_6() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_7
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_7() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_8
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_8() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_9
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_9() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_10
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_10() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_11
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_11() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test016_2_12
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest016_2_12() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201415", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test017_2_5
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest017_2_5() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902200430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902200830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902201030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902202230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902210230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902210430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902210630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test017_2_6
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest017_2_6() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902200430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902200630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902200830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902201030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902201230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902201430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902201630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902201830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201845", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902202030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902202230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902202245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("5"),
                DateUtility.conversionDate("201902210030", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210045", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("6"),
                DateUtility.conversionDate("201902210230", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210245", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("7"),
                DateUtility.conversionDate("201902210430", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210445", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("8"),
                DateUtility.conversionDate("201902210630", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210645", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

    /**
     * Test017_2_7
     * @return
     */
    public LinkedHashSet<CommonScheduleResult> getTest017_2_7() {
        LinkedHashSet<CommonScheduleResult> result = new LinkedHashSet<>();

        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200100", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200200", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(BigDecimal.ONE,
                DateUtility.conversionDate("201902200300", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("2"),
                DateUtility.conversionDate("201902200700", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902200730", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902200930", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("3"),
                DateUtility.conversionDate("201902201130", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902210000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902201330", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201345", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));
        result.add(new CommonScheduleResult(new BigDecimal("4"),
                DateUtility.conversionDate("201902201530", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
                DateUtility.conversionDate("201902201545", DateUtility.DATE_FORMAT_YYYYMMDDHHMM)));

        return result;
    }

}
