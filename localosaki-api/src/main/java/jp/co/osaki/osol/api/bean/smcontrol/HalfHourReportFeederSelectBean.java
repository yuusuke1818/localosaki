package jp.co.osaki.osol.api.bean.smcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.smcontrol.HalfHourReportFeederSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.HalfHourReportFeederSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.HalfHourReportFeederSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200066Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.SmControlUtility;

/**
 * 30分フィーダ計測データ（取得）Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "HalfHourReportFeederSelectBean")
@RequestScoped
public class HalfHourReportFeederSelectBean extends AbstractApiBean<HalfHourReportFeederSelectResult, HalfHourReportFeederSelectParameter> {

    @EJB
    private HalfHourReportFeederSelectDao halfHourReportFeederSelectDao;

    /**
     * 取得回数
     */
    private String historyCount;

    /**
     * ポイントグループリスト
     */
    private List<String> pointGroupList;

    /**
     * DB更新フラグ
     */
    private boolean updateFlg;

    /**
     * 機器情報
     */
    private SmPrmResultData smPrm;

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#getSmCntrolDao()
     */
    @Override
    protected SmCntrolDao getSmCntrolDao() {
        return halfHourReportFeederSelectDao;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#initParam(jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> T initParam(HalfHourReportFeederSelectParameter parameter) {

        A200066Param param = new A200066Param();

        if(param != null) {
            param.setHistoryCount(parameter.getHistoryCount());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
            this.historyCount = parameter.getHistoryCount();
            this.updateFlg = parameter.isUpdateDBflg();

            if(parameter.getPointGroupList() == null || parameter.getPointGroupList().isEmpty()) {
                return (T) param;
            }else {
                this.pointGroupList = Arrays.asList(parameter.getPointGroupList().replace("[", "").replace("]", "").split(","));
                //ポイントグループリストの1番目をセット
                param.setPointGroup(pointGroupList.get(0));
            }
        }

        return (T) param;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#checkSmPrm(jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData, jp.co.osaki.osol.mng.param.BaseParam)
     */
    @Override
    protected boolean checkSmPrm(SmPrmResultData smPrm, BaseParam param) throws SmControlException {
        this.smPrm = smPrm;
        // 機種依存チェック(Ea, Ea2以外はエラー)
        if (!super.isEa(smPrm) && !super.isEa2(smPrm)) {
            StackTraceElement st = Thread.currentThread().getStackTrace()[1];
            super.loggingError(st, "PRODUCT_CD", smPrm.getProductCd());
            throw new SmControlException(OsolApiResultCode.API_ERROR_PARAMETER_VALID, "API_ERROR_PARAMETER_VALID");
        }

        return true;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#callDao(jp.co.osaki.osol.mng.FvpCtrlMngResponse)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void callDao(FvpCtrlMngResponse<?> response) throws Exception {

        //パラメータ取得
        FvpCtrlMngResponse<A200066Param> res = (FvpCtrlMngResponse<A200066Param>) response;

        //param生成
        A200066Param param = (A200066Param)res.getParam();

        //最終的に返却するポイントグループリスト
        List<Map<String, Object>> pointGroupList = new ArrayList<>();

        //最終的に返却するポイントリスト
        List<Map<String, Object>> pointList = new ArrayList<>();

        //初回の結果を取得
        List<Map<String, String>> pointDataTempList = param.getPointDataTempList();
        List<String> pointNoList = SmControlUtility.getPointListFromPointGroup(param.getPointGroup(), param.getProductCd());

        //取得回数を取得する
        int historyCount = param.getHistoryCountNum();
        if(historyCount == 0) {
            historyCount = 12;
        }

        if(pointDataTempList != null && !pointDataTempList.isEmpty()) {

            if(pointDataTempList.size() % A200066Param.POINT_COUNT_EA == 0
                    && pointDataTempList.size() % historyCount == 0) {
                //16および取得回数で割り切れる場合のみ処理を行う
                int i = 0;
                Map<String, Object> pointMap = null;
                List<Map<String, String>> timeValueList = null;
                for(Map<String, String> pointDataTemp : pointDataTempList) {
                    if(i == 0 || i % historyCount == 0) {
                        //初回または指定取得回数の倍数の場合
                        if(i != 0) {
                            pointMap.put("timeValueList", timeValueList);
                            pointList.add(pointMap);
                        }
                        pointMap = new HashMap<>();
                        timeValueList = new ArrayList<>();
                        pointMap.put("pointNo", pointNoList.get(i/historyCount));
                    }
                    timeValueList.add(pointDataTemp);
                    i++;
                }

                //最終レコードのセット
                pointMap.put("timeValueList", timeValueList);
                pointList.add(pointMap);

                //ポイントグループリストに追加
                Map<String, Object> pointGroup = new HashMap<>();
                pointGroup.put("measureDayTime", param.getMeasureDayTime());
                pointGroup.put("pointList", pointList);
                pointGroupList.add(pointGroup);
            }

        }

        //2件目以降のポイントグループの処理を行う
        for(int i = 1;i < this.pointGroupList.size();i++) {
            //新規parameter生成
            A200066Param parameter = new A200066Param();
            parameter.setHistoryCount(this.historyCount);
            parameter.setUpdateDBflg(this.updateFlg);
            parameter.setPointGroup(this.pointGroupList.get(i));
            pointList = new ArrayList<>();

            //機器通信リクエスト生成
            FvpCtrlMngRequest<BaseParam> req =new FvpCtrlMngRequest<>(smPrm,super.apiParameter.getLoginCorpId(),
                    super.apiParameter.getLoginPersonId(),super.loginUserId);
            req.setParam(parameter);

            //機器通信呼び出し
            FvpCtrlMngResponse<BaseParam> baseRes = fvpCtrlMngClient.excute(req);
            A200066Param paramF = (A200066Param)baseRes.getParam();
            pointDataTempList = paramF.getPointDataTempList();
            pointNoList = SmControlUtility.getPointListFromPointGroup(paramF.getPointGroup(), paramF.getProductCd());

            if(pointDataTempList != null && !pointDataTempList.isEmpty()) {

                if(pointDataTempList.size() % A200066Param.POINT_COUNT_EA == 0
                        && pointDataTempList.size() % historyCount == 0) {
                    //16および取得回数で割り切れる場合のみ処理を行う
                    int j = 0;
                    Map<String, Object> pointMap = null;
                    List<Map<String, String>> timeValueList = null;
                    for(Map<String, String> pointDataTemp : pointDataTempList) {
                        if(j == 0 || j % historyCount == 0) {
                            //初回または指定取得回数の倍数の場合
                            if(j != 0) {
                                pointMap.put("timeValueList", timeValueList);
                                pointList.add(pointMap);
                            }
                            pointMap = new HashMap<>();
                            timeValueList = new ArrayList<>();
                            pointMap.put("pointNo", pointNoList.get(j/historyCount));
                        }
                        timeValueList.add(pointDataTemp);
                        j++;
                    }

                    //最終レコードのセット
                    pointMap.put("timeValueList", timeValueList);
                    pointList.add(pointMap);

                    //ポイントグループリストに追加
                    Map<String, Object> pointGroup = new HashMap<>();
                    pointGroup.put("measureDayTime", param.getMeasureDayTime());
                    pointGroup.put("pointList", pointList);
                    pointGroupList.add(pointGroup);
                }

            }
        }

        // コマンド(文字列を復元)
        // 機器からの応答電文をBean変換した際に分割しているため、ここで復元
        String command = param.getCommand() + param.getPointGroup() + String.format("%02d", param.getHistoryCountNum());

        // レスポンスParamを設定
        param.setCommand(command);
        param.setPointGroupList(pointGroupList);

        // レスポンスに格納
        res.setParam(param);
    }

}
