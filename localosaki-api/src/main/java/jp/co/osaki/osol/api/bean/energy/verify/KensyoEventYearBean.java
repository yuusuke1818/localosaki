package jp.co.osaki.osol.api.bean.energy.verify;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants.GROUP_CODE;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.energy.verify.KensyoEventYearDao;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoEventYearParameter;
import jp.co.osaki.osol.api.response.energy.verify.KensyoEventYearResponse;
import jp.co.osaki.osol.api.result.energy.verify.KensyoEventYearResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * イベント制御検証・年報 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "KensyoEventYearBean")
@RequestScoped
public class KensyoEventYearBean extends OsolApiBean<KensyoEventYearParameter>
        implements BaseApiBean<KensyoEventYearParameter, KensyoEventYearResponse> {

    private KensyoEventYearParameter parameter = new KensyoEventYearParameter();

    private KensyoEventYearResponse response = new KensyoEventYearResponse();

    @EJB
    private KensyoEventYearDao kensyoEventYearDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public KensyoEventYearParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(KensyoEventYearParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public KensyoEventYearResponse execute() throws Exception {
        KensyoEventYearParameter param = new KensyoEventYearParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setSmId(this.parameter.getSmId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setSummaryKind(this.parameter.getSummaryKind());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setSumPeriodCalcType(this.parameter.getSumPeriodCalcType());
        param.setSumPeriod(this.parameter.getSumPeriod());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        //各パラメータの区分値チェック
        if (!CheckUtility.isNullOrEmpty(param.getSummaryKind()) && CheckUtility
                .isNullOrEmpty(genericTypeUtility.getKbnName(GROUP_CODE.SUMMARY_UNIT, param.getSummaryKind()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;

        }
        if (!CheckUtility.isNullOrEmpty(param.getSumPeriodCalcType()) && CheckUtility
                .isNullOrEmpty(ApiCodeValueConstants.SUMMARY_RANGE_TYPE.getName(param.getSumPeriodCalcType()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 集計期間の範囲チェック
        if (param.getSumPeriod() != null &&
                !CheckUtility.checkIntegerRange(param.getSumPeriod().toString(),
                        ApiSimpleConstants.DEMAND_SUM_PERIOD_MIN_VALUE, ApiSimpleConstants.DEMAND_VERIFY_SUM_PERIOD_MAX_YEAR)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        KensyoEventYearResponse response = new KensyoEventYearResponse();
        KensyoEventYearResult result = kensyoEventYearDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
