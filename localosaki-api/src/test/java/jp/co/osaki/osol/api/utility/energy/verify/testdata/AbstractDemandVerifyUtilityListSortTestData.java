package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.util.List;

public abstract class AbstractDemandVerifyUtilityListSortTestData<T> extends AbstractDemandVerifyUtilityTestData<T> {

    /**
     * 2件以上のInputList
     * @return
     */
    abstract public List<T> getNotEmptyInputList();

    /**
     * 2件以上を昇順でソートしたList
     * @return
     */
    abstract public List<T> getNotEmptyAscSortList();

    /**
     * 2件以上を降順でソートしたList
     * @return
     */
    abstract public List<T> getNotEmptyDescSortList();

}
