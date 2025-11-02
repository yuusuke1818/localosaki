package jp.co.osaki.sms.bean.sms.collect.setting.meterreading.autoinspection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.ListSmsDevRelationParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionRequestSet;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionRequestSet;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.InsertSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.response.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionResponse;
import jp.co.osaki.osol.api.response.sms.server.setting.buildingdevice.ListSmsDevRelationResponse;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.buildingdevice.ListSmsDevRelationDetailResultData;
import jp.co.osaki.osol.entity.MBuildingSms;
import jp.co.osaki.osol.entity.MBuildingSmsPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MPauseMail;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.StringUtility;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.deviceCtrl.dao.MBuildingSmsDao;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterDao;
import jp.co.osaki.sms.deviceCtrl.dao.MPauseMailDao;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面.
 *
 * @author ozaki.y
 */
@Named(value = "smsCollectSettingMeterreadingAutoInspectionEditBean")
@ConversationScoped
public class EditBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -4995894730324063188L;

    /** 画面遷移文字列 */
    private static final String FORWARD_STR = "autoInspectionEdit";

    /** 自動検針日時 日プルダウン 未選択時. */
    private static final int INSPECTION_DAY_NO_SELECT = 0;

    /** 自動検針日時 日プルダウン 最小値. */
    private static final int INSPECTION_DAY_MIN = 1;

    /** 自動検針日時 日プルダウン 最大値. */
    private static final int INSPECTION_DAY_MAX = 31;

    /** 自動検針日時 時プルダウン 最小値. */
    private static final int INSPECTION_HOUR_MIN = 0;

    /** 自動検針日時 時プルダウン 最大値. */
    private static final int INSPECTION_HOUR_MAX = 23;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 自動検針日時 日プルダウンMap. */
    private Map<String, String> inspectionDayMap;

    /** 自動検針日時 時プルダウンMap. */
    private Map<String, Integer> inspectionHourMap;

    /** 自動検針編集用データList. */
    private List<AutoInspectionEditData> editDataList;

    /** 編集前自動検針データMap(装置-メーター種別をキーとする自動検針データMap). */
    private Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap;

    /** メッセージ 登録確認. */
    private String beforeRegisterMessage;

    /** 登録ボタンの制御. */
    private boolean updateButtonDisabled;

    @Inject
    private SmsMessages beanMessages;

    @Inject
    private PullDownList toolsPullDownList;

    @EJB
    private MBuildingSmsDao mBuildingSmsDao;

    @EJB
    private MPauseMailDao mPauseMailDao;

    @EJB
    private MMeterDao mMeterDao;

    public EditBean() {
        setInspectionDayMap(createInspectionDayMap("%d日", "--"));
        setInspectionHourMap(createInspectionHourMap("%d時"));
        setUpdateButtonDisabled(true);
    }

    /**
     * 初期処理.
     *
     * @return 遷移先
     */
    @Override
    public String init() {
        conversationStart();

        return FORWARD_STR;
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     * @throws Exception 例外
     */
    @Logged
    public String init(ListInfo buildingInfo) throws Exception {
        setBeforeRegisterMessage(beanMessages.getMessage("osol.warn.beforeRegisterMessage"));

        prepare(buildingInfo);

        return init();
    }

    /**
     * 登録.
     *
     * @throws Exception 例外
     */
    @Logged
    public void regist() throws Exception {
        ListInfo buildingInfo = getBuildingInfo();

        List<String> insertDevIdList = insertCheck(editDataList, beforeAutoInspInfoMap, buildingInfo);

        // 予約検針日時取得
        List<MMeter> reservationmMeterList = new ArrayList<MMeter>();
        for (Entry<String, Map<Long, ListSmsAutoInspectionResultData>> entry : beforeAutoInspInfoMap.entrySet()) {
            List<MMeter> mMeterList = new ArrayList<MMeter>();
            // 装置ID毎のメーター登録情報を取得
            MMeter entity = new MMeter();
            MMeterPK id = new MMeterPK();
            id.setDevId(entry.getKey());
            entity.setId(id);
            entity.setDelFlg(0);
            mMeterList = mMeterDao.getMMeterList(entity);
            reservationmMeterList.addAll(mMeterList);
        }

        // 自動検針の登録用に差分を取得
        List<AutoInspectionEditData> diffEditDataList = checkDiff(editDataList, beforeAutoInspInfoMap, buildingInfo, insertDevIdList);

        Date currentDate = DateUtility.conversionDate(
              DateUtility.changeDateFormat(mMeterDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHH),
              DateUtility.DATE_FORMAT_YYYYMMDDHH);

        // 同一時刻にて予約検針が設定されているか判定
        for (AutoInspectionEditData data : diffEditDataList) {

            // 月毎の自動検針実施有無で年月日時を生成(編集前)
            List<String> autoInspExecPreDayList = createPreDayString(
                    data.getInspectionMonth(), data.getInspectionDay(), data.getInspectionHour().toString(),
                    DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDDHH));

            // 対象日時なしの場合は次へ
            if (autoInspExecPreDayList.size() == 0) {
                continue;
            }

            // 月毎の自動検針実施有無で年月日時を生成(編集後)
            List<String> autoInspExecDayList = createAftDayString(autoInspExecPreDayList);

            for (MMeter mMeter : reservationmMeterList) {

                // 予約検針日時が設定されていない場合
                if (mMeter.getReserveInspDate() == null) {
                    continue;
                }

                // メーター種別が異なる場合
                if (mMeter.getMeterType() != data.getMeterType()) {
                    continue;
                }

                for (String autoInspExecDay : autoInspExecDayList) {
                    // 分を追加 自動検針単位は1時間単位のため「00」を追加
                    autoInspExecDay = autoInspExecDay.concat("00");
                    String reservationInspExecDay = DateUtility.changeDateFormat(mMeter.getReserveInspDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMM);

                    // 自動検針日時と予約検針日時にて同一時刻に検針日時の設定がある場合
                    if (reservationInspExecDay.equals(autoInspExecDay)) {
                        addErrorMessage(beanMessages.getMessage("smsCollectSettingMeterreadingManualInspectionBean.error.sameReserveInspTime"));
                        return;
                    }
                }
            }
        }

        UpdateSmsAutoInspectionResponse response = executeUpdate(getEditDataList(), getBeforeAutoInspInfoMap(),
                buildingInfo);

        if (OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            // 正常終了時
            addMessage(beanMessages.getMessage("osol.info.RegisterSuccess"));
        } else {
            addMessageByResultCode(response.getResultCode());
        }

        prepare(buildingInfo);
    }

    /**
     * 月毎の自動検針実施有無で年月日時を生成(編集前)
     *
     * @param month 月
     * @param day 日
     * @param hour 時
     * @param nowTime
     * @return List<String>
     */
    private List<String> createPreDayString(String month, String day, String hour, String nowTime) {
        List<String> ret = new ArrayList<>();
        String[] monthArr = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
        int dayWk = Integer.valueOf(day);
        int hourWk = Integer.valueOf(hour);

        for (int i = 0; i < monthArr.length; i++) {
            String monthEna = month.substring(i, i + 1);
            if (monthEna.equals("1")) {
                String execData = nowTime.substring(0, 4) +
                        monthArr[i] +
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, dayWk) +
                        String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, hourWk);
                ret.add(execData);
            }
        }
        return ret;
    }

    /**
     * 月毎の自動検針実施有無で年月日時を生成(編集後)
     *
     * @param List<String> preDayString
     * @return List<String>  書式:yyyyMMddHH
     */
    private List<String> createAftDayString(List<String> preDayString) {
        List<String> ret = new ArrayList<>();

        String wk_exec_day = "";
        for (String execDayWk : preDayString) {
            String yearWk = execDayWk.substring(0, 4);
            String monthWk = execDayWk.substring(4, 6);
            String dayWk = execDayWk.substring(6, 8);
            String hourWk = execDayWk.substring(8, 10);

            if (day_exists(yearWk, monthWk, dayWk)) {
                if (dayWk.equals("01") && hourWk.equals("00")) {
                    wk_exec_day = yearWk + monthWk + "0100";
                } else {
                    wk_exec_day = execDayWk;
                }
            } else {
                // 該当年月の月末日を取得
                Date dateWk = DateUtility.conversionDate(yearWk + monthWk + "01", DateUtility.DATE_FORMAT_YYYYMMDD);
                String endDays = String.format(StringUtility.STRING_FORMAT_ZERO_PADDING_2, DateUtility.getNumberOfDays(dateWk));
                // 対象日の修正
                wk_exec_day = yearWk + monthWk + endDays + hourWk;
            }
            ret.add(wk_exec_day);
        }
        return ret;
    }

    /**
     * 日付存在チェック
     *
     * @param String yearWk
     * @param String dayWk
     * @param String hourWk
     * @return Boolean
     */
    private Boolean day_exists(String yearWk, String monthWk, String dayWk) {
        Integer[] mlast = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int yearNum = Integer.valueOf(yearWk);
        int monthNum = Integer.valueOf(monthWk);
        int dayNum = Integer.valueOf(dayWk);
        if (monthNum < 1 || 12 < monthNum) {
            return false;
        }

        if (monthNum == 2) {
            if (((yearNum % 4 == 0) && (yearNum % 100 != 0))
                    || (yearNum % 400 == 0)) {
                mlast[1]++;
            }
        }

        if (dayNum < 1 || mlast[monthNum - 1] < dayNum) {
            return false;
        }
        return true;
    }

    /**
     * 準備処理.
     *
     * @param buildingInfo 建物・テナント情報
     */
    private void prepare(ListInfo buildingInfo) throws Exception {
        setBuildingInfo(buildingInfo);

        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        Entry<String, String> devPrmMapEntry = toolsPullDownList.getDevPrm(corpId, buildingId).entrySet().stream()
                .findFirst().orElse(null);
        if (devPrmMapEntry != null) {
            ListSmsAutoInspectionResult result = getAutoInspectionData(buildingInfo);

            setEditDataList(createEditDataList(result.getAutoInspectionInfoList()));
            setBeforeAutoInspInfoMap(result.getDevMeterTypeAutoInspInfoMap());
            setUpdateButtonDisabled(false);
        }
    }

    /**
     * 自動検針データを取得.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 自動検針データ
     */
    private ListSmsAutoInspectionResult getAutoInspectionData(ListInfo buildingInfo) {
        ListSmsAutoInspectionParameter parameter = new ListSmsAutoInspectionParameter();
        parameter.setCorpId(buildingInfo.getCorpId());
        parameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
        parameter.setTenant(buildingInfo.getTenantFlg());

        return callApiPost(parameter, ListSmsAutoInspectionResponse.class).getResult();
    }

    /**
     * 自動検針編集用データListを生成.
     *
     * @param targetDataList 自動検針データList
     * @return 自動検針編集用データList
     * @throws Exception 例外
     */
    private List<AutoInspectionEditData> createEditDataList(List<ListSmsAutoInspectionResultData> targetDataList)
            throws Exception {

        return createEditDataList(targetDataList, "%d日", "%d時", "--");
    }

    /**
     * 自動検針編集用データListを生成.
     *
     * @param targetDataList 自動検針データList
     * @param dayDispFormat 自動検針日表示フォーマット
     * @param hourDispFormat 自動検針時表示フォーマット
     * @param noSelectFormat 未選択表示フォーマット
     * @return 自動検針編集用データList
     * @throws Exception 例外
     */
    private List<AutoInspectionEditData> createEditDataList(List<ListSmsAutoInspectionResultData> targetDataList,
            String dayDispFormat, String hourDispFormat, String noSelectFormat) throws Exception {

        Map<String, String> inspectionDayMap = createInspectionDayMap(dayDispFormat, noSelectFormat);
        Map<String, Integer> inspectionHourMap = createInspectionHourMap(hourDispFormat);

        String defaultInspectionMonth = "";
        String dafaultInspectionDay = new ArrayList<>(inspectionDayMap.values()).get(0);
        BigDecimal dafaultInspectionHour = new BigDecimal(new ArrayList<>(inspectionHourMap.values()).get(0));

        Map<Long, AutoInspectionEditData> editDataMap = new LinkedHashMap<>();
        for (long i = 1; i <= SmsConstants.DEV_METER_TYPE_COUNT; i++) {
            editDataMap.put(i, null);
        }

        for (ListSmsAutoInspectionResultData targetData : targetDataList) {
            editDataMap.replace(targetData.getMeterType(), new AutoInspectionEditData(targetData));
        }

        List<AutoInspectionEditData> editDataList = new ArrayList<>();
        Set<Entry<Long, AutoInspectionEditData>> editDataMapEntrySet = editDataMap.entrySet();
        for (Entry<Long, AutoInspectionEditData> editDataMapEntry : editDataMapEntrySet) {
            AutoInspectionEditData editData = editDataMapEntry.getValue();
            if (editData == null) {
                // 取得できないメーター種別行が存在する場合
                editData = new AutoInspectionEditData(editDataMapEntry.getKey(), null,
                        defaultInspectionMonth, dafaultInspectionDay, dafaultInspectionHour, null);
            }

            editDataList.add(editData);
        }

        return editDataList;
    }

    /**
     * 自動検針日時 日プルダウンMapを生成.
     *
     * @param dayDispFormat 自動検針日表示フォーマット
     * @param noSelectFormat 未選択表示フォーマット
     * @return 自動検針日時 日プルダウンMap
     */
    private Map<String, String> createInspectionDayMap(String dayDispFormat, String noSelectFormat) {
        Map<String, String> inspectionDayMap = new LinkedHashMap<>();
        inspectionDayMap.put(noSelectFormat, String.valueOf(INSPECTION_DAY_NO_SELECT));

        for (int i = INSPECTION_DAY_MIN; i <= INSPECTION_DAY_MAX; i++) {
            inspectionDayMap.put(String.format(dayDispFormat, i), String.valueOf(i));
        }

        return inspectionDayMap;
    }

    /**
     * 自動検針日時 時プルダウンMapを生成.
     *
     * @param hourDispFormat 自動検針時表示フォーマット
     * @return 自動検針日時 時プルダウンMap
     */
    private Map<String, Integer> createInspectionHourMap(String hourDispFormat) {
        Map<String, Integer> inspectionHourMap = new LinkedHashMap<>();

        for (int i = INSPECTION_HOUR_MIN; i <= INSPECTION_HOUR_MAX; i++) {
            inspectionHourMap.put(String.format(hourDispFormat, i), i);
        }

        return inspectionHourMap;
    }

    /**
     * 更新実行.
     *
     * @param editDataList 自動検針編集用データList
     * @param beforeAutoInspInfoMap 編集前自動検針データMap
     * @param buildingInfo 建物・テナント情報
     * @return APIレスポンス
     * @throws Exception 例外
     */
    private UpdateSmsAutoInspectionResponse executeUpdate(List<AutoInspectionEditData> editDataList,
            Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap, ListInfo buildingInfo)
            throws Exception {

        List<String> insertDevIdList = insertCheck(editDataList, beforeAutoInspInfoMap, buildingInfo);

        List<AutoInspectionEditData> diffEditDataList = checkDiff(editDataList, beforeAutoInspInfoMap, buildingInfo, insertDevIdList);
        UpdateSmsAutoInspectionResponse response = new UpdateSmsAutoInspectionResponse();
        if(diffEditDataList.size() > 0) {
            UpdateSmsAutoInspectionRequest request = new UpdateSmsAutoInspectionRequest();
            request.setUpdateDataList(createUpdateRequestSetList(diffEditDataList));
            request.setBeforeAutoInspInfoMap(beforeAutoInspInfoMap);

            UpdateSmsAutoInspectionParameter parameter = new UpdateSmsAutoInspectionParameter();
            parameter.setCorpId(buildingInfo.getCorpId());
            parameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
            parameter.setTenant(buildingInfo.getTenantFlg());
            parameter.setRequest(request);
            response = callApiPost(parameter, UpdateSmsAutoInspectionResponse.class);

            List<String> devIdList = new ArrayList<>();
            for(Map.Entry<String, Map<Long, ListSmsAutoInspectionResultData>> entry : beforeAutoInspInfoMap.entrySet()) {
                devIdList.add(entry.getKey());
            }

            //メール送信
            sendMail(diffEditDataList, buildingInfo, devIdList);
        }else {
            //更新しないが処理結果は成功とする
            response.setResultCode(OsolApiResultCode.API_OK);
        }

        return response;
    }

    /**
     * データ更新用RequestSetのListを生成.
     *
     * @param editDataList 自動検針編集用データList
     * @return データ更新用RequestSetのList
     * @throws Exception 例外
     */
    private List<UpdateSmsAutoInspectionRequestSet> createUpdateRequestSetList(
            List<AutoInspectionEditData> editDataList) throws Exception {

        List<UpdateSmsAutoInspectionRequestSet> updateRequestSetList = new ArrayList<>();
        for (AutoInspectionEditData editData : editDataList) {
            UpdateSmsAutoInspectionRequestSet updateRequestSet = new UpdateSmsAutoInspectionRequestSet();
            updateRequestSet.setMeterType(editData.getMeterType());
            updateRequestSet.setInspectionMonth(editData.getInspectionMonth());
            updateRequestSet.setInspectionDay(editData.getInspectionDay());
            updateRequestSet.setInspectionHour(editData.getInspectionHour());
            updateRequestSet.setVersion(editData.getVersion());

            updateRequestSetList.add(updateRequestSet);
        }

        return updateRequestSetList;
    }

    /**
     * insert(null)チェック
     * 新規装置の場合は自動検針設定の時刻が登録されていないので確認
     * @param editDataList
     * @param beforeAutoInspInfoMap
     * @param buildingInfo
     * @return
     * @throws Exception
     */
    public List<String> insertCheck(List<AutoInspectionEditData> editDataList,
            Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap, ListInfo buildingInfo)
            throws Exception{

        //追加したdevIdのリスト
        List<String> insertDevIdList = new ArrayList<>();

        // 建物に紐づいている装置の一覧取得
        ListSmsDevRelationParameter devParameter = new ListSmsDevRelationParameter();
        //リクエストパラメータセット
        devParameter.setBean("ListSmsDevRelationBean");
        devParameter.setCorpId(buildingInfo.getCorpId());
        devParameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
        ListSmsDevRelationResponse devResponse = callApiPost(devParameter, ListSmsDevRelationResponse.class);
        if(devResponse.getResult() == null) {
            return insertDevIdList;
        }
        // 編集後の内容を新規登録する
        // 20個無ければ新規登録登録処理
        List<ListSmsDevRelationDetailResultData> detailList = devResponse.getResult().getDetailList();
        List<AutoInspectionEditData> nullDataList = new ArrayList<>();
        if(detailList == null || detailList.size() == 0) {
            return insertDevIdList;
        }

        for(ListSmsDevRelationDetailResultData devData : detailList) {
            String devId = devData.getDevId();
            //未登録の装置がある場合はすべて登録
            if(!beforeAutoInspInfoMap.containsKey(devData.getDevId())) {
                for(AutoInspectionEditData editData : editDataList) {
                    AutoInspectionEditData ret = new AutoInspectionEditData(editData);
                    ret.setDevId(devId);
                    nullDataList.add(ret);
                }
                insertDevIdList.add(devId);
            }else {
                Map<Long, ListSmsAutoInspectionResultData> map = beforeAutoInspInfoMap.get(devData.getDevId());
                if(map.size() < 20) {
                    for(AutoInspectionEditData editData : editDataList) {
                        AutoInspectionEditData ret = new AutoInspectionEditData(editData);
                        ret.setDevId(devId);
                        nullDataList.add(ret);
                    }
                    insertDevIdList.add(devId);
                }
            }
        }

        if(nullDataList.size() > 0) {
            InsertSmsAutoInspectionRequest request = new InsertSmsAutoInspectionRequest();
            request.setInsertDataList(createInsertRequestSetList(nullDataList));

            InsertSmsAutoInspectionParameter parameter = new InsertSmsAutoInspectionParameter();
            parameter.setCorpId(buildingInfo.getCorpId());
            parameter.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
            parameter.setTenant(buildingInfo.getTenantFlg());
            parameter.setRequest(request);
            callApiPost(parameter, InsertSmsAutoInspectionResponse.class);

        }
        return insertDevIdList;
    }

    /**
     * データ更新用RequestSetのListを生成.
     *
     * @param editDataList 自動検針編集用データList
     * @return データ更新用RequestSetのList
     * @throws Exception 例外
     */
    private List<InsertSmsAutoInspectionRequestSet> createInsertRequestSetList(List<AutoInspectionEditData> editDataList) throws Exception {

        List<InsertSmsAutoInspectionRequestSet> insertRequestSetList = new ArrayList<>();
        for (AutoInspectionEditData editData : editDataList) {
            InsertSmsAutoInspectionRequestSet insertRequestSet = new InsertSmsAutoInspectionRequestSet();
            insertRequestSet.setDevId(editData.getDevId());
            insertRequestSet.setMeterType(editData.getMeterType());
            insertRequestSet.setInspectionMonth(editData.getInspectionMonth());
            insertRequestSet.setInspectionDay(editData.getInspectionDay());
            insertRequestSet.setInspectionHour(editData.getInspectionHour());
            insertRequestSet.setVersion(editData.getVersion());

            insertRequestSetList.add(insertRequestSet);
        }

        return insertRequestSetList;
    }



    /**
     * 差分チェック
     * 差分があればメール送信
     * @param editDataList
     * @param beforeAutoInspInfoMap
     * @param buildingInfo
     * @return
     * @throws Exception
     */
    public List<AutoInspectionEditData> checkDiff(List<AutoInspectionEditData> editDataList,
            Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap, ListInfo buildingInfo, List<String> insertDevIdList)
            throws Exception{

        List<AutoInspectionEditData> mailEditDataList = new ArrayList<>();
        for(Map.Entry<String, Map<Long, ListSmsAutoInspectionResultData>> entry : beforeAutoInspInfoMap.entrySet()) {
            if(insertDevIdList.contains(entry.getKey())) {
                continue;
            }
            for(AutoInspectionEditData editData : editDataList) {
                ListSmsAutoInspectionResultData beforeData = new ListSmsAutoInspectionResultData();
                beforeData = entry.getValue().get(editData.getMeterType());
                //月の差分, 日の差分, 時間の差分
                if(!beforeData.getInspectionMonth().equals(editData.getInspectionMonth()) || !beforeData.getInspectionDay().equals(editData.getInspectionDay()) || !beforeData.getInspectionHour().equals(editData.getInspectionHour())) {
                    mailEditDataList.add(editData);
                }
            }
            //自動検針設定は建物で決まっているので、全部の装置を見る必要がない。
            //１つ見れば終了させる。
            break;
        }

        return mailEditDataList;
    }

    public void sendMail(List<AutoInspectionEditData> mailEditDataList, ListInfo buildingInfo, List<String> devIdList) {

        int num = 0;

        for(String devId : devIdList) {
            MPauseMail pauseMail = new MPauseMail();
            pauseMail.setDevId(devId);
            if(!mPauseMailDao.isNull(pauseMail)) {
                pauseMail = mPauseMailDao.find(pauseMail);
                if(pauseMail.getAutoinspDateSetting() != null && pauseMail.getAutoinspDateSetting().equals("1")) {
                    num++;
                }
            }
        }

        if(num == devIdList.size()) {
            return;
        }


        MBuildingSmsPK mBuildingSmsPK = new MBuildingSmsPK();
        mBuildingSmsPK.setCorpId(buildingInfo.getCorpId());
        mBuildingSmsPK.setBuildingId(Long.parseLong(buildingInfo.getBuildingId()));
        MBuildingSms mBuildingSms = new MBuildingSms();
        mBuildingSms.setId(mBuildingSmsPK);

        //メールアドレスなどの情報を取得
        mBuildingSms = mBuildingSmsDao.find(mBuildingSms);

        List<String> mailList = new ArrayList<>();
        addMail(mailList, mBuildingSms);

        SendAutoInspUpdateMail sendAutoInspUpdateMail = new SendAutoInspUpdateMail();
        sendAutoInspUpdateMail.autoInspSettingUpdate(mailList, buildingInfo, mailEditDataList);

    }

    /**
     * アドレスを最大10個詰める
     * @param mailList
     * @param ret
     */
    public void addMail(List<String> mailList, MBuildingSms ret) {

        if(!CheckUtility.isNullOrEmpty(ret.getMail1())) {
            mailList.add(ret.getMail1());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail2())) {
            mailList.add(ret.getMail2());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail3())) {
            mailList.add(ret.getMail3());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail4())) {
            mailList.add(ret.getMail4());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail5())) {
            mailList.add(ret.getMail5());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail6())) {
            mailList.add(ret.getMail6());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail7())) {
            mailList.add(ret.getMail7());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail8())) {
            mailList.add(ret.getMail8());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail9())) {
            mailList.add(ret.getMail9());
        }

        if(!CheckUtility.isNullOrEmpty(ret.getMail10())) {
            mailList.add(ret.getMail10());
        }
    }

    public ListInfo getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo(ListInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public Map<String, String> getInspectionDayMap() {
        return inspectionDayMap;
    }

    public void setInspectionDayMap(Map<String, String> inspectionDayMap) {
        this.inspectionDayMap = inspectionDayMap;
    }

    public Map<String, Integer> getInspectionHourMap() {
        return inspectionHourMap;
    }

    public void setInspectionHourMap(Map<String, Integer> inspectionHourMap) {
        this.inspectionHourMap = inspectionHourMap;
    }

    public List<AutoInspectionEditData> getEditDataList() {
        return editDataList;
    }

    public void setEditDataList(List<AutoInspectionEditData> editDataList) {
        this.editDataList = editDataList;
    }

    public Map<String, Map<Long, ListSmsAutoInspectionResultData>> getBeforeAutoInspInfoMap() {
        return beforeAutoInspInfoMap;
    }

    public void setBeforeAutoInspInfoMap(
            Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap) {
        this.beforeAutoInspInfoMap = beforeAutoInspInfoMap;
    }

    public String getBeforeRegisterMessage() {
        return beforeRegisterMessage;
    }

    public void setBeforeRegisterMessage(String beforeRegisterMessage) {
        this.beforeRegisterMessage = beforeRegisterMessage;
    }

    public boolean isUpdateButtonDisabled() {
        return updateButtonDisabled;
    }

    public void setUpdateButtonDisabled(boolean updateButtonDisabled) {
        this.updateButtonDisabled = updateButtonDisabled;
    }
}
