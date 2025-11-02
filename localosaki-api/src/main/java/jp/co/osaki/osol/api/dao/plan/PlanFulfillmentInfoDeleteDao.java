/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.plan;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.plan.PlanFulfillmentInfoDeleteParameter;
import jp.co.osaki.osol.api.result.plan.PlanFulfillmentInfoDeleteResult;
import jp.co.osaki.osol.api.servicedao.entity.MPersonApiServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TBuildingPlanFulfillmentServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TPlanFulfillmentInfoServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.TPlanFulfillmentResultServiceDaoImpl;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.MPersonPK;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillment;
import jp.co.osaki.osol.entity.TBuildingPlanFulfillmentPK;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfo;
import jp.co.osaki.osol.entity.TPlanFulfillmentInfoPK;
import jp.co.osaki.osol.entity.TPlanFulfillmentResult;
import jp.co.osaki.osol.entity.TPlanFulfillmentResultPK;

/**
 * 計画履行情報削除 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class PlanFulfillmentInfoDeleteDao extends OsolApiDao<PlanFulfillmentInfoDeleteParameter> {

    private final MPersonApiServiceDaoImpl mPersonServiceDaoImpl;

    private final TPlanFulfillmentInfoServiceDaoImpl planFulfillmentWriteServiceDaoImpl;

    private final TPlanFulfillmentResultServiceDaoImpl tPlanFulfillmentResultServiceDaoImpl;

    private final TBuildingPlanFulfillmentServiceDaoImpl tBuildingPlanFulfillmentServiceDaoImpl;

    public PlanFulfillmentInfoDeleteDao() {
        mPersonServiceDaoImpl = new MPersonApiServiceDaoImpl();
        planFulfillmentWriteServiceDaoImpl = new TPlanFulfillmentInfoServiceDaoImpl();
        tPlanFulfillmentResultServiceDaoImpl = new TPlanFulfillmentResultServiceDaoImpl();
        tBuildingPlanFulfillmentServiceDaoImpl = new TBuildingPlanFulfillmentServiceDaoImpl();
    }

    @Override
    public PlanFulfillmentInfoDeleteResult query(PlanFulfillmentInfoDeleteParameter parameter) throws Exception {
        PlanFulfillmentInfoDeleteResult ret = new PlanFulfillmentInfoDeleteResult();
        // ユーザーIDを取得
        MPerson personEntity = new MPerson();
        MPersonPK personPk = new MPersonPK();
        personPk.setCorpId(parameter.getLoginCorpId());
        personPk.setPersonId(parameter.getLoginPersonId());
        personEntity.setId(personPk);
        personEntity = find(mPersonServiceDaoImpl, personEntity);
        long userId = personEntity.getUserId();
        // 削除対象のt_plan_fulfillment_infoを取得
        TPlanFulfillmentInfo infoEntity = new TPlanFulfillmentInfo();
        TPlanFulfillmentInfoPK infoEntityPK = new TPlanFulfillmentInfoPK();
        infoEntityPK.setCorpId(parameter.getOperationCorpId());
        infoEntityPK.setPlanFulfillmentId(parameter.getPlanFulfillmentId());
        infoEntity.setId(infoEntityPK);
        TPlanFulfillmentInfo dbEntity = find(planFulfillmentWriteServiceDaoImpl, infoEntity);
        dbEntity.setUpdateUserId(userId);
        dbEntity.setDelFlg(1);

        merge(planFulfillmentWriteServiceDaoImpl, dbEntity);

        // 紐づくt_plan_fulfillment_resultを取得
        TPlanFulfillmentResult resultEntity = new TPlanFulfillmentResult();
        TPlanFulfillmentResultPK resultEntityPK = new TPlanFulfillmentResultPK();
        resultEntityPK.setCorpId(parameter.getOperationCorpId());
        resultEntityPK.setPlanFulfillmentId(parameter.getPlanFulfillmentId());
        resultEntity.setId(resultEntityPK);

        List<TPlanFulfillmentResult> tPlanFulfillmentResultList = getResultList(tPlanFulfillmentResultServiceDaoImpl,
                resultEntity);
        if (tPlanFulfillmentResultList != null) {
            for (TPlanFulfillmentResult entity : tPlanFulfillmentResultList) {
                entity.setUpdateUserId(userId);
                //                entity.setUpdateDate(timestamp);
                entity.setDelFlg(1);
                merge(tPlanFulfillmentResultServiceDaoImpl, entity);
            }
        }
        // 紐づくt_building_plan_fulfillmentを取得
        TBuildingPlanFulfillment tbpfEntity = new TBuildingPlanFulfillment();
        TBuildingPlanFulfillmentPK tbpfEntityPK = new TBuildingPlanFulfillmentPK();
        tbpfEntityPK.setCorpId(parameter.getOperationCorpId());
        tbpfEntityPK.setPlanFulfillmentId(parameter.getPlanFulfillmentId());
        tbpfEntity.setId(tbpfEntityPK);
        tbpfEntity.setDelFlg(0);

        List<TBuildingPlanFulfillment> tBuildingPlanFulfillmentResultList = getResultList(
                tBuildingPlanFulfillmentServiceDaoImpl,
                tbpfEntity);
        if (tBuildingPlanFulfillmentResultList != null) {
            for (TBuildingPlanFulfillment buildingInfo : tBuildingPlanFulfillmentResultList) {
                buildingInfo.setUpdateUserId(userId);
                //                buildingInfo.setUpdateDate(timestamp);
                buildingInfo.setDelFlg(1);
                merge(tBuildingPlanFulfillmentServiceDaoImpl, buildingInfo);
            }
        }

        return ret;
    }

    public void delete(String corpId, long planId, String personCorpId, String personId) throws Exception {
        // ユーザーIDを取得
        MPerson personEntity = new MPerson();
        MPersonPK personPk = new MPersonPK();
        personPk.setCorpId(personCorpId);
        personPk.setPersonId(personId);
        personEntity.setId(personPk);
        personEntity = find(mPersonServiceDaoImpl, personEntity);
        long userId = personEntity.getUserId();
        // 削除対象のt_plan_fulfillment_infoを取得
        TPlanFulfillmentInfo infoEntity = new TPlanFulfillmentInfo();
        TPlanFulfillmentInfoPK infoEntityPK = new TPlanFulfillmentInfoPK();
        infoEntityPK.setCorpId(corpId);
        infoEntityPK.setPlanFulfillmentId(planId);
        infoEntity.setId(infoEntityPK);
        TPlanFulfillmentInfo dbEntity = find(planFulfillmentWriteServiceDaoImpl, infoEntity);
        // TODO TimeStampコメント化
        //        Timestamp timestamp = getSvDate();
        dbEntity.setUpdateUserId(userId);
        //        dbEntity.setUpdateDate(timestamp);
        dbEntity.setDelFlg(1);

        merge(planFulfillmentWriteServiceDaoImpl, dbEntity);

        // 紐づくt_plan_fulfillment_resultを取得
        TPlanFulfillmentResult resultEntity = new TPlanFulfillmentResult();
        TPlanFulfillmentResultPK resultEntityPK = new TPlanFulfillmentResultPK();
        resultEntityPK.setCorpId(corpId);
        resultEntityPK.setPlanFulfillmentId(planId);
        resultEntity.setId(resultEntityPK);

        List<TPlanFulfillmentResult> tPlanFulfillmentResultList = getResultList(tPlanFulfillmentResultServiceDaoImpl,
                resultEntity);
        if (tPlanFulfillmentResultList != null) {
            for (TPlanFulfillmentResult entity : tPlanFulfillmentResultList) {
                entity.setUpdateUserId(userId);
                //                entity.setUpdateDate(timestamp);
                entity.setDelFlg(1);
                merge(tPlanFulfillmentResultServiceDaoImpl, entity);
            }
        }
        // 紐づくt_building_plan_fulfillmentを取得
        TBuildingPlanFulfillment tbpfEntity = new TBuildingPlanFulfillment();
        TBuildingPlanFulfillmentPK tbpfEntityPK = new TBuildingPlanFulfillmentPK();
        tbpfEntityPK.setCorpId(corpId);
        tbpfEntityPK.setPlanFulfillmentId(planId);
        tbpfEntity.setId(tbpfEntityPK);
        tbpfEntity.setDelFlg(0);

        List<TBuildingPlanFulfillment> tBuildingPlanFulfillmentResultList = getResultList(
                tBuildingPlanFulfillmentServiceDaoImpl,
                tbpfEntity);
        if (tBuildingPlanFulfillmentResultList != null) {
            for (TBuildingPlanFulfillment buildingInfo : tBuildingPlanFulfillmentResultList) {
                buildingInfo.setUpdateUserId(userId);
                //                buildingInfo.setUpdateDate(timestamp);
                buildingInfo.setDelFlg(1);
                merge(tBuildingPlanFulfillmentServiceDaoImpl, buildingInfo);
            }
        }
    }

}
