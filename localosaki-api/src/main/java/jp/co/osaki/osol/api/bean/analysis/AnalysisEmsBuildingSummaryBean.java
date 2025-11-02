package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingSummaryDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingSummaryParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingSummaryResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingSummaryResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別サマリー取得 Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsBuildingSummaryBean")
@RequestScoped
public class AnalysisEmsBuildingSummaryBean extends OsolApiBean<AnalysisEmsBuildingSummaryParameter>
            implements BaseApiBean<AnalysisEmsBuildingSummaryParameter,AnalysisEmsBuildingSummaryResponse> {

    private AnalysisEmsBuildingSummaryParameter parameter = new AnalysisEmsBuildingSummaryParameter();
    private AnalysisEmsBuildingSummaryResponse response = new AnalysisEmsBuildingSummaryResponse();

    @EJB
    AnalysisEmsBuildingSummaryDao AnalysisEmsBuildingSummaryDao;

    @Override
    public AnalysisEmsBuildingSummaryParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingSummaryParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingSummaryResponse execute() throws Exception {
        AnalysisEmsBuildingSummaryParameter param = new AnalysisEmsBuildingSummaryParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setLineGroupId(parameter.getLineGroupId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingSummaryResult result = AnalysisEmsBuildingSummaryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
