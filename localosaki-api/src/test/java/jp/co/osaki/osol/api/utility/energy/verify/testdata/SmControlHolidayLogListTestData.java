package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayLogListDetailResultData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御祝日設定履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlHolidayLogListTestData
        extends AbstractDemandVerifyUtilityTestData<SmControlHolidayLogListDetailResultData> {

    /**
     * 1件のList
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getNotEmptySingleList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812171213", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        return list;
    }

    /**
     * 2件のList
     * @param secondFromFlg 2件目がsettingUpdateDateTimeFromかどうか？
     * @param settingUpdateDateTimeFrom
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getNotEmptyMultiList(Boolean secondFromFlg,
            Timestamp settingUpdateDateTimeFrom) {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(DateUtility.plusHour(new Date(settingUpdateDateTimeFrom.getTime()), -1).getTime()),
                Integer.valueOf("0")));
        if (secondFromFlg) {
            list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                    settingUpdateDateTimeFrom, Integer.valueOf("0")));
        } else {
            list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                    new Timestamp(DateUtility.plusHour(new Date(settingUpdateDateTimeFrom.getTime()), 1).getTime()),
                    Integer.valueOf("0")));
        }
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(DateUtility.plusDay(new Date(settingUpdateDateTimeFrom.getTime()), 2).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付やスケジュールとバッティングしないデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getFreeList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812011500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812031500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付とバッティングするデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getDateConflictList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812030000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * スケジュールとバッティングするデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getScheduleConflictList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812011500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812031500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付とスケジュールとバッティングするデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getAllConflictList() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812030000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071500", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812080000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812090000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812100000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_2テストデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getTest015_2_2() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_7テストデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getTest015_2_7() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));
        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_8テストデータ
     * @return
     */
    public List<SmControlHolidayLogListDetailResultData> getTest015_2_8() {
        List<SmControlHolidayLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                Integer.valueOf("0")));

        return list;
    }
}
