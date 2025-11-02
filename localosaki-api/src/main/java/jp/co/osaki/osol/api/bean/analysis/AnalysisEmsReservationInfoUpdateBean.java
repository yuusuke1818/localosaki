package jp.co.osaki.osol.api.bean.analysis;

import java.util.Arrays;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsReservationInfoUpdateDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsReservationInfoUpdateParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsReservationInfoUpdateResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsReservationInfoUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計・分析 EMS実績 予約情報更新(DL予約) Beanクラス
 * @author nishida.t
 *
 */
@Named(value = "AnalysisEmsReservationInfoUpdateBean")
@RequestScoped
public class AnalysisEmsReservationInfoUpdateBean extends OsolApiBean<AnalysisEmsReservationInfoUpdateParameter>
    implements BaseApiBean<AnalysisEmsReservationInfoUpdateParameter, AnalysisEmsReservationInfoUpdateResponse> {

    private AnalysisEmsReservationInfoUpdateParameter parameter = new AnalysisEmsReservationInfoUpdateParameter();
    private AnalysisEmsReservationInfoUpdateResponse response = new AnalysisEmsReservationInfoUpdateResponse();

    @EJB
    AnalysisEmsReservationInfoUpdateDao analysisEmsReservationInfoUpdateDao;

    @Override
    public AnalysisEmsReservationInfoUpdateParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsReservationInfoUpdateParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public AnalysisEmsReservationInfoUpdateResponse execute() throws Exception {
        AnalysisEmsReservationInfoUpdateParameter param = new AnalysisEmsReservationInfoUpdateParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setAggregateConditionResultSet(parameter.getAggregateConditionResultSet());
        param.setAggregateProcessStopFlg(parameter.getAggregateProcessStopFlg());
        param.setAggregateProcessStatus(parameter.getAggregateProcessStatus());
        param.setAggregateProcessResult(parameter.getAggregateProcessResult());
        param.setAggregateStartDate(parameter.getAggregateStartDate());
        param.setAggregateEndDate(parameter.getAggregateEndDate());
        param.setOutputFilePath(parameter.getOutputFilePath());
        param.setOutputFileName(parameter.getOutputFileName());
        param.setReservationInfoListFlg(parameter.getReservationInfoListFlg());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 集計処理中止フラグがOFFの場合、集計処理ステータスと集計処理結果は必須項目
        if (OsolConstants.FLG_OFF.equals(param.getAggregateProcessStopFlg())) {
            //  汎用区分コード定数 132:処理ステータスに存在しない値はエラー
            if (Arrays.asList(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.values()).stream()
                    .filter(o -> o.getVal().equals(param.getAggregateProcessStatus()))
                    .count() < 1) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }

            // 汎用区分コード定数 133:処理結果に存在しない値はエラー
            if (Arrays.asList(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.values()).stream()
                    .filter(o -> o.getVal().equals(param.getAggregateProcessResult()))
                    .count() < 1) {
                response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
                return response;
            }
        }

        // 出力ファイルパスの文字数が300を超えたらエラー
        if (param.getOutputFilePath() != null && param.getOutputFilePath().length() > 300) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 出力ファイル名の文字数が200を超えたらエラー
        if (param.getOutputFileName() != null && param.getOutputFileName().length() > 200) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }


        AnalysisEmsReservationInfoUpdateResult result = analysisEmsReservationInfoUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
