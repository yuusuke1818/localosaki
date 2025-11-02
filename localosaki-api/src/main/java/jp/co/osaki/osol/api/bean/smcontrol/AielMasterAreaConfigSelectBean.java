package jp.co.osaki.osol.api.bean.smcontrol;

import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AielMasterAreaConfigSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AielMasterAreaConfigSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.AielMasterAreaConfigSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.AielMasterAreaConfigSelectDetailResultData;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A210005Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.co.osaki.osol.utility.DateUtility;

/**
 *
 * AielMasterエリア設定(取得) Bean クラス
 *
 * @author s_sunada
 *
 */
@Named(value = SmControlConstants.AIELMASTER_AREA_CONFIG_SELECT)
@RequestScoped
public class AielMasterAreaConfigSelectBean
        extends AbstractApiBean<AielMasterAreaConfigSelectResult, AielMasterAreaConfigSelectParameter> {

    @EJB
    private AielMasterAreaConfigSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(AielMasterAreaConfigSelectParameter parameter) {
        A210005Param param = new A210005Param();


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

    /**
     * エリア情報のエリア名称とセンサ名称の取得を行う
     */
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A210005Param> res = (FvpCtrlMngResponse<A210005Param>) response;
        A210005Param param = res.getParam();

        //エリア情報のエリア名とセンサ名を取得する
        AielMasterAreaConfigSelectDetailResultData detailData = dao.getData(res.getSmId());

        //エリア名称とセンサ名称を設定する
        param.setAreaNameList(detailData.getAreaNameList());

        // レスポンスに格納
        res.setParam(param);
        response = res;

    }
}
