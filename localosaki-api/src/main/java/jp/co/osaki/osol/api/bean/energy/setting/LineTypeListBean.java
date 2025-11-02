package jp.co.osaki.osol.api.bean.energy.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.energy.setting.LineTypeListDao;
import jp.co.osaki.osol.api.parameter.energy.setting.LineTypeListParameter;
import jp.co.osaki.osol.api.response.energy.setting.LineTypeListResponse;
import jp.co.osaki.osol.api.result.energy.setting.LineTypeListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineTypeListDetailResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 系統種別取得処理　Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "LineTypeListBean")
@RequestScoped
public class LineTypeListBean extends OsolApiBean<LineTypeListParameter>
        implements BaseApiBean<LineTypeListParameter, LineTypeListResponse> {

    private LineTypeListParameter parameter = new LineTypeListParameter();

    private LineTypeListResponse response = new LineTypeListResponse();

    @EJB
    private LineTypeListDao lineTypeListDao;

    /* (非 Javadoc)
    * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
    */
    @Override
    public LineTypeListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(LineTypeListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public LineTypeListResponse execute() throws Exception {
        LineTypeListParameter param = new LineTypeListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        List<LineTypeListDetailResult> detailList = lineTypeListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(new LineTypeListResult(detailList));

        return response;
    }

}
