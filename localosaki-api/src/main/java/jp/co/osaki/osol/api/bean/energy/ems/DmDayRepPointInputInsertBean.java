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
import jp.co.osaki.osol.api.dao.energy.ems.DmDayRepPointInputInsertDao;
import jp.co.osaki.osol.api.parameter.energy.ems.DmDayRepPointInputInsertParameter;
import jp.co.osaki.osol.api.response.energy.ems.DmDayRepPointInputInsertResponse;
import jp.co.osaki.osol.api.result.energy.ems.DmDayRepPointInputInsertResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド日報ポイント入力登録 Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "DmDayRepPointInputInsertBean")
@RequestScoped
public class DmDayRepPointInputInsertBean extends OsolApiBean<DmDayRepPointInputInsertParameter>
        implements BaseApiBean<DmDayRepPointInputInsertParameter, DmDayRepPointInputInsertResponse> {

    private DmDayRepPointInputInsertParameter parameter = new DmDayRepPointInputInsertParameter();

    private DmDayRepPointInputInsertResponse response = new DmDayRepPointInputInsertResponse();

    @EJB
    DmDayRepPointInputInsertDao DmDayRepPointInputInsertDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public DmDayRepPointInputInsertParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(DmDayRepPointInputInsertParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public DmDayRepPointInputInsertResponse execute() throws Exception {
        DmDayRepPointInputInsertParameter param = new DmDayRepPointInputInsertParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setDmDayRepPointInputInsertRequest(this.parameter.getDmDayRepPointInputInsertRequest());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        DmDayRepPointInputInsertResult result = DmDayRepPointInputInsertDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
