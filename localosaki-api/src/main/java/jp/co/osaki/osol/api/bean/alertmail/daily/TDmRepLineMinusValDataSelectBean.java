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
import jp.co.osaki.osol.api.dao.alertmail.daily.TDmRepLineMinusValDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepLineMinusValDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TDmRepLineMinusValDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepLineMinusValDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * マイナス値チェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TDmRepLineMinusValDataSelect")
@RequestScoped
public class TDmRepLineMinusValDataSelectBean extends OsolApiBean<TDmRepLineMinusValDataSelectParameter>
        implements
        BaseApiBean<TDmRepLineMinusValDataSelectParameter, TDmRepLineMinusValDataSelectResponse> {

    private TDmRepLineMinusValDataSelectParameter parameter = new TDmRepLineMinusValDataSelectParameter();

    private TDmRepLineMinusValDataSelectResponse response = new TDmRepLineMinusValDataSelectResponse();

    @EJB
    private TDmRepLineMinusValDataSelectDao tDmRepLineMinusValDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TDmRepLineMinusValDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TDmRepLineMinusValDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TDmRepLineMinusValDataSelectResponse execute() throws Exception {
        TDmRepLineMinusValDataSelectParameter param = new TDmRepLineMinusValDataSelectParameter();
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

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TDmRepLineMinusValDataSelectResult result = tDmRepLineMinusValDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
