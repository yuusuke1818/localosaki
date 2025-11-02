package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.DemandUpdateEaDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DemandUpdateEaParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.DemandUpdateEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200050Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * デマンド(設定) Eα Bean クラス
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.DEMAND_UPDATE_EA)
@RequestScoped
public class DemandUpdateEaBean extends AbstractApiBean<DemandUpdateEaResult, DemandUpdateEaParameter> {

    @EJB
    private DemandUpdateEaDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(DemandUpdateEaParameter parameter) {

        A200050Param param = new Gson().fromJson(parameter.getResult(), A200050Param.class);
        param.setUpdateDBflg(parameter.isUpdateDBflg());

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    //機種依存チェック(Ea,Ea2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if(!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        List<Map<String, String>> list = ((A200050Param)param).getLoadInfoList();
        //listサイズチェック
        if(super.isEa(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_E_ALPHA)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isEa2(smPrm) && !(list.size() == SmControlConstants.DEMAND_LOAD_LIST_E_ALPHA_2)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "LoadInfoList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        // 機種固有パラメータチェック
        if (super.isEa2(smPrm)) {
            for (Map<String, String> loadInfo : list) {
                String temperatureRefPoint = loadInfo.get("temperatureRefPoint");

                if ((temperatureRefPoint == null) || !(temperatureRefPoint.matches("[0-9]{1,3}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "temperatureRefPoint", temperatureRefPoint);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
                }
            }
        }

        return true;
    }


    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200050Param> res = (FvpCtrlMngResponse<A200050Param>) response;
        A200050Param resParam = res.getParam();

        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // 登録用ResultSet
        BuildingDmResult param = new BuildingDmResult();

        param.setSmId(res.getSmId());

        BigDecimal targetPower = new BigDecimal((String) resParam.getTargetPower());

        param.setTargetPower(targetPower);
        param.setUpdateUserId(super.loginUserId);

        // dao呼出
        dao.updateBuildingDM(param,res);
    }

}
