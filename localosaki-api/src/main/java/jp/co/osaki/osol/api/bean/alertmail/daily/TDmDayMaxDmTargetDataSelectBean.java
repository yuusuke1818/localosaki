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
import jp.co.osaki.osol.api.dao.alertmail.daily.TDmDayMaxDmTargetDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmDayMaxDmTargetDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TDmDayMaxDmTargetDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmDayMaxDmTargetDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド目標超過チェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TDmDayMaxDmTargetDataSelect")
@RequestScoped
public class TDmDayMaxDmTargetDataSelectBean extends OsolApiBean<TDmDayMaxDmTargetDataSelectParameter>
        implements BaseApiBean<TDmDayMaxDmTargetDataSelectParameter, TDmDayMaxDmTargetDataSelectResponse> {

    private TDmDayMaxDmTargetDataSelectParameter parameter = new TDmDayMaxDmTargetDataSelectParameter();

    private TDmDayMaxDmTargetDataSelectResponse response = new TDmDayMaxDmTargetDataSelectResponse();

    @EJB
    private TDmDayMaxDmTargetDataSelectDao tDmDayMaxDmTargetDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TDmDayMaxDmTargetDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TDmDayMaxDmTargetDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TDmDayMaxDmTargetDataSelectResponse execute() throws Exception {
        TDmDayMaxDmTargetDataSelectParameter param = new TDmDayMaxDmTargetDataSelectParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setCorpName(this.parameter.getCorpName());
        param.setBuildingId(this.parameter.getBuildingId());
        param.setBuildingNo(this.parameter.getBuildingNo());
        param.setBuildingName(this.parameter.getBuildingName());
        param.setSmId(this.parameter.getSmId());
        param.setSmAddress(this.parameter.getSmAddress());
        param.setIpAddress(this.parameter.getIpAddress());
        param.setNowDate(DateUtility.plusDay(this.parameter.getNowDate(), -1));
        // 過去一週間分が対象
        param.setTargetDate(DateUtility.plusDay(this.parameter.getNowDate(), -7));

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TDmDayMaxDmTargetDataSelectResult result = tDmDayMaxDmTargetDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
