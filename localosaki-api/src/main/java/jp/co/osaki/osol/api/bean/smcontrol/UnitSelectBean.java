package jp.co.osaki.osol.api.bean.smcontrol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.dao.smcontrol.UnitSelectDao;
import jp.co.osaki.osol.api.parameter.smcontrol.UnitSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.SmPointResult;
import jp.co.osaki.osol.api.result.smcontrol.UnitSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200012Param;
import jp.co.osaki.osol.mng.param.BaseParam;


/**
*
* ユニット(取得) Bean クラス
*
* @autho d_yamano
*
*/
@Named(value = SmControlConstants.UNIT_SELECT)
@RequestScoped
public class UnitSelectBean extends AbstractApiBean<UnitSelectResult, UnitSelectParameter> {

    @EJB
    private UnitSelectDao dao;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(UnitSelectParameter parameter) {
        A200012Param param = new A200012Param();

        if (parameter != null) {
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

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

        return true;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        // Paramクラス変更
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200012Param> res = (FvpCtrlMngResponse<A200012Param>) response;//paramクラスの型を指定

        // 定数から取得し、キャストする。
        BigDecimal dmValue = new BigDecimal( SmControlConstants.DM_VALUE );
        BigDecimal offSetValue = new BigDecimal( SmControlConstants.OFF_SET_VALUE );
        BigDecimal factorValue = new BigDecimal( SmControlConstants.FACTOR_VALUE);


        //if DBフラグがOFFなら処理無し
        if(!super.apiParameter.isUpdateDBflg()){//superクラスからDBflgを取得
            return;
        }

        // 結果登録用のResult作成
        List<SmPointResult> paramList = new ArrayList<>();

        List< Map<String,String>> smPointList = res.getParam().getRecordPointSetList();

        for ( int i = 0 ; i < smPointList.size() ; i++ ) {

            SmPointResult target = new SmPointResult(); //SmPointのResultを格納
            Map<String,String> type = smPointList.get(i); //Map<String,String>型に変換
            target.setSmId(res.getSmId());
            target.setPointNo((String) String.format("%03d", i + 1));//ゼロ埋めしてset
            target.setPointType((String) type.get("pointType"));

            // ポイント種別を以下のルールに従って変換・格納する
            // 2の場合 : 「P」を格納
            // 3の場合 : 「K」を格納
            // 4,5,6,7の場合 : 「A」を格納
            // それ以外 の場合: 「0」を格納
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
