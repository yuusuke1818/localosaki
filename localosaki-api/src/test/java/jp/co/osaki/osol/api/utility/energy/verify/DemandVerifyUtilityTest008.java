package jp.co.osaki.osol.api.utility.energy.verify;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.result.utility.CommonSchedulePatternChangeResult;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmControlScheduleLogListDetailResultData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.CommonSchedulePatternChangeResultTestData;
import jp.co.osaki.osol.api.utility.energy.verify.testdata.SmControlScheduleLogListTestData;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * createScheduleChangeResultをテストするクラス
 * @author ya-ishida
 *
 */
public class DemandVerifyUtilityTest008 {

    @Rule
    public TestName testName = new TestName();

    private CommonSchedulePatternChangeResultTestData commonSchedulePatternChangeResultTestData = new CommonSchedulePatternChangeResultTestData();
    private SmControlScheduleLogListTestData smControlScheduleLogListTestData = new SmControlScheduleLogListTestData();
    private Timestamp settingUpdateDateTimeFrom = new Timestamp(
            DateUtility.conversionDate("201812171400", DateUtility.DATE_FORMAT_YYYYMMDDHHMM).getTime());

    @Rule
    public TestRule rule = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            System.out.println(testName.getMethodName().concat("：START"));
        }

        @Override
        protected void after() {
            System.out.println(testName.getMethodName().concat("：END"));
        }

    };

    /**
     * スケジュール情報NULL・前データNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNullList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_1_1()));
    }

    /**
     * スケジュール情報NULL・前データEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_1_2()));
    }

    /**
     * スケジュール情報NULL・前データあり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern1_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlScheduleLogListTestData.getNullList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_1_3()));
    }

    /**
     * スケジュール情報EMPTY・前データNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_2_1()));
    }

    /**
     * スケジュール情報EMPTY・前データEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_2_2()));
    }

    /**
     * スケジュール情報EMPTY・前データあり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern2_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlScheduleLogListTestData.getEmptyList(), settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_2_3()));
    }

    /**
     * スケジュール情報1件・フラグON・前データNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_1()));
    }

    /**
     * スケジュール情報1件・フラグON・前データEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_2()));
    }

    /**
     * スケジュール情報1件・フラグON・前データあり・設定変更日重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_3()));
    }

    /**
     * スケジュール情報1件・フラグON・前データあり・設定変更日重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_4()));
    }

    /**
     * スケジュール情報1件・フラグOFF・前データNULL
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_5()));
    }

    /**
     * スケジュール情報1件・フラグOFF・前データEMPTY
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_6()));
    }

    /**
     * スケジュール情報1件・フラグOFF・前データあり・設定変更日重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_7()));
    }

    /**
     * スケジュール情報1件・フラグOFF・前データあり・設定変更日重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern3_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getCurrentSingleData(Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptySingleList(OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_3_8()));
    }

    /**
     * スケジュール情報複数件・フラグON・前データNULL・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_1() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_1(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データNULL・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_2() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_2(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データEMPTY・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_3() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_3(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データEMPTY・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_4() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_4(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データあり・2件目が集計範囲Fromではない・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_5() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_5(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データあり・2件目が集計範囲From・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_6() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_6(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データあり・2件目が集計範囲Fromではない・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_7() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_7(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグON・前データあり・2件目が集計範囲From・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_8() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_ON),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_8(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データNULL・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_9() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_9(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データNULL・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_10() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getNullSet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_10(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データEMPTY・2件目が集計範囲Fromではない
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_11() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_11(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データEMPTY・2件目が集計範囲From
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_12() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(commonSchedulePatternChangeResultTestData.getEmptySet(),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_12(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データあり・2件目が集計範囲Fromではない・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_13() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_13(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データあり・2件目が集計範囲From・前データと重複なし
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_14() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.FALSE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_14(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データあり・2件目が集計範囲Fromではない・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_15() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.FALSE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.FALSE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_15(settingUpdateDateTimeFrom)));
    }

    /**
     * スケジュール情報複数件・フラグOFF・前データあり・2件目が集計範囲From・前データと重複あり
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testPattern4_16() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        assertEquals(Boolean.TRUE,
                getTestResult(
                        commonSchedulePatternChangeResultTestData.getCurrentMultiData(Boolean.TRUE,
                                settingUpdateDateTimeFrom, Boolean.TRUE),
                        smControlScheduleLogListTestData.getNotEmptyMultiList(Boolean.TRUE, settingUpdateDateTimeFrom,
                                OsolConstants.FLG_OFF),
                        settingUpdateDateTimeFrom,
                        commonSchedulePatternChangeResultTestData.getResultTest008_4_16(settingUpdateDateTimeFrom)));
    }

    /**
     * テスト結果を取得する
     * @param currentResultSet
     * @param scheduleLogList
     * @param settingUpdateDateTimeFrom
     * @param expectedSet
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Boolean getTestResult(LinkedHashSet<CommonSchedulePatternChangeResult> currentResultSet,
            List<SmControlScheduleLogListDetailResultData> scheduleLogList, Timestamp settingUpdateDateTimeFrom,
            LinkedHashSet<CommonSchedulePatternChangeResult> expectedSet)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = DemandVerifyUtility.class.getDeclaredMethod("createScheduleChangeResult", LinkedHashSet.class,
                List.class, Timestamp.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        LinkedHashSet<CommonSchedulePatternChangeResult> actualSet = (LinkedHashSet<CommonSchedulePatternChangeResult>) method
                .invoke(null, currentResultSet, scheduleLogList, settingUpdateDateTimeFrom);

        List<CommonSchedulePatternChangeResult> expectedList;
        List<CommonSchedulePatternChangeResult> actualList;

        if (expectedSet == null) {
            expectedList = null;
        } else if (expectedSet.isEmpty()) {
            expectedList = new ArrayList<>();
        } else {
            expectedList = new ArrayList<CommonSchedulePatternChangeResult>(expectedSet);
        }

        if (actualSet == null) {
            actualList = null;
        } else if (actualSet.isEmpty()) {
            actualList = new ArrayList<>();
        } else {
            actualList = new ArrayList<CommonSchedulePatternChangeResult>(actualSet);
        }

        if (expectedList == null) {
            if (actualList == null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else if (expectedList.isEmpty()) {
            if (actualList == null) {
                return Boolean.FALSE;
            } else if (actualList.isEmpty()) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            if (actualList == null || actualList.isEmpty() || expectedList.size() != actualList.size()) {
                return Boolean.FALSE;
            } else {
                for (int i = 0; i < expectedList.size(); i++) {
                    if (!expectedList.get(i).getPatternChangeTimestamp()
                            .equals(actualList.get(i).getPatternChangeTimestamp())
                            || (expectedList.get(i).getDateChangeFlg() == null
                                    && actualList.get(i).getDateChangeFlg() != null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() == null)
                            || (expectedList.get(i).getDateChangeFlg() != null
                                    && actualList.get(i).getDateChangeFlg() != null
                                    && !expectedList.get(i).getDateChangeFlg()
                                            .equals(actualList.get(i).getDateChangeFlg()))
                            || (expectedList.get(i).getScheduleLogChangeFlg() == null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() == null)
                            || (expectedList.get(i).getScheduleLogChangeFlg() != null
                                    && actualList.get(i).getScheduleLogChangeFlg() != null
                                    && !expectedList.get(i).getScheduleLogChangeFlg()
                                            .equals(actualList.get(i).getScheduleLogChangeFlg()))
                            || (expectedList.get(i).getHolidayLogChangeFlg() == null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() == null)
                            || (expectedList.get(i).getHolidayLogChangeFlg() != null
                                    && actualList.get(i).getHolidayLogChangeFlg() != null
                                    && !expectedList.get(i).getHolidayLogChangeFlg()
                                            .equals(actualList.get(i).getHolidayLogChangeFlg()))
                            || (expectedList.get(i).getChangeAfterDate() == null
                                    && actualList.get(i).getChangeAfterDate() != null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() == null)
                            || (expectedList.get(i).getChangeAfterDate() != null
                                    && actualList.get(i).getChangeAfterDate() != null
                                    && !expectedList.get(i).getChangeAfterDate()
                                            .equals(actualList.get(i).getChangeAfterDate()))
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlScheduleLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlScheduleLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlScheduleLogId()))
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() == null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() == null)
                            || (expectedList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && actualList.get(i).getChangeAfterSmControlHolidayLogId() != null
                                    && !expectedList.get(i).getChangeAfterSmControlHolidayLogId()
                                            .equals(actualList.get(i).getChangeAfterSmControlHolidayLogId()))) {
                        return Boolean.FALSE;
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

}
