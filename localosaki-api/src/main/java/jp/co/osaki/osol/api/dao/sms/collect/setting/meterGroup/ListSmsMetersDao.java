package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMetersParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMetersResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMetersResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.ListSmsMetersServiceDaoImpl;

/**
 * メーターグループ管理 メーター一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class ListSmsMetersDao extends OsolApiDao<ListSmsMetersParameter> {

    private ListSmsMetersServiceDaoImpl meterGroupServiceDaoImpl;

    public ListSmsMetersDao() {
        meterGroupServiceDaoImpl = new ListSmsMetersServiceDaoImpl();
    }

    @Override
    public ListSmsMetersResult query(ListSmsMetersParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsMetersResultData meterResultData = new ListSmsMetersResultData();
        meterResultData.setCorpId(parameter.getCorpId());
        meterResultData.setBuildingId(parameter.getBuildingId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsMetersResultData> meterGroupList = getResultList(meterGroupServiceDaoImpl, meterResultData);

        ListSmsMetersResult result = new ListSmsMetersResult();
        result.setMetersList(meterGroupList);
        return result;
    }
}
