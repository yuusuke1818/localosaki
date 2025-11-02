package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.SmLinePointListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointListParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmLinePointListResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統ポイント情報取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "SmLinePointListBean")
@RequestScoped
public class SmLinePointListBean extends OsolApiBean<SmLinePointListParameter>
        implements BaseApiBean<SmLinePointListParameter, SmLinePointListResponse> {

    private SmLinePointListParameter parameter = new SmLinePointListParameter();

    private SmLinePointListResponse response = new SmLinePointListResponse();

    @EJB
    SmLinePointListDao smLinePointListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLinePointListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLinePointListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLinePointListResponse execute() throws Exception {
        SmLinePointListParameter param = new SmLinePointListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setPointNo(this.parameter.getPointNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLinePointListResult result = smLinePointListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
