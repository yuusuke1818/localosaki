package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.CorpLineListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpLineListParameter;
import jp.co.osaki.osol.api.response.energy.setting.CorpLineListResponse;
import jp.co.osaki.osol.api.result.energy.setting.CorpLineListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 企業系統一覧取得 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "CorpLineListBean")
@RequestScoped
public class CorpLineListBean extends OsolApiBean<CorpLineListParameter>
        implements BaseApiBean<CorpLineListParameter, CorpLineListResponse> {

    @EJB
    private CorpLineListDao corpLineListDao;

    private CorpLineListParameter parameter = new CorpLineListParameter();

    private CorpLineListResponse response = new CorpLineListResponse();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CorpLineListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpLineListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public CorpLineListResponse execute() throws Exception {
        CorpLineListParameter param = new CorpLineListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpLineListResult result = corpLineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }
}
