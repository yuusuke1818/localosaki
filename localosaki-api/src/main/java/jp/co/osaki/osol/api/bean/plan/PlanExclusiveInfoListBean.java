package jp.co.osaki.osol.api.bean.plan;

import javax.ejb.EJB;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.plan.PlanExclusiveInfoListDao;
import jp.co.osaki.osol.api.parameter.plan.PlanExclusiveInfoListParameter;
import jp.co.osaki.osol.api.response.plan.PlanExclusiveInfoListResponse;
import jp.co.osaki.osol.api.result.plan.PlanExclusiveInfoListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 計画履行情報登録用排他情報取得 Beanクラス
 * 建物計画履行・計画履行情報を登録する際に必要な排他情報のみを取得する処理（公開はしない）
 * @author ya-ishida
 *
 */
public class PlanExclusiveInfoListBean extends OsolApiBean<PlanExclusiveInfoListParameter>
        implements BaseApiBean<PlanExclusiveInfoListParameter, PlanExclusiveInfoListResponse> {

    private PlanExclusiveInfoListParameter parameter = new PlanExclusiveInfoListParameter();

    private PlanExclusiveInfoListResponse response = new PlanExclusiveInfoListResponse();

    @EJB
    PlanExclusiveInfoListDao planExclusiveInfoListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public PlanExclusiveInfoListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(PlanExclusiveInfoListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public PlanExclusiveInfoListResponse execute() throws Exception {
        PlanExclusiveInfoListParameter param = new PlanExclusiveInfoListParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        PlanExclusiveInfoListResponse response = new PlanExclusiveInfoListResponse();
        PlanExclusiveInfoListResult result = planExclusiveInfoListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
