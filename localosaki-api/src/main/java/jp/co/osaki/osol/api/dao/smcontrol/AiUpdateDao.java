package jp.co.osaki.osol.api.dao.smcontrol;

import java.sql.Timestamp;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.parameter.smcontrol.AiUpdateParameter;
import jp.co.osaki.osol.api.servicedao.entity.MSmPrmServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmPrm;
import jp.co.osaki.osol.mng.FvpCtrlMngResponse;
import jp.co.osaki.osol.mng.param.A200068Param;

/**
 * AI設定(設定) Dao クラス
 * @author nishida.t
 *
 */
@Stateless
public class AiUpdateDao extends BaseSmControlDao {

    private final MSmPrmServiceDaoImpl mSmPrmServiceDaoImpl;

    public AiUpdateDao() {
        mSmPrmServiceDaoImpl = new MSmPrmServiceDaoImpl();
    }


    /**
     * 機器テーブルを更新する
     * @param param
     * @param res
     * @throws Exception
     */
    public void updateSmPrm(AiUpdateParameter param, FvpCtrlMngResponse<A200068Param> res, A200068Param resParam) throws Exception {

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

        data.setAielMasterConnectFlg(Integer.parseInt(resParam.getAielMasterConnection()));
        data.setUpdateUserId(loginUserId);
        data.setUpdateDate(updateDate);
        merge(mSmPrmServiceDaoImpl, data);
    }
}
