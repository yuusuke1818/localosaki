package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.verify.SmControlScheduleDutyLogListDao;
import jp.co.osaki.osol.api.parameter.energy.verify.SmControlScheduleDutyLogListParameter;
import jp.co.osaki.osol.api.response.energy.verify.SmControlScheduleDutyLogListResponse;
import jp.co.osaki.osol.api.result.energy.verify.SmControlScheduleDutyLogListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 機器制御スケジュールデューティ履歴取得 Beanクラス
 * @author ya-ishida
 *
 */
public class SmControlScheduleDutyLogListBean extends OsolApiBean<SmControlScheduleDutyLogListParameter>
        implements BaseApiBean<SmControlScheduleDutyLogListParameter, SmControlScheduleDutyLogListResponse> {

    private SmControlScheduleDutyLogListParameter parameter = new SmControlScheduleDutyLogListParameter();

    private SmControlScheduleDutyLogListResponse response = new SmControlScheduleDutyLogListResponse();

    @EJB
    private SmControlScheduleDutyLogListDao smControlScheduleDutyLogListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public SmControlScheduleDutyLogListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(SmControlScheduleDutyLogListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public SmControlScheduleDutyLogListResponse execute() throws Exception {
        SmControlScheduleDutyLogListParameter param = new SmControlScheduleDutyLogListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSmControlScheduleLogId(this.parameter.getSmControlScheduleLogId());
        param.setSmId(this.parameter.getSmId());
        param.setPatternNoFrom(this.parameter.getPatternNoFrom());
        param.setPatternNoTo(this.parameter.getPatternNoTo());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        if (CheckUtility.isNullOrEmpty(param.getPatternNoFrom())
                && !CheckUtility.isNullOrEmpty(param.getPatternNoTo())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        SmControlScheduleDutyLogListResult result = smControlScheduleDutyLogListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
