package jp.co.osaki.osol.api.bean.sample;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sample.SampleApiDao;
import jp.co.osaki.osol.api.parameter.sample.SampleApiParameter;
import jp.co.osaki.osol.api.response.sample.SampleApiResponse;
import jp.co.osaki.osol.api.result.sample.SampleApiResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * Sample Api Beanクラス.
 *
 * @author take_suzuki
 *
 */
@Named(value = "SampleApiBean")
@RequestScoped
public class SampleApiBean extends OsolApiBean<SampleApiParameter>
        implements BaseApiBean<SampleApiParameter, SampleApiResponse> {

    @EJB
    private SampleApiDao dao;

    /**
     * Parameter
     */
    private SampleApiParameter parameter = new SampleApiParameter();

    /**
     * response
     */
    private SampleApiResponse response = new SampleApiResponse();

    @Override
    public SampleApiParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(SampleApiParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public SampleApiResponse execute() throws Exception {
        SampleApiParameter param = new SampleApiParameter();

        copyOsolApiParameter(this.parameter, param);

        param.setArray(this.parameter.getArray());
        param.setGroupCode(this.parameter.getGroupCode());
        param.setCurlDate(this.parameter.getCurlDate());
        param.setRequest(this.parameter.getRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        SampleApiResult responseResult = new SampleApiResult();
        responseResult.setSampleApiResultList(dao.query(param));
        response.setResult(responseResult);
        response.setResultCode(OsolApiResultCode.API_OK);

        return response;
    }

}
