package jp.co.osaki.osol.api.bean.osolapi;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.osolapi.OsolApiServerDateTimeDao;
import jp.co.osaki.osol.api.parameter.osolapi.OsolApiServerDateTimeParameter;
import jp.co.osaki.osol.api.response.osolapi.OsolApiServerDateTimeResponse;
import jp.co.osaki.osol.api.result.osolapi.OsolApiServerDateTimeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * DBサーバー時刻取得 Beanクラス.
 *
 * @author take_suzuki
 *
 */
@Named(value = "OsolApiServerDateTimeBean")
@RequestScoped
public class OsolApiServerDateTimeBean extends OsolApiBean<OsolApiServerDateTimeParameter>
        implements BaseApiBean<OsolApiServerDateTimeParameter, OsolApiServerDateTimeResponse> {

    @EJB
    private OsolApiServerDateTimeDao dao;

    private OsolApiServerDateTimeResponse response = new OsolApiServerDateTimeResponse();

    private OsolApiServerDateTimeParameter parameter = new OsolApiServerDateTimeParameter();

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public OsolApiServerDateTimeParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(OsolApiServerDateTimeParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public OsolApiServerDateTimeResponse execute() throws Exception {
        this.response.setResult(new OsolApiServerDateTimeResult());
        this.response.setResultCode(OsolApiResultCode.API_OK);
        return response;
    }

}
