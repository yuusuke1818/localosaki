package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisBuildingIdListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisBuildingIdListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisBuildingIdListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisBuildingIdListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 *集計分析 EMS実績 対象建物取得(建物ID指定) Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisBuildingIdListBean")
@RequestScoped
public class AnalysisBuildingIdListBean extends OsolApiBean<AnalysisBuildingIdListParameter>
            implements BaseApiBean<AnalysisBuildingIdListParameter,AnalysisBuildingIdListResponse> {

    private AnalysisBuildingIdListParameter parameter = new AnalysisBuildingIdListParameter();
    private AnalysisBuildingIdListResponse response = new AnalysisBuildingIdListResponse();

    @EJB
    AnalysisBuildingIdListDao AnalysisBuildingIdListDao;

    @Override
    public AnalysisBuildingIdListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisBuildingIdListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisBuildingIdListResponse execute() throws Exception {
        AnalysisBuildingIdListParameter param = new AnalysisBuildingIdListParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        if(parameter.getBelongTenantFlg() == null) {
            param.setBelongTenantFlg(Boolean.FALSE);
        } else {
            param.setBelongTenantFlg(parameter.getBelongTenantFlg());
        }

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisBuildingIdListResult result = AnalysisBuildingIdListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
