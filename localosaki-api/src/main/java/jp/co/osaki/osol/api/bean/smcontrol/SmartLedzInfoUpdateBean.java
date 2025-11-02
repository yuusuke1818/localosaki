package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmartLedzInfoUpdateDao;
import jp.co.osaki.osol.api.parameter.smcontrol.SmartLedzInfoUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.SmartLedzInfoUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200058Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.CheckUtility;

/**
 *
 * SmartLEDZ情報(設定) Bean クラス
 *
 * @autho t_hayama
 *
 */
@Named(value = SmControlConstants.SMART_LEDZ_INFO_UPDATE)
@RequestScoped
public class SmartLedzInfoUpdateBean extends AbstractApiBean<SmartLedzInfoUpdateResult, SmartLedzInfoUpdateParameter> {

    @EJB
    private SmartLedzInfoUpdateDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(SmartLedzInfoUpdateParameter parameter) {

        A200058Param param = new Gson().fromJson(parameter.getResult(), A200058Param.class);

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    //機種依存チェック(Ea,Ea2以外はエラー)
    @SuppressWarnings("serial")
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        if(!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        A200058Param reqParam = (A200058Param)param;

        // Eaバリデーション
        if (super.isEa(smPrm)) {
            // システムIDチェック
            if (!CheckUtility.checkRegex(reqParam.getSystemId(), "[0-9]{10}")) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "SYSTEM_ID", reqParam.getSystemId());
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }

            // グループリストチェック
            boolean ret = false;
            if (reqParam.getGroupList() != null) {
                ret = validateList(reqParam.getGroupList(), new HashMap<String, String>() {
                    {
                        put("dimmingRate",       "[0-9]{1,3}");    // 調光率
                        put("fadeTime",          "[0-9]{1,3}");    // フェード時間
                        put("dimmingLimit",      "[0-9]");         // 調光制限
                        put("scheduleInterlock", "[0-9]{1,2}");    // スケジュール連動
                    }
                });
            }

            if (!ret) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "GROUP_LIST", String.valueOf(reqParam.getGroupList()));
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }

            // ゾーンリストチェック
            ret = false;
            if (reqParam.getZoneList() != null) {
                ret = validateList(reqParam.getZoneList(), new HashMap<String, String>() {
                    {
                        put("sceneNum",          "[0-9]{1,2}");    // シーン番号
                        put("fadeTime",          "[0-9]{1,3}");    // フェード時間
                        put("scheduleInterlock", "[0-9]{1,2}");    // スケジュール連動
                    }
                });
            }

            if (!ret) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "ZONE_LIST", String.valueOf(reqParam.getZoneList()));
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }
        }

        // Ea2バリデーション
        else if (super.isEa2(smPrm)) {
            // システムIDチェック
            if (!CheckUtility.checkRegex(reqParam.getSystemId().trim(), "^[a-zA-Z0-9-_]{8,13}$")) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "SYSTEM_ID", reqParam.getSystemId());
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }

            // ゾーンリストチェック
            boolean ret = false;
            if (reqParam.getZoneList() != null) {
                ret = validateList(reqParam.getZoneList(), new HashMap<String, String>() {
                    {
                        put("sceneNum",          "[0-9]{1,2}");    // シーン番号
                        put("scheduleInterlock", "[0-9]{1,2}");    // スケジュール連動
                    }
                });
            }

            if (!ret) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, "ZONE_LIST", String.valueOf(reqParam.getZoneList()));
                throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
            }
        }

        return true;
    }

        /**
         * Map<String,String>バリデーション対応
         *
         * @param list
         * @param validationPattern
         * @return true:エラーなし、false:エラーあり
         */
        private boolean validateList(List<Map<String, String>> list, Map<String,String> validationPattern) {
            Iterator<Map<String, String>> itr = list.iterator();
            while(itr.hasNext()) {
                Map<String, String> map = itr.next();
                for(Entry<String, String> ent : validationPattern.entrySet()) {
                    String value = String.valueOf(map.get(ent.getKey()));
                    if(value==null || !value.matches(ent.getValue())) {
                        return false;
                    }
                }
            }
            return true;
        }

}
