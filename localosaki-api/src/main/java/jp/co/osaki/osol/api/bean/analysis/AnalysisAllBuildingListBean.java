package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisAllBuildingListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisAllBuildingListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisAllBuildingListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisAllBuildingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 *集計分析 EMS実績 対象建物取得(全建物) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisAllBuildingListBean")
@RequestScoped
public class AnalysisAllBuildingListBean extends OsolApiBean<AnalysisAllBuildingListParameter>
            implements BaseApiBean<AnalysisAllBuildingListParameter,AnalysisAllBuildingListResponse> {

    private AnalysisAllBuildingListParameter parameter = new AnalysisAllBuildingListParameter();
    private AnalysisAllBuildingListResponse response = new AnalysisAllBuildingListResponse();

    @EJB
    AnalysisAllBuildingListDao analysisAllBuildingListDao;

    @Override
    public AnalysisAllBuildingListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisAllBuildingListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisAllBuildingListResponse execute() throws Exception {
        AnalysisAllBuildingListParameter param = new AnalysisAllBuildingListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisAllBuildingListResult result = analysisAllBuildingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
