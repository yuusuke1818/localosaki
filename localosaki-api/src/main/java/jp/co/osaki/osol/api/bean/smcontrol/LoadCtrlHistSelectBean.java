package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.LoadCtrlHistSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.LoadCtrlHistSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.LoadControlLogResult;
import jp.co.osaki.osol.api.result.smcontrol.LoadCtrlHistSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.param.A200027Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.DateUtility;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 負荷制御履歴(取得) Bean クラス
 *
 * @author t_sakamoto
 *
 */

@Named(value = SmControlConstants.LOAD_CTRL_HIST_SELECT)
@RequestScoped
public class LoadCtrlHistSelectBean extends AbstractApiBean<LoadCtrlHistSelectResult, LoadCtrlHistSelectParameter> {

    @EJB
    private LoadCtrlHistSelectDao dao;

    // 設定変更履歴
    private int SETTING_HIST = 0;

    // 機器情報
    private SmPrmResultData SM_PRM;

    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return dao;
    }

    @Override
    protected <T extends BaseParam> T initParam(LoadCtrlHistSelectParameter parameter) {
        A200027Param param = new A200027Param();

        //リクエスト仕様内、固有の値を設定
        if (parameter != null) {
            // 設定変更履歴
            param.setSettingChangeHist(parameter.getSettingChangeHist());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
        }

        if (param.isUpdateDBflg()) {

            // DBフラグがTrueの場合、設定変更履歴の指定がある場合NG (履歴系固有処理)
            if (parameter.getSettingChangeHist() == null || parameter.getSettingChangeHist().isEmpty()) {
                @SuppressWarnings("unchecked")
                T ret = (T) param;

                return ret;
            } else {

                //親クラスで入力バリデーションの処理に入るため設定変更履歴に想定しない値を設定
                param.setSettingChangeHist("ERROR_VALUE");

                @SuppressWarnings("unchecked")
                T ret = (T) param;

                return ret;
            }
        }

        @SuppressWarnings("unchecked")
        T ret = (T) param;

        return ret;
    }

    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        // 機種依存チェック(FV2, FVP(D), FVPa(D), FVPa(G2), Ea, Ea2以外はエラー)
        if (!super.isFV2(smPrm) && !super.isFVPD(smPrm)
                && !super.isFVPaD(smPrm) && !super.isFVPaG2(smPrm)
                && !super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        // 機器情報を保持
        this.SM_PRM = smPrm;

        return true;
    }

    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        // param取得
        @SuppressWarnings("unchecked")
        FvpCtrlMngResponse<A200027Param> res = (FvpCtrlMngResponse<A200027Param>) response;
        A200027Param resParam = new A200027Param();
        resParam = (A200027Param) response.getParam();

        //if DBフラグがOFFなら処理無し
        if (!super.apiParameter.isUpdateDBflg()) {
            return;
        }

        // 機器から取得した履歴が前回履歴に遡るまで繰り返し行う
        for (int i = 0; i < SmControlConstants.LOAD_CONTROL_LOG_RETRY_CNT; i++) {

            // 取得した100件中、最古データ取得
            Map<String, String> loadCtrlHist = resParam.getLoadCtrlHistList()
                    .get(resParam.getLoadCtrlHistList().size() - 1);

            // DB検索用ResultSet
            LoadControlLogResult target = new LoadControlLogResult();

            // 主キーセット
            target.setSmId(this.SM_PRM.getSmId());
            String recordDayTime = loadCtrlHist.get("recordDayTime");
            //記録日時は、西暦が2桁のため、4桁に修正して、主キー検索を行う
            recordDayTime = DateUtility.changeDateFormat(
                    DateUtility.conversionDate(recordDayTime, DateUtility.DATE_FORMAT_YYMMDDHHMM),
                    DateUtility.DATE_FORMAT_YYYYMMDDHHMM);
            target.setRecordYMDHM(recordDayTime);
            target.setRestMS(loadCtrlHist.get("leftMinute") + loadCtrlHist.get("leftSeconds"));

            if (super.isFV2(this.SM_PRM)) { // FV2の場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoFV2"));
            } else if (super.isFVPD(this.SM_PRM)) { // FVPDの場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoFVPD"));
            } else if (super.isFVPaD(this.SM_PRM)) { // FVPaDの場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoFVPaD"));
            } else if (super.isFVPaG2(this.SM_PRM)) { // FVPaG2の場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoFVPaG2"));
            } else if (super.isEa(this.SM_PRM)) { // Eαの場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoEa"));
            } else if (super.isEa2(this.SM_PRM)) { // Eα2の場合
                target.setControlStatus(loadCtrlHist.get("loadCtrlInfoEa2"));
            }

            // 最古レコードチェック
            if (target.getControlStatus().trim() == "" || dao.SelectLoadCntlLog(target) != null) {
                // 空文字が格納されている場合、機器に登録されている履歴を全て取得したとし、forを抜ける
                // dao処理後、値が返却される場合、機器から取得した100件中に履歴差分が収まっているため、forを抜ける
                break;
            }

            // αシリーズ以外は履歴数が少ない為、取得できる最大数に達した場合は処理を抜ける
            if (!super.isFVPaD(this.SM_PRM) && !super.isFVPaG2(this.SM_PRM)) {
                if (i >= SmControlConstants.LOAD_CONTROL_LOG_RETRY_CNT_OTHER_ALPHA) {
                    break;
                }
            }

            // リクエストパラメータ取得
            LoadCtrlHistSelectParameter reParameter = (LoadCtrlHistSelectParameter) super.apiParameter;

            // 設定変更履歴 +1
            this.SETTING_HIST = this.SETTING_HIST + 1;

            // Paramセット
            A200027Param reParam = new A200027Param();

            reParam.setSettingChangeHist(String.valueOf(this.SETTING_HIST)); // 設定変更履歴
            reParam.setUpdateDBflg(super.apiParameter.isUpdateDBflg()); // DB登録フラグ

            // Fvpリクエスト作成
            FvpCtrlMngRequest<BaseParam> req = new FvpCtrlMngRequest<>(this.SM_PRM, reParameter.getLoginCorpId(),
                    reParameter.getLoginPersonId(), super.loginUserId);
            req.setParam(reParam);

            FvpCtrlMngResponse<?> reRes = new FvpCtrlMngResponse<BaseParam>();
            // 機器通信呼び出し
            try {
                reRes = fvpCtrlMngClient.excute(req);
                if ((super.isEa(this.SM_PRM)) || (super.isEa2(this.SM_PRM))) {
                    if (!OsolApiResultCode.API_OK.equals(reRes.getFvpResultCd())) {
                        break;
                    }
                }
            } catch (SmControlException e) {
                StackTraceElement st = Thread.currentThread().getStackTrace()[1];
                super.loggingError(st, e);
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                throw e;
            }

            // Param変換
            @SuppressWarnings("unchecked")
            FvpCtrlMngResponse<A200027Param> myRes = (FvpCtrlMngResponse<A200027Param>) reRes;

            // Param抽出
            A200027Param reResultParam = myRes.getParam();

            // 最新データに過去データを追加
            for (Map<String, String> hist : reResultParam.getLoadCtrlHistList()) {
                resParam.getLoadCtrlHistList().add(hist);
            }
        }

        // 追加取得したParamセット
        res.setParam(resParam);
        // 親の引数を上書き
        response = res;

    }

}
