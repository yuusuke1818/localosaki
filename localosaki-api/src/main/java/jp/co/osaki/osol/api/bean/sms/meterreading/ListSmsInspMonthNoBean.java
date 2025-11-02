package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.ListSmsInspMonthNoDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsInspMonthNoParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsInspMonthNoResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsInspMonthNoResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 月検針連番一覧 取得(連番プルダウン用) Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "ListSmsInspMonthNoBean")
@RequestScoped
public class ListSmsInspMonthNoBean extends OsolApiBean<ListSmsInspMonthNoParameter>
        implements BaseApiBean<ListSmsInspMonthNoParameter, ListSmsInspMonthNoResponse> {

    private ListSmsInspMonthNoParameter parameter = new ListSmsInspMonthNoParameter();

    private ListSmsInspMonthNoResponse response = new ListSmsInspMonthNoResponse();

    @EJB
    private ListSmsInspMonthNoDao listSmsInspMonthNoDao;

    @Override
    public ListSmsInspMonthNoParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsInspMonthNoParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsInspMonthNoResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsInspMonthNoResult result = listSmsInspMonthNoDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
