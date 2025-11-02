package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointInfoCsvListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointInfoCsvListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingPointInfoCsvListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointInfoCsvListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Beanクラス
 *
 * @author yonezawa.a
 *
 */

@Named(value = "AnalysisEmsBuildingPointInfoCsvListBean")
@RequestScoped
public class AnalysisEmsBuildingPointInfoCsvListBean extends OsolApiBean<AnalysisEmsBuildingPointInfoCsvListParameter>
            implements BaseApiBean<AnalysisEmsBuildingPointInfoCsvListParameter,AnalysisEmsBuildingPointInfoCsvListResponse> {

    private AnalysisEmsBuildingPointInfoCsvListParameter parameter = new AnalysisEmsBuildingPointInfoCsvListParameter();
    private AnalysisEmsBuildingPointInfoCsvListResponse response = new AnalysisEmsBuildingPointInfoCsvListResponse();

    @EJB
    AnalysisEmsBuildingPointInfoCsvListDao AnalysisEmsBuildingPointInfoCsvListDao;

    @Override
    public AnalysisEmsBuildingPointInfoCsvListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingPointInfoCsvListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingPointInfoCsvListResponse execute() throws Exception {
        AnalysisEmsBuildingPointInfoCsvListParameter param = new AnalysisEmsBuildingPointInfoCsvListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        param.setSmId(parameter.getSmId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingPointInfoCsvListResult result = AnalysisEmsBuildingPointInfoCsvListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
