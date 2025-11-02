package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleSetLogListDetailResultData;

/**
 * 機器制御スケジュール設定履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlScheduleSetLogListTestData
        extends AbstractDemandVerifyUtilityTestData<SmControlScheduleSetLogListDetailResultData> {

    /**
     * すべて有効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getAllValidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 特定日のパターンNoのみ無効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getOnlySpecificPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
    * 祝日のパターンNoのみ無効なデータ
    * @return
    */
    public List<SmControlScheduleSetLogListDetailResultData> getOnlyHolidayPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 曜日のパターンNoのみ無効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getOnlyWeekDayPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "08", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 特定日と祝日のパターンNoが無効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getSpecificAndHolidayPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "01", "02", "03", "04", "05", "06", "07", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * 祝日と曜日のパターンNoが無効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getHolidayAndWeekDayPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * すべてのパターンNoが無効なデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getAllPatternNoInvalidData() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                BigDecimal.ONE, "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("2"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("3"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("4"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"),
                new BigDecimal("5"), "00", "00", "00", "00", "00", "00", "00", "00", Integer.valueOf("1"),
                Integer.valueOf("2"), Integer.valueOf("3"), Integer.valueOf("4"), Integer.valueOf("5"),
                Integer.valueOf("6"), Integer.valueOf("7"), Integer.valueOf("8"), Integer.valueOf("9"),
                Integer.valueOf("10"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_1 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_1() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_2 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_2() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_3 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_3() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_4 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_4() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("19"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "01", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "02", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "03", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "04", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "05", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "06", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "07", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "08", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_5 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_5() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("19"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "01", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "02", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "03", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "04", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "05", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "06", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "07", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("19"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "08", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_6 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_6() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_7 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_7() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_8 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_8() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_9 テストデータ
     * @return
     */
    public List<SmControlScheduleSetLogListDetailResultData> getTest015_2_9() {
        List<SmControlScheduleSetLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), BigDecimal.ONE,
                new BigDecimal("2"), "01", "02", "03", "04", "05", "06", "07", "08", Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("2"), new BigDecimal("2"), "02", "03", "04", "05", "06", "07", "08", "01",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("3"), new BigDecimal("2"), "03", "04", "05", "06", "07", "08", "01", "02",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("4"), new BigDecimal("2"), "04", "05", "06", "07", "08", "01", "02", "03",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("5"), new BigDecimal("2"), "05", "06", "07", "08", "01", "02", "03", "04",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("6"), new BigDecimal("2"), "06", "07", "08", "01", "02", "03", "04", "05",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("7"), new BigDecimal("2"), "07", "08", "01", "02", "03", "04", "05", "06",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));
        list.add(new SmControlScheduleSetLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"),
                new BigDecimal("8"), new BigDecimal("2"), "08", "01", "02", "03", "04", "05", "06", "07",
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"), Integer.valueOf("0"),
                Integer.valueOf("0"), Integer.valueOf("0"), "00", "00", "00", "00", "00", "00", "00", "00", "00", "00",
                Integer.valueOf("0")));

        return list;
    }

}
