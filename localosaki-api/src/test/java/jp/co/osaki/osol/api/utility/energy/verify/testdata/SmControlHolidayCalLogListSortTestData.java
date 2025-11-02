package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;

/**
 * 機器制御祝日設定カレンダ履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public final class SmControlHolidayCalLogListSortTestData
        extends AbstractDemandVerifyUtilityListSortTestData<SmControlHolidayCalLogListDetailResultData> {

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getNotEmptyInputList() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "1001",
                Integer.valueOf("0")));

        return list;
    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getNotEmptyAscSortList() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));

        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "1001",
                Integer.valueOf("0")));

        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));
        return list;
    }

    @Override
    public List<SmControlHolidayCalLogListDetailResultData> getNotEmptyDescSortList() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("3"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));

        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "1001",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("1"), "0101",
                Integer.valueOf("0")));

        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1001",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0201",
                Integer.valueOf("0")));
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));

        return list;
    }

}
