package jp.co.osaki.osol.api.dao.plan;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiParamKeyConstants;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentResultWriteParameter;
import jp.co.osaki.osol.api.request.plan.PlanFulfillmentResultWriteRequest;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentResultWriteResult;
import jp.co.osaki.osol.api.resultdata.plan.PlanFulfillmentResultDetailResultData;
import jp.co.osaki.osol.api.servicedao.entity.TPlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.plan.PlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.entity.TPlanFulfillmentResult;
import jp.co.osaki.osol.entity.TPlanFulfillmentResultPK;
import jp.co.osaki.osol.utility.DateUtility;

/**
 * 計画履行実績入力 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class PlanFulfillmentResultWriteDao extends OsolApiDao<PlanFulfillmentResultWriteParameter> {

    private final TPlanFulfillmentResultServiceDaoImpl tPlanFulfillmentResultServiceDaoImpl;
    private final PlanFulfillmentResultServiceDaoImpl planFulfillmentResultServiceDaoImpl;

    public PlanFulfillmentResultWriteDao() {
        tPlanFulfillmentResultServiceDaoImpl = new TPlanFulfillmentResultServiceDaoImpl();
        planFulfillmentResultServiceDaoImpl = new PlanFulfillmentResultServiceDaoImpl();
    }

    @Override
    public PlanFulfillmentResultWriteResult query(PlanFulfillmentResultWriteParameter parameter) throws Exception {
        List<TPlanFulfillmentResult> updateList = new ArrayList<>();

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //JSON⇒Resultに変換
        PlanFulfillmentResultWriteRequest resultSet = parameter.getResultSet();

        if (resultSet.getDetailList() == null || resultSet.getDetailList().isEmpty()) {
            return new PlanFulfillmentResultWriteResult();
        }

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //計画履行実績の排他チェック
        for (PlanFulfillmentResultDetailResultData detail : resultSet.getDetailList()) {
            TPlanFulfillmentResult updateParam = new TPlanFulfillmentResult();
            TPlanFulfillmentResultPK pkUpdateParam = new TPlanFulfillmentResultPK();
            pkUpdateParam.setCorpId(detail.getCorpId());
            pkUpdateParam.setBuildingId(detail.getBuildingId());
            pkUpdateParam.setPlanFulfillmentId(detail.getPlanFulfillmentId());
            pkUpdateParam.setPlanFulfillmentResultId(detail.getPlanFulfillmentResultId());
            updateParam.setId(pkUpdateParam);
            TPlanFulfillmentResult updatePlan = find(tPlanFulfillmentResultServiceDaoImpl, updateParam);
            if (updatePlan == null) {
                //データが取得できなかった場合は新規登録と判断
                updateList.add(null);
            } else {
                //データが取得できた場合はバージョンチェックを行う
                if (!detail.getVersion().equals(updatePlan.getVersion())) {
                    //Versionが異なる場合は、排他エラーとする
                    throw new OptimisticLockException();
                } else {
                    //同じ場合は更新情報を保持
                    updateList.add(updatePlan);
                }
            }
        }

        //計画履行実績の更新
        int i = 0;
        for (PlanFulfillmentResultDetailResultData detail : resultSet.getDetailList()) {
            TPlanFulfillmentResult updatePlan = new TPlanFulfillmentResult();
            TPlanFulfillmentResultPK pkUpdatePlan = new TPlanFulfillmentResultPK();
            if (updateList.get(i) == null) {
                //新規登録
                pkUpdatePlan.setCorpId(detail.getCorpId());
                pkUpdatePlan.setBuildingId(detail.getBuildingId());
                pkUpdatePlan.setPlanFulfillmentId(detail.getPlanFulfillmentId());
                pkUpdatePlan.setPlanFulfillmentResultId(
                        createId(OsolConstants.ID_SEQUENCE_NAME.PLAN_FULFILLMENT_RESULT_ID.getVal()));
                updatePlan.setId(pkUpdatePlan);

                updatePlan.setPlanFulfillmentDate(new Timestamp(detail.getPlanFulfillmentDate().getTime()));
                updatePlan.setPlanFulfillmentResult(detail.getPlanFulfillmentResult());
                updatePlan.setVersion(0);
                updatePlan.setDelFlg(0);
                updatePlan.setCreateUserId(loginUserId);
                updatePlan.setCreateDate(serverDateTime);
                updatePlan.setUpdateUserId(loginUserId);
                updatePlan.setUpdateDate(serverDateTime);
                persist(tPlanFulfillmentResultServiceDaoImpl, updatePlan);
            } else {
                //更新
                updatePlan = updateList.get(i);
                updatePlan.setPlanFulfillmentDate(new Timestamp(detail.getPlanFulfillmentDate().getTime()));
                updatePlan.setPlanFulfillmentResult(detail.getPlanFulfillmentResult());
                updatePlan.setDelFlg(detail.getDelFlg());
                updatePlan.setVersion(detail.getVersion());
                updatePlan.setUpdateUserId(loginUserId);
                updatePlan.setUpdateDate(serverDateTime);
                merge(tPlanFulfillmentResultServiceDaoImpl, updatePlan);
            }

            i++;
        }

        //最新の実績情報の取得

        return getUpdatePlanData(parameter);
    }

    /**
     * 更新後の情報を取得する
     * @param result
     * @return
     */
    private PlanFulfillmentResultWriteResult getUpdatePlanData(PlanFulfillmentResultWriteParameter parameter)
            throws Exception {
        PlanFulfillmentResultWriteResult result = new PlanFulfillmentResultWriteResult();

        // 実績
        Map<String, List<Object>> paramMap = new HashMap<>();
        List<Object> corpIdList = new ArrayList<>();
        corpIdList.add(parameter.getOperationCorpId());
        List<Object> buildingIdList = new ArrayList<>();
        buildingIdList.add(parameter.getBuildingId());
        List<Object> planIdList = new ArrayList<>();
        planIdList.add(parameter.getPlanFulfillmentId());
        List<Object> startDateList = new ArrayList<>();
        startDateList.add(DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD));

        Date endDate = DateUtility.plusMonth(DateUtility.conversionDate(
                DateUtility.changeDateFormat(parameter.getPlanStartDate(), DateUtility.DATE_FORMAT_YYYYMMDD),
                DateUtility.DATE_FORMAT_YYYYMMDD), 1);
        endDate = DateUtility.plusDay(endDate, -1);
        List<Object> endDateList = new ArrayList<>();

        endDateList.add(DateUtility.changeDateFormat(endDate, DateUtility.DATE_FORMAT_YYYYMMDD));
        paramMap.put(ApiParamKeyConstants.CORP_ID, corpIdList);
        paramMap.put(ApiParamKeyConstants.BUILDING_ID, buildingIdList);
        paramMap.put(ApiParamKeyConstants.PLAN_ID, planIdList);
        paramMap.put(ApiParamKeyConstants.PLAN_START_DATE, startDateList);
        paramMap.put(ApiParamKeyConstants.PLAN_END_DATE, endDateList);

        result.setDetailList(getResultList(planFulfillmentResultServiceDaoImpl, paramMap));
        return result;
    }

}
