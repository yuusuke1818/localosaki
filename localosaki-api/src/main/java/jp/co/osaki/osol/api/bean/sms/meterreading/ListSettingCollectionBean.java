package jp.co.osaki.osol.api.bean.sms.meterreading;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.sms.meterreading.ListSettingCollectionDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.ListSettingCollectionParameter;
import jp.co.osaki.osol.api.response.sms.meterreading.ListSettingCollectionResponse;
import jp.co.osaki.osol.api.result.sms.meterreading.ListSettingCollectionResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 操作状態一覧 取得（設定一括収集画面） Beanクラス
 * @author kobayashi.sho
 */
@Named(value = "ListSettingCollectionBean")
@RequestScoped
public class ListSettingCollectionBean extends OsolApiBean<ListSettingCollectionParameter>
        implements BaseApiBean<ListSettingCollectionParameter, ListSettingCollectionResponse> {

    private ListSettingCollectionParameter parameter = new ListSettingCollectionParameter();

    private ListSettingCollectionResponse response = new ListSettingCollectionResponse();

    @EJB
    private ListSettingCollectionDao listSettingCollectionDao;

    @Override
    public ListSettingCollectionParameter getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(ListSettingCollectionParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public ListSettingCollectionResponse execute() throws Exception {
        if (this.validate(this.parameter).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        ListSettingCollectionResult result = listSettingCollectionDao.query(this.parameter);

        response.setResultCode(OsolApiResultCode.API_OK); // ※検索結果が無い場合も正常(新規の場合はデータがないため)
        response.setResult(result);
        return response;
    }

}
