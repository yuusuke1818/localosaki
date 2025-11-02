package jp.co.osaki.sms.bean.sms.collect;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meter.ListExpiredMeterParameter;
import jp.co.osaki.osol.api.response.sms.collect.setting.meter.ListExpiredMeterResponse;
import jp.co.osaki.osol.api.resultdata.sms.meter.ListSmsMeterResultData;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterInfo;
import jp.co.osaki.sms.bean.sms.collect.setting.meter.MeterManagementConstants;
import jp.co.osaki.sms.bean.tools.GenericTypeList;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.MCorpDao;
import jp.co.osaki.sms.dao.MDevPrmListDao;
import jp.co.osaki.sms.dao.MGenericTypeDao;

/**
 * メインメニュー画面
 * データ収集装置用
 *
 * @author yoneda_y
 */
@Named(value = "smsCollectTopBean")
@ConversationScoped
public class TopBean extends SmsConversationBean implements Serializable {

    //シリアライズID
    private static final long serialVersionUID = -7605199318030142759L;

    @Inject
    private TopBeanProperty topBeanProperty;

    /** 汎用区分マスタDao. */
    @EJB
    private MGenericTypeDao mGenericTypeDao;

    /** 接続先プルダウンMap. */
    private Map<String, String> devIdMap;

    /** 検満通知プルダウンMap. */
    private Map<String, String> examnoticeMap;

    /**
     * 汎用区分
     */
    @Inject
    private GenericTypeList genericTypeList;

    /** 抽出範囲プルダウンMap. */
    private Map<String, String> monthExtractMap;

    /** 選択値 接続先名. */
    private String devName;

    /** 選択値 接続先(上位連携装置ID). */
    private String devId;

    /** 選択値 抽出範囲. */
    private String monthExtract;

    /** [共通] 装置情報Dao. */
    @EJB
    private MDevPrmListDao mDevPrmListDao;

    @Inject
    private PullDownList toolsPullDownList;

    /** テーブル表示件数 */
    private int dispCnt;

    /** 一覧表示用メーター情報リスト */
    private List<MeterInfo> meterList;



    @EJB
    MCorpDao mCorpDao;

    @Override
    public String init() {
        conversationStart();
        return "collectTop";
    }

    public String initBuildingInfo(ListInfo listInfo) {
        topBeanProperty.setListInfo(listInfo);
        // 選択された建物の企業
        this.setLoginOperationCorp(mCorpDao.find(listInfo.getCorpId()));
        if(getManagementVisble()) {
            String corpId = listInfo.getCorpId();
            Long buildingId = Long.valueOf(listInfo.getBuildingId());
            boolean isTenant = listInfo.getTenantFlg();

            devIdMap = toolsPullDownList.getDevPrm(corpId, buildingId, isTenant);
            setMonthExtractMap(toolsPullDownList.getMonthExtract());

            setExamNotice();

            try {
                monthExtract = "1";
                meterList = executeSearch(listInfo);
                if(meterList == null) {
                    dispCnt = 0;
                }else {
                    dispCnt = meterList.size();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return init();
    }

    /**
     * メーター情報検索.
     * クラス変数(I) buildingInfo 建物・テナント情報 (企業ID, 建物ID など)
     * クラス変数(I) smartProperty スマメ画面共通のプロパティ (装置ID など)
     * クラス変数(I) meterKind メーター種類
     * クラス変数(I) page メーター表示切替
     * クラス変数(I) pulseTypeMap パルス種別Map
     * クラス変数(I) ocstateMap 開閉区分プルダウンMap
     * クラス変数(O) pagerMap メーター表示切替プルダウンの選択肢Map
     * @param isNarrowDownPage メーター表示切替絞り込みフラグ  true:メーター表示切替プルダウンで選択された情報に制限する
     * @return メーター情報検索結果  null:検索エラー
     * @throws InvocationTargetException BeanUtils.copyProperties(～, ～) エラー
     * @throws IllegalAccessException BeanUtils.copyProperties(～, ～) エラー
     */
    public List<MeterInfo> executeSearch(ListInfo buildingInfo) throws IllegalAccessException, InvocationTargetException {
        // ページ初期化
        List<MeterInfo> meterList = null;

        String expiredDate = convertExpiredMeter(monthExtract);
        // 検索
        ListExpiredMeterResponse response = new ListExpiredMeterResponse();
        ListExpiredMeterParameter parameter = new ListExpiredMeterParameter();

        // APIBeanの指定
        parameter.setBean("ListExpiredMeterBean");
        parameter.setCorpId(buildingInfo.getCorpId());  // 企業ID
        parameter.setBuildingId(Long.valueOf(buildingInfo.getBuildingId())); // 建物ID
        parameter.setExpiredMeter(expiredDate);

        // DBから値取得
        response = callApiPost(parameter, ListExpiredMeterResponse.class);

        // 画面表示用にリストの詰め替え
        if (OsolApiResultCode.API_OK.equals(response.getResultCode()) && response.getResult() != null) {

            List<ListSmsMeterResultData> orglist = response.getResult().getList();

            meterList = new ArrayList<>();
            // DBから取得できた件数分ループして詰め替えていく
            for (ListSmsMeterResultData getData : orglist) {
                MeterInfo info = new MeterInfo();
                // BeanUtils.copyPropertiesする際にBigDesimal(Date)=nullでひっかからないように一時的に回避の処理を入れる
                ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
                ConvertUtils.register(new DateConverter(null), Date.class);
                ConvertUtils.register(new LongConverter(null), Long.class);
                // 内容をコピー
                BeanUtils.copyProperties(info, getData);
                // 回避するやつを解除
                ConvertUtils.deregister();

                // 個別でコピーする必要があるものを処理
                info.setConcentratorName(getData.getName());

                // 検満
                String tmpExamEndYm = getData.getExamEndYm();
                if(getData.getDispYearFlg().equals("1")) {
                    String[] warei = convertToJpn(tmpExamEndYm);
                    info.setExamEndYmWareki(warei[0] + warei[1] + "." + warei[2]);
                }
                else if (tmpExamEndYm != null && !tmpExamEndYm.isEmpty()) {
                    // 西暦を整形して(yyyy.MM）セット(MMはゼロパディング)
                    info.setExamEndYm(tmpExamEndYm.substring(0, 4) + "." + tmpExamEndYm.substring(4, 6));
                }

                meterList.add(info);
            }
        }

        return meterList;
    }

    /**
     * yyyyMM(西暦年月)を和暦に変換する
     * @param ym
     * @return [0]:元号(H:平成, R:令和), [1]:年, [2]:月
     */
    private String[] convertToJpn(String ym) {

        // javaのバージョン的に令和に対応していないのでゴリゴリ処理していく
        // 平成:1989年 1月 8日 – 2019年 4月30日
        // 令和:2019年 5月 1日 - 2999年12月31日（ひとまずMAXを定義）

        // 和暦変換テーブル
        String warekiTable[][] = {
                { "19890108", "20190431", MeterManagementConstants.DISP_YEAR_GENGO_H }, //平成
                { "20190501", "99991231", MeterManagementConstants.DISP_YEAR_GENGO_R } }; //令和

        // YYYYMMDDの形式に変換する
        String ymd = ym + "08";
        // 暦変換テーブルをサーチする
        int i = 0;
        for (i = 0; i < warekiTable.length; i++) {
            // 当該西暦が開始年以上で最終年以下ならbreak
            if (ymd.compareTo(warekiTable[i][0]) >= 0 && ymd.compareTo(warekiTable[i][1]) <= 0) {
                break;
            }
        }
        // 暦テーブルに該当レコードがあれば、和暦年を計算する
        if (i < warekiTable.length) {
            int jpYear = Integer.parseInt(ymd.substring(0, 4)) - Integer.parseInt(warekiTable[i][0].substring(0, 4))
                    + 1;
            // 和号+和暦年+月を返す
            String ret[] = { warekiTable[i][2], Integer.toString(jpYear),
                    String.format("%02d", Integer.parseInt(ymd.substring(4, 6))) };
            return ret;
        }
        return null;
    }

    public String reload() throws IllegalAccessException, InvocationTargetException {
        // アクセスログ出力
        exportAccessLog("reload", "ボタン「表示更新」押下");

        meterList = executeSearch(topBeanProperty.getListInfo());

        return init();

    }

    /* Getter Setter */
    public TopBeanProperty getTopBeanProperty() {
        return topBeanProperty;
    }

    public void setTopBeanProperty(TopBeanProperty topBeanProperty) {
        this.topBeanProperty = topBeanProperty;
    }

    private String convertExpiredMeter(String expiredMonth) {
        Date date = new Date(); // 今日の日付
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer month = Integer.parseInt(expiredMonth);
        switch(month) {
        case 1:
            calendar.add(Calendar.YEAR, 3);
            break;
        case 2:
            calendar.add(Calendar.YEAR, 2);
            break;
        case 3:
            calendar.add(Calendar.YEAR, 1);
            break;
        case 4:
            calendar.add(Calendar.MONTH, 6);
            break;
        default:
            break;
        }

        date = calendar.getTime();

        return DateUtility.changeDateFormat(date, DateUtility.DATE_FORMAT_YYYYMM);
    }

    /* メニュー項目の表示状態に関わる権限のチェック */

    /* データ表示 */
    public boolean getDataViewVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.DATA_VIEW) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 日報データ系 */
    public boolean getDayLoadVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.DAY_LOAD) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 検針データ */
    public boolean getInspDataVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.INSP_DATA) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 機器管理 */
    public boolean getManagementVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.MANAGEMENT) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 設定一括収集 */
    public boolean getSettingCollectionVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.SETTING_BULK) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* メーター管理 */
    public boolean getMeterManagementVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.METER) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* メーターテナント管理 */
    public boolean getMeterTenantVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.METER_TENANT) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 検針設定_任意検針 */
    public boolean getManualInspectionVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.MANUAL_INSPECTION) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 検針設定_確定前検針データ */
    public boolean getInspectionMeterBefVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.INSPECTION_METER_BEF) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    /* 権限無い場合表示しない */
    public boolean getNotVisble() {
        return !(getCorpOparationAuth(
                SmsConstants.OPARATION_FUNCTION_TYPE.NONE) == SmsConstants.OPARATION_FUNCTION_RESULT.INVISIBLE);
    }

    public Map<String, String> getDevIdMap() {
        return devIdMap;
    }

    public void setDevIdMap(Map<String, String> devIdMap) {
        this.devIdMap = devIdMap;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public List<MeterInfo> getMeterList() {
        return meterList;
    }

    public void setMeterList(List<MeterInfo> meterList) {
        this.meterList = meterList;
    }

    public Map<String, String> getMonthExtractMap() {
        return monthExtractMap;
    }

    public void setMonthExtractMap(Map<String, String> monthExtractMap) {
        this.monthExtractMap = monthExtractMap;
    }

    public String getMonthExtract() {
        return monthExtract;
    }

    public void setMonthExtract(String monthExtract) {
        this.monthExtract = monthExtract;
    }

    public int getDispCnt() {
        return dispCnt;
    }

    public void setDispCnt(int dispCnt) {
        this.dispCnt = dispCnt;
    }

    public String convertExamNotice(String examNotice) {
        return examnoticeMap.get(examNotice);
    }

    public GenericTypeList getGenericTypeList() {
        return genericTypeList;
    }

    public void setGenericTypeList(GenericTypeList genericTypeList) {
        this.genericTypeList = genericTypeList;
    }

    public Map<String, String> getExamnoticeMap() {
        return examnoticeMap;
    }

    public void setExamnoticeMap(Map<String, String> examnoticeMap) {
        this.examnoticeMap = examnoticeMap;
    }

    private void setExamNotice() {
        examnoticeMap = new HashMap<>();
        examnoticeMap.put("0", "無効");
        examnoticeMap.put("1", "有効");
    }
}
