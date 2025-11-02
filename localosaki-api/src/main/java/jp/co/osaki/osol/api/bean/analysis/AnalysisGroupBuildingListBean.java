package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisGroupBuildingListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisGroupBuildingListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisGroupBuildingListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisGroupBuildingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 *集計分析 EMS実績 対象建物取得(グループ指定) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisGroupBuildingListBean")
@RequestScoped
public class AnalysisGroupBuildingListBean extends OsolApiBean<AnalysisGroupBuildingListParameter>
            implements BaseApiBean<AnalysisGroupBuildingListParameter,AnalysisGroupBuildingListResponse> {

    private AnalysisGroupBuildingListParameter parameter = new AnalysisGroupBuildingListParameter();
    private AnalysisGroupBuildingListResponse response = new AnalysisGroupBuildingListResponse();

    @EJB
    AnalysisGroupBuildingListDao AnalysisGroupBuildingListDao;

    @Override
    public AnalysisGroupBuildingListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisGroupBuildingListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisGroupBuildingListResponse execute() throws Exception {
        AnalysisGroupBuildingListParameter param = new AnalysisGroupBuildingListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisGroupBuildingListResult result = AnalysisGroupBuildingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
