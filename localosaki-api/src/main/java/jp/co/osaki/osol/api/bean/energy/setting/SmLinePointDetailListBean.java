package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.SmLinePointDetailListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.SmLinePointDetailListParameter;
import jp.co.osaki.osol.api.response.energy.setting.SmLinePointDetailListResponse;
import jp.co.osaki.osol.api.result.energy.setting.SmLinePointDetailListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統ポイント情報取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "SmLinePointDetailListBean")
@RequestScoped
public class SmLinePointDetailListBean extends OsolApiBean<SmLinePointDetailListParameter>
        implements BaseApiBean<SmLinePointDetailListParameter, SmLinePointDetailListResponse> {

    private SmLinePointDetailListParameter parameter = new SmLinePointDetailListParameter();

    private SmLinePointDetailListResponse response = new SmLinePointDetailListResponse();

    @EJB
    SmLinePointDetailListDao ｓmLinePointDetailListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLinePointDetailListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLinePointDetailListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLinePointDetailListResponse execute() throws Exception {
        SmLinePointDetailListParameter param = new SmLinePointDetailListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpId(this.parameter.getCorpId());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setSmId(this.parameter.getSmId());
        param.setPointNo(this.parameter.getPointNo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLinePointDetailListResult result = ｓmLinePointDetailListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
