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
import jp.co.osaki.osol.api.dao.alertmail.daily.TDmRepPointMaxLimitValDataSelectDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TDmRepPointMaxLimitValDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TDmRepPointMaxLimitValDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TDmRepPointMaxLimitValDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 上限値チェック Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TDmRepPointMaxLimitValDataSelect")
@RequestScoped
public class TDmRepPointMaxLimitValDataSelectBean extends OsolApiBean<TDmRepPointMaxLimitValDataSelectParameter>
        implements BaseApiBean<TDmRepPointMaxLimitValDataSelectParameter, TDmRepPointMaxLimitValDataSelectResponse> {

    private TDmRepPointMaxLimitValDataSelectParameter parameter = new TDmRepPointMaxLimitValDataSelectParameter();

    private TDmRepPointMaxLimitValDataSelectResponse response = new TDmRepPointMaxLimitValDataSelectResponse();

    @EJB
    private TDmRepPointMaxLimitValDataSelectDao tDmRepPointMaxLimitValDataSelectDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TDmRepPointMaxLimitValDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TDmRepPointMaxLimitValDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TDmRepPointMaxLimitValDataSelectResponse execute() throws Exception {
        TDmRepPointMaxLimitValDataSelectParameter param = new TDmRepPointMaxLimitValDataSelectParameter();
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

        TDmRepPointMaxLimitValDataSelectResult result = tDmRepPointMaxLimitValDataSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
