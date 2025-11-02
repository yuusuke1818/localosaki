package jp.co.osaki.osol.api.dao.analysis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.BuildingDataFilterDao;
import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.OsolApiParameter;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.dao.energy.verify.DemandOrgDailyControlLogDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsLoadControlHistParameter;
import jp.co.osaki.osol.api.parameter.energy.verify.DemandOrgDailyControlLogParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsLoadControlHistResult;
import jp.co.osaki.osol.api.result.energy.verify.DemandOrgDailyControlLogResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsControlHistControlLoadResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsControlHistPeriodResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsControlHistSummaryResultData;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsSmControlHistResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.DemandBuildingSmListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.ProductControlLoadListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogControlDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.DemandOrgDailyControlLogKindResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.DemandBuildingSmListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.ProductControlLoadListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MProductSpecServiceDaoImpl;
import jp.co.osaki.osol.api.utility.energy.ems.DemandEmsUtility;
import jp.co.osaki.osol.entity.MProductSpec;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 集計・分析 EMS実績 負荷制御履歴取得 Daoクラス
 * @author nishida.t
 *
 */
@Stateless
public class AnalysisEmsLoadControlHistDao extends OsolApiDao<AnalysisEmsLoadControlHistParameter> {

    // 企業データフィルター
    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;
    // 建物機器情報
    private final DemandBuildingSmListServiceDaoImpl demandBuildingSmListServiceDaoImpl;
    // 製品仕様
    private final MProductSpecServiceDaoImpl mProductSpecServiceDaoImpl;
    // 製品制御負荷情報
    private final ProductControlLoadListServiceDaoImpl productControlLoadListServiceDaoImpl;

    @EJB
    private BuildingDataFilterDao buildingDataFilterDao;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    // 制御履歴取得Dao
    @EJB
    private DemandOrgDailyControlLogDao demandOrgDailyControlLogDao;

    public AnalysisEmsLoadControlHistDao() {
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
        demandBuildingSmListServiceDaoImpl = new DemandBuildingSmListServiceDaoImpl();
        mProductSpecServiceDaoImpl = new MProductSpecServiceDaoImpl();
        productControlLoadListServiceDaoImpl = new ProductControlLoadListServiceDaoImpl();

    }

    @Override
    public AnalysisEmsLoadControlHistResult query(AnalysisEmsLoadControlHistParameter parameter) throws Exception {

        AnalysisEmsLoadControlHistResult result = new AnalysisEmsLoadControlHistResult();

        // 機器別の結果リスト
        List<AnalysisEmsSmControlHistResultData> smResultList = new ArrayList<>();

        // 操作企業データフィルター用データ取得
        CommonCorpExclusionResult exCorpParam = new CommonCorpExclusionResult();
        exCorpParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exCorpList = getResultList(commonCorpExclusionServiceDaoImpl, exCorpParam);

        //フィルター処理を行う
        exCorpList = corpDataFilterDao.applyDataFilter(exCorpList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exCorpList == null || exCorpList.size() != 1) {
            return new AnalysisEmsLoadControlHistResult();
        }

        // 集計期間From ～ 集計期間Toになっていない場合処理終了
        if (parameter.getRangeDateFrom().compareTo(parameter.getRangeDateTo()) > 0
                || parameter.getRangeDateTo().compareTo(parameter.getRangeDateFrom()) < 0) {
               return new AnalysisEmsLoadControlHistResult();
        }

        // 小数点制度がNULLの場合、1を設定
        if (parameter.getPrecision() == null) {
            parameter.setPrecision(new Integer(1));
        }

        // 指定精度未満の制御がNULLの場合、0：四捨五入を設定
        if (parameter.getBelowAccuracyControl() == null) {
            parameter.setBelowAccuracyControl(ApiCodeValueConstants.PRECISION_CONTROL.ROUND_HALF_UP.getVal());
        }

        // 建物機器情報を取得する
        DemandBuildingSmListDetailResultData buildingSmParam = DemandEmsUtility
                .getBuildingSmListParam(parameter.getOperationCorpId(), parameter.getBuildingId(), null);
        List<DemandBuildingSmListDetailResultData> buildingSmList = getResultList(
                demandBuildingSmListServiceDaoImpl, buildingSmParam);

        // フィルタ処理を行う
        buildingSmList = buildingDataFilterDao.applyDataFilter(buildingSmList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (buildingSmList == null || buildingSmList.isEmpty()) {
            //建物機器情報が存在しない場合は処理終了
            return new AnalysisEmsLoadControlHistResult();
        }

        // 建物に紐づく機器数
         for (DemandBuildingSmListDetailResultData buildingSm : buildingSmList) {
             // 製品コード
             String productCd;

             // 製品情報を取得する
             MProductSpec productParam = new MProductSpec();
             productParam.setProductCd(buildingSm.getProductCd());
             MProductSpec productSpec = find(mProductSpecServiceDaoImpl, productParam);
             if (productSpec == null) {
                 // 製品情報が取得できない場合は、処理を終了する
                 return new AnalysisEmsLoadControlHistResult();
             } else {

                 // FVPα(D), FVPα(G2), Eα, Eα2以外は処理しないのでスキップ
                 if (
                         !(OsolConstants.PRODUCT_CD.FVP_ALPHA_D.getVal().equals(productSpec.getProductCd())
                         || OsolConstants.PRODUCT_CD.FVP_ALPHA_G2.getVal().equals(productSpec.getProductCd())
                         || OsolConstants.PRODUCT_CD.FVP_E_ALPHA.getVal().equals(productSpec.getProductCd())
                         || OsolConstants.PRODUCT_CD.FVP_E_ALPHA2.getVal().equals(productSpec.getProductCd())
                         )) {
                     continue;
                 }
                 productCd = productSpec.getProductCd();
             }


            // 製品制御負荷情報を取得する
            ProductControlLoadListDetailResultData productControlParam = DemandEmsUtility
                    .getProductControlLoadListParam(productCd, null, null);
            List<ProductControlLoadListDetailResultData> productControlLoadList = getResultList(
                    productControlLoadListServiceDaoImpl, productControlParam);
            if (productControlLoadList == null || productControlLoadList.isEmpty()) {
                return null;
            }

            // デマンド制御のみに絞り込み
            List<ProductControlLoadListDetailResultData> productControlLoadDemandList = productControlLoadList.stream()
                    .filter(o -> o.getDemandControlFlg().equals(OsolConstants.FLG_ON))
                    .collect(Collectors.toList());

             // 日付に変換
             Date measurementDateFrom = DateUtility.conversionDate(parameter.getRangeDateFrom().replace("/", ""), DateUtility.DATE_FORMAT_YYYYMMDD);
             Date measurementDateTo = DateUtility.conversionDate(parameter.getRangeDateTo().replace("/", ""), DateUtility.DATE_FORMAT_YYYYMMDD);

             // Map<計測日時, List<制御負荷リスト>>
             Map<Date, List<AnalysisEmsControlHistControlLoadResultData>>  sumPeriodDaysMap = new LinkedHashMap<Date, List<AnalysisEmsControlHistControlLoadResultData>>();

             // 集計期間Fromが集計期間Toより前の日 または集計期間Fromと集計期間Toが同じ日の間繰り返す
             while (measurementDateFrom.before(measurementDateTo)
                     || DateUtility.checkSameYearMonthDay(measurementDateFrom, measurementDateTo)) {

                 // 計測日をKeyで初期化
                 sumPeriodDaysMap.put(measurementDateFrom,  new ArrayList<AnalysisEmsControlHistControlLoadResultData>());

                 // 1日進める
                 measurementDateFrom = DateUtility.plusDay(measurementDateFrom, 1);
             }

             // ないはずだが集計期間0日になっていたら処理終了
             if (sumPeriodDaysMap.size() <= 0) {
                 return null;
             }

             // 制御履歴取得Daoから取得
             DemandOrgDailyControlLogParameter param = new DemandOrgDailyControlLogParameter();
             copyOsolApiParameter(parameter, param);
             param.setSmId(buildingSm.getSmId());
             param.setYmd(DateUtility.conversionDate(parameter.getRangeDateFrom().replace("/",  ""), DateUtility.DATE_FORMAT_YYYYMMDD));
             param.setTimesOfDay("0000");
             param.setSumPeriodCalcType(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.START.getVal());
             param.setSumPeriod(new BigDecimal("24").multiply(new BigDecimal(String.valueOf(sumPeriodDaysMap.size()))));
             param.setControlKind(ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal());
             DemandOrgDailyControlLogResult controlHistResult = demandOrgDailyControlLogDao.query(param);

             // 取得した結果が存在しない場合スキップ
             if (controlHistResult == null ||  controlHistResult.getDetailList() == null
                     || controlHistResult.getDetailList().isEmpty()) {
                 continue;
             }

             // 取得結果から対象の負荷データのみに絞り込み
             controlHistResult.getDetailList().removeAll(
                     controlHistResult.getDetailList().stream()
                     .filter(x -> productControlLoadDemandList.stream().noneMatch(y -> y.getControlLoad().compareTo(BigDecimal.valueOf(x.getControlLoad())) == 0))
                     .collect(Collectors.toList()));

             // 負荷制御履歴サマリー
             AnalysisEmsControlHistSummaryResultData summary = new AnalysisEmsControlHistSummaryResultData();
             // 負荷制御履歴サマリー.制御負荷リスト
             List<AnalysisEmsControlHistControlLoadResultData> controlLoadSummaryList = new ArrayList<>();

             // 対象期間リスト
             List<AnalysisEmsControlHistPeriodResultData> periodList = new ArrayList<>();

             // 対象負荷数
             for (DemandOrgDailyControlLogDetailResultData loadCtrlData : controlHistResult.getDetailList()) {
                 // 複数の種別があるので必要な種別で処理する
                 for (DemandOrgDailyControlLogControlDetailResultData detailData : loadCtrlData.getDetailList()) {
                     // デマンドで無い場合スキップ
                     if (!ApiCodeValueConstants.CONTROL_KIND.DEMAND.getName().equals(detailData.getControlKind())) {
                         continue;
                     }

                     // 日別の時間帯実績Map
                     Map<Date, List<DemandOrgDailyControlLogKindResultData>> timeMap = new HashMap<>();

                     // デマンド制御の結果を処理
                     for (DemandOrgDailyControlLogKindResultData kindData : detailData.getKindList()) {
                         // 日別の時間帯実績Mapに該当の計測日がなければ初期化
                         if (!timeMap.containsKey(kindData.getMeasurementDate())) {
                             timeMap.put(kindData.getMeasurementDate(), new ArrayList<>());
                         }

                         // 時間帯別実績リスト
                         timeMap.get(kindData.getMeasurementDate()).add(kindData);
                     }

                     Map<BigDecimal, DemandOrgDailyControlLogKindResultData> timeSummaryMap = new LinkedHashMap<>();

                     // 日別の時間帯実績Mapから制御負荷情報を加えた制御負荷リストを生成
                     for (Map.Entry<Date, List<DemandOrgDailyControlLogKindResultData>> entry : timeMap.entrySet()) {
                         for (DemandOrgDailyControlLogKindResultData timeData : entry.getValue()) {

                             // 制御時間が0分の場合はnullに置換しておく（期間別の制御負荷リストとサマリーで0分表示されないようにする対応）
                             if (timeData.getControlTime() != null && timeData.getControlTime().compareTo(BigDecimal.ZERO) == 0) {
                                 timeData.setControlTime(null);
                             }

                             if (!timeSummaryMap.containsKey(timeData.getJigenNo())) {
                                 DemandOrgDailyControlLogKindResultData timeSummary = new DemandOrgDailyControlLogKindResultData();
                                 timeSummary.setTimeTitle(timeData.getTimeTitle());
                                 timeSummary.setJigenNo(timeData.getJigenNo());
                                 timeSummary.setControlTime(timeData.getControlTime());
                                 timeSummaryMap.put(timeData.getJigenNo(), timeSummary);
                             } else {
                                 DemandOrgDailyControlLogKindResultData timeSummary = timeSummaryMap.get(timeData.getJigenNo());
                                 // 設定する制御時間がnullでなければ制御時間を加算する
                                 if (timeSummary.getControlTime() != null && timeData.getControlTime() != null) {
                                     timeSummary.setControlTime(timeSummary.getControlTime().add(timeData.getControlTime()));
                                 }
                                 // 設定されている制御時間がnullで設定する制御時間nullでない場合、設定する制御時間を設定する
                                 else if (timeSummary.getControlTime() == null && timeData.getControlTime() != null) {
                                     timeSummary.setControlTime(timeData.getControlTime());
                                 }
                             }
                         }

                         // 期間別の制御負荷リスト
                         AnalysisEmsControlHistControlLoadResultData controlLoadData = new AnalysisEmsControlHistControlLoadResultData();
                         controlLoadData.setControlLoad(loadCtrlData.getControlLoad());
                         controlLoadData.setControlLoadName(loadCtrlData.getControlLoadName());
                         controlLoadData.setControlLoadCircuit(loadCtrlData.getControlLoadCircuit());
                         controlLoadData.setTimeList(entry.getValue());
                         sumPeriodDaysMap.get(entry.getKey()).add(controlLoadData);
                     }

                     // サマリーの制御負荷リスト追加
                     AnalysisEmsControlHistControlLoadResultData controlLoadSummary= new AnalysisEmsControlHistControlLoadResultData();
                     controlLoadSummary.setControlLoad(loadCtrlData.getControlLoad());
                     controlLoadSummary.setControlLoadName(loadCtrlData.getControlLoadName());
                     controlLoadSummary.setControlLoadCircuit(loadCtrlData.getControlLoadCircuit());
                     controlLoadSummary.setTimeList(new ArrayList<DemandOrgDailyControlLogKindResultData>(timeSummaryMap.values()));
                     controlLoadSummaryList.add(controlLoadSummary);
                 }
             }

             int ctrlNullCnt = 0;
             int ctrlRowIndex = 0;
             // 合計値がNULLまたは0の件数（0の場合は出力しないので含める）
             for (AnalysisEmsControlHistControlLoadResultData summaryList : controlLoadSummaryList) {
                for (DemandOrgDailyControlLogKindResultData timeList : summaryList.getTimeList()) {
                    if (timeList.getControlTime() == null
                            || timeList.getControlTime().compareTo(BigDecimal.ZERO) == 0) {
                        ctrlNullCnt++;
                    } else {
                        ctrlNullCnt--;
                        break;
                    }
                    ctrlRowIndex++;
                }
             }

             // 合計値がすべてNULLまたは0の場合スキップ
             if (ctrlNullCnt == ctrlRowIndex) {
                 continue;
             }

             // 負荷制御履歴サマリー生成
             summary.setControlLoadList(controlLoadSummaryList);

             // 対象期間実績リスト生成
             for (Map.Entry<Date, List<AnalysisEmsControlHistControlLoadResultData>> entry : sumPeriodDaysMap.entrySet()) {
                 AnalysisEmsControlHistPeriodResultData loadControlPeriod = new AnalysisEmsControlHistPeriodResultData();
                 loadControlPeriod.setMeasurementDate(entry.getKey());
                 loadControlPeriod.setControlLoadList(entry.getValue());
                 periodList.add(loadControlPeriod);
             }

             // 機器別取得結果ををリストに追加
             AnalysisEmsSmControlHistResultData smLoadControlHist = new  AnalysisEmsSmControlHistResultData();
             smLoadControlHist.setBuildingId(buildingSm.getBuildingId());
             smLoadControlHist.setSmId(buildingSm.getSmId());
             smLoadControlHist.setProductCd(productCd);
             smLoadControlHist.setControlKind(ApiCodeValueConstants.CONTROL_KIND.DEMAND.getVal());
             smLoadControlHist.setControlHistSummary(summary);
             smLoadControlHist.setPeriodList(periodList);
             smResultList.add(smLoadControlHist);
         }

         // 機器別の結果を設定
         result.setSmResultList(smResultList);

        return result;
    }

    // 共通パラメータのコピー
    private void copyOsolApiParameter(OsolApiParameter fromApiParameter, OsolApiParameter toApiParameter) {

        toApiParameter.setBean(fromApiParameter.getBean());
        toApiParameter.setLoginCorpId(fromApiParameter.getLoginCorpId());
        toApiParameter.setLoginPersonId(fromApiParameter.getLoginPersonId());
        toApiParameter.setAuthHash(fromApiParameter.getAuthHash());
        toApiParameter.setMillisec(fromApiParameter.getMillisec());
        toApiParameter.setOperationCorpId(fromApiParameter.getOperationCorpId());
        toApiParameter.setApiKey(fromApiParameter.getApiKey());
        toApiParameter.setOemName(fromApiParameter.getOemName());
        toApiParameter.setInqury(fromApiParameter.getInqury());
        toApiParameter.setUserGuide(fromApiParameter.getUserGuide());
        toApiParameter.setUserGuidePdf(fromApiParameter.getUserGuidePdf());
        toApiParameter.setTermsOfService(fromApiParameter.getTermsOfService());
        toApiParameter.setTermsOfServicePdf(fromApiParameter.getTermsOfServicePdf());
        toApiParameter.setLogo(fromApiParameter.getLogo());
        toApiParameter.setPrivacy(fromApiParameter.getPrivacy());
        toApiParameter.setFavicon(fromApiParameter.getFavicon());
        toApiParameter.setCopyright(fromApiParameter.getCopyright());
    }

}
