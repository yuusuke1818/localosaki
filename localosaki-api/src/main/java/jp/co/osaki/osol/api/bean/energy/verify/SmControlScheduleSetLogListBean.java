package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlScheduleSetLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleSetLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlScheduleSetLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleSetLogListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御スケジュール設定履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlScheduleSetLogListBean")
@RequestScoped
public class SmControlScheduleSetLogListBean extends OsolApiBean<SmControlScheduleSetLogListParameter>
        implements BaseApiBean<SmControlScheduleSetLogListParameter, SmControlScheduleSetLogListResponse> {

    private SmControlScheduleSetLogListParameter parameter = new SmControlScheduleSetLogListParameter();

    private SmControlScheduleSetLogListResponse response = new SmControlScheduleSetLogListResponse();

    @EJB
    private SmControlScheduleSetLogListDao smControlScheduleSetLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlScheduleSetLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlScheduleSetLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlScheduleSetLogListResponse execute() throws Exception {
        SmControlScheduleSetLogListParameter param = new SmControlScheduleSetLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlScheduleLogId(this.parameter.getSmControlScheduleLogId());
        param.setSmId(this.parameter.getSmId());
        param.setControlLoadFrom(this.parameter.getControlLoadFrom());
        param.setControlLoadTo(this.parameter.getControlLoadTo());
        param.setTargetMonthFrom(this.parameter.getTargetMonthFrom());
        param.setTargetMonthTo(this.parameter.getTargetMonthTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (param.getControlLoadFrom() == null && param.getControlLoadTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        if (param.getTargetMonthFrom() == null && param.getTargetMonthTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlScheduleSetLogListResult result = smControlScheduleSetLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
