package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.ListSmsInspectionMeterBefDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspectionMeterBefParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsInspectionMeterBefResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsInspectionMeterBefResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 確定前検針データ一覧 取得 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "ListSmsInspectionMeterBefBean")
@RequestScoped
public class ListSmsInspectionMeterBefBean extends OsolApiBean<ListSmsInspectionMeterBefParameter>
        implements BaseApiBean<ListSmsInspectionMeterBefParameter, ListSmsInspectionMeterBefResponse> {

    private ListSmsInspectionMeterBefParameter parameter = new ListSmsInspectionMeterBefParameter();

    private ListSmsInspectionMeterBefResponse response = new ListSmsInspectionMeterBefResponse();

    @EJB
    private ListSmsInspectionMeterBefDao listSmsInspectionMeterBefDao;

    @Override
    public ListSmsInspectionMeterBefParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsInspectionMeterBefParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsInspectionMeterBefResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsInspectionMeterBefResult result = listSmsInspectionMeterBefDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
