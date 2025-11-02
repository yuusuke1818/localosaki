package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.AiSelectParameter;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200067Param;

/**
 * AI設定(取得) Dao クラス
 * @author nishida.t
 *
 */
@Stateless
public class AiSelectDao extends BaseSmControlDao {

    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;

    public AiSelectDao() {
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
    }

    /**
     * 機器テーブルから取得する
     * @param param
     * @param res
     * @throws Exception
     */
    public void updateSmPrm(AiSelectParameter param, FvpCtrlMngResponse<A200067Param> res) throws Exception {

        // DBサーバ時刻取得
        Timestamp updateDate = super.getServerDateTime();

      //ログインユーザーIDを取得
        Long loginUserId = getMPerson(param.getLoginCorpId(), param.getLoginPersonId()).getUserId();

        MSmPrm parameter = new MSmPrm();

        parameter.setSmId(res.getSmId());

        MSmPrm data = find(mSmPrmServiceDaoImpl, parameter);
        if(data == null) {
            return;
        }

        data.setAielMasterConnectFlg(Integer.parseInt(res.getParam().getAielMasterConnection()));
        data.setUpdateUserId(loginUserId);
        data.setUpdateDate(updateDate);
        merge(mSmPrmServiceDaoImpl, data);
    }

}
