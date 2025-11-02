package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.UnitUpdateDao;
import jp.co.osaki.osol.api.parameter.smcontrol.UnitUpdateParameter;
import jp.co.osaki.osol.api.result.smcontrol.SmPointResult;
import jp.co.osaki.osol.api.result.smcontrol.UnitUpdateResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200013Param;
import jp.co.osaki.osol.mng.param.BaseParam;

/**
 *
 * ユニット(設定) Bean クラス
 *
 * @author da_yamano
 *
 */

@Named(value = SmControlConstants.UNIT_UPDATE)
@RequestScoped
public class UnitUpdateBean
extends AbstractApiBean<UnitUpdateResult, UnitUpdateParameter> {

    @EJB
    private UnitUpdateDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }


    @Override
    protected <T extends BaseParam> T initParam(UnitUpdateParameter parameter) {

        A200013Param param = new Gson().fromJson(parameter.getResult(), A200013Param.class);
        param.setUpdateDBflg(parameter.isUpdateDBflg());

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {

        if(!super.isFV2(smPrm) && !super.isFVPD(smPrm)
                && !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        List<Map<String, String>> list = ((A200013Param)param).getRecordPointSetList();

        //listサイズチェック
        if(super.isFV2(smPrm) && !(list.size() == SmControlConstants.UNIT_LOAD_LIST_FV2)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "RecordPointSetList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isFVPD(smPrm) && !(list.size() == SmControlConstants.UNIT_LOAD_LIST_FVPD)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "RecordPointSetList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isFVPaD(smPrm) && !(list.size() == SmControlConstants.UNIT_LOAD_LIST_FVP_ALPHA_D)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "RecordPointSetList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }else if(super.isFVPaG2(smPrm) && !(list.size() == SmControlConstants.UNIT_LOAD_LIST_FVP_ALPHA_G2)){
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "RecordPointSetList.size()", String.valueOf(list.size()));
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
        }

        //list内の機種依存の項目をバリデーションチェック
        for(Map<String, String> map : list) {

            //FV2、FVP(D)機種依存項目チェック
            if ((super.isFV2(smPrm) || super.isFVPD(smPrm))){

                //pulseWeightをチェック
                String pulseWeight = (String)map.get("pulseWeight");
                //桁数チェックとnullチェックを行う
                if(pulseWeight==null || !(pulseWeight.matches("[0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "pulseWeight", pulseWeight);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }

                //headScaleをチェック
                String headScale = (String)map.get("headScale");
                //桁数チェックとnullチェックを行う
                if(headScale==null || !(headScale.matches("[-+0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "headScale", headScale);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }

                //bottomScaleをチェック
                String bottomScale = (String)map.get("bottomScale");
                //桁数チェックとnullチェックを行う
                if(bottomScale==null || !(bottomScale.matches("[-+0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "bottomScale", bottomScale);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }
            }

            //FVPa(D)、FVPa(G2)機種依存項目チェック
            if (super.isFVPaD(smPrm) || super.isFVPaG2(smPrm))
            {
                //function1をチェック
                String function1 = (String)map.get("function1");
                //桁数チェックとnullチェックを行う
                if(function1==null || !(function1.matches("[0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "function1", function1);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }

                //function2をチェック
                String function2 = (String)map.get("function2");
                //桁数チェックとnullチェックを行う
                if(function2==null || !(function2.matches("[-+0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "function2", function2);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }

                //function3をチェック
                String function3 = (String)map.get("function3");
                //桁数チェックとnullチェックを行う
                if(function3==null || !(function3.matches("[-+0-9]{1,6}"))) {
                    StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                    super.loggingError(st, "function3", function3);
                    throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID,"API_ERROR_PARAMETER_VALID");
                }
            }
        }

        return true;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        // Paramクラス変更
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200013Param> res = (FvpCtrlMngResponse<A200013Param>) response;//paramクラスの型を指定

        // 定数から取得し、キャストする。
        BigDecimal dmValue = new BigDecimal( SmControlConstants.DM_VALUE );
        BigDecimal offSetValue = new BigDecimal( SmControlConstants.OFF_SET_VALUE );
        BigDecimal factorValue = new BigDecimal( SmControlConstants.FACTOR_VALUE);


        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()){
            return;
        }

        // 結果登録用のResult作成
        List<SmPointResult> paramList = new ArrayList<>();

        List< Map<String,String>> SmPointList = res.getParam().getRecordPointSetList();

        for ( int i = 0 ; i < SmPointList.size() ; i++ ) {

            SmPointResult target = new SmPointResult(); //SmPointのResultを格納
            Map<String,String> type = SmPointList.get(i); //Map<String,String>型に変換
            target.setSmId(res.getSmId());
            target.setPointNo((String) String.format("%03d", i + 1));//ゼロ埋めしてset
            target.setPointType((String) type.get("pointType"));

            // ポイント種別を以下のルールに従って変換・格納する
            // 2の場合 : 「P」を格納
            // 3の場合 : 「K」を格納
            // 4,5,6,7の場合 : 「A」を格納
            // それ以外 の場合: 「0」を格納
            // 端末アドレス、内部アドレスともに00の場合は「0」を格納

            String terminalAddress = (String) type.get("terminalAddress");
            String internalAddress = (String) type.get("internalAddress");
            if(terminalAddress.equals("00") && internalAddress.equals("00")) {
                target.setPointType(SmControlConstants.POINT_TYPE_0);
            }else {
                if(target.getPointType().equals(SmControlConstants.POINT_TYPE_4)
                        || target.getPointType().equals(SmControlConstants.POINT_TYPE_5)
                        || target.getPointType().equals(SmControlConstants.POINT_TYPE_6)
                        || target.getPointType().equals(SmControlConstants.POINT_TYPE_7)) {
                    target.setPointType(SmControlConstants.POINT_TYPE_A);
                }else if (target.getPointType().equals(SmControlConstants.POINT_TYPE_2)) {
                    target.setPointType(SmControlConstants.POINT_TYPE_P);
                }else if (target.getPointType().equals(SmControlConstants.POINT_TYPE_3)) {
                    target.setPointType(SmControlConstants.POINT_TYPE_K);
                }else {
                    target.setPointType(SmControlConstants.POINT_TYPE_0);
                }
            }

            target.setDmCorrectionFactor(dmValue);
            target.setAnalogOffSetValue(offSetValue);
            target.setAnalogConversionFactor(factorValue);
            target.setUpdateUserId(super.loginUserId);
            paramList.add(target);
        }

        // Dao呼出
        dao.UpdateSmPoint(paramList);


    }

}
