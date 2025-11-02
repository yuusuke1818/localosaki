package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.AggregateDmListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.AggregateDmListParameter;
import jp.co.osaki.osol.api.response.energy.setting.AggregateDmListResponse;
import jp.co.osaki.osol.api.result.energy.setting.AggregateDmListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計デマンド情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AggregateDmListBean")
@RequestScoped
public class AggregateDmListBean extends OsolApiBean<AggregateDmListParameter>
        implements BaseApiBean<AggregateDmListParameter, AggregateDmListResponse> {

    private AggregateDmListParameter parameter = new AggregateDmListParameter();

    private AggregateDmListResponse response = new AggregateDmListResponse();

    @EJB
    AggregateDmListDao aggregateDmListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AggregateDmListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AggregateDmListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AggregateDmListResponse execute() throws Exception {
        AggregateDmListParameter param = new AggregateDmListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setAggregateDmId(this.parameter.getAggregateDmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AggregateDmListResult result = aggregateDmListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
