package jp.co.osaki.osol.api.dao.sms.meterreading;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.sms.meterreading.GetSmsVariousParameter;
import jp.co.osaki.osol.api.result.sms.meterreading.GetSmsVariousResult;
import jp.co.osaki.osol.api.resultdata.sms.meterreading.VariousResultData;
import jp.co.osaki.osol.api.servicedao.sms.meterreading.VariousServiceDaoImpl;
import jp.co.osaki.osol.entity.MVarious;
import jp.co.osaki.osol.entity.MVariousPK;

/**
 * 各種設定情報検索 Daoクラス
 *
 * @author kobayashi.sho
 */
@Stateless
public class GetSmsVariousDao extends OsolApiDao<GetSmsVariousParameter> {

    private final VariousServiceDaoImpl variousServiceDaoImpl;

    public GetSmsVariousDao() {
        variousServiceDaoImpl = new VariousServiceDaoImpl();
    }

    @Override
    public GetSmsVariousResult query(GetSmsVariousParameter parameter) throws Exception {

        // 各種設定検索条件設定
        MVariousPK targetPk = new MVariousPK();
        targetPk.setCorpId(parameter.getCorpId()); // 企業ID
        targetPk.setBuildingId(parameter.getBuildingId()); // 建物ID
        MVarious target = new MVarious();
        target.setId(targetPk); // 更新条件

        // 各種設定検索を行う
        MVarious result = find(variousServiceDaoImpl, target);
        if (result == null) {
            return null;
        }

        return new GetSmsVariousResult(new VariousResultData(result));
    }

}
