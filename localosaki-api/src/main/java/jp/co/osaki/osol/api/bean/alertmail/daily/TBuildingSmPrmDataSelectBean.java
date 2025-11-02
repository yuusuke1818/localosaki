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
import jp.co.osaki.osol.api.dao.alertmail.daily.TBuildingSmPrmDataSelectServiceDao;
import jp.co.osaki.osol.api.parameter.alertmail.daily.TBuildingSmPrmDataSelectParameter;
import jp.co.osaki.osol.api.response.alertmail.daily.TBuildingSmPrmDataSelectResponse;
import jp.co.osaki.osol.api.result.alertmail.daily.TBuildingSmPrmDataSelectResult;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物機器データ取得 Beanクラス
 *
 * @author yonezawa.a
 */
@Named(value = "TBuildingSmPrmDataSelect")
@RequestScoped
public class TBuildingSmPrmDataSelectBean extends OsolApiBean<TBuildingSmPrmDataSelectParameter>
        implements BaseApiBean<TBuildingSmPrmDataSelectParameter, TBuildingSmPrmDataSelectResponse> {

    private TBuildingSmPrmDataSelectParameter parameter = new TBuildingSmPrmDataSelectParameter();

    private TBuildingSmPrmDataSelectResponse response = new TBuildingSmPrmDataSelectResponse();

    @EJB
    TBuildingSmPrmDataSelectServiceDao tBuildingSmPrmDataSelectServiceDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public TBuildingSmPrmDataSelectParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(TBuildingSmPrmDataSelectParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public TBuildingSmPrmDataSelectResponse execute() throws Exception {
        TBuildingSmPrmDataSelectParameter param = new TBuildingSmPrmDataSelectParameter();
        copyOsolApiNoneParameter(this.parameter, param);
        param.setNowDate(DateUtility.plusDay(this.parameter.getNowDate(), -1));

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        TBuildingSmPrmDataSelectResult result = tBuildingSmPrmDataSelectServiceDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

    /**
     * OsolApiNoneParameterのコピー
     *
     * @param fromApiNoneParameter
     * @param toApiNoneParameter
     */
    private void copyOsolApiNoneParameter(TBuildingSmPrmDataSelectParameter fromApiNoneParameter,
            TBuildingSmPrmDataSelectParameter toApiNoneParameter) {

        toApiNoneParameter.setBean(fromApiNoneParameter.getBean());
        toApiNoneParameter.setApiKey(fromApiNoneParameter.getApiKey());
        toApiNoneParameter.setNowDate(fromApiNoneParameter.getNowDate());
    }
}
