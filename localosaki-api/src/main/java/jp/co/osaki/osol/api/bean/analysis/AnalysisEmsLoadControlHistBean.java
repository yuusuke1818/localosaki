package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.constants.ApiCodeValueConstants;
import jp.co.osaki.osol.api.constants.ApiSimpleConstants;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsLoadControlHistDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsLoadControlHistParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsLoadControlHistResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsLoadControlHistResult;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計・分析 EMS実績 負荷制御履歴取得 Beanクラス
 * @author nishida.t
 *
 */
@Named(value = "AnalysisEmsLoadControlHistBean")
@RequestScoped
public class AnalysisEmsLoadControlHistBean extends OsolApiBean<AnalysisEmsLoadControlHistParameter>
    implements BaseApiBean<AnalysisEmsLoadControlHistParameter, AnalysisEmsLoadControlHistResponse> {

    private AnalysisEmsLoadControlHistParameter parameter = new AnalysisEmsLoadControlHistParameter();
    private AnalysisEmsLoadControlHistResponse response = new AnalysisEmsLoadControlHistResponse();

    @EJB
    AnalysisEmsLoadControlHistDao analysisEmsLoadControlHistDao;

    @Override
    public AnalysisEmsLoadControlHistParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsLoadControlHistParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsLoadControlHistResponse execute() throws Exception {
        AnalysisEmsLoadControlHistParameter param = new AnalysisEmsLoadControlHistParameter();
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

        AnalysisEmsLoadControlHistResult result = analysisEmsLoadControlHistDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
