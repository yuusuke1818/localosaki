package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.GetSmsMeterGroupParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.GetSmsMeterGroupResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.MMeterGroupServiceDaoImpl;
import jp.co.osaki.osol.entity.MMeterGroup;
import jp.co.osaki.osol.entity.MMeterGroupPK;

/**
 * メーターグループ管理 メーターグループ取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class GetSmsMeterGroupDao extends OsolApiDao<GetSmsMeterGroupParameter> {

    private MMeterGroupServiceDaoImpl mMeterGroupServiceDaoImpl;

    public GetSmsMeterGroupDao() {
        mMeterGroupServiceDaoImpl = new MMeterGroupServiceDaoImpl();
    }

    @Override
    public GetSmsMeterGroupResult query(GetSmsMeterGroupParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        MMeterGroup mMeterGroup = new MMeterGroup();
        MMeterGroupPK mMeterGroupPK = new MMeterGroupPK();
        mMeterGroupPK.setCorpId(parameter.getCorpId());
        mMeterGroupPK.setBuildingId(parameter.getBuildingId());
        mMeterGroupPK.setMeterGroupId(parameter.getMeterGroupId());
        mMeterGroupPK.setMeterMngId(parameter.getMeterMngId());
        mMeterGroupPK.setDevId(parameter.getDevId());
        mMeterGroup.setId(mMeterGroupPK);
        // ServiceDaoクラスにてクエリ実行
        MMeterGroup meterGroup = find(mMeterGroupServiceDaoImpl, mMeterGroup);

        GetSmsMeterGroupResult result = new GetSmsMeterGroupResult();
        result.setMeterGroupResulData(setResultData(meterGroup));
        return result;
    }

    private GetSmsMeterGroupResultData setResultData (MMeterGroup entity) {
        GetSmsMeterGroupResultData resultData = new GetSmsMeterGroupResultData();
        resultData.setCorpId(entity.getId().getCorpId());
        resultData.setBuildingId(entity.getId().getBuildingId());
        resultData.setMeterGroupId(entity.getId().getMeterGroupId());
        resultData.setMeterMngId(entity.getId().getMeterMngId());
        resultData.setDevId(entity.getId().getDevId());
        resultData.setCalcType(entity.getCalcType());
        resultData.setVersion(entity.getVersion());

        return resultData;
    }
}
