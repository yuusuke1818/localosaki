/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.ems;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.ems.DemandWeekCorpCalListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandWeekCorpCalListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandWeekCorpCalListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandWeekCorpCalListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド週報企業カレンダ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandWeekCorpCalListBean")
@RequestScoped
public class DemandWeekCorpCalListBean extends OsolApiBean<DemandWeekCorpCalListParameter>
        implements BaseApiBean<DemandWeekCorpCalListParameter, DemandWeekCorpCalListResponse> {

    private DemandWeekCorpCalListParameter parameter = new DemandWeekCorpCalListParameter();

    private DemandWeekCorpCalListResponse response = new DemandWeekCorpCalListResponse();

    @EJB
    DemandWeekCorpCalListDao demandWeekCorpCalListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandWeekCorpCalListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandWeekCorpCalListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandWeekCorpCalListResponse execute() throws Exception {
        DemandWeekCorpCalListParameter param = new DemandWeekCorpCalListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setFiscalYearFrom(this.parameter.getFiscalYearFrom());
        param.setFiscalYearTo(this.parameter.getFiscalYearTo());
        param.setWeekNoFrom(this.parameter.getWeekNoFrom());
        param.setWeekNoTo(this.parameter.getWeekNoTo());
        param.setBaseDate(this.parameter.getBaseDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DemandWeekCorpCalListResult result = demandWeekCorpCalListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
