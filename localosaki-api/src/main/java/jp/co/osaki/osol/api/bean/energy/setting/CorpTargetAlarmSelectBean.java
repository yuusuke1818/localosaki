package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpTargetAlarmSelectDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpTargetAlarmSelectParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpTargetAlarmSelectResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpTargetAlarmSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業目標超過警報取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "CorpTargetAlarmSelectBean")
@RequestScoped
public class CorpTargetAlarmSelectBean extends OsolApiBean<CorpTargetAlarmSelectParameter>
        implements BaseApiBean<CorpTargetAlarmSelectParameter, CorpTargetAlarmSelectResponse> {

    @EJB
    private CorpTargetAlarmSelectDao corpTargetAlarmSelectDao;

    private CorpTargetAlarmSelectParameter parameter = new CorpTargetAlarmSelectParameter();

    private CorpTargetAlarmSelectResponse response = new CorpTargetAlarmSelectResponse();

    /* (非 Javadoc)
    * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
    */
    @Override
    public CorpTargetAlarmSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpTargetAlarmSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public CorpTargetAlarmSelectResponse execute() throws Exception {
        CorpTargetAlarmSelectParameter param = new CorpTargetAlarmSelectParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpTargetAlarmSelectResult result = corpTargetAlarmSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
