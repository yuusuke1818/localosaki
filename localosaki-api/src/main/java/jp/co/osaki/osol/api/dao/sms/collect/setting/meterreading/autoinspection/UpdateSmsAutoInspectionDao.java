package jp.co.osaki.osol.api.dao.sms.collect.setting.meterreading.autoinspection;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import org.apache.commons.collections4.MapUtils;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionRequest;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterreading.autoinspection.UpdateSmsAutoInspectionResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionDevListResultData;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection.ListSmsAutoInspectionDevListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterreading.autoinspection.MAutoInspServiceDaoImpl;
import jp.co.osaki.osol.entity.MAutoInsp;
import jp.co.osaki.osol.entity.MAutoInspPK;

/**
 * データ収集装置 機器管理 検針設定 自動検針画面 データ更新API Daoクラス.
 *
 * @author ozaki.y
 */
@Stateless
public class UpdateSmsAutoInspectionDao extends OsolApiDao<UpdateSmsAutoInspectionParameter> {

    private final ListSmsAutoInspectionDevListServiceDaoImpl devListDaoImpl;
    private final MAutoInspServiceDaoImpl autoInspDaoImpl;

    public UpdateSmsAutoInspectionDao() {
        devListDaoImpl = new ListSmsAutoInspectionDevListServiceDaoImpl();
        autoInspDaoImpl = new MAutoInspServiceDaoImpl();
    }

    @Override
    public UpdateSmsAutoInspectionResult query(UpdateSmsAutoInspectionParameter parameter) throws Exception {
        String loginPersonId = parameter.getLoginPersonId();
        UpdateSmsAutoInspectionRequest request = parameter.getRequest();

        // 装置List
        ListSmsAutoInspectionDevListResultData devListParam = new ListSmsAutoInspectionDevListResultData();
        devListParam.setCorpId(parameter.getCorpId());
        devListParam.setBuildingId(parameter.getBuildingId());
        devListParam.setTenant(parameter.isTenant());

        List<ListSmsAutoInspectionDevListResultData> devList = getResultList(devListDaoImpl, devListParam);

        executeUpdate(request.getUpdateDataList(), devList, loginPersonId,
                getMPerson(parameter.getLoginCorpId(), loginPersonId).getUserId(), getServerDateTime(),
                request.getBeforeAutoInspInfoMap());

        return new UpdateSmsAutoInspectionResult();
    }

    /**
     * 更新実行.
     *
     * @param updateDataList 更新データList
     * @param devList 装置List
     * @param loginPersonId ログイン担当者ID
     * @param userId ユーザーID
     * @param currentDateTime 現在日時
     * @param beforeAutoInspInfoMap 編集前自動検針データMap(装置-メーター種別をキーとする自動検針データMap)
     */
    private void executeUpdate(List<UpdateSmsAutoInspectionRequestSet> updateDataList,
            List<ListSmsAutoInspectionDevListResultData> devList, String loginPersonId, Long userId,
            Timestamp currentDateTime, Map<String, Map<Long, ListSmsAutoInspectionResultData>> beforeAutoInspInfoMap) {

        Set<String> existDevIdSet = new HashSet<>();
        for (ListSmsAutoInspectionDevListResultData devData : devList) {
            String devId = devData.getDevId();
            executeUpdate(updateDataList, devId, loginPersonId, userId, currentDateTime,
                    beforeAutoInspInfoMap.get(devId));

            existDevIdSet.add(devId);
        }
    }

    /**
     * 更新実行.
     *
     * @param updateDataList 更新データList
     * @param devId 装置ID
     * @param loginPersonId ログイン担当者ID
     * @param userId ユーザーID
     * @param currentDateTime 現在日時
     * @param meterTypeKeyBeforeDataMap メーター種別をキーとする編集前自動検針データMap
     */
    private void executeUpdate(List<UpdateSmsAutoInspectionRequestSet> updateDataList,
            String devId, String loginPersonId, Long userId, Timestamp currentDateTime,
            Map<Long, ListSmsAutoInspectionResultData> meterTypeKeyBeforeDataMap) {

        for (UpdateSmsAutoInspectionRequestSet updateData : updateDataList) {
            Long meterType = updateData.getMeterType();

            ListSmsAutoInspectionResultData beforeData = null;
            if (MapUtils.isNotEmpty(meterTypeKeyBeforeDataMap)) {
                beforeData = meterTypeKeyBeforeDataMap.get(meterType);
            }

            boolean updateDataFlg = (beforeData != null);

            MAutoInspPK targetPk = new MAutoInspPK();
            targetPk.setDevId(devId);
            targetPk.setMeterType(meterType);

            MAutoInsp target = new MAutoInsp();
            target.setId(targetPk);
            target.setRecMan(loginPersonId);
            target.setRecDate(currentDateTime);
            target.setMonth(updateData.getInspectionMonth());
            target.setDay(updateData.getInspectionDay());
            target.setHour(updateData.getInspectionHour());
            target.setCreateUserId(userId);
            target.setCreateDate(currentDateTime);
            target.setUpdateUserId(userId);
            target.setUpdateDate(currentDateTime);

            MAutoInsp currentData = find(autoInspDaoImpl, target);
            if (currentData == null) {
                if (updateDataFlg) {
                    // 更新対象レコードが削除されている場合
                    throw new OptimisticLockException();
                }

                // 新規登録
                persist(autoInspDaoImpl, target);

            } else {
                Integer version = null;
                if (updateDataFlg) {
                    version = beforeData.getVersion();
                }

                if (version == null) {
                    version = currentData.getVersion();

                } else {
                    if (currentData.getVersion() != version) {
                        // ほかのユーザーによって更新されている場合
                        throw new OptimisticLockException();
                    }
                }

                currentData.setRecMan(target.getRecMan());
                currentData.setRecDate(target.getRecDate());
                currentData.setMonth(target.getMonth());
                currentData.setDay(target.getDay());
                currentData.setHour(target.getHour());
                currentData.setVersion(version + 1);
                currentData.setUpdateUserId(target.getUpdateUserId());
                currentData.setUpdateDate(target.getUpdateDate());

                // 更新
                merge(autoInspDaoImpl, currentData);
            }
        }
    }
}
