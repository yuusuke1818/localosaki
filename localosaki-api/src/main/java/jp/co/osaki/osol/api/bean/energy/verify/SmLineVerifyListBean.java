package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmLineVerifyListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmLineVerifyListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmLineVerifyListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmLineVerifyListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統検証 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "SmLineVerifyListBean")
@RequestScoped
public class SmLineVerifyListBean extends OsolApiBean<SmLineVerifyListParameter>
        implements BaseApiBean<SmLineVerifyListParameter, SmLineVerifyListResponse> {

    private SmLineVerifyListParameter parameter = new SmLineVerifyListParameter();

    private SmLineVerifyListResponse response = new SmLineVerifyListResponse();

    @EJB
    private SmLineVerifyListDao kensyoEventSmLineVerifyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLineVerifyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLineVerifyListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLineVerifyListResponse execute() throws Exception {
        SmLineVerifyListParameter param = new SmLineVerifyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLineVerifyListResult result = kensyoEventSmLineVerifyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
