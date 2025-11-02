package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlScheduleTimeLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleTimeLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlScheduleTimeLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleTimeLogListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御スケジュール時間帯履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlScheduleTimeLogListBean")
@RequestScoped
public class SmControlScheduleTimeLogListBean extends OsolApiBean<SmControlScheduleTimeLogListParameter>
        implements BaseApiBean<SmControlScheduleTimeLogListParameter, SmControlScheduleTimeLogListResponse> {

    private SmControlScheduleTimeLogListParameter parameter = new SmControlScheduleTimeLogListParameter();

    private SmControlScheduleTimeLogListResponse response = new SmControlScheduleTimeLogListResponse();

    @EJB
    private SmControlScheduleTimeLogListDao smControlScheduleTimeLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlScheduleTimeLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlScheduleTimeLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlScheduleTimeLogListResponse execute() throws Exception {
        SmControlScheduleTimeLogListParameter param = new SmControlScheduleTimeLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlScheduleLogId(this.parameter.getSmControlScheduleLogId());
        param.setSmId(this.parameter.getSmId());
        param.setPatternNoFrom(this.parameter.getPatternNoFrom());
        param.setPatternNoTo(this.parameter.getPatternNoTo());
        param.setTimeSlotNoFrom(this.parameter.getTimeSlotNoFrom());
        param.setTimeSlotNoTo(this.parameter.getTimeSlotNoTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (CheckUtility.isNullOrEmpty(param.getPatternNoFrom())
                && !CheckUtility.isNullOrEmpty(param.getPatternNoTo())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        if (param.getTimeSlotNoFrom() == null && param.getTimeSlotNoTo() != null) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlScheduleTimeLogListResult result = smControlScheduleTimeLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
