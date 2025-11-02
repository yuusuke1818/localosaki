package jp.co.osaki.osol.api.dao.sms.collect.setting.concentrator;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.collect.setting.concentrator.ListSmsConcentratorsParameter;
import jp.co.osaki.osol.api.result.sms.collect.setting.concentrator.ListSmsConcentratorsResult;
import jp.co.osaki.osol.api.resultdata.sms.collect.setting.concentrator.ListSmsConcentratorsResultData;
import jp.co.osaki.osol.api.servicedao.sms.collect.setting.concentrator.ListSmsConcentratorsServiceDaoImpl;

/**
 * コンセントレータ管理 コンセントレータ一覧取得 Daoクラス
 * @author maruta.y
 */
@Stateless
public class ListSmsConcentratorsDao extends OsolApiDao<ListSmsConcentratorsParameter> {

    private final ListSmsConcentratorsServiceDaoImpl concentratorDaoImpl;

    public ListSmsConcentratorsDao() {
        concentratorDaoImpl = new ListSmsConcentratorsServiceDaoImpl();
    }

    @Override
    public ListSmsConcentratorsResult query(ListSmsConcentratorsParameter parameter) throws Exception {

        // クエリ生成時に必要なパラメータを設定
        ListSmsConcentratorsResultData concentratorResultData = new ListSmsConcentratorsResultData();
        concentratorResultData.setDevId(parameter.getDevId());
        concentratorResultData.setConcentId(parameter.getConcentId());

        // ServiceDaoクラスにてクエリ実行
        List<ListSmsConcentratorsResultData> resultConcentratorsList = getResultList(concentratorDaoImpl, concentratorResultData);


        ListSmsConcentratorsResult result = new ListSmsConcentratorsResult();
        result.setConcentratorList(resultConcentratorsList);
        return result;
    }
}
