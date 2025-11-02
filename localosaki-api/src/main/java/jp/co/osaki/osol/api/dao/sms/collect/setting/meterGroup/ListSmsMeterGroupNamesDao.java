package jp.co.osaki.osol.api.dao.sms.collect.setting.meterGroup;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.meterGroup.ListSmsMeterGroupNamesServiceDaoImpl;

/**
 * メーターグループ管理 メーターグループ名一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class ListSmsMeterGroupNamesDao extends OsolApiDao<ListSmsMeterGroupNamesParameter> {

    private ListSmsMeterGroupNamesServiceDaoImpl listSmsMeterGroupNameServiceDao;

    public ListSmsMeterGroupNamesDao() {
        listSmsMeterGroupNameServiceDao = new ListSmsMeterGroupNamesServiceDaoImpl();
    }

    @Override
    public ListSmsMeterGroupNamesResult query(ListSmsMeterGroupNamesParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsMeterGroupNamesResultData meterGroupNameResultData = new ListSmsMeterGroupNamesResultData();
        meterGroupNameResultData.setCorpId(parameter.getCorpId());
        meterGroupNameResultData.setBuildingId(parameter.getBuildingId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsMeterGroupNamesResultData> meterGroupNameList = getResultList(listSmsMeterGroupNameServiceDao, meterGroupNameResultData);


        ListSmsMeterGroupNamesResult result = new ListSmsMeterGroupNamesResult();
        result.setMeterGroupNameList(meterGroupNameList);
        return result;
    }
}
