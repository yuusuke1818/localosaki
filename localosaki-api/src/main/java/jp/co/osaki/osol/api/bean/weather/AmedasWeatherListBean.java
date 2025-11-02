package jp.co.osaki.osol.api.bean.weather;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.weather.AmedasWeatherListDao;
import jp.co.osaki.osol.api.parameter.weather.AmedasWeatherListParameter;
import jp.co.osaki.osol.api.response.weather.AmedasWeatherListResponse;
import jp.co.osaki.osol.api.result.weather.AmedasWeatherListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * アメダス気象情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "AmedasWeatherListBean")
@RequestScoped
public class AmedasWeatherListBean extends OsolApiBean<AmedasWeatherListParameter>
    implements BaseApiBean<AmedasWeatherListParameter, AmedasWeatherListResponse>{

    private AmedasWeatherListParameter parameter = new AmedasWeatherListParameter();

    private AmedasWeatherListResponse response = new AmedasWeatherListResponse();

    @EJB
    AmedasWeatherListDao amedasWeatherListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public AmedasWeatherListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(AmedasWeatherListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public AmedasWeatherListResponse execute() throws Exception {
        AmedasWeatherListParameter param = new AmedasWeatherListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AmedasWeatherListResult result = amedasWeatherListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }



}
