package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.SmLinePointCountDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointCountParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmLinePointCountResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointCountResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統ポイント件数取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "SmLinePointCountBean")
@RequestScoped
public class SmLinePointCountBean extends OsolApiBean<SmLinePointCountParameter>
        implements BaseApiBean<SmLinePointCountParameter, SmLinePointCountResponse> {

    private SmLinePointCountParameter parameter = new SmLinePointCountParameter();

    private SmLinePointCountResponse response = new SmLinePointCountResponse();

    @EJB
    SmLinePointCountDao smLinePointCountDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLinePointCountParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLinePointCountParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLinePointCountResponse execute() throws Exception {
        SmLinePointCountParameter param = new SmLinePointCountParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLinePointCountResult result = smLinePointCountDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
