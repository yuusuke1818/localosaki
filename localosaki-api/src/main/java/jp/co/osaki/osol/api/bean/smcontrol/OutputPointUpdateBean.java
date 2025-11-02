package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.OutputPointUpdateDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.OutputPointUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.OutputPointUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200070Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 * 出力ポイント設定(設定) Bean クラス.
 * @author nishida.t
 *
 */
@Named(value = SmControlConstants.OUTPUT_POINT_UPDATE)
@RequestScoped
public class OutputPointUpdateBean extends AbstractApiBean<OutputPointUpdateResult, OutputPointUpdateParameter> {

    @EJB
    private OutputPointUpdateDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(OutputPointUpdateParameter parameter) {
        A200070Param param = new Gson().fromJson(parameter.getResult(), A200070Param.class);

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

        // List数チェック
        List<Map<String, String>> loadExList = ((A200070Param) param).getLoadExList();
        if (loadExList.size() != SmControlConstants.OUTPUT_POINT_LOAD_EX_LIST) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadExList.size()", String.valueOf(loadExList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        List<Map<String, String>> loadList = ((A200070Param) param).getLoadList();
        if (loadList.size() != SmControlConstants.OUTPUT_POINT_LOAD_LIST) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "loadList.size()", String.valueOf(loadList.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        // アンサーバックポイント範囲チェック (0～256)
        for (Map<String, String> loadMap : loadExList) {
            try {
                // 0～256以外はバリデーションエラー
                int answerBackPoint = Integer.parseInt(loadMap.get("answerBackPoint"));
                if (answerBackPoint < 0 || answerBackPoint > 256) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "loadExList(" + loadExList.indexOf(loadMap) + ")." + "getAnswerBackPoint()", loadMap.get("answerBackPoint"));
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
                }
            } catch (NumberFormatException e) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "loadExList(" + loadExList.indexOf(loadMap) + ")." + "getAnswerBackPoint()", loadMap.get("answerBackPoint"));
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }
        }

        return true;
    }
}
