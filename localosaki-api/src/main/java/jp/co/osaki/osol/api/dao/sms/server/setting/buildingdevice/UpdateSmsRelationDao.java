/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.sms.server.setting.buildingdevice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.server.setting.buildingdevice.UpdateSmsRelationParameter;
import jp.co.osaki.osol.api.request.sms.server.setting.buildingdevice.UpdateSmsRelationRequest;
import jp.co.osaki.osol.api.result.sms.server.setting.buildingdevice.UpdateSmsRelationResult;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.MDevRelationServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.MMeterServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.server.setting.buildingdevice.TBuildDevMeterRelationServiceDaoImpl;
import jp.co.osaki.osol.entity.MDevRelation;
import jp.co.osaki.osol.entity.MDevRelationPK;
import jp.co.osaki.osol.entity.MMeter;
import jp.co.osaki.osol.entity.MMeterPK;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.osol.entity.TBuildDevMeterRelation;
import jp.co.osaki.osol.entity.TBuildDevMeterRelationPK;
import jp.co.osaki.sms.SmsConstants;

/**
 * 各種リレーション更新 Daoクラス
 *
 * @author yoneda_y
 */
@Stateless
public class UpdateSmsRelationDao extends OsolApiDao<UpdateSmsRelationParameter> {

    private final MDevRelationServiceDaoImpl mDevRelationServiceDaoImpl;

    private final TBuildDevMeterRelationServiceDaoImpl tBuildDevMeterRelationServiceDaoImpl;

    private final MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    private final MMeterServiceDaoImpl mMeterServiceDaoImpl;


    public UpdateSmsRelationDao() {
        mDevRelationServiceDaoImpl = new MDevRelationServiceDaoImpl();
        tBuildDevMeterRelationServiceDaoImpl = new TBuildDevMeterRelationServiceDaoImpl();
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
        mMeterServiceDaoImpl = new MMeterServiceDaoImpl();
    }

    @Override
    public UpdateSmsRelationResult query(UpdateSmsRelationParameter parameter) throws Exception {

        UpdateSmsRelationResult result = new UpdateSmsRelationResult();

        if (Objects.isNull(parameter.getRequest())) {
            return new UpdateSmsRelationResult();
        }

        //JSON⇒Resultに変換
        UpdateSmsRelationRequest request = parameter.getRequest();

        //ユーザー識別IDを取得
        MPerson mPerson = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId());
        Long userId = mPerson.getUserId();

        //DBサーバ時刻取得
        Timestamp serverDateTime = getServerDateTime();

        String corpId = request.getCorpId();
        Long buildingId = request.getBuildingId();

        if (Objects.nonNull(request.getAddDevIdList())) {
            for (Object devId : request.getAddDevIdList()) {
                // 建物、装置関連テーブル 追加
                MDevRelation mDevRelation = new MDevRelation();
                MDevRelationPK mDevRelationPK = new MDevRelationPK();
                mDevRelationPK.setCorpId(corpId);
                mDevRelationPK.setBuildingId(buildingId);
                mDevRelationPK.setDevId(devId.toString());
                mDevRelation.setId(mDevRelationPK);
                mDevRelation.setVersion(0);
                mDevRelation.setCreateUserId(userId);
                mDevRelation.setCreateDate(serverDateTime);
                mDevRelation.setUpdateUserId(userId);
                mDevRelation.setUpdateDate(serverDateTime);
                persist(mDevRelationServiceDaoImpl, mDevRelation);

                // メーターテーブル（装置-メーター関係） 取得
                MMeter mMeter = new MMeter();
                MMeterPK mMeterPK = new MMeterPK();
                mMeterPK.setDevId(devId.toString());
                mMeter.setId(mMeterPK);
                List<MMeter> mMeterList = getResultList(mMeterServiceDaoImpl, mMeter);

                if (Objects.nonNull(mMeterList)) {
                    for (MMeter mMeterItem : mMeterList) {
                        // 建物、メーター関連テーブル 追加
                        TBuildDevMeterRelation tBuildDevMeterRelation = new TBuildDevMeterRelation();
                        TBuildDevMeterRelationPK tBuildDevMeterRelationPK = new TBuildDevMeterRelationPK();
                        tBuildDevMeterRelationPK.setCorpId(corpId);
                        tBuildDevMeterRelationPK.setBuildingId(buildingId);
                        tBuildDevMeterRelationPK.setDevId(devId.toString());
                        tBuildDevMeterRelationPK.setMeterMngId(mMeterItem.getId().getMeterMngId());
                        tBuildDevMeterRelation.setId(tBuildDevMeterRelationPK);
                        tBuildDevMeterRelation.setVersion(0);
                        tBuildDevMeterRelation.setCreateUserId(userId);
                        tBuildDevMeterRelation.setCreateDate(serverDateTime);
                        tBuildDevMeterRelation.setUpdateUserId(userId);
                        tBuildDevMeterRelation.setUpdateDate(serverDateTime);
                        persist(tBuildDevMeterRelationServiceDaoImpl, tBuildDevMeterRelation);
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(request.getDeleteDevIdList())) {
            HashMap<String, List<Object>> param = new HashMap<String, List<Object>>();
            List<Object> corpIdList = new ArrayList<Object>();
            corpIdList.add(corpId);
            List<Object> buildingIdList = new ArrayList<Object>();
            buildingIdList.add(buildingId);

            param.put(SmsConstants.RERATION_KEY_NAME.CORP_ID.getVal(), corpIdList);
            param.put(SmsConstants.RERATION_KEY_NAME.BUILDING_ID.getVal(), buildingIdList);
            param.put(SmsConstants.RERATION_KEY_NAME.DEV_ID.getVal(), request.getDeleteDevIdList());
            List<Object> userIdList = new ArrayList<>();
            userIdList.add(userId);
            param.put("updateUserId", userIdList);
            List<Object> serverDateTimeList = new ArrayList<>();
            serverDateTimeList.add(serverDateTime);
            param.put("updateDate", serverDateTimeList);

            // 建物、装置関連テーブル 削除
            executeUpdate(mDevRelationServiceDaoImpl, param);
            // 建物、メーター関連テーブル 削除
            executeUpdate(tBuildDevMeterRelationServiceDaoImpl, param);
            // メーターグループ設定 削除
            executeUpdate(mMeterGroupServiceDaoImpl, param);
            // メーター登録用 更新 　メーター削除要求をセットしないようにコメントアウト
//            executeUpdate(mMeterServiceDaoImpl, param);
        }

        return result;
    }
}
