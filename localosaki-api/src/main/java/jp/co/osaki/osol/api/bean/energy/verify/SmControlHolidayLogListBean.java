package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlHolidayLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlHolidayLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlHolidayLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlHolidayLogListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御祝日設定履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlHolidayLogListBean")
@RequestScoped
public class SmControlHolidayLogListBean extends OsolApiBean<SmControlHolidayLogListParameter>
        implements BaseApiBean<SmControlHolidayLogListParameter, SmControlHolidayLogListResponse> {

    private SmControlHolidayLogListParameter parameter = new SmControlHolidayLogListParameter();

    private SmControlHolidayLogListResponse response = new SmControlHolidayLogListResponse();

    @EJB
    private SmControlHolidayLogListDao smControlHolidayLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlHolidayLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlHolidayLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlHolidayLogListResponse execute() throws Exception {
        SmControlHolidayLogListParameter param = new SmControlHolidayLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlHolidayLogId(this.parameter.getSmControlHolidayLogId());
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

        SmControlHolidayLogListResult result = smControlHolidayLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
