package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御スケジュール履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlScheduleLogListSortTestData
        extends AbstractDemandVerifyUtilityListSortTestData<SmControlScheduleLogListDetailResultData> {

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyInputList()
     */
    @Override
    public List<SmControlScheduleLogListDetailResultData> getNotEmptyInputList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_OFF, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121315", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));

        return list;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyAscSortList()
     */
    @Override
    public List<SmControlScheduleLogListDetailResultData> getNotEmptyAscSortList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_OFF, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121315", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));

        return list;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyDescSortList()
     */
    @Override
    public List<SmControlScheduleLogListDetailResultData> getNotEmptyDescSortList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121315", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_ON, Integer.parseInt("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                OsolConstants.FLG_OFF, Integer.parseInt("0")));

        return list;
    }

}
