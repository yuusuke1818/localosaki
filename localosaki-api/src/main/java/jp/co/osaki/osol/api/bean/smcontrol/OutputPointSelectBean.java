package jp.co.osaki.osol.api.bean.smcontrol;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.OutputPointSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.OutputPointSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.OutputPointSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200069Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 * 出力ポイント設定(取得) Bean クラス.
 * @author nishida.t
 *
 */
@Named(value = SmControlConstants.OUTPUT_POINT_SELECT)
@RequestScoped
public class OutputPointSelectBean extends AbstractApiBean<OutputPointSelectResult, OutputPointSelectParameter> {

    @EJB
    private OutputPointSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(OutputPointSelectParameter parameter) {
        A200069Param param = new A200069Param();

        if (parameter != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
        }

        @SuppressWarnings("unchecked")
        T ret = (T) param;
        return ret;
    }

    // 機種依存チェック(Eα/Eα2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        // 設定変更履歴 Eα：0～4
        if (super.isEa(smPrm) && ((A200069Param)param).getSettingChangeHist() != null
                && !CheckUtility.checkRegex(((A200069Param)param).getSettingChangeHist(), SmControlConstants.OUTPUT_POINT_PATTERN_SETTING_CHANGE_HIST_EA)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "SettingChangeHist()", ((A200069Param)param).getSettingChangeHist());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }
        return true;
    }
}
