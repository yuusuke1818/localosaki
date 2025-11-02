package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingLineListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingLineListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingLineListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingLineListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別系統実績値取得 Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsBuildingLineListBean")
@RequestScoped
public class AnalysisEmsBuildingLineListBean extends OsolApiBean<AnalysisEmsBuildingLineListParameter>
            implements BaseApiBean<AnalysisEmsBuildingLineListParameter,AnalysisEmsBuildingLineListResponse> {

    private AnalysisEmsBuildingLineListParameter parameter = new AnalysisEmsBuildingLineListParameter();
    private AnalysisEmsBuildingLineListResponse response = new AnalysisEmsBuildingLineListResponse();

    @EJB
    AnalysisEmsBuildingLineListDao AnalysisEmsBuildingLineListDao;

    @Override
    public AnalysisEmsBuildingLineListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingLineListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingLineListResponse execute() throws Exception {
        AnalysisEmsBuildingLineListParameter param = new AnalysisEmsBuildingLineListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingLineListResult result = AnalysisEmsBuildingLineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
