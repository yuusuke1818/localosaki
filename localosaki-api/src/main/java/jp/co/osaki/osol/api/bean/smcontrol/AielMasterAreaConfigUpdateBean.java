package jp.co.osaki.osol.api.bean.smcontrol;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterAreaConfigUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterAreaConfigUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterAreaConfigUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.extract.AreaNameExtractResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210006Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * AielMasterエリア設定(設定) Bean クラス
 *
 * @autho s_sunada
 *
 */
@Named(value = SmControlConstants.AIELMASTER_AREA_CONFIG_UPDATE)
@RequestScoped
public class AielMasterAreaConfigUpdateBean
        extends AbstractApiBean<AielMasterAreaConfigUpdateResult, AielMasterAreaConfigUpdateParameter> {

    @EJB
    private AielMasterAreaConfigUpdateDao dao;

    private A210006Param areaParameter;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(AielMasterAreaConfigUpdateParameter parameter) {
        A210006Param param = new Gson().fromJson(parameter.getResult(), A210006Param.class);

        String settingtServerDateTimeString = parameter.getDateTime();//日付

        //パラメータに日付がセットされていなかった場合、サーバの時刻をセット
        if (CheckUtility.isNullOrEmpty(settingtServerDateTimeString)) {
            settingtServerDateTimeString = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(dao.getServerDateTime());
        }
        param.setDateTime(settingtServerDateTimeString);

        areaParameter = param;

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    //機種依存チェック(Ea2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if (!super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        return true;
    }

    /**
     * エリア情報のエリア名称,センサ名称の登録
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A210006Param> res = (FvpCtrlMngResponse<A210006Param>) response;
        A210006Param param = res.getParam();

        if (areaParameter == null || areaParameter.getAreaNameList() == null || areaParameter.getAreaNameList().isEmpty()) {
            //エリア情報がない場合は処理を終了する
            return;
        }

        List<AreaNameExtractResultData> areaNameResultList = dao.updateAreaNameData(areaParameter,
                super.loginUserId);

        param.setAreaNameList(areaNameResultList);

        // レスポンスに格納
        res.setParam(param);
        response = res;

    }

}
