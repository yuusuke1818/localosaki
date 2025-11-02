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
import jp.co.osaki.osol.api.dao.energy.setting.LineGroupSearchDao;
import jp.co.osaki.osol.api.parameter.energy.setting.LineGroupSearchParameter;
import jp.co.osaki.osol.api.response.energy.setting.LineGroupSearchResponse;
import jp.co.osaki.osol.api.result.energy.setting.LineGroupSearchResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 系統グループ取得Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "LineGroupSearchBean")
@RequestScoped
public class LineGroupSearchBean extends OsolApiBean<LineGroupSearchParameter>
        implements BaseApiBean<LineGroupSearchParameter, LineGroupSearchResponse> {

    private LineGroupSearchParameter parameter = new LineGroupSearchParameter();

    private LineGroupSearchResponse response = new LineGroupSearchResponse();

    @EJB
    LineGroupSearchDao lineGroupSearchDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public LineGroupSearchParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(LineGroupSearchParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public LineGroupSearchResponse execute() throws Exception {
        LineGroupSearchParameter param = new LineGroupSearchParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setSelectedCorpId(this.parameter.getSelectedCorpId());
        param.setLineGroupType(this.parameter.getLineGroupType());
        param.setBuildingId(this.parameter.getBuildingId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        LineGroupSearchResult result = lineGroupSearchDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
