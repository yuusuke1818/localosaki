package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsSmListDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsSmListParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsSmListResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsSmListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 *集計・分析 EMS実績 対象機器取得 Beanクラス
 *
 * @author y-maruta
 *
 */

@Named(value = "AnalysisEmsSmListBean")
@RequestScoped
public class AnalysisEmsSmListBean extends OsolApiBean<AnalysisEmsSmListParameter>
            implements BaseApiBean<AnalysisEmsSmListParameter,AnalysisEmsSmListResponse> {

    private AnalysisEmsSmListParameter parameter = new AnalysisEmsSmListParameter();
    private AnalysisEmsSmListResponse response = new AnalysisEmsSmListResponse();

    @EJB
    AnalysisEmsSmListDao AnalysisEmsSmListDao;

    @Override
    public AnalysisEmsSmListParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsSmListParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsSmListResponse execute() throws Exception {
        AnalysisEmsSmListParameter param = new AnalysisEmsSmListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsSmListResult result = AnalysisEmsSmListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
