package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlHolidayCalLogListDetailResultData;

/**
 * 機器制御祝日設定カレンダ履歴取得 ResultDataクラス テストデータ
 * @author ya-ishida
 *
 */
public class SmControlHolidayCalLogListTestData
        extends AbstractDemandVerifyUtilityTestData<SmControlHolidayCalLogListDetailResultData> {

    /**
     * 機器制御祝日設定カレンダ履歴
     * @return
     */
    public List<SmControlHolidayCalLogListDetailResultData> getHolidayCalLogList() {
        List<SmControlHolidayCalLogListDetailResultData> resultList = new ArrayList<>();

        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0114",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0211",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0321",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0429",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0430",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0501",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0502",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0503",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0504",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0505",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0506",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0715",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0802",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0916",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1014",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1104",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "1123",
                Integer.valueOf("0")));

        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0101",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0114",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0211",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0321",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0429",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0503",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0504",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0505",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0506",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0715",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0802",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "0916",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "1014",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "1104",
                Integer.valueOf("0")));
        resultList.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("2"), Long.valueOf("2"), "1123",
                Integer.valueOf("0")));

        return resultList;
    }

    /**
     * Test015_2_2テストデータ
     * @return
     */
    public List<SmControlHolidayCalLogListDetailResultData> getTest015_2_2() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0219",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_7テストデータ
     * @return
     */
    public List<SmControlHolidayCalLogListDetailResultData> getTest015_2_7() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();
        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0219",
                Integer.valueOf("0")));

        return list;
    }

    /**
     * Test015_2_8テストデータ
     * @return
     */
    public List<SmControlHolidayCalLogListDetailResultData> getTest015_2_8() {
        List<SmControlHolidayCalLogListDetailResultData> list = new ArrayList<>();

        list.add(new SmControlHolidayCalLogListDetailResultData(Long.valueOf("1"), Long.valueOf("2"), "0219",
                Integer.valueOf("0")));

        return list;
    }

}
