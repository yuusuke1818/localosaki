package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 機器制御スケジュール履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlScheduleLogListTestData
        extends AbstractDemandVerifyUtilityTestData<SmControlScheduleLogListDetailResultData> {

    /**
     * 1件のList
     * @param manageDesignationFlg スケジュール管理指定有効フラグ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getNotEmptySingleList(Integer manageDesignationFlg) {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), new Timestamp(
                DateUtility.conversionDate("201812171213", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                manageDesignationFlg, Integer.valueOf("0")));
        return list;
    }

    /**
     * 2件のList
     * @param secondFromFlg 2件目がsettingUpdateDateTimeFromかどうか？
     * @param settingUpdateDateTimeFrom
     * @param スケジュール管理指定有効フラグ FLGOFFの場合、1件だけOFFにする
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getNotEmptyMultiList(Boolean secondFromFlg,
            Timestamp settingUpdateDateTimeFrom, Integer manageDesignationFlg) {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(DateUtility.plusHour(new Date(settingUpdateDateTimeFrom.getTime()), -1).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));

        if (secondFromFlg) {
            list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                    settingUpdateDateTimeFrom, OsolConstants.FLG_ON, Integer.valueOf("0")));
        } else {
            list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                    new Timestamp(DateUtility.plusHour(new Date(settingUpdateDateTimeFrom.getTime()), 1).getTime()),
                    OsolConstants.FLG_ON, Integer.valueOf("0")));
        }
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(DateUtility.plusDay(new Date(settingUpdateDateTimeFrom.getTime()), 2).getTime()),
                manageDesignationFlg, Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付や祝日とバッティングしないデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getFreeList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812011400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812031400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付とバッティングするデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getDateConflictList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812030000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));

        return list;
    }

    /**
     * 祝日とバッティングするデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getHolidayConflictList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812011400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812031400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812081600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812091600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812101600", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));

        return list;
    }

    /**
     * 日付と祝日とバッティングするデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getAllConflictList() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201801021400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812020000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("4"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812030000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("5"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812041400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("6"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812051400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("7"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812061400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("8"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812071400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("9"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812080000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("10"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812090000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("11"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201812100000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                OsolConstants.FLG_ON, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_1テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_1() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_2テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_2() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_3テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_3() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_4テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_4() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902191000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_5テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_5() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));
        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new Timestamp(
                        DateUtility.conversionDate("201902190830", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_6テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_6() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_7テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_7() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_8テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_8() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_9テストデータ
     * @return
     */
    public List<SmControlScheduleLogListDetailResultData> getTest015_2_9() {
        List<SmControlScheduleLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleLogListDetailResultData(Long.valueOf("1"), Long.valueOf("1"),
                new Timestamp(
                        DateUtility.conversionDate("201902010000", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime()),
                1, Integer.valueOf("0")));

        return list;
    }

}
