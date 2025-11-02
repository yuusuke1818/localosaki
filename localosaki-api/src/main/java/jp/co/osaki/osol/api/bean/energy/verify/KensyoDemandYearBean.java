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
import jp.co.osaki.osol.api.dao.energy.verify.KensyoDemandYearDao;
import jp.co.osaki.osol.api.parameter.energy.verify.KensyoDemandYearParameter;
import jp.co.osaki.osol.api.response.energy.verify.KensyoDemandYearResponse;
import jp.co.osaki.osol.api.result.energy.verify.KensyoDemandYearResult;
import jp.co.osaki.osol.api.utility.common.GenericTypeUtility;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * デマンド制御検証・年報 Beanクラス
 *
 * @author t_hirata
 */
@Named(value = "KensyoDemandYearBean")
@RequestScoped
public class KensyoDemandYearBean extends OsolApiBean<KensyoDemandYearParameter>
        implements BaseApiBean<KensyoDemandYearParameter, KensyoDemandYearResponse> {

    private KensyoDemandYearParameter parameter = new KensyoDemandYearParameter();

    private KensyoDemandYearResponse response = new KensyoDemandYearResponse();

    @EJB
    private KensyoDemandYearDao kensyoDemandYearDao;

    @Inject
    GenericTypeUtility genericTypeUtility;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public KensyoDemandYearParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(KensyoDemandYearParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public KensyoDemandYearResponse execute() throws Exception {
        KensyoDemandYearParameter param = new KensyoDemandYearParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setBuildingId(this.parameter.getBuildingId());
        param.setLineGroupId(this.parameter.getLineGroupId());
        param.setLineNo(this.parameter.getLineNo());
        param.setSmId(this.parameter.getSmId());
        param.setYear(this.parameter.getYear());
        param.setMonth(this.parameter.getMonth());
        param.setSummaryKind(this.parameter.getSummaryKind());
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

        KensyoDemandYearResponse response = new KensyoDemandYearResponse();
        KensyoDemandYearResult result = kensyoDemandYearDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
