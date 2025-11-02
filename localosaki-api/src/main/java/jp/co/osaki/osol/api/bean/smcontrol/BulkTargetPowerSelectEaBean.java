package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.BulkTargetPowerSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.BulkSmControlApiParameter;
import jp.co.osaki.osol.api.result.smcontrol.BuildingDmResult;
import jp.co.osaki.osol.api.result.smcontrol.BulkTargetPowerSelectEaResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200049Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
*
* 複数建物・テナント一括 目標電力(取得)Ea Bean クラス
*
* @author nishida.t
*
*/
@Named(value = SmControlConstants.BULK_TARGET_POWER_SELECT_EA)
@RequestScoped
public class BulkTargetPowerSelectEaBean extends AbstructBulkApiBean<BulkTargetPowerSelectEaResult, BulkSmControlApiParameter> {

    @EJB
    private BulkTargetPowerSelectDao dao;

    // 設定値
    private List<A200049Param> PARAM_LIST =  new ArrayList<>();

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected List<? extends BaseParam> initParamList(List<Map<String,String>> parameterList){
        List<A200049Param> paramList = new ArrayList<>();
        for(Map<String, String> p : parameterList) {
            A200049Param param = new A200049Param();

            // String型で宣言されているのでweb側でjson形式に変換した際、値に""が付与されるので削除
            String settingChangeHist = p.get("settingChangeHist");
            boolean updateDBflg = Boolean.valueOf(p.get("updateDBflg"));

            // 未設定時、0をセット
            if (CheckUtility.isNullOrEmpty(settingChangeHist)) {
                settingChangeHist = SmControlConstants.SETTING_CHG_HIST_LATEST;
            } else {
                settingChangeHist = settingChangeHist.replaceAll("\"", "");
            }

            param.setSettingChangeHist(settingChangeHist);
            param.setUpdateDBflg(updateDBflg);
            paramList.add(param);
        }

        // リクエスト情報保持
        this.PARAM_LIST = paramList;
        return paramList;
    }

    //機種依存チェック(Ea,Ea2以外はエラー)
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }
        return true;
    }

    @Override
    protected void callDao(List<FvpCtrlMngResponse<BaseParam>> fvpResList) {

        for (int i = 0; i < fvpResList.size(); i++) {

            FvpCtrlMngResponse<BaseParam> fvpRes = fvpResList.get(i);
            A200049Param res = (A200049Param) fvpResList.get(i).getParam();

            // DBフラグがOFF,もしくは結果コードに例外が格納されている場合登録処理無し
            if(!this.PARAM_LIST.get(i).isUpdateDBflg()
                    || fvpResList.get(i).getFvpResultCd() != OsolApiResultCode.API_OK){
                continue;
            }

            // 登録用ResultSet
            BuildingDmResult param = new BuildingDmResult();

            param.setSmId(fvpRes.getSmId());

            BigDecimal targetPower = new BigDecimal((String) res.getTargetPower());

            param.setTargetPower(targetPower);
            param.setUpdateUserId(super.loginUserId);
            // dao呼出
            try {
                dao.updateBuildingDM(param);
            } catch (Exception e) {
                // 更新エラー時はfvpResListの結果コードに例外を格納
                fvpResList.get(i).setFvpResultCd(((SmControlException) e).getErrorCode());
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
            }
        }
    }

}
