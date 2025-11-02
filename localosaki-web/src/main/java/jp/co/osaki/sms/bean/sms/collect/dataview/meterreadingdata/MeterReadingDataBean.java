package jp.co.osaki.sms.bean.sms.collect.dataview.meterreadingdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.MeterDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataParameter;
import jp.co.osaki.osol.api.parameter.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoParameter;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.GetSmsClaimantInfoResponse;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResponse;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsMonthMeterReadingNoResponse;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingDataResponse;
import jp.co.osaki.osol.api.response.sms.collect.dataview.meterreadingdata.UpdateSmsClaimantInfoResponse;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDataByTenant;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.BillingAmountDetailData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ClaimantInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsBillingAmountDataInfoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataMonthNoResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsMeterReadingDataResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.ListSmsSelectBillingResultDate;
import jp.co.osaki.osol.api.resultdata.sms.collect.dataview.meterreadingdata.SmsTenantInfoResultDate;
import jp.co.osaki.osol.entity.MCorpPersonAuth;
import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.web.csv.sms.converter.MeterReadingCsvConverter;
import jp.co.osaki.sms.Logged;
import jp.co.osaki.sms.SmsConstants;
import jp.co.osaki.sms.SmsConstants.DEVICE_KIND;
import jp.co.osaki.sms.SmsConversationBean;
import jp.co.osaki.sms.SmsFileDownload;
import jp.co.osaki.sms.SmsFileZipArchive;
import jp.co.osaki.sms.SmsMessages;
import jp.co.osaki.sms.bean.building.info.ListInfo;
import jp.co.osaki.sms.bean.tools.PullDownList;
import jp.co.osaki.sms.dao.BuildingInfoDao;
import jp.co.osaki.sms.dao.ListSmsBillingAmountDataSearchDao;
import jp.co.osaki.sms.dao.MDevPrmListDao;
import jp.co.osaki.sms.dao.MVariousDao;
import jp.co.osaki.sms.utility.DataViewUtility;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 検針データ画面.
 *
 * @author hosono.s
 */
@Named("meterReadingDataBean")
@ConversationScoped
public class MeterReadingDataBean extends SmsConversationBean implements Serializable {

    /** シリアライズID. */
    private static final long serialVersionUID = -8182315825341919426L;

    /** 値未取得時表示文字列(文字型). */
    private static final String NO_VALUE_STR = "-";
    /** 値未取得時表示文字列(数型). */
    private static final String NO_VALUE_NUM = "0";

    /** zipファイル作成時の拡張子. */
    private static final String ZIP_EXTENSION = ".zip";

    /** ページ表示件数. */
    private int pageDataCount;

    /** 建物・テナント情報. */
    private ListInfo buildingInfo;

    /** 検針データ画面用. */
    @Inject
    private MeterReadingDataBeanProperty meterReadingDataBeanProperty;

    /** 検針データリスト */
    @Inject
    private MeterReadingDataPagingList meterReadingDataPagingList;

    /** 請求金額データ画面用. */
    @Inject
    private BillingAmountDataBeanProperty billingAmountDataBeanProperty;

    /** 請求金額データ詳細画面用. */
    @Inject
    private BillingAmountDataDetailsBeanProperty billingAmountDataDetailsBeanProperty;

    /** 請求金額データリスト */
    @Inject
    private BillingAmountDataPagingList billingAmountDataPagingList;

    /** プルダウンリストクラス. */
    @Inject
    private PullDownList toolsPullDownList;

    @Inject
    private SmsFileDownload smsFileDownload;

    @EJB
    private MDevPrmListDao mDevPrmListDao;

    @EJB
    private ListSmsBillingAmountDataSearchDao listSmsBillingAmountDataSearchDao;

    @EJB
    private BuildingInfoDao buildingInfoDao;

    @Inject
    private SelectBillingDataPagingList selectBillingDataPagingList;

    @EJB
    private MVariousDao mVariousDao;

    /**
     * メーターフィルター
     */
    @EJB
    private MeterDataFilterDao meterDataFilterDao;

    private String downloadFilePath;

    private String saveFilename;

    private List<String> savePdfFileList;

    private String[] savePdfPaths;

    ClaimantInfoResultData claimantData;

    @Inject
    private SmsFileDownload fileDownloader;

    private List<BillingAmountDataByTenant> pdfList;

    //メッセージクラス
    @Inject
    private SmsMessages beanMessages;

    // デフォルトコンストラクタ
    public MeterReadingDataBean() {
    }

    /**
     * 初期処理.
     *
     * @return 遷移先
     */
    @Override
    public String init() {
        conversationStart();

        return "meterReadingData";
    }

    /**
     * 初期処理.
     *
     * @param buildingInfo 建物・テナント情報
     * @return 遷移先
     */
    @Logged
    public String init(ListInfo buildingInfo) {
        setPageDataCount(changeInteger(
                getWrapped().getInitParameter(SmsConstants.PARAM_NAME_PAGE_DATA_COUNT_METER_READING), false));

        this.meterReadingDataPagingList.init(new ArrayList<>(), getPageDataCount());
        this.billingAmountDataPagingList.init(new ArrayList<>());
        this.selectBillingDataPagingList.init(new ArrayList<>());

        meterReadingDataBeanProperty.setDevIdMap(new HashMap<String, String>());
        meterReadingDataBeanProperty.setMonthMeterReadingNoList(new ArrayList<>());
        meterReadingDataBeanProperty.setYearList(new ArrayList<>());
        meterReadingDataBeanProperty.setMonthList(new ArrayList<>());

        meterReadingDataBeanProperty.setSearchFlg(Boolean.FALSE);

        meterReadingDataBeanProperty.setProratedChargeFlg(Boolean.TRUE);
        meterReadingDataBeanProperty.setFixedCostFlg(Boolean.TRUE);

        viewMeterReadingDataClick(Boolean.TRUE);
        billingAmountDataBeanProperty.setPrintBuildingNoFrom("");
        billingAmountDataBeanProperty.setPrintBuildingNoTo("");
        billingAmountDataBeanProperty.setDateIssue("");
        billingAmountDataBeanProperty.setBillingDate("");
        changeInputDateIssue();
        billingAmountDataBeanProperty.setDeadLine("");
        changeInputDeadLine();
        billingAmountDataBeanProperty.setJapaneseCalendar(Boolean.FALSE);
        billingAmountDataBeanProperty.setClaimantName("");
        billingAmountDataBeanProperty.setRegNo("");
        billingAmountDataBeanProperty.setInvoice(Boolean.FALSE);
        billingAmountDataBeanProperty.setReceipt(Boolean.FALSE);
        billingAmountDataBeanProperty.setInvoiceAndReceiptCopy(Boolean.FALSE);

        this.buildingInfo = buildingInfo;

        String corpType = this.getLoginCorpType();
        String authorityType = this.getLoginOperationCorpAuthorityType();

        //月検針連番
        //接続先
        if(corpType.equals("1") && authorityType.equals("1") && isOcrAuth()) {
            //パートナー企業でOCR権限がある場合
            CorpPersonAuthParam corpPersonAuthParam = new CorpPersonAuthParam(this.getLoginCorpId(), this.getLoginPersonId(), this.getLoginOperationCorpId());
            meterReadingDataBeanProperty.setDevIdMap(toolsPullDownList.getOcrDevPrm(this.buildingInfo.getCorpId(), Long.parseLong(this.buildingInfo.getBuildingId()), corpPersonAuthParam));
        }else {
            meterReadingDataBeanProperty.setDevIdMap(toolsPullDownList.getDevPrm(this.buildingInfo.getCorpId(), Long.parseLong(this.buildingInfo.getBuildingId())));
        }


        if(0 < meterReadingDataBeanProperty.getDevIdMap().size()){
            List<String> devIdList = null;
            devIdList = new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values());
            meterReadingDataBeanProperty.setDevId(devIdList.get(0));
        }

        // 建物情報
        TBuilding tBuilding =
                buildingInfoDao.getBuildingInfo(
                        buildingInfo.getCorpId(), Long.valueOf(buildingInfo.getBuildingId()));
        boolean isAllDisplay = DataViewUtility.hasAllDevicesDisplayInBiko(tBuilding.getBiko());
        Map<String, String> devIdMap = meterReadingDataBeanProperty.getDevIdMap();
        if (0 < devIdMap.size()) {
            if (isAllDisplay) {
                //建物情報備考欄に「全装置表示」の記載がある場合
                devIdMap.put("全て", "0");
                meterReadingDataBeanProperty.setDevIdMap(devIdMap);
                // 「すべて」初期表示にするので"0"を入れる
                meterReadingDataBeanProperty.setDevId("0");
            } else {
                meterReadingDataBeanProperty.setDevIdMap(devIdMap);
            }
        }
        //表示年月
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //表示年
        List<String> yearList = new ArrayList<>();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String year = yearFormat.format(cal.getTime());
        meterReadingDataBeanProperty.setYear(year);
        meterReadingDataBeanProperty.setSearchYear(year);
        meterReadingDataBeanProperty.setFromYear(year);
        meterReadingDataBeanProperty.setSearchFromYear(year);
        meterReadingDataBeanProperty.setToYear(year);
        meterReadingDataBeanProperty.setSearchToYear(year);
        yearList.add(year);
        while (SmsConstants.OLDEST_DISP_YEAR != Integer.parseInt(year)) {
            cal.add(Calendar.YEAR, -1);
            year = yearFormat.format(cal.getTime());
            yearList.add(year);
        }
        meterReadingDataBeanProperty.setYearList(yearList);
        //表示月
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        String month = monthFormat.format(cal.getTime());
        meterReadingDataBeanProperty.setMonth(month);
        meterReadingDataBeanProperty.setSearchMonth(month);
        meterReadingDataBeanProperty.setFromMonth(month);
        meterReadingDataBeanProperty.setSearchFromMonth(month);
        meterReadingDataBeanProperty.setToMonth(month);
        meterReadingDataBeanProperty.setSearchToMonth(month);
        List<String> monthList = new ArrayList<>();
        for (Integer i = 1; i <= 12; i++) {
            monthList.add(i.toString());
        }
        meterReadingDataBeanProperty.setMonthList(monthList);

        getMonthMeterReadingNo();

        return init();
    }

    /**
     * 月検針連番取得.
     *
     */
    public void getMonthMeterReadingNo() {
        // アクセスログ出力
        exportAccessLog("getMonthMeterReadingNo", "「表示条件」選択");

        ListSmsMonthMeterReadingNoParameter parameter = new ListSmsMonthMeterReadingNoParameter();

        parameter.setBean("listSmsMonthMeterReadingNoSearchBean");

        if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) {
            if (CheckUtility.isNullOrEmpty(meterReadingDataBeanProperty.getDevId())) {
                return;
            } else {
                if (meterReadingDataBeanProperty.getDevId().equals("0")) {
                    List<String> devIdList = new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values());
                    parameter.setDevIdListStr(String.join(",", devIdList));
                }
                parameter.setDevId(meterReadingDataBeanProperty.getDevId());
            }
        } else {
            if (null == meterReadingDataBeanProperty.getDevIdMap()
                    || 0 == meterReadingDataBeanProperty.getDevIdMap().size()) {
                return;
            } else {
                List<String> devIdList = new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values());
                parameter.setDevIdListStr(String.join(",", devIdList));
            }
        }

        parameter.setYear(meterReadingDataBeanProperty.getYear());
        parameter.setMonth(meterReadingDataBeanProperty.getMonth());

        ListSmsMonthMeterReadingNoResponse response = callApiPost(parameter, ListSmsMonthMeterReadingNoResponse.class);

        if (null != response && null != response.getResult()) {
            ListSmsMeterReadingDataMonthNoDataResultData monthMeterReadingNoDataResultData = response.getResult()
                    .getMonthMeterReadingNoDataResultData();

            List<String> monthMeterReadingNoList = new ArrayList<>();

            for (ListSmsMeterReadingDataMonthNoResultData monthMeterReadingNo : monthMeterReadingNoDataResultData
                    .getMonthMeterReadingNoResultDataList()) {

                String data = String.format("%03d%s", monthMeterReadingNo.getMonthMeterReadingNo(),
                        monthMeterReadingNo.getMeterReadingType());

                if (!monthMeterReadingNoList.contains(data)) {

                    monthMeterReadingNoList.add(data);
                }
            }

            meterReadingDataBeanProperty.setMonthMeterReadingNoList(monthMeterReadingNoList);

            if (monthMeterReadingNoList.size() == 0) {
                meterReadingDataBeanProperty.setBtnReloadDisabled(true);
            } else {
                meterReadingDataBeanProperty.setBtnReloadDisabled(false);
            }
        }
    }

    private Map<String, List<ListSmsMeterReadingDataResultData>> createReadingDataMap(
            List<ListSmsMeterReadingDataResultData> list, List<String> devIdList) {
        Map<String, List<ListSmsMeterReadingDataResultData>> map = new HashMap<>();
        for (String devId : devIdList) {
            for (ListSmsMeterReadingDataResultData data : list) {
                if (devId.equals(data.getDevId())) {
                    List<ListSmsMeterReadingDataResultData> dataList = map.getOrDefault(devId, new ArrayList<>());
                    dataList.add(data);
                    map.put(data.getDevId(), dataList);
                }
            }
        }
        return map;
    }

    /**
     * 表示更新.
     *
     */
    public void reload() {
        // アクセスログ出力
        exportAccessLog("reload", "ボタン「表示更新」押下");

        if (!searchValidation()) {
            return;
        }

        meterReadingDataBeanProperty.setSearchFlg(Boolean.TRUE);

        if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) { // true:検針データ
            ListSmsMeterReadingDataParameter parameter = new ListSmsMeterReadingDataParameter();
            parameter.setBean("listSmsMeterReadingDataSearchBean");
            parameter.setCorpId(this.buildingInfo.getCorpId());
            parameter.setBuildingId(Long.parseLong(this.buildingInfo.getBuildingId()));
            parameter.setDevId(meterReadingDataBeanProperty.getDevId());
            if (meterReadingDataBeanProperty.getDevId().equals("0")) {
                List<String> devIdList = new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values());
                parameter.setDevIdListStr(String.join(",", devIdList));
            }
            parameter.setYear(meterReadingDataBeanProperty.getYear());
            parameter.setMonth(meterReadingDataBeanProperty.getMonth());
            parameter.setMonthMeterReadingNo(
                    Long.parseLong(meterReadingDataBeanProperty.getMonthMeterReadingNo().substring(0, 3)));
            parameter.setMeterReadingType(meterReadingDataBeanProperty.getMonthMeterReadingNo().substring(3, 4));

            ListSmsMeterReadingDataResponse response = callApiPost(parameter, ListSmsMeterReadingDataResponse.class);

            if (Objects.nonNull(response) && OsolApiResultCode.API_OK.equals(response.getResultCode())
                    && Objects.nonNull(response.getResult())) {
                List<ListSmsMeterReadingDataResultData> list = new ArrayList<>();
                // 全装置か１装置だけかの判別を行う
                if (!meterReadingDataBeanProperty.getDevId().equals("0")) {
                    list = response.getResult().getMeterReadingDataResultDataList();
                    // 担当者が権限のあるメーターに絞り込む
                    list = meterDataFilterDao.applyDataFilter(list, new BuildingPersonDevDataParam(
                            this.buildingInfo.getCorpId(), Long.parseLong(this.buildingInfo.getBuildingId()),
                            this.getLoginCorpId(), this.getLoginPersonId(),
                            meterReadingDataBeanProperty.getDevId()));
                } else {
                    List<String> idList = new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values());
                    Map<String, List<ListSmsMeterReadingDataResultData>> dataMap = createReadingDataMap(
                            response.getResult().getMeterReadingDataResultDataList(), idList);
                    for (String retId : idList) {
                        if (dataMap.containsKey(retId)) {
                            // 担当者が権限のあるメーターに絞り込む
                            list.addAll(meterDataFilterDao.applyDataFilter(dataMap.get(retId),
                                    new BuildingPersonDevDataParam(
                                            this.buildingInfo.getCorpId(),
                                            Long.parseLong(this.buildingInfo.getBuildingId()), this.getLoginCorpId(),
                                            this.getLoginPersonId(),
                                            retId)));
                        }
                    }
                }

                // ハンディー端末の場合、最大デマンド値（kW）を表示しない
                if (parameter.getDevId().startsWith(DEVICE_KIND.HANDY.getVal())) {
                    list.stream().forEach(row -> row.setMaxDemandVal(null));
                }

                // 全装置のときだけ装置名をSET
                if (meterReadingDataBeanProperty.getDevId().equals("0")) {
                    // 装置名をセット
                    BidiMap<String, String> bidiMap = new DualHashBidiMap<String, String>(
                            meterReadingDataBeanProperty.getDevIdMap());
                    for (ListSmsMeterReadingDataResultData data : list) {
                        data.setDevName(bidiMap.getKey(data.getDevId()));
                    }
                }

                meterReadingDataPagingList.init(list, getPageDataCount());

            } else {
                addErrorMessage(
                        beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            }
            meterReadingDataBeanProperty
                    .setSearchMonthMeterReadingNo(meterReadingDataBeanProperty.getMonthMeterReadingNo());
            meterReadingDataBeanProperty.setSearchYear(meterReadingDataBeanProperty.getYear());
            meterReadingDataBeanProperty.setSearchMonth(meterReadingDataBeanProperty.getMonth());

        } else { // false:請求金額データ

            //1.検針年月を取得する
            //2.APIを実行し、メーター使用料金を取得する
            //リクエスト：企業ID/建物ID/開始年・月/終了年・月
            ListSmsSelectBillingDataParameter parameter = new ListSmsSelectBillingDataParameter();
            parameter.setCorpId(this.buildingInfo.getCorpId());
            parameter.setBuildingId(Long.parseLong(this.buildingInfo.getBuildingId()));
            parameter.setFromYear(meterReadingDataBeanProperty.getFromYear());
            parameter.setToYear(meterReadingDataBeanProperty.getToYear());
            parameter.setFromMonth(meterReadingDataBeanProperty.getFromMonth());
            parameter.setToMonth(meterReadingDataBeanProperty.getToMonth());

            // レスポンス：検針日/検針連番/種別/メーター台数/検針未完了の台数
            parameter.setBean("listSmsSelectBillingDataBean");
            ListSmsSelectBillingDataResponse response = callApiPost(parameter, ListSmsSelectBillingDataResponse.class);

            // データを表示するためにListを作る
            List<ListSmsSelectBillingResultDate> list = new ArrayList<>();
            if (Objects.nonNull(response) && OsolApiResultCode.API_OK.equals(response.getResultCode())
                    && Objects.nonNull(response.getResult())) {
                list = response.getResult().getSelectBillingResultDateListList();

                selectBillingDataPagingList.init(list, 1000);

            }

            //検索条件をmeterReadingDataBeanPropertyに詰める
            meterReadingDataBeanProperty.setSearchFromYear(meterReadingDataBeanProperty.getFromYear());
            meterReadingDataBeanProperty.setSearchFromMonth(meterReadingDataBeanProperty.getFromMonth());
            meterReadingDataBeanProperty.setSearchToYear(meterReadingDataBeanProperty.getToYear());
            meterReadingDataBeanProperty.setSearchToMonth(meterReadingDataBeanProperty.getToMonth());

        }
    }

    /**
     * 帳票出力(CSV).
     */
    public String downloadCsvFile() {
        // アクセスログ出力
        exportAccessLog("downloadCsvFile", "ボタン「帳票出力」押下");

        // 1装置だけの場合
        if (!"0".equals(meterReadingDataBeanProperty.getDevId())) {
            createCsvDevOnly();
        } else {
            //全装置
            createCsvDevAll();
        }

        return STR_EMPTY;
    }

    /**
     * CSV 1装置だけの場合
     */
    private void createCsvDevOnly() {

        Boolean errorFlg = false;
        String outputDir = null;
        List<List<String>> csvList = new ArrayList<>();

        if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) {

            String devName = "";
            ;
            for (Map.Entry<String, String> entry : meterReadingDataBeanProperty.getDevIdMap().entrySet()) {
                if (Objects.equals(entry.getValue(), meterReadingDataBeanProperty.getDevId())) {
                    devName = entry.getKey();
                }

            }
            String downloadFileName = String.format("検針データ_%s_%s_%s.csv",
                    this.buildingInfo.getBuildingName(), devName,
                    DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            saveFilename = downloadFileName;

            outputDir = getCsvOutputDir();
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            //タイトル
            List<String> titleList = new ArrayList<>();
            titleList.add("管理番号");
            titleList.add("テナント名");
            titleList.add("種別");
            titleList.add("今回検針値");
            titleList.add("前回検針値");
            titleList.add("乗率");
            titleList.add("今回使用量");
            titleList.add("前回使用量");
            titleList.add("使用量率");
            titleList.add("ユーザーコード");
            titleList.add("今回検針日時");
            titleList.add("ステータス");
            titleList.add("前回検針日時");
            titleList.add("前年同月検針日時");
            titleList.add("前年同月使用量");
            titleList.add("最大デマンド値（kW）");
            csvList.add(titleList);

            for (ListSmsMeterReadingDataResultData result : meterReadingDataPagingList.getAllList()) {
                //データ
                List<String> dataList = new ArrayList<>();
                dataList.add(result.getMeterMngId().toString()); // 管理番号 (型:文字)
                dataList.add(result.getBuildingName()); // テナント名 (型:文字)
                dataList.add(nvlStr(result.getMeterTypeName())); // 種別 (型:文字)
                dataList.add(nvlStr(result.getLatestInspVal())); // 今回検針値 (型:数)
                dataList.add(nvlStr(result.getPrevInspVal())); // 前回検針値 (型:数)
                dataList.add(result.getMultipleRate()); // 乗率 (型:数)
                dataList.add(nvlStr(result.getLatestUseVal())); // 今回使用量 (型:数)
                dataList.add(nvlStr(result.getPrevUseVal())); // 前回使用量 (型:数)
                dataList.add(result.getUsePerRate() == null || result.getUsePerRate().isEmpty() ? "---.--"
                        : result.getUsePerRate()); // 使用量率 (型:数)
                dataList.add(Objects.isNull(result.getTenantId()) ? "" : result.getTenantId().toString()); // ユーザーコード
                dataList.add(result.getLatestInspDate()); // 今回検針日時 (型:文字)
                dataList.add(result.getEndFlg()); // ステータス (型:文字)
                dataList.add(result.getPrevInspDate()); // 前回検針日時 (型:文字)
                dataList.add(result.getPreviousYearInspDate()); // 前年同月検針日時 (型:文字)
                dataList.add(result.getPreviousYearUseVal()); // 前年同月使用量 (型:数)
                dataList.add(result.getMaxDemandVal()); // 最大デマンド値（kW） (型:数)
                csvList.add(dataList);
            }
        } else {
            String downloadFileName = String.format("請求金額データ_%s_%s.csv",
                    this.buildingInfo.getBuildingName(),
                    DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            saveFilename = downloadFileName;

            outputDir = getCsvOutputDir();
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            //タイトル
            List<String> titleList = new ArrayList<>();

            titleList.add("ユーザーコード");
            titleList.add("テナント番号");
            titleList.add("テナント名");

            titleList.add("料金メニュー");
            titleList.add("装置ID");
            titleList.add("メーター管理番号");
            titleList.add("コメント");
            titleList.add("計器ID");
            titleList.add("メーター種別名");
            titleList.add("今回検針値");
            titleList.add("前回検針値");
            titleList.add("乗率");
            titleList.add("使用量");
            titleList.add("基本料金");
            titleList.add("従量料金");
            titleList.add("燃料費調整額");
            titleList.add("再生可能エネルギー発電促進賦課金");
            titleList.add("割引額");
            titleList.add("使用料金");

            // データ先頭行のみ出力
            titleList.add("按分料金(グループ1)");
            titleList.add("按分料金(グループ2)");
            titleList.add("按分料金(グループ3)");
            titleList.add("按分料金(グループ4)");
            titleList.add("按分料金(グループ5)");
            titleList.add("按分料金(グループ6)");
            titleList.add("按分料金(グループ7)");
            titleList.add("按分料金(グループ8)");
            titleList.add("按分料金(グループ9)");
            titleList.add("按分料金(グループ10)");
            titleList.add("グループ按分小計");
            titleList.add("固定費1名称");
            titleList.add("固定費1費用");
            titleList.add("固定費2名称");
            titleList.add("固定費2費用");
            titleList.add("固定費3名称");
            titleList.add("固定費3費用");
            titleList.add("固定費4名称");
            titleList.add("固定費4費用");
            titleList.add("固定費小計");

            titleList.add("今回請求金額");
            titleList.add("消費税");

            csvList.add(titleList);

            boolean status = true;

            for (BillingAmountDataByTenant tenant : billingAmountDataPagingList.getAllList()) {

            	eventLogger.debug("***** 帳票CSV出力 テナント情報" + tenant);

            	boolean isFirstLine = true;

            	// 完了のみに限定
            	List<BillingAmountDetailData> outputTargetDetails = tenant.getBillingAmountDetails().stream()
            			.filter(detail -> detail.getStatus().equals("完了")) // "完了"のみでフィルタ
            			.sorted(Comparator.comparing(BillingAmountDetailData::getMeterMngId))
            			.collect(Collectors.toList()); //

            	// ステータスが"完了"でのフィルタ前後で件数に変動があった場合は"未完了"を含んでいる
            	if (tenant.getBillingAmountDetails().size() != outputTargetDetails.size()) {
            		status = false;
            	}

            	for (BillingAmountDetailData detail : outputTargetDetails) {

            		List<String> dataList = new ArrayList<>();

					dataList.add(String.valueOf(tenant.getTenantId())); // ユーザーコード
					dataList.add(tenant.getBuildingNo()); // テナント番号
					dataList.add(tenant.getBuildingName()); // テナント名

					dataList.add(nvlStr(detail.getPriceMenuName())); // 料金メニュー
					dataList.add(detail.getDevId()); // 装置ID
					dataList.add(String.valueOf(detail.getMeterMngId())); // メーター管理番号
					dataList.add(detail.getMemo()); // コメント
					dataList.add(detail.getMeterId()); // 計器ID
					dataList.add(nvlStr(detail.getMeterTypeName())); // メーター種別名
					dataList.add(nvlStr(detail.getThisInspVal())); // 今回検針値
					dataList.add(nvlStr(detail.getPrevInspVal())); // 前回検針値
					dataList.add(detail.getMultipleRate()); // 乗率
					dataList.add(nvlNum(detail.getUsageVal())); // 使用量
					dataList.add(nvlNum(detail.getBasicCharge())); // 基本料金
					dataList.add(nvlNum(detail.getUsageVolumeFee())); // 従量料金
					dataList.add(nvlNum(detail.getFuelAdjustmentFee())); // 燃料費調整額
					dataList.add(nvlNum(detail.getRenewableEnergyLevy())); // 再生可能エネルギー発電促進賦課金
					dataList.add(nvlNum(detail.getDiscountAmount())); // 割引額
					dataList.add(nvlNum(detail.getUsageFee())); // 使用料金

            		if (isFirstLine) {
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ1)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ2)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ3)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ4)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ5)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ6)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ7)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ8)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ9)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ10)
            			dataList.add(nvlNum(null)); // FIXME グループ按分小計

            			dataList.add(nvlStr(tenant.getFixedCosts().get(0).getFixedCostName())); // 固定費1名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(0).getFixedCost())); // 固定費1費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(1).getFixedCostName())); // 固定費2名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(1).getFixedCost())); // 固定費2費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(2).getFixedCostName())); // 固定費3名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(2).getFixedCost())); // 固定費3費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(3).getFixedCostName())); // 固定費4名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(3).getFixedCost())); // 固定費4費用
            			dataList.add(nvlNum(tenant.getFixedCostSubtotal())); // 固定費小計

    					dataList.add(nvlNum(tenant.getSumUsageFee())); // 今回請求金額
    					dataList.add(nvlNum(tenant.getSumTax())); // 消費税
            		}
            		else {
            			dataList.add(nvlStr(null)); // 按分料金(グループ1)
            			dataList.add(nvlStr(null)); // 按分料金(グループ2)
            			dataList.add(nvlStr(null)); // 按分料金(グループ3)
            			dataList.add(nvlStr(null)); // 按分料金(グループ4)
            			dataList.add(nvlStr(null)); // 按分料金(グループ5)
            			dataList.add(nvlStr(null)); // 按分料金(グループ6)
            			dataList.add(nvlStr(null)); // 按分料金(グループ7)
            			dataList.add(nvlStr(null)); // 按分料金(グループ8)
            			dataList.add(nvlStr(null)); // 按分料金(グループ9)
            			dataList.add(nvlStr(null)); // 按分料金(グループ10)
            			dataList.add(nvlStr(null)); // グループ按分小計

            			dataList.add(nvlStr(null)); //  固定費1名称
            			dataList.add(nvlStr(null)); // 固定費1費用
            			dataList.add(nvlStr(null)); // 固定費2名称
            			dataList.add(nvlStr(null)); // 固定費2費用
            			dataList.add(nvlStr(null)); // 固定費3名称
            			dataList.add(nvlStr(null)); // 固定費3費用
            			dataList.add(nvlStr(null)); // 固定費4名称
            			dataList.add(nvlStr(null)); // 固定費4費用
            			dataList.add(nvlStr(null)); // 固定費小計

    					dataList.add(nvlStr(null)); // 今回請求金額
    					dataList.add(nvlStr(null)); // 消費税
            		}

            		csvList.add(dataList);

            		isFirstLine = false;
            	}
            }

            if (false == status) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.status"));
                errorFlg = true;
            }
        }

        if (!errorFlg) {
            new MeterReadingCsvConverter().csvPrint(outputDir, saveFilename, csvList);
        }
    }

    /**
     * 全装置の場合
     */
    private void createCsvDevAll() {

        Boolean errorFlg = false;
        String outputDir = null;
        List<List<String>> csvList = new ArrayList<>();

        if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) {

            String devName = "";
            ;
            for (Map.Entry<String, String> entry : meterReadingDataBeanProperty.getDevIdMap().entrySet()) {
                if (Objects.equals(entry.getValue(), meterReadingDataBeanProperty.getDevId())) {
                    devName = entry.getKey();
                }

            }
            String downloadFileName = String.format("検針データ_%s_%s_%s.csv",
                    this.buildingInfo.getBuildingName(), devName,
                    DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            saveFilename = downloadFileName;

            outputDir = getCsvOutputDir();
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            //タイトル
            List<String> titleList = new ArrayList<>();
            titleList.add("装置名");
            titleList.add("管理番号");
            titleList.add("テナント名");
            titleList.add("種別");
            titleList.add("今回検針値");
            titleList.add("前回検針値");
            titleList.add("乗率");
            titleList.add("今回使用量");
            titleList.add("前回使用量");
            titleList.add("使用量率");
            titleList.add("ユーザーコード");
            titleList.add("今回検針日時");
            titleList.add("ステータス");
            titleList.add("前回検針日時");
            titleList.add("前年同月検針日時");
            titleList.add("前年同月使用量");
            titleList.add("最大デマンド値（kW）");
            csvList.add(titleList);

            for (ListSmsMeterReadingDataResultData result : meterReadingDataPagingList.getAllList()) {
                //データ
                List<String> dataList = new ArrayList<>();
                dataList.add(result.getDevName()); // 装置名 (型:文字)
                dataList.add(result.getMeterMngId().toString()); // 管理番号 (型:文字)
                dataList.add(result.getBuildingName()); // テナント名 (型:文字)
                dataList.add(nvlStr(result.getMeterTypeName())); // 種別 (型:文字)
                dataList.add(nvlStr(result.getLatestInspVal())); // 今回検針値 (型:数)
                dataList.add(nvlStr(result.getPrevInspVal())); // 前回検針値 (型:数)
                dataList.add(result.getMultipleRate()); // 乗率 (型:数)
                dataList.add(nvlStr(result.getLatestUseVal())); // 今回使用量 (型:数)
                dataList.add(nvlStr(result.getPrevUseVal())); // 前回使用量 (型:数)
                dataList.add(result.getUsePerRate() == null || result.getUsePerRate().isEmpty() ? "---.--"
                        : result.getUsePerRate()); // 使用量率 (型:数)
                dataList.add(Objects.isNull(result.getTenantId()) ? "" : result.getTenantId().toString()); // ユーザーコード
                dataList.add(result.getLatestInspDate()); // 今回検針日時 (型:文字)
                dataList.add(result.getEndFlg()); // ステータス (型:文字)
                dataList.add(result.getPrevInspDate()); // 前回検針日時 (型:文字)
                dataList.add(result.getPreviousYearInspDate()); // 前年同月検針日時 (型:文字)
                dataList.add(result.getPreviousYearUseVal()); // 前年同月使用量 (型:数)
                dataList.add(result.getMaxDemandVal()); // 最大デマンド値（kW） (型:数)
                csvList.add(dataList);
            }
        } else {
            String downloadFileName = String.format("請求金額データ_%s_%s.csv",
                    this.buildingInfo.getBuildingName(),
                    DateUtility.changeDateFormat(mDevPrmListDao.getSvDate(), DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

            saveFilename = downloadFileName;

            outputDir = getCsvOutputDir();
            downloadFilePath = Paths.get(outputDir, downloadFileName).toString();

            //タイトル
            List<String> titleList = new ArrayList<>();

            titleList.add("ユーザーコード");
            titleList.add("テナント番号");
            titleList.add("テナント名");

            titleList.add("料金メニュー");
            titleList.add("装置ID");
            titleList.add("メーター管理番号");
            titleList.add("コメント");
            titleList.add("計器ID");
            titleList.add("メーター種別名");
            titleList.add("今回検針値");
            titleList.add("前回検針値");
            titleList.add("乗率");
            titleList.add("使用量");
            titleList.add("基本料金");
            titleList.add("従量料金");
            titleList.add("燃料費調整額");
            titleList.add("再生可能エネルギー発電促進賦課金");
            titleList.add("割引額");
            titleList.add("使用料金");

            // データ先頭行のみ出力
            titleList.add("按分料金(グループ1)");
            titleList.add("按分料金(グループ2)");
            titleList.add("按分料金(グループ3)");
            titleList.add("按分料金(グループ4)");
            titleList.add("按分料金(グループ5)");
            titleList.add("按分料金(グループ6)");
            titleList.add("按分料金(グループ7)");
            titleList.add("按分料金(グループ8)");
            titleList.add("按分料金(グループ9)");
            titleList.add("按分料金(グループ10)");
            titleList.add("グループ按分小計");
            titleList.add("固定費1名称");
            titleList.add("固定費1費用");
            titleList.add("固定費2名称");
            titleList.add("固定費2費用");
            titleList.add("固定費3名称");
            titleList.add("固定費3費用");
            titleList.add("固定費4名称");
            titleList.add("固定費4費用");
            titleList.add("固定費小計");

            titleList.add("今回請求金額");
            titleList.add("消費税");

            csvList.add(titleList);

            boolean status = true;

            for (BillingAmountDataByTenant tenant : billingAmountDataPagingList.getAllList()) {

            	eventLogger.debug("***** 帳票CSV出力 テナント情報" + tenant);

            	boolean isFirstLine = true;

            	// 完了のみに限定
            	List<BillingAmountDetailData> outputTargetDetails = tenant.getBillingAmountDetails().stream()
            			.filter(detail -> detail.getStatus().equals("完了")) // "完了"のみでフィルタ
            			.sorted(Comparator.comparing(BillingAmountDetailData::getMeterMngId))
            			.collect(Collectors.toList()); //

            	// ステータスが"完了"でのフィルタ前後で件数に変動があった場合は"未完了"を含んでいる
            	if (tenant.getBillingAmountDetails().size() != outputTargetDetails.size()) {
            		status = false;
            	}

            	for (BillingAmountDetailData detail : outputTargetDetails) {

            		List<String> dataList = new ArrayList<>();

					dataList.add(String.valueOf(tenant.getTenantId())); // ユーザーコード
					dataList.add(tenant.getBuildingNo()); // テナント番号
					dataList.add(tenant.getBuildingName()); // テナント名

					dataList.add(nvlStr(detail.getPriceMenuName())); // 料金メニュー
					dataList.add(detail.getDevId()); // 装置ID
					dataList.add(String.valueOf(detail.getMeterMngId())); // メーター管理番号
					dataList.add(detail.getMemo()); // コメント
					dataList.add(detail.getMeterId()); // 計器ID
					dataList.add(nvlStr(detail.getMeterTypeName())); // メーター種別名
					dataList.add(nvlStr(detail.getThisInspVal())); // 今回検針値
					dataList.add(nvlStr(detail.getPrevInspVal())); // 前回検針値
					dataList.add(detail.getMultipleRate()); // 乗率
					dataList.add(nvlNum(detail.getUsageVal())); // 使用量
					dataList.add(nvlNum(detail.getBasicCharge())); // 基本料金
					dataList.add(nvlNum(detail.getUsageVolumeFee())); // 従量料金
					dataList.add(nvlNum(detail.getFuelAdjustmentFee())); // 燃料費調整額
					dataList.add(nvlNum(detail.getRenewableEnergyLevy())); // 再生可能エネルギー発電促進賦課金
					dataList.add(nvlNum(detail.getDiscountAmount())); // 割引額
					dataList.add(nvlNum(detail.getUsageFee())); // 使用料金

            		if (isFirstLine) {
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ1)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ2)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ3)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ4)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ5)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ6)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ7)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ8)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ9)
            			dataList.add(nvlNum(null)); // FIXME 按分料金(グループ10)
            			dataList.add(nvlNum(null)); // FIXME グループ按分小計

            			dataList.add(nvlStr(tenant.getFixedCosts().get(0).getFixedCostName())); // 固定費1名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(0).getFixedCost())); // 固定費1費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(1).getFixedCostName())); // 固定費2名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(1).getFixedCost())); // 固定費2費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(2).getFixedCostName())); // 固定費3名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(2).getFixedCost())); // 固定費3費用
            			dataList.add(nvlStr(tenant.getFixedCosts().get(3).getFixedCostName())); // 固定費4名称
            			dataList.add(nvlNum(tenant.getFixedCosts().get(3).getFixedCost())); // 固定費4費用
            			dataList.add(nvlNum(tenant.getFixedCostSubtotal())); // 固定費小計

    					dataList.add(nvlNum(tenant.getSumUsageFee())); // 今回請求金額
    					dataList.add(nvlNum(tenant.getSumTax())); // 消費税
            		}
            		else {
            			dataList.add(nvlStr(null)); // 按分料金(グループ1)
            			dataList.add(nvlStr(null)); // 按分料金(グループ2)
            			dataList.add(nvlStr(null)); // 按分料金(グループ3)
            			dataList.add(nvlStr(null)); // 按分料金(グループ4)
            			dataList.add(nvlStr(null)); // 按分料金(グループ5)
            			dataList.add(nvlStr(null)); // 按分料金(グループ6)
            			dataList.add(nvlStr(null)); // 按分料金(グループ7)
            			dataList.add(nvlStr(null)); // 按分料金(グループ8)
            			dataList.add(nvlStr(null)); // 按分料金(グループ9)
            			dataList.add(nvlStr(null)); // 按分料金(グループ10)
            			dataList.add(nvlStr(null)); // グループ按分小計

            			dataList.add(nvlStr(null)); //  固定費1名称
            			dataList.add(nvlStr(null)); // 固定費1費用
            			dataList.add(nvlStr(null)); // 固定費2名称
            			dataList.add(nvlStr(null)); // 固定費2費用
            			dataList.add(nvlStr(null)); // 固定費3名称
            			dataList.add(nvlStr(null)); // 固定費3費用
            			dataList.add(nvlStr(null)); // 固定費4名称
            			dataList.add(nvlStr(null)); // 固定費4費用
            			dataList.add(nvlStr(null)); // 固定費小計

    					dataList.add(nvlStr(null)); // 今回請求金額
    					dataList.add(nvlStr(null)); // 消費税
            		}

            		csvList.add(dataList);

            		isFirstLine = false;
            	}
            }

            if (false == status) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.status"));
                errorFlg = true;
            }
        }

        if (!errorFlg) {
            new MeterReadingCsvConverter().csvPrint(outputDir, saveFilename, csvList);
        }
    }

    /**
     * 印刷出力(PDF).
     */
    public String downloadPdfFile() {
        // アクセスログ出力
        exportAccessLog("downloadPdfFile", "ボタン「印刷」押下");

        savePdfFileList = new ArrayList<>();

        // PDF出力可能かのチェック
        if (!validation()) {
            return STR_EMPTY;
        }

        UpdateSmsClaimantInfoParameter parameter = new UpdateSmsClaimantInfoParameter();

        parameter.setBean("updateSmsClaimantInfoBean");
        parameter.setCorpId(this.buildingInfo.getCorpId());
        parameter.setBuildingId(Long.parseLong(this.buildingInfo.getBuildingId()));
        parameter.setPersonCorpId(this.getLoginCorpId());
        parameter.setPersonId(this.getLoginPersonId());
        parameter.setClaimantName(billingAmountDataBeanProperty.getClaimantName());
        parameter.setRegNo(billingAmountDataBeanProperty.getRegNo());

        if (null != claimantData) {
            parameter.setVersion(claimantData.getVersion());
        }

        UpdateSmsClaimantInfoResponse response = callApiPost(parameter, UpdateSmsClaimantInfoResponse.class);

        if (Objects.isNull(response)) {
            // (発生しないはず。念のための処理)
            addErrorMessage(beanMessages
                    .getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(OsolApiResultCode.API_ERROR_UNKNOWN)));
            return STR_EMPTY;
        } else if (!OsolApiResultCode.API_OK.equals(response.getResultCode())) {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response.getResultCode())));
            return STR_EMPTY;
        } else if (Objects.isNull(response.getResult())) {
            addErrorMessage(beanMessages
                    .getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(OsolApiResultCode.API_ERROR_UNKNOWN)));
            return STR_EMPTY;
        }

        claimantData = response.getResult().getClaimantInfoResultData();

        billingAmountDataBeanProperty.getBillingDate();

        String title = "";
        String claimDate = "";
        String dateIssue = billingAmountDataBeanProperty.getDateIssue();
        String deadLineDate = billingAmountDataBeanProperty.getDeadLine();

        // 請求年月
        int claimYear = Integer.parseInt(billingAmountDataBeanProperty.getBillingDate().substring(0, 4));
        int claimMonth = Integer.parseInt(billingAmountDataBeanProperty.getBillingDate().substring(5));

        // 和暦の場合
        if (billingAmountDataBeanProperty.getJapaneseCalendar().equals(Boolean.TRUE)) {

            // 発行年月日
            int issueYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(billingAmountDataBeanProperty.getCalDateIssue()));
            int issueMonth = Integer.parseInt(new SimpleDateFormat("MM").format(billingAmountDataBeanProperty.getCalDateIssue()));
            int issueDay = Integer.parseInt(new SimpleDateFormat("dd").format(billingAmountDataBeanProperty.getCalDateIssue()));

            // 納品年月日
            int deadLineYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(billingAmountDataBeanProperty.getCalDeadLine()));
            int deadLineMonth = Integer.parseInt(new SimpleDateFormat("MM").format(billingAmountDataBeanProperty.getCalDeadLine()));
            int deadLineDay = Integer.parseInt(new SimpleDateFormat("dd").format(billingAmountDataBeanProperty.getCalDeadLine()));

        	// 請求年月
        	String japaneseClaimDate = japaneseCalendarChange(claimYear, claimMonth, 1);
        	claimDate = japaneseClaimDate.substring(0, japaneseClaimDate.indexOf("月") + 1) + "分";

        	// 発行年月日
        	dateIssue = japaneseCalendarChange(issueYear, issueMonth, issueDay);

        	// 納品年月日
        	deadLineDate = japaneseCalendarChange(deadLineYear, deadLineMonth, deadLineDay);
        }
        // 西暦の場合
        else {

        	// 請求年月
        	claimDate = claimYear + "年" + claimMonth + "月分";
        }

        Date today = buildingInfoDao.getSvDate();
        boolean errFlg = false;
        if (billingAmountDataBeanProperty.getInvoice().equals(Boolean.TRUE)) {

            Map<String, Object> titleMap = new HashMap<>();

            title = "請求書";
            titleMap.put("title", title);
            titleMap.put("claimDate", claimDate);
            titleMap.put("dateIssue", dateIssue);
            titleMap.put("deadLine", "納期限");
            titleMap.put("deadLineDate", deadLineDate);
            titleMap.put("memo", "右記の料金を請求申し上げます。");
            titleMap.put("caution1", "※1「燃料調整費」が設定されている場合は、これを含んだ金額になります。");
            titleMap.put("caution2", "※2「割引率」が設定されている場合は、割引後の金額になります。");
            titleMap.put("claimantName", billingAmountDataBeanProperty.getClaimantName());
            titleMap.put("regNo", "登録番号：" + billingAmountDataBeanProperty.getRegNo());

            if (!createPdfFile(title, titleMap, today)) {
                // PDF生成失敗
                errFlg = true;
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.createPdfFile"));
                return STR_EMPTY;
            }
        }

        if (billingAmountDataBeanProperty.getReceipt().equals(Boolean.TRUE)) {

            Map<String, Object> titleMap = new HashMap<>();

            title = "領収書";
            titleMap.put("title", title);
            titleMap.put("claimDate", claimDate);
            titleMap.put("dateIssue", dateIssue);
            titleMap.put("deadLine", "納期限");
            titleMap.put("deadLineDate", deadLineDate);
            titleMap.put("memo", "右記の料金を領収いたしました。");
            titleMap.put("caution1", "※1「燃料調整費」が設定されている場合は、これを含んだ金額になります。");
            titleMap.put("caution2", "※2「割引率」が設定されている場合は、割引後の金額になります。");
            titleMap.put("claimantName", billingAmountDataBeanProperty.getClaimantName());
            titleMap.put("regNo", "登録番号：" + billingAmountDataBeanProperty.getRegNo());

            if (!createPdfFile(title, titleMap, today)) {
                // PDF生成失敗
                errFlg = true;
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.createPdfFile"));
                return STR_EMPTY;
            }
        }

        if (billingAmountDataBeanProperty.getInvoiceAndReceiptCopy().equals(Boolean.TRUE)) {

            Map<String, Object> titleMap = new HashMap<>();

            title = "請求書及び領収書控";
            titleMap.put("title", title);
            titleMap.put("claimDate", claimDate);
            titleMap.put("dateIssue", dateIssue);
            titleMap.put("deadLine", "");
            titleMap.put("deadLineDate", "");
            titleMap.put("memo", "");
            titleMap.put("caution1", "※1「燃料調整費」が設定されている場合は、これを含んだ金額になります。");
            titleMap.put("caution2", "※2「割引率」が設定されている場合は、割引後の金額になります。");
            titleMap.put("claimantName", billingAmountDataBeanProperty.getClaimantName());
            titleMap.put("regNo", "登録番号：" + billingAmountDataBeanProperty.getRegNo());

            if (!createPdfFile(title, titleMap, today)) {
                // PDF生成失敗
                errFlg = true;
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.createPdfFile"));
                return STR_EMPTY;
            }
        }

        if (!errFlg) {
            if (savePdfFileList.size() > 1) {
                //複数帳票の場合
                // ZIPファイルを作成する
                downloadFilePath = createZipFile(savePdfPaths[0]);
                // 保存ファイル名を作成
                saveFilename = createSaveFileNameForZip(today);
            }

        }

        return STR_EMPTY;
    }

    /**
     * ファイルを保存するフォルダを作成
     * @param outputDate
     * @param isCreateBuildingList
     * @return
     */
    private String[] createFolderForPdf(Date outputDate, boolean isCreateBuildingList) {

        // 出力日を文字列化
        String outputDateFormat = DateUtility.changeDateFormat(outputDate, DateUtility.DATE_FORMAT_YYYYMMDD);

        // ルートフォルダパス
        String pdfOutputRootPath = this.getPdfOutputDir().concat(File.separator).concat(outputDateFormat);
        eventLogger.debug(this.getClass().toString() + ": ■ pdfOutputRootPath:" + pdfOutputRootPath);
        File rootNewdir = new File(pdfOutputRootPath);

        // フォルダーが存在しない場合作成
        if (!rootNewdir.exists()) {
            rootNewdir.mkdir();
        }

        // ユーザーIDフォルダパス(同時出力時のファイル名かぶり回避)
        String pdfOutputUserIdFolderPath = pdfOutputRootPath.concat(File.separator).concat(getLoginUserId().toString());
        eventLogger.debug(this.getClass().toString() + ": ■ pdfOutputRootPath:" + pdfOutputUserIdFolderPath);
        File userIdNewdir = new File(pdfOutputUserIdFolderPath);

        // フォルダーが存在しない場合作成
        if (!userIdNewdir.exists()) {
            userIdNewdir.mkdir();
        }

        // 使用禁止文字を削除し、請求金額データ情報フォルダ名作成(企業名・建物名称も付記)
        String pdfFolderName = "請求金額データ_" + buildingInfo.getCorpName() + "_" + buildingInfo.getBuildingName() + "_%s";
        String buildingFolderName = String.format(pdfFolderName,
                DateUtility.changeDateFormat(outputDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

        // 請求金額データ情報用のフォルダパス
        String pdfFolderPath = pdfOutputUserIdFolderPath.concat(File.separator).concat(buildingFolderName);
        eventLogger.debug(this.getClass().toString() + ": ■ pdfFolderPath:" + pdfFolderPath);
        File pdfDir = new File(pdfFolderPath);

        // フォルダーが存在しない場合作成
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        String pdfListFolderPath = null;

        if (isCreateBuildingList) {
            // 建物情報用のフォルダパス
            pdfListFolderPath = pdfFolderPath.concat(File.separator).concat("帳票");
            eventLogger.debug(this.getClass().toString() + ": ■ pdfListFolderPath:" + pdfListFolderPath);
            File pdfListDir = new File(pdfListFolderPath);

            // フォルダーが存在しない場合作成
            if (!pdfListDir.exists()) {
                pdfListDir.mkdir();
            }
        }

        // 請求金額データフォルダパス、請求金額データリストフォルダパス
        return new String[] { pdfFolderPath, pdfListFolderPath };
    }

    /**
     * 保存ファイル名を作成。（zip）
     *
     * @param 日付
     * @return 使用禁止文字を削除した保存ファイル名
     */
    public String createSaveFileNameForZip(Date today) {
        String pdfFolderName = "請求金額データ_" + buildingInfo.getCorpName() + "_"+ buildingInfo.getBuildingName() + "_%s";

        return String.format(pdfFolderName,
                DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)) + ZIP_EXTENSION;
    }


    private String japaneseCalendarChange(Integer year, Integer month, Integer day) {

        String japaneseCalendar = "";

        DateTimeFormatter japaseseFormat = DateTimeFormatter.ofPattern("GGGGy年M月d日", Locale.JAPAN);
        JapaneseDate japaneseDate = JapaneseDate.of(year, month, day);
        japaneseCalendar = japaseseFormat.format(japaneseDate);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 9, 0, 0);

        if ((1556668800000L <= calendar.getTimeInMillis()) && (0 <= japaneseCalendar.indexOf("平成"))) {
            japaneseCalendar = "令和" + String.valueOf(year - 2019 + 1) + "年" + month + "月" + day + "日";
        }

        return japaneseCalendar;

    }

	/**
	 * PDFファイル生成
	 * @param title タイトル(ファイル名の生成に使用)
	 * @param titleMap PDFデータ
	 * @param today システム日付
	 * @return 処理結果 true:正常終了 false:エラー終了
	 */
	private boolean createPdfFile(String title, Map<String, Object> titleMap, Date today) {

		boolean isSuccess = true;

		String downloadFileName = String.format("%s_%s.pdf", title,
				billingAmountDataBeanProperty.getDateIssue().replace("/", ""));

		JasperPrint jasperPrint = null;
		byte[] byteData = null;
		Integer pageCount = 0;

		try (InputStream input = new FileInputStream(
				Paths.get(getPdfTemplateDir(), "smsBillingAmountData.jrxml").toString());) {

			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			// ページ最大数
			Integer pageMax = pdfList.size();

			for (BillingAmountDataByTenant tenant : pdfList) {

				// 完了している件数
				long completedCnt = tenant.getBillingAmountDetails().stream() //
						.filter(detail -> detail.getStatus().equals("完了")) //
						.count();

				// 対象メータ数が5以上の場合、ページ数+1
				if (completedCnt >= 5) {
					pageMax += 1;
				}
			}
			titleMap.put("pageMax", pageMax);

			for (BillingAmountDataByTenant tenant : pdfList) {

				Map<String, Object> paramMap = new HashMap<>(titleMap);

				paramMap.put("tenantId", tenant.getTenantId().toString());
				paramMap.put("buildingName", tenant.getBuildingName());
				paramMap.put("page", pageCount);

				List<BillingAmountDataPdfBeanProperty> pdfPropertyList = tenant.getBillingAmountDetails().stream() //
						.filter(detail -> detail.getStatus().equals("完了")) //
						.map(detail -> new BillingAmountDataPdfBeanProperty(detail)) //
						.collect(Collectors.toList());

				// 小計(ア)
				BigDecimal subTotal_a = new BigDecimal(tenant.getMeterSubtotal());
				// 小計(イ)
				BigDecimal subTotal_b = BigDecimal.ZERO;
				// 小計(ウ)
				BigDecimal subTotal_c = new BigDecimal(tenant.getFixedCostSubtotal());
				// 小計の合計金額
				BigDecimal taxExcludedAmount = subTotal_a.add(subTotal_b).add(subTotal_c);
				// 消費税
				BigDecimal consumptionTax = BigDecimal.ZERO;
				// 請求金額
				BigDecimal billingAmount = BigDecimal.ZERO;
				// 税率
				BigDecimal salesTaxRate = new BigDecimal(tenant.getSalesTaxRate());
				// 内税外税フラグ
				String salesTaxTreatment = tenant.getSalesTaxTreatment();

				paramMap.put("subtotal_a", subTotal_a.toString());

				// FIXME グループ按分は一旦保留
				paramMap.put("groupTotalCharges1", "0");
				paramMap.put("groupTotalChargesName1", "");
				paramMap.put("groupTotalCharges2", "0");
				paramMap.put("groupTotalChargesName2", "");
				paramMap.put("groupTotalCharges3", "0");
				paramMap.put("groupTotalChargesName3", "");
				paramMap.put("groupTotalCharges4", "0");
				paramMap.put("groupTotalChargesName4", "");
				paramMap.put("groupTotalCharges5", "0");
				paramMap.put("groupTotalChargesName5", "");
				paramMap.put("groupTotalCharges6", "0");
				paramMap.put("groupTotalChargesName6", "");
				paramMap.put("groupTotalCharges7", "0");
				paramMap.put("groupTotalChargesName7", "");
				paramMap.put("groupTotalCharges8", "0");
				paramMap.put("groupTotalChargesName8", "");
				paramMap.put("groupTotalCharges9", "0");
				paramMap.put("groupTotalChargesName9", "");
				paramMap.put("groupTotalCharges10", "0");
				paramMap.put("groupTotalChargesName10", "");

				paramMap.put("subtotal_b", "0");

				paramMap.put("fixedCostName1", tenant.getFixedCosts().get(0).getFixedCostName() == null ? "" : tenant.getFixedCosts().get(0).getFixedCostName());
				paramMap.put("fixedCost1", tenant.getFixedCosts().get(0).getFixedCost());
				paramMap.put("fixedCostName2", tenant.getFixedCosts().get(1).getFixedCostName() == null ? "" : tenant.getFixedCosts().get(1).getFixedCostName());
				paramMap.put("fixedCost2", tenant.getFixedCosts().get(1).getFixedCost());
				paramMap.put("fixedCostName3", tenant.getFixedCosts().get(2).getFixedCostName() == null ? "" : tenant.getFixedCosts().get(2).getFixedCostName());
				paramMap.put("fixedCost3", tenant.getFixedCosts().get(2).getFixedCost());
				paramMap.put("fixedCostName4", tenant.getFixedCosts().get(3).getFixedCostName() == null ? "" : tenant.getFixedCosts().get(3).getFixedCostName());
				paramMap.put("fixedCost4", tenant.getFixedCosts().get(3).getFixedCost());

				paramMap.put("subtotal_c", subTotal_c.toString());

				// 消費税
				if (salesTaxTreatment.equals(SmsConstants.TAX_TYPE.INCLUDED.getVal())) {

					// 内税
					consumptionTax = taxExcludedAmount
							.multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP))
							.divide(BigDecimal.valueOf(1)
									.add(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP)), 3,
									RoundingMode.HALF_UP);
					//請求金額
					billingAmount = taxExcludedAmount;

					paramMap.put("taxMemo", String.format("消費税相当額（%s：%s ％）", "内税", salesTaxRate.toString()));
				}
				else {

					// 外税
					consumptionTax = taxExcludedAmount
							.multiply(salesTaxRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
					//請求金額に外税加算
					billingAmount = taxExcludedAmount.add(consumptionTax);

					paramMap.put("taxMemo", String.format("消費税相当額（%s：%s ％）", "外税", salesTaxRate.toString()));
				}
				consumptionTax = fractionalProcessing(tenant.getFractionalProcessing(), consumptionTax, 0);
				// 請求金額
				billingAmount = fractionalProcessing(tenant.getFractionalProcessing(), billingAmount, 0);

				paramMap.put("salesTaxTreatment", salesTaxTreatment); // 消費税相当額 or 消費税額

				paramMap.put("taxExcludedAmount", taxExcludedAmount.toString()); // (ア)、(イ)、(ウ)の合計
				paramMap.put("consumptionTax", consumptionTax.toString()); // 税金額
				paramMap.put("billingAmount", billingAmount.toString()); // 最終的な請求金額

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(pdfPropertyList);
				JasperPrint jasperPrintElement = JasperFillManager.fillReport(jasperReport, paramMap, dataSource);

				if (null == jasperPrint) {
					jasperPrint = jasperPrintElement;
				} else {
					for (JRPrintPage page : jasperPrintElement.getPages()) {
						jasperPrint.addPage(page);
					}
				}

				pageCount += jasperPrintElement.getPages().size();
			}

			byteData = JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (Throwable t) {
			errorLogger.error("PDFファイル生成時に異常が発生しました。", t);
			isSuccess = false;
		}

		if (!isSuccess) {
			return isSuccess;
		}

		savePdfFileList.add(downloadFileName);

		//フォルダ(請求金額データ_企業名_建物名_yyyymmddhhmmss)を作成し、その中にPDFを格納する
		// ファイルを保存するフォルダを作成
		savePdfPaths = createFolderForPdf(today, false);

		try (OutputStream output = new FileOutputStream(Paths.get(savePdfPaths[0], downloadFileName).toString())) {
			output.write(byteData);
		} catch (Exception e) {
			errorLogger.error("PDFファイル生成時に異常が発生しました。", e);
			isSuccess = false;
		}

		return isSuccess;
	}

    /**
     * ファイルダウンロード開始.
     */
    public String downloadFileStartCsv() {

        if (CheckUtility.isNullOrEmpty(saveFilename)) {
            return STR_EMPTY;
        }

        fileDownloader.fileDownload(downloadFilePath, saveFilename);

        return STR_EMPTY;
    }

    /**
     * ダウンロード待ちファイル数.
     * @return ダウンロード待ちファイル数
     */
    public int getSavePdfFileCnt() {
        if (savePdfFileList == null) {
            return 0;
        }
        return savePdfFileList.size();
    }

    /**
     * ファイルダウンロード開始.
     * savePdfFileListのファイル から1件ダウンロードして、ダウンローしたファイルは savePdfFileList から削除する.
     * 作成したZipファイルをダウンロード
     */
    public String downloadFileStartPdf() {

        if (savePdfFileList != null && !savePdfFileList.isEmpty()) {
            if (savePdfFileList.size() > 1) {
                //複数帳票の場合
            	//Zip用
                if (downloadFilePath != null && saveFilename != null) {
                    downloadFile(downloadFilePath, saveFilename);
                }
            } else {
                String pdfFile = savePdfFileList.get(0);
                savePdfFileList.remove(0);

                downloadFilePath = Paths.get(savePdfPaths[0], pdfFile).toString();
                SmsFileDownload pdfFileDownloader = new SmsFileDownload();
                pdfFileDownloader.fileDownload(downloadFilePath, pdfFile);
                // 処理後ダウンロードした一時ファイルを削除する
                AnalysisEmsUtility.deleteFileDirectory(new File(downloadFilePath));

            }
        }


        return STR_EMPTY;
    }

    /**
     * 作成したファイルをダウンロードする
     *
     * @param downloadFilePath ダウンロードするファイルのパス
     * @param saveFilename 保存ファイル名
     */
    private void downloadFile(String downloadFilePath, String saveFilename) {
        eventLogger.debug(this.getClass().getName().concat(".downloadFile():START"));
        int ret = smsFileDownload.fileDownload(downloadFilePath, saveFilename);
        if (ret != RETURN_CODE.SUCCESS.getInt()) {
            eventLogger.debug(this.getClass().getName().concat(".downloadFile():Error ret(" + ret + ")"));
            addErrorMessage(beanMessages.getMessage("osol.error.fileDownload"));
        } else if (ret == RETURN_CODE.SUCCESS.getInt()) {
            // 処理後ダウンロードした一時ファイルを削除する
            AnalysisEmsUtility.deleteFileDirectory(new File(downloadFilePath));
        }
        eventLogger.debug(this.getClass().getName().concat(".downloadFile():END"));
    }


    private void viewMeterReadingDataClick(Boolean initFlg) {

        meterReadingDataBeanProperty.setTitle("検針データ");
        meterReadingDataBeanProperty.setContent("検針結果を表示します。");

        //表示条件のタイトルを検針データと請求金額データで変更
        meterReadingDataBeanProperty.setUpdateDisplayTitle("表示条件");
        meterReadingDataBeanProperty.setUpdateDisplayMessage("表示内容を変更したい場合は、下記を変更し、[表示更新]ボタンを押してください。");

        meterReadingDataBeanProperty.setMeterReadingDataFlg(Boolean.TRUE);
        meterReadingDataBeanProperty.setMeterReadingDataDisabled(Boolean.TRUE);
        meterReadingDataBeanProperty.setBillingAmountDataFlg(Boolean.FALSE);
        meterReadingDataBeanProperty.setBillingAmountDataDisabled(Boolean.FALSE);
        meterReadingDataBeanProperty.setMeterReadingDataStyle("gradation_type_1");
        meterReadingDataBeanProperty.setBillingAmountDataStyle("gradation_type_3");

        if (meterReadingDataBeanProperty.getSearchFlg().equals(Boolean.TRUE)) {
            meterReadingDataBeanProperty
                    .setDevId(new ArrayList<>(meterReadingDataBeanProperty.getDevIdMap().values()).get(0));
        }
        meterReadingDataBeanProperty.setYear(meterReadingDataBeanProperty.getSearchYear());
        meterReadingDataBeanProperty.setMonth(meterReadingDataBeanProperty.getSearchMonth());
        if (Boolean.FALSE.equals(initFlg)) {
            getMonthMeterReadingNo();
        }
        meterReadingDataBeanProperty
                .setMonthMeterReadingNo(meterReadingDataBeanProperty.getSearchMonthMeterReadingNo());
        if (meterReadingDataBeanProperty.getSearchFlg().equals(Boolean.TRUE)) {
            reload();
        }
        meterReadingDataBeanProperty.setSearchFlg(Boolean.FALSE);
    }

    private void viewBillingAmountDataClick() {
        meterReadingDataBeanProperty.setTitle("請求金額データ");
        meterReadingDataBeanProperty.setContent("請求金額を表示します。");

        //表示条件のタイトルとメッセージを検針データと請求金額データで変更
        meterReadingDataBeanProperty.setUpdateDisplayTitle("請求データ選択");
        meterReadingDataBeanProperty.setUpdateDisplayMessage("請求を行う検針データの年月を選択してください。選択後「表示更新」ボタンを押してください。");

        meterReadingDataBeanProperty.setMeterReadingDataFlg(Boolean.FALSE);
        meterReadingDataBeanProperty.setMeterReadingDataDisabled(Boolean.FALSE);
        meterReadingDataBeanProperty.setBillingAmountDataFlg(Boolean.TRUE);
        meterReadingDataBeanProperty.setBillingAmountDataDisabled(Boolean.TRUE);
        meterReadingDataBeanProperty.setMeterReadingDataStyle("gradation_type_3");
        meterReadingDataBeanProperty.setBillingAmountDataStyle("gradation_type_1");

        meterReadingDataBeanProperty.setFromYear(meterReadingDataBeanProperty.getSearchFromYear());
        meterReadingDataBeanProperty.setFromMonth(meterReadingDataBeanProperty.getSearchFromMonth());
        meterReadingDataBeanProperty.setToYear(meterReadingDataBeanProperty.getSearchToYear());
        meterReadingDataBeanProperty.setToMonth(meterReadingDataBeanProperty.getSearchToMonth());
        meterReadingDataBeanProperty.setBtnReloadDisabled(false);

        if (meterReadingDataBeanProperty.getSearchFlg().equals(Boolean.TRUE)) {
            reload();
        }
        meterReadingDataBeanProperty.setSearchFlg(Boolean.FALSE);
    }

    /**
     * 検針結果ボタン押下.
     *
     */
    public void btnMeterReadingDataClick() {
        // アクセスログ出力
        exportAccessLog("btnMeterReadingDataClick", "ボタン「検針データ」押下");

        viewMeterReadingDataClick(Boolean.FALSE);
    }

    /**
     * 請求金額ボタン押下.
     *
     */
    public void btnBillingAmountDataClick() {
        // アクセスログ出力
        exportAccessLog("btnBillingAmountDataClick", "ボタン「請求金額」押下");

        viewBillingAmountDataClick();
    }

    /**
     * 詳細ボタン押下.
     *
     */
    public void btnDetailsClick(BillingAmountDataByTenant billingAmountDataByTenant) {
        // アクセスログ出力
        exportAccessLog("btnDetailsClick", "ボタン「詳細」押下");

        billingAmountDataDetailsBeanProperty.clearData();
        billingAmountDataDetailsBeanProperty.setData(billingAmountDataByTenant);

        // 固定費が""(空)のときは、"0"を表示
        billingAmountDataDetailsBeanProperty.getFixedCostList().stream()
                .forEach(fixedCost -> fixedCost.setFixedCost(nvlNum(fixedCost.getFixedCost())));
    }

    /**
     * 発行日入力.
     *
     */
    public void changeInputDateIssue() {
        // アクセスログ出力
        exportAccessLog("changeInputDateIssue", "「発行日」入力");

        billingAmountDataBeanProperty.setCalDateIssue(DateUtility
                .conversionDate(billingAmountDataBeanProperty.getDateIssue(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
    }

    /**
     * 発行日カレンダーコントロール入力.
     *
     */
    public void changeCalDateIssue() {
        // アクセスログ出力
        exportAccessLog("changeCalDateIssue", "「発行日」カレンダーにて日付選択");

        billingAmountDataBeanProperty.setDateIssue(DateUtility.changeDateFormat(
                billingAmountDataBeanProperty.getCalDateIssue(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
    }

    /**
     * 納期限入力.
     *
     */
    public void changeInputDeadLine() {
        // アクセスログ出力
        exportAccessLog("changeInputDeadLine", "「納期限」入力");

        billingAmountDataBeanProperty.setCalDeadLine(DateUtility
                .conversionDate(billingAmountDataBeanProperty.getDeadLine(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
    }

    /**
     * 納期限カレンダーコントロール入力.
     *
     */
    public void changeCalDeadLine() {
        // アクセスログ出力
        exportAccessLog("changeCalDeadLine", "「納期限」カレンダーにて日付選択");

        billingAmountDataBeanProperty.setDeadLine(DateUtility.changeDateFormat(
                billingAmountDataBeanProperty.getCalDeadLine(), DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
    }

    private boolean searchValidation() {
        boolean result = true;

        if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) { // true:検針データ
            //必須チェック
            if (CheckUtility.isNullOrEmpty(meterReadingDataBeanProperty.getMonthMeterReadingNo())) {
                result = false;
            }
        } else { // false:請求金額データ
            //日付が逆転している場合はエラー
            String fromYear = meterReadingDataBeanProperty.getFromYear();
            String toYear = meterReadingDataBeanProperty.getToYear();
            String fromMonth = meterReadingDataBeanProperty.getFromMonth();
            String toMonth = meterReadingDataBeanProperty.getToMonth();

            int cnvIntFromDate = Integer.parseInt(fromYear + String.format("%02d", Integer.parseInt(fromMonth)));
            int cnvIntToDate = Integer.parseInt(toYear + String.format("%02d", Integer.parseInt(toMonth)));

            if (cnvIntToDate < cnvIntFromDate) {
                //検索結果を初期化
                List<ListSmsSelectBillingResultDate> list = new ArrayList<>();
                selectBillingDataPagingList.init(list);

                result = false;
            }

        }

        return result;
    }

    private boolean validation() {

        boolean result = true;

        //必須チェック
        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getPrintBuildingNoFrom())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userFrom3"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getPrintBuildingNoTo())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userTo3"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getBillingDate())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.billingDate"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getDateIssue())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.dateIssue"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getDeadLine())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.deadLine"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getClaimantName())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.claimantName"));
            result = false;
        }

        if (CheckUtility.isNullOrEmpty(billingAmountDataBeanProperty.getRegNo())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.regNo"));
            result = false;
        }

        if ((billingAmountDataBeanProperty.getInvoice().equals(Boolean.FALSE))
                && (billingAmountDataBeanProperty.getReceipt().equals(Boolean.FALSE))
                && (billingAmountDataBeanProperty.getInvoiceAndReceiptCopy().equals(Boolean.FALSE))) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.print"));
            result = false;
        }

        if (false == result) {
            return result;
        }

        //形式チェック
        Pattern p = Pattern.compile("^[0-9]+$");

        if (false == p.matcher(billingAmountDataBeanProperty.getPrintBuildingNoFrom()).matches()) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userFrom2"));
            result = false;
        } else {

            if (1 > Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoFrom())
                    || (999999 < Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoFrom()))) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userFrom1"));
                result = false;
            }
        }

        if (false == p.matcher(billingAmountDataBeanProperty.getPrintBuildingNoTo()).matches()) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userTo2"));
            result = false;
        } else {

            if (1 > Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoTo())
                    || (999999 < Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoTo()))) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userTo1"));
                result = false;
            }
        }

        p = Pattern.compile("^\\d{4}/\\d{2}$");

        if (false == p.matcher(billingAmountDataBeanProperty.getBillingDate()).matches()) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.billingDateFormat"));
            result = false;
        } else {
            if (!CheckUtility.checkRegDateYm(billingAmountDataBeanProperty.getBillingDate())) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.billingDateExistence"));
                result = false;
            } else {
                if (1900 > Integer.parseInt(billingAmountDataBeanProperty.getBillingDate().split("/")[0])) {
                    addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.billingDateExistence"));
                    result = false;
                }
            }
        }

        p = Pattern.compile("^\\d{4}/\\d{2}/\\d{2}$");

        if (false == p.matcher(billingAmountDataBeanProperty.getDateIssue()).matches()) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.dateIssueFormat"));
            result = false;
        } else {
            if (!CheckUtility.checkRegDateYmd(billingAmountDataBeanProperty.getDateIssue())) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.dateIssueExistence"));
                result = false;
            } else {
                if (1900 > Integer.parseInt(billingAmountDataBeanProperty.getDateIssue().split("/")[0])) {
                    addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.dateIssueExistence"));
                    result = false;
                }
            }
        }

        if (false == p.matcher(billingAmountDataBeanProperty.getDeadLine()).matches()) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.deadLineFormat"));
            result = false;
        } else {

            if (!CheckUtility.checkRegDateYmd(billingAmountDataBeanProperty.getDeadLine())) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.deadLineExistence"));
                result = false;
            } else {
                if (1900 > Integer.parseInt(billingAmountDataBeanProperty.getDeadLine().split("/")[0])) {
                    addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.deadLineExistence"));
                    result = false;
                }
            }
        }

        if (false == result) {
            return result;
        }

        //相関チェック
        if (Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoTo()) < Integer
                .parseInt(billingAmountDataBeanProperty.getPrintBuildingNoFrom())) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.user"));
            return false;
        }

        boolean status = false;
        boolean condition = false;
        pdfList = new ArrayList<>();

        for (BillingAmountDataByTenant tenant : billingAmountDataPagingList.getAllList()) {
            if ((tenant.getTenantId() >= Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoFrom()))
                    && (tenant.getTenantId() <= Integer.parseInt(billingAmountDataBeanProperty.getPrintBuildingNoTo()))) {
                condition = true;
                if (tenant.getAggregateStatus().equals("完了")) {
                    status = true;
                    pdfList.add(tenant);
                }
            }
        }

        if (!condition) {
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.userNot"));
            result = false;
        } else {

            if (!status) {
                addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.status"));
                result = false;
            }
        }
        return result;
    }

    public boolean hasError() {
        List<FacesMessage> messageList = getMessageList();
        if (messageList == null || messageList.isEmpty()) {
            return false;
        }
        for (FacesMessage message : messageList) {
            if (message.getSeverity().equals(FacesMessage.SEVERITY_FATAL)
                    || message.getSeverity().equals(FacesMessage.SEVERITY_ERROR)) {
                return true;
            }
        }
        return false;
    }

    public String pageJump() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String param = params.get("page");
        if (param != null) {
            if (meterReadingDataBeanProperty.getMeterReadingDataFlg().equals(Boolean.TRUE)) {
                meterReadingDataPagingList.setCurrentPage(Integer.valueOf(param));
            } else {
                billingAmountDataPagingList.setCurrentPage(Integer.valueOf(param));
            }
        }
        return STR_EMPTY;
    }

    public int getPageDataCount() {
        return pageDataCount;
    }

    public void setPageDataCount(int pageDataCount) {
        if (pageDataCount == 0) {
            pageDataCount = changeInteger(
                    getWrapped().getInitParameter(SmsConstants.PARAM_NAME_PAGE_DATA_COUNT), false);
        }

        this.pageDataCount = pageDataCount;
    }

    public ListInfo getBuildingInfo() {
        return buildingInfo;
    }

    public void setBuildingInfo(ListInfo buildingInfo) {
        this.buildingInfo = buildingInfo;
    }

    public MeterReadingDataBeanProperty getMeterReadingDataBeanProperty() {
        return meterReadingDataBeanProperty;
    }

    public void setMeterReadingDataBeanProperty(MeterReadingDataBeanProperty meterReadingDataBeanProperty) {
        this.meterReadingDataBeanProperty = meterReadingDataBeanProperty;
    }

    public BillingAmountDataBeanProperty getBillingAmountDataBeanProperty() {
        return billingAmountDataBeanProperty;
    }

    public void setBillingAmountDataBeanProperty(BillingAmountDataBeanProperty billingAmountDataBeanProperty) {
        this.billingAmountDataBeanProperty = billingAmountDataBeanProperty;
    }

    public BillingAmountDataDetailsBeanProperty getBillingAmountDataDetailsBeanProperty() {
        return billingAmountDataDetailsBeanProperty;
    }

    public void setBillingAmountDataDetailsBeanProperty(
            BillingAmountDataDetailsBeanProperty billingAmountDataDetailsBeanProperty) {
        this.billingAmountDataDetailsBeanProperty = billingAmountDataDetailsBeanProperty;
    }

    public MeterReadingDataPagingList getMeterReadingDataPagingList() {
        return meterReadingDataPagingList;
    }

    public void setMeterReadingDataPagingList(MeterReadingDataPagingList meterReadingDataPagingList) {
        this.meterReadingDataPagingList = meterReadingDataPagingList;
    }

    public BillingAmountDataPagingList getBillingAmountDataPagingList() {
        return billingAmountDataPagingList;
    }

    public void setBillingAmountDataPagingList(BillingAmountDataPagingList billingAmountDataPagingList) {
        this.billingAmountDataPagingList = billingAmountDataPagingList;
    }

    public SelectBillingDataPagingList getSelectBillingDataPagingList() {
        return selectBillingDataPagingList;
    }

    public void setSelectBillingDataPagingList(SelectBillingDataPagingList selectBillingDataPagingList) {
        this.selectBillingDataPagingList = selectBillingDataPagingList;
    }

    /**
     * null or empty value
     * 値が空の場合は NO VALUE 値をセットする
     * @param val 値
     * @return 変換後の値
     */
    private String nvlStr(String val) {
        if (val == null || val.isEmpty()) {
            return NO_VALUE_STR;
        }
        return val;
    }

    /**
     * null or empty value
     * 値が空の場合は NO VALUE 値をセットする
     * @param val 値
     * @return 変換後の値
     */
    private String nvlNum(String val) {
        if (val == null || val.isEmpty()) {
            return NO_VALUE_NUM;
        }
        return val;
    }

    /**
     * 文字列から整数に変換.
     *
     * @param target 対象文字列
     * @param isNegative 負数フラグ
     * @return 整数
     */
    private int changeInteger(String target, boolean isNegative) {
        int result = 0;
        try {
            if (isNegative) {
                result = Integer.parseInt(target);
            } else {
                result = Integer.parseUnsignedInt(target);
            }

            return result;

        } catch (Exception e) {
            return result;
        }
    }

    /**
     * OCR権限を持っていればtrueを返却
     * @return
     */
    private boolean isOcrAuth() {
        // 担当者のOCR権限を取得（SMS権限情報が見つからない場合はnullを取得）
        MCorpPersonAuth ocrAuthority = this.getLoginCorpPersonAuthList().stream()
                .filter(o -> Objects.equals(o.getMAuth().getAuthorityCd(), OsolConstants.USER_AUTHORITY.OCR.getVal()))
                .findFirst()
                .orElse(null);

        // OCR権限可否を取得（nullの場合は0:権限なしとする）
        Integer ocrAuthorityFlg = Objects.nonNull(ocrAuthority) ? ocrAuthority.getAuthorityFlg()
                : OsolConstants.FLG_OFF;

        //SMS権限とOCR権限がある場合は非表示にする
        if(ocrAuthorityFlg.equals(OsolConstants.FLG_ON)) {
            return true;
        }

        return false;
    }

    /**
     * 数値の丸め計算を行う
     *
     * @param fractionalProcessing
     * @param value
     * @param tagetDigits
     * @return
     */
    private BigDecimal fractionalProcessing(String fractionalProcessing, BigDecimal value, int tagetDigits) {

        BigDecimal result = BigDecimal.valueOf(0);

        switch (fractionalProcessing) {
        case "1":
            //四捨五入
            result = value.setScale(tagetDigits, RoundingMode.HALF_UP);
            break;
        case "2":
            //切り捨て
            result = value.setScale(tagetDigits, RoundingMode.DOWN);
            break;
        case "3":
            //切り上げ
            result = value.setScale(tagetDigits, RoundingMode.UP);
            break;
        default:
            //不正値(そのまま返す)
            result = value;
            break;
        }

        return result;
    }

    /**
     * 請求額算出ボタン押下
     *
     */
    public String btnBillingAmountCalculationClick() {
        // アクセスログ出力
        exportAccessLog("btnBillingAmountCalculationClick", "ボタン「請求額算出」押下");

        List<BillingAmountDataByTenant> billingAmountList = new ArrayList<BillingAmountDataByTenant>();
        Map<String, List<SmsTenantInfoResultDate>> tenantInfoListMap = new HashMap<>();

        //消費税率のチェック
        if (!isSalesTax()) {
            //消費税率が未設定の場合、エラーメッセージを表示
            addErrorMessage(beanMessages.getMessage("meterReadingDataBean.error.salesTaxRateExistence"));
            return "";
        }

        for (ListSmsSelectBillingResultDate listSmsSelectBillingResultDate : selectBillingDataPagingList.getAllList()) {

            //選択されたメータ使用料金のみ取得
            if (listSmsSelectBillingResultDate.isSelectBillingFlg()) {
                List<SmsTenantInfoResultDate> list = listSmsSelectBillingResultDate.getTenantInfoList();
                for (SmsTenantInfoResultDate data : list) {
                    ListSmsBillingAmountDataInfoResultData listSmsBillingAmountDataInfoResultData = data
                            .getListSmsBillingAmountDataInfoResultData();
                    String tenantId = listSmsBillingAmountDataInfoResultData.getTenantId().toString();

                    if (tenantInfoListMap.containsKey(tenantId)) {
                        List<SmsTenantInfoResultDate> tmpBillingAmountList = tenantInfoListMap.get(tenantId);
                        tmpBillingAmountList.add(data);
                        tenantInfoListMap.put(tenantId, tmpBillingAmountList);
                    } else {
                        List<SmsTenantInfoResultDate> tmpBillingAmountList = new ArrayList<SmsTenantInfoResultDate>();
                        tmpBillingAmountList.add(data);
                        tenantInfoListMap.put(tenantId, tmpBillingAmountList);
                    }
                }
            }
        }

        //ユーザコード(テナントコード)単位で請求額を合算
        Boolean isProratedChargeFlg = meterReadingDataBeanProperty.isProratedChargeFlg(); //按分料金チェックボックス
        Boolean isFixedCostFlg = meterReadingDataBeanProperty.isFixedCostFlg(); //固定費

        for (Entry<String, List<SmsTenantInfoResultDate>> tenantEntry : tenantInfoListMap.entrySet()) {

            try {

                billingAmountList.add( //
                		listSmsBillingAmountDataSearchDao.getBillingAmount( //
                				tenantEntry.getValue(), //
                				isProratedChargeFlg, //
                				isFixedCostFlg));

                if (!billingAmountList.isEmpty()) {
                	billingAmountDataBeanProperty.setSalesTaxRate(billingAmountList.get(0).getSalesTaxRate());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //テナントコード順に並び変える
        billingAmountList.sort((a, b) -> Long.compare(a.getTenantId(), b.getTenantId()));

        billingAmountDataPagingList.init(billingAmountList, getPageDataCount());

        GetSmsClaimantInfoParameter parameter2 = new GetSmsClaimantInfoParameter();
        parameter2.setBean("getSmsClaimantInfoBean");
        parameter2.setCorpId(this.buildingInfo.getCorpId());
        parameter2.setBuildingId(Long.parseLong(this.buildingInfo.getBuildingId()));
        parameter2.setPersonCorpId(this.getLoginCorpId());
        parameter2.setPersonId(this.getLoginPersonId());

        GetSmsClaimantInfoResponse response2 = callApiPost(parameter2, GetSmsClaimantInfoResponse.class);

        if (Objects.nonNull(response2) && OsolApiResultCode.API_OK.equals(response2.getResultCode())
                && Objects.nonNull(response2.getResult())) {
            claimantData = response2.getResult().getClaimantInfoResultData();
            if (Objects.nonNull(claimantData) && Objects.nonNull(claimantData.getCorpId())) {
                billingAmountDataBeanProperty.setClaimantName(claimantData.getClaimantName());
                billingAmountDataBeanProperty.setRegNo(claimantData.getRegNo());
            }
        } else {
            addErrorMessage(
                    beanMessages.getMessage(OsolConstants.PREFIX_API_MESSSAGE.concat(response2.getResultCode())));
        }

        return STR_EMPTY;
    }

    /**
     * 消費税率の値を持っていればtrueを返却
     * @return
     */
    public boolean isSalesTax() {
        String corpId = buildingInfo.getCorpId();
        Long buildingId = Long.valueOf(buildingInfo.getBuildingId());

        //各種設定情報を取得
        MVarious variousInfo = mVariousDao.getVariousInfo(corpId, buildingId);

        if (variousInfo == null) {
            //レコードが存在しない
            return false;
        } else {
            //消費税率の値をチェック
            if (variousInfo.getSaleTaxRate() == null) {
                return false;
            }
        }

        return true;
    }

    /**
     * zipファイルを作成する
     *
     * @param zipCompressFolder 圧縮するフォルダ
     * @return 圧縮後の出力ファイルパス
     */
    private String createZipFile(String zipCompressFolder) {

        // 圧縮後の出力パス + ファイル名
        String zipFilePath = zipCompressFolder + ZIP_EXTENSION;

        boolean ret = SmsFileZipArchive.compressDirectory(zipCompressFolder, zipFilePath);

        for (int i=0; i < savePdfFileList.size(); i++) {
            String filePath = Paths.get(savePdfPaths[0], savePdfFileList.get(i)).toString();
            // 圧縮元のファイルを削除
            AnalysisEmsUtility.deleteFileDirectory(new File(filePath));
        }

        eventLogger.debug(this.getClass().toString() + ": ■ ZipArchive ret:" + ret);

        return zipFilePath;
    }

}
