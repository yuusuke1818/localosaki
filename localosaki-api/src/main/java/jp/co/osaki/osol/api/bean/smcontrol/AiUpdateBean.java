package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.AiUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.AiUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.AiUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200068Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * AI設定(設定) Bean クラス.
 * @author nishida.t
 *
 */
@Named(value = "AiUpdateBean")
@RequestScoped
public class AiUpdateBean extends AbstractApiBean<AiUpdateResult, AiUpdateParameter> {

    @EJB
    private AiUpdateDao dao;

    AiUpdateParameter parameter;
    A200068Param resParam;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(AiUpdateParameter parameter) {

        this.resParam = new Gson().fromJson(parameter.getResult(), A200068Param.class);
        this.parameter = parameter;

        @SuppressWarnings("unchecked")
        T ret = (T) this.resParam;

        return ret;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200068Param> res = (FvpCtrlMngResponse<A200068Param>) response;

        //DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()) {
            return;
        }
        // dao呼出
        dao.updateSmPrm(this.parameter, res, this.resParam);
    }

  //機種依存チェック(Ea2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if (!super.isEa2(smPrm)) {
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        return true;
    }
}
