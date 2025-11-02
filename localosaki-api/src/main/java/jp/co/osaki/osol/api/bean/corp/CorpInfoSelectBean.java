package jp.co.osaki.osol.api.bean.corp;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.corp.CorpInfoSelectDao;
import jp.co.osaki.osol.api.parameter.corp.CorpInfoSelectParameter;
import jp.co.osaki.osol.api.response.corp.CorpInfoSelectResponse;
import jp.co.osaki.osol.api.result.corp.CorpInfoSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

@Named(value = "CorpInfoSelectBean")
@RequestScoped
public class CorpInfoSelectBean extends OsolApiBean<CorpInfoSelectParameter>
        implements BaseApiBean<CorpInfoSelectParameter, CorpInfoSelectResponse> {

    private CorpInfoSelectParameter parameter = new CorpInfoSelectParameter();

    private CorpInfoSelectResponse response = new CorpInfoSelectResponse();

    @EJB
    CorpInfoSelectDao corpInfoSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public CorpInfoSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(CorpInfoSelectParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public CorpInfoSelectResponse execute() throws Exception {
        CorpInfoSelectParameter param = new CorpInfoSelectParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        CorpInfoSelectResult result = corpInfoSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
