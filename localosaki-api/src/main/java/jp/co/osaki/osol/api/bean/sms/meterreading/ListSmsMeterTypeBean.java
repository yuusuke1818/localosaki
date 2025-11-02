package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.ListSmsMeterTypeDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSmsMeterTypeParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSmsMeterTypeResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSmsMeterTypeResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 種別設定情報検索 Beanクラス
 *
 * @author kobayashi.sho
 */
@Named(value = "ListSmsMeterTypeBean")
@RequestScoped
public class ListSmsMeterTypeBean extends OsolApiBean<ListSmsMeterTypeParameter>
        implements BaseApiBean<ListSmsMeterTypeParameter, ListSmsMeterTypeResponse> {

    private ListSmsMeterTypeParameter parameter = new ListSmsMeterTypeParameter();

    private ListSmsMeterTypeResponse response = new ListSmsMeterTypeResponse();

    @EJB
    private ListSmsMeterTypeDao listSmsMeterTypeDao;

    @Override
    public ListSmsMeterTypeParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSmsMeterTypeParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSmsMeterTypeResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSmsMeterTypeResult result = listSmsMeterTypeDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
