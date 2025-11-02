package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsEventControlHistDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsEventControlHistParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsEventControlHistResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsEventControlHistResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計・分析 EMS実績 イベント/温湿度 制御履歴取得 Beanクラス
 * @author nishida.t
 *
 */
@Named(value = "AnalysisEmsEventControlHistBean")
@RequestScoped
public class AnalysisEmsEventControlHistBean extends OsolApiBean<AnalysisEmsEventControlHistParameter>
    implements BaseApiBean<AnalysisEmsEventControlHistParameter, AnalysisEmsEventControlHistResponse> {

    private AnalysisEmsEventControlHistParameter parameter = new AnalysisEmsEventControlHistParameter();
    private AnalysisEmsEventControlHistResponse response = new AnalysisEmsEventControlHistResponse();

    @EJB
    AnalysisEmsEventControlHistDao analysisEmsEventControlHistDao;

    @Override
    public AnalysisEmsEventControlHistParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsEventControlHistParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsEventControlHistResponse execute() throws Exception {
        AnalysisEmsEventControlHistParameter param = new AnalysisEmsEventControlHistParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        param.setRangeDateFrom(parameter.getRangeDateFrom());
        param.setRangeDateTo(parameter.getRangeDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 小数点精度の範囲チェック
        if(param.getPrecision() != null && !CheckUtility.checkIntegerRange(param.getPrecision().toString(),
                ApiSimpleConstants.PRECISION_MIN_VALUE, ApiSimpleConstants.PRECISION_MAX_VALUE)) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        // 指定精度未満の制御のチェック
        if (!CheckUtility.isNullOrEmpty(param.getBelowAccuracyControl()) &&
                CheckUtility.isNullOrEmpty(ApiCodeValueConstants.PRECISION_CONTROL.getName(param.getBelowAccuracyControl()))) {
            response.setResultCode(OsolApiResultCode.API_ERROR_ORG_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsEventControlHistResult result = analysisEmsEventControlHistDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
