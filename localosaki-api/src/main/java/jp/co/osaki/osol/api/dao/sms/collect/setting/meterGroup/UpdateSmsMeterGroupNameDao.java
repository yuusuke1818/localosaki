package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.UpdateSmsMeterGroupNameResult;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.MMeterGroupNameServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterGroupName;
import jp.co.osaki.osol.entity.MMeterGroupNamePK;

/**
 * メーターグループ管理 メーターグループ名更新 Daoクラス
 * @author maruta.y
 */
@Stateless
public class UpdateSmsMeterGroupNameDao extends OsolApiDao<UpdateSmsMeterGroupNameParameter> {

    private final MMeterGroupNameServiceDaoImpl mMeterGroupNameServiceDaoImpl;

    public UpdateSmsMeterGroupNameDao() {
        mMeterGroupNameServiceDaoImpl = new MMeterGroupNameServiceDaoImpl();
    }

    @Override
    public UpdateSmsMeterGroupNameResult query(UpdateSmsMeterGroupNameParameter parameter) throws Exception {

        Timestamp serverDateTime = getServerDateTime(); // DBサーバ時刻取得

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        MMeterGroupName target = new MMeterGroupName();
        MMeterGroupNamePK targetPk = new MMeterGroupNamePK();
        targetPk.setCorpId(parameter.getCorpId());
        targetPk.setBuildingId(parameter.getBuildingId());
        targetPk.setMeterGroupId(parameter.getMeterGroupId());
        target.setId(targetPk);

        MMeterGroupName entity = find(mMeterGroupNameServiceDaoImpl, target);
        if (entity != null) {
            //更新
            //排他チェック
            if (entity.getVersion().equals(parameter.getVersion())) {
                entity.setMeterGroupName(parameter.getMeterGroupName());
                entity.setUpdateUserId(loginUserId);
                entity.setUpdateDate(serverDateTime);

                merge(mMeterGroupNameServiceDaoImpl, entity);

            } else {
                throw new OptimisticLockException();
            }
        }
        UpdateSmsMeterGroupNameResult result = new UpdateSmsMeterGroupNameResult();
        return result;
    }

}
