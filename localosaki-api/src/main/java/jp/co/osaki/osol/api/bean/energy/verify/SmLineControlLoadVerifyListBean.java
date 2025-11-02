package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmLineControlLoadVerifyListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmLineControlLoadVerifyListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmLineControlLoadVerifyListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmLineControlLoadVerifyListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器系統制御負荷検証取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmLineControlLoadVerifyListBean")
@RequestScoped
public class SmLineControlLoadVerifyListBean extends OsolApiBean<SmLineControlLoadVerifyListParameter>
        implements BaseApiBean<SmLineControlLoadVerifyListParameter, SmLineControlLoadVerifyListResponse> {

    private SmLineControlLoadVerifyListParameter parameter = new SmLineControlLoadVerifyListParameter();

    private SmLineControlLoadVerifyListResponse response = new SmLineControlLoadVerifyListResponse();

    @EJB
    private SmLineControlLoadVerifyListDao smLineControlLoadVerifyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmLineControlLoadVerifyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmLineControlLoadVerifyListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmLineControlLoadVerifyListResponse execute() throws Exception {
        SmLineControlLoadVerifyListParameter param = new SmLineControlLoadVerifyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmLineControlLoadVerifyListResult result = smLineControlLoadVerifyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
