package jp.co.osaki.osol.api.bean.analysis;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.analysis.AnalysisEmsRecordCsvDao;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsRecordCsvParameter;
import jp.co.osaki.osol.api.response.analysis.AnalysisEmsGroupLineCsvResponse;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsGroupLineRecordCsvResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 *
 * 集計・分析 EMS実績 系統別計測値CSV出力情報取得 Beanクラス
 *
 * @author yonezawa.a
 *
 */

@Named(value = "AnalysisEmsRecordCsvListBean")
@RequestScoped
public class AnalysisEmsRecordCsvListBean extends OsolApiBean<AnalysisEmsRecordCsvParameter>
            implements BaseApiBean<AnalysisEmsRecordCsvParameter,AnalysisEmsGroupLineCsvResponse> {

    private AnalysisEmsRecordCsvParameter parameter = new AnalysisEmsRecordCsvParameter();
    private AnalysisEmsGroupLineCsvResponse response = new AnalysisEmsGroupLineCsvResponse();

    @EJB
    AnalysisEmsRecordCsvDao analysisEmsRecordCsvDao;

    @Override
    public AnalysisEmsRecordCsvParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(AnalysisEmsRecordCsvParameter parameter) {
        this.parameter = parameter;

    }

    @Override
    public AnalysisEmsGroupLineCsvResponse execute() throws Exception {
        AnalysisEmsRecordCsvParameter param = new AnalysisEmsRecordCsvParameter();
        copyOsolApiParameter(this.parameter, param);

        param.setBuildingId(parameter.getBuildingId());
        param.setParentGroupId(parameter.getParentGroupId());
        param.setChildGroupId(parameter.getChildGroupId());
        param.setLineGroupId(parameter.getLineGroupId());
        param.setLineNo(parameter.getLineNo());
        param.setMeasurementDateFrom(parameter.getMeasurementDateFrom());
        param.setMeasurementDateTo(parameter.getMeasurementDateTo());
        param.setPrecision(parameter.getPrecision());
        param.setBelowAccuracyControl(parameter.getBelowAccuracyControl());
        param.setEmsExtendsPlusMinus12hEnabled(parameter.getEmsExtendsPlusMinus12hEnabled());
        param.setSelectedLineId(parameter.getSelectedLineId());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        AnalysisEmsGroupLineRecordCsvResult result = analysisEmsRecordCsvDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
