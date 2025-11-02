package jp.co.osaki.osol.api.dao.analysis;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.analysis.AnalysisEmsReservationInfoUpdateParameter;
import jp.co.osaki.osol.api.result.analysis.AnalysisEmsReservationInfoUpdateResult;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsCategoryInfoJson;
import jp.co.osaki.osol.api.resultdata.analysis.AnalysisEmsReservationInfoResultData;
import jp.co.osaki.osol.api.servicedao.analysis.AnalysisEmsReservationInfoServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.utility.AnalysisEmsUtility;

/**
 * 集計・分析 EMS実績 予約情報更新(DL予約) Daoクラス
 * @author nishida.t
 *
 */
@Stateless
public class AnalysisEmsReservationInfoUpdateDao extends OsolApiDao<AnalysisEmsReservationInfoUpdateParameter> {

    private final AnalysisEmsReservationInfoServiceDaoImpl analysisEmsReservationInfoServiceDaoImpl;

    public AnalysisEmsReservationInfoUpdateDao() {
        analysisEmsReservationInfoServiceDaoImpl = new AnalysisEmsReservationInfoServiceDaoImpl();
    }

    @Override
    public AnalysisEmsReservationInfoUpdateResult query(AnalysisEmsReservationInfoUpdateParameter parameter) throws Exception {
        AnalysisEmsReservationInfoUpdateResult result = new AnalysisEmsReservationInfoUpdateResult();

        if (parameter.getReservationInfoListFlg() == null) {
            parameter.setReservationInfoListFlg(OsolConstants.FLG_OFF);
        }

        // 現在日時（予約日時）
        Timestamp svDate = getServerDateTime();

        // 担当者情報
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();

        // Json(String) → Json(Object)に変換
        AnalysisEmsCategoryInfoJson jsonObj;
        try {
            jsonObj = AnalysisEmsUtility.changeToObject(parameter.getAggregateConditionResultSet());
        } catch (JsonSyntaxException e) {
            return null;
        }

        // 更新チェック
        boolean updateFlg = false;
        updateFlg = AnalysisEmsUtility.isUpdateCheck(jsonObj);

        if (updateFlg) {
            AnalysisEmsReservationInfoResultData searchParam = new AnalysisEmsReservationInfoResultData();
            searchParam.setCorpId(jsonObj.getCorpId());
            searchParam.setPersonCorpId(jsonObj.getPersonCorpId());
            searchParam.setPersonId(jsonObj.getPersonId());
            searchParam.setAggregateId(jsonObj.getAggregateId());

            AnalysisEmsReservationInfoResultData updateData = find(analysisEmsReservationInfoServiceDaoImpl, searchParam);
            // レコード存在チェック
            if (updateData == null) {
                return null;
            }

            // 集計処理中止フラグONの場合
            if (OsolConstants.FLG_ON.equals(parameter.getAggregateProcessStopFlg())) {
                if (ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.BEFORE_PROCESSING.getVal().equals(updateData.getAggregateProcessStatus())) {
                    // 処理前の場合、処理ステータスを「中止済み」に更新
                    updateData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING_CANCEL.getVal());
                    updateData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.OK.getVal());
                } else if (ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.NOW_PROCESSING.getVal().equals(updateData.getAggregateProcessStatus())) {
                    // 処理中の場合
                    updateData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.NOW_PROCESSING_CANCEL.getVal());
                    updateData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.WAIT.getVal());
                } else if (ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING.getVal().equals(updateData.getAggregateProcessStatus())) {
                    // 処理終了の場合
                    updateData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.AFTER_PROCESSING_CANCEL.getVal());
                    updateData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.OK.getVal());
                    updateData.setAggregateEndDate(svDate);
                }
            } else {
                updateData.setAggregateProcessStatus(parameter.getAggregateProcessStatus());
                updateData.setAggregateProcessResult(parameter.getAggregateProcessResult());
            }

            // 更新
            updateData.setOutputFilePath(parameter.getOutputFilePath());
            updateData.setOutputFileName(parameter.getOutputFileName());
            updateData.setUpdateUserId(userId);
            updateData.setUpdateDate(svDate);
            merge(analysisEmsReservationInfoServiceDaoImpl, updateData);

        } else {
            // 集計ID取得
            Long aggregateId = createAggregateId();

            // Json(Object)に主キーの要素を追加する
            jsonObj = addElementJson(jsonObj, parameter.getOperationCorpId(), parameter.getLoginCorpId(), parameter.getLoginPersonId(), aggregateId);

            // 登録
            AnalysisEmsReservationInfoResultData insertData = new AnalysisEmsReservationInfoResultData();
            insertData.setCorpId(parameter.getOperationCorpId());
            insertData.setPersonCorpId(parameter.getLoginCorpId());
            insertData.setPersonId(parameter.getLoginPersonId());
            insertData.setAggregateId(aggregateId);
            insertData.setReservationDate(svDate);
            insertData.setAggregateCondition(AnalysisEmsUtility.changeToJson(jsonObj));
            insertData.setAggregateProcessStatus(ApiGenericTypeConstants.AGGREGATE_PROCESS_STATUS.BEFORE_PROCESSING.getVal());
            insertData.setAggregateProcessResult(ApiGenericTypeConstants.AGGREGATE_PROCESS_RESULT.WAIT.getVal());
            insertData.setDelFlg(OsolConstants.FLG_OFF);
            insertData.setVersion(0);
            insertData.setCreateUserId(userId);
            insertData.setCreateDate(svDate);
            insertData.setUpdateUserId(userId);
            insertData.setUpdateDate(svDate);
            persist(analysisEmsReservationInfoServiceDaoImpl, insertData);

        }

        // 予約情報取得フラグがONの場合取得
        if (OsolConstants.FLG_ON.equals(parameter.getReservationInfoListFlg())) {
            // 最新の集計分析予約情報を取得
            AnalysisEmsReservationInfoResultData serachData = new AnalysisEmsReservationInfoResultData();
            serachData.setCorpId(parameter.getOperationCorpId());
            List<AnalysisEmsReservationInfoResultData> resultList = getResultList(analysisEmsReservationInfoServiceDaoImpl, serachData);
            result.setAggregateReservationInfoList(resultList);
        }


        return result;
    }

    /**
     * 企業ID、担当者企業ID、担当者ID、集計IDを追加して返却する
     * @param JsonStr
     * @param corpId
     * @param personCorpId
     * @param personId
     * @return
     */
    private AnalysisEmsCategoryInfoJson addElementJson(AnalysisEmsCategoryInfoJson analysisEmsCategoryInfoJson,
            String corpId, String personCorpId, String personId, Long aggregateId) {

        Gson gson = new Gson();
        JsonObject jsonSrcObj = gson.fromJson(gson.toJson(analysisEmsCategoryInfoJson), JsonObject.class);

        JsonObject jsonObj= new JsonObject();

        // Json要素に、企業ID、担当者企業ID、担当者ID、集計IDを追加
        if (corpId != null) jsonObj.addProperty("corpId", corpId);
        if (personCorpId != null) jsonObj.addProperty("personCorpId", personCorpId);
        if (personId != null) jsonObj.addProperty("personId", personId);
        if (aggregateId != null) jsonObj.addProperty("aggregateId", aggregateId);

        for (Map.Entry<String, JsonElement> entry : jsonSrcObj.getAsJsonObject().entrySet()) {
            jsonObj.add(entry.getKey(), entry.getValue());
        }
        return gson.fromJson(gson.toJson(jsonObj), AnalysisEmsCategoryInfoJson.class);
    }

    /**
    *
    * 集計ID採番
    *
    * @return 新規採番された建物ID
    */
   private Long createAggregateId() {

       return  super.createId(OsolConstants.ID_SEQUENCE_NAME.AGGREGATE_ID.getVal());

   }
}
