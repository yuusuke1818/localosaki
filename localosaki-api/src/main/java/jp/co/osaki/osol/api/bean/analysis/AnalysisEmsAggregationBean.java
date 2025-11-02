package jp.co.osaki.osol.api.bean.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.SystemUtils;
import org.jboss.logging.Logger;

import com.google.gson.JsonSyntaxException;

import jp.co.osaki.osol.OsolConfigs;
import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.OsolFolderAndFileName;
import jp.co.osaki.osol.api.OsoApiFileZipArchive;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.AGGREGATE_TYPE;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.EVENT_CTRL_HISTORY_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_BUILDING_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_GROUP_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_GROUP_SUMMARY_ALL_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LINE_GROUP_SUMMARY_BILL_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.LOAD_CTRL_HISTORY_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.MAX_MIN;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.OUTPUT_LINE_VAL_RECOEDS_NO_EXTENDS;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.SEARCH_TYPE;
import jp.co.osaki.osol.api.constants.ApiAnalysisEmsConstants.TIME_STANDARD_DEFINE_NAME;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.dao.analysis.AnalysisAllBuildingListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisBuildingIdListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsAggregationDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsAllLineSummaryDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingIdLineSummaryDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingLineListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointInfoCsvListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointLineInfoCsvListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingSummaryDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsEventControlHistDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsGroupLineSummaryDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsLoadControlHistDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsRecordCsvDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsSmListDao;
import jp.co.osaki.osol.api.dao.analysis.AnalysisGroupBuildingListDao;
import jp.co.osaki.osol.api.dao.energy.setting.BuildingLineTimeStandardListDao;
import jp.co.osaki.osol.api.dao.energy.setting.CorpDemandSelectDao;
import jp.co.osaki.osol.api.dao.energy.setting.LineGroupSearchDao;
import jp.co.osaki.osol.api.dao.energy.setting.LineListDao;
import jp.co.osaki.osol.api.dao.grouping.ChildGroupListDao;
import jp.co.osaki.osol.api.dao.grouping.ParentGroupListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisAllBuildingListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisBuildingIdListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsAggregationParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsAllLineSummaryParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingIdLineSummaryParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingLineListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointInfoCsvListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointLineInfoCsvListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingSummaryParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsEventControlHistParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsGroupLineSummaryParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsLoadControlHistParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsRecordCsvParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsSmListParameter;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisGroupBuildingListParameter;
import jp.co.osaki.osol.api.parameter.energy.setting.BuildingLineTimeStandardListParameter;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpDemandSelectParameter;
import jp.co.osaki.osol.api.parameter.energy.setting.LineGroupSearchParameter;
import jp.co.osaki.osol.api.parameter.energy.setting.LineListParameter;
import jp.co.osaki.osol.api.parameter.grouping.ChildGroupListParameter;
import jp.co.osaki.osol.api.parameter.grouping.ParentGroupListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsAggregationResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisAllBuildingListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisBuildingIdListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsAggregationResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsAllLineSummaryResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingIdLineSummaryResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingLineListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointInfoCsvListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointLineInfoCsvListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingSummaryResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsEventControlHistResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsGroupLineRecordCsvResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsGroupLineSummaryResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsLoadControlHistResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsSmListResult;
import jp.co.osaki.osol.api.result.analysis.AnalysisGroupBuildingListResult;
import jp.co.osaki.osol.api.result.energy.setting.BuildingLineTimeStandardListResult;
import jp.co.osaki.osol.api.result.energy.setting.CorpDemandSelectResult;
import jp.co.osaki.osol.api.result.energy.setting.LineGroupSearchResult;
import jp.co.osaki.osol.api.result.energy.setting.LineListResult;
import jp.co.osaki.osol.api.result.grouping.ChildGroupListResult;
import jp.co.osaki.osol.api.result.grouping.ParentGroupListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonDemandDayReportPointListResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisAllBuildingListResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsAllLineSummaryResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsCategoryInfoJson;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsControlHistControlLoadResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsControlHistPeriodResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsGroupLineSummaryResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsReservationInfoResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsSmControlHistResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsSmListResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.BuildingLineTimeStandardListTimeResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmPointListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.generic.GenericTypeListDetailResultData;
import jp.co.osaki.osol.api.resultdata.grouping.ChildGroupListDetailResultData;
import jp.co.osaki.osol.api.resultdata.grouping.ParentGroupListDetailResultData;
import jp.co.osaki.osol.api.utility.analysis.AnalysisEmsFileUploadUtility;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.entity.MCorp;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.exceloutput.analysis.AnalysisEmsRecordExcelOutput;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;
import jp.co.osaki.osol.utility.AnalysisEmsUtility.ContentsType;
import jp.co.osaki.osol.utility.AnalysisEmsUtility.OutputFormat;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.osol.utility.ExcelUtility;
import jp.skygroup.enl.webap.base.BaseConstants;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "AnalysisEmsAggregationBean")
@RequestScoped
public class AnalysisEmsAggregationBean extends OsolApiBean<AnalysisEmsAggregationParameter>
    implements BaseApiBean<AnalysisEmsAggregationParameter, AnalysisEmsAggregationResponse> {

    private AnalysisEmsAggregationParameter parameter = new AnalysisEmsAggregationParameter();
    private AnalysisEmsAggregationResponse response = new AnalysisEmsAggregationResponse();

    /**
     * Eventログ.
     */
    private final Logger eventLogger = Logger.getLogger(BaseConstants.LOGGER_NAME.EVENT.getVal());

    private String selectedCorpId;
    private String selectedPersonCorpId;
    private String selectedPersonId;
    private MPerson person;
    private MCorp selectedCorp;
    private AnalysisEmsReservationInfoResultData resultData;
    private boolean processStopFlg;
    private String tempFolderPath;

    // 処理結果0件かフラグ
    boolean noResultFlg;
    private String saveFilename;
    private String downloadFilePath;
    private Map<String, List<Object>> optionSettingMap;
    private Map<String, List<Object>> outputDataMap;
    private Map<String, List<Object>> preSettingMap;
    private List<String> emsNameList;
    private Map<String, String> orgCloneSheetNameMap;
    private Map<String, Map<String, String>> templateMap;

    /**
     * フォルダ＆ファイル名クラス
     */
    @Inject
    private OsolFolderAndFileName folderAndFileName;

    @Inject
    private OsolConfigs osolConfigs;

    @Inject
    private GenericTypeUtility genericTypeUtility;

    @Inject
    private AnalysisEmsFileUploadUtility analysisEmsFileUploadUtility;

    @EJB
    private AnalysisEmsAggregationDao dao;

    @EJB
    private AnalysisAllBuildingListDao analysisAllBuildingListDao;

    @EJB
    private AnalysisBuildingIdListDao analysisBuildingIdListDao;

    @EJB
    private AnalysisGroupBuildingListDao analysisGroupBuildingListDao;

    @EJB
    private AnalysisEmsGroupLineSummaryDao analysisEmsGroupLineSummaryDao;

    @EJB
    private LineGroupSearchDao lineGroupSearchDao;

    @EJB
    private LineListDao lineListDao;

    @EJB
    private CorpDemandSelectDao corpDemandSelectDao;

    @EJB
    private ParentGroupListDao parentGroupListDao;

    @EJB
    private ChildGroupListDao childGroupListDao;

    @EJB
    private AnalysisEmsBuildingLineListDao analysisEmsBuildingLineListDao;

    @EJB
    private AnalysisEmsAllLineSummaryDao analysisEmsAllLineSummaryDao;

    @EJB
    private AnalysisEmsBuildingIdLineSummaryDao analysisEmsBuildingIdLineSummaryDao;

    @EJB
    private AnalysisEmsBuildingSummaryDao analysisEmsBuildingSummaryDao;

    @EJB
    private AnalysisEmsSmListDao analysisEmsSmListDao;

    @EJB
    private AnalysisEmsBuildingPointListDao analysisEmsBuildingPointListDao;

    @EJB
    private BuildingLineTimeStandardListDao buildingLineTimeStandardListDao;

    @EJB
    private AnalysisEmsLoadControlHistDao analysisEmsLoadControlHistDao;

    @EJB
    private AnalysisEmsEventControlHistDao analysisEmsEventControlHistDao;

    @EJB
    private AnalysisEmsRecordCsvDao analysisEmsRecordCsvDao;

    @EJB
    private AnalysisEmsBuildingPointInfoCsvListDao analysisEmsBuildingPointInfoCsvListDao;

    @EJB
    private AnalysisEmsBuildingPointLineInfoCsvListDao analysisEmsBuildingPointCsvListDao;

    @Override
    public AnalysisEmsAggregationParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsAggregationParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsAggregationResponse execute() throws Exception {

        AnalysisEmsAggregationParameter param = new AnalysisEmsAggregationParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setAggregateId(parameter.getAggregateId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsAggregationResult result = executeApi(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    /**
     * 処理実行
     * @param param
     * @return
     */
    private AnalysisEmsAggregationResult executeApi(AnalysisEmsAggregationParameter param) {

        // 初期化
        initMap();
        resultData = new AnalysisEmsReservationInfoResultData();
        processStopFlg = false;
        tempFolderPath = OsolConstants.STR_EMPTY;

        AnalysisEmsAggregationResult result = new AnalysisEmsAggregationResult();

        // 集計分析予約情報を取得
        List<AnalysisEmsReservationInfoResultData> resultList = dao.getAnalysisEmsReservationInfoResultList(parameter.getAggregateId());

        // 取得結果がnullまたは、1件以外だった場合処理終了
        if (resultList == null || (resultList != null && resultList.size() != 1)) {
            eventLogger.error(this.getClass().getName().concat(".aggregateId:Error ret(" + resultList + ")"));
            return null;
        }

        // 取得した結果を格納
        resultData = resultList.get(0);
        selectedCorpId = resultData.getCorpId();
        selectedPersonCorpId = resultData.getPersonCorpId();
        selectedPersonId = resultData.getPersonId();

        // 担当者情報を取得
        person = dao.getMPerson(selectedPersonCorpId, selectedPersonId);

        // 選択企業の企業情報を取得
        selectedCorp = dao.getMCorp(selectedCorpId);

        // 集計条件(Jsonデータ)を取得
        AnalysisEmsCategoryInfoJson jsonObj;
        try {
            jsonObj = AnalysisEmsUtility.changeToObject(resultData.getAggregateCondition());

            // 集計条件(Jsonデータ)がnullの場合も処理できないので終了
            if (jsonObj == null) {
                resultData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING.getVal());
                resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.ERROR.getVal());
                resultData.setAggregateEndDate(getCurrentDateTime());
                AnalysisEmsReservationInfoResultData resultInfo = updateTAggregateReservationInfo(resultData);
                result.setAggregateReservationInfo(resultInfo);
                eventLogger.error(this.getClass().getName().concat(".JsonObject:Null Error"));
                return result;
            }
        } catch (JsonSyntaxException e) {
            resultData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING.getVal());
            resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.ERROR.getVal());
            resultData.setAggregateEndDate(getCurrentDateTime());
            AnalysisEmsReservationInfoResultData resultInfo = updateTAggregateReservationInfo(resultData);
            result.setAggregateReservationInfo(resultInfo);
            eventLogger.error(this.getClass().getName().concat(".JsonObject:JsonSyntaxException"));
            return result;
        }

        // 処理結果0件フラグの初期設定
        initNoResultFlg();
        boolean errFlg = false;
        String contentsType = jsonObj.getContentsType();
        String outputFormat = jsonObj.getOutputFormat();
        String outputTargetType = jsonObj.getOutputTargetType();

        // EMS実績機能種別と出力条件によって呼び出し先判断
        if (contentsType != null && outputFormat != null && outputTargetType != null
                && AnalysisEmsUtility.ContentsType.getText(contentsType) != null
                && AnalysisEmsUtility.OutputFormat.getText(outputFormat) != null
                && AnalysisEmsUtility.OutputTargetType.getText(outputTargetType) != null) {

            if (ContentsType.LINE.getCd().equals(contentsType)) {
                // 系統別計測値
                if (OutputFormat.EXCEL.getCd().equals(outputFormat)) {
                    // Excel
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        String parentGroupName =  getParentGroupName(jsonObj.getParentGroupId());
                        LinkedHashMap<String, String> childGroupMap =  getChildGroup(Long.valueOf(jsonObj.getParentGroupId()));
                        String childGroupId = jsonObj.getChildGroupId();
                        if (OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(childGroupId)) {
                            childGroupId = null;
                        }
                        errFlg = aggregationLineGroup(
                                jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(), parentGroupName,
                                childGroupMap, jsonObj.getParentGroupId(), childGroupId,
                                AnalysisEmsUtility.changeFlg(jsonObj.getSummaryCheckFlg()), AnalysisEmsUtility.changeFlg(jsonObj.getBuildingCheckFlg()));

                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = aggregationLineBuildingAll(
                                jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(), AnalysisEmsUtility.changeFlg(jsonObj.getSummaryCheckFlg()),
                                AnalysisEmsUtility.changeFlg(jsonObj.getBuildingCheckFlg()));

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationLineBuildingNo(
                                jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(), jsonObj.getBuildingNo(), jsonObj.getBuildingName(),
                                jsonObj.getBuildingId(), AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()),
                                AnalysisEmsUtility.changeFlg(jsonObj.getSummaryCheckFlg()), AnalysisEmsUtility.changeFlg(jsonObj.getBuildingCheckFlg()),
                                Long.valueOf(jsonObj.getLineGroupId()));
                    }

                } else if (OutputFormat.CSV.getCd().equals(outputFormat)) {
                    // CSV
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {

                        String targetDateTo = jsonObj.getTargetDateTo();
                        // 念のため使用量が選択されている場合は、終了期間をnullを設定する
                        if (AnalysisEmsUtility.OutputItemValue.USE.getCd().equals(jsonObj.getCsvOutputItemValue())) {
                            targetDateTo = null;
                        }

                        // グループ設定
                        String parentGroupName =  getParentGroupName(jsonObj.getParentGroupId());
                        String childGroupName =  getChildGroupName(jsonObj.getParentGroupId(), jsonObj.getChildGroupId());
                        String childGroupId = jsonObj.getChildGroupId();
                        if (OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE.equals(childGroupId)) {
                            childGroupId = null;
                        }
                        errFlg = aggregationLineGroupCsv(jsonObj.getTargetDateFrom(), targetDateTo,
                                parentGroupName, childGroupName, jsonObj.getParentGroupId(), childGroupId,
                                getLineName(getCorpLineGroupId(), jsonObj.getCsvLineValue()), jsonObj.getCsvLineValue(), getCsvOutputItemName(jsonObj.getCsvOutputItemValue()),
                                AnalysisEmsUtility.changeFlg(jsonObj.getCsv12hCheckFlg()), jsonObj.getCsvOutputItemValue());

                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {

                        String targetDateTo = jsonObj.getTargetDateTo();
                        // 念のため使用量が選択されている場合は、終了期間にnullを設定する
                        if (AnalysisEmsUtility.OutputItemValue.USE.getCd().equals(jsonObj.getCsvOutputItemValue())) {
                            targetDateTo = null;
                        }

                        // 全建物・テナントを指定する
                        errFlg = aggregationLineBuildingAllCsv(jsonObj.getTargetDateFrom(), targetDateTo,
                                jsonObj.getParentGroupId(), jsonObj.getChildGroupId(), getLineName(getCorpLineGroupId(), jsonObj.getCsvLineValue()),
                                jsonObj.getCsvLineValue(), getCsvOutputItemName(jsonObj.getCsvOutputItemValue()),
                                AnalysisEmsUtility.changeFlg(jsonObj.getCsv12hCheckFlg()), jsonObj.getCsvOutputItemValue());

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = true;
                    }
                }
            } else if (ContentsType.POINT.getCd().equals(contentsType)) {
                // ポイント別計測値
                if (OutputFormat.EXCEL.getCd().equals(outputFormat)) {
                    // Excel
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = aggregationPointAll(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo());

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationPointBuildingNo(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(), jsonObj.getBuildingNo(),
                                jsonObj.getBuildingName(), jsonObj.getBuildingId(), AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()));
                    }

                } else if (OutputFormat.CSV.getCd().equals(outputFormat)) {
                    // CSV
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationPointBuildingNoCsv(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(), jsonObj.getBuildingNo(),
                                jsonObj.getBuildingName(), jsonObj.getBuildingId(), AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()),
                                Long.valueOf(jsonObj.getLineGroupId()));

                    }

                }
            } else if (ContentsType.LINE_JIGEN.getCd().equals(contentsType)) {
                // 系統別時限標準値
                if (OutputFormat.EXCEL.getCd().equals(outputFormat)) {
                    // Excel
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = aggregationTimeStandardAll(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo());

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationTimeStandardBuildingNo(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(),
                                jsonObj.getBuildingNo(), jsonObj.getBuildingName(), jsonObj.getBuildingId(),
                                AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()), Long.valueOf(jsonObj.getLineGroupId()));
                    }

                } else if (OutputFormat.CSV.getCd().equals(outputFormat)) {
                    // CSVは選択できないはず
                    errFlg = true;
                }
            } else if (ContentsType.LOAD.getCd().equals(contentsType)) {
                // 負荷制御履歴
                if (OutputFormat.EXCEL.getCd().equals(outputFormat)) {
                    // Excel
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = aggregationLoadCtrlBuildingAll(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo());

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationLoadCtrlBuildingNo(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(),
                                jsonObj.getBuildingNo(), jsonObj.getBuildingName(), jsonObj.getBuildingId(),
                                AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()));
                    }

                } else if (OutputFormat.CSV.getCd().equals(outputFormat)) {
                    // CSV（2020/12/01時点では、利用不可）
                }
            } else if (ContentsType.EVENT.getCd().equals(contentsType)) {
                // イベント制御履歴
                if (OutputFormat.EXCEL.getCd().equals(outputFormat)) {
                    // Excel
                    if (AnalysisEmsUtility.OutputTargetType.GROUP.getCd().equals(outputTargetType)) {
                        // グループ設定
                        errFlg = true;
                    } else if (AnalysisEmsUtility.OutputTargetType.ALL.getCd().equals(outputTargetType)) {
                        // 全建物・テナントを指定する
                        errFlg = aggregationEventCtrlBuildingAll(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo());

                    } else if (AnalysisEmsUtility.OutputTargetType.NO.getCd().equals(outputTargetType)) {
                        // 建物・テナントを指定する
                        errFlg = aggregationEventCtrlBuildingNo(jsonObj.getTargetDateFrom(), jsonObj.getTargetDateTo(),
                                jsonObj.getBuildingNo(), jsonObj.getBuildingName(), jsonObj.getBuildingId(),
                                AnalysisEmsUtility.changeFlg(jsonObj.getSearchTenantCheckFlg()));
                    }

                } else if (OutputFormat.CSV.getCd().equals(outputFormat)) {
                    // CSV（2020/12/01時点では、利用不可）
                }
            }
        } else {
            eventLogger.error(this.getClass().getName().concat(".aggregateCondition:No Matching"));
            errFlg = true;
        }

        // 最後の集計処理中に中止されたことも考慮してチェックしておく
        // 中止チェックは、建物ごとの最初にチェックするため
        if (chkProcessStop()) {
            // 中断されていたら最新値を取得する
            resultData = dao.find(resultData);
        }

        // 処理ステータスが中止処理中の場合
        if (processStopFlg) {
            // 集計処理ステータス、集計処理結果、集計終了時間を設定
            resultData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING_CANCEL.getVal());
            resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.OK.getVal());
            resultData.setAggregateEndDate(getCurrentDateTime());
        } else if (errFlg) {
            // 不正値はエラー
            // 出力対象が0件の場合、処理結果NG
            if (noResultFlg) {
                resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.NONE.getVal());
            } else {
                // その他エラーは、処理結果エラー
                resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.ERROR.getVal());
            }
            resultData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING.getVal());
            resultData.setAggregateEndDate(getCurrentDateTime());

        } else {
            // 正常終了、処理終了ステータスを設定
            resultData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING.getVal());
            resultData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.OK.getVal());
            resultData.setAggregateEndDate(getCurrentDateTime());
            resultData.setOutputFilePath(downloadFilePath);
            resultData.setOutputFileName(saveFilename);
        }

         // 更新
        AnalysisEmsReservationInfoResultData resultInfo = updateTAggregateReservationInfo(resultData);
        result.setAggregateReservationInfo(resultInfo);

        if (!CheckUtility.isNullOrEmpty(tempFolderPath)) {
            // 作業フォルダが残っていた場合削除
            deleteFileDirectory(new File(tempFolderPath));
        }

        return result;

    }

    /**
     * 系統別計測値のグループ指定出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return true:エラー、false:正常
     */
    public boolean aggregationLineGroup(
            String targetDateFrom, String targetDateTo, String parentGroupName, Map<String,String> childNameList,
            String parentId,String childId,Boolean isOutputSummary,Boolean isOutputBuilding) {
        Date today = getCurrentDateTime();
        // 全建物出力
        Long parentIdL = null;
        Long childIdL = null;
        try {
            parentIdL = Long.valueOf(parentId);
            if (childId != null) {
                childIdL = Long.valueOf(childId);
            }
        } catch (Exception e) {
            eventLogger.error(this.getClass().getName().concat(".aggregationLineGroup():NumberFormatException"));
            return true;
        }
        List<AnalysisAllBuildingListResultData> targetBuildingList = getGroupTargetBuildingList(parentIdL, childIdL);

        //企業系統のグループIDを取得
        Long corpLineGroupId = getCorpLineGroupId();

        //企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(corpLineGroupId);

        //全体を先頭に設定
        for (LineListDetailResultData lineData : corpLineDetailList) {
            if (ApiAnalysisEmsConstants.LINE_ALL.equals(lineData.getLineNo())) {
                corpLineDetailList.remove(lineData);
                corpLineDetailList.add(0, lineData);
                break;
            }
        }

        LineListDetailResultData tempLine = new LineListDetailResultData();
        tempLine.setLineNo("");
        tempLine.setLineName("アメダス(外気温)");
        tempLine.setLineUnit("℃");

        corpLineDetailList.add(tempLine);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLineListFolder", today, isOutputBuilding);

        //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        //対象期間のカレンダーリストを作成
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        } catch (Exception e) {
            return true;
        }
        List<Date> dateList = createTermCalendar(measurementDateFrom, measurementDateTo);

        //Excel出力
        boolean nonErrFlg = false;
        if (isOutputBuilding) {
            nonErrFlg = createLineGroupExcel(targetBuildingList,corpLineGroupId,corpLineDetailList,SEARCH_TYPE.NO,dateList,measurementDateFrom,measurementDateTo,targetTerm,corpSumDate,emsRecordPaths,isOutputSummary);
        }

        if ((!isOutputBuilding || (isOutputBuilding && nonErrFlg)) && isOutputSummary) {
            nonErrFlg = createGroupLineSummaryExcel(parentIdL,childIdL,targetBuildingList,corpLineGroupId,corpLineDetailList,parentGroupName,childNameList,measurementDateFrom,measurementDateTo,targetTerm,corpSumDate,emsRecordPaths,isOutputBuilding);
        }

        // ZIPファイルを作成する
        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsLineListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLineGroup():S3 Upload Failed"));
        }
        return !nonErrFlg;
    }

    /**
     * 系統別計測値の全建物出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return true:エラー、false:正常
     */
    private boolean aggregationLineBuildingAll(String targetDateFrom, String targetDateTo, Boolean isOutputSummary, Boolean isOutputBuilding) {
        Date today = getCurrentDateTime();
        // 全建物出力

        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        //企業系統のグループIDを取得
        Long corpLineGroupId = getCorpLineGroupId();

        //企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(corpLineGroupId);

        //全体を先頭に設定
        for (LineListDetailResultData lineData : corpLineDetailList) {
            if (lineData.getLineNo().equals(ApiAnalysisEmsConstants.LINE_ALL)) {
                corpLineDetailList.remove(lineData);
                corpLineDetailList.add(0, lineData);
                break;
            }
        }

        LineListDetailResultData tempLine = new LineListDetailResultData();
        tempLine.setLineNo("");
        tempLine.setLineName("アメダス(外気温)");
        tempLine.setLineUnit("℃");

        corpLineDetailList.add(tempLine);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLineListFolder", today, isOutputBuilding);

        // 企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        // 対象期間のカレンダーリストを作成
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        } catch (Exception e) {
            return true;
        }
        List<Date> dateList = createTermCalendar(measurementDateFrom, measurementDateTo);

        // Excel出力
        boolean nonErrFlg = false;
        if (isOutputBuilding) {
            nonErrFlg = createLineBuildingExcel(targetBuildingList, corpLineGroupId, corpLineDetailList,
                    SEARCH_TYPE.ALL, dateList, measurementDateFrom, measurementDateTo, targetTerm, corpSumDate,
                    emsRecordPaths, isOutputSummary);
        }

        if ((!isOutputBuilding || (isOutputBuilding && nonErrFlg)) && isOutputSummary) {
            nonErrFlg = createAllLineSummaryExcel(targetBuildingList, corpLineGroupId, corpLineDetailList,
                    SEARCH_TYPE.ALL, dateList, measurementDateFrom, measurementDateTo, targetTerm, corpSumDate,
                    emsRecordPaths, isOutputBuilding);
        }

        // ZIPファイルを作成する
        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsLineListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLineBuildingAll():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 系統別計測値の建物指定出力処理（目安時間算出以降）
     *
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @param isOutputSummary
     * @param isOutputBuilding
     * @param lineGroupId
     *
     * @return true:エラー、false:正常
     */
    private boolean aggregationLineBuildingNo(String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter,Boolean isOutputSummary,Boolean isOutputBuilding,Long lineGroupId) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        // 建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // 企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(lineGroupId);

        // 全体を先頭に設定
        for (LineListDetailResultData lineData : corpLineDetailList) {
            if (lineData.getLineNo().equals(ApiAnalysisEmsConstants.LINE_ALL)) {
                corpLineDetailList.remove(lineData);
                corpLineDetailList.add(0, lineData);
                break;
            }
        }

        LineListDetailResultData tempLine = new LineListDetailResultData();
        tempLine.setLineNo("");
        tempLine.setLineName("アメダス(外気温)");
        tempLine.setLineUnit("℃");

        corpLineDetailList.add(tempLine);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLineListFolder", today, isOutputBuilding);

        // 企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        // 対象期間のカレンダーリストを作成
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        } catch (Exception e) {
            return true;
        }
        List<Date> dateList = createTermCalendar(measurementDateFrom, measurementDateTo);

        //建物別情報の出力にチェックが入っている場合のみ出力
        boolean nonErrFlg = false;
        if (isOutputBuilding) {
            //Excel出力(建物)
            nonErrFlg = createLineBuildingExcel(targetBuildingList,lineGroupId,corpLineDetailList,SEARCH_TYPE.NO,dateList,measurementDateFrom,measurementDateTo,targetTerm,corpSumDate,emsRecordPaths,isOutputSummary);
        }

        if ((!isOutputBuilding || (isOutputBuilding && nonErrFlg)) && isOutputSummary) {
            nonErrFlg = createLineSummaryExcel(buildingId,buildingFilter,targetBuildingList,lineGroupId,corpLineDetailList,SEARCH_TYPE.NO,dateList,measurementDateFrom,measurementDateTo,targetTerm,corpSumDate,emsRecordPaths,isOutputBuilding);
        }

        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            // ZIPファイルを作成する
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsLineListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLineBuildingNo():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 系統別計測値のグループ指定出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return true:エラー、false:正常
     */
    public boolean aggregationLineGroupCsv(String targetDateFrom, String targetDateTo, String parentGroupName, String childGroupName, String parentId,
            String childId, String lineName, String lineId, String outputName,
            boolean extendsPlusMinus12hEnabled, String emsOutputFormatCsvItemsCd) {
        Date today = getCurrentDateTime();
         // 全建物出力
        Long parentIdL = null;
        Long childIdL = null;
        try {
            parentIdL = Long.valueOf(parentId);
            if(childId != null) {
                childIdL = Long.valueOf(childId);
            }
        } catch(Exception e) {
            eventLogger.error(this.getClass().getName().concat(".aggregationLineGroupCsv():NumberFormatException"));
            return true;
        }

        // 対象建物取得
        List<AnalysisAllBuildingListResultData> targetBuildingList = getGroupTargetBuildingList(parentIdL, childIdL);

        if (targetBuildingList == null || targetBuildingList.isEmpty()) {
            eventLogger.error(this.getClass().getName().concat(".aggregationLineGroupCsv():targetBuildingList Target No Result"));
            noResultFlgOn();
            return true;
        }

        // 建物番号でソート
        targetBuildingList
                .sort(Comparator.comparing((AnalysisAllBuildingListResultData o) -> o.getBuildingId())
                        .thenComparing((AnalysisAllBuildingListResultData o) -> o.getBuildingId()));

        // 企業系統のグループIDを取得
        Long corpLineGroupId = getCorpLineGroupId();

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLineListFolder", today, false);

        boolean nonErrFlg = false;
        try {
            downloadFilePath = "";
            saveFilename = "";

            // CSV出力
            downloadFilePath = createLineCsv(targetBuildingList, targetDateFrom, targetDateTo,
                    extendsPlusMinus12hEnabled, lineId, outputName, parentGroupName, childGroupName, parentIdL,
                    childIdL, corpLineGroupId, lineName, emsRecordPaths[0], emsOutputFormatCsvItemsCd);

            // エラーでなければ
            if (!CheckUtility.isNullOrEmpty(downloadFilePath)) {
                nonErrFlg = true;
            }


            if (nonErrFlg) {
                // ZIPファイルを作成する
                downloadFilePath = createZipFile(emsRecordPaths[0]);

                // 保存ファイル名を作成
                saveFilename = createSaveFileNameForZip("TotalizationEmsLineListFolder", today);

                // S3アップロード
                nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
                if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLineGroupCsv():S3 Upload Failed"));
            }

        } catch(Exception e) {
            eventLogger.error(this.getClass().getName().concat(".aggregationLineGroupCsv(): (" +  e.getMessage() + ")"));
            return true;
        }

        return !nonErrFlg;
    }

    /**
     * 系統別計測値の全建物出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return true:エラー、false:正常
     */
    public boolean aggregationLineBuildingAllCsv(String targetDateFrom, String targetDateTo, String parentId,
            String childId, String lineName, String lineId, String outputName,
            boolean extendsPlusMinus12hEnabled, String emsOutputFormatCsvItemsCd) {
        Date today = getCurrentDateTime();

        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        // 建物番号でソート
        targetBuildingList
                .sort(Comparator.comparing((AnalysisAllBuildingListResultData o) -> o.getBuildingId())
                        .thenComparing((AnalysisAllBuildingListResultData o) -> o.getBuildingId()));

        //企業系統のグループIDを取得
        Long corpLineGroupId = getCorpLineGroupId();

        //企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(corpLineGroupId);

        //全体を先頭に設定
        for(LineListDetailResultData lineData : corpLineDetailList) {
            if(lineData.getLineNo().equals("ALL")) {
                corpLineDetailList.remove(lineData);
                corpLineDetailList.add(0,lineData);
                break;
            }
        }

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLineListFolder", today, true);

        boolean nonErrFlg = false;
        try {
            downloadFilePath = "";
            saveFilename = "";

            // CSV出力
            downloadFilePath = createLineCsv(targetBuildingList, targetDateFrom, targetDateTo,
                    extendsPlusMinus12hEnabled, lineId, outputName, null, null, null, null, corpLineGroupId,
                    lineName, emsRecordPaths[1], emsOutputFormatCsvItemsCd);

            // エラーでなければ
            if (!CheckUtility.isNullOrEmpty(downloadFilePath)) {
                nonErrFlg = true;
            }


            if (nonErrFlg) {
                // ZIPファイルを作成する
                downloadFilePath = createZipFile(emsRecordPaths[0]);

                // 保存ファイル名を作成
                saveFilename = createSaveFileNameForZip("TotalizationEmsLineListFolder", today);

                // S3アップロード
                nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
                if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLineBuildingAllCsv():S3 Upload Failed"));
            }

        } catch(Exception e) {
            eventLogger.error(this.getClass().getName().concat(".aggregationLineBuildingAllCsv(): (" +  e.getMessage() + ")"));
            return true;
        }
        return !nonErrFlg;
    }


    /**
     * ポイントの全建物出力処理（目安時間以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return
     */
    public boolean aggregationPointAll(String targetDateFrom, String targetDateTo) {
        Date today = getCurrentDateTime();
        // 全建物出力
        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsPointListFolder", today, true);

        //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        //対象期間のカレンダーリストを作成
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        } catch (Exception e) {
            return true;
        }
        List<Date> dateList = createTermCalendar(measurementDateFrom, measurementDateTo);

        //Excel出力
        boolean nonErrFlg = false;
        nonErrFlg = createPointExcel(targetBuildingList, measurementDateFrom, measurementDateTo, dateList,
                targetTerm, corpSumDate, emsRecordPaths);

        // ZIPファイルを作成する
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsPointListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationPointAll():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * ポイントの建物指定出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @return true:エラー、false:正常
     */
    public boolean aggregationPointBuildingNo(String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        //建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsPointListFolder", today, true);

        //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        //対象期間のカレンダーリストを作成
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
        } catch (Exception e) {
            return true;
        }
        List<Date> dateList = createTermCalendar(measurementDateFrom, measurementDateTo);

        //Excel出力
        boolean nonErrFlg = false;
        nonErrFlg = createPointExcel(targetBuildingList,measurementDateFrom,measurementDateTo,dateList,targetTerm,corpSumDate,emsRecordPaths);

        // ZIPファイルを作成する
        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsPointListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationPointBuildingNo():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * ポイントの建物指定出力処理（目安時間算出以降）
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @return true:エラー、false:正常
     */
    public boolean aggregationPointBuildingNoCsv( String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter, Long selectedLineGroupId) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        //建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsPointListFolder", today, false);

         //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        boolean nonErrFlg = false;
        Date measurementDateFrom;
        Date measurementDateTo;
        try {
            measurementDateFrom = DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            measurementDateTo = DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
            nonErrFlg = createPointCsv(targetBuildingList, selectedLineGroupId, measurementDateFrom, measurementDateTo,
                    corpSumDate, emsRecordPaths[0]);
        } catch (Exception e) {
            eventLogger.error(this.getClass().getName().concat(".aggregationPointBuildingNoCsv(): (" +  e.getMessage() + ")"));
        }

        // ZIPファイルを作成する
        downloadFilePath = "";
        saveFilename = "";
        if(nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsPointListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationPointBuildingNoCsv():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 時限標準値の全建物出力処理
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @return true:エラー、false:正常
     */
    public boolean aggregationTimeStandardAll(String targetDateFrom, String targetDateTo) {
        Date today = getCurrentDateTime();

        // 全建物出力
        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        // 対象建物が無い場合はエラーを返す
        if (targetBuildingList == null || targetBuildingList.isEmpty()) {
            eventLogger.error(this.getClass().getName().concat(".aggregationTimeStandardAll():targetBuildingList No Result"));
            noResultFlgOn();
            return true;
        }

        //企業系統のグループIDを取得
        Long corpLineGroupId = getCorpLineGroupId();

        //企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(corpLineGroupId);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsTimeStandardFolder", today, true);

        //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        //Excel出力
        boolean nonErrFlg = false;
        nonErrFlg = createTimeStandardExcel(targetBuildingList, corpLineGroupId, corpLineDetailList, SEARCH_TYPE.ALL,
                targetTerm, corpSumDate, emsRecordPaths);

        // ZIPファイルを作成する
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsTimeStandardFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationTimeStandardAll():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 時限標準値の建物指定出力処理
     *
     * @param nendo
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @param lineGroupId
     * @return true:エラー、false:正常
     */
    public boolean aggregationTimeStandardBuildingNo(String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter, Long lineGroupId) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        // 建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // 対象建物が無い場合はエラーを返す
        if (targetBuildingList == null || targetBuildingList.isEmpty()) {
            eventLogger.error(this.getClass().getName().concat(".aggregationTimeStandardBuildingNo():targetBuildingList No Result"));
            noResultFlgOn();
            return true;
        }

        //企業系統の系統情報を取得
        List<LineListDetailResultData> corpLineDetailList = getCorpLineList(lineGroupId);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsTimeStandardFolder", today, true);

        //企業デマンドから企業集計日を取得
        String corpSumDate = getCorpSumDate();

        //Excel出力
        boolean nonErrFlg = false;
        nonErrFlg = createTimeStandardExcel(targetBuildingList, lineGroupId, corpLineDetailList, SEARCH_TYPE.NO,
                targetTerm, corpSumDate, emsRecordPaths);

        // ZIPファイルを作成する
        if (nonErrFlg) {
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsTimeStandardFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationTimeStandardBuildingNo():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 負荷制御履歴の全建物出力処理（目安時間算出以降）
     *
     * @param targetDateFrom
     * @param targetDateTo
     * @return
     */
    public boolean aggregationLoadCtrlBuildingAll(String targetDateFrom, String targetDateTo) {
        Date today = getCurrentDateTime();
        // 全建物出力

        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLoadControlHistListFolder", today, false);

        boolean nonErrFlg = false;

        // エクセル出力
        nonErrFlg = createLoadCtrlBuildingExcel(targetBuildingList, targetDateFrom, targetDateTo, targetTerm,
                emsRecordPaths);

        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            // ZIPファイルを作成する
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsLoadControlHistListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLoadCtrlBuildingAll():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 負荷制御履歴の建物指定出力処理（目安時間算出以降）
     *
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @return
     */
    public boolean aggregationLoadCtrlBuildingNo(String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        // 建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsLoadControlHistListFolder", today, false);

        boolean nonErrFlg = false;

        // エクセル出力
        nonErrFlg = createLoadCtrlBuildingExcel(targetBuildingList, targetDateFrom, targetDateTo, targetTerm,
                emsRecordPaths);

        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            // ZIPファイルを作成する
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsLoadControlHistListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationLoadCtrlBuildingNo():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * イベント制御履歴の全建物出力処理（目安時間算出以降）
     *
     * @param targetDateFrom
     * @param targetDateTo
     * @return
     */
    public boolean aggregationEventCtrlBuildingAll(String targetDateFrom, String targetDateTo) {
        Date today = getCurrentDateTime();
        // 全建物出力

        List<AnalysisAllBuildingListResultData> targetBuildingList = getAllTargetBuildingList();

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsEventControlHistListFolder", today, false);

        boolean nonErrFlg = false;

        // エクセル出力
        nonErrFlg = createEventCtrlBuildingExcel(targetBuildingList, targetDateFrom, targetDateTo, targetTerm,
                emsRecordPaths);

        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            // ZIPファイルを作成する
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsEventControlHistListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationEventCtrlBuildingAll():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * イベント制御履歴の建物指定出力処理（目安時間算出以降）
     *
     * @param targetDateFrom
     * @param targetDateTo
     * @param buildingNo
     * @param buildingName
     * @param buildingId
     * @param buildingFilter
     * @return
     */
    public boolean aggregationEventCtrlBuildingNo(String targetDateFrom, String targetDateTo,
            String buildingNo, String buildingName, String buildingId, Boolean buildingFilter) {
        Date today = getCurrentDateTime();
        List<AnalysisAllBuildingListResultData> targetBuildingList;

        // 建物指定
        targetBuildingList = getTargetIdBuildingList(buildingId, buildingFilter);

        // メインシート出力のために解析
        String targetTerm = targetDateFrom + "～" + targetDateTo;

        // ファイルを保存するフォルダを作成
        String[] emsRecordPaths = createFolder("TotalizationEmsEventControlHistListFolder", today, false);

        boolean nonErrFlg = false;

        // エクセル出力
        nonErrFlg = createEventCtrlBuildingExcel(targetBuildingList, targetDateFrom, targetDateTo, targetTerm,
                emsRecordPaths);

        downloadFilePath = "";
        saveFilename = "";
        if (nonErrFlg) {
            // ZIPファイルを作成する
            downloadFilePath = createZipFile(emsRecordPaths[0]);

            // 保存ファイル名を作成
            saveFilename = createSaveFileNameForZip("TotalizationEmsEventControlHistListFolder", today);

            // S3アップロード
            nonErrFlg = executeS3Upload(downloadFilePath, saveFilename);
            if (nonErrFlg == false) eventLogger.error(this.getClass().getName().concat(".aggregationEventCtrlBuildingNo():S3 Upload Failed"));
        }

        return !nonErrFlg;
    }

    /**
     * 系統別計測値をグループ指定で建物別に出力する処理
     * @return
     */

    private boolean createLineGroupExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            SEARCH_TYPE searchType,
            List<Date> dateList,
            Date measurementDateFrom,
            Date measurementDateTo,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths,
            Boolean isOutputSummary) {
        //対象建物毎に機器情報を取得する
        int fileIndex = 1;
        int folderIndex = 1;
        Long beforeChildGroupId = null;
        String childFolderPath = "";
        int dataNotExistCount = 0;

        //建物情報を子グループの並び順に並び変える
        Comparator<AnalysisAllBuildingListResultData> comparator = Comparator
                .comparing(AnalysisAllBuildingListResultData::getChildGroupDisplayOrder)
                .thenComparing(AnalysisAllBuildingListResultData::getBuildingNo);
        targetBuildingList = targetBuildingList.stream().sorted(comparator).collect(Collectors.toList());

        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            initMap();

            // 建物毎の系統計測値情報取得処理
            AnalysisEmsBuildingLineListParameter analysisEmsBuildingLineListParameter = new AnalysisEmsBuildingLineListParameter();
            settingParameter(analysisEmsBuildingLineListParameter);
            analysisEmsBuildingLineListParameter.setBuildingId(targetBuilding.getBuildingId());
            analysisEmsBuildingLineListParameter.setLineGroupId(corpLineGroupId);
            analysisEmsBuildingLineListParameter.setMeasurementDateFrom(measurementDateFrom);
            analysisEmsBuildingLineListParameter.setMeasurementDateTo(measurementDateTo);

            try {
                AnalysisEmsBuildingLineListResult result = analysisEmsBuildingLineListDao.query(analysisEmsBuildingLineListParameter);

                if (result == null || (result != null && result.getDemandDayReportLineList().isEmpty())) {
                    // 取得件数が0の場合はカウントアップ
                    dataNotExistCount++;
                }

                // シート毎の連番
                String sheetNo = "_" + 0 + "_";

                // 登録用Mapの初期化
                initSearchLineGroupMap(sheetNo);
                Map<String, String> defineNameMap = getLineGroupDefineNameMapEachSheetNo(sheetNo);
                Date today = getCurrentDateTime();

                // 出力データ取得
                if (isOutputSummary) {
                    outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.SUMMARY_LINK.getName())).add("サマリーへ");

                    // プロパティファイルからエクセルファイル名を取得
                    String filename = createExcelFileName("TotalizationEmsLineListGroup", today);

                    optionSettingMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.SUMMARY_LINK.getName())).add("../../" + filename);
                }
                // 建物名
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.BUILDING_NAME.getName())).add(targetBuilding.getBuildingNo() + "_" + targetBuilding.getBuildingName());
                // 出力日時
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.OUTPUT_DATE.getName())).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
                // 対象期間
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.TARGET_TERM.getName())).add(targetTerm);
                // 開店準備開始
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.WORK_START_TIME.getName())).add(targetBuilding.getWorkStartTime());
                // 営業開始
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.SHOP_OPEN_TIME.getName())).add(targetBuilding.getShopOpenTime());
                //  営業終了
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.SHOP_CLOSE_TIME.getName())).add(targetBuilding.getShopCloseTime());
                // 閉店準備終了
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.WORK_END_TIME.getName())).add(targetBuilding.getWorkEndTime());
                // 親グループ名
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.PARENTS_GROUP_NAME.getName())).add(targetBuilding.getParentGroupName());
                // 子グループ名
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.CHILD_GROUP_NAME.getName())).add(targetBuilding.getChildGroupName());
                // 企業集計日
                outputDataMap.get(defineNameMap.get(LINE_GROUP_DEFINE_NAME.CORP_SUM_DATE.getName()))
                        .add(DateUtility.getFormateedSumDate(corpSumDate));

                for (Date currentDate : dateList) {

                    //系統計測値を出力
                    List<LineListDetailResultData> buildingLineDetailList = corpLineDetailList;

                    //建物に外気温がない場合は系統から削除する
                    if (targetBuilding.getOutAirTempDispFlg().equals(OsolConstants.FLG_OFF)) {
                        buildingLineDetailList =
                                corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
                    }

                    Iterator<LineListDetailResultData> iterator = buildingLineDetailList.iterator();
                    Boolean isFirst = true;
                    while (iterator.hasNext()) {
                        LineListDetailResultData line = iterator.next();

                        List<CommonDemandDayReportLineListResult> lineDataList =
                                result.getDemandDayReportLineList().stream()
                                .filter(o -> o.getMeasurementDate().equals(currentDate))
                                .filter(o -> o.getLineNo().equals(line.getLineNo()))
                                .collect(Collectors.toList());

                        setGroupLineInfo(line, lineDataList, currentDate, defineNameMap, iterator.hasNext(), isFirst);

                        isFirst = false;
                    }
                }

                //追加するシート名を指定(使用不可文字列を削除
                String trueSmName = ExcelUtility.getTrueExcelSheetName(targetBuilding.getBuildingName());
                //31文字以上になる場合、後ろを削除する
                if (trueSmName.length() >= 31) {
                    trueSmName = trueSmName.substring(0, 30);
                } else if (trueSmName.length() == 0) {
                    //0文字の場合、emptyを挿入する(POI準拠)
                    trueSmName = "empty";
                }

                emsNameList.add(trueSmName);
                //追加するシートのコピー元シート名を指定
                orgCloneSheetNameMap.put(trueSmName, ApiAnalysisEmsConstants.TEMPLATE_SHEET_NAME);
                //追加するシートの名前定義マップを指定
                templateMap.put(trueSmName, getLineGroupDefineNameMap(sheetNo));

                //追加するファイル名を指定(使用不可文字列を削除
                String trueChildGroupName = ExcelUtility.getTrueFileName(targetBuilding.getChildGroupName(), true);

                //子グループ毎にフォルダを切り替える処理
                if (beforeChildGroupId == null || !beforeChildGroupId.equals(targetBuilding.getChildGroupId())) {
                    fileIndex = 1;
                    // 子グループ用のフォルダパス
                    childFolderPath= emsRecordPaths[1].concat(File.separator).concat(folderIndex + "_" + trueChildGroupName);
//                    eventLogger.debug(this.getClass().toString() + ": ■ emsRecordFolderPath:" + childFolderPath);
                    File childFolderDir = new File(childFolderPath);

                    // フォルダーが存在しない場合作成
                    if (!childFolderDir.exists()) {
                        childFolderDir.mkdir();
                    }
                    folderIndex++;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("LineGroup", fileIndex, targetBuilding.getBuildingName(), today);

                ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                        emsNameList, orgCloneSheetNameMap, templateMap,
                        filename, ApiAnalysisEmsConstants.LINE_GROUP_TEMPLATE_FILE_NAME, today,
                        getExcelTemplateDir(), childFolderPath, true);

                clearMap();

                fileIndex++;
                beforeChildGroupId = targetBuilding.getChildGroupId();

            } catch (Exception e) {
                dataNotExistCount++;
            }
        }

        // 出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createLineGroupExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    /**
     * 系統別計測値のサマリーを出力する処理
     * @return
     */

    private boolean createGroupLineSummaryExcel(
            Long parentId,
            Long childId,
            List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            String parentGroupName,
            Map<String, String> childGroupNameList,
            Date measurementDateFrom,
            Date measurementDateTo,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths,
            Boolean isOutputBuilding) {

        // 処理ステータスチェック
        if(chkProcessStop()) {
            // 処理中断
            return false;
        }

        initMap();
        // グループ毎のサマリーデータ取得処理
        AnalysisEmsGroupLineSummaryParameter analysisEmsGroupLineSummaryParameter = new AnalysisEmsGroupLineSummaryParameter();
       settingParameter(analysisEmsGroupLineSummaryParameter);
        analysisEmsGroupLineSummaryParameter.setParentGroupId(parentId);
        analysisEmsGroupLineSummaryParameter.setChildGroupId(childId);
        analysisEmsGroupLineSummaryParameter.setLineGroupId(corpLineGroupId);
        analysisEmsGroupLineSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
        analysisEmsGroupLineSummaryParameter.setMeasurementDateTo(measurementDateTo);

        try {
            AnalysisEmsGroupLineSummaryResult result = analysisEmsGroupLineSummaryDao.query(analysisEmsGroupLineSummaryParameter);

            if (result == null || (result != null && result.getGroupLineSummaryList().isEmpty())) {
                // 取得件数が0の場合ははエラー
                eventLogger.error(this.getClass().getName().concat(".createGroupLineSummaryExcel():AnalysisEmsGroupLineSummaryResult Output Target No Result"));
                noResultFlgOn();
                return false;
            }

            initSearchLineGroupSummaryMap();
            Date today = getCurrentDateTime();

            // 出力データ取得
            //出力日時
            outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.OUTPUT_DATE.getName()).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
            //対象期間
            outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.TARGET_TERM.getName()).add(targetTerm);
            //対象建物・テナント
            outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.TARGET_BUILDING_CONDITION.getName()).add(parentGroupName);
            //企業集計日
            outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CORP_SUM_DATE.getName())
                    .add(DateUtility.getFormateedSumDate(corpSumDate));

            //系統毎の全体のサマリーデータを出力する
            //childIdがある場合、対象グループを絞る
            if (childId != null) {
                childGroupNameList = childGroupNameList.entrySet().stream()
                        .filter(o -> o.getValue().equals(String.valueOf(childId)))
                        .collect(Collectors.toMap(o -> o.getKey(), o -> o.getValue()));
            }

            Iterator<Map.Entry<String, String>> childGroupIterator = childGroupNameList.entrySet().iterator();
            while (childGroupIterator.hasNext()) {
                Map.Entry<String, String> childGroupNameEntry = childGroupIterator.next();

                //子グループIDの数値変換が失敗したら処理を中断する
                Long childGroupIdL = new Long(0);
                try {
                    childGroupIdL = Long.valueOf(childGroupNameEntry.getValue());
                } catch (Exception e) {
                    eventLogger.error(this.getClass().getName().concat(".createGroupLineSummaryExcel(): (" +  e.getMessage() + ")"));
                    return false;
                }

                //系統毎の全体のサマリーデータを出力する
                List<LineListDetailResultData> summaryLineDetailList = corpLineDetailList;

                //建物に外気温がない場合は系統から削除する
                Long nowGroupId = childGroupIdL;
                Long outAirTempCount = targetBuildingList.stream()
                        .filter(o -> o.getChildGroupId().equals(nowGroupId))
                        .filter(o -> o.getOutAirTempDispFlg().equals(OsolConstants.FLG_ON)).count();

                if (outAirTempCount.equals(new Long(0))) {
                    summaryLineDetailList =
                            corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
                }

                Boolean isFirst = true;
                Iterator<LineListDetailResultData> lineIterator = summaryLineDetailList.iterator();
                while (lineIterator.hasNext()) {
                    LineListDetailResultData line = lineIterator.next();
                    Long tmpChildGroupId = childGroupIdL;
                    //指定無しの選択肢を除外する
                    if (tmpChildGroupId.equals(Long.valueOf("-1"))) {
                        continue;
                    }
                    List<AnalysisEmsGroupLineSummaryResultData> summaryDataList =
                            result.getGroupLineSummaryList().stream()
                            .filter(o -> o.getChildGroupId().equals(tmpChildGroupId))
                            .filter(o -> o.getLineNo().equals(line.getLineNo()))
                            .collect(Collectors.toList());

                    setGroupLineSummaryAllInfo(childGroupNameEntry.getKey(),line,summaryDataList,childGroupIterator.hasNext(),isFirst,lineIterator.hasNext());
                    isFirst = false;
                }

            }

            int sumRowCount = outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).size();
            preSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.ROW_SHIFT.getName()).add(sumRowCount);

            //建物を子グループ順にソート
            Comparator<AnalysisAllBuildingListResultData> comparator =
                    Comparator.comparing(AnalysisAllBuildingListResultData::getChildGroupDisplayOrder).thenComparing(AnalysisAllBuildingListResultData::getBuildingNo);
            targetBuildingList = targetBuildingList.stream().sorted(comparator).collect(Collectors.toList());

            Iterator<AnalysisAllBuildingListResultData> buildingIterator = targetBuildingList.iterator();

            int index = 1;
            Long beforeGroupId = null;
            int groupIndex = 0;
            while (buildingIterator.hasNext()) {

                AnalysisAllBuildingListResultData targetBuilding = buildingIterator.next();
                // 建物毎の系統計測値情報取得処理
                AnalysisEmsBuildingSummaryParameter analysisEmsBuildingSummaryParameter = new AnalysisEmsBuildingSummaryParameter();
                settingParameter(analysisEmsBuildingSummaryParameter);
                analysisEmsBuildingSummaryParameter.setBuildingId(targetBuilding.getBuildingId());
                analysisEmsBuildingSummaryParameter.setLineGroupId(corpLineGroupId);
                analysisEmsBuildingSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
                analysisEmsBuildingSummaryParameter.setMeasurementDateTo(measurementDateTo);

                AnalysisEmsBuildingSummaryResult summaryResult = analysisEmsBuildingSummaryDao.query(analysisEmsBuildingSummaryParameter);

                if (summaryResult == null) {
                    continue;
                }

                if (beforeGroupId == null || !beforeGroupId.equals(targetBuilding.getChildGroupId())) {
                    groupIndex++;
                    beforeGroupId = targetBuilding.getChildGroupId();
                    index = 1;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("LineGroup", index, targetBuilding.getBuildingName(), today);

                String linkPath = ApiAnalysisEmsConstants.CREATE_BUILDING_FOLDER_NAME + "/" + groupIndex + "_" + targetBuilding.getChildGroupName() + "/" + filename;

                //系統毎の全体のサマリーデータを出力する

                List<LineListDetailResultData> buildingLineDetailList = corpLineDetailList;

                //建物に外気温がない場合は系統から削除する
                if (targetBuilding.getOutAirTempDispFlg().equals(OsolConstants.FLG_OFF)) {
                    buildingLineDetailList =
                            corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
                }

                Iterator<LineListDetailResultData> lineIterator = buildingLineDetailList.iterator();
                Boolean isBuildingFirst = true;
                while (lineIterator.hasNext()) {
                    LineListDetailResultData line = lineIterator.next();
                    List<AnalysisEmsAllLineSummaryResultData> summaryDataList =
                            summaryResult.getBuildingSummaryList().stream()
                            .filter(o -> o.getLineNo().equals(line.getLineNo()))
                            .collect(Collectors.toList());

                    setGroupLineSummaryBillInfo(targetBuilding, line,summaryDataList,buildingIterator.hasNext(),isBuildingFirst,lineIterator.hasNext(),isOutputBuilding,linkPath);
                    isBuildingFirst = false;
                }

                index++;
            }

            // プロパティファイルからエクセルファイル名を取得
            String filename = createExcelFileName("TotalizationEmsLineListGroup", today);

            String templateName = "";
            if (isOutputBuilding) {
                templateName = ApiAnalysisEmsConstants.LINE_GROUP_SUMMARY_TEMPLATE_LINK_FILE_NAME;
            } else {
                templateName = ApiAnalysisEmsConstants.LINE_GROUP_SUMMARY_TEMPLATE_FILE_NAME;
            }

            ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                    emsNameList, orgCloneSheetNameMap, templateMap,
                    filename, templateName, today,
                    getExcelTemplateDir(), emsRecordPaths[0], true);

            clearMap();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 系統別計測値を建物別に出力する処理
     * @return
     */

    private boolean createLineBuildingExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            SEARCH_TYPE searchType,
            List<Date> dateList,
            Date measurementDateFrom,
            Date measurementDateTo,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths,
            Boolean isOutputSummary) {

        // 系統グループの情報を取得する
        LineGroupSearchDetailResultData lineGroupInfo = getLineGroupInfo(corpLineGroupId);

        //対象建物毎に機器情報を取得する
        int index = 1;
        int dataNotExistCount = 0;
        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            List<LineListDetailResultData> buildingLineDetailList = corpLineDetailList;

            //建物に外気温がない場合は系統から削除する
            if (targetBuilding.getOutAirTempDispFlg().equals(OsolConstants.FLG_OFF)) {
                buildingLineDetailList =
                        corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
            }

            initMap();
            // 建物毎の系統計測値情報取得処理
            AnalysisEmsBuildingLineListParameter analysisEmsBuildingLineListParameter = new AnalysisEmsBuildingLineListParameter();
            settingParameter(analysisEmsBuildingLineListParameter);
            analysisEmsBuildingLineListParameter.setBuildingId(targetBuilding.getBuildingId());
            analysisEmsBuildingLineListParameter.setLineGroupId(corpLineGroupId);
            analysisEmsBuildingLineListParameter.setMeasurementDateFrom(measurementDateFrom);
            analysisEmsBuildingLineListParameter.setMeasurementDateTo(measurementDateTo);

            try {
                AnalysisEmsBuildingLineListResult result = analysisEmsBuildingLineListDao.query(analysisEmsBuildingLineListParameter);

                if (result == null || (result != null && result.getDemandDayReportLineList().isEmpty())) {
                    // 取得件数が0の場合はカウントアップ
                    dataNotExistCount++;
                }

                // シート毎の連番
                String sheetNo = "_" + 0 + "_";

                // 登録用Mapの初期化
                initSearchLineBuildingMap(sheetNo);
                Date today = getCurrentDateTime();
                Map<String, String> defineNameMap = getLineBuildingDefineNameMapEachSheetNo(sheetNo);

                // 出力データ取得
                if (isOutputSummary) {
                    outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.SUMMARY_LINK.getName())).add("サマリーへ");

                    // プロパティファイルからエクセルファイル名を取得
                    String filename = createExcelFileName("TotalizationEmsLineListBuilding", today);

                    optionSettingMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.SUMMARY_LINK.getName()))
                            .add("../" + filename);
                }

                // 建物名
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.BUILDING_NAME.getName()))
                        .add(targetBuilding.getBuildingNo() + "_" + targetBuilding.getBuildingName());
                // 出力日時
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.OUTPUT_DATE.getName())).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
                // 対象期間
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.TARGET_TERM.getName())).add(targetTerm);
                // 開店準備開始
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.WORK_START_TIME.getName())).add(targetBuilding.getWorkStartTime());
                // 営業開始
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.SHOP_OPEN_TIME.getName())).add(targetBuilding.getShopOpenTime());
                // 営業終了
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.SHOP_CLOSE_TIME.getName())).add(targetBuilding.getShopCloseTime());
                // 閉店準備終了
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.WORK_END_TIME.getName())).add(targetBuilding.getWorkEndTime());
                // 企業集計日
                outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.CORP_SUM_DATE.getName()))
                        .add(DateUtility.getFormateedSumDate(corpSumDate));
                // 系統グループ
                if (lineGroupInfo == null) {
                    outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.LINE_GROUP_ID.getName())).add(OsolConstants.STR_EMPTY);
                } else {
                    outputDataMap.get(defineNameMap.get(LINE_BUILDING_DEFINE_NAME.LINE_GROUP_ID.getName())).add(lineGroupInfo.getLineGroupName());
                }

                for (Date currentDate : dateList) {

                    // 系統計測値を出力

                    Iterator<LineListDetailResultData> iterator = buildingLineDetailList.iterator();
                    Boolean isFirst = true;
                    while (iterator.hasNext()) {
                        LineListDetailResultData line = iterator.next();

                        List<CommonDemandDayReportLineListResult> lineDataList =
                                result.getDemandDayReportLineList().stream()
                                .filter(o -> o.getMeasurementDate().equals(currentDate))
                                .filter(o -> o.getLineNo().equals(line.getLineNo()))
                                .collect(Collectors.toList());

                        setBuildingLineInfo(line, lineDataList, currentDate, defineNameMap, iterator.hasNext(), isFirst);

                        isFirst = false;

                    }

                }
                // 追加するシート名を指定(使用不可文字列を削除
                String trueSmName = ExcelUtility.getTrueExcelSheetName(targetBuilding.getBuildingName());
                // 31文字以上になる場合、後ろを削除する
                if (trueSmName.length() >= 31) {
                    trueSmName = trueSmName.substring(0, 30);
                } else if (trueSmName.length() == 0) {
                    //0文字の場合、emptyを挿入する(POI準拠)
                    trueSmName = "empty";
                }

                emsNameList.add(trueSmName);
                // 追加するシートのコピー元シート名を指定
                orgCloneSheetNameMap.put(trueSmName, ApiAnalysisEmsConstants.TEMPLATE_SHEET_NAME);
                // 追加するシートの名前定義マップを指定
                templateMap.put(trueSmName, getLineBuildingDefineNameMap(sheetNo));

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("LineBuilding", index, targetBuilding.getBuildingName(), today);

                ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                        emsNameList, orgCloneSheetNameMap, templateMap,
                        filename, ApiAnalysisEmsConstants.LINE_BUILDING_TEMPLATE_FILE_NAME, today,
                        getExcelTemplateDir(), emsRecordPaths[1], true);

                clearMap();

                index++;

            } catch (Exception e) {
                dataNotExistCount++;
            }
        }

        // 出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createLineBuildingExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    /**
     * 系統別計測値のサマリーを出力する処理
     * @return
     */

    private boolean createLineSummaryExcel(
            String buildingId,
            Boolean buildingFilter,
            List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            SEARCH_TYPE searchType,
            List<Date> dateList,
            Date measurementDateFrom,
            Date measurementDateTo,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths,
            Boolean isOutputBuilding) {
        initMap();

        // 系統グループの情報を取得する
        LineGroupSearchDetailResultData lineGroupInfo = getLineGroupInfo(corpLineGroupId);

        // 指定建物のサマリーデータ取得処理
        AnalysisEmsBuildingIdLineSummaryParameter analysisEmsBuildingIdLineSummaryParameter = new AnalysisEmsBuildingIdLineSummaryParameter();
        settingParameter(analysisEmsBuildingIdLineSummaryParameter);
        analysisEmsBuildingIdLineSummaryParameter.setBuildingId(Long.valueOf(buildingId));
        analysisEmsBuildingIdLineSummaryParameter.setBelongTenantFlg(buildingFilter);
        analysisEmsBuildingIdLineSummaryParameter.setLineGroupId(corpLineGroupId);
        analysisEmsBuildingIdLineSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
        analysisEmsBuildingIdLineSummaryParameter.setMeasurementDateTo(measurementDateTo);

        try {
            AnalysisEmsBuildingIdLineSummaryResult result = analysisEmsBuildingIdLineSummaryDao.query(analysisEmsBuildingIdLineSummaryParameter);

            // 取得件数が0の場合はエラー
            if (result == null || (result != null && result.getLineSummaryList().isEmpty())) {
                eventLogger.error(this.getClass().getName().concat(".createLineSummaryExcel():AnalysisEmsBuildingIdLineSummaryResult Output Target No Result"));
                noResultFlgOn();
                return false;
            }

            initSearchLineBuildingSummaryMap();
            Date today = getCurrentDateTime();

            // 出力データ取得
            // 出力日時
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.OUTPUT_DATE.getName()).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
            // 対象期間
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.TARGET_TERM.getName()).add(targetTerm);
            // 対象建物・テナント
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.TARGET_BUILDING_CONDITION.getName()).add(searchType.getLabel());
            // 企業集計日
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.CORP_SUM_DATE.getName())
                    .add(DateUtility.getFormateedSumDate(corpSumDate));
            // 系統グループ
            if (lineGroupInfo == null) {
                outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_GROUP_ID.getName()).add(OsolConstants.STR_EMPTY);
            } else {
                outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_GROUP_ID.getName()).add(lineGroupInfo.getLineGroupName());
            }

            // 系統毎の全体のサマリーデータを出力する
            List<LineListDetailResultData> summaryLineDetailList = corpLineDetailList;

            // 建物に外気温がない場合は系統から削除する
            Long outAirTempCount = targetBuildingList.stream().filter(o -> o.getOutAirTempDispFlg().equals(OsolConstants.FLG_ON)).count();
            if (outAirTempCount.equals(new Long(0))) {
                summaryLineDetailList =
                        corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
            }

            for (LineListDetailResultData line : summaryLineDetailList) {
                List<AnalysisEmsAllLineSummaryResultData> summaryDataList =
                        result.getLineSummaryList().stream()
                        .filter(o -> o.getLineNo().equals(line.getLineNo()))
                        .collect(Collectors.toList());

                setBuildingLineSummaryAllInfo(line, summaryDataList);

            }

            int sumRowCount = outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).size();
            preSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.ROW_SHIFT.getName()).add(sumRowCount);

            int index = 1;
            for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

                List<LineListDetailResultData> buildingLineDetailList = corpLineDetailList;

                // 建物に外気温がない場合は系統から削除する
                if (targetBuilding.getOutAirTempDispFlg().equals(OsolConstants.FLG_OFF)) {
                    buildingLineDetailList =
                            corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
                }

                // 建物毎の系統計測値情報取得処理
                AnalysisEmsBuildingSummaryParameter analysisEmsBuildingSummaryParameter = new AnalysisEmsBuildingSummaryParameter();
                settingParameter(analysisEmsBuildingSummaryParameter);
                analysisEmsBuildingSummaryParameter.setBuildingId(targetBuilding.getBuildingId());
                analysisEmsBuildingSummaryParameter.setLineGroupId(corpLineGroupId);
                analysisEmsBuildingSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
                analysisEmsBuildingSummaryParameter.setMeasurementDateTo(measurementDateTo);

                AnalysisEmsBuildingSummaryResult summaryResult = analysisEmsBuildingSummaryDao.query(analysisEmsBuildingSummaryParameter);

                // 取得結果がnull場合はエラー
                if (summaryResult == null) {
                    continue;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("LineBuilding", index, targetBuilding.getBuildingName(), today);

                String linkPath = ApiAnalysisEmsConstants.CREATE_BUILDING_FOLDER_NAME + "/" + filename;

                //系統毎の全体のサマリーデータを出力する
                Iterator<LineListDetailResultData> iterator = buildingLineDetailList.iterator();
                Boolean isFirst = true;
                while (iterator.hasNext()) {
                    LineListDetailResultData line = iterator.next();
                    List<AnalysisEmsAllLineSummaryResultData> summaryDataList =
                            summaryResult.getBuildingSummaryList().stream()
                            .filter(o -> o.getLineNo().equals(line.getLineNo()))
                            .collect(Collectors.toList());

                    setBuildingLineSummaryBillInfo(targetBuilding.getBuildingNo(),targetBuilding.getBuildingName(), line,summaryDataList,iterator.hasNext(),isFirst,isOutputBuilding,linkPath);

                    isFirst = false;
                }

                index++;

            }

            // プロパティファイルからエクセルファイル名を取得
            String filename = createExcelFileName("TotalizationEmsLineListBuilding", today);

            String templateName = "";
            if (isOutputBuilding) {
                templateName = ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_TEMPLATE_LINK_FILE_NAME;
            } else {
                templateName = ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_TEMPLATE_FILE_NAME;
            }

            ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                    emsNameList, orgCloneSheetNameMap, templateMap,
                    filename, templateName, today,
                    getExcelTemplateDir(), emsRecordPaths[0], true);

            clearMap();
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 系統別計測値のサマリーを出力する処理
     * @return
     */
    private boolean createAllLineSummaryExcel(
            List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            SEARCH_TYPE searchType,
            List<Date> dateList,
            Date measurementDateFrom,
            Date measurementDateTo,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths,
            Boolean isOutputBuilding) {

        // 処理ステータスチェック
        if(chkProcessStop()) {
            // 処理中断
            return false;
        }

        initMap();

        // 系統グループの情報を取得する
        LineGroupSearchDetailResultData lineGroupInfo = getLineGroupInfo(corpLineGroupId);

        // 指定建物のサマリーデータ取得処理
        AnalysisEmsAllLineSummaryParameter analysisEmsAllLineSummaryParameter = new AnalysisEmsAllLineSummaryParameter();
        settingParameter(analysisEmsAllLineSummaryParameter);
        analysisEmsAllLineSummaryParameter.setLineGroupId(corpLineGroupId);
        analysisEmsAllLineSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
        analysisEmsAllLineSummaryParameter.setMeasurementDateTo(measurementDateTo);

        try {
            AnalysisEmsAllLineSummaryResult result = analysisEmsAllLineSummaryDao.query(analysisEmsAllLineSummaryParameter);

            // 取得件数が0の場合はエラー
            if (result == null || (result != null && result.getLineSummaryList().isEmpty())) {
                eventLogger.error(this.getClass().getName().concat(".createAllLineSummaryExcel():AnalysisEmsAllLineSummaryResult Output Target No Result"));
                noResultFlgOn();
                return false;
            }

            initSearchLineBuildingSummaryMap();
            Date today = getCurrentDateTime();
            // 出力データ取得
            // 出力日時
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.OUTPUT_DATE.getName()).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
            // 対象期間
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.TARGET_TERM.getName()).add(targetTerm);
            // 対象建物・テナント
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.TARGET_BUILDING_CONDITION.getName()).add(searchType.getLabel());
            // 企業集計日
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.CORP_SUM_DATE.getName())
                    .add(DateUtility.getFormateedSumDate(corpSumDate));
            // 系統グループ
            if (lineGroupInfo == null) {
                outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_GROUP_ID.getName()).add(OsolConstants.STR_EMPTY);
            } else {
                outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_GROUP_ID.getName()).add(lineGroupInfo.getLineGroupName());
            }
            // 系統毎の全体のサマリーデータを出力する
            List<LineListDetailResultData> summaryLineDetailList = corpLineDetailList;

            // 建物に外気温がない場合は系統から削除する
            Long outAirTempCount = targetBuildingList.stream().filter(o -> o.getOutAirTempDispFlg().equals(OsolConstants.FLG_ON)).count();
            if (outAirTempCount.equals(new Long(0))) {
                summaryLineDetailList =
                        corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
            }

            for (LineListDetailResultData line : summaryLineDetailList) {
                List<AnalysisEmsAllLineSummaryResultData> summaryDataList =
                        result.getLineSummaryList().stream()
                        .filter(o -> o.getLineNo().equals(line.getLineNo()))
                        .collect(Collectors.toList());

                setBuildingLineSummaryAllInfo(line, summaryDataList);
            }

            int sumRowCount = outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).size();
            preSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.ROW_SHIFT.getName()).add(sumRowCount);

            int index = 1;

            for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

                List<LineListDetailResultData> buildingLineDetailList = corpLineDetailList;

                // 建物に外気温がない場合は系統から削除する
                if (targetBuilding.getOutAirTempDispFlg().equals(OsolConstants.FLG_OFF)) {
                    buildingLineDetailList =
                            corpLineDetailList.stream().filter(o -> !o.getLineNo().equals("")).collect(Collectors.toList());
                }

                // 建物毎の系統計測値情報取得処理
                AnalysisEmsBuildingSummaryParameter analysisEmsBuildingSummaryParameter = new AnalysisEmsBuildingSummaryParameter();
                settingParameter(analysisEmsBuildingSummaryParameter);
                analysisEmsBuildingSummaryParameter.setBuildingId(targetBuilding.getBuildingId());
                analysisEmsBuildingSummaryParameter.setLineGroupId(corpLineGroupId);
                analysisEmsBuildingSummaryParameter.setMeasurementDateFrom(measurementDateFrom);
                analysisEmsBuildingSummaryParameter.setMeasurementDateTo(measurementDateTo);

                AnalysisEmsBuildingSummaryResult summaryResult = analysisEmsBuildingSummaryDao.query(analysisEmsBuildingSummaryParameter);

                if (summaryResult == null) {
                    continue;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("LineBuilding", index, targetBuilding.getBuildingName(), today);

                String linkPath = ApiAnalysisEmsConstants.CREATE_BUILDING_FOLDER_NAME + "/" + filename;

                //系統毎の全体のサマリーデータを出力する
                Iterator<LineListDetailResultData> iterator = buildingLineDetailList.iterator();
                Boolean isFirst = true;
                while (iterator.hasNext()) {
                    LineListDetailResultData line = iterator.next();
                    List<AnalysisEmsAllLineSummaryResultData> summaryDataList =
                            summaryResult.getBuildingSummaryList().stream()
                            .filter(o -> o.getLineNo().equals(line.getLineNo()))
                            .collect(Collectors.toList());

                    setBuildingLineSummaryBillInfo(targetBuilding.getBuildingNo(),targetBuilding.getBuildingName(), line,summaryDataList,iterator.hasNext(),isFirst,isOutputBuilding,linkPath);

                    isFirst = false;
                }
                index++;
            }

            // プロパティファイルからエクセルファイル名を取得
            String filename = createExcelFileName("TotalizationEmsLineListBuilding", today);

            String templateName = "";
            if (isOutputBuilding) {
                templateName = ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_TEMPLATE_LINK_FILE_NAME;
            } else {
                templateName = ApiAnalysisEmsConstants.LINE_BUILDING_SUMMARY_TEMPLATE_FILE_NAME;
            }

            ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                    emsNameList, orgCloneSheetNameMap, templateMap,
                    filename, templateName, today,
                    getExcelTemplateDir(), emsRecordPaths[0], true);

            clearMap();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ポイント計測値を出力する処理
     * @return
     */
    private boolean createPointExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            Date measurementDateFrom,
            Date measurementDateTo,
            List<Date> dateList,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths) {
        //対象建物毎に機器情報を取得する
        int index = 1;
        int dataNotExistCount = 0;
        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            // 建物毎の機器情報取得処理
            AnalysisEmsSmListParameter analysisEmsSmListParameter = new AnalysisEmsSmListParameter();
            settingParameter(analysisEmsSmListParameter);
            analysisEmsSmListParameter.setBuildingId(targetBuilding.getBuildingId());
            try {
                AnalysisEmsSmListResult result = analysisEmsSmListDao.query(analysisEmsSmListParameter);

                if (result == null) {
                    eventLogger.error(this.getClass().getName().concat(".createPointExcel():AnalysisEmsSmListResult Output Target No Result"));
                    noResultFlgOn();
                    return false;
                }

                List<AnalysisEmsSmListResultData> smDataList = null;
                if (result != null && result.getTargetSmList() != null  && !result.getTargetSmList().isEmpty()) {
                    smDataList = result.getTargetSmList();
                }

                // 対象機器のポイント計測値情報取得処理
                AnalysisEmsBuildingPointListParameter analysisEmsBuildingPointListParameter = new AnalysisEmsBuildingPointListParameter();
                settingParameter(analysisEmsBuildingPointListParameter);
                analysisEmsBuildingPointListParameter.setBuildingId(targetBuilding.getBuildingId());
                analysisEmsBuildingPointListParameter.setMeasurementDateFrom(measurementDateFrom);
                analysisEmsBuildingPointListParameter.setMeasurementDateTo(measurementDateTo);
                AnalysisEmsBuildingPointListResult pointListResult = analysisEmsBuildingPointListDao.query(analysisEmsBuildingPointListParameter);

                if (pointListResult == null) {
                    eventLogger.error(this.getClass().getName().concat(".createPointExcel():AnalysisEmsBuildingPointListResult Output Target No Result"));
                    noResultFlgOn();
                    return false;
                }

                // 取得件数が0の場合はカウントアップ
                if (pointListResult.getDemandDayReportPointList() == null || pointListResult.getDemandDayReportPointList().isEmpty()) {
                    dataNotExistCount++;
                }

                List<CommonDemandDayReportPointListResult> pointValueList = null;
                if (pointListResult != null && pointListResult.getDemandDayReportPointList() != null
                        && !pointListResult.getDemandDayReportPointList().isEmpty()) {
                    pointValueList = pointListResult.getDemandDayReportPointList();
                }

                Date today = getCurrentDateTime();

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("TotalizationEmsPointListBuilding", index,
                        targetBuilding.getBuildingName(), today);

                AnalysisEmsRecordExcelOutput.outputPointListExcel(filename, today, emsRecordPaths[1], dateList, targetTerm,
                        corpSumDate, targetBuilding, smDataList, pointValueList, genericTypeMap(ApiGenericTypeConstants.GROUP_CODE.POINT_TYPE));
                clearMap();

                index++;

            } catch (Exception e) {
                return false;
            }
        }

        // 出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createPointExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    /**
     * 時限標準値を出力する処理
     * @return
     */
    private boolean createTimeStandardExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            Long corpLineGroupId,
            List<LineListDetailResultData> corpLineDetailList,
            SEARCH_TYPE searchType,
            String targetTerm,
            String corpSumDate,
            String[] emsRecordPaths) {

        // 系統グループの情報を取得する
        LineGroupSearchDetailResultData lineGroupInfo = getLineGroupInfo(corpLineGroupId);

        //対象建物毎に時限標準値を取得する
        int index = 1;
        int dataNotExistCount = 0;
        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            initMap();
            BuildingLineTimeStandardListParameter buildingLineTimeStandardListParameter = new BuildingLineTimeStandardListParameter();
            settingParameter(buildingLineTimeStandardListParameter);
            buildingLineTimeStandardListParameter.setBuildingId(targetBuilding.getBuildingId());
            buildingLineTimeStandardListParameter.setLineGroupId(corpLineGroupId);

            try {
                BuildingLineTimeStandardListResult result = buildingLineTimeStandardListDao.query(buildingLineTimeStandardListParameter);

                if (result == null) {
                    eventLogger.error(this.getClass().getName().concat(".createTimeStandardExcel():BuildingLineTimeStandardListResult Output Target No Result"));
                    noResultFlgOn();
                    return false;
                }

                // 取得件数が0の場合はカウントアップ
                if (result.getDetailList() == null || result.getDetailList().isEmpty()) {
                    dataNotExistCount++;
                }

                // 単一シートのため、名前定後のシート連番不要
                String sheetNo = "";

                // 登録用Mapの初期化
                initSearchTimeStandardMap(sheetNo);
                Date today = getCurrentDateTime();

                Map<String, String> defineNameMap = getTimeStandardDefineNameMapEachSheetNo(sheetNo);

                // 出力データ取得
                // 出力日時
                outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.OUTPUT_DATE.getName())).add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
                // 対象期間
                outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.TARGET_TERM.getName())).add(targetTerm);
                // 対象建物・テナント
                outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.TARGET_BUILDING_CONDITION.getName())).add(searchType.getLabel());
                // 企業集計日
                outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.CORP_SUM_DATE.getName())).add(DateUtility.getFormateedSumDate(corpSumDate));
                // 系統グループ
                if (lineGroupInfo == null) {
                    outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.LINE_GROUP_ID.getName())).add(OsolConstants.STR_EMPTY);
                } else {
                    outputDataMap.get(defineNameMap.get(TIME_STANDARD_DEFINE_NAME.LINE_GROUP_ID.getName())).add(lineGroupInfo.getLineGroupName());
                }

                // 系統毎の時限標準値を出力
                Iterator<LineListDetailResultData> iterator = corpLineDetailList.iterator();
                while (iterator.hasNext()) {
                    LineListDetailResultData lineListDetailResult = iterator.next();
                    for(BuildingLineTimeStandardListDetailResultData lineDetail: result.getDetailList()) {
                        if (lineDetail.getLineNo().equals(lineListDetailResult.getLineNo())) {
                            setTimeStandardBuildingLineInfo(targetBuilding.getBuildingNo(),targetBuilding.getBuildingName(),lineListDetailResult, lineDetail.getTimeList(),defineNameMap,iterator.hasNext());
                            break;
                        }
                    }
                }

                // 追加するシート名を指定(使用不可文字列を削除
                // 複数シートは想定していない
                String trueBuildingName = ExcelUtility.getTrueExcelSheetName(targetBuilding.getBuildingName());
                //31文字以上になる場合、後ろを削除する
                if (trueBuildingName.length() >= 31) {
                    trueBuildingName = trueBuildingName.substring(0, 30);
                } else if (trueBuildingName.length() == 0) {
                    //0文字の場合、emptyを挿入する(POI準拠)
                    trueBuildingName = "empty";
                }

                emsNameList.add(trueBuildingName);
                // 追加するシートのコピー元シート名を指定
                orgCloneSheetNameMap.put(trueBuildingName, ApiAnalysisEmsConstants.TEMPLATE_SHEET_NAME);
                // 追加するシートの名前定義マップを指定
                templateMap.put(trueBuildingName, getTimeStandardDefineNameMap(sheetNo));

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("TotalizationEmsTimeStandardBuilding", index,
                        targetBuilding.getBuildingName(), today);

                ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                        emsNameList, orgCloneSheetNameMap, templateMap,
                        filename, ApiAnalysisEmsConstants.TIME_STANDARD_TEMPLATE_FILE_NAME, today,
                        getExcelTemplateDir(), emsRecordPaths[1], true);

                clearMap();

                index++;

            } catch (Exception e) {
                return false;
            }
        }

        //出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createTimeStandardExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    /**
     * 負荷制御履歴の建物を出力する処理
     *
     * @param targetBuildingList
     * @param today
     * @param targetDateFrom
     * @param targetDateTo
     * @param targetTerm
     * @param emsRecordPaths
     * @return
     */
    private boolean createLoadCtrlBuildingExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            String targetDateFrom, String targetDateTo, String targetTerm, String[] emsRecordPaths) {

        //対象建物毎に機器情報を取得する
        int index = 1;
        int sheetIndex = 1;
        int dataNotExistCount = 0;
        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            // 初期化
            initMap();

            // 建物毎の負荷制御履歴取得処理
            AnalysisEmsLoadControlHistParameter analysisEmsLoadControlHistParameter = new AnalysisEmsLoadControlHistParameter();
            settingParameter(analysisEmsLoadControlHistParameter);
            analysisEmsLoadControlHistParameter.setBuildingId(targetBuilding.getBuildingId());
            analysisEmsLoadControlHistParameter.setRangeDateFrom(targetDateFrom);
            analysisEmsLoadControlHistParameter.setRangeDateTo(targetDateTo);

            try {
                AnalysisEmsLoadControlHistResult result = analysisEmsLoadControlHistDao.query(analysisEmsLoadControlHistParameter);

                if (result == null) {
                    eventLogger.error(this.getClass().getName().concat(".createLoadCtrlBuildingExcel():AnalysisEmsLoadControlHistResult Output Target No Result"));
                    noResultFlgOn();
                    return false;
                }

                // 取得件数が0の場合は次の建物へ
                if (result.getSmResultList() == null || result.getSmResultList().isEmpty()) {
                    dataNotExistCount++;
                    continue;
                }

                Date today = getCurrentDateTime();

                for (AnalysisEmsSmControlHistResultData data : result.getSmResultList()) {
                    //シート毎の連番
                    String sheetNo = "_" + String.valueOf(sheetIndex) + "_";

                    //登録用Mapの初期化
                    initSearchLoadCtrlBuildingMap(sheetNo);
                    Map<String, String> defineNameMap = getLoadCtrlBuildingDefineNameMapSheetNo(sheetNo);

                    //出力日時
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.OUTPUT_DATE.getName()))
                            .add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
                    //対象期間
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.TARGET_TERM.getName())).add(targetTerm);
                    //開店準備開始
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.WORK_START_TIME.getName()))
                            .add(targetBuilding.getWorkStartTime());
                    //営業開始
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SHOP_OPEN_TIME.getName()))
                            .add(targetBuilding.getShopOpenTime());
                    //営業終了
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SHOP_CLOSE_TIME.getName()))
                            .add(targetBuilding.getShopCloseTime());
                    //閉店準備終了
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.WORK_END_TIME.getName()))
                            .add(targetBuilding.getWorkEndTime());

                    // 負荷制御履歴を出力
                    setLoadCtrlInfo(data, defineNameMap);

                    // 合計と建物の行間を調整
                    int sumRowCount = outputDataMap
                            .get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                            .size();
                    preSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.ROW_SHIFT.getName())).add(sumRowCount);

                    //シート名（機器ID）をセット
                    String trueSmName = String.valueOf(data.getSmId());
                    emsNameList.add(trueSmName);
                    //追加するシートのコピー元シート名を指定
                    orgCloneSheetNameMap.put(trueSmName, ApiAnalysisEmsConstants.TEMPLATE_SHEET_NAME);
                    //追加するシートの名前定義マップを指定
                    templateMap.put(trueSmName, getLoadCtrlBuildingDefineNameMap(sheetNo));

                    sheetIndex++;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("TotalizationEmsLoadControlHistListBuilding", index,
                        targetBuilding.getBuildingName(), today);

                // エクセル作成
                ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                        emsNameList, orgCloneSheetNameMap, templateMap,
                        filename, ApiAnalysisEmsConstants.LOAL_CTRL_BUILDING_TEMPLATE_FILE_NAME, today,
                        getExcelTemplateDir(), emsRecordPaths[0], true);

                clearMap();
                index++;

            } catch (Exception e) {
                return false;
            }
        }

        // 出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createLoadCtrlBuildingExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    /**
     * イベント制御履歴の建物を出力する処理
     *
     * @param targetBuildingList
     * @param today
     * @param targetDateFrom
     * @param targetDateTo
     * @param targetTerm
     * @param emsRecordPaths
     * @return
     */
    private boolean createEventCtrlBuildingExcel(List<AnalysisAllBuildingListResultData> targetBuildingList,
            String targetDateFrom, String targetDateTo, String targetTerm, String[] emsRecordPaths) {

        //対象建物毎に機器情報を取得する
        int index = 1;
        int sheetIndex = 1;
        int dataNotExistCount = 0;
        for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

            // 処理ステータスチェック
            if(chkProcessStop()) {
                // 処理中断
                return false;
            }

            // 初期化
            initMap();

            // 建物毎の負荷制御履歴取得処理
            AnalysisEmsEventControlHistParameter analysisEmsEventControlHistParameter = new AnalysisEmsEventControlHistParameter();
            settingParameter(analysisEmsEventControlHistParameter);
            analysisEmsEventControlHistParameter.setBuildingId(targetBuilding.getBuildingId());
            analysisEmsEventControlHistParameter.setRangeDateFrom(targetDateFrom);
            analysisEmsEventControlHistParameter.setRangeDateTo(targetDateTo);

            try {
                AnalysisEmsEventControlHistResult result = analysisEmsEventControlHistDao.query(analysisEmsEventControlHistParameter);

                if (result == null) {
                    eventLogger.error(this.getClass().getName().concat(".createEventCtrlBuildingExcel():AnalysisEmsEventControlHistResult Output Target No Result"));
                    noResultFlgOn();
                    return false;
                }

                // 取得件数が0の場合は次の建物へ
                if (result.getSmResultList() == null || result.getSmResultList().isEmpty()) {
                    dataNotExistCount++;
                    continue;
                }

                Date today = getCurrentDateTime();

                for (AnalysisEmsSmControlHistResultData data : result.getSmResultList()) {
                    //シート毎の連番
                    String sheetNo = "_" + String.valueOf(sheetIndex) + "_";

                    //登録用Mapの初期化
                    initSearchEventCtrlBuildingMap(sheetNo);
                    Map<String, String> defineNameMap = getEventCtrlBuildingDefineNameMapSheetNo(sheetNo);

                    //出力日時
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.OUTPUT_DATE.getName()))
                            .add(DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH));
                    //対象期間
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.TARGET_TERM.getName())).add(targetTerm);
                    //開店準備開始
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.WORK_START_TIME.getName()))
                            .add(targetBuilding.getWorkStartTime());
                    //営業開始
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SHOP_OPEN_TIME.getName()))
                            .add(targetBuilding.getShopOpenTime());
                    //営業終了
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SHOP_CLOSE_TIME.getName()))
                            .add(targetBuilding.getShopCloseTime());
                    //閉店準備終了
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.WORK_END_TIME.getName()))
                            .add(targetBuilding.getWorkEndTime());

                    // イベント制御履歴を出力
                    setEventCtrlInfo(data, defineNameMap);

                    // 合計と建物の行間を調整
                    int sumRowCount = outputDataMap
                            .get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                            .size();
                    preSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.ROW_SHIFT.getName())).add(sumRowCount);

                    //シート名（機器ID）をセット
                    String trueSmName = String.valueOf(data.getSmId());
                    emsNameList.add(trueSmName);
                    //追加するシートのコピー元シート名を指定
                    orgCloneSheetNameMap.put(trueSmName, ApiAnalysisEmsConstants.TEMPLATE_SHEET_NAME);
                    //追加するシートの名前定義マップを指定
                    templateMap.put(trueSmName, getEventCtrlBuildingDefineNameMap(sheetNo));

                    sheetIndex++;
                }

                // プロパティファイルからエクセルファイル名を取得
                String filename = createExcelFileName("TotalizationEmsEventControlHistListBuilding", index,
                        targetBuilding.getBuildingName(), today);

                // エクセル作成
                ExcelUtility.createForExcelWithFullPath(outputDataMap, preSettingMap, optionSettingMap,
                        emsNameList, orgCloneSheetNameMap, templateMap,
                        filename, ApiAnalysisEmsConstants.EVENT_CTRL_BUILDING_TEMPLATE_FILE_NAME, today,
                        getExcelTemplateDir(), emsRecordPaths[0], true);

                clearMap();
                index++;

            } catch (Exception e) {
                return false;
            }
        }

        // 出力データが1つもない場合はエラー
        if (dataNotExistCount == targetBuildingList.size()) {
            eventLogger.error(this.getClass().getName().concat(".createEventCtrlBuildingExcel():Output Target No Result"));
            noResultFlgOn();
            return false;
        }
        return true;

    }

    // --------------------------------------------------------------------
    // ここからCSV処理
    // --------------------------------------------------------------------

    /**
    * 系統別計測値を出力する処理（CSV）
    */
   private String createLineCsv(List<AnalysisAllBuildingListResultData> targetBuildingList, String targetDateFrom,
           String targetDateTo, boolean extendsPlusMinus12hEnabled, String lineId, String outputName, String parentGroupName,
           String childGroupName, Long parentIdL, Long childIdL, Long corpLineGroupId,
           String lineName, String tempDir, String emsOutputFormatCsvItemsCd) throws IOException {

       File file = createLineCsvFile(tempDir);
       List<String> writeLine = new ArrayList<>();
       String firstRow = "";

       // 対象建物の系統情報を出力
       for (AnalysisAllBuildingListResultData building : targetBuildingList) {

           // 処理ステータスチェック
           if(chkProcessStop()) {
               // 処理中断
               return OsolConstants.STR_EMPTY;
           }

           // グループ毎のサマリーデータ取得処理
           AnalysisEmsRecordCsvParameter analysisEmsGroupLineSummaryParameter = new AnalysisEmsRecordCsvParameter();
           settingParameter(analysisEmsGroupLineSummaryParameter);
           analysisEmsGroupLineSummaryParameter.setBuildingId(building.getBuildingId());
           analysisEmsGroupLineSummaryParameter.setParentGroupId(parentIdL);
           analysisEmsGroupLineSummaryParameter.setChildGroupId(childIdL);
           analysisEmsGroupLineSummaryParameter.setLineGroupId(corpLineGroupId);
           analysisEmsGroupLineSummaryParameter.setLineNo(lineId);
           analysisEmsGroupLineSummaryParameter.setEmsExtendsPlusMinus12hEnabled(extendsPlusMinus12hEnabled);
           // ±12h拡張にチェックが入っている場合の集計期間の拡張はAPIで行う
           analysisEmsGroupLineSummaryParameter.setMeasurementDateFrom(
                   DateUtility.conversionDate(targetDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
           if (CheckUtility.isNullOrEmpty(targetDateTo)) {
               analysisEmsGroupLineSummaryParameter
                       .setMeasurementDateTo(analysisEmsGroupLineSummaryParameter.getMeasurementDateFrom());
           } else {
               analysisEmsGroupLineSummaryParameter.setMeasurementDateTo(
                       DateUtility.conversionDate(targetDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH));
           }
           analysisEmsGroupLineSummaryParameter.setSelectedLineId(emsOutputFormatCsvItemsCd);

           try {
               AnalysisEmsGroupLineRecordCsvResult result = analysisEmsRecordCsvDao.query(analysisEmsGroupLineSummaryParameter);

               if (result == null) {
                   eventLogger.error(this.getClass().getName().concat(".createLineCsv():AnalysisEmsGroupLineRecordCsvResult Output Target No Result"));
                   noResultFlgOn();
                   return OsolConstants.STR_EMPTY;
               }

               if (CheckUtility.isNullOrEmpty(firstRow)) {
                   firstRow = selectedCorp.getCorpName() + ",,期間,";
                   if (CheckUtility.isNullOrEmpty(targetDateTo)) {
                       firstRow += "日指定";
                   } else {
                       firstRow += "任意期間";
                   }
                   firstRow += ",,計測日," + targetDateFrom;
                   if (CheckUtility.isNullOrEmpty(targetDateTo)) {
                       if (!extendsPlusMinus12hEnabled) {
                           firstRow += ",";
                       } else {
                           firstRow += "," + "±12h拡張";
                       }
                   } else {
                       firstRow += "," + targetDateTo;
                   }
                   firstRow += ",,系統";
                   if (CheckUtility.isNullOrEmpty(lineId)) {
                       firstRow += ",";
                   } else {
                       firstRow += "," + lineName;
                   }
                   firstRow += ",,計測値";
                   if (CheckUtility.isNullOrEmpty(outputName)) {
                       firstRow += ",";
                   } else {
                       firstRow += "," + outputName;
                   }
                   firstRow += ",,親グループ";
                   if (CheckUtility.isNullOrEmpty(parentGroupName)) {
                       firstRow += ",指定無し";
                   } else {
                       firstRow += "," + parentGroupName;
                   }
                   firstRow += ",,子グループ";
                   if (CheckUtility.isNullOrEmpty(childGroupName)) {
                       firstRow += ",指定無し";
                   } else {
                       firstRow += "," + childGroupName;
                   }
                   writeLine.add(firstRow);

                   // 空行
                   writeLine.add("");

                   // ヘッダ
                   String header = "";

                   for (ApiAnalysisEmsConstants.COMMON_HEADER common : ApiAnalysisEmsConstants.COMMON_HEADER.values()) {
                       header += common.getHeaderName().toString() + ",";
                   }

                   if (CheckUtility.isNullOrEmpty(targetDateTo)) {
                       // 日指定
                       if (!extendsPlusMinus12hEnabled) {
                           // 当日のみを出力
                           for (int i = 1; i <= 48; i++) {
                               header += changeJigenNoToHHMM(new BigDecimal(i)) + ",";
                           }
                       } else {
                           // 当日と当日の±12h（前日の12:30～23:30、翌日の0:30～12:00）を出力
                           for (int i = 25; i <= 48; i++) {
                               header += changeJigenNoToHHMM(new BigDecimal(i)) + ",";
                           }
                           for (int i = 1; i <= 48; i++) {
                               header += changeJigenNoToHHMM(new BigDecimal(i)) + ",";
                           }
                           for (int i = 1; i <= 24; i++) {
                               header += changeJigenNoToHHMM(new BigDecimal(i)) + ",";
                           }
                       }
                   } else {
                       // 任意期間
                       for (int i = 1; i <= 48; i++) {
                           header += changeJigenNoToHHMM(new BigDecimal(i)) + ",";
                       }
                   }
                   writeLine.add(header);
               }

               String dataRow = "";

               // 共通カラム
               dataRow = building.getBuildingId().toString() + ",";
               if (CheckUtility.isNullOrEmpty(building.getBuildingNo())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getBuildingNo() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getBuildingName())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getBuildingName() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getWorkStartTime())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getWorkStartTime() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getShopOpenTime())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getShopOpenTime() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getShopCloseTime())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getShopCloseTime() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getWorkEndTime())) {
                   dataRow += ",";
               } else {
                   dataRow += building.getWorkEndTime() + ",";
               }
               if (CheckUtility.isNullOrEmpty(building.getSumDate())) {
                   dataRow += ",";
               } else {
                   dataRow += DateUtility.getFormateedSumDate(building.getSumDate()) + ",";
               }

               int index = 0;

               if (extendsPlusMinus12hEnabled) {
                   for (ApiAnalysisEmsConstants.OUTPUT_LINE_VAL_RECOEDS_EXTENDS rec : ApiAnalysisEmsConstants.OUTPUT_LINE_VAL_RECOEDS_EXTENDS.values()) {
                       if (result.getRecordCsvList().size() > index && DateUtility.plusDay(DateUtility.conversionDate(targetDateFrom,
                                               DateUtility.DATE_FORMAT_YYYYMMDD_SLASH), rec.getAddDay())
                                       .compareTo(result.getRecordCsvList().get(index).getMeasurementDate()) == 0
                               && rec.getJigenNo().compareTo(result.getRecordCsvList()
                                       .get(index).getJigenNo()) == 0) {
                           if ("合計".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getSummary(), "") + ",";
                           } else if ("平均".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getAverage(), "") + ",";
                           } else if ("最大".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getMax(), "") + ",";
                           } else if ("最小".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getMin(), "") + ",";
                           } else {
                               // 使用量
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getLineValueKw(), "") + ",";
                           }
                           index++;
                       } else {
                           dataRow += ",";
                       }
                   }
               } else {
                   for (OUTPUT_LINE_VAL_RECOEDS_NO_EXTENDS rec : OUTPUT_LINE_VAL_RECOEDS_NO_EXTENDS.values()) {
                       if (result.getRecordCsvList().size() > index
                               && rec.getJigenNo().compareTo(result.getRecordCsvList()
                                       .get(index).getJigenNo()) == 0) {
                           if ("合計".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index).getSummary(), "")  + ",";
                           } else if ("平均".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index).getAverage(), "") + ",";
                           } else if ("最大".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index).getMax(), "") + ",";
                           } else if ("最小".equals(outputName)) {
                               dataRow += Objects.toString(result.getRecordCsvList().get(index).getMin(), "") + ",";
                           } else {
                               // 使用量
                               dataRow += Objects.toString(result.getRecordCsvList().get(index)
                                       .getLineValueKw(), "") + ",";
                           }
                           index++;
                       } else {
                           dataRow += ",";
                       }
                   }
               }
               writeLine.add(dataRow);

           } catch (Exception e) {
               eventLogger.error(this.getClass().getName().concat(".createLineCsv(): (" +  e.getMessage() + ")"));
               return OsolConstants.STR_EMPTY;
           }
       }

       // CSV出力
       downloadFilePath = writeCsvFile(file, writeLine);

       return downloadFilePath;
   }

   /**
    * ポイント計測値を出力する処理
    * @return
    * @throws IOException
    */
   private boolean createPointCsv(List<AnalysisAllBuildingListResultData> targetBuildingList, Long lineGroupId,
           Date measurementDateFrom, Date measurementDateTo, String corpSumDate, String tempDir)
           throws IOException {

       String filePath = "";

       //対象建物毎に機器情報を取得する
       for (AnalysisAllBuildingListResultData targetBuilding : targetBuildingList) {

           // 処理ステータスチェック
           if(chkProcessStop()) {
               // 処理中断
               return false;
           }

           // 建物毎の機器情報取得処理
           AnalysisEmsSmListParameter analysisEmsSmListParameter = new AnalysisEmsSmListParameter();
           settingParameter(analysisEmsSmListParameter);
           analysisEmsSmListParameter.setBuildingId(targetBuilding.getBuildingId());

           try {
               AnalysisEmsSmListResult result = analysisEmsSmListDao.query(analysisEmsSmListParameter);

               if (result == null) {
                   eventLogger.error(this.getClass().getName().concat(".createPointCsv():AnalysisEmsSmListResult Output Target No Result"));
                   noResultFlgOn();
                   return false;
               }

               List<AnalysisEmsSmListResultData> smDataList = null;
               if (result != null && result.getTargetSmList() != null && !result.getTargetSmList().isEmpty()) {
                   smDataList = result.getTargetSmList();
               }

               // 建物に紐づく機器毎にCSVファイルを作成
               for (AnalysisEmsSmListResultData smData : smDataList) {

                   // 対象機器のポイント計測値情報取得処理
                   AnalysisEmsBuildingPointInfoCsvListParameter analysisEmsBuildingPointInfoCsvListParameter = new AnalysisEmsBuildingPointInfoCsvListParameter();
                   settingParameter(analysisEmsBuildingPointInfoCsvListParameter);
                   analysisEmsBuildingPointInfoCsvListParameter.setBuildingId(targetBuilding.getBuildingId());
                   analysisEmsBuildingPointInfoCsvListParameter.setSmId(smData.getSmId());
                   analysisEmsBuildingPointInfoCsvListParameter.setMeasurementDateFrom(measurementDateFrom);
                   analysisEmsBuildingPointInfoCsvListParameter.setMeasurementDateTo(measurementDateTo);
                   AnalysisEmsBuildingPointInfoCsvListResult buildingPointInfoResult = analysisEmsBuildingPointInfoCsvListDao.query(analysisEmsBuildingPointInfoCsvListParameter);

                   if (buildingPointInfoResult == null) {
                       eventLogger.error(this.getClass().getName().concat(".createPointCsv():AnalysisEmsBuildingPointInfoCsvListResult Output Target No Result"));
                       noResultFlgOn();
                       return false;
                   }

                   // 取得件数が0の場合は次の建物へ
                   if (buildingPointInfoResult.getDemandDayReportPointList() == null || buildingPointInfoResult.getDemandDayReportPointList().isEmpty()) {
                       continue;
                   }

                   List<CommonDemandDayReportPointListResult> pointValueList = null;
                   if (buildingPointInfoResult != null && buildingPointInfoResult.getDemandDayReportPointList() != null
                           && !buildingPointInfoResult.getDemandDayReportPointList().isEmpty()) {
                       pointValueList = buildingPointInfoResult.getDemandDayReportPointList();
                   }

                   // 系統情報、屋外温度、系統名取得
                   AnalysisEmsBuildingPointLineInfoCsvListParameter analysisEmsBuildingPointCsvListParameter = new AnalysisEmsBuildingPointLineInfoCsvListParameter();
                   settingParameter(analysisEmsBuildingPointCsvListParameter);
                   analysisEmsBuildingPointCsvListParameter.setCorpId(targetBuilding.getCorpId());
                   analysisEmsBuildingPointCsvListParameter.setBuildingId(targetBuilding.getBuildingId());
                   analysisEmsBuildingPointCsvListParameter.setLineGroupId(lineGroupId);
                   analysisEmsBuildingPointCsvListParameter.setMeasurementDateFrom(measurementDateFrom);
                   analysisEmsBuildingPointCsvListParameter.setMeasurementDateTo(measurementDateTo);
                   analysisEmsBuildingPointCsvListParameter.setJigenNoFrom(new BigDecimal(1));
                   analysisEmsBuildingPointCsvListParameter.setJigenNoTo(new BigDecimal(48));

                   AnalysisEmsBuildingPointLineInfoCsvListResult pointLineInfoResult =  analysisEmsBuildingPointCsvListDao.query(analysisEmsBuildingPointCsvListParameter);


                   if (pointLineInfoResult == null) {
                       eventLogger.error(this.getClass().getName().concat(".createPointCsv():AnalysisEmsBuildingPointLineInfoCsvListResult Output Target No Result"));
                       noResultFlgOn();
                       return false;
                   }

                   // 系統取得処理
                   List<LineListDetailResultData> lineList = new ArrayList<>();;
                   List<LineListDetailResultData> lineListOld = new ArrayList<>();
                   LineListParameter lineParameter = new LineListParameter();
                   settingParameter(lineParameter);
                   lineParameter.setSelectedCorpId(targetBuilding.getCorpId());
                   lineParameter.setLineGroupId(lineGroupId);
                   lineParameter.setLineEnableFlg(OsolConstants.FLG_ON);

                   LineListResult lineListResult = lineListDao.query(lineParameter);

                   if (lineListResult != null && lineListResult.getDetailList() != null && !lineListResult.getDetailList().isEmpty()) {
                       lineList = lineListResult.getDetailList();

                       // 取得してきた系統順が正しくない時があるので、系統番号を昇順にソートする
                       lineList.sort(Comparator.comparing(LineListDetailResultData::getLineNo));

                       // ソート前
                       for (LineListDetailResultData list : lineList) {
                           lineListOld.add(list);
                       }

                       // ソート
                       lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                           int lineNo1 = "ALL".equals(rs1.getLineNo()) ? 0
                                   : "ETC".equals(rs1.getLineNo()) ? Integer.MAX_VALUE
                                           : Integer.parseInt(rs1.getLineNo());
                           int lineNo2 = "ALL".equals(rs2.getLineNo()) ? 0
                                   : "ETC".equals(rs2.getLineNo()) ? Integer.MAX_VALUE
                                           : Integer.parseInt(rs2.getLineNo());
                           return lineNo1 - lineNo2;
                       });
                   }

                   // CSVファイル作成
                   File file = createPointCsvFile(tempDir, smData.getSmId());
                   List<String> writeLine = new ArrayList<>();

                   // 一行目
                   String firstRow = selectedCorp.getCorpName() + "　【" + targetBuilding.getBuildingName() + "】　"
                           + DateUtility.changeDateFormat(measurementDateFrom, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                           + "～"
                           + DateUtility.changeDateFormat(measurementDateTo, DateUtility.DATE_FORMAT_YYYYMMDD_SLASH);
                   writeLine.add(firstRow);

                   // 二行目（改行）
                   writeLine.add("");

                   String header = "";

                   // 三行目（共通ヘッダ、ポイント名）
                   for (DemandBuildingSmPointListDetailResultData pointList : smData.getSmPointList()) {
                       if (CheckUtility.isNullOrEmpty(header)) {
                           header = "計測日,計測時間,集計月日,デマンド値,屋外温度,屋内温度,";
                       }
                       if (!pointList.getPointNo().equals("SRC")) {
                           header += "P" + pointList.getPointNo() + ",";
                       }
                   }

                   // 三行目（ヘッダ（系統名））
                   for (LineListDetailResultData lineHeader : lineList) {
                       header += lineHeader.getLineName() + ",";
                   }
                   writeLine.add(header);

                   int pointIndex = 0;
                   int rowIndex = 0;
                   int dayCnt = 0;
                   int outputJigenNo = 1;

                   String demandDataRow = "";
                   String lineDataRow = "";
                   String pointDataRow = "";
                   String tempDataRow = "";

                   // SRCを除いた要素数
                   String[] pointValueArray = new String[smData.getSmPointList().size() - 1];

                   for (int i = 0; i < pointValueArray.length; i++) {
                       pointValueArray[i] = "";
                   }

                   // 出力済フラグ
                   boolean writeFlg = false;

                   // 集計開始終了の日数を取得
                   int diffDays = (int)((measurementDateTo.getTime() - measurementDateFrom.getTime()) / (1000 * 60 * 60 * 24 ));

                   Date[] targetMeasurementDateArray = new Date[diffDays + 1];
                   for (int i = 0; i <= diffDays; i++) {
                       if (DateUtility.plusDay(measurementDateFrom, i).compareTo(measurementDateTo) != 1) {
                           // 集計開始終了の日付を格納
                           targetMeasurementDateArray[i] = DateUtility.plusDay(measurementDateFrom, i);
                       }
                   }

                   // SRCポイント値をマップに格納
                   Map<String, String> srcPointMap = new HashMap<String, String>();

                   for (CommonDemandDayReportPointListResult info : pointValueList) {
                       if ("SRC".equals(info.getPointNo())) {
                           String dateStr = DateUtility.changeDateFormat(info.getMeasurementDate(),
                                   DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) + "_"
                                   + String.format("%02d", info.getJigenNo().intValue());
                           if (info.getPointVal() == null) {
                               srcPointMap.put(dateStr, "");
                           } else {
                               srcPointMap.put(dateStr, String.valueOf(info.getPointVal()));
                           }
                       }
                   }

                   // SRCポイントを除いたポイントリスト
                   List<CommonDemandDayReportPointListResult> noSrcPointList = new ArrayList<>();

                   for (CommonDemandDayReportPointListResult newList : pointValueList) {
                       if (!"SRC".equals(newList.getPointNo())) {
                           noSrcPointList.add(newList);
                       }
                   }

                   // SRCポイントを除いたポイントリストでループ
                   for (int i = 0; i < noSrcPointList.size(); i++) {

                       // 開始より前に計測日が存在しない場合
                       if (targetMeasurementDateArray[dayCnt]
                               .compareTo(noSrcPointList.get(i).getMeasurementDate()) != 1) {
                           if (writeLine != null && !writeLine.get(writeLine.size() - 1).contains(DateUtility
                                   .changeDateFormat(targetMeasurementDateArray[dayCnt], DateUtility.DATE_FORMAT_YYYYMMDD_SLASH))
                                   && !writeFlg) {
                               for (int j = dayCnt; j < targetMeasurementDateArray.length; j++) {
                                   if (targetMeasurementDateArray[dayCnt]
                                           .compareTo(noSrcPointList.get(i).getMeasurementDate()) == -1) {
                                       // 計測日
                                       demandDataRow += DateUtility.changeDateFormat(targetMeasurementDateArray[dayCnt],
                                               DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) + ",";
                                       // 計測時間
                                       demandDataRow += "未計測,";

                                       writeLine.add(demandDataRow);

                                       dayCnt++;
                                       demandDataRow = "";
                                       writeFlg = true;
                                   }
                               }
                           }
                       }

                       // ポイント値を時限No単位で配列に格納
                       for (DemandBuildingSmPointListDetailResultData pointList : smData.getSmPointList()) {
                           if ("SRC".equals(pointList.getPointNo())) {
                               continue;
                           }
                           if (noSrcPointList.get(i).getPointNo().equals(pointList.getPointNo())) {
                               if (noSrcPointList.get(i).getPointVal() == null) {
                                   pointValueArray[Integer.parseInt(pointList.getPointNo()) - 1] = "";
                               } else {
                                   pointValueArray[Integer.parseInt(pointList.getPointNo()) - 1] = String
                                           .valueOf(noSrcPointList.get(i).getPointVal());
                               }
                           }
                       }

                       if (chkOutputCsv(noSrcPointList, i)) {
                           // 時限Noの歯抜け補正
                           while (outputJigenNo < noSrcPointList.get(i).getJigenNo().intValue()) {
                               demandDataRow += DateUtility.changeDateFormat(
                                       noSrcPointList.get(i).getMeasurementDate(),
                                       DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) + ",";
                               // 計測時間
                               demandDataRow += changeJigenNoToHHMM(new BigDecimal(outputJigenNo - 1)) + " - "
                                       + changeJigenNoToHHMM(new BigDecimal(outputJigenNo)) + ",";

                               writeLine.add(demandDataRow);

                               demandDataRow = "";
                               outputJigenNo++;
                           }

                           // 計測日
                           demandDataRow += DateUtility.changeDateFormat(
                                   noSrcPointList.get(i).getMeasurementDate(),
                                   DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) + ",";
                           // 計測時間
                           demandDataRow += changeJigenNoToHHMM(new BigDecimal(rowIndex)) + " - "
                                   + changeJigenNoToHHMM(new BigDecimal(rowIndex + 1)) + ",";
                           // 集計月日
                           demandDataRow += DateUtility.getFormateedSumDate(targetBuilding.getSumDate()) + ",";

                           List<CommonDemandDayReportLineListResult> tempList = new ArrayList<>();

                           int lineIndex = 0;

                           // 系統情報
                           if (pointLineInfoResult.getPointCsvList() != null && pointLineInfoResult.getPointCsvList().size() != 0) {

                               // 表示対象外の有効でない系統のデマンド日報系統データは除外しておく
                               if (lineListOld != null) {
                                   pointLineInfoResult.getPointCsvList().removeAll(
                                           pointLineInfoResult.getPointCsvList().stream()
                                           .filter(x -> lineListOld.stream().noneMatch(y -> y.getLineNo().equals(x.getLineNo())))
                                           .collect(Collectors.toList()));
                               }

                               if (outputJigenNo == noSrcPointList.get(i).getJigenNo().intValue()
                                       && (pointIndex + lineIndex) < pointLineInfoResult.getPointCsvList().size()
                                       && noSrcPointList.get(i).getMeasurementDate().compareTo(
                                               pointLineInfoResult.getPointCsvList().get(pointIndex + lineIndex).getMeasurementDate()) == 0) {
                                   if (lineListOld != null && lineListOld.size() != 0) {
                                       // 系統情報（系統ヘッダ分ループ）
                                       for (int j = 0; j < lineListOld.size(); j++) {
                                           if (lineListOld.get(j).getLineNo().equals(
                                                   pointLineInfoResult.getPointCsvList().get(pointIndex + lineIndex).getLineNo())
                                                   && String.valueOf(outputJigenNo).equals(
                                                           pointLineInfoResult.getPointCsvList().get(pointIndex + lineIndex).getJigenNo().toString())) {
                                               tempList.add(
                                                       pointLineInfoResult.getPointCsvList().get(pointIndex + lineIndex));
                                               lineIndex++;
                                           } else {
                                               CommonDemandDayReportLineListResult tempBean = new CommonDemandDayReportLineListResult();
                                               tempBean.setLineNo(lineListOld.get(j).getLineNo());
                                               tempList.add(tempBean);
                                           }
                                       }
                                   }

                                   // ALLが先頭、ETCが最終に来るようにソート
                                   tempList.sort((CommonDemandDayReportLineListResult rs1,
                                           CommonDemandDayReportLineListResult rs2) -> {
                                       int lineNo1 = "ALL".equals(rs1.getLineNo()) ? 0
                                               : "ETC".equals(rs1.getLineNo()) ? Integer.MAX_VALUE
                                                       : Integer.parseInt(rs1.getLineNo());
                                       int lineNo2 = "ALL".equals(rs2.getLineNo()) ? 0
                                               : "ETC".equals(rs2.getLineNo()) ? Integer.MAX_VALUE
                                                       : Integer.parseInt(rs2.getLineNo());
                                       return lineNo1 - lineNo2;
                                   });

                                   // 屋外温度
                                   if (pointLineInfoResult.getOutAirTempList()
                                           .get(rowIndex)
                                           .getOutAirTemp() == null) {
                                       tempDataRow += ",";
                                   } else {
                                       tempDataRow += String.valueOf(pointLineInfoResult.getOutAirTempList().get(rowIndex).getOutAirTemp()) + ",";
                                   }

                                   // 系統毎の計測値をセット
                                   for (CommonDemandDayReportLineListResult lineVal : tempList) {
                                       if (lineVal.getLineValueKw() == null) {
                                           lineDataRow += ",";
                                       } else {
                                           lineDataRow += String.valueOf(lineVal.getLineValueKw()) + ",";
                                       }
                                   }
                               } else {
                                   // 屋外温度
                                   tempDataRow += ",";

                                   // デマンド日報系統から取得したリストが想定件数より少ない場合、足りない時限の系統を処理
                                   if ((pointIndex + lineIndex) >= pointLineInfoResult.getPointCsvList().size()
                                           && lineListOld != null && lineListOld.size() != 0) {
                                       for (int j = 0; j < lineListOld.size(); j++) {
                                           CommonDemandDayReportLineListResult tempBean = new CommonDemandDayReportLineListResult();
                                           tempBean.setLineNo(lineListOld.get(j).getLineNo());
                                           tempList.add(tempBean);
                                       }
                                       // ALLが先頭、ETCが最終に来るようにソート
                                       tempList.sort((CommonDemandDayReportLineListResult rs1,
                                               CommonDemandDayReportLineListResult rs2) -> {
                                           int lineNo1 = "ALL".equals(rs1.getLineNo()) ? 0
                                                   : "ETC".equals(rs1.getLineNo()) ? Integer.MAX_VALUE
                                                           : Integer.parseInt(rs1.getLineNo());
                                           int lineNo2 = "ALL".equals(rs2.getLineNo()) ? 0
                                                   : "ETC".equals(rs2.getLineNo()) ? Integer.MAX_VALUE
                                                           : Integer.parseInt(rs2.getLineNo());
                                           return lineNo1 - lineNo2;
                                       });
                                       // 系統毎の計測値をセット
                                       for (CommonDemandDayReportLineListResult lineVal : tempList) {
                                           if (lineVal.getLineValueKw() == null) {
                                               lineDataRow += ",";
                                           } else {
                                               lineDataRow += String.valueOf(lineVal.getLineValueKw()) + ",";
                                           }
                                       }
                                   }
                               }
                           } else {
                               // 屋外温度
                               tempDataRow += ",";
                           }

                           // デマンド値
                           for (Entry<String, String> srcPoint : srcPointMap.entrySet()) {
                               String targetDate = srcPoint.getKey().substring(0, 10);
                               int targetJigenNo = Integer
                                       .parseInt(srcPoint.getKey().substring(srcPoint.getKey().length() - 2));

                               if (DateUtility
                                       .changeDateFormat(noSrcPointList.get(i).getMeasurementDate(),
                                               DateUtility.DATE_FORMAT_YYYYMMDD_SLASH)
                                       .equals(targetDate)
                                       && noSrcPointList.get(i).getJigenNo().intValue() == targetJigenNo) {
                                   demandDataRow += srcPoint.getValue() + ",";
                                   break;
                               }
                           }

                           // 屋外温度を出力行に追加
                           demandDataRow += tempDataRow;

                           // 屋内温度（ブランク）
                           demandDataRow += ",";

                           // ポイント値
                           for (String pointValueStr : pointValueArray) {
                               if (CheckUtility.isNullOrEmpty(pointValueStr)) {
                                   pointDataRow += ",";
                               } else {
                                   pointDataRow += pointValueStr + ",";
                               }
                           }

                           writeLine.add(demandDataRow + pointDataRow + lineDataRow);
                           rowIndex++;
                           outputJigenNo++;
                           writeFlg = false;

                           // 系統数分加算
                           pointIndex += lineIndex;

                           firstRow = "";
                           demandDataRow = "";
                           pointDataRow = "";
                           lineDataRow = "";
                           tempDataRow = "";

                           pointValueArray = new String[smData.getSmPointList().size() - 1];

                           // 最終の時限Noの場合
                           if (i != noSrcPointList.size() - 1 && noSrcPointList.get(i).getJigenNo()
                                   .compareTo(noSrcPointList.get(i + 1).getJigenNo()) != -1) {
                               dayCnt++;
                               outputJigenNo = 1;
                           }

                           // 最終行は日付カウントを加算して補填されないようにする
                           if (i == noSrcPointList.size() - 1) {
                               dayCnt++;
                           }
                       }
                   }

                   // 不足分の補填
                   for (int i = dayCnt; i < targetMeasurementDateArray.length; i++) {
                       writeLine.add(DateUtility.changeDateFormat(targetMeasurementDateArray[i],
                               DateUtility.DATE_FORMAT_YYYYMMDD_SLASH) + "," + "未計測,");
                   }

                   // CSV出力
                   filePath = writeCsvFile(file, writeLine);
               }

           } catch (Exception e) {
               eventLogger.error(this.getClass().getName().concat(".createPointCsv(): (" +  e.getMessage() + ")"));
               return false;
           }
       }

       // 出力対象が0件の場合
       if (CheckUtility.isNullOrEmpty(filePath)) {
           eventLogger.error(this.getClass().getName().concat(".createPointCsv():Output Target No Result"));
           noResultFlgOn();
           return false;
       }
       return true;

   }



    /**
     * 負荷制御履歴の出力値、セル書式を設定
     *
     * @param sumResult
     * @param defineNameMap
     */
    private void setLoadCtrlInfo(AnalysisEmsSmControlHistResultData sumResult, Map<String, String> defineNameMap) {

        String[] sumFirstBorder = createLoadCtrlSumFirstBorder();
        String[] sumCenterBorder = createLoadCtrlSumCenterBorder();
        String[] sumEndBorder = createLoadCtrlSumEndBorder();
        String[] eachFirstBorder = createLoadCtrlEachFirstBorder();
        String[] eachCenterBorder = createLoadCtrlEachCenterBorder();
        String[] eachEndBorder = createLoadCtrlEachEndBorder();

        // 合計
        for (int i = 0; i < sumResult.getControlHistSummary().getControlLoadList().size(); i++) {
            outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName())).add("合計");
            // 負荷名称が設定されている場合
            if (!CheckUtility.isNullOrEmpty(
                    sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadName())) {
                outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                        .add(sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadName());
            } else {
                // 負荷名称が設定されていない場合は、制御負荷回路名称を表示する（K1、Aなど）
                outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                        .add(sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadCircuit());
            }
            outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName())).add("分");

            setLoadCtrlInfoSumCommonOption(i, sumResult.getControlHistSummary().getControlLoadList().size(),
                    defineNameMap, sumFirstBorder, sumCenterBorder, sumEndBorder);

            int sumJigenIndex = 0;
            int sumJigenOptionIndex = 3;
            for (LOAD_CTRL_HISTORY_DEFINE_NAME values : LOAD_CTRL_HISTORY_DEFINE_NAME.values()) {
                if (values.getName().contains("_NUMBER_SUM_")) {
                    setLoadCtrlSumValue(sumResult.getControlHistSummary().getControlLoadList(), i, sumJigenIndex,
                            values.getName(), defineNameMap);
                    setLoadCtrlInfoSumJigenOption(i, sumJigenOptionIndex,
                            sumResult.getControlHistSummary().getControlLoadList().size(), defineNameMap, values.getName(),
                            sumFirstBorder, sumCenterBorder, sumEndBorder);
                    sumJigenIndex++;
                    sumJigenOptionIndex++;
                }
            }
        }

        // 建物
        for (int j = 0; j < sumResult.getPeriodList().size(); j++) {
            for (int k = 0; k < sumResult.getPeriodList().get(j).getControlLoadList().size(); k++) {
                outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                        .add(DateUtility.changeDateFormat(sumResult.getPeriodList().get(j).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER));
                // 負荷名称が設定されている場合
                if (!CheckUtility.isNullOrEmpty(
                        sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadName())) {
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                            .add(sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadName());
                } else {
                    // 負荷名称が設定されていない場合は、制御負荷回路名称を表示する（K1、Aなど）
                    outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                            .add(sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadCircuit());
                }
                outputDataMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName())).add("分");

                setLoadCtrlInfoEachCommonOption(k, sumResult.getPeriodList().get(j).getControlLoadList().size(),
                        defineNameMap, eachFirstBorder, eachCenterBorder, eachEndBorder);

                int eachJigenIndex = 0;
                int eachJigenOptionIndex = 3;
                for (LOAD_CTRL_HISTORY_DEFINE_NAME values : LOAD_CTRL_HISTORY_DEFINE_NAME.values()) {
                    if (values.getName().contains("_NUMBER_EACH_")) {
                        setLoadCtrlEachValue(sumResult.getPeriodList(), j, k, eachJigenIndex, values.getName(),
                                defineNameMap);
                        setLoadCtrlInfoEachJigenOption(k, eachJigenOptionIndex,
                                sumResult.getPeriodList().get(j).getControlLoadList().size(), defineNameMap,
                                values.getName(), eachFirstBorder, eachCenterBorder, eachEndBorder);
                        eachJigenIndex++;
                        eachJigenOptionIndex++;
                    }
                }
            }
        }
    }

    /**
     * イベント制御履歴の出力値、セル書式を設定
     *
     * @param sumResult
     * @param defineNameMap
     */
    private void setEventCtrlInfo(AnalysisEmsSmControlHistResultData sumResult, Map<String, String> defineNameMap) {

        String[] sumFirstBorder = createEventCtrlSumFirstBorder();
        String[] sumCenterBorder = createEventCtrlSumCenterBorder();
        String[] sumEndBorder = createEventCtrlSumEndBorder();
        String[] eachFirstBorder = createEventCtrlEachFirstBorder();
        String[] eachCenterBorder = createEventCtrlEachCenterBorder();
        String[] eachEndBorder = createEventCtrlEachEndBorder();

        // 合計
        for (int i = 0; i < sumResult.getControlHistSummary().getControlLoadList().size(); i++) {
            outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName())).add("合計");
            // 負荷名称が設定されている場合
            if (!CheckUtility.isNullOrEmpty(
                    sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadName())) {
                outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                        .add(sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadName());
            } else {
                // 負荷名称が設定されていない場合は、制御負荷回路名称を表示する（K1、EX01など）
                outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                        .add(sumResult.getControlHistSummary().getControlLoadList().get(i).getControlLoadCircuit());
            }
            outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName())).add("分");

            setEventCtrlInfoSumCommonOption(i, sumResult.getControlHistSummary().getControlLoadList().size(),
                    defineNameMap, sumFirstBorder, sumCenterBorder, sumEndBorder);

            int sumJigenIndex = 0;
            int sumJigenOptionIndex = 3;
            for (EVENT_CTRL_HISTORY_DEFINE_NAME values : EVENT_CTRL_HISTORY_DEFINE_NAME.values()) {
                if (values.getName().contains("_NUMBER_SUM_")) {
                    setEventCtrlSumValue(sumResult.getControlHistSummary().getControlLoadList(), i, sumJigenIndex,
                            values.getName(), defineNameMap);
                    setEventCtrlInfoSumJigenOption(i, sumJigenOptionIndex,
                            sumResult.getControlHistSummary().getControlLoadList().size(), defineNameMap, values.getName(),
                            sumFirstBorder, sumCenterBorder, sumEndBorder);
                    sumJigenIndex++;
                    sumJigenOptionIndex++;
                }
            }
        }

        // 建物
        for (int j = 0; j < sumResult.getPeriodList().size(); j++) {
            for (int k = 0; k < sumResult.getPeriodList().get(j).getControlLoadList().size(); k++) {
                outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                        .add(DateUtility.changeDateFormat(sumResult.getPeriodList().get(j).getMeasurementDate(),
                                DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER));
                // 負荷名称が設定されている場合
                if (!CheckUtility.isNullOrEmpty(
                        sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadName())) {
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                            .add(sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadName());
                } else {
                    // 負荷名称が設定されていない場合は、制御負荷回路名称を表示する（K1、EX01など）
                    outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                            .add(sumResult.getPeriodList().get(j).getControlLoadList().get(k).getControlLoadCircuit());
                }
                outputDataMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName())).add("分");

                setEventCtrlInfoEachCommonOption(k, sumResult.getPeriodList().get(j).getControlLoadList().size(),
                        defineNameMap, eachFirstBorder, eachCenterBorder, eachEndBorder);

                int eachJigenIndex = 0;
                int eachJigenOptionIndex = 3;
                for (EVENT_CTRL_HISTORY_DEFINE_NAME values : EVENT_CTRL_HISTORY_DEFINE_NAME.values()) {
                    if (values.getName().contains("_NUMBER_EACH_")) {
                        setEventCtrlEachValue(sumResult.getPeriodList(), j, k, eachJigenIndex, values.getName(),
                                defineNameMap);
                        setEventCtrlInfoEachJigenOption(k, eachJigenOptionIndex,
                                sumResult.getPeriodList().get(j).getControlLoadList().size(), defineNameMap,
                                values.getName(), eachFirstBorder, eachCenterBorder, eachEndBorder);
                        eachJigenIndex++;
                        eachJigenOptionIndex++;
                    }
                }
            }
        }
    }

    /**
     * 負荷制御履歴の合計値をセット
     *
     * @param ctrlLoadList
     * @param i
     * @param j
     * @param targetName
     * @param defineNameMap
     */
    private void setLoadCtrlSumValue(List<AnalysisEmsControlHistControlLoadResultData> ctrlLoadList, int i, int j,
            String targetName, Map<String, String> defineNameMap) {
        outputDataMap.get(defineNameMap.get(targetName)).add(ctrlLoadList.get(i).getTimeList().get(j).getControlTime());
    }

    /**
     * イベント制御履歴の合計値をセット
     *
     * @param ctrlLoadList
     * @param i
     * @param j
     * @param targetName
     * @param defineNameMap
     */
    private void setEventCtrlSumValue(List<AnalysisEmsControlHistControlLoadResultData> ctrlLoadList, int i, int j,
            String targetName, Map<String, String> defineNameMap) {
        outputDataMap.get(defineNameMap.get(targetName)).add(ctrlLoadList.get(i).getTimeList().get(j).getControlTime());
    }

    /**
     * 負荷制御履歴の共通項目のセル書式を設定
     *
     * @param i
     * @param listSize
     * @param defineNameMap
     * @param sumFirstBorder
     * @param sumCenterBorder
     * @param sumEndBorder
     */
    private void setLoadCtrlInfoSumCommonOption(int i, int listSize, Map<String, String> defineNameMap,
            String[] sumFirstBorder, String[] sumCenterBorder, String[] sumEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumFirstBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumFirstBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumFirstBorder[2]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumEndBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumEndBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumEndBorder[2]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumCenterBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumCenterBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumCenterBorder[2]);
        }
    }

    /**
     * イベント制御履歴の合計の共通項目のセル書式を設定
     *
     * @param i
     * @param listSize
     * @param defineNameMap
     * @param sumFirstBorder
     * @param sumCenterBorder
     * @param sumEndBorder
     */
    private void setEventCtrlInfoSumCommonOption(int i, int listSize, Map<String, String> defineNameMap,
            String[] sumFirstBorder, String[] sumCenterBorder, String[] sumEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumFirstBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumFirstBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumFirstBorder[2]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumEndBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumEndBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumEndBorder[2]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_TARGET_DATE.getName()))
                    .add(sumCenterBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_NAME.getName()))
                    .add(sumCenterBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.SUM_LOAD_UNIT.getName()))
                    .add(sumCenterBorder[2]);
        }
    }

    /**
     * 負荷制御履歴の合計の時限のセル書式を設定
     *
     * @param i
     * @param sumJigenOptionIndex
     * @param listSize
     * @param defineNameMap
     * @param targetName
     * @param sumFirstBorder
     * @param sumCenterBorder
     * @param sumEndBorder
     */
    private void setLoadCtrlInfoSumJigenOption(int i, int sumJigenOptionIndex, int listSize,
            Map<String, String> defineNameMap, String targetName, String[] sumFirstBorder, String[] sumCenterBorder,
            String[] sumEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumFirstBorder[sumJigenOptionIndex]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumEndBorder[sumJigenOptionIndex]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumCenterBorder[sumJigenOptionIndex]);
        }
    }

    /**
     * イベント制御履歴の合計の時限のセル書式を設定
     *
     * @param i
     * @param sumJigenOptionIndex
     * @param listSize
     * @param defineNameMap
     * @param targetName
     * @param sumFirstBorder
     * @param sumCenterBorder
     * @param sumEndBorder
     */
    private void setEventCtrlInfoSumJigenOption(int i, int sumJigenOptionIndex, int listSize,
            Map<String, String> defineNameMap, String targetName, String[] sumFirstBorder, String[] sumCenterBorder,
            String[] sumEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumFirstBorder[sumJigenOptionIndex]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumEndBorder[sumJigenOptionIndex]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(targetName)).add(sumCenterBorder[sumJigenOptionIndex]);
        }
    }

    /**
     * 負荷制御履歴の建物の共通項目のセル書式を設定
     *
     * @param i
     * @param listSize
     * @param defineNameMap
     * @param eachFirstBorder
     * @param eachCenterBorder
     * @param eachEndBorder
     */
    private void setLoadCtrlInfoEachCommonOption(int i, int listSize, Map<String, String> defineNameMap,
            String[] eachFirstBorder, String[] eachCenterBorder, String[] eachEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachFirstBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachFirstBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachFirstBorder[2]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachEndBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachEndBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachEndBorder[2]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachCenterBorder[0]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachCenterBorder[1]);
            optionSettingMap.get(defineNameMap.get(LOAD_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachCenterBorder[2]);
        }
    }

    /**
     * イベント制御履歴の建物の共通項目のセル書式を設定
     *
     * @param i
     * @param listSize
     * @param defineNameMap
     * @param eachFirstBorder
     * @param eachCenterBorder
     * @param eachEndBorder
     */
    private void setEventCtrlInfoEachCommonOption(int i, int listSize, Map<String, String> defineNameMap,
            String[] eachFirstBorder, String[] eachCenterBorder, String[] eachEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachFirstBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachFirstBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachFirstBorder[2]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachEndBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachEndBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachEndBorder[2]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_TARGET_DATE.getName()))
                    .add(eachCenterBorder[0]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_NAME.getName()))
                    .add(eachCenterBorder[1]);
            optionSettingMap.get(defineNameMap.get(EVENT_CTRL_HISTORY_DEFINE_NAME.EACH_LOAD_UNIT.getName()))
                    .add(eachCenterBorder[2]);
        }
    }

    /**
     * 負荷制御履歴の建物の時限のセル書式を設定
     *
     * @param i
     * @param eachJigenOptionIndex
     * @param listSize
     * @param defineNameMap
     * @param targetName
     * @param eachFirstBorder
     * @param eachCenterBorder
     * @param eachEndBorder
     */
    private void setLoadCtrlInfoEachJigenOption(int i, int eachJigenOptionIndex, int listSize,
            Map<String, String> defineNameMap, String targetName, String[] eachFirstBorder, String[] eachCenterBorder,
            String[] eachEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachFirstBorder[eachJigenOptionIndex]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachEndBorder[eachJigenOptionIndex]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachCenterBorder[eachJigenOptionIndex]);
        }
    }

    /**
     * イベント制御履歴の建物の時限のセル書式を設定
     *
     * @param i
     * @param eachJigenOptionIndex
     * @param listSize
     * @param defineNameMap
     * @param targetName
     * @param eachFirstBorder
     * @param eachCenterBorder
     * @param eachEndBorder
     */
    private void setEventCtrlInfoEachJigenOption(int i, int eachJigenOptionIndex, int listSize,
            Map<String, String> defineNameMap, String targetName, String[] eachFirstBorder, String[] eachCenterBorder,
            String[] eachEndBorder) {

        if (i == 0) {
            // 先頭
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachFirstBorder[eachJigenOptionIndex]);
        } else if (i == listSize - 1) {
            // 最終
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachEndBorder[eachJigenOptionIndex]);
        } else {
            // 中間
            optionSettingMap.get(defineNameMap.get(targetName)).add(eachCenterBorder[eachJigenOptionIndex]);
        }
    }

    /**
     * 負荷制御履歴の負荷毎の値をセット
     *
     * @param periodList
     * @param i
     * @param j
     * @param k
     * @param targetName
     * @param defineNameMap
     */
    private void setLoadCtrlEachValue(List<AnalysisEmsControlHistPeriodResultData> periodList, int i, int j, int k,
            String targetName, Map<String, String> defineNameMap) {
        outputDataMap.get(defineNameMap.get(targetName))
                .add(periodList.get(i).getControlLoadList().get(j).getTimeList().get(k).getControlTime());
    }

    /**
     * イベント制御履歴の負荷毎の値をセット
     *
     * @param periodList
     * @param i
     * @param j
     * @param k
     * @param targetName
     * @param defineNameMap
     */
    private void setEventCtrlEachValue(List<AnalysisEmsControlHistPeriodResultData> periodList, int i, int j, int k,
            String targetName, Map<String, String> defineNameMap) {
        outputDataMap.get(defineNameMap.get(targetName))
                .add(periodList.get(i).getControlLoadList().get(j).getTimeList().get(k).getControlTime());
    }

    /**
     * 現在操作中企業のすべての建物リストを取得する
     * @return
     */

    private List<AnalysisAllBuildingListResultData> getAllTargetBuildingList() {
        AnalysisAllBuildingListParameter analysisAllBuildingListParameter = new AnalysisAllBuildingListParameter();
        settingParameter(analysisAllBuildingListParameter);

        try {
            AnalysisAllBuildingListResult result = analysisAllBuildingListDao.query(analysisAllBuildingListParameter);

            if (result != null) {
                return result.getTargetBuildingList();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 指定された建物IDと、所属するテナントの建物リストを取得する
     * @param buildingId
     * @param buildingFilter
     * @return
     */
    private List<AnalysisAllBuildingListResultData> getTargetIdBuildingList(String buildingId, Boolean buildingFilter) {
        //対象建物取得
        AnalysisBuildingIdListParameter analysisBuildingIdListParameter = new AnalysisBuildingIdListParameter();
        settingParameter(analysisBuildingIdListParameter);
        analysisBuildingIdListParameter.setBuildingId(Long.valueOf(buildingId));
        analysisBuildingIdListParameter.setBelongTenantFlg(buildingFilter);
        try {
            AnalysisBuildingIdListResult result = analysisBuildingIdListDao.query(analysisBuildingIdListParameter);

            if (result != null) {
                return result.getTargetBuildingList();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 現在操作中企業の指定されたグループの建物リストを取得する
     * @return
     */

    private List<AnalysisAllBuildingListResultData> getGroupTargetBuildingList(Long parentGroupId, Long childGroupId) {
        AnalysisGroupBuildingListParameter analysisGroupBuildingListParameter = new AnalysisGroupBuildingListParameter();
        settingParameter(analysisGroupBuildingListParameter);
        analysisGroupBuildingListParameter.setParentGroupId(parentGroupId);
        analysisGroupBuildingListParameter.setChildGroupId(childGroupId);
        try {
            AnalysisGroupBuildingListResult result = analysisGroupBuildingListDao.query(analysisGroupBuildingListParameter);

            if (result != null) {
                return result.getTargetBuildingList();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 企業系統の系統グループIDを取得する
     * @return
     */
    private Long getCorpLineGroupId() {
        // 系統グループ取得処理
        LineGroupSearchParameter lineGroupParameter = new LineGroupSearchParameter();
        settingParameter(lineGroupParameter);
        //企業系統を指定
        lineGroupParameter.setLineGroupType("0");

        try {
            LineGroupSearchResult result = lineGroupSearchDao.query(lineGroupParameter);

            if (result != null && result.getDetailList().size() == 1) {
                return result.getDetailList().get(0).getLineGroupId();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 引数の系統グループIDの系統情報を取得
     * @return
     */
    private List<LineListDetailResultData> getCorpLineList(Long corpLineGroupId) {
        // 企業系統の系統情報取得処理
        LineListParameter lineListParameter = new LineListParameter();
        settingParameter(lineListParameter);
        lineListParameter.setLineGroupId(corpLineGroupId);
        lineListParameter.setLineEnableFlg(1);

        try {
            LineListResult result = lineListDao.query(lineListParameter);

            if (result != null) {
                //系統No順でソートする
                result.getDetailList()
                        .sort(Comparator.comparing((LineListDetailResultData o) -> o.getLineNo().length())
                                .thenComparing((LineListDetailResultData o) -> o.getLineNo()));
                return result.getDetailList();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 指定された系統グループの情報を取得する
     * @param lineGroupId
     * @return
     */
    private LineGroupSearchDetailResultData getLineGroupInfo(Long lineGroupId) {
        // 系統グループ取得処理
        LineGroupSearchParameter lineGroupParameter = new LineGroupSearchParameter();
        settingParameter(lineGroupParameter);
        try {
            LineGroupSearchResult result = lineGroupSearchDao.query(lineGroupParameter);

            if (result != null && result.getDetailList() != null && !result.getDetailList().isEmpty()) {
                for (LineGroupSearchDetailResultData detail : result.getDetailList()) {
                    if (lineGroupId.equals(detail.getLineGroupId())) {
                        return detail;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 企業集計日を取得する
     * @return 企業集計日
     */
    private String getCorpSumDate() {
        //企業デマンド情報取得
        CorpDemandSelectParameter corpDemandSelectParameter = new CorpDemandSelectParameter();
        settingParameter(corpDemandSelectParameter);
        try {
            CorpDemandSelectResult result = corpDemandSelectDao.query(corpDemandSelectParameter);

            if (result != null) {
                //企業集計日を返す
                return result.getSumDate();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 親グループマスタ情報を取得する
     *
     * @param defaultBoxFlg
     * @param dispMessage
     * @param loginUserId
     * @param loginUserCorpId
     * @param corpId
     * @return
     */
    private LinkedHashMap<String, String> getParentGroup() {
        LinkedHashMap<String, String> parentGroupMap = new LinkedHashMap<>();

        // 親グループ一覧取得処理
        ParentGroupListParameter parentParameter = new ParentGroupListParameter();
        settingParameter(parentParameter);

        try {
            ParentGroupListResult result = parentGroupListDao.query(parentParameter);

            if (result != null && result.getDetailList() != null && !result.getDetailList().isEmpty()) {
                for (ParentGroupListDetailResultData resultData : result.getDetailList()) {
                    parentGroupMap.put(resultData.getParentGroupName(), String.valueOf(resultData.getParentGroupId()));
                }
                return parentGroupMap;
            }
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
        return new LinkedHashMap<>();
    }

    /**
     * 子グループマスタ情報を取得する
     *
     * @param defaultBoxFlg
     * @param dispMessage
     * @param loginUserId
     * @param loginUserCorpId
     * @param corpId
     * @param parentGroupId
     * @return
     */
    private LinkedHashMap<String, String> getChildGroup(long parentGroupId) {
        LinkedHashMap<String, String> childGroupMap = new LinkedHashMap<>();
        // 指定無しケースもあるので初期値として登録しておく
        childGroupMap.put(OsolConstants.SELECT_BOX_KEY_SHITEI_NASHI,
                OsolConstants.DEFAULT_SELECT_DEFAULT_VALUE);

        // 子グループ一覧取得処理
        ChildGroupListParameter childParameter = new ChildGroupListParameter();
        settingParameter(childParameter);
        childParameter.setParentGroupId(parentGroupId);

        try {
            ChildGroupListResult result = childGroupListDao.query(childParameter);

            if (result != null && result.getDetailList() != null && !result.getDetailList().isEmpty()) {
                for (ChildGroupListDetailResultData resultData : result.getDetailList()) {
                    childGroupMap.put(resultData.getChildGroupName(), String.valueOf(resultData.getChildGroupId()));
                }
                return childGroupMap;
            }
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
        return new LinkedHashMap<>();
    }

    /**
     * 親グループマスタから親グループ名を取得する
     * @param parentGroupId
     * @return
     */
    private String getParentGroupName(String parentGroupId) {
        LinkedHashMap<String, String> parentGroupMap = getParentGroup();
        String parentGroupName = "";
        for (Entry<String, String> parentName : parentGroupMap.entrySet()) {
            if (parentName.getValue().equals(parentGroupId)) {
                parentGroupName = parentName.getKey();
            }
        }
        return parentGroupName;
    }

    /**
     * 子グループマスタから子グループ名を取得する
     * @param parentGroupId
     * @return
     */
    private String getChildGroupName(String parentGroupId, String childGroupId) {

        LinkedHashMap<String, String> childGroupMap = getChildGroup(Long.valueOf(parentGroupId));
        String childGroupName = "";
        for (Entry<String, String> childName : childGroupMap.entrySet()) {
            if (childName.getValue().equals(childGroupId)) {
                childGroupName = childName.getKey();
            }
        }
        return childGroupName;
    }

    /**
     * 汎用区マスタから取得
     * @param groupCode
     * @return
     */
    private LinkedHashMap<String, String> genericTypeMap(ApiGenericTypeConstants.GROUP_CODE groupCode) {
        List<GenericTypeListDetailResultData> genericTypeList = genericTypeUtility.getKbnList(groupCode);
        LinkedHashMap<String, String> genericTypeMap = new LinkedHashMap<>();
        for (GenericTypeListDetailResultData genericType : genericTypeList) {
            genericTypeMap.put(genericType.getKbnName(), genericType.getKbnCode());
        }
        return genericTypeMap;
    }

    /**
     * 表示系統（全体用）を取得する
     * @param lineGroupId
     * @param temperatureFlg ONの場合末尾に外気温を追加
     * @return
     */
    public LinkedHashMap<String, String> getLineList(long lineGroupId, Integer temperatureFlg) {
        List<LineListDetailResultData> lineList;
        LinkedHashMap<String, String> lineMap = new LinkedHashMap<>();

        // 系統取得処理
        LineListParameter lineParameter = new LineListParameter();
        settingParameter(lineParameter);
        lineParameter.setLineGroupId(lineGroupId);
        lineParameter.setLineEnableFlg(OsolConstants.FLG_ON);

        try {
            LineListResult result = lineListDao.query(lineParameter);

            if (result != null && result.getDetailList() != null && !result.getDetailList().isEmpty()) {
                lineList = result.getDetailList();

                // ソート
                lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                    int lineNo1 = "ALL".equals(rs1.getLineNo()) ? 0
                            : "ETC".equals(rs1.getLineNo()) ? Integer.MAX_VALUE
                                    : Integer.parseInt(rs1.getLineNo());
                    int lineNo2 = "ALL".equals(rs2.getLineNo()) ? 0
                            : "ETC".equals(rs2.getLineNo()) ? Integer.MAX_VALUE
                                    : Integer.parseInt(rs2.getLineNo());
                    return lineNo1 - lineNo2;
                });
                for (LineListDetailResultData line : lineList) {
                    lineMap.put(line.getLineName(), line.getLineNo());
                }

                // 末尾に外気温追加
                if (OsolConstants.FLG_ON.equals(temperatureFlg)) {
                    lineMap.put(ApiCodeValueConstants.ANALYSIS_LINE_TYPE.TEMP.getName(),
                            ApiCodeValueConstants.ANALYSIS_LINE_TYPE.TEMP.getVal());
                }

                return lineMap;
            }
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
        return new LinkedHashMap<>();
    }

    /**
     * EMS実績 CSV出力値項目値取得
     * @param csvOutputItemValue
     * @return
     */
    private String getCsvOutputItemName(String csvOutputItemValue) {

        String outputItemName = "";
        // 出力値
        for (AnalysisEmsUtility.OutputItemValue outputItemValue : AnalysisEmsUtility.OutputItemValue.values()) {
            if (outputItemValue.getCd().equals(csvOutputItemValue)) {
                outputItemName = outputItemValue.getText();
            }
        }
        return outputItemName;
    }

    /**
     * 系統名を取得する
     * @param lineGroupId
     * @param lineNo
     * @return
     */
    private String getLineName(long lineGroupId, String lineNo) {
        LinkedHashMap<String, String> lineList = getLineList(lineGroupId, OsolConstants.FLG_ON);
        String lineName = "";
        if (lineList != null && lineList.size() > 0) {
            for (Entry<String, String> line : lineList.entrySet()) {
                if (line.getValue().equals(lineNo)) {
                    lineName = line.getKey();
                }
            }
        }
        return lineName;
    }

    /**
     * 出力対象かどうかチェックする
     *
     * @param pointValueList
     * @param index
     * @return true：出力対象、false：出力対象外
     */
    private boolean chkOutputCsv(List<CommonDemandDayReportPointListResult> pointValueList, int index) {

        if (index == pointValueList.size() - 1) {
            return true;
        }

        if (!(pointValueList.get(index).getMeasurementDate()
                .compareTo(pointValueList.get(index + 1).getMeasurementDate()) == 0
                && pointValueList.get(index).getJigenNo().compareTo(pointValueList.get(index + 1).getJigenNo()) == 0)) {
            return true;
        }
        return false;
    }

    /**
     * ファイルを保存するフォルダを作成
     * @param folderName
     * @param outputDate
     * @param isCreateBuildingList
     * @return
     */
    private String[] createFolder(String folderName, Date outputDate, boolean isCreateBuildingList) {

        // 出力日を文字列化
        String outputDateFormat = DateUtility.changeDateFormat(outputDate, DateUtility.DATE_FORMAT_YYYYMMDD);

        // ルートフォルダパス
        String excelOutputRootPath = this.getExcelOutputDir().concat(File.separator).concat(outputDateFormat);
//        eventLogger.debug(this.getClass().toString() + ": ■ excelOutputRootPath:" + excelOutputRootPath);
        File rootNewdir = new File(excelOutputRootPath);

        // フォルダーが存在しない場合作成
        if (!rootNewdir.exists()) {
            rootNewdir.mkdirs();
        }

        // ユーザーIDフォルダパス(同時出力時のファイル名かぶり回避)
        String excelOutputUserIdFolderPath = excelOutputRootPath.concat(File.separator).concat(person.getUserId().toString());
//        eventLogger.debug(this.getClass().toString() + ": ■ excelOutputRootPath:" + excelOutputUserIdFolderPath);
        File userIdNewdir = new File(excelOutputUserIdFolderPath);

        // フォルダーが存在しない場合作成
        if (!userIdNewdir.exists()) {
            userIdNewdir.mkdir();
        }

        // 使用禁止文字を削除し、建物一覧情報フォルダ名作成
        String buildingFolderName = String.format(
                folderAndFileName.getFolderAndFileNameFormat(folderName, selectedCorp.getCorpName()),
                DateUtility.changeDateFormat(outputDate, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));

        // 建物一覧情報用のフォルダパス
        String emsRecordFolderPath = excelOutputUserIdFolderPath.concat(File.separator).concat(buildingFolderName);
//        eventLogger.debug(this.getClass().toString() + ": ■ emsRecordFolderPath:" + emsRecordFolderPath);
        File emsRecordDir = new File(emsRecordFolderPath);

        // フォルダーが存在しない場合作成
        if (!emsRecordDir.exists()) {
            emsRecordDir.mkdir();
        }

        String emsBuildingListFolderPath = null;

        if (isCreateBuildingList) {
            // 建物情報用のフォルダパス
            emsBuildingListFolderPath = emsRecordFolderPath.concat(File.separator).concat(ApiAnalysisEmsConstants.CREATE_BUILDING_FOLDER_NAME);
//            eventLogger.debug(this.getClass().toString() + ": ■ facilityLedgerFolderPath:" + emsBuildingListFolderPath);
            File emsBuildingListDir = new File(emsBuildingListFolderPath);

            // フォルダーが存在しない場合作成
            if (!emsBuildingListDir.exists()) {
                emsBuildingListDir.mkdir();
            }
        }

        // 削除用に保存先フォルダを格納
        tempFolderPath = emsRecordFolderPath;

        // EMS実績フォルダパス、EMS実績建物リストフォルダパス
        return new String[] { emsRecordFolderPath, emsBuildingListFolderPath };
    }

    /**
     * zipファイルを作成する
     * zipファイル作成後、元のフォルダは削除
     *
     * @param zipCompressFolder 圧縮するフォルダ
     * @return 圧縮後の出力ファイルパス
     */
    private String createZipFile(String zipCompressFolder) {

        // 圧縮後の出力パス + ファイル名
        String zipFilePath = zipCompressFolder + ApiAnalysisEmsConstants.ZIP_EXTENSION;

        OsoApiFileZipArchive.compressDirectory(zipCompressFolder, zipFilePath);

        // zip圧縮元フォルダを削除
        deleteFileDirectory(new File(zipCompressFolder));

        return zipFilePath;
    }

    /**
     * 保存ファイル名を作成。（zip）
     *
     * @param プロパティ名
     * @param 日付
     * @return 使用禁止文字を削除した保存ファイル名
     */
    private String createSaveFileNameForZip(String propName, Date today) {

        return String.format(folderAndFileName.getFolderAndFileNameFormat(propName, selectedCorp.getCorpName()),
                DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)) + ApiAnalysisEmsConstants.ZIP_EXTENSION;
    }

    /**
     * 保存ファイル名を作成。
     *
     * @param プロパティ名
     * @param 日付
     * @return 使用禁止文字を削除した保存ファイル名
     */
    private String createExcelFileName(String propName, Date today) {

        return String.format(folderAndFileName.getFolderAndFileNameFormat(propName, selectedCorp.getCorpName()),
                DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
    }

    /**
     * 保存ファイル名を作成。
     *
     * @param プロパティ名
     * @param 添字
     * @param 建物名
     * @param 日付
     * @return 使用禁止文字を削除した保存ファイル名
     */
    private String createExcelFileName(String propName, int index, String buildingName, Date today) {

        String replaceBuildingName = ExcelUtility.getTrueFileName(buildingName, true);

        return String.format(
                folderAndFileName.getFolderAndFileNameFormat(propName,
                        new String[] { String.valueOf(index), replaceBuildingName }),
                DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS));
    }

    /**
     * 保存ファイル名を作成。（csv）
     *
     * @param プロパティ名
     * @param 日付
     * @return 使用禁止文字を削除した保存ファイル名
     */
    private String createCsvFileName(String propName, Date today, String smId) {

        if (CheckUtility.isNullOrEmpty(smId)) {
            // 系統別計測値CSV出力
            return String.format(folderAndFileName.getFolderAndFileNameFormat(propName, selectedCorp.getCorpName()),
                    DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)) + ApiAnalysisEmsConstants.CSV_EXTENSION;
        } else {
            // ポイント別計測値CSV出力
            return String.format(
                    folderAndFileName.getFolderAndFileNameFormat(propName, new String[] { selectedCorp.getCorpName(), smId }),
                    DateUtility.changeDateFormat(today, DateUtility.DATE_FORMAT_YYYYMMDDHHMMSS)) + ApiAnalysisEmsConstants.CSV_EXTENSION;
        }
    }

    /**
     * ファイルを作成
     *
     * @throws IOException
     * @return
     */
    private File createLineCsvFile(String tempDir) throws IOException {

        // ファイル作成部分
        StringBuilder sb = new StringBuilder(tempDir);
        String fileName = createCsvFileName("TotalizationEmsLineListFolder", new Date(System.currentTimeMillis()), "");
        sb.append(File.separator);

        File folder = new File(sb.toString());
        folder.mkdirs();
        sb.append(fileName);
        File file = new File(sb.toString());
        file.createNewFile();

        saveFilename = file.getName();

        return file;
    }

    /**
     * ファイルを作成
     *
     * @throws IOException
     * @return
     */
    private File createPointCsvFile(String tempDir, Long smId) throws IOException {

        // ファイル作成部分
        StringBuilder sb = new StringBuilder(tempDir);
        String fileName = createCsvFileName("TotalizationEmsPointCsvFolder", new Date(System.currentTimeMillis()), String.valueOf(smId));
        sb.append(File.separator);

        File folder = new File(sb.toString());
        folder.mkdirs();
        sb.append(fileName);
        File file = new File(sb.toString());
        file.createNewFile();

        saveFilename = file.getName();

        return file;
    }

    /**
     * csvファイルへ書き込み
     *
     * @param targetFile
     * @param csvRows
     * @exception IOException
     * @return
     */
    private String writeCsvFile(File targetFile, List<String> csvRowList) throws IOException {

        // ファイル書き込み
        //BOM 追加
        try (FileOutputStream os = new FileOutputStream(targetFile)) {
            os.write(0xef);
            os.write(0xbb);
            os.write(0xbf);
        }

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(targetFile, true), "UTF-8"))) {
            for (String row : csvRowList) {
                writer.println(row);
            }
        }

        return targetFile.getPath();
    }

    /**
     * S3アップロード
     * @param outputFileDir
     * @param outputFileName
     * @return
     */
    private boolean executeS3Upload(String outputFileDir, String outputFileName) {
        File outputFile = null;

        // nullまたは空白
        if (CheckUtility.isNullOrEmpty(outputFileDir)
                || CheckUtility.isNullOrEmpty(outputFileName)) {
            return false;
        }

        if (!outputFileDir.endsWith(ApiAnalysisEmsConstants.ZIP_EXTENSION)) {
            outputFile = new File(outputFileDir.concat(File.separator).concat(outputFileName));
        } else {
            outputFile = new File(outputFileDir);
        }

        if (!outputFile.exists()) {
            return false;
        }

        // S3へアップロード
        analysisEmsFileUploadUtility.S3fileUpload(outputFile.getPath());

        // S3アップロード後に元のzipファイルは削除
        deleteFileDirectory(outputFile);

        return true;
    }

    /**
     * エクセルテンプレートディレクトリ
     * @return
     */
    private String getExcelTemplateDir() {

        return osolConfigs.getConfig(OsolConstants.EXCEL_TEMPLATE_DIR);
    }

    /**
     * エクセル出力先ディレクトリ
     * @return
     */
    private String getExcelOutputDir() {
//        return osolConfigs.getConfig(OsolConstants.ANALYSIS_EMS_RESERVATION_UPLOAD_DIR);

        if (SystemUtils.IS_OS_WINDOWS) {
            return osolConfigs.getConfig(OsolConstants.ANALYSIS_EMS_RESERVATION_UPLOAD_DIR_WIN);
        } else {
            return osolConfigs.getConfig(OsolConstants.ANALYSIS_EMS_RESERVATION_UPLOAD_DIR);
        }

//        return osolConfigs.getConfig(OsolConstants.EXCEL_OUTPUT_DIR);
    }

    /**
     * 処理結果0件か判別フラグの初期化
     */
    private void initNoResultFlg() {
        noResultFlg = false;
    }

    private void noResultFlgOn() {
        noResultFlg = true;
    }

    /**
     * 集計分析予約情報を更新
     * @param target
     * @return true:更新成功、false:更新失敗
     */
    private AnalysisEmsReservationInfoResultData updateTAggregateReservationInfo(AnalysisEmsReservationInfoResultData target) {

        // バッチからの使用を想定しているのでユーザーID[0]固定
        target.setUpdateUserId(Long.valueOf(0));
        target.setUpdateDate(getCurrentDateTime());

        // 更新実行
        AnalysisEmsReservationInfoResultData result = dao.merge(target);
        return result;
    }

    /**
     * ログイン情報系を設定する
     * @param param
     */
    private void settingParameter (OsolApiParameter param) {
        param.setOperationCorpId(this.selectedCorpId);
        param.setLoginCorpId(this.selectedPersonCorpId);
        param.setLoginPersonId(this.selectedPersonId);
    }

    private List<Date> createTermCalendar(Date from, Date to) {
        List<Date> dateList = new ArrayList<>();
        Date current = from;
        do {
            dateList.add(current);
            current = DateUtility.plusDay(current, 1);
        } while (current.compareTo(to) <= 0);

        return dateList;
    }

    private String changeJigenNoToHHMM(BigDecimal jigenNo) {

        if (jigenNo == null) {
            return null;
        }

        jigenNo = jigenNo.setScale(0);
        int jigenInt = jigenNo.intValue();
        int minute = jigenInt * 30;

        String tempDateString = DateUtility.changeDateFormat(new Date(), DateUtility.DATE_FORMAT_YYYYMMDD);
        Date tempDate = DateUtility.conversionDate(tempDateString, DateUtility.DATE_FORMAT_YYYYMMDD);
        Date retDate = DateUtility.plusMinute(tempDate, minute);

        return DateUtility.changeDateFormat(retDate, DateUtility.DATE_FORMAT_HHMM_COLON);
    }

    /**
     * Excel出力用全MAPを初期化する
     */
    private void initMap() {
        optionSettingMap = new HashMap<>();
        outputDataMap = new HashMap<>();
        preSettingMap = new HashMap<>();
        emsNameList = new ArrayList<>();
        orgCloneSheetNameMap = new HashMap<>();
        templateMap = new TreeMap<>();
    }

    /**
     * Excel出力用全MAPをクリアする
     */
    private void clearMap() {
        optionSettingMap = null;
        outputDataMap = null;
        preSettingMap = null;
        emsNameList = null;
        orgCloneSheetNameMap = null;
        templateMap = null;
    }

    /**
     * 系統別 全体サマリー 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setGroupLineSummaryAllInfo(String childGroupName, LineListDetailResultData line,
            List<AnalysisEmsGroupLineSummaryResultData> summaryDataList, Boolean hasGroupNext, Boolean isFirst,
            Boolean hasLineNext) {

        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName(), childGroupName,
                4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName(), line.getLineName(),
                4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName(), "", 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName(), "", 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName(), line.getLineUnit(), 4);
        for (AGGREGATE_TYPE type : AGGREGATE_TYPE.values()) {
            outputDataMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(type.getLabel());
        }

        //罫線設定
        String[] childFirstBorder = createGroupSummaryChildFirstBorder();
        String[] childLineFirstBorder = createGroupSummaryChildLineFirstBorder();
        String[] childCenterBorder = createGroupSummaryAllCenterBorder();
        String[] childLineLastBorder = createGroupSummaryChildLineLastBorder();
        String[] childLastBorder = createGroupSummaryChildLastBorder();

        if (isFirst) {
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childFirstBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childFirstBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childFirstBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childFirstBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childFirstBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childFirstBorder[5]);
        } else {
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childLineFirstBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childLineFirstBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childLineFirstBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childLineFirstBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childLineFirstBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childLineFirstBorder[5]);
        }

        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childCenterBorder[0]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childCenterBorder[1]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childCenterBorder[2]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childCenterBorder[3]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childCenterBorder[4]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childCenterBorder[5]);

        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childCenterBorder[0]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childCenterBorder[1]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childCenterBorder[2]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childCenterBorder[3]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childCenterBorder[4]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childCenterBorder[5]);

        if (hasGroupNext || hasLineNext) {
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childLineLastBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childLineLastBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childLineLastBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childLineLastBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childLineLastBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childLineLastBorder[5]);
        } else {
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(childLastBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(childLastBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(childLastBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(childLastBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(childLastBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(childLastBorder[5]);
        }

        //時限とデータのマップ作成

        Map<Integer, AnalysisEmsGroupLineSummaryResultData> jigenDataMap = new HashMap<>();
        for (AnalysisEmsGroupLineSummaryResultData jigenData : summaryDataList) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData);
        }

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 6;

        for (String jigenName : getLineGroupSummaryAllDefineNameList()) {

            if (jigenDataMap.containsKey(jigenIndex)) {
                if (line.getLineNo().equals("")) {
                    outputDataMap.get(jigenName).add("'-");
                } else {
                    outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getSummary());
                }
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getAverage());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMax());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMin());
            } else {
                setSameColumnSameData(outputDataMap, jigenName, "", 4);
            }

            //罫線設定
            if (isFirst) {
                optionSettingMap.get(jigenName).add(childFirstBorder[borderIndex]);
            } else {
                optionSettingMap.get(jigenName).add(childLineFirstBorder[borderIndex]);
            }

            optionSettingMap.get(jigenName).add(childCenterBorder[borderIndex]);
            optionSettingMap.get(jigenName).add(childCenterBorder[borderIndex]);
            if (hasGroupNext) {
                optionSettingMap.get(jigenName).add(childLineLastBorder[borderIndex]);
            } else {
                optionSettingMap.get(jigenName).add(childLastBorder[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 系統別 建物別サマリー 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setGroupLineSummaryBillInfo(AnalysisAllBuildingListResultData targetBuilding,
            LineListDetailResultData line, List<AnalysisEmsAllLineSummaryResultData> summaryDataList,
            Boolean hasNextBuilding, Boolean isBuildingFirst, Boolean hasNextLine, Boolean isOutputBuilding,
            String linkPath) {

        if (isOutputBuilding) {
            setSameColumnSameData(optionSettingMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_LINK.getName(), linkPath,
                    4);
        }

        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName(),
                targetBuilding.getChildGroupName(), 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName(),
                targetBuilding.getBuildingNo(), 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName(),
                targetBuilding.getBuildingName(), 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName(), line.getLineName(), 4);
        setSameColumnSameData(outputDataMap, LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName(), line.getLineUnit(), 4);
        for (AGGREGATE_TYPE type : AGGREGATE_TYPE.values()) {
            outputDataMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(type.getLabel());
        }

        //罫線設定
        String[] buildingFirstBorder = createGroupSummaryBillFirstBorder();
        String[] lineFirstBorder = createGroupSummaryBillLineFirstBorder();
        String[] lineCenterBorder = createGroupSummaryBillLineCenterBorder();
        String[] lineLastBorder = createGroupSummaryBillLineLastBorder();
        String[] buildingLastBorder = createGroupSummaryBillLastBorder();

        if (isBuildingFirst) {
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(buildingFirstBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(buildingFirstBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(buildingFirstBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(buildingFirstBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(buildingFirstBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(buildingFirstBorder[5]);
        } else {
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(lineFirstBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineFirstBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineFirstBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineFirstBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineFirstBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineFirstBorder[5]);
        }

        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(lineCenterBorder[0]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineCenterBorder[1]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineCenterBorder[2]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineCenterBorder[3]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineCenterBorder[4]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineCenterBorder[5]);

        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(lineCenterBorder[0]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineCenterBorder[1]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineCenterBorder[2]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineCenterBorder[3]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineCenterBorder[4]);
        optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineCenterBorder[5]);

        if (hasNextBuilding || hasNextLine) {
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(lineLastBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineLastBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineLastBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineLastBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineLastBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineLastBorder[5]);
        } else {
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.CHILD_GROUP_NAME.getName()).add(buildingLastBorder[0]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(buildingLastBorder[1]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(buildingLastBorder[2]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(buildingLastBorder[3]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(buildingLastBorder[4]);
            optionSettingMap.get(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(buildingLastBorder[5]);
        }

        //時限とデータのマップ作成
        Map<Integer, AnalysisEmsAllLineSummaryResultData> jigenDataMap = new HashMap<>();
        for (AnalysisEmsAllLineSummaryResultData jigenData : summaryDataList) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData);
        }

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 6;

        for (String jigenName : getLineGroupSummaryBillDefineNameList()) {

            if (jigenDataMap.containsKey(jigenIndex)) {
                if (line.getLineNo().equals("")) {
                    outputDataMap.get(jigenName).add("-");
                } else {
                    outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getSummary());
                }
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getAverage());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMax());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMin());
            } else {
                setSameColumnSameData(outputDataMap, jigenName, "", 4);
            }

            //罫線設定
            if (isBuildingFirst) {
                optionSettingMap.get(jigenName).add(buildingFirstBorder[borderIndex]);
            } else {
                optionSettingMap.get(jigenName).add(lineFirstBorder[borderIndex]);
            }

            optionSettingMap.get(jigenName).add(lineCenterBorder[borderIndex]);

            optionSettingMap.get(jigenName).add(lineCenterBorder[borderIndex]);

            if (hasNextBuilding) {
                optionSettingMap.get(jigenName).add(lineLastBorder[borderIndex]);
            } else {
                optionSettingMap.get(jigenName).add(buildingLastBorder[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 系統別 全体サマリー 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setBuildingLineSummaryAllInfo(LineListDetailResultData line,List<AnalysisEmsAllLineSummaryResultData> summaryDataList) {

        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName(), line.getLineName(),
                4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName(), "", 4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName(), "", 4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName(), line.getLineUnit(),
                4);
        for (AGGREGATE_TYPE type : AGGREGATE_TYPE.values()) {
            outputDataMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(type.getLabel());
        }

        //罫線設定
        String[] firstBorder = createBuildingSummaryAllFirstBorder();
        String[] centerBorder = createBuildingSummaryAllCenterBorder();
        String[] lastBorder = createBuildingSummaryAllLastBorder();

        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(firstBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(firstBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(firstBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(firstBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(firstBorder[4]);

        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(centerBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(centerBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(centerBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(centerBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(centerBorder[4]);

        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(centerBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(centerBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(centerBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(centerBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(centerBorder[4]);

        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_1.getName()).add(lastBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_2.getName()).add(lastBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_NAME_3.getName()).add(lastBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.LINE_UNIT.getName()).add(lastBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lastBorder[4]);

        //時限とデータのマップ作成
        Map<Integer, AnalysisEmsAllLineSummaryResultData> jigenDataMap = new HashMap<>();
        for (AnalysisEmsAllLineSummaryResultData jigenData : summaryDataList) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData);
        }
        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 5;

        for (String jigenName : getLineBuildingSummaryAllDefineNameList()) {
            if (jigenDataMap.containsKey(jigenIndex)) {
                if (line.getLineNo().equals("")) {
                    outputDataMap.get(jigenName).add("-");
                } else {
                    outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getSummary());
                }
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getAverage());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMax());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMin());
            } else {
                setSameColumnSameData(outputDataMap, jigenName, "", 4);
            }

            //罫線設定
            optionSettingMap.get(jigenName).add(firstBorder[borderIndex]);
            optionSettingMap.get(jigenName).add(centerBorder[borderIndex]);
            optionSettingMap.get(jigenName).add(centerBorder[borderIndex]);
            optionSettingMap.get(jigenName).add(lastBorder[borderIndex]);

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 系統別 建物別サマリー 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setBuildingLineSummaryBillInfo(String buildingNo, String buildingName, LineListDetailResultData line,
            List<AnalysisEmsAllLineSummaryResultData> summaryDataList, Boolean hasNext, Boolean isFirst,
            Boolean isOutputBuilding, String linkPath) {

        if (isOutputBuilding) {
            setSameColumnSameData(optionSettingMap, LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_LINK.getName(), linkPath,
                    4);
        }

        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName(), buildingNo, 4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName(), buildingName,
                4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName(), line.getLineName(),
                4);
        setSameColumnSameData(outputDataMap, LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName(), line.getLineUnit(),
                4);
        for (AGGREGATE_TYPE type : AGGREGATE_TYPE.values()) {
            outputDataMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(type.getLabel());
        }

        //罫線設定
        String[] buildingFirstBorder = createBuildingSummaryBillFirstBorder();
        String[] lineFirstBorder = createBuildingSummaryBillLineFirstBorder();
        String[] lineCenterBorder = createBuildingSummaryBillLineCenterBorder();
        String[] lineLastBorder = createBuildingSummaryBillLineLastBorder();
        String[] buildingLastBorder = createBuildingSummaryBillLastBorder();

        if (isFirst) {
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(buildingFirstBorder[0]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(buildingFirstBorder[1]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(buildingFirstBorder[2]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(buildingFirstBorder[3]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(buildingFirstBorder[4]);
        } else {
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineFirstBorder[0]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineFirstBorder[1]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineFirstBorder[2]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineFirstBorder[3]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineFirstBorder[4]);
        }

        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineCenterBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineCenterBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineCenterBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineCenterBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineCenterBorder[4]);

        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineCenterBorder[0]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineCenterBorder[1]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineCenterBorder[2]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineCenterBorder[3]);
        optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineCenterBorder[4]);

        if (hasNext) {
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(lineLastBorder[0]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(lineLastBorder[1]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(lineLastBorder[2]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(lineLastBorder[3]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(lineLastBorder[4]);
        } else {
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NO.getName()).add(buildingLastBorder[0]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.BUILDING_NAME.getName()).add(buildingLastBorder[1]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_NAME.getName()).add(buildingLastBorder[2]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.LINE_UNIT.getName()).add(buildingLastBorder[3]);
            optionSettingMap.get(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.SUMMARY_TYPE.getName()).add(buildingLastBorder[4]);
        }

        //時限とデータのマップ作成
        Map<Integer, AnalysisEmsAllLineSummaryResultData> jigenDataMap = new HashMap<>();
        for (AnalysisEmsAllLineSummaryResultData jigenData : summaryDataList) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData);
        }

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 5;

        for (String jigenName : getLineBuildingSummaryBillDefineNameList()) {
            if (jigenDataMap.containsKey(jigenIndex)) {
                if (line.getLineNo().equals("")) {
                    outputDataMap.get(jigenName).add("-");
                } else {
                    outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getSummary());
                }
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getAverage());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMax());
                outputDataMap.get(jigenName).add(jigenDataMap.get(jigenIndex).getMin());
            } else {
                setSameColumnSameData(outputDataMap, jigenName, "", 4);
            }

            //罫線設定
            if (isFirst) {
                optionSettingMap.get(jigenName).add(buildingFirstBorder[borderIndex]);

            } else {
                optionSettingMap.get(jigenName).add(lineFirstBorder[borderIndex]);
            }

            optionSettingMap.get(jigenName).add(lineCenterBorder[borderIndex]);
            optionSettingMap.get(jigenName).add(lineCenterBorder[borderIndex]);

            if (hasNext) {
                optionSettingMap.get(jigenName).add(lineLastBorder[borderIndex]);
            } else {
                optionSettingMap.get(jigenName).add(buildingLastBorder[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 系統別 建物別 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setBuildingLineInfo(LineListDetailResultData line, List<CommonDemandDayReportLineListResult> lineData,
            Date currentDate, Map<String, String> defineNameList, boolean hasNext, boolean isFirst) {

        outputDataMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.DATE.getName()))
                .add(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER));
        outputDataMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_NAME.getName())).add(line.getLineName());
        outputDataMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_UNIT.getName())).add(line.getLineUnit());

        //罫線設定
        if (isFirst) {
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.DATE.getName()))
                    .add(createLineBuildingFirstBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_NAME.getName()))
                    .add(createLineBuildingFirstBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_UNIT.getName()))
                    .add(createLineBuildingFirstBorder()[2]);
        } else if (hasNext) {
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.DATE.getName()))
                    .add(createLineBuildingCenterBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_NAME.getName()))
                    .add(createLineBuildingCenterBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_UNIT.getName()))
                    .add(createLineBuildingCenterBorder()[2]);

        } else {
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.DATE.getName())).add(createLineBuildingEndBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_NAME.getName())).add(createLineBuildingEndBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_BUILDING_DEFINE_NAME.LINE_UNIT.getName())).add(createLineBuildingEndBorder()[2]);
        }

        //時限とデータのマップ作成
        Map<Integer, BigDecimal> jigenDataMap = new HashMap<>();
        for (CommonDemandDayReportLineListResult jigenData : lineData) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData.getLineValueKw());
        }

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 3;

        for (String jigenName : getLineBuildingDefineNameList()) {

            if (jigenDataMap.containsKey(jigenIndex)) {
                outputDataMap.get(defineNameList.get(jigenName)).add(jigenDataMap.get(jigenIndex));
            } else {
                outputDataMap.get(defineNameList.get(jigenName)).add("");
            }

            //罫線設定
            if (isFirst) {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingFirstBorder()[borderIndex]);
            } else if (hasNext) {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingCenterBorder()[borderIndex]);
            } else {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingEndBorder()[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 系統別 グループ指定 情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setGroupLineInfo(LineListDetailResultData line, List<CommonDemandDayReportLineListResult> lineData,
            Date currentDate, Map<String, String> defineNameList, boolean hasNext, boolean isFirst) {

        outputDataMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.DATE.getName()))
                .add(DateUtility.changeDateFormat(currentDate, DateUtility.DATE_FORMAT_YYYYMMDD_CHINESE_CHARACTER));
        outputDataMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_NAME.getName())).add(line.getLineName());
        outputDataMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_UNIT.getName())).add(line.getLineUnit());
        //罫線設定
        if (isFirst) {
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.DATE.getName()))
                    .add(createLineBuildingFirstBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_NAME.getName()))
                    .add(createLineBuildingFirstBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_UNIT.getName()))
                    .add(createLineBuildingFirstBorder()[2]);
        } else if (hasNext) {
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.DATE.getName()))
                    .add(createLineBuildingCenterBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_NAME.getName()))
                    .add(createLineBuildingCenterBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_UNIT.getName()))
                    .add(createLineBuildingCenterBorder()[2]);

        } else {
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.DATE.getName())).add(createLineBuildingEndBorder()[0]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_NAME.getName())).add(createLineBuildingEndBorder()[1]);
            optionSettingMap.get(defineNameList.get(LINE_GROUP_DEFINE_NAME.LINE_UNIT.getName())).add(createLineBuildingEndBorder()[2]);
        }

        //時限とデータのマップ作成
        Map<Integer, BigDecimal> jigenDataMap = new HashMap<>();
        for (CommonDemandDayReportLineListResult jigenData : lineData) {
            jigenDataMap.put(jigenData.getJigenNo().intValue(), jigenData.getLineValueKw());
        }

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 1;
        int borderIndex = 3;

        for (String jigenName : getLineGroupDefineNameList()) {
            if (jigenDataMap.containsKey(jigenIndex)) {
                outputDataMap.get(defineNameList.get(jigenName)).add(jigenDataMap.get(jigenIndex));
            } else {
                outputDataMap.get(defineNameList.get(jigenName)).add("");
            }

            //罫線設定
            if (isFirst) {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingFirstBorder()[borderIndex]);
            } else if (hasNext) {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingCenterBorder()[borderIndex]);
            } else {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createLineBuildingEndBorder()[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    /**
     * 時限標準値情報出力
     *
     * @param outputDataMap
     * @param planName
     */
    private void setTimeStandardBuildingLineInfo(String buildingNo,String buildingName,LineListDetailResultData resultData,List<BuildingLineTimeStandardListTimeResultData>jigenDataList,Map<String,String> defineNameList,boolean hasNext) {
        // データと空行セット
        setSameColumnSameData(outputDataMap, defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NO.getName()), buildingNo,
                2);

        setSameColumnSameData(outputDataMap, defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NAME.getName()),
                buildingName, 2);

        setSameColumnSameData(outputDataMap, defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE.getName()),
                resultData.getLineName(), 2);

        setSameColumnSameData(outputDataMap, defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE_UNIT.getName()),
                resultData.getLineUnit(), 2);
        outputDataMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.SETTING.getName())).add(MAX_MIN.LIMIT.getLabel());
        outputDataMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.SETTING.getName())).add(MAX_MIN.LOWER.getLabel());

        //罫線設定
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NO.getName()))
                .add(createTimeStandardLimitBorder()[0]);
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NO.getName()))
                .add(getBorderSetting(hasNext, 0));

        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NAME.getName()))
                .add(createTimeStandardLimitBorder()[1]);
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.BUILDING_NAME.getName()))
                .add(getBorderSetting(hasNext, 1));

        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE.getName()))
                .add(createTimeStandardLimitBorder()[2]);
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE.getName())).add(getBorderSetting(hasNext, 2));

        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE_UNIT.getName()))
                .add(createTimeStandardLimitBorder()[3]);
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.LINE_UNIT.getName()))
                .add(getBorderSetting(hasNext, 3));

        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.SETTING.getName()))
                .add(createTimeStandardLimitBorder()[4]);
        optionSettingMap.get(defineNameList.get(TIME_STANDARD_DEFINE_NAME.SETTING.getName()))
                .add(getBorderSetting(hasNext, 4));

        //データがある場合は48時限分のデータを表示する
        int jigenIndex = 0;
        int borderIndex = 5;
        for (String jigenName : getTimeStandardDefineNameList()) {

            if (jigenDataList != null && jigenDataList.size() != 0) {
                outputDataMap.get(defineNameList.get(jigenName))
                        .add(jigenDataList.get(jigenIndex).getLineLimitStandardKw());
                outputDataMap.get(defineNameList.get(jigenName))
                        .add(jigenDataList.get(jigenIndex).getLineLowerStandardKw());

            } else {
                setSameColumnSameData(outputDataMap, defineNameList.get(jigenName), "", 2);
            }

            //罫線設定
            optionSettingMap.get(defineNameList.get(jigenName)).add(createTimeStandardLimitBorder()[borderIndex]);
            if (hasNext) {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createTimeStandardLowerBorder()[borderIndex]);
            } else {
                optionSettingMap.get(defineNameList.get(jigenName)).add(createTimeStandardLastBorder()[borderIndex]);
            }

            jigenIndex++;
            borderIndex++;

        }

    }

    //最終行の場合は、最終行用の罫線設定を取得する
    private String getBorderSetting(boolean hasNext, int index) {
        if (hasNext) {
            return createTimeStandardLowerBorder()[index];
        } else {
            return createTimeStandardLastBorder()[index];
        }
    }

    /**
     * 同じカラムに同じデータを指定された回数付与する
     *
     * @param map
     * @param name
     * @param data
     * @param times
     */
    private void setSameColumnSameData(Map<String, List<Object>> map, String name, Object data, int times) {
        for (int i = 0; i < times; i++) {
            map.get(name).add(data);
        }
    }

    /**
     * 系統別計測値の建物別出力シートに設定する名前定義を生成する処理
     * @param sheetNo
     * @return
     */
    private Map<String, String> getLineBuildingDefineNameMap(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LINE_BUILDING_DEFINE_NAME defineName : LINE_BUILDING_DEFINE_NAME.values()) {
            defineNameMap.put(sheetNo + defineName.getName(), defineName.getFormula());
        }

        return defineNameMap;
    }

    /**
     * 系統別計測値の建物別出力シートのシート連番付き名前定義をマップで出力
     * @return
     */
    private Map<String, String> getLineBuildingDefineNameMapEachSheetNo(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LINE_BUILDING_DEFINE_NAME defineName : LINE_BUILDING_DEFINE_NAME.values()) {
            defineNameMap.put(defineName.getName(), sheetNo + defineName.getName());
        }

        return defineNameMap;

    }

    /**
     * 系統別計測値のグループ指定出力シートに設定する名前定義を生成する処理
     * @param sheetNo
     * @return
     */
    private Map<String, String> getLineGroupDefineNameMap(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LINE_GROUP_DEFINE_NAME defineName : LINE_GROUP_DEFINE_NAME.values()) {
            defineNameMap.put(sheetNo + defineName.getName(), defineName.getFormula());
        }

        return defineNameMap;
    }

    /**
     * 系統別計測値のグループ指定出力シートのシート連番付き名前定義をマップで出力
     * @return
     */
    private Map<String, String> getLineGroupDefineNameMapEachSheetNo(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LINE_GROUP_DEFINE_NAME defineName : LINE_GROUP_DEFINE_NAME.values()) {
            defineNameMap.put(defineName.getName(), sheetNo + defineName.getName());
        }

        return defineNameMap;

    }

    /**
     * 時限標準値シートに設定する名前定義を生成する処理
     * @param sheetNo
     * @return
     */
    private Map<String, String> getTimeStandardDefineNameMap(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (TIME_STANDARD_DEFINE_NAME defineName : TIME_STANDARD_DEFINE_NAME.values()) {
            defineNameMap.put(sheetNo + defineName.getName(), defineName.getFormula());
        }

        return defineNameMap;
    }

    /**
     * 時限標準値出力シートのシート連番付き名前定義をマップで出力
     * @return
     */
    private Map<String, String> getTimeStandardDefineNameMapEachSheetNo(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (TIME_STANDARD_DEFINE_NAME defineName : TIME_STANDARD_DEFINE_NAME.values()) {
            defineNameMap.put(defineName.getName(), sheetNo + defineName.getName());
        }

        return defineNameMap;

    }

    /**
     * 負荷制御履歴の建物別出力シートに設定する名前定義を生成する処理
     * @param sheetNo
     * @return
     */
    private Map<String, String> getLoadCtrlBuildingDefineNameMap(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LOAD_CTRL_HISTORY_DEFINE_NAME defineName : LOAD_CTRL_HISTORY_DEFINE_NAME.values()) {
            defineNameMap.put(sheetNo + defineName.getName(), defineName.getFormula());
        }

        return defineNameMap;
    }

    /**
     * 負荷制御履歴の建物別出力シートのシート連番付き名前定義をマップで出力
     * @return
     */
    private Map<String, String> getLoadCtrlBuildingDefineNameMapSheetNo(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (LOAD_CTRL_HISTORY_DEFINE_NAME defineName : LOAD_CTRL_HISTORY_DEFINE_NAME.values()) {
            defineNameMap.put(defineName.getName(), sheetNo + defineName.getName());
        }

        return defineNameMap;

    }

    /**
     * イベント制御履歴の建物別出力シートに設定する名前定義を生成する処理
     * @param sheetNo
     * @return
     */
    private Map<String, String> getEventCtrlBuildingDefineNameMap(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (EVENT_CTRL_HISTORY_DEFINE_NAME defineName : EVENT_CTRL_HISTORY_DEFINE_NAME.values()) {
            defineNameMap.put(sheetNo + defineName.getName(), defineName.getFormula());
        }

        return defineNameMap;
    }

    /**
     * イベント制御履歴の建物別出力シートのシート連番付き名前定義をマップで出力
     * @return
     */
    private Map<String, String> getEventCtrlBuildingDefineNameMapSheetNo(String sheetNo) {
        Map<String, String> defineNameMap = new HashMap<>();
        for (EVENT_CTRL_HISTORY_DEFINE_NAME defineName : EVENT_CTRL_HISTORY_DEFINE_NAME.values()) {
            defineNameMap.put(defineName.getName(), sheetNo + defineName.getName());
        }

        return defineNameMap;

    }

    /**
     * 系統別(グループ指定)サマリー出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineGroupSummaryAllDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別(グループ指定)サマリー出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineGroupSummaryBillDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別(建物指定)サマリー出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineBuildingSummaryAllDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別(建物指定)サマリー出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineBuildingSummaryBillDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別(建物指定)出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineBuildingDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_BUILDING_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別(グループ指定)出力シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getLineGroupDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(LINE_GROUP_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 時限標準値シートの時限の名前定義リストを生成する処理
     * @param sheetNo
     * @return
     */
    private List<String> getTimeStandardDefineNameList() {
        List<String> jigenDefineNameList = new ArrayList<>();
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN1.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN2.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN3.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN4.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN5.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN6.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN7.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN8.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN9.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN10.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN11.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN12.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN13.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN14.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN15.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN16.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN17.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN18.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN19.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN20.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN21.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN22.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN23.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN24.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN25.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN26.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN27.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN28.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN29.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN30.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN31.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN32.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN33.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN34.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN35.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN36.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN37.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN38.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN39.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN40.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN41.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN42.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN43.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN44.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN45.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN46.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN47.getName());
        jigenDefineNameList.add(TIME_STANDARD_DEFINE_NAME.JIGEN48.getName());

        return jigenDefineNameList;
    }

    /**
     * 系統別出力(建物毎)時のサマリー出力用Mapの初期化
     */
    private void initSearchLineBuildingSummaryMap() {
        //全体サマリー部
        for (LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME value : LINE_BUILDING_SUMMARY_ALL_DEFINE_NAME.values()) {
            optionSettingMap.put(value.getName(), new ArrayList<>());
            outputDataMap.put(value.getName(), new ArrayList<>());
            preSettingMap.put(value.getName(), new ArrayList<>());
        }
        //個別サマリー部
        for (LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME value : LINE_BUILDING_SUMMARY_BILL_DEFINE_NAME.values()) {
            optionSettingMap.put(value.getName(), new ArrayList<>());
            outputDataMap.put(value.getName(), new ArrayList<>());
            preSettingMap.put(value.getName(), new ArrayList<>());
        }
    }

    /**
     * 系統別出力グループ指定時のサマリー出力用Mapの初期化
     */
    private void initSearchLineGroupSummaryMap() {
        //全体サマリー部
        for (LINE_GROUP_SUMMARY_ALL_DEFINE_NAME value : LINE_GROUP_SUMMARY_ALL_DEFINE_NAME.values()) {
            optionSettingMap.put(value.getName(), new ArrayList<>());
            outputDataMap.put(value.getName(), new ArrayList<>());
            preSettingMap.put(value.getName(), new ArrayList<>());
        }
        //個別サマリー部
        for (LINE_GROUP_SUMMARY_BILL_DEFINE_NAME value : LINE_GROUP_SUMMARY_BILL_DEFINE_NAME.values()) {
            optionSettingMap.put(value.getName(), new ArrayList<>());
            outputDataMap.put(value.getName(), new ArrayList<>());
            preSettingMap.put(value.getName(), new ArrayList<>());
        }
    }

    /**
     * 系統別出力(建物毎)時の出力用Mapの初期化
     */
    private void initSearchLineBuildingMap(String sheetNo) {
        for (LINE_BUILDING_DEFINE_NAME value : LINE_BUILDING_DEFINE_NAME.values()) {
            optionSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
            outputDataMap.put(sheetNo + value.getName(), new ArrayList<>());
            preSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
        }
    }

    /**
     * 系統別出力(グループ指定)時の出力用Mapの初期化
     */
    private void initSearchLineGroupMap(String sheetNo) {
        for (LINE_GROUP_DEFINE_NAME value : LINE_GROUP_DEFINE_NAME.values()) {
            optionSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
            outputDataMap.put(sheetNo + value.getName(), new ArrayList<>());
            preSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
        }
    }

    /**
     * 時限標準値時の出力用Mapの初期化
     */
    private void initSearchTimeStandardMap(String sheetNo) {
        for (TIME_STANDARD_DEFINE_NAME value : TIME_STANDARD_DEFINE_NAME.values()) {
            optionSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
            outputDataMap.put(sheetNo + value.getName(), new ArrayList<>());
            preSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
        }
    }

    /**
     * 負荷制御履歴出力時の出力用Mapの初期化
     */
    private void initSearchLoadCtrlBuildingMap(String sheetNo) {
        for (LOAD_CTRL_HISTORY_DEFINE_NAME value : LOAD_CTRL_HISTORY_DEFINE_NAME.values()) {
            optionSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
            outputDataMap.put(sheetNo + value.getName(), new ArrayList<>());
            preSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
        }
    }

    /**
     * イベント制御履歴出力時の出力用Mapの初期化
     */
    private void initSearchEventCtrlBuildingMap(String sheetNo) {
        for (EVENT_CTRL_HISTORY_DEFINE_NAME value : EVENT_CTRL_HISTORY_DEFINE_NAME.values()) {
            optionSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
            outputDataMap.put(sheetNo + value.getName(), new ArrayList<>());
            preSettingMap.put(sheetNo + value.getName(), new ArrayList<>());
        }
    }

    /**
     * グループ別系統別計測値サマリー出力時、建物別の開始行用
     *
     * @return
     */
    private String[] createGroupSummaryBillFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[2] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * グループ別系統別計測値サマリー出力時、建物別系統別の開始行用
     *
     * @return
     */
    private String[] createGroupSummaryBillLineFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * グループ別系統別計測値サマリー出力時、建物別系統別の中間行用
     *
     * @return
     */
    private String[] createGroupSummaryBillLineCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * グループ別系統別計測値サマリー出力時、建物別系統別の最終行用
     *
     * @return
     */
    private String[] createGroupSummaryBillLineLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * グループ別系統別計測値サマリー出力時、建物別の最終行用
     *
     * @return
     */
    private String[] createGroupSummaryBillLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、子グループの開始行用
     *
     * @return
     */
    private String[] createGroupSummaryChildFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:SINGLE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:SINGLE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、子グループ系統の開始行用
     *
     * @return
     */
    private String[] createGroupSummaryChildLineFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の中間行用
     *
     * @return
     */
    private String[] createGroupSummaryAllCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の最終行用
     *
     * @return
     */
    private String[] createGroupSummaryChildLineLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の最終行用
     *
     * @return
     */
    private String[] createGroupSummaryChildLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[54];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[5] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 6; index < 54; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、建物別の開始行用
     *
     * @return
     */
    private String[] createBuildingSummaryBillFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 5; index < 53; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、建物別系統別の開始行用
     *
     * @return
     */
    private String[] createBuildingSummaryBillLineFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 5; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、建物別系統別の中間行用
     *
     * @return
     */
    private String[] createBuildingSummaryBillLineCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        for (int index = 5; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、建物別系統別の最終行用
     *
     * @return
     */
    private String[] createBuildingSummaryBillLineLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 5; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、建物別の最終行用
     *
     * @return
     */
    private String[] createBuildingSummaryBillLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:NONE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[4] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 5; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の開始行用
     *
     * @return
     */
    private String[] createBuildingSummaryAllFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:SINGLE,RIGHT:NONE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:SINGLE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の中間行用
     *
     * @return
     */
    private String[] createBuildingSummaryAllCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:NONE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:NONE";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値サマリー出力時、全体、系統の最終行用
     *
     * @return
     */
    private String[] createBuildingSummaryAllLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:NONE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[1] = "TOP:NONE,RIGHT:NONE,LEFT:NONE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 系統別計測値出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLineBuildingFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値出力時、建物毎、日付毎の中間行用
     *
     * @return
     */
    private String[] createLineBuildingCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 系統別計測値出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLineBuildingEndBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:NONE,RIGHT:SINGLE,LEFT:NONE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 時限標準値の上限行
     *
     * @return
     */
    private String[] createTimeStandardLimitBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[2] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[3] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 時限標準値の下限行
     *
     * @return
     */
    private String[] createTimeStandardLowerBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 時限標準値の最終行
     *
     * @return
     */
    private String[] createTimeStandardLastBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[53];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[1] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[2] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        ret[3] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 4; index < 53; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（合計欄）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLoadCtrlSumFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（合計行）出力時、建物毎、日付毎の中間行用
     *
     * @return
     */
    private String[] createLoadCtrlSumCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（合計行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLoadCtrlSumEndBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（建物行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLoadCtrlEachFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（建物行）出力時、建物毎、日付毎の中間行用
     *
     * @return
     */
    private String[] createLoadCtrlEachCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * 負荷制御履歴（建物行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createLoadCtrlEachEndBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * イベント制御履歴（合計欄）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createEventCtrlSumFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * イベント制御履歴（合計行）出力時、建物毎、日付毎の中間行用
     *
     * @return
     */
    private String[] createEventCtrlSumCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * イベント制御履歴（合計行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createEventCtrlSumEndBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * イベント制御履歴（建物行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createEventCtrlEachFirstBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:SINGLE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * イベント制御履歴（建物行）出力時、建物毎、日付毎の中間行用
     *
     * @return
     */
    private String[] createEventCtrlEachCenterBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:NONE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:DOTTED";
        }
        return ret;
    }

    /**
     * イベント制御履歴（建物行）出力時、建物毎、日付毎の開始行用
     *
     * @return
     */
    private String[] createEventCtrlEachEndBorder() {
        // 出力カラム数分設定を用意する
        String[] ret = new String[51];
        ret[0] = "TOP:NONE,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        for (int index = 1; index < 51; index++) {
            ret[index] = "TOP:DOTTED,RIGHT:SINGLE,LEFT:SINGLE,BOTTOM:SINGLE";
        }
        return ret;
    }

    /**
     * DBから最新日時を取得できないため
     * @return
     */
    private Timestamp getCurrentDateTime() {
        return new Timestamp((new Date()).getTime());
    }

    /**
     * 対象の予約情報を取得して、処理ステータスが「中止処理中」かチェックする
     * @return true:処理中止、false:処理続行
     */
    private boolean chkProcessStop() {
        // 最新の情報を取得
        AnalysisEmsReservationInfoResultData result = dao.find(resultData);
        // 最新情報が取得できない、または処理ステータスが中止処理中の場合
        if (result == null || ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.NOW_PROCESSING_CANCEL.getVal().equals(result.getAggregateProcessStatus())) {
            processStopFlg = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 指定パスのファイルまたは、ディレクトリ削除
     *
     * @param file
     */
    private void deleteFileDirectory(File file) {
        if (file.exists() && file.isDirectory()) {
            if (file.listFiles().length <= 0) {
                file.delete();
            } else {
                for (File file2 : file.listFiles()) {
                    deleteFileDirectory(file2);
                }
                file.delete();
            }
        } else if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
}
