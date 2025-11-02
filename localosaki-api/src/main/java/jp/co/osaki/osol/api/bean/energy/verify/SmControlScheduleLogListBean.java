package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlScheduleLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlScheduleLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleLogListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御スケジュール履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlScheduleLogListBean")
@RequestScoped
public class SmControlScheduleLogListBean extends OsolApiBean<SmControlScheduleLogListParameter>
        implements BaseApiBean<SmControlScheduleLogListParameter, SmControlScheduleLogListResponse> {

    private SmControlScheduleLogListParameter parameter = new SmControlScheduleLogListParameter();

    private SmControlScheduleLogListResponse response = new SmControlScheduleLogListResponse();

    @EJB
    private SmControlScheduleLogListDao smControlScheduleLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlScheduleLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlScheduleLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlScheduleLogListResponse execute() throws Exception {
        SmControlScheduleLogListParameter param = new SmControlScheduleLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlScheduleLogId(this.parameter.getSmControlScheduleLogId());
        param.setSmId(this.parameter.getSmId());
        param.setSettingUpdateDateTimeFrom(this.parameter.getSettingUpdateDateTimeFrom());
        param.setSettingUpdateDateTimeTo(this.parameter.getSettingUpdateDateTimeTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (param.getSettingUpdateDateTimeFrom() == null && param.getSettingUpdateDateTimeTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlScheduleLogListResult result = smControlScheduleLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
