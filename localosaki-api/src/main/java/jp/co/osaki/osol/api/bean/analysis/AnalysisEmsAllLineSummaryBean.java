package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsAllLineSummaryDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsAllLineSummaryParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsAllLineSummaryResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsAllLineSummaryResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 系統別サマリー取得(全建物) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsAllLineSummaryBean")
@RequestScoped
public class AnalysisEmsAllLineSummaryBean extends OsolApiBean<AnalysisEmsAllLineSummaryParameter>
            implements BaseApiBean<AnalysisEmsAllLineSummaryParameter,AnalysisEmsAllLineSummaryResponse> {

    private AnalysisEmsAllLineSummaryParameter parameter = new AnalysisEmsAllLineSummaryParameter();
    private AnalysisEmsAllLineSummaryResponse response = new AnalysisEmsAllLineSummaryResponse();

    @EJB
    AnalysisEmsAllLineSummaryDao AnalysisEmsAllLineSummaryDao;

    @Override
    public AnalysisEmsAllLineSummaryParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsAllLineSummaryParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsAllLineSummaryResponse execute() throws Exception {
        AnalysisEmsAllLineSummaryParameter param = new AnalysisEmsAllLineSummaryParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsAllLineSummaryResult result = AnalysisEmsAllLineSummaryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
