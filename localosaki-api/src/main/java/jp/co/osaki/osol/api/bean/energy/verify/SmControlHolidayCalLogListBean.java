package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlHolidayCalLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlHolidayCalLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlHolidayCalLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlHolidayCalLogListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御祝日設定カレンダ履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "SmControlHolidayCalLogListBean")
@RequestScoped
public class SmControlHolidayCalLogListBean extends OsolApiBean<SmControlHolidayCalLogListParameter>
        implements BaseApiBean<SmControlHolidayCalLogListParameter, SmControlHolidayCalLogListResponse> {

    private SmControlHolidayCalLogListParameter parameter = new SmControlHolidayCalLogListParameter();

    private SmControlHolidayCalLogListResponse response = new SmControlHolidayCalLogListResponse();

    @EJB
    private SmControlHolidayCalLogListDao smControlHolidayCalLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlHolidayCalLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlHolidayCalLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlHolidayCalLogListResponse execute() throws Exception {
        SmControlHolidayCalLogListParameter param = new SmControlHolidayCalLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlHolidayLogId(this.parameter.getSmControlHolidayLogId());
        param.setSmId(this.parameter.getSmId());
        param.setHolidayMmDdFrom(this.parameter.getHolidayMmDdFrom());
        param.setHolidayMmDdTo(this.parameter.getHolidayMmDdTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (CheckUtility.isNullOrEmpty(param.getHolidayMmDdFrom())
                && !CheckUtility.isNullOrEmpty(param.getHolidayMmDdTo())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlHolidayCalLogListResult result = smControlHolidayCalLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
