package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpLineListUpdateDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpLineListUpdateParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpLineListUpdateResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpLineListUpdateResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業系統一覧更新 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "CorpLineListUpdateBean")
@RequestScoped
public class CorpLineListUpdateBean extends OsolApiBean<CorpLineListUpdateParameter>
        implements BaseApiBean<CorpLineListUpdateParameter, CorpLineListUpdateResponse> {

    @EJB
    private CorpLineListUpdateDao corpLineListUpdateDao;

    private CorpLineListUpdateParameter parameter = new CorpLineListUpdateParameter();

    private CorpLineListUpdateResponse response = new CorpLineListUpdateResponse();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CorpLineListUpdateParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpLineListUpdateParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public CorpLineListUpdateResponse execute() throws Exception {
        CorpLineListUpdateParameter param = new CorpLineListUpdateParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setResultSet(this.parameter.getResultSet());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpLineListUpdateResult result = corpLineListUpdateDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
