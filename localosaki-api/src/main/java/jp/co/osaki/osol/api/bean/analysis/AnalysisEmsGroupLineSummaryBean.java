package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsGroupLineSummaryDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsGroupLineSummaryParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsGroupLineSummaryResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsGroupLineSummaryResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 系統別サマリー取得(グループ指定) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsGroupLineSummaryBean")
@RequestScoped
public class AnalysisEmsGroupLineSummaryBean extends OsolApiBean<AnalysisEmsGroupLineSummaryParameter>
            implements BaseApiBean<AnalysisEmsGroupLineSummaryParameter,AnalysisEmsGroupLineSummaryResponse> {

    private AnalysisEmsGroupLineSummaryParameter parameter = new AnalysisEmsGroupLineSummaryParameter();
    private AnalysisEmsGroupLineSummaryResponse response = new AnalysisEmsGroupLineSummaryResponse();

    @EJB
    AnalysisEmsGroupLineSummaryDao AnalysisEmsGroupLineSummaryDao;

    @Override
    public AnalysisEmsGroupLineSummaryParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsGroupLineSummaryParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsGroupLineSummaryResponse execute() throws Exception {
        AnalysisEmsGroupLineSummaryParameter param = new AnalysisEmsGroupLineSummaryParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setParentGroupId(parameter.getParentGroupId());
        param.setChildGroupId(parameter.getChildGroupId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsGroupLineSummaryResult result = AnalysisEmsGroupLineSummaryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
