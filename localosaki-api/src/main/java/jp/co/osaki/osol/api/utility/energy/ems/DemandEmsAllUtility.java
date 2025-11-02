package jp.co.osaki.osol.api.utility.energy.ems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.resultdata.building.AllBuildingListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.ems.DemandAllSummaryPowerDayUsedAnalysisTimeResultData;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * エネルギー使用状況（全体）関係のUtilityクラス
 * @author ya-ishida
 *
 */
public class DemandEmsAllUtility {

    /**
     * 建物・テナント絞込みのバリデーションチェックを行う
     * @param buildingNarrowing
     * @param parentGroupId
     * @param buildingNo
     * @return
     */
    public static List<String> validateBuildingNarrowing(String buildingNarrowing, Long parentGroupId,
            String buildingNo) {
        List<String> errorMessageList = new ArrayList<>();
        if (ApiCodeValueConstants.BUILDING_FILTER.ALL.getVal().equals(buildingNarrowing)) {
            return errorMessageList;
        } else if (ApiCodeValueConstants.BUILDING_FILTER.GROUP.getVal().equals(buildingNarrowing)) {
            if (parentGroupId == null) {
                errorMessageList.add("no parameter parentGroupId");
            }
        } else if (ApiCodeValueConstants.BUILDING_FILTER.NO.getVal().equals(buildingNarrowing)) {
            if (CheckUtility.isNullOrEmpty(buildingNo)) {
                errorMessageList.add("no parameter buildingNo");
            }
        }
        return errorMessageList;
    }

    /**
     * 対象建物取得のパラメータを設定する
     * @param corpId
     * @param buildingNarrowing
     * @param parentGroupId
     * @param childGroupId
     * @param buildingNo
     * @return
     */
    public static AllBuildingListDetailResultData getBuildingListParam(String corpId, String buildingNarrowing,
            Long parentGroupId, Long childGroupId, String buildingNo) {

        AllBuildingListDetailResultData param = new AllBuildingListDetailResultData();
        param.setCorpId(corpId);
        if (ApiCodeValueConstants.BUILDING_FILTER.ALL.getVal().equals(buildingNarrowing)) {
        } else if (ApiCodeValueConstants.BUILDING_FILTER.GROUP.getVal().equals(buildingNarrowing)) {
            //グループの場合
            param.setParentGroupId(parentGroupId);
            param.setChildGroupId(childGroupId);
        } else if (ApiCodeValueConstants.BUILDING_FILTER.NO.getVal().equals(buildingNarrowing)) {
            //建物番号の場合
            param.setBuildingNo(buildingNo);
        }
        return param;
    }

    /**
     * 電力量評価のデータを作成する
     *
     * @param tempResult
     * @param decimal
     * @param control
     * @return
     */
    public final static DemandAllSummaryPowerDayUsedAnalysisDetailResultData setUsedAnalysis(
            DemandAllSummaryPowerDayUsedAnalysisDetailResultData tempResult,
            Integer decimal, String control) {

        DemandAllSummaryPowerDayUsedAnalysisDetailResultData result = new DemandAllSummaryPowerDayUsedAnalysisDetailResultData();

        //ヘッダ情報を設定する
        result.setHeader(tempResult.getHeader());
        //単位のみ再設定
        result.getHeader().setUnit(ApiSimpleConstants.UNIT_USE_POWER);

        //明細部リストを再設定する
        result.setDetail(tempResult.getDetail());
        //精度の調整と6%削減値の設定
        for (DemandAllSummaryPowerDayUsedAnalysisTimeResultData detail : result.getDetail()) {
            if (detail.getCompareTargetValue() != null) {
                detail.setReductionValue(detail.getCompareTargetValue().multiply(OsolConstants.RATE_MINUS_PERCENT_6)
                        .setScale(decimal, ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
                detail.setCompareTargetValue(detail.getCompareTargetValue().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
            if (detail.getCurrentValue() != null) {
                detail.setCurrentValue(detail.getCurrentValue().setScale(decimal,
                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)));
            }
        }

        return result;
    }

    /**
     * 削減率評価のデータを作成する
     *
     * @param tempResult
     * @param decimal
     * @param control
     * @return
     */
    public final static DemandAllSummaryPowerDayUsedAnalysisDetailResultData setReductionAnalysis(
            DemandAllSummaryPowerDayUsedAnalysisDetailResultData tempResult, Integer decimal, String control) {

        DemandAllSummaryPowerDayUsedAnalysisDetailResultData result = new DemandAllSummaryPowerDayUsedAnalysisDetailResultData();
        List<DemandAllSummaryPowerDayUsedAnalysisTimeResultData> detail = new ArrayList<>();

        //ヘッダ情報を設定する
        result.setHeader(tempResult.getHeader());
        //単位のみ再設定
        result.getHeader().setUnit(ApiSimpleConstants.PERCENT);

        //明細部リストを再設定する
        for (DemandAllSummaryPowerDayUsedAnalysisTimeResultData tempDetail : tempResult.getDetail()) {
            if (tempDetail.getCompareTargetValue() == null || tempDetail.getCurrentValue() == null) {
                detail.add(new DemandAllSummaryPowerDayUsedAnalysisTimeResultData());
            } else if (tempDetail.getCompareTargetValue().compareTo(BigDecimal.ZERO) == 0) {
                detail.add(new DemandAllSummaryPowerDayUsedAnalysisTimeResultData(
                        BigDecimal.ZERO.setScale(decimal,
                                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)),
                        BigDecimal.ZERO.setScale(decimal,
                                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)),
                        new BigDecimal(6).setScale(decimal,
                                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control))));
            } else {
                detail.add(new DemandAllSummaryPowerDayUsedAnalysisTimeResultData(
                        (tempDetail.getCompareTargetValue().subtract(tempDetail.getCurrentValue()))
                                .multiply(new BigDecimal(100))
                                .divide(tempDetail.getCompareTargetValue(), decimal,
                                        ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)),
                        BigDecimal.ZERO.setScale(decimal,
                                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control)),
                        new BigDecimal(6).setScale(decimal,
                                ApiCodeValueConstants.PRECISION_CONTROL.getControlType(control))));
            }
        }

        result.setDetail(detail);

        return result;
    }

}
