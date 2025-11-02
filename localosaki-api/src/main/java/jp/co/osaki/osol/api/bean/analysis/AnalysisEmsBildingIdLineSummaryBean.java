package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingIdLineSummaryDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingIdLineSummaryParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingIdLineSummaryResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingIdLineSummaryResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 系統別サマリー取得(建物ID指定) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsBuildingIdLineSummaryBean")
@RequestScoped
public class AnalysisEmsBildingIdLineSummaryBean extends OsolApiBean<AnalysisEmsBuildingIdLineSummaryParameter>
            implements BaseApiBean<AnalysisEmsBuildingIdLineSummaryParameter,AnalysisEmsBuildingIdLineSummaryResponse> {

    private AnalysisEmsBuildingIdLineSummaryParameter parameter = new AnalysisEmsBuildingIdLineSummaryParameter();
    private AnalysisEmsBuildingIdLineSummaryResponse response = new AnalysisEmsBuildingIdLineSummaryResponse();

    @EJB
    AnalysisEmsBuildingIdLineSummaryDao AnalysisEmsBuildingIdLineSummaryDao;

    @Override
    public AnalysisEmsBuildingIdLineSummaryParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingIdLineSummaryParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingIdLineSummaryResponse execute() throws Exception {
        AnalysisEmsBuildingIdLineSummaryParameter param = new AnalysisEmsBuildingIdLineSummaryParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        if(parameter.getBelongTenantFlg() == null) {
            param.setBelongTenantFlg(Boolean.FALSE);
        } else {
            param.setBelongTenantFlg(parameter.getBelongTenantFlg());
        }

        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingIdLineSummaryResult result = AnalysisEmsBuildingIdLineSummaryDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
