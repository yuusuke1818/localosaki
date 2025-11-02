package jp.co.osaki.osol.api.bean.smcontrol;

import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterStoreConfigUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterStoreConfigUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterStoreConfigUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210004Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * AielMaster店舗設定(設定) Bean クラス
 *
 * @author s_sunada
 *
 */
@Named(value = SmControlConstants.AIELMASTER_STORECONFIG_UPDATE)
@RequestScoped
public class AielMasterStoreConfigUpdateBean extends AbstractApiBean<AielMasterStoreConfigUpdateResult, AielMasterStoreConfigUpdateParameter>{

    @EJB
    private AielMasterStoreConfigUpdateDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(AielMasterStoreConfigUpdateParameter parameter) {
        A210004Param param = new Gson().fromJson(parameter.getResult(), A210004Param.class);
        String settingtServerDateTimeString = parameter.getDateTime();//日付

        //パラメータに日付がセットされていなかった場合、サーバの時刻をセット
        if (CheckUtility.isNullOrEmpty(settingtServerDateTimeString)) {
            settingtServerDateTimeString = new SimpleDateFormat(DateUtility.DATE_FORMAT_YYMMDDHHMM).format(dao.getServerDateTime());
        }

        param.setDateTime(settingtServerDateTimeString);

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

}
