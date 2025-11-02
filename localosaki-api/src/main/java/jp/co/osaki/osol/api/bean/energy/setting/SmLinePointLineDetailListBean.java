package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.SmLinePointLineDetailListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointLineDetailListParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmLinePointLineDetailListResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointLineDetailListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統ポイント情報取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "SmLinePointLineDetailListBean")
@RequestScoped
public class SmLinePointLineDetailListBean extends OsolApiBean<SmLinePointLineDetailListParameter>
        implements BaseApiBean<SmLinePointLineDetailListParameter, SmLinePointLineDetailListResponse> {

    private SmLinePointLineDetailListParameter parameter = new SmLinePointLineDetailListParameter();

    private SmLinePointLineDetailListResponse response = new SmLinePointLineDetailListResponse();

    @EJB
    SmLinePointLineDetailListDao SmLinePointLineDetailListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLinePointLineDetailListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLinePointLineDetailListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLinePointLineDetailListResponse execute() throws Exception {
        SmLinePointLineDetailListParameter param = new SmLinePointLineDetailListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setPointNo(this.parameter.getPointNo());
        param.setInputEnableFlg(this.parameter.getInputEnableFlg());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLinePointLineDetailListResult result = SmLinePointLineDetailListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
