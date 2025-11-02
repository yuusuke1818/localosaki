package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointLineInfoCsvListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointLineInfoCsvListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingPointLineInfoCsvListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointLineInfoCsvListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Beanクラス
 *
 * @author yonezawa.a
 *
 */

@Named(value = "AnalysisEmsBuildingPointLineInfoCsvListBean")
@RequestScoped
public class AnalysisEmsBuildingPointLineInfoCsvListBean extends OsolApiBean<AnalysisEmsBuildingPointLineInfoCsvListParameter>
            implements BaseApiBean<AnalysisEmsBuildingPointLineInfoCsvListParameter,AnalysisEmsBuildingPointLineInfoCsvListResponse> {

    private AnalysisEmsBuildingPointLineInfoCsvListParameter parameter = new AnalysisEmsBuildingPointLineInfoCsvListParameter();
    private AnalysisEmsBuildingPointLineInfoCsvListResponse response = new AnalysisEmsBuildingPointLineInfoCsvListResponse();

    @EJB
    AnalysisEmsBuildingPointLineInfoCsvListDao analysisEmsBuildingPointCsvListDao;

    @Override
    public AnalysisEmsBuildingPointLineInfoCsvListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingPointLineInfoCsvListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingPointLineInfoCsvListResponse execute() throws Exception {

        AnalysisEmsBuildingPointLineInfoCsvListParameter param = new AnalysisEmsBuildingPointLineInfoCsvListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(parameter.getCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setJigenNoFrom(parameter.getJigenNoFrom());
        param.setJigenNoTo(parameter.getJigenNoTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingPointLineInfoCsvListResult result = analysisEmsBuildingPointCsvListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
