package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.util.LinkedHashSet;

import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeCurrentNextResult;

/**
 * スケジュールパターンNo変更トリガのcurrentとnextを格納するResultクラス テストデータ
 * @author ya-ishida
 *
 */
public final class CommonSchedulePatternChangeCurrentNextResultTestData
        extends AbstractDemandVerifyUtilityTestData<CommonSchedulePatternChangeCurrentNextResult> {

    /**
     * Test010_1_1の結果
     * @return
     */
    public LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> getResultTest010_1_1() {
        return getEmptySet();
    }

    /**
     * Test010_2_1の結果
     * @return
     */
    public LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> getResultTest010_2_1() {
        return getEmptySet();
    }

    /**
     * Test010_3_1の結果
     * @return
     */
    public LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> getResultTest010_3_1() {
        LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> result = new LinkedHashSet<>();

//        CommonSchedulePatternChangeCurrentNextResult detail = new CommonSchedulePatternChangeCurrentNextResult();
        //        detail.setCurrentSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812171400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, null, null, Long.valueOf("1")));

        //        detail.setNextSchedulePatternChangeResult(null);
        //
        //        result.add(detail);

        return result;
    }

    /**
     * Test010_4_1の結果
     * @return
     */
    public LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> getResultTest010_4_1() {
        LinkedHashSet<CommonSchedulePatternChangeCurrentNextResult> result = new LinkedHashSet<>();

        CommonSchedulePatternChangeCurrentNextResult detail = new CommonSchedulePatternChangeCurrentNextResult();
        //        detail.setCurrentSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812011234", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
        //                DateUtility.conversionDate(DateUtility.changeDateFormat(
        //                        DateUtility.conversionDate("201812011234", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
        //                        DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD),
        //                null, null));
        //        detail.setNextSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
        //                DateUtility.conversionDate(DateUtility.changeDateFormat(
        //                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
        //                        DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD),
        //                null, null));

        result.add(detail);

        detail = new CommonSchedulePatternChangeCurrentNextResult();

        //        detail.setCurrentSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
        //                DateUtility.conversionDate(DateUtility.changeDateFormat(
        //                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM),
        //                        DateUtility.DATE_FORMAT_YYYYMMDD), DateUtility.DATE_FORMAT_YYYYMMDD),
        //                null, null));
        //
        //        detail.setNextSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812181213", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                null, null, null, null, null, null));

        result.add(detail);

        detail = new CommonSchedulePatternChangeCurrentNextResult();

        //        detail.setCurrentSchedulePatternChangeResult(new CommonSchedulePatternChangeResult(
        //                new Timestamp(
        //                        DateUtility.conversionDate("201812181213", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
        //                null, null, null, null, null, null));

        detail.setNextSchedulePatternChangeResult(null);

        result.add(detail);

        return result;
    }

}
