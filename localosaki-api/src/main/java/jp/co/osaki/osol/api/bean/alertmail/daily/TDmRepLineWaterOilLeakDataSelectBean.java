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
import jp.co.osaki.osol.api.dao.alertmail.daily.TDmRepLineWaterOilLeakDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepLineWaterOilLeakDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TDmRepLineWaterOilLeakDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepLineWaterOilLeakDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 水・油漏れチェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TDmRepLineWaterOilLeakDataSelect")
@RequestScoped
public class TDmRepLineWaterOilLeakDataSelectBean extends OsolApiBean<TDmRepLineWaterOilLeakDataSelectParameter>
        implements
        BaseApiBean<TDmRepLineWaterOilLeakDataSelectParameter, TDmRepLineWaterOilLeakDataSelectResponse> {

    private TDmRepLineWaterOilLeakDataSelectParameter parameter = new TDmRepLineWaterOilLeakDataSelectParameter();

    private TDmRepLineWaterOilLeakDataSelectResponse response = new TDmRepLineWaterOilLeakDataSelectResponse();

    @EJB
    private TDmRepLineWaterOilLeakDataSelectDao tDmRepLineWaterOilLeakDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TDmRepLineWaterOilLeakDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TDmRepLineWaterOilLeakDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TDmRepLineWaterOilLeakDataSelectResponse execute() throws Exception {
        TDmRepLineWaterOilLeakDataSelectParameter param = new TDmRepLineWaterOilLeakDataSelectParameter();
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
        param.setChkLineValueKw(this.parameter.getChkLineValueKw());
        param.setChkJigenNoFrom(this.parameter.getChkJigenNoFrom());
        param.setChkJigenNoTo(this.parameter.getChkJigenNoTo());
        param.setChkLineType(this.parameter.getChkLineType());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TDmRepLineWaterOilLeakDataSelectResult result = tDmRepLineWaterOilLeakDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
