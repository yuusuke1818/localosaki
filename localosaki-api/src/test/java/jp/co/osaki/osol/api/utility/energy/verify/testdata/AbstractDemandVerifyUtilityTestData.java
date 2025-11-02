package jp.co.osaki.osol.api.utility.energy.verify.testdata;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class AbstractDemandVerifyUtilityTestData<T> {

    /**
     * NULL（List)
     * @return
     */
    public final List<T> getNullList() {
        return null;
    }

    /**
     * EMPTY（List）
     * @return
     */
    public final List<T> getEmptyList() {
        return new ArrayList<>();
    }

    /**
     * NULL(LinkedHashSet）
     * @return
     */
    public final LinkedHashSet<T> getNullSet() {
        return null;
    }

    /**
     * EMPTY（LinkedHashSet）
     * @return
     */
    public final LinkedHashSet<T> getEmptySet() {
        return new LinkedHashSet<>();
    }

}
