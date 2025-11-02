package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.ListSmsCheckMeterDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsCheckMeterParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsCheckMeterResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsCheckMeterResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 確定漏れ管理番号 一覧 検索 (確定前検針データ一覧表示画面用) Beanクラス
 * @author kobayashi.sho
 */
@Named(value = "ListSmsCheckMeterBean")
@RequestScoped
public class ListSmsCheckMeterBean extends OsolApiBean<ListSmsCheckMeterParameter>
        implements BaseApiBean<ListSmsCheckMeterParameter, ListSmsCheckMeterResponse> {

    private ListSmsCheckMeterParameter parameter = new ListSmsCheckMeterParameter();

    private ListSmsCheckMeterResponse response = new ListSmsCheckMeterResponse();

    @EJB
    private ListSmsCheckMeterDao listSmsCheckMeterDao;

    @Override
    public ListSmsCheckMeterParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsCheckMeterParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsCheckMeterResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        // 確定漏れ管理番号取得
        ListSmsCheckMeterResult result = listSmsCheckMeterDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
