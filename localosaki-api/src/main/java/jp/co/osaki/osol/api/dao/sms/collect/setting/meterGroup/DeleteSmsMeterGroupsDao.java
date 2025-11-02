package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.request.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsRequestSet;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.DeleteSmsMeterGroupsResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK;

/**
 * メーターグループ管理 メーターグループ削除 Daoクラス
 * @author maruta.y
 */
@Stateless
public class DeleteSmsMeterGroupsDao extends OsolApiDao<DeleteSmsMeterGroupsParameter> {

    private final MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    public DeleteSmsMeterGroupsDao() {
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
    }

    @Override
    public DeleteSmsMeterGroupsResult query(DeleteSmsMeterGroupsParameter parameter) throws Exception {
        Long meterGroupId = parameter.getDeleteMeterGroupsRequest().getMeterGroupId();
        String corpId = parameter.getDeleteMeterGroupsRequest().getCorpId();
        Long buildingId = parameter.getDeleteMeterGroupsRequest().getBuildingId();
        List<DeleteSmsMeterGroupsRequestSet> requestList = parameter.getDeleteMeterGroupsRequest().getMeterGroupList();
        if (requestList != null && requestList.size() > 0) {
            for (DeleteSmsMeterGroupsRequestSet requestSet : requestList) {
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

                //削除
                //排他チェック
                if (entity != null && entity.getVersion().equals(requestSet.getVersion())) {
                    //排他OKで削除
                    remove(mMeterGroupServiceDaoImpl, entity);
                } else {
                    throw new OptimisticLockException();
                }
            }
        }

        DeleteSmsMeterGroupsResult result = new DeleteSmsMeterGroupsResult();
        return result;
    }
}
