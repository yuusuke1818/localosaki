package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsReservationInfoSelectDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsReservationInfoSelectParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsReservationInfoSelectResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsReservationInfoSelectResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 集計・分析 EMS実績 予約情報取得(DL予約) Beanクラス
 * @author nishida.t
 *
 */
@Named(value = "AnalysisEmsReservationInfoSelectBean")
@RequestScoped
public class AnalysisEmsReservationInfoSelectBean extends OsolApiBean<AnalysisEmsReservationInfoSelectParameter>
    implements BaseApiBean<AnalysisEmsReservationInfoSelectParameter, AnalysisEmsReservationInfoSelectResponse> {

    private AnalysisEmsReservationInfoSelectParameter parameter = new AnalysisEmsReservationInfoSelectParameter();
    private AnalysisEmsReservationInfoSelectResponse response = new AnalysisEmsReservationInfoSelectResponse();

    @EJB
    AnalysisEmsReservationInfoSelectDao analysisEmsReservationInfoSelectDao;

    @Override
    public AnalysisEmsReservationInfoSelectParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsReservationInfoSelectParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public AnalysisEmsReservationInfoSelectResponse execute() throws Exception {
        AnalysisEmsReservationInfoSelectParameter param = new AnalysisEmsReservationInfoSelectParameter();
        copyOsolApiParameter(this.parameter, param);

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsReservationInfoSelectResult result = analysisEmsReservationInfoSelectDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }
}
