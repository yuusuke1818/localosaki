package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.UpdateSmsMeterGroupsRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupsResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK;

/**
 * メーターグループ管理 メーターグループ一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class UpdateSmsMeterGroupsDao extends OsolApiDao<UpdateSmsMeterGroupsParameter> {

    private final MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    public UpdateSmsMeterGroupsDao() {
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
    }

    @Override
    public ListSmsMeterGroupsResult query(UpdateSmsMeterGroupsParameter parameter) throws Exception {

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        Long meterGroupId = parameter.getUpdateMeterGroupsRequest().getMeterGroupId();
        String corpId = parameter.getUpdateMeterGroupsRequest().getCorpId();
        Long buildingId = parameter.getUpdateMeterGroupsRequest().getBuildingId();
        List<UpdateSmsMeterGroupsRequestSet> requestList = parameter.getUpdateMeterGroupsRequest().getMeterGroupList();
        List<GetSmsMeterGroupResultData> resultMeterGroupsList = new ArrayList<>();
        if (requestList != null && requestList.size() > 0) {
            for (UpdateSmsMeterGroupsRequestSet requestSet : requestList) {
                // クエリ生成時に必要なパラメータを設定
                MMeterGroup target = new MMeterGroup();
                MMeterGroupPK targetPk = new MMeterGroupPK();
                targetPk.setCorpId(corpId);
                targetPk.setMeterGroupId(meterGroupId);
                targetPk.setBuildingId(buildingId);
                targetPk.setMeterMngId(requestSet.getMeterMngId());
                targetPk.setDevId(requestSet.getDevId());
                target.setId(targetPk);

                MMeterGroup entity = find(mMeterGroupServiceDaoImpl, target);
                if (entity == null) {
                    //追加
                    target.setCalcType(BigDecimal.valueOf(requestSet.getCalcType().longValue()));
                    target.setVersion(1);
                    target.setCreateUserId(loginUserId);
                    target.setCreateDate(serverDateTime);
                    target.setUpdateUserId(loginUserId);
                    target.setUpdateDate(serverDateTime);

                    // 新規登録
                    persist(mMeterGroupServiceDaoImpl, target);

                    resultMeterGroupsList.add(setResultData(find(mMeterGroupServiceDaoImpl, target)));
                } else {
                    //更新
                    //排他チェック
                    if (entity.getVersion().equals(requestSet.getVersion())) {
                        entity.setCalcType(requestSet.getCalcType());
                        entity.setVersion(entity.getVersion() + 1);
                        entity.setUpdateUserId(loginUserId);
                        entity.setUpdateDate(serverDateTime);

                        merge(mMeterGroupServiceDaoImpl, entity);

                        resultMeterGroupsList.add(setResultData(find(mMeterGroupServiceDaoImpl, entity)));
                    } else {
                        throw new OptimisticLockException();
                    }

                }
            }
        }

        ListSmsMeterGroupsResult result = new ListSmsMeterGroupsResult();
        result.setMeterGroupList(resultMeterGroupsList);
        return result;
    }

    private GetSmsMeterGroupResultData setResultData (MMeterGroup entity) {
        GetSmsMeterGroupResultData resultData = new GetSmsMeterGroupResultData();
        resultData.setMeterGroupId(entity.getId().getMeterGroupId());
        resultData.setCorpId(entity.getId().getCorpId());
        resultData.setBuildingId(entity.getId().getBuildingId());
        resultData.setDevId(entity.getId().getDevId());
        resultData.setMeterMngId(entity.getId().getMeterMngId());
        resultData.setCalcType(entity.getCalcType());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
