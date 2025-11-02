/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.energy.setting;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.LineListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.LineListParameter;
import jp.co.osaki.osol.api.response.energy.setting.LineListResponse;
import jp.co.osaki.osol.api.result.energy.setting.LineListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 系統取得処理　Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "LineListBean")
@RequestScoped
public class LineListBean extends OsolApiBean<LineListParameter>
        implements BaseApiBean<LineListParameter, LineListResponse> {

    private LineListParameter parameter = new LineListParameter();

    private LineListResponse response = new LineListResponse();

    @EJB
    LineListDao lineListDao;

    @Override
    public LineListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(LineListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public LineListResponse execute() throws Exception {
        LineListParameter param = new LineListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setLineEnableFlg(this.parameter.getLineEnableFlg());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //TODO 系統の値のチェックが必要
        LineListResponse response = new LineListResponse();
        LineListResult result = lineListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);

        return response;
    }

}
