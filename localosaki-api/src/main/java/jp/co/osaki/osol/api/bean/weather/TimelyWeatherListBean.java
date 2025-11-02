package jp.co.osaki.osol.api.bean.weather;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.weather.TimelyWeatherListDao;
import jp.co.osaki.osol.api.parameter.weather.TimelyWeatherListParameter;
import jp.co.osaki.osol.api.response.weather.TimelyWeatherListResponse;
import jp.co.osaki.osol.api.result.weather.TimelyWeatherListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 時間別天気情報取得 Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "TimelyWeatherListBean")
@RequestScoped
public class TimelyWeatherListBean extends OsolApiBean<TimelyWeatherListParameter>
    implements BaseApiBean<TimelyWeatherListParameter, TimelyWeatherListResponse>{

    private TimelyWeatherListParameter parameter = new TimelyWeatherListParameter();

    private TimelyWeatherListResponse response = new TimelyWeatherListResponse();

    @EJB
    TimelyWeatherListDao timelyWeatherListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TimelyWeatherListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TimelyWeatherListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TimelyWeatherListResponse execute() throws Exception {
        TimelyWeatherListParameter param = new TimelyWeatherListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TimelyWeatherListResult result = timelyWeatherListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
