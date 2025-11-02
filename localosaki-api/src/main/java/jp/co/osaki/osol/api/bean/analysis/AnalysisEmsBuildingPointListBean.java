package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingPointListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別ポイント実績値取得 Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsBuildingPointListBean")
@RequestScoped
public class AnalysisEmsBuildingPointListBean extends OsolApiBean<AnalysisEmsBuildingPointListParameter>
            implements BaseApiBean<AnalysisEmsBuildingPointListParameter,AnalysisEmsBuildingPointListResponse> {

    private AnalysisEmsBuildingPointListParameter parameter = new AnalysisEmsBuildingPointListParameter();
    private AnalysisEmsBuildingPointListResponse response = new AnalysisEmsBuildingPointListResponse();

    @EJB
    AnalysisEmsBuildingPointListDao AnalysisEmsBuildingPointListDao;

    @Override
    public AnalysisEmsBuildingPointListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingPointListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingPointListResponse execute() throws Exception {
        AnalysisEmsBuildingPointListParameter param = new AnalysisEmsBuildingPointListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingPointListResult result = AnalysisEmsBuildingPointListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
