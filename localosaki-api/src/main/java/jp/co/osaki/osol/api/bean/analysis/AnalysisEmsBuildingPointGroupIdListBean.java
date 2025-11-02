package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsBuildingPointGroupIdListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsBuildingPointGroupIdListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsBuildingPointGroupIdListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsBuildingPointGroupIdListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 建物別ポイントCSV実績値取得 Beanクラス
 *
 * @author yonezawa.a
 *
 */

@Named(value = "AnalysisEmsBuildingPointGroupIdListBean")
@RequestScoped
public class AnalysisEmsBuildingPointGroupIdListBean extends OsolApiBean<AnalysisEmsBuildingPointGroupIdListParameter>
            implements BaseApiBean<AnalysisEmsBuildingPointGroupIdListParameter,AnalysisEmsBuildingPointGroupIdListResponse> {

    private AnalysisEmsBuildingPointGroupIdListParameter parameter = new AnalysisEmsBuildingPointGroupIdListParameter();
    private AnalysisEmsBuildingPointGroupIdListResponse response = new AnalysisEmsBuildingPointGroupIdListResponse();

    @EJB
    AnalysisEmsBuildingPointGroupIdListDao analysisEmsBuildingPointGroupIdListDao;

    @Override
    public AnalysisEmsBuildingPointGroupIdListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsBuildingPointGroupIdListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsBuildingPointGroupIdListResponse execute() throws Exception {

        AnalysisEmsBuildingPointGroupIdListParameter param = new AnalysisEmsBuildingPointGroupIdListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setCorpId(parameter.getCorpId());
        param.setBuildingId(parameter.getBuildingId());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsBuildingPointGroupIdListResult result = analysisEmsBuildingPointGroupIdListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
