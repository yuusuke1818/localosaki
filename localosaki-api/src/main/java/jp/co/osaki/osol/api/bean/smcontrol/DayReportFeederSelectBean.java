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
import jp.co.osaki.osol.api.dao.smcontrol.DayReportFeederSelectDao;
import jp.co.osaki.osol.api.dao.smcontrol.SmCntrolDao;
import jp.co.osaki.osol.api.parameter.smcontrol.DayReportFeederSelectParameter;
import jp.co.osaki.osol.api.result.smcontrol.DayReportFeederSelectResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.mng.FvpCtrlMngRequest;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.SmControlException;
import jp.co.osaki.osol.mng.param.A200064Param;
import jp.co.osaki.osol.mng.param.BaseParam;
import jp.co.osaki.osol.utility.SmControlUtility;

/**
 * フィーダ日報（取得）Beanクラス
 * @author ya-ishida
 *
 */
@Named(value = "DayReportFeederSelectBean")
@RequestScoped
public class DayReportFeederSelectBean extends AbstractApiBean<DayReportFeederSelectResult, DayReportFeederSelectParameter> {

    @EJB
    private DayReportFeederSelectDao dayReportFeederSelectDao;

    /**
     * ポイントグループリスト
     */
    private List<String> pointGroupList;

    /**
     * N日前
     */
    private String daysAgo;

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
        return dayReportFeederSelectDao;
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.bean.smcontrol.AbstractApiBean#initParam(jp.co.osaki.osol.api.parameter.smcontrol.BaseSmControlApiParameter)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> T initParam(DayReportFeederSelectParameter parameter) {

        A200064Param param = new A200064Param();

        if(param != null) {
            param.setDaysAgo(parameter.getDaysAgo());
            param.setUpdateDBflg(parameter.isUpdateDBflg());
            this.daysAgo = parameter.getDaysAgo();
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
        FvpCtrlMngResponse<A200064Param> res = (FvpCtrlMngResponse<A200064Param>) response;

        //param生成
        A200064Param param = (A200064Param)res.getParam();

        //最終的に返却するポイントグループリスト
        List<Map<String, Object>> pointGroupList = new ArrayList<>();

        //最終的に返却するポイントリスト
        List<Map<String, Object>> pointList = new ArrayList<>();

        //初回の結果を取得
        List<Map<String,List<Map<String,String>>>> pointDataList = param.getPointDataList();
        List<String> pointNoList = SmControlUtility.getPointListFromPointGroup(param.getPointGroup(), param.getProductCd());

        if(pointDataList != null && !pointDataList.isEmpty()) {
            int i = 0;
            for(Map<String,List<Map<String,String>>> pointData : pointDataList) {
                Map<String, Object> pointMap = new HashMap<>();
                pointMap.put("pointNo", pointNoList.get(i));
                pointMap.putAll(pointData);
                pointList.add(pointMap);
                i++;
            }
            //ポイントグループリストに追加
            Map<String, Object> pointGroup = new HashMap<>();
            pointGroup.put("measureDayTime", param.getMeasureDayTime());
            pointGroup.put("aggregateHour", param.getAggregateHour());
            pointGroup.put("pointList", pointList);
            pointGroupList.add(pointGroup);
        }



        //2件目以降のポイントグループの処理を行う
        for(int i = 1;i < this.pointGroupList.size();i++) {
            //新規parameter生成
            A200064Param parameter = new A200064Param();
            parameter.setDaysAgo(this.daysAgo);
            parameter.setUpdateDBflg(this.updateFlg);
            parameter.setPointGroup(this.pointGroupList.get(i));
            pointList = new ArrayList<>();

            //機器通信リクエスト生成
            FvpCtrlMngRequest<BaseParam> req =new FvpCtrlMngRequest<>(smPrm,super.apiParameter.getLoginCorpId(),
                    super.apiParameter.getLoginPersonId(),super.loginUserId);
            req.setParam(parameter);

            //機器通信呼び出し
            FvpCtrlMngResponse<BaseParam> baseRes = fvpCtrlMngClient.excute(req);
            A200064Param paramF = (A200064Param)baseRes.getParam();
            pointDataList = paramF.getPointDataList();
            pointNoList = SmControlUtility.getPointListFromPointGroup(paramF.getPointGroup(), paramF.getProductCd());

            if(pointDataList != null && !pointDataList.isEmpty()) {
                int j = 0;
                pointList = new ArrayList<>();
                for(Map<String,List<Map<String,String>>> pointData : pointDataList) {
                    Map<String, Object> pointMap = new HashMap<>();
                    pointMap.put("pointNo", pointNoList.get(j));
                    pointMap.putAll(pointData);
                    pointList.add(pointMap);
                    j++;
                }
                //ポイントグループリストに追加
                Map<String, Object> pointGroup = new HashMap<>();
                pointGroup.put("measureDayTime", param.getMeasureDayTime());
                pointGroup.put("aggregateHour", param.getAggregateHour());
                pointGroup.put("pointList", pointList);
                pointGroupList.add(pointGroup);
            }
        }

        // コマンド(文字列を復元)
        // 機器からの応答電文をBean変換した際に分割しているため、ここで復元
        String command = param.getCommand() + param.getPointGroup() + param.getDaysAgo();

        // レスポンスParamを設定
        param.setCommand(command);
        param.setPointGroupList(pointGroupList);

        // レスポンスに格納
        res.setParam(param);
    }

}
