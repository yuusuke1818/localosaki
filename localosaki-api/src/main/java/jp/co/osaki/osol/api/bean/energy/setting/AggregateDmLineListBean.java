package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.AggregateDmLineListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.AggregateDmLineListParameter;
import jp.co.osaki.osol.api.response.energy.setting.AggregateDmLineListResponse;
import jp.co.osaki.osol.api.result.energy.setting.AggregateDmLineListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計デマンド系統情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AggregateDmLineListBean")
@RequestScoped
public class AggregateDmLineListBean extends OsolApiBean<AggregateDmLineListParameter>
        implements BaseApiBean<AggregateDmLineListParameter, AggregateDmLineListResponse> {

    private AggregateDmLineListParameter parameter = new AggregateDmLineListParameter();

    private AggregateDmLineListResponse response = new AggregateDmLineListResponse();

    @EJB
    AggregateDmLineListDao aggregateDmLineListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AggregateDmLineListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AggregateDmLineListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public AggregateDmLineListResponse execute() throws Exception {
        AggregateDmLineListParameter param = new AggregateDmLineListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setAggregateDmCorpId(this.parameter.getAggregateDmCorpId());
        param.setAggregateDmBuildingId(this.parameter.getAggregateDmBuildingId());
        param.setAggregateDmId(this.parameter.getAggregateDmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (!CheckUtility.isNullOrEmpty(param.getLineNo()) && param.getLineGroupId() == null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        AggregateDmLineListResult result = aggregateDmLineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
