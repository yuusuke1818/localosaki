package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlLoadVerifyListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlLoadVerifyListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlLoadVerifyListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlLoadVerifyListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御負荷検証取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlLoadVerifyListBean")
@RequestScoped
public class SmControlLoadVerifyListBean extends OsolApiBean<SmControlLoadVerifyListParameter>
        implements BaseApiBean<SmControlLoadVerifyListParameter, SmControlLoadVerifyListResponse> {

    private SmControlLoadVerifyListParameter parameter = new SmControlLoadVerifyListParameter();

    private SmControlLoadVerifyListResponse response = new SmControlLoadVerifyListResponse();

    @EJB
    private SmControlLoadVerifyListDao smControlLoadVerifyListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlLoadVerifyListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlLoadVerifyListParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SmControlLoadVerifyListResponse execute() throws Exception {
        SmControlLoadVerifyListParameter param = new SmControlLoadVerifyListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmControlLoadVerifyListResult result = smControlLoadVerifyListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
