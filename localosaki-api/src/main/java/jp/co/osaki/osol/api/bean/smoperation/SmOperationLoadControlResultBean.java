package jp.co.osaki.osol.api.bean.smoperation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smoperation.SmOperationLoadControlResultDao;
import jp.co.osaki.osol.api.parameter.smoperation.SmOperationLoadControlResultParameter;
import jp.co.osaki.osol.api.response.smoperation.SmOperationLoadControlResultResponse;
import jp.co.osaki.osol.api.result.smoperation.SmOperationLoadControlResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御DB閲覧 負荷制御実績 Beanクラス
 *
 * @author t_hirata
 *
 */
@Named(value = "SmOperationLoadControlResultBean")
@RequestScoped
public class SmOperationLoadControlResultBean extends OsolApiBean<SmOperationLoadControlResultParameter>
        implements BaseApiBean<SmOperationLoadControlResultParameter, SmOperationLoadControlResultResponse> {

    private SmOperationLoadControlResultParameter parameter = new SmOperationLoadControlResultParameter();
    private SmOperationLoadControlResultResponse response = new SmOperationLoadControlResultResponse();

    @EJB
    SmOperationLoadControlResultDao smOperationLoadControlResultDao;

    @Override
    public SmOperationLoadControlResultParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(SmOperationLoadControlResultParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SmOperationLoadControlResultResponse execute() throws Exception {
        SmOperationLoadControlResultParameter param = new SmOperationLoadControlResultParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmId(this.parameter.getSmId());
        param.setControlTarget(this.parameter.getControlTarget());
        param.setTargetYm(this.parameter.getTargetYm());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SmOperationLoadControlResult result = smOperationLoadControlResultDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
