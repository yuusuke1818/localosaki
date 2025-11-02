package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupsParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupsResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.GetSmsMeterGroupResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.ListSmsMeterGroupsServiceDaoImpl;

/**
 * メーターグループ管理 メーターグループ一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class ListSmsMeterGroupsDao extends OsolApiDao<ListSmsMeterGroupsParameter> {

    private ListSmsMeterGroupsServiceDaoImpl meterGroupServiceDaoImpl;

    public ListSmsMeterGroupsDao() {
        meterGroupServiceDaoImpl = new ListSmsMeterGroupsServiceDaoImpl();
    }

    @Override
    public ListSmsMeterGroupsResult query(ListSmsMeterGroupsParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        GetSmsMeterGroupResultData meterGroupResultData = new GetSmsMeterGroupResultData();
        meterGroupResultData.setCorpId(parameter.getCorpId());
        meterGroupResultData.setBuildingId(parameter.getBuildingId());
        meterGroupResultData.setMeterGroupId(parameter.getMeterGroupId());

        // ServiceDaoクラスにてクエリ実行
        List<GetSmsMeterGroupResultData> meterGroupList = getResultList(meterGroupServiceDaoImpl, meterGroupResultData);

        ListSmsMeterGroupsResult result = new ListSmsMeterGroupsResult();
        result.setMeterGroupList(meterGroupList);
        return result;
    }
}
