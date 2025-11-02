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
import jp.co.osaki.osol.api.dao.energy.ems.DemandYearCorpCalListDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DemandYearCorpCalListParameter;
import jp.co.osaki.osol.api.response.energy.ems.DemandYearCorpCalListResponse;
import jp.co.osaki.osol.api.result.energy.ems.DemandYearCorpCalListResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド年報企業カレンダ取得 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DemandYearCorpCalListBean")
@RequestScoped
public class DemandYearCorpCalListBean extends OsolApiBean<DemandYearCorpCalListParameter>
        implements BaseApiBean<DemandYearCorpCalListParameter, DemandYearCorpCalListResponse> {

    private DemandYearCorpCalListParameter parameter = new DemandYearCorpCalListParameter();

    private DemandYearCorpCalListResponse response = new DemandYearCorpCalListResponse();

    @EJB
    DemandYearCorpCalListDao demandYearCorpCalListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DemandYearCorpCalListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DemandYearCorpCalListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DemandYearCorpCalListResponse execute() throws Exception {
        DemandYearCorpCalListParameter param = new DemandYearCorpCalListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setYearNoFrom(this.parameter.getYearNoFrom());
        param.setYearNoTo(this.parameter.getYearNoTo());
        param.setCalYmFrom(this.parameter.getCalYmFrom());
        param.setCalYmTo(this.parameter.getCalYmTo());
        param.setBaseDate(this.parameter.getBaseDate());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //FromToのチェック
        if (!CheckUtility.isNullOrEmpty(param.getYearNoTo()) && CheckUtility.isNullOrEmpty(param.getYearNoFrom())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }
        if (!CheckUtility.isNullOrEmpty(param.getCalYmTo()) && CheckUtility.isNullOrEmpty(param.getCalYmFrom())) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        DemandYearCorpCalListResult result = demandYearCorpCalListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
