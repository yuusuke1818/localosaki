package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御祝日設定履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlHolidayLogListSortTestData
        extends AbstractDemandVerifyUtilityListSortTestData<SmControlHolidayLogListDetailResultData> {

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyInputList()
     */
    @Override
    public List<SmControlHolidayLogListDetailResultData> getNotEmptyInputList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121313", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyAscSortList()
     */
    @Override
    public List<SmControlHolidayLogListDetailResultData> getNotEmptyAscSortList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121313", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.utility.energy.verify.AbstractDemandVerifyUtilityListTestData#getNotEmptyDescSortList()
     */
    @Override
    public List<SmControlHolidayLogListDetailResultData> getNotEmptyDescSortList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121314", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121313", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("20181217121312", DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS).getTime()),
                Integer.valueOf("0")));

        return list;

    }

}
