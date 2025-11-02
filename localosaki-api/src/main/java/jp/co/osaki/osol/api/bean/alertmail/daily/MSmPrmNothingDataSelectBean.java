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
import jp.co.osaki.osol.api.dao.alertmail.daily.MSmPrmNothingDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.MSmPrmNothingDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.MSmPrmNothingDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.MSmPrmNothingDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * データなしチェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "MSmPrmNothingDataSelect")
@RequestScoped
public class MSmPrmNothingDataSelectBean extends OsolApiBean<MSmPrmNothingDataSelectParameter>
        implements BaseApiBean<MSmPrmNothingDataSelectParameter, MSmPrmNothingDataSelectResponse> {

    private MSmPrmNothingDataSelectParameter parameter = new MSmPrmNothingDataSelectParameter();

    private MSmPrmNothingDataSelectResponse response = new MSmPrmNothingDataSelectResponse();

    @EJB
    MSmPrmNothingDataSelectDao mSmPrmNothingDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public MSmPrmNothingDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(MSmPrmNothingDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public MSmPrmNothingDataSelectResponse execute() throws Exception {
        MSmPrmNothingDataSelectParameter param = new MSmPrmNothingDataSelectParameter();
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

        MSmPrmNothingDataSelectResult result = mSmPrmNothingDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
