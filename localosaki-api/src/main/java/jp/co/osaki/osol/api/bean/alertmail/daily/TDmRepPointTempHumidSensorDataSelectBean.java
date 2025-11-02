/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.alertmail.daily;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.alertmail.daily.TDmRepPointTempHumidSensorDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepPointTempHumidSensorDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TDmRepPointTempHumidSensorDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepPointTempHumidSensorDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 温湿度センサーチェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TDmRepPointTempHumidSensorDataSelect")
@RequestScoped
public class TDmRepPointTempHumidSensorDataSelectBean extends OsolApiBean<TDmRepPointTempHumidSensorDataSelectParameter>
        implements
        BaseApiBean<TDmRepPointTempHumidSensorDataSelectParameter, TDmRepPointTempHumidSensorDataSelectResponse> {

    private TDmRepPointTempHumidSensorDataSelectParameter parameter = new TDmRepPointTempHumidSensorDataSelectParameter();

    private TDmRepPointTempHumidSensorDataSelectResponse response = new TDmRepPointTempHumidSensorDataSelectResponse();

    @EJB
    private TDmRepPointTempHumidSensorDataSelectDao tDmRepPointTempHumidSensorDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TDmRepPointTempHumidSensorDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TDmRepPointTempHumidSensorDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TDmRepPointTempHumidSensorDataSelectResponse execute() throws Exception {
        TDmRepPointTempHumidSensorDataSelectParameter param = new TDmRepPointTempHumidSensorDataSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpName(this.parameter.getCorpName());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setBuildingName(this.parameter.getBuildingName());
        param.setSmId(this.parameter.getSmId());
        param.setSmAddress(this.parameter.getSmAddress());
        param.setIpAddress(this.parameter.getIpAddress());
        param.setNowDate(DateUtility.plusDay(this.parameter.getNowDate(), -1));
        // 過去一週間が対象
        param.setTargetDate(DateUtility.plusDay(this.parameter.getNowDate(), -7));
        param.setThreshold(this.parameter.getThreshold());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TDmRepPointTempHumidSensorDataSelectResult result = tDmRepPointTempHumidSensorDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
