package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.verify.KensyoEventMonthDao;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoEventMonthParameter;
import jp.co.osaki.osol.api.response.energy.verify.KensyoEventMonthResponse;
import jp.co.osaki.osol.api.result.energy.verify.KensyoEventMonthResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * イベント制御検証・月報
 *
 * @author t_hirata
 */
@Named(value = "KensyoEventMonthBean")
@RequestScoped
public class KensyoEventMonthBean extends OsolApiBean<KensyoEventMonthParameter>
        implements BaseApiBean<KensyoEventMonthParameter, KensyoEventMonthResponse> {

    private KensyoEventMonthParameter parameter = new KensyoEventMonthParameter();

    private KensyoEventMonthResponse response = new KensyoEventMonthResponse();

    @EJB
    private KensyoEventMonthDao kensyoEventMonthDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public KensyoEventMonthParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(KensyoEventMonthParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public KensyoEventMonthResponse execute() throws Exception {
        KensyoEventMonthParameter param = new KensyoEventMonthParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setYmd(this.parameter.getYmd());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_VERIFY__SUM_PERIOD_MAX_MONTH)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        KensyoEventMonthResult result = kensyoEventMonthDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
