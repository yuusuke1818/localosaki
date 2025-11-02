package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SettingEventCtrlUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SettingEventCtrlUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.SettingEventCtrlUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.LinkSettingExtractResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200021Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * イベント制御設定(設定) Bean クラス
 *
 * @autho t_sakamoto
 *
 */
@Named(value = SmControlConstants.SETTING_EVENT_CTRL_UPDATE)
@RequestScoped
public class SettingEventCtrlUpdateBean
        extends AbstractApiBean<SettingEventCtrlUpdateResult, SettingEventCtrlUpdateParameter> {

    @EJB
    private SettingEventCtrlUpdateDao dao;

    private A200021Param tempParameter;
    private Long smId;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(SettingEventCtrlUpdateParameter parameter) {
        A200021Param param = new Gson().fromJson(parameter.getResult(), A200021Param.class);
        tempParameter = param;
        smId = parameter.getSmId();

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    //機種依存チェック(FVPa(G2)以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

        if (!super.isFVPaG2(smPrm)
                && !super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        // List数チェック
        List<Map<String, Object>> loadList = ((A200021Param) param).getLoadList();
        if ((super.isFVPaG2(smPrm) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST)
                || (super.isEa(smPrm) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST_E_ALPHA)
                || (super.isEa2(smPrm) && loadList.size() != SmControlConstants.SETTING_EVENT_LOAD_LIST_E_ALPHA_2)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        for (Map<String, Object> load : loadList) {
            // 入れ子リスト取得
            Object strSettingEventList = load.get("settingEventCtrlList");
            List<Map<String, String>> eventList = new Gson().fromJson(String.valueOf(strSettingEventList),
                    new TypeToken<Collection<Map<String, String>>>() {
                    }.getType());
            if (eventList.size() != SmControlConstants.SETTING_EVENT_LIST) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "eventList.size()", String.valueOf(eventList.size()));
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }

            // 機種固有パラメータチェック
            if (super.isEa2(smPrm)) {
                for (Map<String, String> eventInfo : eventList) {
                    String comparePoint = eventInfo.get("comparePoint");

                    if ((comparePoint == null) || !(comparePoint.matches("[0-9]{1,3}"))) {
                        StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                        super.loggingError(st, "comparePoint", comparePoint);
                        throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
                    }
                }
           }
        }

        return true;
    }

    /**
     * 機器空調設定情報の登録
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200021Param> res = (FvpCtrlMngResponse<A200021Param>) response;
        A200021Param param = res.getParam();

        if (tempParameter == null || tempParameter.getLinkSettingList() == null || tempParameter.getLinkSettingList().isEmpty()) {
            //機器空調設定情報がない場合は処理を終了する
            return;
        }

        List<LinkSettingExtractResultData> linkSettingResultList = dao.updateLinkSettingData(tempParameter,
                super.loginUserId, this.smId);

        param.setLinkSettingList(linkSettingResultList);

        // レスポンスに格納
        res.setParam(param);
        response = res;

    }

}
