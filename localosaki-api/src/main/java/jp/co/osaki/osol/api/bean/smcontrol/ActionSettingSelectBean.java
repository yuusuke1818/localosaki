package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.ActionSettingSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.ActionSettingSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.ActionSettingSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200071Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 動作モード(取得) Bean クラス.
 * @author nishida.t
 *
 */
@Named(value = "ActionSettingSelectBean")
@RequestScoped
public class ActionSettingSelectBean extends AbstractApiBean<ActionSettingSelectResult, ActionSettingSelectParameter> {

    @EJB
    private ActionSettingSelectDao dao;

    ActionSettingSelectParameter parameter;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(ActionSettingSelectParameter parameter) {
        A200071Param param = new A200071Param();
        this.parameter = parameter;

        if (parameter != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }



  @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {
      // param取得
      @SuppressWarnings("unchecked")
      FvpCtrlMngResponse<A200071Param> res = (FvpCtrlMngResponse<A200071Param>) response;

    //if DBフラグがOFFなら処理無し
      if(!super.apiParameter.isUpdateDBflg()) {
          return;
      }

      // dao呼出
      dao.updateSmPrm(this.parameter, res);

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
